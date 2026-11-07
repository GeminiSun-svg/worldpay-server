package com.worldpay.enums;

import lombok.Getter;

/**
 * 类描述：登录身份
 *
 * @author GeminiSun
 * @date 2026/3/9 15:00
 */
@Getter
public enum UserIdentityEnums {

    APP(0,"APP"),
    SYSTEM(1,"SYSTEM");

    private Integer index;

    private String name;

    private UserIdentityEnums(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

}
