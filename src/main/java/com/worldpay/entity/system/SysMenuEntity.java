package com.worldpay.entity.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * 菜单权限表 sys_menu
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@Schema(description = "")
public class SysMenuEntity extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@Schema(description = "")
private Integer menuId;

	@TableField("menu_name")
	@Schema(description = "")
private String menuName;

	@TableField(exist = false)
	@Schema(description = "")
private String parentName;

	@TableField("parent_id")
	@Schema(description = "")
private Integer parentId;

	@TableField("order_num")
	@Schema(description = "")
private Integer orderNum;

	@TableField("path")
	@Schema(description = "")
private String path;

	@TableField("component")
	@Schema(description = "")
private String component;

	@TableField("query")
	@Schema(description = "")
private String query;

	@TableField("is_frame")
	@Schema(description = "")
private String isFrame;

	@TableField("is_cache")
	@Schema(description = "")
private String isCache;

	@TableField("menu_type")
	@Schema(description = "")
private String menuType;

	@TableField("visible")
	@Schema(description = "")
private String visible;

	@TableField("status")
	@Schema(description = "")
private String status;

	@TableField("perms")
	@Schema(description = "")
private String perms;

	@TableField("icon")
	@Schema(description = "")
private String icon;

	@TableField("create_by")
	@Schema(description = "")
private String createBy;

	@TableField("create_time")
	@Schema(description = "")
private Date createTime;

	@TableField("update_by")
	@Schema(description = "")
private String updateBy;

	@TableField("update_time")
	@Schema(description = "")
private Date updateTime;

	@TableField("remark")
	@Schema(description = "")
private String remark;

	@TableField(exist = false)
	@Schema(description = "")
private List<SysMenuEntity> children = new ArrayList<SysMenuEntity>();

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@NotBlank(message = "菜单名称不能为空")
	@Size(min = 0, max = 50, message = "菜单名称长度不能超过50个字符")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getIsFrame() {
		return isFrame;
	}

	public void setIsFrame(String isFrame) {
		this.isFrame = isFrame;
	}

	public String getIsCache() {
		return isCache;
	}

	public void setIsCache(String isCache) {
		this.isCache = isCache;
	}

	@NotBlank(message = "菜单类型不能为空")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<SysMenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenuEntity> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("menuId", getMenuId())
				.append("menuName", getMenuName()).append("parentId", getParentId()).append("orderNum", getOrderNum())
				.append("path", getPath()).append("component", getComponent()).append("isFrame", getIsFrame())
				.append("IsCache", getIsCache()).append("menuType", getMenuType()).append("visible", getVisible())
				.append("status ", getStatus()).append("perms", getPerms()).append("icon", getIcon())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.toString();
	}
}
