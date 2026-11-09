package com.worldpay.entity.brokerage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("channel_level")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "渠道等级")
public class ChannelLevelEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;

    @TableField("level_name")
    @Schema(description = "等级名称")
	private String levelName;

    @TableField("level_no")
    @Schema(description = "等级序号")
	private Integer levelNo;

    @TableField("level_info")
    @Schema(description = "等级说明")
	private String levelInfo;

    @TableField("directly_rates_fee")
    @Schema(description = "直推费率")
	private BigDecimal directlyRatesFee;

    @TableField("rates_fee")
    @Schema(description = "费率")
	private BigDecimal ratesFee;

    @TableField("two_rates_fee")
    @Schema(description = "二级费率")
	private BigDecimal twoRatesFee;

    @TableField("three_rates_fee")
    @Schema(description = "三级费率")
	private BigDecimal threeRatesFee;

    @TableField("prepaid_fees")
    @Schema(description = "预付费金额")
	private Double prepaidFees;

    @TableField("open_fees")
    @Schema(description = "开通费用")
	private Double openFees;

    @TableField("delete_state")
    @Schema(description = "删除标记")
	private Integer deleteState;

    @TableField("apply_state")
    @Schema(description = "申请状态")
	private Integer applyState;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;
}
