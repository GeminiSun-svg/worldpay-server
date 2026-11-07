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
@Schema(description = "")
public class CardApplyEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("holder_id")
	@Schema(description = "")
private Integer holderId;
	
	@TableField("topup_type")
	@Schema(description = "")
private Integer topupType;
	
	@TableField("card_id")
	@Schema(description = "")
private Integer cardId;
	
	@TableField("card_uuid")
	@Schema(description = "")
private String cardUuid;
	
	@TableField("card_type")
	@Schema(description = "")
private String cardType;
	
	@TableField("apply_state")
	@Schema(description = "")
private Integer applyState;
	
	@TableField("apply_state_name")
	@Schema(description = "")
private String applyStateName;
	
	@TableField("kyc_state")
	@Schema(description = "")
private Integer kycState;
	
	@TableField("kyc_state_name")
	@Schema(description = "")
private String kycStateName;
	
	@TableField("kyc_audit_result")
	@Schema(description = "")
private String kycAuditResult;
	
	@TableField("month_fee")
	@Schema(description = "")
private Integer monthFee;
	
	@TableField("open_card_cost")
	@Schema(description = "")
private Double openCardCost;
	
	@TableField("pre_save_cost")
	@Schema(description = "")
private Double preSaveCost;
	
	@TableField("open_card_total")
	@Schema(description = "")
private Double openCardTotal;
	
	@TableField("reject_info")
	@Schema(description = "")
private String rejectInfo;
	
	@TableField("reject_user")
	@Schema(description = "")
private Integer rejectUser;
	
	@TableField("reject_user_name")
	@Schema(description = "")
private String rejectUserName;
	
	@TableField("shipping_state")
	@Schema(description = "")
private Integer shippingState;
	
	@TableField("shipping_state_name")
	@Schema(description = "")
private String shippingStateName;
	
	@TableField("shipping_time")
	@Schema(description = "")
private Date shippingTime;
	
	@TableField("logistics_num")
	@Schema(description = "")
private String logisticsNum;
	
	@TableField("logistics_monery")
	@Schema(description = "")
private Double logisticsMonery;
	
	@TableField("port_type")
	@Schema(description = "")
private Integer portType;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardEntity cardData;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardApplyManEntity applyManData;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardApplySendEntity applySendData;
	
	@TableField(exist = false)
	@Schema(description = "")
private CardApplyKycEntity kycData;
	
	@TableField(exist = false)
	@Schema(description = "")
private AppUserCardEntity userCardData;
	
	@TableField(exist = false)
	@Schema(description = "")
private Object logisticsInfo;
	
	@TableField(exist = false)
	@Schema(description = "")
private String cardNo;


}
