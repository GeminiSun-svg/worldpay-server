package com.worldpay.enums;

/**
 * @category 充值方式
 * @author Hlin
 *
 */
public enum TradeTypeEnums {
	INCOME(1, "收入"),
	EXPENDITURE(2, "支出");
	
	private Integer index;

	private String name;


	private TradeTypeEnums(Integer index, String name) {
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
