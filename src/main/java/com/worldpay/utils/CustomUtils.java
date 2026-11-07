package com.worldpay.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CustomUtils {

    public static void sendJsonMessage(HttpServletResponse response, Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setContentType("application/json; charset=utf-8");

            // 直接返回明文 JSON
            String jsonMessage = objectMapper.writeValueAsString(obj);

            PrintWriter writer = response.getWriter();
            writer.print(jsonMessage);
            writer.close();

            // 内容写到客户端浏览器
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // AES 解密方法
    public static String decryptAES(String encryptedData, String key) throws Exception {
        // Base64 解码
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

        // 解密
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // 解密后的字节数组
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        // 转换为字符串并返回
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}