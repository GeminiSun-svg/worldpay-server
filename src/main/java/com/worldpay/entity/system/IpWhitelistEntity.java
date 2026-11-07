package com.worldpay.entity.system;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("sys_ip")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class IpWhitelistEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private String id;
	
	@TableField("ip_site")
	@Schema(description = "")
private String ipSite;
	
	@TableField("set_user")
	@Schema(description = "")
private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "")
private String setUserName;
	
	@TableField("set_time")
	@Schema(description = "")
private Date setTime;
	
	@TableField("gmt_modified")
	@Schema(description = "")
private Date gmtModified;

}
