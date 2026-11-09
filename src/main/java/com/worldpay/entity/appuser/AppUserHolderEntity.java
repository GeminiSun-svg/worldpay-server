package com.worldpay.entity.appuser;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("app_user_holder")
@Schema(description = "持卡人信息")

public class AppUserHolderEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")

    private Integer id;
	
	@TableField("uid")
	@NotBlank(message = "用户id不能为空")
	@Schema(description = "用户ID")

	private String uid;
	
	@TableField("user_name")
	@Schema(description = "名字")

	private String userName;
	
	@TableField("user_surname")
	@Schema(description = "姓氏")

	private String userSurname;
	
	@TableField("user_tel_dial_code")
	@Schema(description = "手机区号")

	private String userTelDialCode;
	
	@TableField("user_tel_code")
	@Schema(description = "手机国家代码")

	private String userTelCode;
	
	@TableField("user_tel")
	@Schema(description = "手机号")

	private String userTel;
	
	@TableField("user_email")
	@Schema(description = "邮箱")

	private String userEmail;
	
	@TableField("user_number")
	@Schema(description = "证件号码")

	private String userNumber;
	
	@TableField("user_sex")
	@Schema(description = "性别")

	private String userSex;
	
	@TableField("user_sex_num")
	@Schema(description = "性别编号")

	private Integer userSexNum;
	
	@TableField("user_address")
	@Schema(description = "地址")

	private String userAddress;
	
	@TableField("user_birthday")
	@Schema(description = "出生日期")

	private String userBirthday;
	
	@TableField("setTime")
	@Schema(description = "创建时间")

    private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "修改时间")

	private Date gmtModified;
	
	

}
