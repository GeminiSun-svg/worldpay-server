package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：app用户绑定渠道商
 *
 * @author GeminiSun
 * @date 2026/2/4 9:15
 */
@Data
@Schema(description = "APP用户绑定渠道商请求")
public class AppBindChannelEditEntity {

    @Schema(description = "APP用户ID")
	private Integer appId;

    @Schema(description = "APP用户UID")
	private String appUid;

    @Schema(description = "渠道商邀请码")
	private String invitationCode;

    @Schema(description = "渠道商用户ID")
	private Integer channelId;

    @Schema(description = "渠道商UID")
	private String channelUid;

    @Schema(description = "用户支付密码")
	private String paymentCode;

}
