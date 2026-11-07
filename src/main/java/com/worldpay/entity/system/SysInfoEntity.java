package com.worldpay.entity.system;

import java.util.Date;
import com.worldpay.query.PageQueryHelperEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_info")
@Schema(description = "")
public class SysInfoEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("config_type")
	@Schema(description = "")
private Integer configType;
	
	@TableField("config_type_name")
	@Schema(description = "")
private String configTypeName;
	
	@TableField("config_lable")
	@Schema(description = "")
private Integer configLable;

    @TableField("language")
    @Schema(description = "")
private String language;

	@TableField("config_name")
	@Schema(description = "")
private String configName;

	@TableField("config_content")
	@Schema(description = "")
private String configContent;
	
	@TableField("config_url")
	@Schema(description = "")
private String configUrl;

	@TableField("status")
	@Schema(description = "")
private Integer status;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
    @Schema(description = "")
private String languageName;


}
