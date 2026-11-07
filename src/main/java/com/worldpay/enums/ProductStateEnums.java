package com.worldpay.enums;

/**
 * @category 产品状态
 * @author Hlin
 *
 */
public enum ProductStateEnums {
	SUPERIOR_ZH(1, "已上架","zh"), 
	DOWN_ZH(2, "已下架","zh"), 
	SUPERIOR_EN(1, "Available","en"), 
	DOWN_EN(2, "Removed","en"),
	SUPERIOR_Tw(1, "Available","en"), 
	DOWN_Tw(2, "Removed","en");

	private Integer index;

	private String name;
	
	private String lable;

	private ProductStateEnums(Integer index, String name, String lable) {
		this.index = index;
		this.name = name;
		this.lable = lable;
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

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static String getName(int i) {
		ProductStateEnums[] carTypeEnums = values();
		for (ProductStateEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}

	public static Integer getIndex(String index) {
		ProductStateEnums[] carTypeEnums = values();
		for (ProductStateEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getName().equals(index)) {
				return carTypeEnum.getIndex();
			}
		}
		return null;
	}
	
	
	public static String getLanguageName(int i,String language) {
		ProductStateEnums[] carTypeEnums = values();
		for (ProductStateEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i) && carTypeEnum.getLable().equals(language)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}

}
