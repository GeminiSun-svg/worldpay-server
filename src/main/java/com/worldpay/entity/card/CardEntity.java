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
@Schema(description = "卡片信息")
public class CardEntity extends PageQueryHelperEntity{
	
	@TableField("uuid")
	@Schema(description = "唯一标识")
	private String uuid;
	
	@TableField("applyDiscount")
	@Schema(description = "申请折扣")
	private Integer applyDiscount;

	@TableField("applyFee")
	@Schema(description = "申请费用")
	private Double applyFee;

	@TableField("bankCardNature")
	@Schema(description = "卡片性质")
	private String bankCardNature;

	@TableField("bankCardSource")
	@Schema(description = "卡片来源")
	private String bankCardSource;

	@TableField("bankCardType")
	@Schema(description = "卡片种类")
	private String bankCardType;

	@TableField("categoryId")
	@Schema(description = "分类ID")
	private Integer categoryId;

	@TableField("ccy")
	@Schema(description = "币种")
	private String ccy;

	@TableField("description1")
	@Schema(description = "描述一")
	private String description1;

	@TableField("description2")
	@Schema(description = "描述二")
	private String description2;

	@TableField("enable")
	@Schema(description = "是否启用")
	private Boolean enable;

	@TableField("hot")
	@Schema(description = "是否热门")
	private Boolean hot;

	@TableId(type = IdType.INPUT)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("img")
	@Schema(description = "图片地址")
	private String img;
	
	@TableField("list_img")
	@Schema(description = "列表图片地址")
	private String listImg;

	@TableField("monthFee")
	@Schema(description = "月费")
	private Integer monthFee;

	@TableField("rechargeFee")
	@Schema(description = "充值手续费率")
	private Double rechargeFee;

	@TableField("refundFee")
	@Schema(description = "退款手续费率")
	private Double refundFee;

	@TableField("recommend")
	@Schema(description = "是否推荐")
	private Boolean recommend;

	@TableField("sortParam")
	@Schema(description = "排序")
	private Integer sortParam;

	@TableField("title")
	@Schema(description = "标题")
	private String title;

	@TableField("activeMinLimit")
	@Schema(description = "激活最低金额")
	private Double activeMinLimit;

	@TableField("rechargeMinLimit")
	@Schema(description = "单次充值最低金额")
	private Double rechargeMinLimit;

	@TableField("rechargeMaxLimit")
	@Schema(description = "单次充值最高金额")
	private Double rechargeMaxLimit;

	@TableField("cardBin")
	@Schema(description = "卡BIN")
	private String cardBin;
	
	@TableField("open_card_cost")
	@Schema(description = "开卡费用")
	private Double openCardCost;
	
	@TableField("pre_save_cost")
	@Schema(description = "预存金额")
	private Double preSaveCost;
	
	@TableField("card_state")
	@Schema(description = "卡片状态")
	private Integer cardState;
	
	@TableField(exist = false)
	@Schema(description = "语言")
	private String language;
	
	@TableField(exist = false)
	@Schema(description = "标签列表")
	private List<CardLableEntity> lableList;
	
	@TableField(exist = false)
	@Schema(description = "标签ID列表")
	private List<Integer> lableIdList;
	
	@TableField(exist = false)
	@Schema(description = "简介信息")
	private CardSynopsisEntity synopsisData;
	
	@TableField(exist = false)
	@Schema(description = "简介ID列表")
	private List<Integer> synopsisIdList;
	
	@TableField(exist = false)
	@Schema(description = "简介请求列表")
	private List<CardSynopsisEntity> synopsisReq;
	
	@TableField(exist = false)
	@Schema(description = "卡组织品牌")
	private String cardBrand;

}
