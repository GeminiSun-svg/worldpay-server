package com.worldpay.entity.system;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "")
public class SysUserEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer id;
	
	@TableField("username")
	@Schema(description = "")
private String username;

	@TableField("tel")
	@Schema(description = "")
private String tel;
    
	@TableField("acctive")
	@Schema(description = "")
private String acctive;
    
	@TableField("head_pic")
	@Schema(description = "")
private String headPic;
    
	@TableField("user_state")
	@Schema(description = "")
private Integer userState;
	
	@TableField("password")
	@Schema(description = "")
private String password;
	
	@TableField("create_user")
	@Schema(description = "")
private Integer createUser;
	
	@TableField("create_time")
	@Schema(description = "")
private String createTime;
	
	@TableField("login_time")
	@Schema(description = "")
private Date loginTime;
	
	@TableField("google_secretkey")
	@Schema(description = "")
private String googleSecretkey;
	
	@TableField(exist = false)
	@Schema(description = "")
private String userStateNmae;
    
	@TableField(exist = false)
	@Schema(description = "")
private List<Integer> roleId;
    
	@TableField(exist = false)
	@Schema(description = "")
private List<SysUserPowerEntity> power;
	
	@TableField(exist = false)
	@Schema(description = "")
private List<SysRoleEntity> roleList;
    
	@TableField(exist = false)
	@Schema(description = "")
private List<String>  roles;

	@TableField(exist = false)
	@Schema(description = "")
private List<String>  permissions;

	@TableField(exist = false)
	private Integer signCount;
	
	@TableField(exist = false)
	private Integer adminRole;
	
	@TableField(exist = false)
	private Integer deleteState;
	
	@TableField(exist = false)
	private Integer deptId;
	
	@TableField(exist = false)
	private Integer manageId;
	
	@TableField(exist = false)
	private Integer googleCode;
	

	public SysUserEntity() {
		super();
	}


}

