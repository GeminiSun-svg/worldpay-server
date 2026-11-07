package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

public enum LogisticsSubStateEnums {
	
	INFORECEIVED(16, "InfoReceived",2,"已发货"),
	INTRANSIT_PICKEDUP(0, "InTransit_PickedUp",2,"已发货"),
	INTRANSIT_OTHER(1, "InTransit_Other",3,"运输中"),
	INTRANSIT_DEPARTURE(2, "InTransit_Departure",3,"运输中"),
	INTRANSIT_ARRIVAL(3, "InTransit_Arrival",3,"运输中"),
	INTRANSIT_CUSTOMSPROCESSING(4, "InTransit_CustomsProcessing",3,"运输中"),
	INTRANSIT_CUSTOMSRELEASED(5, "InTransit_CustomsReleased",3,"运输中"),
	INTRANSIT_CUSTOMSREQUIRINGINFORMATION(6, "InTransit_CustomsRequiringInformation",3,"运输中"),
	EXPIRED_OTHER(7, "Expired_Other",3,"运输中"),
	AVAILABLEFORPICKUP_OTHER(8, "AvailableForPickup_Other",3,"运输中"),
	OUTFORDELIVERY_OTHER(9, "OutForDelivery_Other",4,"配送中"),
	DELIVERYFAILURE_OTHER(10, "DeliveryFailure_Other",4,"配送中"),
	DELIVERYFAILURE_NOBODY(11, "DeliveryFailure_NoBody",4,"配送中"),
	DELIVERYFAILURE_SECURITY(12, "DeliveryFailure_Security",4,"配送中"),
	DELIVERYFAILURE_REJECTED(3, "DeliveryFailure_Rejected",4,"配送中"),
	DELIVERYFAILURE_INVALIDADDRESS(14, "DeliveryFailure_InvalidAddress",4,"配送中"),
	Delivered_Other(15, "Delivered_Other",5,"完成");

	private Integer index;

	private String name;
	
	private Integer lable;
	
	private String value;
	
	

	private LogisticsSubStateEnums(Integer index, String name,Integer lable, String value) {
		this.index = index;
		this.name = name;
		this.lable = lable;
		this.value = value;
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
	

	public Integer getLable() {
		return lable;
	}

	public void setLable(Integer lable) {
		this.lable = lable;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static List<DicEntity> getList() {
		LogisticsSubStateEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (LogisticsSubStateEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

	//根据子节点状态获取物流主状态id
	public static Integer getLable(String i) {
		LogisticsSubStateEnums[] typeEnums = values();
		for (LogisticsSubStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(i)) {
				return typeEnum.getLable();
			}
		}
		return null;
	}
	
	// 根据子节点状态获取物流主状态名称
	public static String getVale(String i) {
		LogisticsSubStateEnums[] typeEnums = values();
		for (LogisticsSubStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(i)) {
				return typeEnum.getValue();
			}
		}
		return null;
	}

}
