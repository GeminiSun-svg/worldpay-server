package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

public enum TokenTypeEnums {
	USDC(1, "usdc"), USDT(0, "usdt");

	private Integer index;

	private String name;

	private TokenTypeEnums(Integer index, String name) {
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

	public static String getValue(int i) {
		TokenTypeEnums[] carTypeEnums = values();
		for (TokenTypeEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}

	public static Integer getIndex(String index) {
		TokenTypeEnums[] carTypeEnums = values();
		for (TokenTypeEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getName().equals(index)) {
				return carTypeEnum.getIndex();
			}
		}
		return null;
	}

	public static List<DicEntity> getList() {
		TokenTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (TokenTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
