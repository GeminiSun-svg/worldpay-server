package com.worldpay.entity.forecast;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：轮播图
 *
 * @author GeminiSun
 * @date 2026/3/26 10:53
 */
@Data
@TableName("forecast_slideshow")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "预测轮播图")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastSlideshowEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;

    @TableField("language")
    @Schema(description = "语言")
	private String language;

    @TableField("img_url")
    @Schema(description = "图片地址")
	private String imgUrl;

    @TableField("type")
    @Schema(description = "轮播图类型")
	private Integer type;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;


}
