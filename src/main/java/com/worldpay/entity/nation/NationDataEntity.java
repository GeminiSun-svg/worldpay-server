package com.worldpay.entity.nation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("fa_country")
@Schema(description = "")
public class NationDataEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("parentId")
	@Schema(description = "")
private Integer parentId;
	
	@TableField("cname")
	@Schema(description = "")
private String cname;
	
	@TableField("ename")
	@Schema(description = "")
private String ename;

}
