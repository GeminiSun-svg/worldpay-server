package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

public enum EmsStateEnums {
	NOTFOUND(0, "NotFound"),
	INFORECEIVED(1, "InfoReceived"),
	INTRANSIT(2, "InTransit"),
	EXPIRED(3, "Expired"),
	AVAILABLEFORPICKUP(4, "AvailableForPickup"),
	OUTFORDELIVERY(5, "OutForDelivery"),
	DELIVERYFAILURE(6, "DeliveryFailure"),
	DELIVERED(7, "Delivered"),
	EXCEPTION(8, "Exception");

	private Integer index;

	private String name;

	private EmsStateEnums(Integer index, String name) {
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
		EmsStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (EmsStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
