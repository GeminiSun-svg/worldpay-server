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
	@Schema(description = "")
private Integer id;

    @TableField("parentId")
    @Schema(description = "")
private String parentId;
    
    @TableField("cname")
    @Schema(description = "")
private String cname;
    
    @TableField("ename")
    @Schema(description = "")
private String ename;
    
    @TableField("tname")
    @Schema(description = "")
private String tname;
    
    @TableField(exist = false)
    @Schema(description = "")
private String name;

}
