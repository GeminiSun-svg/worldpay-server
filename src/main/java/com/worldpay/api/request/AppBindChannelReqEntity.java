package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：app用户绑定渠道商查询当前用户是否已绑定
 *
 * @author GeminiSun
 * @date 2026/2/4 9:15
 */
@Data
@Schema(description = "渠道商绑定状态查询请求")
public class AppBindChannelReqEntity {

    @Schema(description = "渠道商邀请码")
	private String invitationCode;


}
