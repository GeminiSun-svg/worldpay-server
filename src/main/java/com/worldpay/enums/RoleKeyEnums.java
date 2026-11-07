package com.worldpay.enums;

/**
 * 权限字符枚举
 */
public enum RoleKeyEnums {

    ADMIN(0, "admin"),
    OPERATIONS(1, "operations"),
    CHANNEL(2, "channel");


    private Integer index;

    private String name;


    private RoleKeyEnums(Integer index, String name) {
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
