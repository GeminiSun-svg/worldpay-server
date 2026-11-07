package com.worldpay.service;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson2.JSONObject;

public interface DecryptController {

	public Map<String, Object> encryption(@RequestBody Map<String, Object> requestBody);

	public JSONObject Decryption(@RequestBody Map<String, Object> requestBody);
	
}
