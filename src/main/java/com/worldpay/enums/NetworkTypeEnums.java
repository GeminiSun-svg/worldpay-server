package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.worldpay.entity.response.DicEntity;

public enum NetworkTypeEnums {
	TRON(1, "TRON", "TRON", "tron"), 
	BNB(2, "BNB", "BSC", "bnb"), 
	ETH(3, "ETH", "ETH", "eth"),
	BTC(4, "BTC", "BTC", "btc");

	private Integer index;

	private String name;

	private String lable;

	private String lowerName;

	private NetworkTypeEnums(Integer index, String name, String lable, String lowerName) {
		this.index = index;
		this.name = name;
		this.lable = lable;
		this.lowerName = lowerName;
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

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static String getValue(int i) {
		NetworkTypeEnums[] carTypeEnums = values();
		for (NetworkTypeEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}

	public String getLowerName() {
		return lowerName;
	}

	public void setLowerName(String lowerName) {
		this.lowerName = lowerName;
	}

	public static Integer getIndex(String index) {
		NetworkTypeEnums[] carTypeEnums = values();
		for (NetworkTypeEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getName().equals(index)) {
				return carTypeEnum.getIndex();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getListLable() {
		NetworkTypeEnums[] typeEnums = values();
		List<Map<String, Object>> list = new ArrayList<>();
		for (NetworkTypeEnums typeEnum : typeEnums) {
			Map<String, Object> map = new HashedMap();
			if (typeEnum.getIndex() < 4) {
				map.put("networkType", typeEnum.getLowerName());
				List<Map<String, Object>> tokenList = new ArrayList<>();
				if(typeEnum.getIndex() == 1) {
					Map<String, Object> usdt = new HashedMap();
					usdt.put("tokenName", "usdt");
					usdt.put("tokenAddress", "TR7NHqjeKQxGTCi8q8ZY4pL8ot5gCx7Fj1");
					tokenList.add(usdt);
//					Map<String, Object> usdc = new HashedMap();
//					usdc.put("tokenName", "usdc");
//					usdc.put("tokenAddress", "TEkxiTehnzSmSe2XqrBj4w32RUN966rdz8");
//					tokenList.add(usdc);
				}else if(typeEnum.getIndex() == 2) {
					Map<String, Object> usdt = new HashedMap();
					usdt.put("tokenName", "usdt");
					usdt.put("tokenAddress", "0xdAC17F958D2ee523a2206206994597C13D831ec7");
					tokenList.add(usdt);
					Map<String, Object> usdc = new HashedMap();
					usdc.put("tokenName", "usdc");
					usdc.put("tokenAddress", "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48");
					tokenList.add(usdc);
				}else if(typeEnum.getIndex() == 3){
					Map<String, Object> usdt = new HashedMap();
					usdt.put("tokenName", "usdt");
					usdt.put("tokenAddress", "0xdAC17F958D2ee523a2206206994597C13D831ec7");
					tokenList.add(usdt);
					Map<String, Object> usdc = new HashedMap();
					usdc.put("tokenName", "usdc");
					usdc.put("tokenAddress", "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48");
					tokenList.add(usdc);
				}
				map.put("tokenList", tokenList);
				list.add(map);
			}
		}
		return list;
	}

	public static List<DicEntity> getList() {
		NetworkTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (NetworkTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
