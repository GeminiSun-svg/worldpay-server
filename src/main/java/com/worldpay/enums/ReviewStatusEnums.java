package com.worldpay.enums;

/**
 * @category 银行卡类型
 * @author Hlin
 *
 */
public enum ReviewStatusEnums {
	PROCESSING(1, "注销处理中"),
	SUCCESS(2, "注销成功"),
	ERROR(3,"注销失败");
	
	private Integer index;

	private String name;


	private ReviewStatusEnums(Integer index, String name) {
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

	public static String getName(int i) {
		ReviewStatusEnums[] typeEnums = values();
		for (ReviewStatusEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}
	
}
