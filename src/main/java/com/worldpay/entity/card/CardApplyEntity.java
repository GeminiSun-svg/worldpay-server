package com.worldpay.entity.card;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("card_apply")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "卡片申请记录")
public class CardApplyEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("holder_id")
	@Schema(description = "持卡人ID")
	private Integer holderId;
	
	@TableField("topup_type")
	@Schema(description = "充值类型")
	private Integer topupType;
	
	@TableField("card_id")
	@Schema(description = "卡片ID")
	private Integer cardId;
	
	@TableField("card_uuid")
	@Schema(description = "卡片唯一标识")
	private String cardUuid;
	
	@TableField("card_type")
	@Schema(description = "卡片类型")
	private String cardType;
	
	@TableField("apply_state")
	@Schema(description = "申请状态")
	private Integer applyState;
	
	@TableField("apply_state_name")
	@Schema(description = "申请状态名称")
	private String applyStateName;
	
	@TableField("kyc_state")
	@Schema(description = "实名认证状态")
	private Integer kycState;
	
	@TableField("kyc_state_name")
	@Schema(description = "实名认证状态名称")
	private String kycStateName;
	
	@TableField("kyc_audit_result")
	@Schema(description = "实名审核结果")
	private String kycAuditResult;
	
	@TableField("month_fee")
	@Schema(description = "月费")
	private Integer monthFee;
	
	@TableField("open_card_cost")
	@Schema(description = "开卡费用")
	private Double openCardCost;
	
	@TableField("pre_save_cost")
	@Schema(description = "预存金额")
	private Double preSaveCost;
	
	@TableField("open_card_total")
	@Schema(description = "开卡总费用")
	private Double openCardTotal;
	
	@TableField("reject_info")
	@Schema(description = "驳回原因")
	private String rejectInfo;
	
	@TableField("reject_user")
	@Schema(description = "驳回人ID")
	private Integer rejectUser;
	
	@TableField("reject_user_name")
	@Schema(description = "驳回人名称")
	private String rejectUserName;
	
	@TableField("shipping_state")
	@Schema(description = "邮寄状态")
	private Integer shippingState;
	
	@TableField("shipping_state_name")
	@Schema(description = "邮寄状态名称")
	private String shippingStateName;
	
	@TableField("shipping_time")
	@Schema(description = "邮寄时间")
	private Date shippingTime;
	
	@TableField("logistics_num")
	@Schema(description = "物流单号")
	private String logisticsNum;
	
	@TableField("logistics_monery")
	@Schema(description = "物流费用")
	private Double logisticsMonery;
	
	@TableField("port_type")
	@Schema(description = "申请端口类型")
	private Integer portType;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "卡片信息")
	private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "申请人信息")
	private CardApplyManEntity applyManData;
	
	@TableField(exist = false)
	@Schema(description = "邮寄信息")
	private CardApplySendEntity applySendData;
	
	@TableField(exist = false)
	@Schema(description = "实名认证信息")
	private CardApplyKycEntity kycData;
	
	@TableField(exist = false)
	@Schema(description = "用户卡片信息")
	private AppUserCardEntity userCardData;
	
	@TableField(exist = false)
	@Schema(description = "物流信息")
	private Object logisticsInfo;
	
	@TableField(exist = false)
	@Schema(description = "卡号")
	private String cardNo;


}
