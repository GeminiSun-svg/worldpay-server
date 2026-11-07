package com.worldpay.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.base.BaseApiService;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class DecryptControllerImpl extends BaseApiService implements com.worldpay.service.DecryptController{

	  private static final String AES_KEY = "Ej0c8VSW9l2sP7kM"; // 使用的 AES 密钥，和加密时一致

	    /**
	     * 加密方法
	     * @param requestBody 请求体，包含需要加密的数据
	     * @return 返回加密后的数据（Base64 编码）
	     */
	    @SuppressWarnings("unchecked")
		@Override
	    public Map<String, Object> encryption(Map<String, Object> requestBody) {
	        try {
	            // 获取请求体中的原始数据
	            String data = JSON.toJSONString(requestBody);

	            // 加密数据
	            String encryptedData = encryptAES(data, AES_KEY);
	            Map<String, Object> map = new HashedMap();
	            // 返回加密后的数据
	           map.put("data", encryptedData);
	           return map;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException();
	        }
	    }

	    /**
	     * 解密方法
	     * @param requestBody 请求体，包含加密的 Base64 字符串
	     * @return 返回解密后的原始数据
	     */
	    @Override
	    public JSONObject Decryption(Map<String, Object> requestBody) {
	        try {
	            // 获取请求体中的加密数据（Base64 编码的字符串）
	            String encryptedData = (String) requestBody.get("data");

	            // 解密数据
	            String decryptedData = decryptAES(encryptedData, AES_KEY);
	            JSONObject jsonObject = JSONObject.parseObject(decryptedData);
	            // 返回解密后的数据
	            return jsonObject;

	        } catch (Exception e) {
	            e.printStackTrace();
	           throw new RuntimeException();
	        }
	    }

	    /**
	     * AES 加密方法
	     * @param data 需要加密的原始数据
	     * @param key 密钥
	     * @return 返回加密后的 Base64 编码数据
	     * @throws Exception 如果加密失败
	     */
	    private String encryptAES(String data, String key) throws Exception {
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
	        return Base64.getEncoder().encodeToString(encrypted);
	    }

	    /**
	     * AES 解密方法
	     * @param encryptedData 加密后的 Base64 编码数据
	     * @param key 密钥
	     * @return 返回解密后的原始数据
	     * @throws Exception 如果解密失败
	     */
	    private String decryptAES(String encryptedData, String key) throws Exception {
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
