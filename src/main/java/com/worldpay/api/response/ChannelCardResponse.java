package com.worldpay.api.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.worldpay.entity.card.CardEntity;
import lombok.Data;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ChannelCardResponse {

    @Schema(description = "")
private Integer id;

    @Schema(description = "")
private Integer cardId;

    @Schema(description = "")
private String cardUuid;

    @Schema(description = "")
private String cardType;

    @Schema(description = "")
private Integer channelId;

    @Schema(description = "")
private Double openFee;

    @Schema(description = "")
private Double ratesFee;

    @Schema(description = "")
private Date setTime;

    @Schema(description = "")
private Date gmtModified;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String acctive;

    @TableField(exist = false)
    private CardEntity cardEntity;
}
