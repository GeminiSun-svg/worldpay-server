package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.query.PublicQueryEntity;
import com.worldpay.service.StatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/card/api/stat")
@Tag(name = "Stat")
public class StatController {

    @Autowired
    private StatService statService;

    @PostMapping("/admin-stat")
    @Operation(summary = "后台数据统计")
    public ResponseBase adminStat(HttpServletRequest req, @RequestBody PublicQueryEntity entity) {
        return statService.adminStat(req, entity);
    }

    @PostMapping("/app-stat")
    @Operation(summary = "App 数据统计")
    public ResponseBase appStat(HttpServletRequest req, @RequestBody PublicQueryEntity entity) {
        return statService.appStat(req, entity);
    }

    @GetMapping("/app-total")
    @Operation(summary = "App 总额统计")
    public ResponseBase appTotal(HttpServletRequest request) {
        return statService.appTotal(request);
    }

    @GetMapping("/total")
    @Operation(summary = "总额统计")
    public ResponseBase total(HttpServletRequest request) {
        return statService.total(request);
    }

    @PostMapping("/channel-total")
    @Operation(summary = "渠道总额统计")
    public ResponseBase channelTotal(@RequestBody PublicQueryEntity entity) {
        return statService.channelTotal(entity);
    }
}
