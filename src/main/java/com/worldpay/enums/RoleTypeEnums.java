package com.worldpay.enums;


public enum RoleTypeEnums {

    ADMIN(1, "超级管理员");
	
	
	
    private Integer index;

    private String name;


    private RoleTypeEnums(Integer index, String name) {
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
