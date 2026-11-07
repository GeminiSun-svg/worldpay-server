package com.worldpay.service;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysRoleEntity;


import io.swagger.v3.oas.annotations.Operation;
/**
 *  @category 角色接口
 */

public interface RoleService {

ResponseBase findAll(SysRoleEntity entity);

ResponseBase findById(Integer roleId);

ResponseBase add(SysRoleEntity entity);

ResponseBase update(SysRoleEntity entity);

ResponseBase delete(Integer roleId);

ResponseBase changeStatus(Integer roleId,String status);

ResponseBase findNormalRole();
	
	
}
