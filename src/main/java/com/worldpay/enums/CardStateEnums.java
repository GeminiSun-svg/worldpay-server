package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;


/**
 * @category 开卡状态
 * @author Hlin
 *
 */
public enum CardStateEnums {
	INITIAL(0, "INITIAL","初始状态",""),
	TBA(1, "TBA","待激活",""),
	ACTIVE_PROCESSING(2, "ACTIVE_PROCESSING","激活中",""),
	ACTIVE(3, "ACTIVE","正常","cardActive"),
	FREEZE(4, "FREEZE","冻结","cardFreeze"),
	LOSS_REPORT(5, "LOSS_REPORT","挂失",""),
	BEFORE_CANCELLATION(6, "BEFORE_CANCELLATION","注销中",""),
	CANCELLATION(7, "CANCELLATION","注销","cardClose"),
	EXPIRED(8, "EXPIRED","过期",""),
	PAUSE(9, "PAUSE","暂停","");
	
	private Integer index;

	private String name;
	
	private String value;
	
	private String lable;


	private CardStateEnums(Integer index, String name,String value,String lable) {
	    this.index = index;
	    this.name = name;
	    this.value = value;
	    this.lable = lable;
	}

	public static Integer getId(String name) {
		CardStateEnums[] typeEnums = values();
		for (CardStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getIndex();
			}
		}
		return null;
	}
	
	public static String getValue(String id) {
		CardStateEnums[] typeEnums = values();
		for (CardStateEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().toString().equals(id)) {
				return typeEnum.getValue();
			}
		}
		return null;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
	public static List<DicEntity> getList() {
		CardStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (CardStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getValue());
			list.add(dicEntity);
		}
		return list;
	}
	
	

}
