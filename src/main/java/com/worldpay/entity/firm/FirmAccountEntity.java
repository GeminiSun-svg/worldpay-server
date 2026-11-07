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
@Schema(description = "")
public class FirmAccountEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("opening_bank")
	@Schema(description = "")
private String openingBank;
	
	@TableField("payee_man")
	@Schema(description = "")
private String payeeMan;
	
	@TableField("payee_account")
	@Schema(description = "")
private String payeeAccount;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
