package com.worldpay.dao.forecast;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.forecast.ForecastUserRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 类描述：预测记录表数据层
 *
 * @author GeminiSun
 * @date 2026/3/9 14:05
 */
@Mapper
public interface ForecastUserDao extends BaseMapper<ForecastUserRecordEntity> {
}
