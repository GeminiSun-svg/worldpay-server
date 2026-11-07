package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 三方接口状态码类型
 * @author Hlin
 *
 */
public enum SysConfigTypeEnums {
	USER_AGREEMENT(1, "user_agreement", "用户协议", 1, "用户协议", "User Agreement", "用戶協議"),
	PRIVATE_AGREEMENT(2, "private_agreement", "隐私协议", 1, "隐私协议", "Privacy Policy", "隱私協議"),
	ABOUT_US(3, "about_us", "关于我们", 1, "关于我们", "about Us", "關於我們"),
	CONTACT_US(4, "contact_us", "联系我们", 1, "联系我们", "Contact Us", "聯絡我們"),
	SUPPORT(5, "support", "客服", 2, "客服", "customer service", "客服"),
//	HELP_CENTER (7, "help_center","帮助中心",1),
	WALLET_CENTER(8, "wallet_center", "钱包充值介绍", 1, "钱包充值介绍", "Wallet Top-up Introduction", "錢包儲值介紹"),
	BANK_CENTER(9, "bank_center", "银行卡充值介绍", 1, "银行卡充值介绍", "Bank card top-up introduction", "銀行卡儲值介紹"),
	ADD_WALLET(10, "add_wallet", "添加到钱包", 1, "添加到钱包", "Add to wallet", "加入錢包"), CRYPTOCURRENCY_CENTER(11,
			"cryptocurrency_center", "虚拟币充值介绍", 1, "虚拟币充值介绍", "Introduction to virtual currency top-up", "虛擬幣儲值介紹");

	private Integer index;

	private String name;

	private String lable;

	private Integer type;

	private String zhName;

	private String enName;

	private String twName;

	private SysConfigTypeEnums(Integer index, String name, String lable, Integer type, String zhName, String enName,
			String twName) {
		this.index = index;
		this.name = name;
		this.lable = lable;
		this.type = type;
		this.zhName = zhName;
		this.enName = enName;
		this.twName = twName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getZhName() {
		return zhName;
	}

	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getTwName() {
		return twName;
	}

	public void setTwName(String twName) {
		this.twName = twName;
	}

	public static List<DicEntity> getProtocolType() {
		SysConfigTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getType() == 1) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setId(typeEnum.getIndex());
				dicEntity.setName(typeEnum.getLable());
				list.add(dicEntity);
			}
		}
		return list;
	}

	public static List<DicEntity> getJumpAddress() {
		SysConfigTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getType() == 2) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setId(typeEnum.getIndex());
				dicEntity.setName(typeEnum.getLable());
				list.add(dicEntity);
			}
		}
		return list;
	}

	public static List<DicEntity> getLists() {
		SysConfigTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getLable());
			list.add(dicEntity);
		}
		return list;
	}

	public static String getName(int i) {
		SysConfigTypeEnums[] typeEnums = values();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getLable();
			}
		}
		return null;
	}

	public static Integer getLable(int i) {
		SysConfigTypeEnums[] typeEnums = values();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getType();
			}
		}
		return null;
	}

	public static String getType(int i) {
		SysConfigTypeEnums[] typeEnums = values();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}
	
	public static String getLanguageName(int i,String language) {
		SysConfigTypeEnums[] typeEnums = values();
		for (SysConfigTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				if(LanguageEnums.EN_US.getName().equals(language)) {
					return typeEnum.getEnName();
				}else if(LanguageEnums.ZH_CN.getName().equals(language)) {
					return typeEnum.getZhName();
				}else if(LanguageEnums.ZH_TW.getName().equals(language)) {
					return typeEnum.getTwName();
				}
			}
		}
		return null;
	}

}
