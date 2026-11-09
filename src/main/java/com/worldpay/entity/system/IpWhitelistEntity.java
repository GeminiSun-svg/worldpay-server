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
@Schema(description = "IP白名单")
public class IpWhitelistEntity extends PageQueryHelperEntity{
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private String id;
	
	@TableField("ip_site")
	@Schema(description = "IP地址")
	private String ipSite;
	
	@TableField("set_user")
	@Schema(description = "设置人ID")
	private Integer setUser;
	
	@TableField("set_user_name")
	@Schema(description = "设置人名称")
	private String setUserName;
	
	@TableField("set_time")
	@Schema(description = "创建时间")
	private Date setTime;
	
	@TableField("gmt_modified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
