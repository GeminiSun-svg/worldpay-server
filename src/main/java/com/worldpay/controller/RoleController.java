package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysRoleEntity;
import com.worldpay.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/role")
@Tag(name = "Role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/all")
    @Operation(summary = "查询角色列表")
    public ResponseBase findAll(SysRoleEntity entity) {
        return roleService.findAll(entity);
    }

    @GetMapping("/{roleId}")
    @Operation(summary = "查询角色详情")
    public ResponseBase findById(@PathVariable Integer roleId) {
        return roleService.findById(roleId);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public ResponseBase add(SysRoleEntity entity) {
        return roleService.add(entity);
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public ResponseBase update(SysRoleEntity entity) {
        return roleService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除角色")
    public ResponseBase delete(Integer roleId) {
        return roleService.delete(roleId);
    }

    @PostMapping("/change-status")
    @Operation(summary = "变更状态")
    public ResponseBase changeStatus(Integer roleId,String status) {
        return roleService.changeStatus(roleId, status);
    }

    @GetMapping("/normal-role")
    @Operation(summary = "查询正常角色列表")
    public ResponseBase findNormalRole() {
        return roleService.findNormalRole();
    }
}
