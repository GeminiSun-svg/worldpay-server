package com.worldpay.entity.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysUserPowerEntity {
	
	@Schema(description = "权限标识")
	private String authority;

}
