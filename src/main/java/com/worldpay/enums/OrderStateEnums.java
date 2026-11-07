package com.worldpay.enums;

import com.worldpay.enums.OrderStateEnums;

/**
 * @category 充值订单状态
 * @author Hlin
 *
 */
public enum OrderStateEnums {
	PENDING_PROCESSING(0, "pendingProcessing","待处理","Pending processing","待處理"),
	PROCESSING(1, "processing","处理中","Processing","處理中"),
	POSTED(2, "posted","完成","Finish","完成"),
	FAILED(3, "failed","失败","fail","失敗"),
	CANCELLED(4, "cancelled","已取消","Cancelled","已取消"),
	REFUND_PENDING(5, "refundPending","待退款","refund pending","待退款"),
	REFUND_RECEIVED(6, "refundReceived","Refund received","fail","已退款"),
	REFUND_FAILED(7, "refundFailed","退款失败","Refund failed","退款失敗"),
	ACCEPTED(8, "accepted","已受理","Accepted","已受理"),
	CREATED(9, "created","已创建","Created","已創建"),
	REJECTED(10, "rejected","已拒绝","Rejected","已拒絕"),
	FINISH(11, "finish","结束","Finish","結束");
	
	private Integer index;

	private String name;
	
	private String zhName;
	
	private String enName;
	
	private String twName;

	private OrderStateEnums(Integer index, String name, String zhName, String enName, String twName) {
	    this.index = index;
	    this.name = name;
	    this.zhName = zhName;
	    this.enName = enName;
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

	public static String getZhName(String name) {
		OrderStateEnums[] typeEnums = values();
		for (OrderStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getZhName();
			}
		}
		return null;
	}
	
	public static String getEnName(String name) {
		OrderStateEnums[] typeEnums = values();
		for (OrderStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getEnName();
			}
		}
		return null;
	}
	
	public static String getTwName(String name) {
		OrderStateEnums[] typeEnums = values();
		for (OrderStateEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getTwName();
			}
		}
		return null;
	}
	
	
	
	
	public static String getIdToName(Integer id) {
		OrderStateEnums[] typeEnums = values();
		for (OrderStateEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(id)) {
				return typeEnum.getName();
			}
		}
		return null;
	}
	
}
