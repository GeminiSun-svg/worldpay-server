package com.worldpay.config;

import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.brokerage.BrokerageLogDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 类描述：初始化用户钱包中的提现余额
 *
 * @author GeminiSun
 * @date 2026/2/10 11:30
 */
@Component
public class InitWithdrawalWalletConfig {

    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private BrokerageLogDao brokerageLogDao;

    @Autowired
    private RedisUtil redisUtil;

    private static final String INIT_REDIS_KEY = "APP_INIT_REDIS_KEY";

    //@PostConstruct
    public void init(){
        boolean hasKey = redisUtil.hasKey(INIT_REDIS_KEY);
        if (hasKey){
            return;
        }
        List<AppChannelIntiterEntity> appChannelInviterEntities = appChannelIntiterDao.selectList(null);
        if (appChannelInviterEntities.isEmpty()){
            return;
        }
        for (AppChannelIntiterEntity appChannelInviterEntity : appChannelInviterEntities) {
            BigDecimal channelUserIdBrokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(appChannelInviterEntity.getChannelUid(), IntiterUserTypeEnums.CHANNEL_USER.getIndex());
            AppUserEntity appUserEntity = appUserDao.selectById(appChannelInviterEntity.getAppId());
            appUserEntity.setWithdrawableBalance(channelUserIdBrokerageFee.doubleValue());
            appUserEntity.setGmtModified(new Date());
            appUserDao.updateById(appUserEntity);
        }
        redisUtil.set(INIT_REDIS_KEY,"true");
    }
}
