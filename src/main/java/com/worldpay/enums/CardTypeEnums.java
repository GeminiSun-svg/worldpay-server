package com.worldpay.enums;

/**
 * @category 银行卡类型
 * @author Hlin
 *
 */
public enum CardTypeEnums {
	VIRTUAL(1, "VIRTUAL"),//虚拟卡
	PHYSICAL(2, "PHYSICAL");//实体卡
	
	private Integer index;

	private String name;


	private CardTypeEnums(Integer index, String name) {
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
