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
@Schema(description = "系统配置")
public class SysInfoEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("config_type")
	@Schema(description = "配置类型")
	private Integer configType;
	
	@TableField("config_type_name")
	@Schema(description = "配置类型名称")
	private String configTypeName;
	
	@TableField("config_lable")
	@Schema(description = "配置标签")
	private Integer configLable;

    @TableField("language")
    @Schema(description = "语言编码")
	private String language;

	@TableField("config_name")
	@Schema(description = "配置名称")
	private String configName;

	@TableField("config_content")
	@Schema(description = "配置内容")
	private String configContent;
	
	@TableField("config_url")
	@Schema(description = "配置链接")
	private String configUrl;

	@TableField("status")
	@Schema(description = "状态")
	private Integer status;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
    @Schema(description = "语言名称")
	private String languageName;


}
