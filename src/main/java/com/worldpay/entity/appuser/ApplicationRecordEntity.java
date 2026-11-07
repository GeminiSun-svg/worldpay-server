package com.worldpay.entity.appuser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：用户申请成为渠道商申请记录
 *
 * @author GeminiSun
 * @date 2026/2/4 9:25
 */
@Data
@TableName("application_record")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class ApplicationRecordEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("app_id")
    @Schema(description = "")
private Integer appId;

    @TableField("app_uid")
    @Schema(description = "")
private String appUid;

    @TableField("app_phone")
    @Schema(description = "")
private String appPhone;

    @TableField("app_email")
    @Schema(description = "")
private String appEmail;

    @TableField("channel_id")
    @Schema(description = "")
private Integer channelId;

    @TableField("channel_uid")
    @Schema(description = "")
private String channelUid;

    @TableField("type")
    @Schema(description = "")
private Integer type;

    @TableField("status")
    @Schema(description = "")
private Integer status;

    @TableField("setTime")
    @Schema(description = "")
private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")
private Date gmtModified;

}
