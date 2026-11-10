package com.worldpay.utils.api;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Strings;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * worldPay 聚合三方 API 客户端（全量切换，不兼容旧 openapi）
 * 文档：https://jf86pwbggg.apifox.cn/
 */
@Slf4j
@Component
public class UCardApi extends BaseApiService {

    private static final String SIGN_ALGORITHM = "SHA256withRSA";
    private static final String KEY_ALGORITHM = "RSA";
    private static final int SUCCESS_CODE = 200;

    private static final Set<String> GET_METHODS = new HashSet<>(Arrays.asList(
            UCardMethods.MERCHANT_BANKCARD_TEMPLATE_LIST,
            UCardMethods.MERCHANT_ASSET,
            UCardMethods.USER_KYC_STATUS
    ));

    private static String GATEWAY;
    private static String APP_ID;
    private static String RSA_PRIVATE_KEY;
    private static String RSA_PUBLIC_KEY;
    private static int CONNECT_TIMEOUT_MS = 10000;
    private static int READ_TIMEOUT_MS = 60000;

    public static boolean useProxy = false;
    public static String proxyAddress = "127.0.0.1";
    public static int proxyPort = 7070;

    @Value("${bank.gatewayUrl:}")
    public void setGateway(String gateway) {
        if (gateway != null && gateway.endsWith("/")) {
            gateway = gateway.substring(0, gateway.length() - 1);
        }
        UCardApi.GATEWAY = gateway;
    }

    @Value("${bank.appId:}")
    public void setAppId(String appId) {
        UCardApi.APP_ID = appId;
    }

    @Value("${bank.rsaPrivateKey:}")
    public void setRsaPrivateKey(String rsaPrivateKey) {
        UCardApi.RSA_PRIVATE_KEY = cleanKey(rsaPrivateKey);
    }

    @Value("${bank.rsaPublicKey:}")
    public void setRsaPublicKey(String rsaPublicKey) {
        UCardApi.RSA_PUBLIC_KEY = cleanKey(rsaPublicKey);
    }

    @Value("${bank.connectTimeoutMs:10000}")
    public void setConnectTimeoutMs(int connectTimeoutMs) {
        UCardApi.CONNECT_TIMEOUT_MS = connectTimeoutMs;
    }

    @Value("${bank.readTimeoutMs:60000}")
    public void setReadTimeoutMs(int readTimeoutMs) {
        UCardApi.READ_TIMEOUT_MS = readTimeoutMs;
    }

    private static String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static String cleanKey(String keyStr) {
        if (keyStr == null) {
            return "";
        }
        return keyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }

    private static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private static String rsaSign(String data, String privateKeyStr) {
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("RSA签名失败", e);
        }
    }

    public static boolean rsaVerify(String data, String sign) {
        try {
            if (Strings.isNullOrEmpty(RSA_PUBLIC_KEY) || Strings.isNullOrEmpty(data) || Strings.isNullOrEmpty(sign)) {
                log.warn("RSA验签跳过/失败：publicKey空白={}, data空白={}, sign空白={}",
                        Strings.isNullOrEmpty(RSA_PUBLIC_KEY),
                        Strings.isNullOrEmpty(data),
                        Strings.isNullOrEmpty(sign));
                return false;
            }
            byte[] keyBytes = Base64.decodeBase64(RSA_PUBLIC_KEY);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            boolean ok = signature.verify(Base64.decodeBase64(sign));
            if (!ok) {
                log.warn("RSA验签未通过，签名原文: {}", data);
            }
            return ok;
        } catch (Exception e) {
            log.error("RSA验签异常", e);
            return false;
        }
    }

    /**
     * Webhook 验签：按文档规则构建原文后用公钥校验
     */
    public static boolean verifyWebhook(String appId, String nonce, String timestamp,
                                       Object body, String sign) {
        String bodyJson = body == null ? null : JSON.toJSONString(body);
        String signString = buildSignString(
                Strings.isNullOrEmpty(appId) ? APP_ID : appId,
                nonce,
                timestamp,
                bodyJson);
        return rsaVerify(signString, sign);
    }

    private static String getSortedJsonString(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            return "{}";
        }
        TreeMap<String, Object> sortedMap = new TreeMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof JSONObject) {
                sortedMap.put(entry.getKey(), getSortedJsonString((JSONObject) value));
            } else if (value instanceof JSONArray) {
                sortedMap.put(entry.getKey(), getSortedJsonArrayString((JSONArray) value));
            } else {
                sortedMap.put(entry.getKey(), value);
            }
        }
        return JSON.toJSONString(sortedMap);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String getSortedJsonArrayString(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return "[]";
        }
        List list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            if (item instanceof JSONObject) {
                list.add(getSortedJsonString((JSONObject) item));
            } else if (item instanceof JSONArray) {
                list.add(getSortedJsonArrayString((JSONArray) item));
            } else {
                list.add(item);
            }
        }
        list.sort((a, b) -> String.valueOf(a).compareTo(String.valueOf(b)));
        JSONArray sortedArray = new JSONArray();
        sortedArray.addAll(list);
        return sortedArray.toJSONString();
    }

    private static String buildSignString(String appId, String nonce, String timestamp, String bodyJson) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=").append(nullToEmpty(appId));
        sb.append("&nonce=").append(nullToEmpty(nonce));
        sb.append("&timestamp=").append(nullToEmpty(timestamp));

        if (!Strings.isNullOrEmpty(bodyJson) && !"{}".equals(bodyJson.trim())
                && !"null".equalsIgnoreCase(bodyJson.trim())) {
            try {
                JSONObject bodyObject = JSON.parseObject(bodyJson);
                if (bodyObject != null && !bodyObject.isEmpty()) {
                    TreeMap<String, Object> sortedParams = new TreeMap<>();
                    for (Map.Entry<String, Object> entry : bodyObject.entrySet()) {
                        Object value = entry.getValue();
                        if (value == null) {
                            continue;
                        }
                        if (value instanceof JSONObject) {
                            sortedParams.put(entry.getKey(), getSortedJsonString((JSONObject) value));
                        } else if (value instanceof JSONArray) {
                            sortedParams.put(entry.getKey(), getSortedJsonArrayString((JSONArray) value));
                        } else {
                            if (String.valueOf(value).trim().isEmpty()) {
                                continue;
                            }
                            sortedParams.put(entry.getKey(), value);
                        }
                    }
                    for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
                        sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                    }
                }
            } catch (Exception e) {
                log.error("解析Body参数失败", e);
            }
        }
        return sb.toString();
    }

    /**
     * 按新文档裁剪请求体，避免旧字段污染
     */
    private static JSONObject sanitizeBody(String method, Object body, String requestOrderId) {
        JSONObject src = body == null ? new JSONObject() : JSON.parseObject(JSON.toJSONString(body));
        JSONObject dest = new JSONObject();

        if (UCardMethods.USER_REGISTER.equals(method)) {
            putIfPresent(dest, "email", src.get("email"));
            Object tel = src.get("tel");
            if (tel == null) {
                tel = src.get("mobileNumber");
            }
            putIfPresent(dest, "tel", tel);
            return dest;
        }
        if (UCardMethods.APPLY_BANKCARD.equals(method)) {
            putIfPresent(dest, "productId", src.get("productId"));
            putIfPresent(dest, "deliveryAddressId", src.get("deliveryAddressId"));
            return dest;
        }
        if (UCardMethods.RECHARGE_BANKCARD.equals(method)) {
            putIfPresent(dest, "userBankcardId", src.get("userBankcardId"));
            putIfPresent(dest, "amount", src.get("amount"));
            Object orderId = src.get("requestOrderId");
            if (orderId == null || String.valueOf(orderId).trim().isEmpty()) {
                orderId = requestOrderId;
            }
            putIfPresent(dest, "requestOrderId", orderId);
            return dest;
        }
        if (UCardMethods.SET_BANKCARD_PIN.equals(method)) {
            putIfPresent(dest, "userBankcardId", src.get("userBankcardId"));
            putIfPresent(dest, "pin", src.get("pin"));
            return dest;
        }
        if (UCardMethods.QUERY_BANKCARD_BALANCE.equals(method)
                || UCardMethods.BANKCARD_INFO.equals(method)
                || UCardMethods.CLOSE_CARD.equals(method)) {
            putIfPresent(dest, "userBankcardId", src.get("userBankcardId"));
            return dest;
        }
        if (UCardMethods.UPDATE_CARD_STATUS.equals(method)) {
            putIfPresent(dest, "userBankcardId", src.get("userBankcardId"));
            putIfPresent(dest, "enable", src.get("enable"));
            return dest;
        }
        if (UCardMethods.BANKCARD_ACTIVE.equals(method)) {
            copyKeys(src, dest,
                    "productId", "cardNo", "verifyCode", "mobilePrefix", "mobile",
                    "countryCode", "address", "address2", "city", "state", "postCode");
            return dest;
        }
        if (UCardMethods.USER_KYC_APPLY.equals(method)) {
            copyKeys(src, dest,
                    "firstName", "lastName", "idNo", "email", "nationCode", "certType",
                    "idUrl", "idBackUrl", "birthday", "countryCode", "areaCode", "phone",
                    "fileType", "fileUrl", "faceUrl", "referenceId", "referenceType", "selfieUrl");
            return dest;
        }
        // 默认：去掉明显内部字段后透传
        src.remove("pageNumber");
        src.remove("pageSize");
        src.remove("language");
        return src;
    }

    private static void putIfPresent(JSONObject dest, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String && ((String) value).trim().isEmpty()) {
            return;
        }
        dest.put(key, value);
    }

    private static void copyKeys(JSONObject src, JSONObject dest, String... keys) {
        for (String key : keys) {
            putIfPresent(dest, key, src.get(key));
        }
    }

    private static ResponseBase parseResponse(String dataStr) {
        ApiResponseEntity responseEntity = JSONObject.parseObject(dataStr, ApiResponseEntity.class);
        if (responseEntity == null) {
            return setResult(Constants.HTTP_RES_CODE_500, "三方响应为空", null);
        }
        String message = !Strings.isNullOrEmpty(responseEntity.getMsg())
                ? responseEntity.getMsg()
                : responseEntity.getMessage();
        if (responseEntity.getCode() != null && SUCCESS_CODE == responseEntity.getCode()) {
            return setResultSuccess(responseEntity.getData(), message);
        }
        Integer code = responseEntity.getCode() == null ? Constants.HTTP_RES_CODE_500 : responseEntity.getCode();
        return setResult(code, message, null);
    }

    /**
     * 统一请求入口
     *
     * @param uid            用户 uid（放入 header）
     * @param method         接口 path
     * @param object         业务 body
     * @param requestOrderId 商户订单号（充值等接口写入 body.requestOrderId）
     */
    public static ResponseBase postData(String uid, String method, Object object, String requestOrderId) {
        try {
            boolean isGet = GET_METHODS.contains(method);
            JSONObject body = isGet ? null : sanitizeBody(method, object, requestOrderId);
            String url = GATEWAY + method;
            log.info("请求地址：{} method={}", url, isGet ? "GET" : "POST");

            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String bodyJson = body == null || body.isEmpty() ? null : body.toJSONString();
            String signStr = buildSignString(APP_ID, nonce, timestamp, isGet ? null : bodyJson);
            log.info("签名原文: {}", signStr);
            String sign = rsaSign(signStr, RSA_PRIVATE_KEY);

            HttpRequest httpRequest = isGet ? HttpRequest.get(url) : HttpRequest.post(url);
            httpRequest.header("appId", APP_ID)
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign);
            if (!Strings.isNullOrEmpty(uid)) {
                httpRequest.header("uid", uid);
            }
            if (useProxy) {
                httpRequest.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)));
            }
            httpRequest.timeout(READ_TIMEOUT_MS)
                    .setConnectionTimeout(CONNECT_TIMEOUT_MS)
                    .charset(StandardCharsets.UTF_8);
            if (!isGet) {
                httpRequest.header("Content-Type", "application/json");
                httpRequest.body(bodyJson == null ? "{}" : bodyJson);
            }

            String dataStr = httpRequest.execute().body();
            log.info("响应原始数据: {}", dataStr);
            return parseResponse(dataStr);
        } catch (Exception e) {
            log.error("请求异常", e);
            return setResult(Constants.HTTP_RES_CODE_500, "系统异常：" + e.getMessage(), null);
        }
    }

    public static ResponseBase postFormFile(String uid, String filedName, File file, String method) throws IOException {
        try {
            String url = GATEWAY + method;
            String nonce = generateNonce();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String signStr = buildSignString(APP_ID, nonce, timestamp, null);
            log.info("文件上传签名原文: {}", signStr);
            String sign = rsaSign(signStr, RSA_PRIVATE_KEY);

            HttpRequest httpRequest = HttpRequest.post(url)
                    .form(filedName, file)
                    .header("appId", APP_ID)
                    .header("nonce", nonce)
                    .header("timestamp", timestamp)
                    .header("sign", sign);
            if (!Strings.isNullOrEmpty(uid)) {
                httpRequest.header("uid", uid);
            }
            if (useProxy) {
                httpRequest.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)));
            }
            String dataStr = httpRequest.timeout(READ_TIMEOUT_MS)
                    .setConnectionTimeout(CONNECT_TIMEOUT_MS)
                    .charset(StandardCharsets.UTF_8)
                    .execute()
                    .body();
            log.info("文件上传响应: {}", dataStr);
            return parseResponse(dataStr);
        } catch (Exception e) {
            log.error("文件上传异常", e);
            return setResult(Constants.HTTP_RES_CODE_500, "文件上传异常：" + e.getMessage(), null);
        }
    }
}
