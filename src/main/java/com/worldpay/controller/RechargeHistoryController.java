package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.merchant.RechargeHistoryEntity;
import com.worldpay.service.RechargeHistoryService;
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
@RequestMapping("/card/api/recharge-history")
@Tag(name = "RechargeHistory")
public class RechargeHistoryController {

    @Autowired
    private RechargeHistoryService rechargeHistoryService;

    @PostMapping("/list")
    @Operation(summary = "条件查询充值记录列表")
    public ResponseBase findList(@RequestBody RechargeHistoryEntity entity) {
        return rechargeHistoryService.findList(entity);
    }
}
