package com.worldpay.service;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysMenuEntity;


import io.swagger.v3.oas.annotations.Operation;
/**
 * @category 菜单管理接口
 *
 */

public interface MenuService {

ResponseBase findByUser(Integer userId);

ResponseBase findAll(SysMenuEntity entity);

ResponseBase treeselect(SysMenuEntity entity);

ResponseBase roleMenuTreeselect(Integer roleId);

ResponseBase add(SysMenuEntity entity);

ResponseBase findById(Integer menuId);

ResponseBase update(SysMenuEntity entity);

ResponseBase delete(Integer menuId);
	
}
