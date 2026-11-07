package com.worldpay.exceptionHandler;

import jakarta.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;

@RestControllerAdvice
public class CommonExceptionHandle extends BaseApiService {

    /**
     * @RequestBody + @Valid 的校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseBase handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError error = ex.getBindingResult().getFieldError();
        String msg = error != null ? error.getDefaultMessage() : "参数错误";
        return setResult(400, msg, null);
    }

    /**
     * 普通参数校验异常（如 @RequestParam + @NotBlank）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseBase handleConstraintViolationException(ConstraintViolationException ex) {
        // 只返回你定义的 message
        String msg = ex.getConstraintViolations().iterator().next().getMessage();
        return setResult(400, msg, null);
    }

    /**
     * 表单参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseBase handleBindException(BindException ex) {
        FieldError error = ex.getFieldError();
        String msg = error != null ? error.getDefaultMessage() : "参数绑定错误";
        return setResult(400, msg, null);
    }

    /**
     * 全局异常兜底
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBase handleException(Exception ex) {
        return setResultError("系统异常，请稍后再试");
    }
}
