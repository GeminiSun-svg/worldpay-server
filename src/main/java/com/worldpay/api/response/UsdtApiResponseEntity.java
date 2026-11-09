package com.worldpay.api.response;

import lombok.Data;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "USDT接口响应")
public class UsdtApiResponseEntity {

    @Schema(description = "响应状态码")
    private Integer code;
    @Schema(description = "响应消息")
    private String msg;
    @Schema(description = "响应数据")
    private Map<String, Object> data;
}
