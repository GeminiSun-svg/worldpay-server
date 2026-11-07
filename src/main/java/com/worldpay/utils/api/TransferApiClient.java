package com.worldpay.utils.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.worldpay.api.response.ApiResponseEntity;
import com.worldpay.constants.Constants;
import com.worldpay.constants.TransferMethods;
import com.worldpay.utils.RedisUtil;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class TransferApiClient {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Value("${nationTransfer.apk_key:}")
	public void setApiKey(String apikey) {
		TransferApiClient.API_KEY = apikey;
	}
	
	@Value("${nationTransfer.api_secret:}")
	public void setApiSecret(String apiSecret) {
		TransferApiClient.API_SECRET = apiSecret;
	}
	
	@Value("${nationTransfer.api_url:}")
	public void setApiUrl(String apiUrl) {
		TransferApiClient.API_URL = apiUrl;
	}
	
	private static String API_KEY;
	
	private static String API_SECRET;
	
	private static String API_URL;

	public static String getApikey() {
		return API_KEY;
	}

	public static String getApiSecret() {
		return API_SECRET;
	}
	
	public static String getApiUrl() {
		return API_URL;
	}
	
	public String getToken() throws Exception {
		try {
			
			// 创建 OkHttpClient
			OkHttpClient client = new OkHttpClient()
					.newBuilder()
					.connectTimeout(30, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS)
					.writeTimeout(300, TimeUnit.SECONDS)
					.build();

			// 构建请求
			Request request = new Request
					.Builder()
					.url(API_URL+TransferMethods.GET_TOKEN)
					.addHeader("Content-Type", "application/json")
					.addHeader("X-API-KEY", API_KEY)
					.addHeader("X-API-SECRET",API_SECRET)
					.build();
			// 发送请求
			try (Response response = client.newCall(request).execute()) {
				String responseString = response.body().string();
				ApiResponseEntity responseEntity = JSONObject.parseObject(responseString, ApiResponseEntity.class);
				if(Constants.HTTP_RES_CODE_200.equals(responseEntity.getCode())) {
					JSONObject object = JSONObject.parseObject(responseEntity.getData().toString());
					String token = object.getString("accessToken");
					redisUtil.set(Constants.TRANSFER_TOKEN_BEARER, token,Constants.TRANSFER_EXPIRE_TIME);
					return token;
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	public String getData(String uri,String paramsJson) throws Exception {
		 try {
		        // 将字符串类型的 JSON 对象转为 JSONObject
		        JSONObject params = JSONObject.parseObject(paramsJson);
		        // 构建带查询参数的 URL
		        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL + uri).newBuilder();
		        // 遍历 JSON 对象，将键值对添加为查询参数
		        if (params != null) {
		            Iterator<String> keys = params.keySet().iterator();
		            while (keys.hasNext()) {
		                String key = keys.next();
		                String value = params.getString(key);
		                urlBuilder.addQueryParameter(key, value);
		            }
		        }
		        // 创建 OkHttpClient
		        OkHttpClient client = new OkHttpClient()
		                .newBuilder()
		                .connectTimeout(30, TimeUnit.SECONDS)
		                .readTimeout(30, TimeUnit.SECONDS)
		                .writeTimeout(300, TimeUnit.SECONDS)
		                .build();

		        // 构建请求
		        Request request = new Request
		                .Builder()
		                .url(urlBuilder.build())
		                .addHeader("Content-Type", "application/json")
		                .addHeader("authorization", Constants.AUTH_HEADER_START_WITH)
		                .build();

		        // 发送请求
		        try (Response response = client.newCall(request).execute()) {
		            String responseString = response.body().string();
		            ApiResponseEntity responseEntity = JSONObject.parseObject(responseString, ApiResponseEntity.class);
		            if (Constants.HTTP_RES_CODE_200.equals(responseEntity.getCode())) {
		                
		            }
		            return null;
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("Error during API request: " + e.getMessage(), e);
		    }
	}
	
	
	

//	public static String KuCoinPaySend(String params, Map<String, Object> signParams, String apiUrl) throws Exception {
//		try {
//			
//			// 创建 OkHttpClient
//			OkHttpClient client = new OkHttpClient()
//					.newBuilder()
//					.connectTimeout(30, TimeUnit.SECONDS)
//					.readTimeout(30, TimeUnit.SECONDS)
//					.writeTimeout(300, TimeUnit.SECONDS)
//					.build();
//
//			// 构建请求体
//			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//			RequestBody body = RequestBody.create(params, mediaType);
//			// 构建请求
//			Request request = new Request
//					.Builder()
//					.url(apiUrl)
//					.post(body)
//					.addHeader("Content-Type", "application/json")
//					.addHeader("PAY-API-KEY", API_KEY)
//					.addHeader("PAY-API-VERSION", "1.0")
//					.build();
//			// 发送请求
//			try (Response response = client.newCall(request).execute()) {
//				String responseString = response.body().string();
//				return responseString;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}

}
