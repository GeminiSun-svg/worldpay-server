package com.worldpay.enums;

/**
 * 类描述：预测回调枚举
 *
 * @author GeminiSun
 * @date 2026/3/25 14:57
 */
public enum ForecastEnums {

    TOP_UP(1, "充值"),
    WITHDRAW(2, "提现")
    ;

    private Integer index;

    private String name;


    private ForecastEnums(Integer index, String name) {
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
