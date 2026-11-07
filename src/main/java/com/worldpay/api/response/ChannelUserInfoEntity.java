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
public class ChannelUserInfoEntity {

    @Schema(description = "")
private String userId;

    @Schema(description = "")
private String channelName;
}
