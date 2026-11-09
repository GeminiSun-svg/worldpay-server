package com.worldpay.entity.card;

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
@TableName("card_banner")
@Schema(description = "卡片轮播图")
public class CardBannerEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

    @TableField("language")
    @Schema(description = "语言")
	private String language;

	@TableField("title")
	@Schema(description = "标题")
	private String title;

	@TableField("img_url")
	@Schema(description = "图片地址")
	private String imgUrl;

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
