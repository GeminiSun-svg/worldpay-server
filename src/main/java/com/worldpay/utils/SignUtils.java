package com.worldpay.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 类描述：生成签名工具类
 *
 * @author GeminiSun
 * @date 2026/3/25 14:45
 */
public class SignUtils {
    /**
     * 生成签名（两次 MD5）
     *
     * @param data 待签名参数（键值对）
     * @param key  商户密钥
     * @return 签名字符串（32位小写）
     */
    public static String makeSign(Map<String, String> data, String key) {
        // 1. 按字典序排序键
        List<String> keys = new ArrayList<>(data.keySet());
        // 默认升序
        Collections.sort(keys);

        // 2. 拼接 key=value& 形式，跳过空值和键名为 'key' 的参数
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            // 跳过键名为 'key' 的参数
            if ("key".equals(k)) {
                continue;
            }
            String v = data.get(k);
            // 跳过空值（null 或空字符串）
            if (v == null || v.isEmpty()) {
                continue;
            }
            sb.append(k).append("=").append(v).append("&");
        }

        // 3. 拼接密钥部分
        sb.append("key=").append(key);
        System.out.println(sb);

        // 4. 两次 MD5
        String firstMd5 = md5(sb.toString());
        String sign = md5(firstMd5);
        System.out.println("加密sign：" + sign);
        return sign;
    }

    /**
     * MD5 加密（返回 32 位小写十六进制）
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                String s = Integer.toHexString(0xff & b);
                if (s.length() == 1) {
                    hex.append('0');
                }
                hex.append(s);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

   public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", "test1@gmail.com");
        String secret = "306fb4887e733cba2f85d34d9a000d3c";
        String sign = SignUtils.makeSign(params, secret);
        System.out.println("签名结果: " + sign);
    }
}
