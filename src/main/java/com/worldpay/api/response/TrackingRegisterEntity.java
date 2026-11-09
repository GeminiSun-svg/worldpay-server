package com.worldpay.api.response;

import java.util.List;


import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "物流单号注册响应")
public class TrackingRegisterEntity {

	@Schema(description = "响应状态码")
	private int code;
    @Schema(description = "响应数据")
    private DataField data;

    @Data
    public static class DataField {
        @Schema(description = "注册成功的运单列表")
        private List<AcceptedItem> accepted;
        @Schema(description = "注册失败的运单列表")
        private List<RejectedItem> rejected;
    }

    @Data
    public static class AcceptedItem {
        @Schema(description = "承运商编码")
        private int carrier;
        @Schema(description = "运单号")
        private String number;
        @Schema(description = "来源标识")
        private int origin;
    }

    @Data
    public static class RejectedItem {
        @Schema(description = "运单号")
        private String number;
        @Schema(description = "错误详情")
        private ErrorDetail error;
    }

    @Data
    public static class ErrorDetail {
        @Schema(description = "错误码")
        private int code;
        @Schema(description = "错误信息")
        private String message;
    }

}
