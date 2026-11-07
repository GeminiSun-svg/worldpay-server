package com.worldpay.entity.wallet;

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
@TableName("wallet_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class WalletLogEntity extends PageQueryHelperEntity {
    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;
    
    @TableField("order_no")
    @Schema(description = "")
private String orderNo;
    
    @TableField("out_order_no")
    @Schema(description = "")
private String outOrderNo;
    
    @TableField("user_id")
    @Schema(description = "")
private Integer userId;
    
    @TableField("uid")
    @Schema(description = "")
private String uid;
    
    @TableField("trade_type")
    @Schema(description = "")
private Integer tradeType;
    
    @TableField("title")
    @Schema(description = "")
private String title;
    
    @TableField("primeval_money")
    @Schema(description = "")
private Double primevalMoney;
    
    @TableField("primeval_money_unit")
    @Schema(description = "")
private String primevalMoneyUnit;
    
    @TableField("real_money")
    @Schema(description = "")
private BigDecimal realMoney;
    
    @TableField("service_charge")
    @Schema(description = "")
private BigDecimal serviceCharge;
    
    @TableField("form_name")
    @Schema(description = "")
private String formName;
    
    @TableField("form_account")
    @Schema(description = "")
private String formAccount;
    
    @TableField("to_name")
    @Schema(description = "")
private String toName;
    
    @TableField("to_account")
    @Schema(description = "")
private String toAccount;
    
    @TableField("memo")
    @Schema(description = "")
private String memo;
    
    @TableField("user_bankcard_id")
	@Schema(description = "")
private Integer userBankcardId;
    
    @TableField("operate_type")
	@Schema(description = "")
private Integer operateType;
    
    @TableField("status")
    @Schema(description = "")
private String status;
 
    @TableField("setTime")
    @Schema(description = "")
private Date setTime;
    
    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;
    
	@TableField("setUser")
	@Schema(description = "")
private Integer setUser;
	
	@TableField("setUserName")
	@Schema(description = "")
private String setUserName;
	
    @TableField("network_type")
    @Schema(description = "")
private String networkType;
    
	@TableField("tran_hash")
	@Schema(description = "")
private String tranHash;

    // 新增字段：符合条件的realMoney总和，数据库中不存在
    @TableField(exist = false)
    @Schema(description = "")
private BigDecimal realMoneySum;
    
    @TableField(exist = false)
    @Schema(description = "")
private String userEmail;
    
    @TableField(exist = false)
    @Schema(description = "")
private BigDecimal oldMonery;
    
    @TableField(exist = false)
    @Schema(description = "")
private String totalMoney;
    
}
