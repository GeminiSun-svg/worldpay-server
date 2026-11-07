package com.worldpay.entity.system;

import java.util.Date;

import com.worldpay.entity.appuser.AppUserEntity;
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
@TableName("msg_notice")
@Schema(description = "")
public class NoticeEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("uid")
	@Schema(description = "")
private String uid;

    @TableField("language")
    @Schema(description = "")
private String language;
    
    @TableField("language_name")
    @Schema(description = "")
private String languageName;

	@TableField("title")
	@Schema(description = "")
private String title;

	@TableField("intro")
	@Schema(description = "")
private String intro;

    @TableField("content")
    @Schema(description = "")
private String content;

    @TableField("type")
    @Schema(description = "")
private Integer type;
    
    @TableField("type_name")
    @Schema(description = "")
private String typeName;

	@TableField("status")
	@Schema(description = "")
private Integer status;
	
	@TableField("status_name")
	@Schema(description = "")
private String statusName;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "")
private AppUserEntity userData;


}
