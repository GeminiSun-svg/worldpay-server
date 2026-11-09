package com.worldpay.api.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "渠道商用户新增请求")
public class ChannelUserAddEntity {

    @Schema(description = "渠道商用户姓名")
	private String username;

    @Schema(description = "渠道商手机号")
	private String tel;

    @Schema(description = "后台登录账号")
	private String acctive;

    @Schema(description = "后台登录密码")
	private String password;

    @Schema(description = "谷歌验证密钥")
	private String googleSecretkey;

    @Schema(description = "渠道佣金费率")
	private Double commissionRate;

    @Schema(description = "登录IP白名单")
	private String ipWhite;

    @Schema(description = "系统用户ID")
	private Integer userId;

    @Schema(description = "渠道等级编号")
	private Integer levelNo;

}
