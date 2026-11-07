package com.worldpay.enums;


public enum LogisticsStateEnums {
	
	WAIT_SUCCESS(1, "待发货"),
	ALREADY_SHIPPING(2, "已发货"),
	INTRANSIT(3, "运输中"),
	OUTFORDELIVERY(4, "配送中"),
	DELIVERED(5, "完成");

	private Integer index;

	private String name;

	private LogisticsStateEnums(Integer index, String name) {
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
