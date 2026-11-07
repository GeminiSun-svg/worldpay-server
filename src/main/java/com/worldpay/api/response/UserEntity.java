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
public class UserEntity {

    @Schema(description = "")
private String uid;

    @Schema(description = "")
private String roleKey;
}
