package com.worldpay.utils.api;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.api.request.RobotNotifyEntity;
import com.worldpay.api.response.UsdtApiResponseEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.UsdtTopinMethods;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;


public class WalletApi extends BaseApiService {
//    public static final String GATEWAY = "https://duihuan.tronapis.com";
//	public static final String GATEWAY = "https://jt.worldpay.club";
	public static final String GATEWAY = "https://mornitor.kyl-in.com";
    public static final String KEY_SIGN = "key=Q5vG7bN9mK1jF3dS5fA7";
    public static final int NOTIFY_TIMEOUT = 15000;

    public static final int NOTIFY_CONNECT_TIMEOUT = 1000;

    //获取钱包的充值地址
    @SuppressWarnings("unchecked")
	public static ResponseBase walletPost(String method, Object object) {
        String jsonDataString = JSON.toJSONString(object);
        Map<String, Object> params = JSON.parseObject(jsonDataString, Map.class);
        Map<String, Object> sortedParams = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.append(KEY_SIGN);
        String firstSign = MD5Util.digest(sb.toString());
        String finalSign = MD5Util.digest(firstSign);
        Map<String, Object> requestParams = new TreeMap<>(params);
        
        requestParams.put("sign", finalSign);
        String finalJsonData = JSON.toJSONString(requestParams);
        String url = GATEWAY + method;
        HttpRequest httpRequest = HttpRequest.post(url);
        httpRequest.timeout(NOTIFY_TIMEOUT)
                .body(finalJsonData)
                .charset(StandardCharsets.UTF_8)
                .setConnectionTimeout(NOTIFY_CONNECT_TIMEOUT);
        //这里获取响应数据
        String dataStr = httpRequest.execute().body();
        System.out.println(dataStr);
        UsdtApiResponseEntity responseEntity = JSONObject.parseObject(dataStr, UsdtApiResponseEntity.class);
        if( 1 == responseEntity.getCode() || 1078 == responseEntity.getCode() ) {
            Map<String,Object> resData = responseEntity.getData();
            return setResultSuccess(resData, responseEntity.getMsg());
        }else {
            return setResult(responseEntity.getCode(), responseEntity.getMsg(), null);
        }
    }
    
    
	@SuppressWarnings("unchecked")
	public static boolean Verification(Object object, String sing) {
		String jsonDataString = JSON.toJSONString(object);
		Map<String, Object> params = JSON.parseObject(jsonDataString, Map.class);
		Map<String, Object> sortedParams = new TreeMap<>(params);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.append(KEY_SIGN);
		String firstSign = MD5Util.digest(sb.toString());
		String finalSign = MD5Util.digest(firstSign);
		if (finalSign.equals(sing)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		RobotNotifyEntity entity = new RobotNotifyEntity();
		entity.setType(1);
		entity.setContent("<b>测试测试测试测试请速度前往后台进行审核<b>");
		ResponseBase base = WalletApi.walletPost(UsdtTopinMethods.MSG_NOTIFY, entity);
		System.out.println(base);
	}


}
