package com.worldpay.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 角色菜单关联
 */
@Data
@TableName("sys_role_menu")
@Schema(description = "角色菜单关联")
public class SysRoleMenuEntity {
	
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
	private Integer id;
    
    @TableField("role_id")
    @Schema(description = "角色ID")
	private Integer roleId;
    
    @TableField("menu_id")
    @Schema(description = "菜单ID")
	private Integer menuId;
    
    @TableField("create_user")
    @Schema(description = "创建人")
	private Integer createUser;
    
    @TableField("create_time")
    @Schema(description = "创建时间")
	private Date createTime;
}
