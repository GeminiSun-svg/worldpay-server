package com.worldpay.enums;

/**
 * @category api三方kyc认证状态
 * @author Hlin
 *
 */
public enum KycApiStateEnums {
	UNCOMMITTED(1, "uncommitted"),//尚未提交
	WAITING(2, "waiting"),//等待审核
	SUCCESS(3, "success"),//已通过
	FAIL(4, "fail"),//被拒绝
	WAIT_CONFIRM(5, "wait_confirm"),//待用户确认
	WAIT_AUDIT(6, "wait_audit");//	审核中
	
	private Integer index;

	private String name;


	private KycApiStateEnums(Integer index, String name) {
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

}
