package com.worldpay.api.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.worldpay.entity.card.CardEntity;
import lombok.Data;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "渠道卡片响应")
public class ChannelCardResponse {

    @Schema(description = "主键ID")
	private Integer id;

    @Schema(description = "卡片ID")
	private Integer cardId;

    @Schema(description = "卡片UUID")
	private String cardUuid;

    @Schema(description = "卡片类型")
	private String cardType;

    @Schema(description = "渠道ID")
	private Integer channelId;

    @Schema(description = "开卡手续费")
	private Double openFee;

    @Schema(description = "充值费率")
	private Double ratesFee;

    @Schema(description = "创建时间")
	private Date setTime;

    @Schema(description = "修改时间")
	private Date gmtModified;

    @Schema(description = "用户名")
    @TableField(exist = false)
    private String username;

    @Schema(description = "账号（邮箱）")
    @TableField(exist = false)
    private String acctive;

    @Schema(description = "卡片实体信息")
    @TableField(exist = false)
    private CardEntity cardEntity;
}
