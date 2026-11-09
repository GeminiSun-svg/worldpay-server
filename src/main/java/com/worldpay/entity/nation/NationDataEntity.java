package com.worldpay.entity.nation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("fa_country")
@Schema(description = "国家数据")
public class NationDataEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("parentId")
	@Schema(description = "父级ID")
	private Integer parentId;
	
	@TableField("cname")
	@Schema(description = "中文名称")
	private String cname;
	
	@TableField("ename")
	@Schema(description = "英文名称")
	private String ename;

}
