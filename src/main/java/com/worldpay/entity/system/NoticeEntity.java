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
@Schema(description = "公告消息")
public class NoticeEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("uid")
	@Schema(description = "用户ID")
	private String uid;

	@TableField("language")
	@Schema(description = "语言编码")
	private String language;
    
	@TableField("language_name")
	@Schema(description = "语言名称")
	private String languageName;

	@TableField("title")
	@Schema(description = "标题")
	private String title;

	@TableField("intro")
	@Schema(description = "简介")
	private String intro;

	@TableField("content")
	@Schema(description = "内容")
	private String content;

	@TableField("type")
	@Schema(description = "类型")
	private Integer type;
    
	@TableField("type_name")
	@Schema(description = "类型名称")
	private String typeName;

	@TableField("status")
	@Schema(description = "状态")
	private Integer status;
	
	@TableField("status_name")
	@Schema(description = "状态名称")
	private String statusName;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@TableField(exist = false)
	@Schema(description = "用户信息")
	private AppUserEntity userData;


}
