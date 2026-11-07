package com.worldpay.entity.card;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_data")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class CardEntity extends PageQueryHelperEntity{
	
	@TableField("uuid")
	@Schema(description = "")
private String uuid;
	
	@TableField("applyDiscount")
	@Schema(description = "")
private Integer applyDiscount;

	@TableField("applyFee")
	@Schema(description = "")
private Double applyFee;

	@TableField("bankCardNature")
	@Schema(description = "")
private String bankCardNature;

	@TableField("bankCardSource")
	@Schema(description = "")
private String bankCardSource;

	@TableField("bankCardType")
	@Schema(description = "")
private String bankCardType;

	@TableField("categoryId")
	@Schema(description = "")
private Integer categoryId;

	@TableField("ccy")
	@Schema(description = "")
private String ccy;

	@TableField("description1")
	@Schema(description = "")
private String description1;

	@TableField("description2")
	@Schema(description = "")
private String description2;

	@TableField("enable")
	@Schema(description = "")
private Boolean enable;

	@TableField("hot")
	@Schema(description = "")
private Boolean hot;

	@TableId(type = IdType.INPUT)
	@Schema(description = "")
private Integer id;

	@TableField("img")
	@Schema(description = "")
private String img;
	
	@TableField("list_img")
	@Schema(description = "")
private String listImg;

	@TableField("monthFee")
	@Schema(description = "")
private Integer monthFee;

	@TableField("rechargeFee")
	@Schema(description = "")
private Double rechargeFee;

	@TableField("refundFee")
	@Schema(description = "")
private Double refundFee;

	@TableField("recommend")
	@Schema(description = "")
private Boolean recommend;

	@TableField("sortParam")
	@Schema(description = "")
private Integer sortParam;

	@TableField("title")
	@Schema(description = "")
private String title;

	@TableField("activeMinLimit")
	@Schema(description = "")
private Double activeMinLimit;

	@TableField("rechargeMinLimit")
	@Schema(description = "")
private Double rechargeMinLimit;

	@TableField("rechargeMaxLimit")
	@Schema(description = "")
private Double rechargeMaxLimit;

	@TableField("cardBin")
	@Schema(description = "")
private String cardBin;
	
	@TableField("open_card_cost")
	@Schema(description = "")
private Double openCardCost;
	
	@TableField("pre_save_cost")
	@Schema(description = "")
private Double preSaveCost;
	
	@TableField("card_state")
	@Schema(description = "")
private Integer cardState;
	
	@TableField(exist = false)
	@Schema(description = "")
private String language;
	
	@TableField(exist = false)
	@Schema(description = "")
private List<CardLableEntity> lableList;
	
	@TableField(exist = false)
	@Schema(description = "")
private List<Integer> lableIdList;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardSynopsisEntity synopsisData;
	
	@TableField(exist = false)
	@Schema(description = "")
private List<Integer> synopsisIdList;
	
	@TableField(exist = false)
	@Schema(description = "")
private List<CardSynopsisEntity> synopsisReq;
	
	@TableField(exist = false)
	@Schema(description = "")
private String cardBrand;

}
