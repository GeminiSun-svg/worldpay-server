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
@Schema(description = "系统用户")
public class SysUserEntity extends PageQueryHelperEntity{

	@TableId(type = IdType.AUTO)
	@Schema(description = "主键ID")
	private Integer id;
	
	@TableField("username")
	@Schema(description = "用户名")
	private String username;

	@TableField("tel")
	@Schema(description = "手机号")
	private String tel;
    
	@TableField("acctive")
	@Schema(description = "登录账号")
	private String acctive;
    
	@TableField("head_pic")
	@Schema(description = "头像")
	private String headPic;
    
	@TableField("user_state")
	@Schema(description = "用户状态")
	private Integer userState;
	
	@TableField("password")
	@Schema(description = "密码")
	private String password;
	
	@TableField("create_user")
	@Schema(description = "创建人")
	private Integer createUser;
	
	@TableField("create_time")
	@Schema(description = "创建时间")
	private String createTime;
	
	@TableField("login_time")
	@Schema(description = "登录时间")
	private Date loginTime;
	
	@TableField("google_secretkey")
	@Schema(description = "谷歌验证密钥")
	private String googleSecretkey;
	
	@TableField(exist = false)
	@Schema(description = "用户状态名称")
	private String userStateNmae;
    
	@TableField(exist = false)
	@Schema(description = "角色ID")
	private List<Integer> roleId;
    
	@TableField(exist = false)
	@Schema(description = "权限列表")
	private List<SysUserPowerEntity> power;
	
	@TableField(exist = false)
	@Schema(description = "角色列表")
	private List<SysRoleEntity> roleList;
    
	@TableField(exist = false)
	@Schema(description = "角色编码列表")
	private List<String>  roles;

	@TableField(exist = false)
	@Schema(description = "权限标识列表")
	private List<String>  permissions;

	@TableField(exist = false)
	@Schema(description = "签到次数")
	private Integer signCount;
	
	@TableField(exist = false)
	@Schema(description = "管理员角色ID")
	private Integer adminRole;
	
	@TableField(exist = false)
	@Schema(description = "删除状态")
	private Integer deleteState;
	
	@TableField(exist = false)
	@Schema(description = "部门ID")
	private Integer deptId;
	
	@TableField(exist = false)
	@Schema(description = "管理机构ID")
	private Integer manageId;
	
	@TableField(exist = false)
	@Schema(description = "谷歌验证码")
	private Integer googleCode;
	

	public SysUserEntity() {
		super();
	}


}

