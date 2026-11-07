package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysMenuEntity;
import com.worldpay.service.MenuService;
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
@RequestMapping("/card/api/menu")
@Tag(name = "Menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/by-user")
    @Operation(summary = "查询按用户")
    public ResponseBase findByUser(Integer userId) {
        return menuService.findByUser(userId);
    }

    @PostMapping("/all")
    @Operation(summary = "查询菜单列表")
    public ResponseBase findAll(SysMenuEntity entity) {
        return menuService.findAll(entity);
    }

    @PostMapping("/treeselect")
    @Operation(summary = "查询菜单树")
    public ResponseBase treeselect(SysMenuEntity entity) {
        return menuService.treeselect(entity);
    }

    @GetMapping("/role-menu-treeselect")
    @Operation(summary = "查询角色菜单树")
    public ResponseBase roleMenuTreeselect(Integer roleId) {
        return menuService.roleMenuTreeselect(roleId);
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    public ResponseBase add(SysMenuEntity entity) {
        return menuService.add(entity);
    }

    @GetMapping("/{menuId}")
    @Operation(summary = "查询菜单详情")
    public ResponseBase findById(@PathVariable Integer menuId) {
        return menuService.findById(menuId);
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    public ResponseBase update(SysMenuEntity entity) {
        return menuService.update(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除菜单")
    public ResponseBase delete(Integer menuId) {
        return menuService.delete(menuId);
    }
}
