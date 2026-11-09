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
@Schema(description = "申请记录变更请求")
public class ApplicationRecordReqEntity {

    @Schema(description = "申请记录主键ID")
	private Integer id;

    @Schema(description = "APP用户ID")
	private String appId;

    @Schema(description = "APP用户UID")
	private String appUid;

    @Schema(description = "渠道商用户ID")
	private String channelId;

    @Schema(description = "渠道商UID")
	private String channelUid;

    @Schema(description = "渠道等级编号")
	private Integer channelLevel;

    @Schema(description = "申请记录状态")
	private Integer status;
}
