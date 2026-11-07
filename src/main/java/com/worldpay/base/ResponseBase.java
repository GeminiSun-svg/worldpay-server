package com.worldpay.base;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author csz
 *
 */
@Getter
@Setter
public class ResponseBase {

	private Integer rtncode;
	private String msg;
	private Object data;
	
	
	public ResponseBase() {
		super();
	}
	public ResponseBase(Integer rtncode, String msg, Object data) {
		super();
		this.rtncode = rtncode;
		this.msg = msg;
		this.data = data;
	}
	

}
