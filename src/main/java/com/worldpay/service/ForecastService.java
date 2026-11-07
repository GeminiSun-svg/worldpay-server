package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import com.worldpay.constants.ApiVersion;
import com.worldpay.constants.ClientType;
import com.worldpay.entity.forecast.ForecastSlideshowEntity;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
/**
 * 类描述：预测模块接口
 *
 * @author GeminiSun
 * @date 2026/3/9 11:45
 */

public interface ForecastService {

ResponseBase forecastLogin(HttpServletRequest request);

ResponseBase getForecastSlideshow(HttpServletRequest request);

ResponseBase forecastLoginV2(HttpServletRequest request);

String callback(String json);

ResponseBase saveSlideshow(ForecastSlideshowEntity entity);

}
