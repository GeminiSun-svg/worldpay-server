package com.worldpay.entity.baidu;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "姓名信息")
public class NameParts {
	
	@Schema(description = "姓氏")
	private String lastName;  // 姓
    @Schema(description = "名字")
    private String firstName; // 名
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
