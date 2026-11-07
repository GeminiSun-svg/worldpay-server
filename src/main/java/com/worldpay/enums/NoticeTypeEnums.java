package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 消息通知类型
 * @author Hlin
 *
 */
public enum NoticeTypeEnums {
	SYSTEM_NOTIFICATIONS(1, "系统通知"),
	EVENT_NOTICE(2, "活动通知");
	
	private Integer index;

	private String name;


	private NoticeTypeEnums(Integer index, String name) {
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
		NoticeTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (NoticeTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

	public static String getName(int i) {
		NoticeTypeEnums[] typeEnums = values();
		for (NoticeTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}

}
