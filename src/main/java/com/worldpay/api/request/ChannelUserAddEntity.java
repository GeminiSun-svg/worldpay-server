package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ChannelUserAddEntity {

    @Schema(description = "")
private String username;

    @Schema(description = "")
private String tel;

    @Schema(description = "")
private String acctive;

    @Schema(description = "")
private String password;

    @Schema(description = "")
private String googleSecretkey;

    @Schema(description = "")
private Double commissionRate;

    @Schema(description = "")
private String ipWhite;

    @Schema(description = "")
private Integer userId;

    @Schema(description = "")
private Integer levelNo;

}
