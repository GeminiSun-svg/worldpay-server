package com.worldpay.utils;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.worldpay.constants.BaiduMethods;
import jakarta.annotation.PostConstruct;

/**
 * 百度API认证工具类
 * 用于获取和管理百度API的access_token
 * 使用静态方法调用
 */
@Component
public class BaiduAuthApiUtil {
	
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    
    private static String BASE_URL;
    
    private static String API_KEY;
    
    private static String SECRET_KEY;
    
    private static String accessToken;
    private static long expireTime; // token过期时间戳（毫秒）
    
    @Value("${baidu.baseUrl:}")
    public void setBaseUrl(String baseUrl) {
        BaiduAuthApiUtil.BASE_URL = baseUrl;
    }

    @Value("${baidu.apiKey:}")
    public void setApiKey(String apiKey) {
        BaiduAuthApiUtil.API_KEY = apiKey;
    }

    @Value("${baidu.secretKey:}")
    public void setSecretKey(String secretKey) {
        BaiduAuthApiUtil.SECRET_KEY = secretKey;
    }
    
    /**
     * 获取API Key
     */
    public static String getApiKey() {
        return API_KEY;
    }
    
    /**
     * 获取Secret Key
     */
    public static String getSecretKey() {
        return SECRET_KEY;
    }
    
    /**
     * 获取Base URL
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }
    
    @PostConstruct
    public void init() {
        // 初始化时获取一次token（配置为空时跳过）
        if (BASE_URL == null || BASE_URL.isEmpty()
                || API_KEY == null || API_KEY.isEmpty()
                || SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            return;
        }
        try {
            refreshAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取access_token（自动缓存和刷新）
     * @return access_token字符串
     * @throws IOException 网络请求异常
     */
    public static String getAccessToken() throws IOException {
        // 检查token是否有效
        if (isTokenValid()) {
            return accessToken;
        }
        
        // 重新获取token
        refreshAccessToken();
        return accessToken;
    }
    
    /**
     * 刷新access_token
     * @throws IOException 网络请求异常
     */
    @SuppressWarnings("deprecation")
	public static void refreshAccessToken() throws IOException {
        if (API_KEY == null || SECRET_KEY == null || BASE_URL == null) {
            throw new IOException("百度API配置未初始化，请检查配置文件");
        }
        
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        
        String url = String.format("%s%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                BASE_URL, BaiduMethods.OAUTH_TOKEN, API_KEY, SECRET_KEY);
        
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            
            String responseBody = response.body().string();
            
            // 手动解析JSON字符串
            String accessTokenKey = "\"access_token\":\"";
            int tokenStartIndex = responseBody.indexOf(accessTokenKey);
            
            if (tokenStartIndex == -1) {
                // 尝试查找带空格的格式
                accessTokenKey = "\"access_token\": \"";
                tokenStartIndex = responseBody.indexOf(accessTokenKey);
            }
            
            if (tokenStartIndex != -1) {
                tokenStartIndex += accessTokenKey.length();
                int tokenEndIndex = responseBody.indexOf("\"", tokenStartIndex);
                if (tokenEndIndex != -1) {
                    accessToken = responseBody.substring(tokenStartIndex, tokenEndIndex);
                } else {
                    throw new IOException("Failed to parse access_token from response: " + responseBody);
                }
            } else {
                // 检查是否有错误信息
                String errorKey = "\"error\":\"";
                int errorStartIndex = responseBody.indexOf(errorKey);
                if (errorStartIndex != -1) {
                    errorStartIndex += errorKey.length();
                    int errorEndIndex = responseBody.indexOf("\"", errorStartIndex);
                    String errorMsg = errorEndIndex != -1 ? 
                            responseBody.substring(errorStartIndex, errorEndIndex) : "Unknown error";
                    throw new IOException("API Error: " + errorMsg);
                }
                throw new IOException("Failed to parse response: " + responseBody);
            }
            
            // 解析expires_in
            String expiresKey = "\"expires_in\":";
            int expiresStartIndex = responseBody.indexOf(expiresKey);
            if (expiresStartIndex != -1) {
                expiresStartIndex += expiresKey.length();
                int expiresEndIndex = responseBody.indexOf(",", expiresStartIndex);
                if (expiresEndIndex == -1) {
                    expiresEndIndex = responseBody.indexOf("}", expiresStartIndex);
                }
                String expiresStr = responseBody.substring(expiresStartIndex, expiresEndIndex).trim();
                int expiresIn = Integer.parseInt(expiresStr);
                // 设置过期时间（提前5分钟刷新）
                expireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
            } else {
                // 默认1小时过期
                expireTime = System.currentTimeMillis() + 3300 * 1000L;
            }
        }
    }
    
    /**
     * 检查token是否有效
     * @return true=有效，false=无效或不存在
     */
    private static boolean isTokenValid() {
        return accessToken != null && !accessToken.isEmpty() 
                && System.currentTimeMillis() < expireTime;
    }
    
    /**
     * 强制清除缓存的token
     */
    public static void clearToken() {
        accessToken = null;
        expireTime = 0;
    }
    
    
    /**
     * 获取Token信息的原始响应（用于调试）
     */
    @SuppressWarnings("deprecation")
	public static String getTokenRawResponse() throws IOException {
        if (API_KEY == null || SECRET_KEY == null || BASE_URL == null) {
            throw new IOException("百度API配置未初始化，请检查配置文件");
        }
        
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        
        String url = String.format("%s%s?grant_type=client_credentials&client_id=%s&client_secret=%s",
                BASE_URL, BaiduMethods.OAUTH_TOKEN, API_KEY, SECRET_KEY);
        
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    
    
    /**
     * 身份证识别
     * @param filePath 本地文件路径
     * @param idCardSide 身份证正反面：front-正面，back-反面
     * @return 识别结果JSON字符串
     * @throws IOException IO异常
     */
    public static String idcard(String filePath, String idCardSide) throws IOException {
        // 请求url
        String url = BASE_URL+BaiduMethods.OCR_CARD;
        // 读取本地文件
        Path path = Paths.get(filePath);
        byte[] imgData = Files.readAllBytes(path);
        String imgStr = Base64.getEncoder().encodeToString(imgData);
        // URL编码
        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
        // 构建请求参数
        String param = "id_card_side=" + idCardSide + "&image=" + imgParam;
        // 获取access_token
        String accessToken = getAccessToken();
        // 发送请求
        Request request = new Request.Builder()
                .url(url + "?access_token=" + accessToken)
                .post(RequestBody.create(param, MediaType.parse("application/x-www-form-urlencoded")))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            String result = response.body().string();
            return result;
        }
    }
    
    
    
}