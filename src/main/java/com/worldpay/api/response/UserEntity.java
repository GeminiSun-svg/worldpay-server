package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：权限用户查询返回
 *
 * @author GeminiSun
 * @date 2026/1/15 16:23
 */
@Data
@Schema(description = "权限用户查询响应")
public class UserEntity {

    @Schema(description = "用户ID")
	private String uid;

    @Schema(description = "角色标识")
	private String roleKey;
}
