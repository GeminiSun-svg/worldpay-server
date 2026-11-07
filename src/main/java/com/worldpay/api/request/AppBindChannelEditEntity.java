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
public class AppBindChannelEditEntity {

    @Schema(description = "")
private Integer appId;

    @Schema(description = "")
private String appUid;

    @Schema(description = "")
private String invitationCode;

    @Schema(description = "")
private Integer channelId;

    @Schema(description = "")
private String channelUid;

    @Schema(description = "")
private String paymentCode;

}
