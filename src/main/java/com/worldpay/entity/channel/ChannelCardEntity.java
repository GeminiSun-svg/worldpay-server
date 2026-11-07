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
@Schema(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelCardEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("card_id")
    @Schema(description = "")
private Integer cardId;

    @TableField("card_uuid")
    @Schema(description = "")
private String cardUuid;

    @TableField("card_type")
    @Schema(description = "")
private String cardType;

    @TableField("channel_id")
    @Schema(description = "")
private Integer channelId;

    @TableField("open_fee")
    @Schema(description = "")
private Double openFee;

    @TableField("rates_fee")
    @Schema(description = "")
private Double ratesFee;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;

    @TableField(exist = false)
    @Schema(description = "")
private String acctive;

}
