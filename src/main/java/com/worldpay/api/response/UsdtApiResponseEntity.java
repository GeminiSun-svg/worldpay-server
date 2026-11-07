package com.worldpay.api.response;

import lombok.Data;

import java.util.Map;

@Data
public class UsdtApiResponseEntity {
    
    private Integer code;
    private String msg;
    private Map<String, Object> data;
}
