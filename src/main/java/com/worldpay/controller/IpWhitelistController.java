package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.IpWhitelistEntity;
import com.worldpay.service.IpWhitelistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/card/api/ip-whitelist")
@Tag(name = "IpWhitelist")
public class IpWhitelistController {

    @Autowired
    private IpWhitelistService ipWhitelistService;

    @PostMapping("/list")
    @Operation(summary = "查询IP 白名单")
    public ResponseBase findUserCardAssets(@RequestBody IpWhitelistEntity entity) {
        return ipWhitelistService.findUserCardAssets(entity);
    }

    @PostMapping
    @Operation(summary = "新增IP 白名单")
    public ResponseBase add(@RequestBody IpWhitelistEntity entity) {
        return ipWhitelistService.add(entity);
    }

    @DeleteMapping
    @Operation(summary = "删除IP 白名单")
    public ResponseBase delete(Integer id) {
        return ipWhitelistService.delete(id);
    }
}
