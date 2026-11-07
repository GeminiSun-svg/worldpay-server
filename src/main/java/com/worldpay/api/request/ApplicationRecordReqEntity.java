package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：申请记录变更请求
 *
 * @author GeminiSun
 * @date 2026/2/4 10:20
 */
@Data
public class ApplicationRecordReqEntity {

    @Schema(description = "")
private Integer id;

    @Schema(description = "")
private String appId;

    @Schema(description = "")
private String appUid;

    @Schema(description = "")
private String channelId;

    @Schema(description = "")
private String channelUid;

    @Schema(description = "")
private Integer channelLevel;

    @Schema(description = "")
private Integer status;
}
