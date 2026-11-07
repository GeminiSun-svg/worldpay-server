package com.worldpay.query;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class PublicQueryEntity extends PageQueryHelperEntity{
	
	@TableField(exist = false)
	@Schema(description = "")
private String begTime;
	
	@TableField(exist = false)
	@Schema(description = "")
private String endTime;
	
	@TableField(exist = false)
	@Schema(description = "")
private String status;
	
	@TableField(exist = false)
	@Schema(description = "")
private Integer statusId;

	@TableField(exist = false)
	@Schema(description = "")
private Integer openState;
	
	@TableField(exist = false)
	@Schema(description = "")
private String type;

	@TableField(exist = false)
	@Schema(description = "")
private String userId;

	@TableField(exist = false)
	@Schema(description = "")
private List<String> uid;
	
	

}
