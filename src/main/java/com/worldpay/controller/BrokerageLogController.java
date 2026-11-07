package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.brokerage.BrokerageLogEntity;
import com.worldpay.service.BrokerageLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/card/api/brokerage-log")
@Tag(name = "BrokerageLog")
public class BrokerageLogController {

    @Autowired
    private BrokerageLogService brokerageLogService;

    @PostMapping("/all")
    @Operation(summary = "查询佣金流水列表")
    public ResponseBase findAll(@RequestBody BrokerageLogEntity entity, HttpServletRequest request) {
        return brokerageLogService.findAll(entity, request);
    }

    @GetMapping("/sum")
    @Operation(summary = "查询按渠道ID")
    public ResponseBase findByChannelId(Integer ChannelId) {
        return brokerageLogService.findByChannelId(ChannelId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询佣金日志按ID")
    public ResponseBase findBrokerageLogById(@PathVariable Integer id) {
        return brokerageLogService.findBrokerageLogById(id);
    }
}
