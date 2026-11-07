package com.worldpay.entity.system;

import java.math.BigDecimal;
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
@TableName("sys_brokerage_rates")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class SysBrokerageRatesEntity extends PageQueryHelperEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("brokerage_rates")
	@Schema(description = "")
private BigDecimal brokerageRates;

	@TableField("brokerage_rates_type")
	@Schema(description = "")
private String brokerageRatesType;
	
	@TableField("remark_info")
	@Schema(description = "")
private String remarkInfo;
	
	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
