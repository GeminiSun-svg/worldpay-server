package com.worldpay.entity.baidu;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category kyc文件索引
 * @author Hlin
 *
 */
@Data
@TableName("kyc_file_index")
@Schema(description = "KYC文件索引")
public class KycFileIndexEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;

	@TableField("config_key")
	@Schema(description = "配置键")
	private String configKey;

	@TableField("config_value")
	@Schema(description = "配置值")
	private Integer configValue;
	
	@TableField("config_info")
	@Schema(description = "配置信息")
	private String configInfo;

	@TableField("setTime")
	@Schema(description = "创建时间")
	private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "修改时间")
	private Date gmtModified;

}
