package com.worldpay.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

@Service
public class AppleVerifyServiceImpl {

	private final String PROD_URL = "https://buy.itunes.apple.com/verifyReceipt";
	private final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

	// 订阅才需要，共享密钥；一次性充值可以不传
//	private final String SHARED_SECRET = "你的App共享密钥";

	public JSONObject verifyReceipt(String receiptData) {
		JSONObject request = new JSONObject();
		request.put("receipt-data", receiptData);
//		request.put("password", SHARED_SECRET);

		JSONObject result = post(PROD_URL, request);

		// 21007 = 沙盒票据发到了正式环境
		if (result.getIntValue("status") == 21007) {
			result = post(SANDBOX_URL, request);
		}
		return result;
	}

	public JSONObject post(String url, JSONObject body) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(body.toJSONString(), headers);

		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

		return JSON.parseObject(response.getBody());
	}

}
