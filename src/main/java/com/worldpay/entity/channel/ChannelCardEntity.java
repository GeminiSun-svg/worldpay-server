package com.worldpay.entity.channel;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("channel_card")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "渠道卡片配置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelCardEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;

    @TableField("card_id")
    @Schema(description = "卡片ID")
	private Integer cardId;

    @TableField("card_uuid")
    @Schema(description = "卡片唯一标识")
	private String cardUuid;

    @TableField("card_type")
    @Schema(description = "卡片类型")
	private String cardType;

    @TableField("channel_id")
    @Schema(description = "渠道ID")
	private Integer channelId;

    @TableField("open_fee")
    @Schema(description = "开卡费")
	private Double openFee;

    @TableField("rates_fee")
    @Schema(description = "充值费率")
	private Double ratesFee;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;

    @TableField(exist = false)
    @Schema(description = "登录账号")
	private String acctive;

}
