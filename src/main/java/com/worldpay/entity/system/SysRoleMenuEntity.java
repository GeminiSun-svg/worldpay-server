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
@Schema(description = "")
public class SysRoleMenuEntity {
	
    @TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;
    
    @TableField("role_id")
    @Schema(description = "")
private Integer roleId;
    
    @TableField("menu_id")
    @Schema(description = "")
private Integer menuId;
    
    @TableField("create_user")
    @Schema(description = "")
private Integer createUser;
    
    @TableField("create_time")
    @Schema(description = "")
private Date createTime;
}
