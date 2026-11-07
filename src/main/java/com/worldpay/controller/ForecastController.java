package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.constants.ApiVersion;
import com.worldpay.constants.ClientType;
import com.worldpay.entity.forecast.ForecastSlideshowEntity;
import com.worldpay.service.ForecastService;
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
@RequestMapping("/card/api/forecast")
@Tag(name = "Forecast")
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/login")
    @Operation(summary = "预测系统登录")
    public ResponseBase forecastLogin(HttpServletRequest request) {
        return forecastService.forecastLogin(request);
    }

    @GetMapping("/forecast-slideshow")
    @Operation(summary = "获取预测轮播图")
    public ResponseBase getForecastSlideshow(HttpServletRequest request) {
        return forecastService.getForecastSlideshow(request);
    }

    @GetMapping
    @Operation(summary = "预测系统登录（V2）")
    public ResponseBase forecastLoginV2(HttpServletRequest request) {
        return forecastService.forecastLoginV2(request);
    }

    @PostMapping("/callback")
    @Operation(summary = "回调")
    public String callback(@RequestBody String json) {
        return forecastService.callback(json);
    }

    @PostMapping("/slideshow")
    @Operation(summary = "保存预测轮播图")
    public ResponseBase saveSlideshow(ForecastSlideshowEntity entity) {
        return forecastService.saveSlideshow(entity);
    }
}
