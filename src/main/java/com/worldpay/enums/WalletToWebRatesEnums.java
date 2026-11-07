package com.worldpay.enums;

/**
 * @category 提现手续费费率、服务费兜底
 * @author Hlin
 *
 */
public enum WalletToWebRatesEnums {
	HANDLINGRATES (1, 0.01),//成功
	SERVERAMOUNT (2, 1.0),
	MIN_AMOUNT (3, 5.0),
	MAX_AMOUNT (4, 10000.0);//失败
	
	private Integer index;

	private Double value;


	private WalletToWebRatesEnums(Integer index, Double value) {
	    this.index = index;
	    this.value = value;
	}


	public Integer getIndex() {
	    return index;
	}


	public void setIndex(Integer index) {
	   this.index = index;
	}


	public Double getValue() {
	     return value;
	}


	public void setName(Double value) {
	    this.value = value;
	}

}
