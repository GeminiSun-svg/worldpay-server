package com.worldpay.service;

import java.util.Map;


import com.alibaba.fastjson2.JSONObject;

public interface DecryptController {

	public Map<String, Object> encryption(Map<String, Object> requestBody);

	public JSONObject Decryption(Map<String, Object> requestBody);
	
}
