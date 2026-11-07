package com.worldpay.api.response;
import com.worldpay.constants.Constants;
import com.worldpay.enums.PublicEnums;

import lombok.Data;

@Data
public class ApiResponseEntity {
	
	private Boolean success;
	
	private String result;
	
	private Integer code;
	
	private String message;
	
	private Object data;
	
	private String msg;
	
	public static ApiResponseEntity error() {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ONE_HUNDRED.getIndex());
		response.setMsg(Constants.API_RES_E);
		return response;
	}
	
	public static ApiResponseEntity success() {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ZERO.getIndex());
		response.setMsg(Constants.API_RES_W);
		return response;
	}
	
	public static ApiResponseEntity successMsg(String msg) {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(PublicEnums.ZERO.getIndex());
		response.setMsg(msg);
		return response;
	}
	
	public static ApiResponseEntity setResult(Integer code, String msg, Object data) {
		ApiResponseEntity response = new ApiResponseEntity();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}
	
}
