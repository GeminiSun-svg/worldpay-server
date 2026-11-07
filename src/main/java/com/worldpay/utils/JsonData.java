package com.worldpay.utils;

import java.io.Serializable;

@SuppressWarnings("serial")
public class JsonData implements Serializable {

    /**
     * 状态码 0表示成功过，-1,-2,-3、、、为失败
     */
    private Integer rtncode;

    /**
     * 业务数据
     */
    private Object data;

    /**
     * 信息表示
     */
    private String msg;

    public JsonData() {
    }

    public JsonData(Integer rtncode, Object data, String msg) {
        this.rtncode = rtncode;
        this.data = data;
        this.msg = msg;
    }


    /**
     * 成功，不用返回数据
     *
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(200, null, null);
    }

    /**
     * 成功，返回数据
     *
     * @param data
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(200, data, null);
    }
    
    /**
     * 成功，返回提示
     *
     * @param data
     * @return
     */
    public static JsonData buildSuccess(String msg) {
        return new JsonData(200, null,msg);
    }


    /**
     * 失败，固定状态码
     *
     * @param msg
     * @return
     */
    public static JsonData buildError(String msg) {
        return new JsonData(403, null, msg);
    }
    
    /**
     * 失败，固定状态码
     *
     * @param msg
     * @return
     */
    public static JsonData PowerError(String msg) {
        return new JsonData(401, null, msg);
    }
    
    /**
     * 失败，固定状态码
     *
     * @param msg
     * @return
     */
    public static JsonData Error(String msg) {
        return new JsonData(500, null, msg);
    }


    /**
     * 失败，自定义错误码和信息
     *
     * @param code
     * @param msg
     * @return
     */
    public static JsonData buildError(Integer code, String msg) {
        return new JsonData(code, null, msg);
    }

	public Integer getRtncode() {
		return rtncode;
	}

	public void setRtncode(Integer rtncode) {
		this.rtncode = rtncode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    
}

