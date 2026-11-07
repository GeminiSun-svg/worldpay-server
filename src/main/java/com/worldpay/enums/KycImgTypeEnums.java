package com.worldpay.enums;

/**
 * @category 银行卡类型
 * @author Hlin
 *
 */
public enum KycImgTypeEnums {
	FRONT_PHOTO(1, "正面"),//正面
	BACK_PHOTO(2, "反面"),//反面
	HANDHELD_PHOTO(3, "手持");//手持
	
	private Integer index;

	private String name;


	private KycImgTypeEnums(Integer index, String name) {
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
		KycImgTypeEnums[] typeEnums = values();
		for (KycImgTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}

}
