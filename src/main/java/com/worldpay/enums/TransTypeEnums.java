package com.worldpay.enums;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.enums.TransTypeEnums;

public enum TransTypeEnums {
	TOPUP(1, "TOPUP","银行卡充值($)","Bank card top-up ($)","銀行卡儲值($)zhong",-1),//充值
	AUTH(2, "AUTH","银行卡消费($)","Bank card spending ($)","金融卡消費($)",170),//支出
	REFUND(4, "REFUND","银行卡退款($)","Bank card refund ($)","金融卡退款($)",171),
	CLOSE(3, "CLOSE","银行卡注销($)","Bank card cancellation ($)","金融卡註銷($)",-2);
	
	private Integer index;

	private String name;
	
	private String valueZh;
	
	private String valueEn;
	
	private String valueTw;

	private Integer apiType;

	private TransTypeEnums(Integer index, String name, String valueZh, String valueEn, String valueTw,Integer apiType) {
	    this.index = index;
	    this.name = name;
	    this.valueZh = valueZh;
	    this.valueEn = valueEn;
	    this.valueTw = valueTw;
	    this.apiType = apiType;
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


	public String getValueZh() {
		return valueZh;
	}


	public void setValueZh(String valueZh) {
		this.valueZh = valueZh;
	}



	public Integer getApiType() {
		return apiType;
	}


	public void setApiType(Integer apiType) {
		this.apiType = apiType;
	}
	
	
	public String getValueEn() {
		return valueEn;
	}


	public void setValueEn(String valueEn) {
		this.valueEn = valueEn;
	}


	public String getValueTw() {
		return valueTw;
	}


	public void setValueTw(String valueTw) {
		this.valueTw = valueTw;
	}


	public static String getType(Integer apiType) {
		TransTypeEnums[] typeEnums = values();
		for (TransTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getApiType().equals(apiType)) {
				return typeEnum.getName();
			}
		}
		return null;
	}

	
	public static String getValue(String name) {
		TransTypeEnums[] typeEnums = values();
		for (TransTypeEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getValueZh();
			}
		}
		return null;
	}
	
	public static String getLanguagName(String type, String languag, CardTransactionEntity entity) {
		TransTypeEnums[] typeEnums = values();
	    for (TransTypeEnums typeEnum : typeEnums) {
	        if (typeEnum.getName().equals(type)) {
	            String name = "";
	            if ("zh".equals(languag)) {
	                name = typeEnum.getValueZh();
	            } else if ("en".equals(languag)) {
	                name = typeEnum.getValueEn();
	            } else if ("tw".equals(languag)) {
	                name = typeEnum.getValueTw();
	            } 
	            
	            // 根据操作类型替换括号中的内容
	            name = replacePlaceholder(name,entity);
	            return name;
	        }
	    }
	    return null;
	}
	
	/**
	 * 根据操作类型替换名称中的占位符
	 */
	private static String replacePlaceholder(String name, CardTransactionEntity entity) {
	    if (name == null || !name.contains("($)")) {
	        return name;
	    }
	    
	    String replacement = getLast4Digits(entity.getCardNo());
	    return name.replace("($)", "(" + replacement + ")");
	}

	/**
	 * @category 截取后四位
	 * @param str
	 * @return
	 */
	public static String getLast4Digits(String str) {
	    if (str == null || str.isEmpty()) {
	        return "";
	    }
	    return str.length() >= 4 ? str.substring(str.length() - 4) : str;
	}

}
