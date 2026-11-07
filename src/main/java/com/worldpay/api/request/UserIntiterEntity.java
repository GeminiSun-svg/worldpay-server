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
public class UserIntiterEntity {

    @Schema(description = "")
private String coverInviterUser;

    @Schema(description = "")
private String inviterUser;
}
