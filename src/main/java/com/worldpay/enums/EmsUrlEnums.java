package com.worldpay.enums;

/**
 * @category 快递接口地址
 * @author Hlin
 *
 */
public enum EmsUrlEnums {
	REGISTER_NUMBER(0, "https://api.17track.net/track/v2.2/register"),
	ORDER_INFO(1, "https://api.17track.net/track/v2.2/gettrackinfo");

	private Integer index;

	private String name;

	private EmsUrlEnums(Integer index, String name) {
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
