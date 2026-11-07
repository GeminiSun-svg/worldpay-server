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
@Schema(description = "")

public class AppUserHolderEntity {
	
	@TableId(type = IdType.AUTO)
	@Schema(description = "")

    private Integer id;
	
	@TableField("uid")
	@NotBlank(message = "用户id不能为空")
	@Schema(description = "")

	private String uid;
	
	@TableField("user_name")
	@Schema(description = "")

	private String userName;
	
	@TableField("user_surname")
	@Schema(description = "")

	private String userSurname;
	
	@TableField("user_tel_dial_code")
	@Schema(description = "")

	private String userTelDialCode;
	
	@TableField("user_tel_code")
	@Schema(description = "")

	private String userTelCode;
	
	@TableField("user_tel")
	@Schema(description = "")

	private String userTel;
	
	@TableField("user_email")
	@Schema(description = "")

	private String userEmail;
	
	@TableField("user_number")
	@Schema(description = "")

	private String userNumber;
	
	@TableField("user_sex")
	@Schema(description = "")

	private String userSex;
	
	@TableField("user_sex_num")
	@Schema(description = "")

	private Integer userSexNum;
	
	@TableField("user_address")
	@Schema(description = "")

	private String userAddress;
	
	@TableField("user_birthday")
	@Schema(description = "")

	private String userBirthday;
	
	@TableField("setTime")
	@Schema(description = "")

    private Date setTime;
	
	@TableField("gmtModified")
	@Schema(description = "")

	private Date gmtModified;
	
	

}
