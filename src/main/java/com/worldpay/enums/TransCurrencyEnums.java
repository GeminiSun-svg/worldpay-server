package com.worldpay.enums;


public enum TransCurrencyEnums {
	USD(1, "USD"),
	USDT(2, "USDT");
	
	private Integer index;

	private String name;


	private TransCurrencyEnums(Integer index, String name) {
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
