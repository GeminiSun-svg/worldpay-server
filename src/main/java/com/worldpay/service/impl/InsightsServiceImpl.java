package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.card.CardTransactionDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.card.CardInsightEntity;
import com.worldpay.service.InsightsService;
import com.worldpay.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InsightsServiceImpl extends BaseApiService implements InsightsService {

    /** 默认区间：近 30 天；最大跨度：90 天；季度维度最大跨度：370 天 */
    private static final int DEFAULT_DAYS = 30;
    private static final int MAX_DAYS = 90;
    private static final int MAX_DAYS_QUARTER = 370;
    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    @Autowired
    private CardTransactionDao cardTransactionDao;

    @Autowired
    private AppUserDao appUserDao;

    @Override
    public ResponseBase query(String startDate, String endDate, String dimension, Integer userBankcardId, HttpServletRequest request) {
        String uid = currentUid(request);
        if (uid == null) {
            return setResultError("user does not exist");
        }
        String dim = (dimension == null || dimension.isEmpty()) ? "week" : dimension.toLowerCase();
        if (!dim.equals("day") && !dim.equals("week") && !dim.equals("month") && !dim.equals("quarter")) {
            return setResultError("dimension 仅支持 day/week/month/quarter");
        }
        int maxDays = "quarter".equals(dim) ? MAX_DAYS_QUARTER : MAX_DAYS;
        Date[] range = resolveRange(startDate, endDate, maxDays);
        if (range == null) {
            return setResultError("日期格式应为 yyyy-MM-dd，且跨度不能超过 " + maxDays + " 天");
        }
        String start = format(range[0]);
        String end = format(range[1]);

        // 区间合计
        CardInsightEntity data = cardTransactionDao.insightOverview(uid, start, end, userBankcardId);
        if (data == null) {
            data = new CardInsightEntity();
        }

        // 上一周期（与当前区间等长，紧邻 start 之前），用于环比
        long days = (range[1].getTime() - range[0].getTime()) / DAY_MS + 1;
        Date prevEnd = new Date(range[0].getTime() - DAY_MS);
        Date prevStart = new Date(prevEnd.getTime() - (days - 1) * DAY_MS);
        CardInsightEntity prev = cardTransactionDao.insightOverview(uid, format(prevStart), format(prevEnd), userBankcardId);
        if (prev != null) {
            data.setPrevConsumeTotal(prev.getConsumeTotal());
            data.setPrevTopupTotal(prev.getTopupTotal());
            data.setPrevRefundTotal(prev.getRefundTotal());
            data.setPrevTotalCount(prev.getTotalCount());
        }
        data.setStartDate(start);
        data.setEndDate(end);

        // 按时间维度聚合的明细列表
        List<CardInsightEntity> list;
        switch (dim) {
            case "day":
                list = cardTransactionDao.insightByDay(uid, start, end, userBankcardId);
                break;
            case "month":
                list = cardTransactionDao.insightByMonth(uid, start, end, userBankcardId);
                break;
            case "quarter":
                list = cardTransactionDao.insightByQuarter(uid, start, end, userBankcardId);
                break;
            default:
                list = cardTransactionDao.insightByWeek(uid, start, end, userBankcardId);
        }
        data.setList(list);
        return setResultSuccess(data);
    }

    /** 从 JWT 解析当前登录用户 uid */
    private String currentUid(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity user = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        return user == null ? null : user.getUid();
    }

    /**
     * 解析日期区间：缺省为近 30 天；跨度超过 maxDays 或格式非法时返回 null
     * range[0]=开始日期(当天 0 点)，range[1]=结束日期
     */
    private Date[] resolveRange(String startDate, String endDate, int maxDays) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Calendar endCal = Calendar.getInstance();
            if (endDate != null && !endDate.isEmpty()) {
                endCal.setTime(sdf.parse(endDate));
            }
            Calendar startCal = (Calendar) endCal.clone();
            startCal.add(Calendar.DAY_OF_MONTH, -(DEFAULT_DAYS - 1));
            if (startDate != null && !startDate.isEmpty()) {
                startCal.setTime(sdf.parse(startDate));
            }
            long spanDays = (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / DAY_MS;
            if (spanDays < 0 || spanDays >= maxDays) {
                return null;
            }
            return new Date[]{startCal.getTime(), endCal.getTime()};
        } catch (Exception e) {
            return null;
        }
    }

    private String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
