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
 * 类描述：app用户与渠道商绑定关系
 *
 * @author GeminiSun
 * @date 2026/2/4 9:15
 */
@Data
@TableName("app_channel_intiter")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class AppChannelIntiterEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("app_id")
	@Schema(description = "")
private Integer appId;
	
	@TableField("app_uid")
	@Schema(description = "")
private String appUid;
	
	@TableField("channel_id")
	@Schema(description = "")
private Integer channelId;
	
	@TableField("channel_uid")
	@Schema(description = "")
private String channelUid;
	
	@TableField("invitation_code")
	@Schema(description = "")
private String invitationCode;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

	
}
