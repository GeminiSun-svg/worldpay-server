package com.worldpay.entity.system;
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
@TableName("toll_address")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class TollAddressEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("address_type")
	@Schema(description = "")
private String addressType;
	
	@TableField("address_site")
	@Schema(description = "")
private String addressSite;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	

}
