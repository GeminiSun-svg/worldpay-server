package com.worldpay.enums;

public enum SexEnums {
	MALE(1, "男"),
	FEMALE(1, "女");

	    private Integer index;

	    private String name;


	    private SexEnums(Integer index, String name) {
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
