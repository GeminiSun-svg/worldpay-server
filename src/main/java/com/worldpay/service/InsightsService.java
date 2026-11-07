package com.worldpay.service;

import com.worldpay.base.ResponseBase;
import jakarta.servlet.http.HttpServletRequest;

public interface InsightsService {

    /** 消费洞察：区间合计（消费/充值/退款/笔数 + 上一周期环比）+ 按时间维度聚合列表 */
    ResponseBase query(String startDate, String endDate, String dimension, Integer userBankcardId, HttpServletRequest request);
}
