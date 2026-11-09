package com.worldpay.entity.baidu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "地址信息")
public class AddressInfo {
	
	 @Schema(description = "省份")
	 private String province;//省
	 
     @Schema(description = "城市")
     private String city;//市
     
     @Schema(description = "邮编")
     private String zipCode;//邮编

 }

