package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.service.InsightsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/card/api/insights")
@Tag(name = "Insights")
public class InsightsController {

    @Autowired
    private InsightsService insightsService;

    @GetMapping
    @Operation(summary = "卡片消费洞察（区间合计+环比，按 dimension=day/week/month/quarter 聚合列表，默认 week）")
    public ResponseBase query(String startDate, String endDate, String dimension, Integer userBankcardId, HttpServletRequest request) {
        return insightsService.query(startDate, endDate, dimension, userBankcardId, request);
    }
}
