package com.worldpay.enums;

import java.util.ArrayList;
import java.util.List;

import com.worldpay.entity.response.DicEntity;

/**
 * @category 钱包操作类型
 * @author Hlin
 *
 */
public enum WalletTitleTypeEnums {
	WALLET_TOP_UP(1, "walletTopUp","钱包充值","錢包儲值",1),//钱包充值
	CARD_TOP_UP(2, "cardTopUp","银行卡充值","金融卡儲值",1),
	OPEN_CARD(3, "openCard","实体卡开卡","實體卡開卡",1),
	WALLET_TDEDUCTIONS(4, "Back-end transfer","后台转账","后台转账",1),
	INTERNAL_TRANSFER(5, "Internal transfer","内部转账","內部轉帳",1),
	CARD_CLOSE(6, "Cancel bank card","注销银行卡","註銷銀行卡",1),
	OPEN_CARD_FREEZE(7, "Card freezing","开卡冻结","開卡凍結",0),
	OPEN_CARD_THAW(8, "Card activation cancellation","开卡取消","開卡取消",0),
	PAY_PRODUCT_THAW(9, "Investment freeze","理财冻结","理財凍結",1),
	PAY_INVESTMENT_RETURNS(10, "Investment returns","理财收益","理財收益",1),
	OPEN_CARD_VIRTUAL(11, "openCard","虚拟卡开卡","虛擬卡開卡",1),
	BROKERAGE_REVENUE(12, "Commission income","佣金收入","佣金收入",1),
	COMMISSION_WITHDRAWAL(13, "commission withdrawal","佣金提现","佣金提領",1),
	FORECAST_TOP_UP(14, "Predicted recharge","预测充值","預測充值",0),
	FORECAST_WITHDRAWAL(15, "Predicted withdrawal","预测提现","預測提現",0),
	TRANSFER_OUT_WEB(16,"Withdrawal", "提现", "提現",1),
	TRANSFER_OUT_WEB_RETURN(17, "Withdrawal Rejected Refund", "提现拒绝退回", "提現拒絕退回",1),
	ENTITY_CARD_FREIGHT(18, "Physical card shipping fee","实体卡运费","實體卡運費",1),
	TRANSFER_OUT_WEB_ERROR(19, "Withdrawal failed and refunded","提现失败退回","提現失敗退回",1);


	private Integer index;

	private String enName;

	private String cnName;

	private String twName;

	private Integer state;

	private WalletTitleTypeEnums(Integer index, String enName, String cnName, String twName,Integer state) {
	    this.index = index;
	    this.enName = enName;
	    this.cnName = cnName;
	    this.twName = twName;
	    this.state = state;
	}


	public Integer getIndex() {
	    return index;
	}


	public void setIndex(Integer index) {
	   this.index = index;
	}
	
	public String getEnName() {
		return enName;
	}


	public void setEnName(String enName) {
		this.enName = enName;
	}


	public String getCnName() {
		return cnName;
	}


	public void setCnName(String cnName) {
		this.cnName = cnName;
	}


	public String getTwName() {
		return twName;
	}


	public void setTwName(String twName) {
		this.twName = twName;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public static String getLanguagName(Integer id,String languag) {
		WalletTitleTypeEnums[] typeEnums = values();
		for (WalletTitleTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(id)) {//中文简体
				if("zh".equals(languag)) {
					return typeEnum.getCnName();
				}else if("tw".equals(languag)) {
					return typeEnum.getTwName();
				}else {
					return typeEnum.getEnName();
				}
			}
		}
		return null;
	}
	
	public static List<DicEntity> getList() {
		WalletTitleTypeEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<DicEntity>();
		for (WalletTitleTypeEnums typeEnum : typeEnums) {
			if (PublicEnums.ONE.getIndex().equals(typeEnum.getState()) ) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setId(typeEnum.getIndex());
				dicEntity.setName(typeEnum.getCnName());
				list.add(dicEntity);
			}
		}
		return list;
	}
	
}
