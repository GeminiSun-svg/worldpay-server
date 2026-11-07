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
@Schema(description = "")
public class BrokerageLogEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("order_id")
    @Schema(description = "")
private String orderId;

    @TableField("intiter_user_type")
    @Schema(description = "")
private Integer intiterUserType;

    @TableField("intiter_user_level")
    @Schema(description = "")
private Integer intiterUserLevel;

    @TableField("Inviter_user")
    @Schema(description = "")
private Integer inviterUser;

    @TableField("Inviter_user_uid")
    @Schema(description = "")
private String inviterUserUid;

    @TableField("cover_Inviter_user")
    @Schema(description = "")
private Integer coverInviterUser;

    @TableField(exist = false)
    private String email;

    @TableField("cover_Inviter_user_uid")
    @Schema(description = "")
private String coverInviterUserUid;

    @TableField("card_id")
    @Schema(description = "")
private Integer cardId;

    @TableField("card_uuid")
    @Schema(description = "")
private String cardUuid;

    @TableField("card_type")
    @Schema(description = "")
private String cardType;

    @TableField("topup_fee")
    @Schema(description = "")
private BigDecimal topupFee;

    @TableField("handling_fee")
    @Schema(description = "")
private BigDecimal handlingFee;

    @TableField("brokerage_rates")
    @Schema(description = "")
private BigDecimal brokerageRates;

    @TableField("brokerage_fee")
    @Schema(description = "")
private BigDecimal brokerageFee;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;

}
