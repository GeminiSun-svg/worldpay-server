package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 开卡状态
 * @author Hlin
 *
 */
public enum OpenCardStateEnums {
	WAIT_ACTIVATION(1, "待激活"),
	PROCESS_ACTIVATION(2, "激活中"),
	SUCCESS_ACTIVATION(3, "激活成功"),
	ERROR_ACTIVATION(4, "激活失败"),
	WAIT_USER_ACTIVATION(5, "待用户充值激活");
	
	private Integer index;

	private String name;


	private OpenCardStateEnums(Integer index, String name) {
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
	
	public static List<DicEntity> getList() {
		OpenCardStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (OpenCardStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
