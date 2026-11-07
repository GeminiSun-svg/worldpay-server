package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 入口开启状态
 * @author Hlin
 *
 */
public enum OpenStateEnums {
	CLOSURE(0, "关闭"),
	OPEN(1, "开启");
	
	private Integer index;

	private String name;


	private OpenStateEnums(Integer index, String name) {
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
		OpenStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (OpenStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
