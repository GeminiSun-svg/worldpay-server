package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 充值方式
 * @author Hlin
 *
 */
public enum PayTypeEnums {
	WALLET(1, "钱包"),
	BANK_CARD(2, "银行卡"),
	MANUAL_TRANSFER(3, "客服手动转入");
	
	private Integer index;

	private String name;


	private PayTypeEnums(Integer index, String name) {
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
		PayTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (PayTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}
	
}
