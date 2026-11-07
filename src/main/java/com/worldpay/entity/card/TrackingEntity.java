package com.worldpay.entity.card;

import lombok.Data;

@Data
public class TrackingEntity {

	  /**
     * 必填：物流单号，需满足格式要求
     */
    private String number;

    /**
     * 可选：用于翻译物流事件的语言代码
     * 若使用翻译，将消耗额外的请求额度
     */
    private String lang;

    /**
     * 可选：用户邮箱，最长250字符。
     * 邮箱有效且启用通知功能时，可接收物流状态变更通知
     */
    private String email;

    /**
     * 可选：物流单号附加参数，如邮编、下单日期等
     */
    private String param;

    /**
     * 可选：订单编号，3到50个字符
     */
    private String orderNo;

    /**
     * 可选：订单日期（例如 2023/1/1）
     */
    private String orderTime;

    /**
     * 可选：运输商代码，系统支持自动识别多数运输商
     */
    private Integer carrier;

    /**
     * 可选：尾程运输商代码（仅支持邮政渠道）
     */
    private Integer finalCarrier;

    /**
     * 可选：是否开启自动识别运输商，默认 true。
     * 如果传入了有效的 carrier，则此参数无效
     */
    private Boolean autoDetection;

    /**
     * 可选：自定义标签（最长100字符），可用于备注或标识
     */
    private String tag;

    /**
     * 可选：自定义备注（最长1000字符），用于描述包裹情况
     */
    private String remark;

}
