package com.worldpay.entity.firm;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("firm_account")
@Schema(description = "公司账户")
public class FirmAccountEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("opening_bank")
	@Schema(description = "开户银行")
	private String openingBank;
	
	@TableField("payee_man")
	@Schema(description = "收款人姓名")
	private String payeeMan;
	
	@TableField("payee_account")
	@Schema(description = "收款账户")
	private String payeeAccount;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
