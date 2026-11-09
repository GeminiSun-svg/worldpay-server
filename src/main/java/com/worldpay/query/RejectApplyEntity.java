package com.worldpay.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "驳回申请参数")
public class RejectApplyEntity {
	
	@Schema(description = "申请记录ID")
	private Integer id;
	
	@Schema(description = "驳回原因")
	private String rejectInfo;
	
	@Schema(description = "驳回操作人ID")
	private Integer rejectUser;
	
	@Schema(description = "驳回操作人姓名")
	private String rejectUserName;

}
