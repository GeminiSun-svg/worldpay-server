package com.worldpay.query;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通用分页查询参数")
public class PublicQueryEntity extends PageQueryHelperEntity{
	
	@TableField(exist = false)
	@Schema(description = "开始时间")
	private String begTime;
	
	@TableField(exist = false)
	@Schema(description = "结束时间")
	private String endTime;
	
	@TableField(exist = false)
	@Schema(description = "状态")
	private String status;
	
	@TableField(exist = false)
	@Schema(description = "状态ID")
	private Integer statusId;

	@TableField(exist = false)
	@Schema(description = "开卡状态")
	private Integer openState;
	
	@TableField(exist = false)
	@Schema(description = "类型")
	private String type;

	@TableField(exist = false)
	@Schema(description = "用户标识")
	private String userId;

	@TableField(exist = false)
	@Schema(description = "用户ID列表")
	private List<String> uid;
	
	

}
