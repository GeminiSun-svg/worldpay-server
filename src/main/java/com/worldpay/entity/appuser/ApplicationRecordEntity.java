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
@Schema(description = "渠道商申请记录")
public class ApplicationRecordEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;

    @TableField("app_id")
    @Schema(description = "应用ID")
	private Integer appId;

    @TableField("app_uid")
    @Schema(description = "用户ID")
	private String appUid;

    @TableField("app_phone")
    @Schema(description = "用户手机号")
	private String appPhone;

    @TableField("app_email")
    @Schema(description = "用户邮箱")
	private String appEmail;

    @TableField("channel_id")
    @Schema(description = "渠道ID")
	private Integer channelId;

    @TableField("channel_uid")
    @Schema(description = "渠道用户ID")
	private String channelUid;

    @TableField("type")
    @Schema(description = "申请类型")
	private Integer type;

    @TableField("status")
    @Schema(description = "审核状态")
	private Integer status;

    @TableField("setTime")
    @Schema(description = "创建时间")
	private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")
	private Date gmtModified;

}
