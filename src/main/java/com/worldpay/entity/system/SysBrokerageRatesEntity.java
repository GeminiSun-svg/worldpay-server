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
@Schema(description = "佣金费率")
public class SysBrokerageRatesEntity extends PageQueryHelperEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("brokerage_rates")
	@Schema(description = "佣金费率")
	private BigDecimal brokerageRates;

	@TableField("brokerage_rates_type")
	@Schema(description = "佣金费率类型")
	private String brokerageRatesType;
	
	@TableField("remark_info")
	@Schema(description = "备注信息")
	private String remarkInfo;
	
	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
