package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：归纳请求：用户没有填写渠道商邀请码情况下，把渠道商下的用户的卡或者账号归纳到相应的渠道商后台下并统计金额
 *
 * @author GeminiSun
 * @date 2026/1/15 11:46
 */
@Data
@Schema(description = "用户归纳渠道商请求")
public class UserIntiterEntity {

    @Schema(description = "被覆盖的推荐人ID")
	private String coverInviterUser;

    @Schema(description = "渠道推荐人ID")
	private String inviterUser;
}
