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
@Schema(description = "")
public class ChannelLevelEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("level_name")
    @Schema(description = "")
private String levelName;

    @TableField("level_no")
    @Schema(description = "")
private Integer levelNo;

    @TableField("level_info")
    @Schema(description = "")
private String levelInfo;

    @TableField("directly_rates_fee")
    @Schema(description = "")
private BigDecimal directlyRatesFee;

    @TableField("rates_fee")
    @Schema(description = "")
private BigDecimal ratesFee;

    @TableField("two_rates_fee")
    @Schema(description = "")
private BigDecimal twoRatesFee;

    @TableField("three_rates_fee")
    @Schema(description = "")
private BigDecimal threeRatesFee;

    @TableField("prepaid_fees")
    @Schema(description = "")
private Double prepaidFees;

    @TableField("open_fees")
    @Schema(description = "")
private Double openFees;

    @TableField("delete_state")
    @Schema(description = "")
private Integer deleteState;

    @TableField("apply_state")
    @Schema(description = "")
private Integer applyState;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;
}
