package com.worldpay.entity.system;

import java.util.Date;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(description = "角色")
public class SysRoleEntity extends BaseEntity  {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@Schema(description = "角色ID")
	private Integer roleId;

	@TableField("role_name")
	@Schema(description = "角色名称")
	private String roleName;

	@TableField("role_key")
	@Schema(description = "权限字符")
	private String roleKey;

	@TableField("role_sort")
	@Schema(description = "排序")
	private Integer roleSort;

	@TableField("data_scope")
	@Schema(description = "数据范围")
	private String dataScope;

	@TableField("menu_check_strictly")
	@Schema(description = "菜单树是否父子联动")
	private boolean menuCheckStrictly;

	@TableField("dept_check_strictly")
	@Schema(description = "部门树是否父子联动")
	private boolean deptCheckStrictly;

	@TableField("status")
	@Schema(description = "状态")
	private String status;

	@TableField("del_flag")
	@Schema(description = "删除标记")
	private String delFlag;

	@TableField("create_by")
	@Schema(description = "创建人")
	private String createBy;

	@TableField("create_time")
	@Schema(description = "创建时间")
	private Date createTime;

	@TableField("update_by")
	@Schema(description = "更新人")
	private String updateBy;

	@TableField("update_time")
	@Schema(description = "修改时间")
	private Date updateTime;
	
	@TableField("remark")
	@Schema(description = "备注")
	private String remark;
	
	@TableField(exist = false)
	@Schema(description = "页码")
	private Integer pageNum;
	
	@TableField(exist = false)
	@Schema(description = "每页条数")
	private Integer pageSize;

	@TableField(exist = false)
	@Schema(description = "标识")
	private boolean flag = false;

	@TableField(exist = false)
	@Schema(description = "菜单ID集合")
	private Integer[] menuIds;

	@TableField(exist = false)
	@Schema(description = "部门ID集合")
	private Long[] deptIds;

	@TableField(exist = false)
	@Schema(description = "权限集合")
	private Set<String> permissions;
	

	public SysRoleEntity() {

	}

	public SysRoleEntity(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Integer roleId) {
		return roleId != null && 1L == roleId;
	}

	@NotBlank(message = "角色名称不能为空")
	@Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@NotBlank(message = "权限字符不能为空")
	@Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getRoleSort() {
		return roleSort;
	}

	public void setRoleSort(Integer roleSort) {
		this.roleSort = roleSort;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public boolean isMenuCheckStrictly() {
		return menuCheckStrictly;
	}

	public void setMenuCheckStrictly(boolean menuCheckStrictly) {
		this.menuCheckStrictly = menuCheckStrictly;
	}

	public boolean isDeptCheckStrictly() {
		return deptCheckStrictly;
	}

	public void setDeptCheckStrictly(boolean deptCheckStrictly) {
		this.deptCheckStrictly = deptCheckStrictly;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Integer[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Integer[] menuIds) {
		this.menuIds = menuIds;
	}

	public Long[] getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(Long[] deptIds) {
		this.deptIds = deptIds;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("roleId", getRoleId())
				.append("roleName", getRoleName()).append("roleKey", getRoleKey()).append("roleSort", getRoleSort())
				.append("dataScope", getDataScope()).append("menuCheckStrictly", isMenuCheckStrictly())
				.append("deptCheckStrictly", isDeptCheckStrictly()).append("status", getStatus())
				.append("delFlag", getDelFlag()).append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
