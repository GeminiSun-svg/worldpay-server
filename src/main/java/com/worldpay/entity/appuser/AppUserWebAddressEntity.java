package com.worldpay.entity.appuser;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_user_webaddress")
@Schema(description = "")
public class AppUserWebAddressEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;
	
	@TableField("user_email")
	@Schema(description = "")
private String userEmail;
	
	@TableField("tron_address")
	@Schema(description = "")
private String tronAddress;
	
	@TableField("eth_address")
	@Schema(description = "")
private String ethAddress;
	
	@TableField("bnb_address")
	@Schema(description = "")
private String bnbAddress;
	
	@TableField("btc_address")
	@Schema(description = "")
private String btcAddress;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
}
