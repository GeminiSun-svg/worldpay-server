package com.worldpay.api.dto;

import lombok.Data;

/**
 * 类描述：封装递归结果
 *
 * @author GeminiSun
 * @date 2026/1/16 18:32
 */
@Data
public class InviterLevelDTO {
    // 上级 id
    private Integer inviterId;
    // 上级uid
    private String inviterUid;
    // 邀请类型
    private Integer inviterType;
    // 渠道商等级
    private Integer inviterLevel;
    //本人id
    private Integer coverId;
    //本人uid
    private Integer coverUid;
    // 层级编号（1=直接上级，2=二级上级，3=三级上级）
    private Integer levelNum;
}
