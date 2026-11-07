package com.worldpay.entity.system;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@TableName("sys_user_role")
@Schema(description = "")
public class SysUserRoleEntity {

	@TableId(type = IdType.AUTO)
    @Schema(description = "")
private Integer id;

    @TableField("user_id")
    @Schema(description = "")
private Integer userId;
    
    @TableField("role_id")
    @Schema(description = "")
private Integer roleId;

    @TableField("create_user")
    @Schema(description = "")
private Integer createUser;
    
    @TableField("create_time")
    @Schema(description = "")
private Date createTime;
	
}
