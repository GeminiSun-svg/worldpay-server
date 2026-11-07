package com.worldpay.entity.baidu;

import lombok.Data;

@Data
public class ProcessedData {
	private final String formattedBirthday;
    private final NameParts nameParts;
    private final AddressInfo addressInfo;
    
    public ProcessedData(String formattedBirthday, NameParts nameParts, AddressInfo addressInfo) {
        this.formattedBirthday = formattedBirthday;
        this.nameParts = nameParts;
        this.addressInfo = addressInfo;
    }
}
