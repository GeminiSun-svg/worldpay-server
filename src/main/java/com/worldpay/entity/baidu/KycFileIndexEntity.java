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
public class KycFileIndexEntity {

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;

	@TableField("config_key")
	@Schema(description = "")
private String configKey;

	@TableField("config_value")
	@Schema(description = "")
private Integer configValue;
	
	@TableField("config_info")
	@Schema(description = "")
private String configInfo;

	@TableField("setTime")
	@Schema(description = "")
private Date setTime;

	@TableField("gmtModified")
	@Schema(description = "")
private Date gmtModified;

}
