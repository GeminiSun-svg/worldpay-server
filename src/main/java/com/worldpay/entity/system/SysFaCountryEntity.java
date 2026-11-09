package com.worldpay.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 国家省市级联
 * @author Hlin
 *
 */
@Data
@TableName("sys_fa_country")
public class SysFaCountryEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

    @TableField("parentId")
    @Schema(description = "父级ID")
	private String parentId;
    
    @TableField("cname")
    @Schema(description = "中文名称")
	private String cname;
    
    @TableField("ename")
    @Schema(description = "英文名称")
	private String ename;
    
    @TableField("tname")
    @Schema(description = "繁体名称")
	private String tname;
    
    @TableField(exist = false)
    @Schema(description = "名称")
	private String name;

}
