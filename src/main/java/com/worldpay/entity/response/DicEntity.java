package com.worldpay.entity.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "字典数据")
public class DicEntity {
	
	@Schema(description = "主键ID")
	private Integer id;
	
	@Schema(description = "名称")
	private String name;
	
	@Schema(description = "值")
	private String value;
	
	@Schema(description = "标签")
	private String lable;

}
