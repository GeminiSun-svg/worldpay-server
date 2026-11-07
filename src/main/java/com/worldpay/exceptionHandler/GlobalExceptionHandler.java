 package com.worldpay.exceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
 import org.springframework.web.bind.annotation.ExceptionHandler;
 import org.springframework.web.bind.annotation.ResponseBody;

import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;


 //全局异常捕获
 @ControllerAdvice
 public class GlobalExceptionHandler extends BaseApiService{

	 @ExceptionHandler(Exception.class)//捕获运行时异常
 	@ResponseBody
 	@SysLogAnnotation(module = "全局异常捕获",type = "异常捕获",remark = "异常捕获")
 	public ResponseBase exceptionHandler(Exception e,HttpServletRequest request) {
 		return setResultError(Constants.EXCEPTION_MSG);
 	}
	
 }
