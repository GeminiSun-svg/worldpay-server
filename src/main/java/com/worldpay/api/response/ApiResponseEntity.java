package com.worldpay.api.response;
import com.worldpay.constants.Constants;
import com.worldpay.enums.PublicEnums;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "通用API响应")
public class ApiResponseEntity {
	
	@Schema(description = "请求是否成功")
	private Boolean success;
	
	@Schema(description = "响应结果")
	private String result;
	
	@Schema(description = "响应状态码")
	private Integer code;
	
	@Schema(description = "响应消息")
	private String message;
	
	@Schema(description = "响应数据")
	private Object data;
	
	@Schema(description = "响应提示信息")
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
