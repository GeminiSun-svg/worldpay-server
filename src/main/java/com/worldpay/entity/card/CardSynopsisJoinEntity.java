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
@Schema(description = "卡片简介关联")
public class CardSynopsisJoinEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("language")
	@Schema(description = "语言")
	private String language;
	
	@TableField("card_id")
	@Schema(description = "卡片ID")
	private String cardId;
	
	@TableField("synopsis_id")
	@Schema(description = "简介ID")
	private Integer synopsisId;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
