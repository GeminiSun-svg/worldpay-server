package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

import lombok.Getter;

/**
 * 类描述：用户申请记录状态
 *
 * @author GeminiSun
 * @date 2026/2/4 9:28
 */
@Getter
public enum ApplicationRecordStateEnums {
    UNDER_REVIEW (0, "待审核"),
    AUDIT_PASS (1, "完成"),
    AUDIT_FAIL(2, "审核不通过 "),
    AUDIT_PASS_PROCESSING (3, "审核通过处理中"),
    TRANSFER_FAILED (501, "交易失败"),
    TRANSFER_NULL (502, "交易不存在"),
    TRANSFER_EXCEPTION (503, "交易异常"),
    CAHIN_EXCEPTION (900, "节点异常");


    private Integer index;

    private String name;

    private ApplicationRecordStateEnums(Integer index, String name) {
        this.index = index;
        this.name = name;
    }
    
	public static List<DicEntity> getList() {
		ApplicationRecordStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (ApplicationRecordStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
