package com.worldpay.enums;

/**
 * @category 机器人角色
 * @author Hlin
 *
 */
public enum RobotRoleEnums {
	FINANCE(1, "finance"),//财务
	CUSTOMER_SERVICE(2, "customerService");//客服
	
	private Integer index;

	private String name;


	private RobotRoleEnums(Integer index, String name) {
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
