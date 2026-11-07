package com.worldpay.entity.card;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_synopsis_join")
@Schema(description = "")
public class CardSynopsisJoinEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("language")
	@Schema(description = "")
private String language;
	
	@TableField("card_id")
	@Schema(description = "")
private String cardId;
	
	@TableField("synopsis_id")
	@Schema(description = "")
private Integer synopsisId;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
