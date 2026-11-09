package com.worldpay.entity.baidu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "KYC处理结果")
public class ProcessedData {
	@Schema(description = "格式化出生日期")
	private final String formattedBirthday;
    @Schema(description = "姓名信息")
    private final NameParts nameParts;
    @Schema(description = "地址信息")
    private final AddressInfo addressInfo;
    
    public ProcessedData(String formattedBirthday, NameParts nameParts, AddressInfo addressInfo) {
        this.formattedBirthday = formattedBirthday;
        this.nameParts = nameParts;
        this.addressInfo = addressInfo;
    }
}
