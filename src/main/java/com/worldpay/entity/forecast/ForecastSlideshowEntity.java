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
@Schema(description = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastSlideshowEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("language")
    @Schema(description = "")
private String language;

    @TableField("img_url")
    @Schema(description = "")
private String imgUrl;

    @TableField("type")
    @Schema(description = "")
private Integer type;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;


}
