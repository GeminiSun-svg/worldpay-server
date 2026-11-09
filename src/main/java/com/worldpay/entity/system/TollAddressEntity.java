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
@Schema(description = "收费地址")
public class TollAddressEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("address_type")
	@Schema(description = "地址类型")
	private String addressType;
	
	@TableField("address_site")
	@Schema(description = "地址")
	private String addressSite;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	

}
