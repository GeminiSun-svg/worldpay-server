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
@Schema(description = "用户链上地址")
public class AppUserWebAddressEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;
	
	@TableField("user_email")
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@TableField("tron_address")
	@Schema(description = "TRON地址")
	private String tronAddress;
	
	@TableField("eth_address")
	@Schema(description = "ETH地址")
	private String ethAddress;
	
	@TableField("bnb_address")
	@Schema(description = "BNB地址")
	private String bnbAddress;
	
	@TableField("btc_address")
	@Schema(description = "BTC地址")
	private String btcAddress;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
}
