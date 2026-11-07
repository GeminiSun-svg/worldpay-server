package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.worldpay.api.response.UserEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.brokerage.BrokerageLogDao;
import com.worldpay.dao.card.AppUserCardOpenDao;
import com.worldpay.dao.card.CardApplyDao;
import com.worldpay.dao.card.CardTransactionDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.response.AdminStatEntity;
import com.worldpay.enums.*;
import com.worldpay.query.PublicQueryEntity;
import com.worldpay.service.StatService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.JwtUtil;
import com.worldpay.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@CrossOrigin
public class StatServiceImpl extends BaseApiService implements StatService {

    private static final Logger log = LoggerFactory.getLogger(StatServiceImpl.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private WalletLogDao walletLogDao;

    @Autowired
    private AppUserCardDao appUserCardDao;

    @Autowired
    private CardTransactionDao cardTransactionDao;

    @Autowired
    private CardApplyDao applyDao;

    @Autowired
    private AppUserIntiterDao appUserIntiterDao;

    @Autowired
    private BrokerageLogDao brokerageLogDao;
    
    @Autowired
    private SysUserDao sysUserDao;
    
    @Autowired
    private ChannelUserDao channelUserDao;
    
    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;
    
    @Autowired
    private AppUserCardOpenDao appUserCardOpenDao;


    @Override
    public ResponseBase adminStat(HttpServletRequest req, @RequestBody PublicQueryEntity entity) {
        try {
            AdminStatEntity adminStatEntity = new AdminStatEntity();
            // 如果没有传开始时间和结束时间 设置为今天的时间
            initTime(entity);
            UserEntity userEntity = getUserEntity(req);
            //添加渠道商后台
            if (userEntity.getRoleKey().equals(RoleKeyEnums.CHANNEL.getName())) {
                List<String> uidList = new ArrayList<>();
                entity.setUserId(userEntity.getUid());
                this.appChannelStat(uidList, entity);
                BigDecimal brokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
                if (brokerageFee == null){
                    brokerageFee = BigDecimal.ZERO;
                }
                BigDecimal oldBrokerageFee = BigDecimal.ZERO;
                AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                        new QueryWrapper<AppChannelIntiterEntity>()
                                .eq("channel_uid", userEntity.getUid()));
                if (appChannelIntiterEntity != null) {
                    entity.setUserId(appChannelIntiterEntity.getAppUid());
                    this.appChannelStat(uidList, entity);
                    BigDecimal channelUserIdBrokerageFee1 = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
                    brokerageFee = brokerageFee.add(channelUserIdBrokerageFee1 == null ? BigDecimal.ZERO : channelUserIdBrokerageFee1);
                    BigDecimal channelUserIdBrokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.OPERATIONS.getIndex());
                    oldBrokerageFee = oldBrokerageFee.add(channelUserIdBrokerageFee != null ? channelUserIdBrokerageFee : BigDecimal.ZERO);
                }
                entity.setUid(uidList);
                // 根据是否有用户数据，分别处理
                if (uidList != null && !uidList.isEmpty()) {
                    fillChannelStatData(entity, adminStatEntity, uidList);
                } else {
                    setDefaultChannelStatValues(adminStatEntity);
                }
                // 查询返利总金额
                adminStatEntity.setOldBorkerageFee(oldBrokerageFee);
                adminStatEntity.setRegisterUserNum(uidList.size());
                adminStatEntity.setBorkerageFee((brokerageFee != null ? brokerageFee : BigDecimal.ZERO).add(oldBrokerageFee));
                return setResultSuccess(adminStatEntity);
            }
            // app用户用户注册量
            Integer registerUserNum = appUserDao.findUserNum(entity);
            adminStatEntity.setRegisterUserNum(registerUserNum);
            // 钱包充值人数
            Integer topupUserNum = walletLogDao.findTopupUserNum(entity);
            adminStatEntity.setTopupUserNum(topupUserNum);
            // 开卡人数
            Integer openCardUserNum = appUserCardDao.findOpenCardUserNum(entity);
            adminStatEntity.setOpenCardUserNum(openCardUserNum);
            // 银行卡充值总额
            entity.setType(TransTypeEnums.TOPUP.getName());
            entity.setStatus(OrderStateEnums.POSTED.getName());
            AdminStatEntity statEntity = cardTransactionDao.findCardTopup(entity);
            BigDecimal cardTopupTotal = statEntity.getCardTopupTotal();
            if (cardTopupTotal != null) {
                adminStatEntity.setCardTopupTotal(
                        cardTopupTotal.setScale(3, RoundingMode.HALF_UP)
                );
            } else {
                adminStatEntity.setCardTopupTotal(BigDecimal.ZERO.setScale(3));
            }
//            adminStatEntity.setCardTopupTotal(statEntity.getCardTopupTotal());
            //银行卡充值手续费总额
            AdminStatEntity fees = cardTransactionDao.findFees(entity);
            adminStatEntity.setCardTopupFee(fees.getCardTopupFee());
            // 银行卡消费总额
            AdminStatEntity stat = cardTransactionDao.findCardConsume(entity);
            if (stat != null) {
                adminStatEntity.setCardConsumeTotal(stat.getCardConsumeTotal().abs().setScale(2, RoundingMode.HALF_UP));
                adminStatEntity.setCardConsumeFee(stat.getCardConsumeFee().abs().setScale(2, RoundingMode.HALF_UP));
            }
            //开卡费用总额
//            entity.setStatusId(OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex());
//            BigDecimal openCardFee = applyDao.findOpenCardFee(entity);
            entity.setStatusId(OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex());
            BigDecimal openCardFee = appUserCardOpenDao.findSum(entity);
            
            adminStatEntity.setOpenCardFee(openCardFee);
//			adminStatEntity.setCardTopupFee(adminStatEntity.getCardTopupFee().subtract(openCardFee));
            //虚拟卡开卡申请数量
            entity.setType(CardTypeEnums.VIRTUAL.getName());
            Integer openCardViNum = applyDao.findOpenCardNum(entity);
            adminStatEntity.setOpenCardViNum(openCardViNum);
            //实体卡申请数量
            entity.setType(CardTypeEnums.PHYSICAL.getName());
            Integer openCardPhNum = applyDao.findOpenCardNum(entity);
            adminStatEntity.setOpenCardPhNum(openCardPhNum);
            return setResultSuccess(adminStatEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseBase appStat(HttpServletRequest req, PublicQueryEntity entity) {
        // 如果没有传开始时间和结束时间 设置为今天的时间
        initTime(entity);
        String token = req.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        entity.setUserId(appUserEntity.getUid());
        AdminStatEntity appStat = new AdminStatEntity();
        List<String> appList = new ArrayList<>();
        this.appChannelStat(appList, entity);
        ChannelUserEntity channelUserEntity = channelUserDao.findChannelByAppUid(appUserEntity.getUid());
        BigDecimal brokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
        if (brokerageFee == null){
            brokerageFee = BigDecimal.ZERO;
        }
        BigDecimal channelUserIdBrokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.OPERATIONS.getIndex());
        BigDecimal oldBrokerageFee = channelUserIdBrokerageFee != null ? channelUserIdBrokerageFee : BigDecimal.ZERO;
        appStat.setOldBorkerageFee(oldBrokerageFee);
        if (channelUserEntity != null) {
            entity.setUserId(String.valueOf(channelUserEntity.getUserId()));
            this.appChannelStat(appList, entity);
            BigDecimal channelUserIdBrokerageFee1 = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
            brokerageFee = brokerageFee.add(channelUserIdBrokerageFee1 == null ? BigDecimal.ZERO : channelUserIdBrokerageFee1);
        }
        // 查询返利总金额
        appStat.setBorkerageFee(brokerageFee.add(oldBrokerageFee));
        entity.setUid(appList);
        // 根据是否有用户数据，分别处理
        if (appList != null && !appList.isEmpty()) {
            fillChannelStatData(entity, appStat, appList);
        } else {
            setDefaultChannelStatValues(appStat);
        }
        appStat.setRegisterUserNum(appList.size());
        appStat.setUserId(appUserEntity.getUid());
        appStat.setChannelName(appUserEntity.getUserEmail());
        return setResultSuccess(appStat);
    }

    @Override
    public ResponseBase appTotal(HttpServletRequest request) {
        try {
            // 商户资产情况
            String username = JwtUtil.getUserNameByJwt(request.getHeader("token"));
            PublicQueryEntity entity = new PublicQueryEntity();
            //查询app下面的信息
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
            List<String> appList = new ArrayList<>();
            entity.setUserId(appUserEntity.getUid());
            this.findFirstChannelUser(entity.getUserId(), appList, entity);
            Set<String> mergedUids = new LinkedHashSet<>(appList);
            //查询渠道商账号下面的信息
            ChannelUserEntity channelUserEntity = channelUserDao.findChannelByAppUid(appUserEntity.getUid());
            if (channelUserEntity != null) {
                List<String> channelUidList = new ArrayList<>();
                entity.setUserId(String.valueOf(channelUserEntity.getUserId()));
                this.findFirstChannelUser(entity.getUserId(), channelUidList, entity);
                mergedUids.addAll(channelUidList);
            }
            // 转为 list 用于后续统计
            List<String> finalUidList = new ArrayList<>(mergedUids);
            AdminStatEntity appStat = this.appTotalStat(finalUidList);
            if (channelUserEntity != null) {
                appStat.setMerchantAvailableAmount(BigDecimal.valueOf(channelUserEntity.getCardOpeningDeductionFee()));
            }
            return setResultSuccess(appStat);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 渠道商数据返回
     *
     * @param entity 查询实体
     * @return 统计数据实体
     */
    public void appChannelStat(List<String> uidList, PublicQueryEntity entity) {
        try {
            // 1. 查询被邀请用户列表
            if (entity.getUserId() != null) {
                this.findFirstChannelUser(entity.getUserId(), uidList, entity);
            }
            if (uidList != null && !uidList.isEmpty()) {
                Set<String> uniqueSet = new LinkedHashSet<>(uidList);
                uidList.clear();               // 清空原 list
                uidList.addAll(uniqueSet);
            }
        } catch (Exception e) {
            log.error("渠道商首页展示异常：{}", e.getMessage(), e);
            throw new RuntimeException("渠道商数据统计失败", e);
        }
    }

    private AdminStatEntity appTotalStat(List<String> uidList) {
        AdminStatEntity adminStatEntity = new AdminStatEntity();
        if (uidList == null || uidList.isEmpty()) {
            adminStatEntity.setWalletTotal(0.0);
            adminStatEntity.setWalletFreezeTotal(0.0);
            adminStatEntity.setCardTotal(0.0);
        } else {
            // 钱包总额/冻结总额
            AdminStatEntity statEntity = appUserDao.findWalletTotal(uidList);
            if (statEntity != null) {
                adminStatEntity.setWalletTotal(statEntity.getWalletTotal());
                adminStatEntity.setWalletFreezeTotal(statEntity.getWalletFreezeTotal());
            } else {
                adminStatEntity.setWalletTotal(0.0);
                adminStatEntity.setWalletFreezeTotal(0.0);
            }
            // 银行卡总额
            Double cardTotal = appUserCardDao.findCardTotal(uidList);
            adminStatEntity.setCardTotal(cardTotal);
        }
        return adminStatEntity;
    }

    public UserEntity getUserEntity(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        //获取权限标识符
        return sysUserDao.findRoleKeyByUserName(username);
    }

    @Override
    public ResponseBase total(HttpServletRequest req) {
        try {
            // 商户资产情况
            AdminStatEntity adminStatEntity = new AdminStatEntity();
            //添加渠道商判断
            UserEntity userEntity = getUserEntity(req);
            //如果是渠道商
            if (userEntity.getRoleKey().equals(RoleKeyEnums.CHANNEL.getName())) {
                List<String> mergedUids = new ArrayList<>();
                ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", userEntity.getUid()));
                adminStatEntity.setMerchantAvailableAmount(BigDecimal.valueOf(channelUserEntity.getCardOpeningDeductionFee()));
                PublicQueryEntity entity = new PublicQueryEntity();
                entity.setUserId(userEntity.getUid());
                // 如果没有传开始时间和结束时间 设置为今天的时间
                this.findFirstChannelUser(entity.getUserId(), mergedUids, entity);
                AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                        new QueryWrapper<AppChannelIntiterEntity>()
                                .eq("channel_uid", channelUserEntity.getUserId()));
                if (appChannelIntiterEntity != null) {
                    List<String> channelUidList = new ArrayList<>();
                    entity.setUserId(String.valueOf(appChannelIntiterEntity.getAppUid()));
                    this.findFirstChannelUser(String.valueOf(appChannelIntiterEntity.getAppUid()), channelUidList, entity);
                    mergedUids.addAll(channelUidList);
                }
                // 转为 list 用于后续统计
                List<String> uidList = new ArrayList<>(mergedUids);
                if (uidList == null || uidList.isEmpty()) {
                    adminStatEntity.setWalletTotal(0.0);
                    adminStatEntity.setWalletFreezeTotal(0.0);
                    adminStatEntity.setCardTotal(0.0);
                } else {
                    // 钱包总额/冻结总额
                    AdminStatEntity statEntity = appUserDao.findWalletTotal(uidList);
                    if (statEntity != null) {
                        adminStatEntity.setWalletTotal(statEntity.getWalletTotal());
                        adminStatEntity.setWalletFreezeTotal(statEntity.getWalletFreezeTotal());
                    } else {
                        adminStatEntity.setWalletTotal(0.0);
                        adminStatEntity.setWalletFreezeTotal(0.0);
                    }
                    // 银行卡总额
                    Double cardTotal = appUserCardDao.findCardTotal(uidList);
                    adminStatEntity.setCardTotal(cardTotal);
                }
            } else {
                List<String> uidList = new ArrayList<>();
//                ResponseBase assetData = UCardApi.postData(null, UCardMethods.MERCHANT_ASSET, new Object(), null);
//                if (Constants.HTTP_RES_CODE_200.equals(assetData.getRtncode())) {
//                    MerchantApiEntity merchant = JSONObject.parseObject(assetData.getData().toString(),
//                            MerchantApiEntity.class);
//                    if (merchant != null) {
//                        adminStatEntity.setMerchantAvailableAmount(merchant.getAvailableAmount() == null ? BigDecimal.ZERO
//                                : merchant.getAvailableAmount().setScale(2, RoundingMode.HALF_UP));
//
//                        adminStatEntity.setMerchantFrozenAmount(merchant.getFrozenAmount() == null ? BigDecimal.ZERO
//                                : merchant.getFrozenAmount().setScale(2, RoundingMode.HALF_UP));
//                    }
//                }
                // 钱包总额/冻结总额
                AdminStatEntity statEntity = appUserDao.findWalletTotal(uidList);
                adminStatEntity.setWalletTotal(statEntity.getWalletTotal());
                adminStatEntity.setWalletFreezeTotal(statEntity.getWalletFreezeTotal());
                // 银行卡总额
                Double cardTotal = appUserCardDao.findCardTotal(uidList);
                adminStatEntity.setCardTotal(cardTotal);
                // 钱包总额/冻结总额
                adminStatEntity.setWalletTotal(appUserDao.findWalletTotal(uidList).getWalletTotal());
                adminStatEntity.setWalletFreezeTotal(appUserDao.findWalletTotal(uidList).getWalletFreezeTotal());
                // 银行卡总额
                adminStatEntity.setCardTotal(appUserCardDao.findCardTotal(uidList));
            }
            return setResultSuccess(adminStatEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase channelTotal(@RequestBody PublicQueryEntity entity) {
//        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        // 如果没有传开始时间和结束时间 设置为今天的时间
        PageInfo<AdminStatEntity> info = null;
        try {
            initTime(entity);
            List<AdminStatEntity> adminStats = new ArrayList<>();
            //获取渠道商列表
            List<ChannelUserEntity> channelUserEntities = channelUserDao.selectchannelUsersByUid(entity.getUserId());
            for (ChannelUserEntity channelUserEntity : channelUserEntities) {
                PublicQueryEntity loopEntity = new PublicQueryEntity();
                BeanUtils.copyProperties(entity, loopEntity);
                loopEntity.setUserId(String.valueOf(channelUserEntity.getUserId()));
                AdminStatEntity adminStatEntity = new AdminStatEntity();
                adminStatEntity.setUserId(String.valueOf(channelUserEntity.getUserId()));
                adminStatEntity.setChannelName(channelUserEntity.getUsername());
                adminStatEntity.setMerchantAvailableAmount(BigDecimal.valueOf(channelUserEntity.getCardOpeningDeductionFee()));
                List<String> uidList = new ArrayList<>();
                this.appChannelStat(uidList,loopEntity);
                BigDecimal brokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(loopEntity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
                if (brokerageFee == null) {
                    brokerageFee = BigDecimal.ZERO;
                }
                BigDecimal channelOldBrokerageFee = BigDecimal.ZERO;
                AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                        new QueryWrapper<AppChannelIntiterEntity>()
                                .eq("channel_uid", channelUserEntity.getUserId()));
                if (appChannelIntiterEntity != null) {
                    loopEntity.setUserId(appChannelIntiterEntity.getAppUid());
                    this.appChannelStat(uidList,loopEntity);
                    BigDecimal channelBrokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(loopEntity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
                    BigDecimal appBrokerageFee = channelBrokerageFee != null ? channelBrokerageFee : BigDecimal.ZERO;
                    brokerageFee = brokerageFee.add(appBrokerageFee);
                    BigDecimal appChannelBrokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(loopEntity.getUserId(), RoleKeyEnums.OPERATIONS.getIndex());
                    BigDecimal appChannelBrokerageFee1 =  appChannelBrokerageFee != null ? appChannelBrokerageFee : BigDecimal.ZERO;
                    channelOldBrokerageFee = channelOldBrokerageFee.add(appChannelBrokerageFee1);
                }
                BigDecimal oldBrokerageFee = channelOldBrokerageFee != null ? channelOldBrokerageFee : BigDecimal.ZERO;
                adminStatEntity.setOldBorkerageFee(oldBrokerageFee);
                adminStatEntity.setBorkerageFee(brokerageFee.add(oldBrokerageFee));
                adminStatEntity.setRegisterUserNum(uidList.size());
                loopEntity.setUid(uidList);
                // 根据是否有用户数据，分别处理
                if (uidList != null && !uidList.isEmpty()) {
                    fillChannelStatData(loopEntity, adminStatEntity, uidList);
                } else {
                    setDefaultChannelStatValues(adminStatEntity);
                }
                adminStats.add(adminStatEntity);
            }
            List<AdminStatEntity> adminStatsList = GenericityUtil.Page(adminStats, entity.getPageNumber(), entity.getPageSize());
            info = new PageInfo<>(adminStatsList);
            info.setTotal(adminStats.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return setResultSuccess(info);
    }


    /**
     * 渠道商数据返回
     *
     * @param entity          查询实体
     * @param adminStatEntity 统计数据实体
     * @return 统计数据实体
     */
    public AdminStatEntity channelStat(PublicQueryEntity entity, AdminStatEntity adminStatEntity) {
        try {
            // 1. 查询被邀请用户列表
            List<String> uidList = new ArrayList<>();
            if (entity.getUserId() != null) {
                this.findFirstChannelUser(entity.getUserId(), uidList, entity);
            }
            entity.setUid(uidList);
            adminStatEntity.setRegisterUserNum(uidList.size());

            // 2. 根据是否有用户数据，分别处理
            if (uidList != null && !uidList.isEmpty()) {
                fillChannelStatData(entity, adminStatEntity, uidList);
            } else {
                setDefaultChannelStatValues(adminStatEntity);
            }

            // 3. 查询返利总金额
            BigDecimal brokerageFee = brokerageLogDao.findChannelUserIdBrokerageFee(entity.getUserId(), RoleKeyEnums.CHANNEL.getIndex());
            adminStatEntity.setBorkerageFee(brokerageFee != null ? brokerageFee : BigDecimal.ZERO);

            return adminStatEntity;
        } catch (Exception e) {
            log.error("渠道商首页展示异常：{}", e.getMessage(), e);
            throw new RuntimeException("渠道商数据统计失败", e);
        }
    }

    /**
     * 填充渠道商统计数据
     *
     * @param entity          查询实体
     * @param adminStatEntity 统计数据实体
     * @param uidList         用户ID列表
     */
    private void fillChannelStatData(PublicQueryEntity entity, AdminStatEntity adminStatEntity, List<String> uidList) {
        // 2.1 查询钱包和银行卡总额
        AdminStatEntity walletStat = appUserDao.findWalletTotal(uidList);
        adminStatEntity.setWalletTotal(walletStat != null && walletStat.getWalletTotal() != null
                ? walletStat.getWalletTotal() : 0.0);

        Double cardTotal = appUserCardDao.findCardTotal(uidList);
        adminStatEntity.setCardTotal(cardTotal != null ? cardTotal : 0.0);

        // 2.2 查询充值人数和开卡人数
        Integer topUpChannelUserNum = walletLogDao.findTopUpChannelUserNum(entity);
        adminStatEntity.setTopupUserNum(topUpChannelUserNum != null ? topUpChannelUserNum : 0);

        Integer openCardUserNum = appUserCardDao.findOpenCardChannelUserNum(entity);
        adminStatEntity.setOpenCardUserNum(openCardUserNum != null ? openCardUserNum : 0);

        // 2.3 查询银行卡充值相关数据
        entity.setType(TransTypeEnums.TOPUP.getName());
        entity.setStatus(OrderStateEnums.POSTED.getName());

        AdminStatEntity topupStat = cardTransactionDao.findCardChannelTopup(entity);
        adminStatEntity.setCardTopupTotal(topupStat != null && topupStat.getCardTopupTotal() != null
                ? topupStat.getCardTopupTotal() : BigDecimal.ZERO);

        AdminStatEntity fees = cardTransactionDao.findChannelFees(entity);
        adminStatEntity.setCardTopupFee(fees != null && fees.getCardTopupFee() != null
                ? fees.getCardTopupFee() : BigDecimal.ZERO);

        // 2.4 查询银行卡消费相关数据
        AdminStatEntity consumeStat = cardTransactionDao.findChannelCardConsume(entity);
        if (consumeStat != null) {
            BigDecimal consumeTotal = consumeStat.getCardConsumeTotal();
            BigDecimal consumeFee = consumeStat.getCardConsumeFee();
            adminStatEntity.setCardConsumeTotal(consumeTotal != null
                    ? consumeTotal.abs().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            adminStatEntity.setCardConsumeFee(consumeFee != null
                    ? consumeFee.abs().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        } else {
            adminStatEntity.setCardConsumeTotal(BigDecimal.ZERO);
            adminStatEntity.setCardConsumeFee(BigDecimal.ZERO);
        }

        // 2.5 查询开卡费用和申请数量
//        entity.setStatusId(OpenCardStateEnums.SUCCESS_ACTIVATION.getIndex());
//        BigDecimal openCardFee = applyDao.findChannelOpenCardFee(entity);
        BigDecimal openCardFee = appUserCardOpenDao.findChannelOpenCardFee(entity);
        adminStatEntity.setOpenCardFee(openCardFee != null ? openCardFee : BigDecimal.ZERO);

        entity.setType(CardTypeEnums.VIRTUAL.getName());
        Integer openCardViNum = applyDao.findChannelOpenCardNum(entity);
        adminStatEntity.setOpenCardViNum(openCardViNum != null ? openCardViNum : 0);

        entity.setType(CardTypeEnums.PHYSICAL.getName());
        Integer openCardPhNum = applyDao.findChannelOpenCardNum(entity);
        adminStatEntity.setOpenCardPhNum(openCardPhNum != null ? openCardPhNum : 0);
    }

    /**
     * 设置渠道商统计默认值
     *
     * @param adminStatEntity 统计数据实体
     */
    private void setDefaultChannelStatValues(AdminStatEntity adminStatEntity) {
        adminStatEntity.setCardTotal(0.0);
        adminStatEntity.setTopupUserNum(0);
        adminStatEntity.setOpenCardUserNum(0);
        adminStatEntity.setCardTopupTotal(BigDecimal.ZERO);
        adminStatEntity.setCardTopupFee(BigDecimal.ZERO);
        adminStatEntity.setCardConsumeTotal(BigDecimal.ZERO);
        adminStatEntity.setCardConsumeFee(BigDecimal.ZERO);
        adminStatEntity.setOpenCardFee(BigDecimal.ZERO);
        adminStatEntity.setOpenCardViNum(0);
        adminStatEntity.setOpenCardPhNum(0);
    }

    /**
     * 初始化查询时间
     *
     * @param entity
     */
    public void initTime(PublicQueryEntity entity) {
        if ((entity.getBegTime() == null || entity.getBegTime().isEmpty())
                && (entity.getEndTime() == null || entity.getEndTime().isEmpty())) {
            String endTime = dateFormat.format(new Date());
            String begTime = dateFormat.format(new Date());
            entity.setBegTime(begTime);
            entity.setEndTime(endTime);
        }
    }

    /**
     * 查找前3级渠道用户
     *
     * @param startUid 起始用户UID
     * @param uids     存储结果的列表
     * @param entity
     */
    public void findFirstChannelUser(String startUid, List<String> uids, PublicQueryEntity entity) {
        // 先做入参兜底，避免空指针
        if (StringUtils.isBlank(startUid) || uids == null) {
            return;
        }
        // 调用递归方法，起始层级为1
        findChannelUserRecursive(startUid, 1, uids, entity);
    }

    /**
     * 递归查找渠道用户（修正后）
     *
     * @param currentUid   当前用户UID
     * @param currentLevel 当前层级
     * @param uids         存储结果的列表
     * @param entity
     */
    private void findChannelUserRecursive(String currentUid, int currentLevel, List<String> uids, PublicQueryEntity entity) {
        // 终止条件改为 >3，确保处理1、2、3级
        if (currentLevel > 3) {
            return;
        }

        // 查询当前用户的邀请人相关数据
//        List<AppUserIntiterEntity> userIntiterEntity = appUserIntiterDao.selectList(
//                new QueryWrapper<AppUserIntiterEntity>().eq("inviter_user_uid", currentUid)
//        );
        List<AppUserIntiterEntity> userIntiterEntity = null;
        if (entity.getBegTime() != null && entity.getEndTime() != null) {
            userIntiterEntity = appUserIntiterDao.findInviterUserList(currentUid, entity.getBegTime(), entity.getEndTime());
        } else {
            userIntiterEntity = appUserIntiterDao.findInviterUserListNoTime(currentUid);
        }

        // 补充空列表判断，提升健壮性
        if (userIntiterEntity == null || userIntiterEntity.isEmpty()) {
            return;
        }

        for (AppUserIntiterEntity appUserIntiterEntity : userIntiterEntity) {
            // 获取邀请人UID，继续往上查找
            String coverInviterUid = appUserIntiterEntity.getCoverInviterUserUid();
            String inviterUserUid = appUserIntiterEntity.getInviterUserUid();

            // 使用continue跳过当前无效元素，而非return终止整个方法
            if (StringUtils.isBlank(coverInviterUid)) {
                continue;
            }

            // 可选去重，避免重复添加（根据业务需求决定是否保留）
            if (inviterUserUid != null) {
                uids.add(coverInviterUid);
            }

            // 递归查找下一级邀请人，层级+1
            findChannelUserRecursive(coverInviterUid, currentLevel + 1, uids, entity);
        }
    }
}
