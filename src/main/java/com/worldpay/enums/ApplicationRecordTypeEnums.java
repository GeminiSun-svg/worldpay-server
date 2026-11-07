package com.worldpay.enums;

import lombok.Getter;

/**
 * 类描述：用户申请记录类型
 *
 * @author GeminiSun
 * @date 2026/2/4 9:28
 */
@Getter
public enum ApplicationRecordTypeEnums {

    UPGRADE (0, "绑定成为渠道商"),
    BECAME (1, "升级成为渠道商 ");

    private Integer index;

    private String name;

    private ApplicationRecordTypeEnums(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

}
