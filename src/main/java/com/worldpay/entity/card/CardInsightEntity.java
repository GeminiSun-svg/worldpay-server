package com.worldpay.entity.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 卡片消费洞察统计结果
 */
@Data
@Schema(description = "卡片消费洞察统计")
public class CardInsightEntity {

    @Schema(description = "统计维度标签（日：yyyy-MM-dd；周：当周周一日期；月：yyyy-MM；季度：yyyy-Qn）")
    private String dateLabel;

    @Schema(description = "消费合计（AUTH，金额绝对值）")
    private Double consumeTotal;

    @Schema(description = "充值合计（TOPUP，金额绝对值）")
    private Double topupTotal;

    @Schema(description = "退款合计（REFUND，金额绝对值）")
    private Double refundTotal;

    @Schema(description = "交易笔数")
    private Integer totalCount;

    @Schema(description = "上一周期消费合计（环比）")
    private Double prevConsumeTotal;

    @Schema(description = "上一周期充值合计（环比）")
    private Double prevTopupTotal;

    @Schema(description = "上一周期退款合计（环比）")
    private Double prevRefundTotal;

    @Schema(description = "上一周期交易笔数（环比）")
    private Integer prevTotalCount;

    @Schema(description = "实际生效的区间开始日期")
    private String startDate;

    @Schema(description = "实际生效的区间结束日期")
    private String endDate;

    @Schema(description = "按时间维度聚合的明细列表")
    private List<CardInsightEntity> list;
}
