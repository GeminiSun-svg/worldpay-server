package com.worldpay.dao.forecast;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.forecast.ForecastSlideshowEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 类描述：轮播图数据层
 *
 * @author GeminiSun
 * @date 2026/3/26 11:00
 */
@Mapper
public interface ForecastSlideshowDao extends BaseMapper<ForecastSlideshowEntity> {
}
