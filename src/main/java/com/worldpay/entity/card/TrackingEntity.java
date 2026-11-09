package com.worldpay.entity.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "物流查询参数")
public class TrackingEntity {

	  /**
     * 必填：物流单号，需满足格式要求
     */
    @Schema(description = "物流单号")
    private String number;

    /**
     * 可选：用于翻译物流事件的语言代码
     * 若使用翻译，将消耗额外的请求额度
     */
    @Schema(description = "语言代码")
    private String lang;

    /**
     * 可选：用户邮箱，最长250字符。
     * 邮箱有效且启用通知功能时，可接收物流状态变更通知
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 可选：物流单号附加参数，如邮编、下单日期等
     */
    @Schema(description = "物流单号附加参数")
    private String param;

    /**
     * 可选：订单编号，3到50个字符
     */
    @Schema(description = "订单编号")
    private String orderNo;

    /**
     * 可选：订单日期（例如 2023/1/1）
     */
    @Schema(description = "订单日期")
    private String orderTime;

    /**
     * 可选：运输商代码，系统支持自动识别多数运输商
     */
    @Schema(description = "运输商代码")
    private Integer carrier;

    /**
     * 可选：尾程运输商代码（仅支持邮政渠道）
     */
    @Schema(description = "尾程运输商代码")
    private Integer finalCarrier;

    /**
     * 可选：是否开启自动识别运输商，默认 true。
     * 如果传入了有效的 carrier，则此参数无效
     */
    @Schema(description = "是否自动识别运输商")
    private Boolean autoDetection;

    /**
     * 可选：自定义标签（最长100字符），可用于备注或标识
     */
    @Schema(description = "自定义标签")
    private String tag;

    /**
     * 可选：自定义备注（最长1000字符），用于描述包裹情况
     */
    @Schema(description = "自定义备注")
    private String remark;

}
