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
@Schema(description = "钱包流水")
public class WalletLogEntity extends PageQueryHelperEntity {
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
    
	@TableField("order_no")
	@Schema(description = "订单号")
	private String orderNo;
    
	@TableField("out_order_no")
	@Schema(description = "外部订单号")
	private String outOrderNo;
    
	@TableField("user_id")
	@Schema(description = "用户ID")
	private Integer userId;
    
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
    
	@TableField("trade_type")
	@Schema(description = "交易类型")
	private Integer tradeType;
    
	@TableField("title")
	@Schema(description = "交易标题")
	private String title;
    
	@TableField("primeval_money")
	@Schema(description = "原始金额")
	private Double primevalMoney;
    
	@TableField("primeval_money_unit")
	@Schema(description = "原始金额单位")
	private String primevalMoneyUnit;
    
	@TableField("real_money")
	@Schema(description = "实际金额")
	private BigDecimal realMoney;
    
	@TableField("service_charge")
	@Schema(description = "手续费")
	private BigDecimal serviceCharge;
    
	@TableField("form_name")
	@Schema(description = "付款方名称")
	private String formName;
    
	@TableField("form_account")
	@Schema(description = "付款方账户")
	private String formAccount;
    
	@TableField("to_name")
	@Schema(description = "收款方名称")
	private String toName;
    
	@TableField("to_account")
	@Schema(description = "收款方账户")
	private String toAccount;
    
	@TableField("memo")
	@Schema(description = "备注")
	private String memo;
    
	@TableField("user_bankcard_id")
	@Schema(description = "用户银行卡ID")
	private Integer userBankcardId;
    
	@TableField("operate_type")
	@Schema(description = "操作类型")
	private Integer operateType;
    
	@TableField("status")
	@Schema(description = "状态")
	private String status;
 
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
    
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
    
	@TableField("setUser")
	@Schema(description = "操作人ID")
	private Integer setUser;
	
	@TableField("setUserName")
	@Schema(description = "操作人名称")
	private String setUserName;
	
	@TableField("network_type")
	@Schema(description = "网络类型")
	private String networkType;
    
	@TableField("tran_hash")
	@Schema(description = "交易哈希")
	private String tranHash;

    // 新增字段：符合条件的realMoney总和，数据库中不存在
	@TableField(exist = false)
	@Schema(description = "实际金额合计")
	private BigDecimal realMoneySum;
    
	@TableField(exist = false)
	@Schema(description = "用户邮箱")
	private String userEmail;
    
	@TableField(exist = false)
	@Schema(description = "调整前金额")
	private BigDecimal oldMonery;
    
	@TableField(exist = false)
	@Schema(description = "总金额")
	private String totalMoney;
    
}
