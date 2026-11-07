package com.worldpay.enums;

/**
 * @category 邀请人类型
 * @author Hlin
 *
 */
public enum IntiterUserTypeEnums {
	NOT_USER (0, "没有邀请人"),
	APP_USER (1, "C端用户"),
	CHANNEL_USER (2, "渠道商 ");
	
	private Integer index;

	private String name;


	private IntiterUserTypeEnums(Integer index, String name) {
	    this.index = index;
	    this.name = name;
	}


	public Integer getIndex() {
	    return index;
	}


	public void setIndex(Integer index) {
	   this.index = index;
	}


	public String getName() {
	     return name;
	}


	public void setName(String name) {
	    this.name = name;
	}

}
