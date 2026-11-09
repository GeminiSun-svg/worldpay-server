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
@TableName("brokerage_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "佣金流水")
public class BrokerageLogEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;

    @TableField("order_id")
    @Schema(description = "订单ID")
	private String orderId;

    @TableField("intiter_user_type")
    @Schema(description = "邀请人用户类型")
	private Integer intiterUserType;

    @TableField("intiter_user_level")
    @Schema(description = "邀请人用户等级")
	private Integer intiterUserLevel;

    @TableField("Inviter_user")
    @Schema(description = "邀请人ID")
	private Integer inviterUser;

    @TableField("Inviter_user_uid")
    @Schema(description = "邀请人用户ID")
	private String inviterUserUid;

    @TableField("cover_Inviter_user")
    @Schema(description = "上级邀请人ID")
	private Integer coverInviterUser;

    @TableField(exist = false)
    @Schema(description = "邀请人邮箱")
    private String email;

    @TableField("cover_Inviter_user_uid")
    @Schema(description = "上级邀请人用户ID")
	private String coverInviterUserUid;

    @TableField("card_id")
    @Schema(description = "卡片ID")
	private Integer cardId;

    @TableField("card_uuid")
    @Schema(description = "卡片唯一标识")
	private String cardUuid;

    @TableField("card_type")
    @Schema(description = "卡片类型")
	private String cardType;

    @TableField("topup_fee")
    @Schema(description = "充值金额")
	private BigDecimal topupFee;

    @TableField("handling_fee")
    @Schema(description = "手续费")
	private BigDecimal handlingFee;

    @TableField("brokerage_rates")
    @Schema(description = "佣金费率")
	private BigDecimal brokerageRates;

    @TableField("brokerage_fee")
    @Schema(description = "佣金金额")
	private BigDecimal brokerageFee;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;

}
