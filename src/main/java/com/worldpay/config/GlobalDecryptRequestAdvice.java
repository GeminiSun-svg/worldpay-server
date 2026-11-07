package com.worldpay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局请求解密拦截器
 */
@RestControllerAdvice
public class GlobalDecryptRequestAdvice implements RequestBodyAdvice {

    private static final String AES_KEY = "Ej0c8VSW9l2sP7kM";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/card/api/webhook/reset-addres",
            "/card/api/webhook/agent-notify",
            "/card/api/wallet/topin-usdt-notify",
            "/card/api/file/upload-image",
            "/card/api/file/api-upload-image",
            "/card/api/sign/encryption",
            "/card/api/sign/decryption"
    );

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {

        String requestPath = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            requestPath = request.getServletPath();
        }

        String body = new BufferedReader(new InputStreamReader(inputMessage.getBody(), StandardCharsets.UTF_8))
                .lines()
                .reduce("", (acc, cur) -> acc + cur);

        if (body == null || body.isEmpty()) {
            return inputMessage;
        }

        // ✅ 白名单放行（也要重建流）
        for (String white : WHITE_LIST) {
            if (requestPath.startsWith(white)) {
                InputStream reInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return reInputStream;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return inputMessage.getHeaders();
                    }
                };
            }
        }

        try {
            Map<String, Object> bodyMap = objectMapper.readValue(body, Map.class);
            if (bodyMap.containsKey("data")) {
                String encryptedData = (String) bodyMap.get("data");
                String decryptedJson = decryptAES(encryptedData, AES_KEY);

                InputStream decryptedStream = new ByteArrayInputStream(decryptedJson.getBytes(StandardCharsets.UTF_8));
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() {
                        return decryptedStream;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return inputMessage.getHeaders();
                    }
                };
            }
        } catch (Exception e) {
            System.err.println("【GlobalDecryptRequestAdvice】解密或解析失败：" + e.getMessage());
            e.printStackTrace();
        }

        // 默认：返回重新包装的原始流
        InputStream reInputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return reInputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                                MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
                                  MethodParameter parameter, Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private String decryptAES(String base64Data, String key) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(base64Data);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
