package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：渠道商user_id和渠道商名称
 *
 * @author GeminiSun
 * @date 2026/1/16 11:15
 */
@Data
@Schema(description = "渠道商用户信息响应")
public class ChannelUserInfoEntity {

    @Schema(description = "渠道商用户ID")
	private String userId;

    @Schema(description = "渠道商名称")
	private String channelName;
}
