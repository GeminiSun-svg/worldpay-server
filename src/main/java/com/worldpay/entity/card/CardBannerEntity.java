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
@Schema(description = "")
public class CardBannerEntity extends PageQueryHelperEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

    @TableField("language")
    @Schema(description = "")
private String language;

	@TableField("title")
	@Schema(description = "")
private String title;

	@TableField("img_url")
	@Schema(description = "")
private String imgUrl;

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
