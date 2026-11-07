package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.AppBindChannelEditEntity;
import com.worldpay.api.request.AppBindChannelReqEntity;
import com.worldpay.api.request.ApplicationRecordReqEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.brokerage.BrokerageLogDao;
import com.worldpay.dao.brokerage.ChannelLevelDao;
import com.worldpay.dao.card.CardDao;
import com.worldpay.dao.channel.ChannelCardDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.record.ApplicationRecordDao;
import com.worldpay.dao.system.SysRoleDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.appuser.ApplicationRecordEntity;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import com.worldpay.entity.card.CardEntity;
import com.worldpay.entity.channel.ChannelCardEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.system.SysRoleEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.enums.*;
import com.worldpay.service.ApplicationRecordService;
import com.worldpay.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.worldpay.constants.UserConstants.ROLE_KEY;

/**
 * 类描述：申请记录
 *
 * @author GeminiSun
 * @date 2026/2/4 10:14
 */
@Service
@Transactional
@CrossOrigin
public class ApplicationRecordServiceImpl extends BaseApiService implements ApplicationRecordService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRecordServiceImpl.class);
    @Autowired
    private ApplicationRecordDao applicationRecordDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private ChannelUserDao channelUserDao;

    @Autowired
    private AppUserIntiterDao appUserIntiterDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private ChannelLevelDao channelLevelDao;

    @Autowired
    private MessageTemplateServiceImpl messageTemplateServiceImpl;

    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private ChannelCardDao channelCardDao;

    @Autowired
    private CardDao cardDao;
    @Autowired
    private BrokerageLogDao brokerageLogDao;


    @Override
    public ResponseBase findApplicationRecord(ApplicationRecordEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        QueryWrapper<ApplicationRecordEntity> wrapper = new QueryWrapper<>();
        if (entity.getStatus() != null) {
            wrapper.eq("status", entity.getStatus());
        }
        if (entity.getAppEmail() != null){
            wrapper.eq("app_email", entity.getAppEmail());
        }
        wrapper.orderByDesc("setTime");
        List<ApplicationRecordEntity> list = applicationRecordDao.selectList(wrapper);
        PageInfo<ApplicationRecordEntity> info = new PageInfo<>(list);
        return setResultSuccess(info);
    }

    @Override
    public ResponseBase appFindChannel(AppBindChannelReqEntity appBindChannelReqEntity) {
        //搜索渠道商信息根据邀请码
        String invitationCode = appBindChannelReqEntity.getInvitationCode();
        ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("invitation_code", invitationCode));
        if (channelUserEntity == null) {
            return setResultError(I18nUtil.getMessage("channel_nonentity"));
        }
        channelUserEntity.setAcctive(sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("id", channelUserEntity.getUserId())).getAcctive());
        //返回渠道商信息
        return setResultSuccess(channelUserEntity);
    }

    @Override
    @SysLogAnnotation(module = "app用户绑定渠道商账户", type = "post", remark = "app用户绑定渠道商账户")
    public ResponseBase appBindChannel(AppBindChannelEditEntity entity) {
        try {
            //支付密码
            String paymentCode = entity.getPaymentCode();
            if (StringUtils.isBlank(paymentCode)) {
                return setResultError(I18nUtil.getMessage("pay_password_error"));
            }
            String appUid = entity.getAppUid();
            //判断当前用户信息是否正确
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>()
                    .eq("uid", appUid)
                    .eq("pay_password", DigestUtils.md5DigestAsHex(paymentCode.getBytes())));
            if (appUserEntity == null) {
                return setResultError(I18nUtil.getMessage("user.password_error"));
            }
            //查询当前渠道商是否为渠道商用户
            List<ApplicationRecordEntity> applicationRecordEntitys = applicationRecordDao.selectList(new QueryWrapper<ApplicationRecordEntity>()
                    .nested(wrapper -> wrapper
                            .eq("app_id", entity.getAppId())
                            .or()
                            .eq("channel_id", entity.getChannelId()
                            )
                    )
                    .ne("status", ApplicationRecordStateEnums.AUDIT_FAIL.getIndex()));
            for (ApplicationRecordEntity record : applicationRecordEntitys) {
                if (record.getAppId().equals(entity.getAppId())) {
                    return setResultError(I18nUtil.getMessage("application_record_already_exists"));
                }
                if (record.getChannelId().equals(entity.getChannelId())) {
                    return setResultError(I18nUtil.getMessage("channel_is_binding_app"));
                }
            }
            ApplicationRecordEntity applicationRecord = this.editApplicationRecord(appUserEntity, entity, ApplicationRecordTypeEnums.UPGRADE.getIndex());
            applicationRecord.setStatus(ApplicationRecordStateEnums.AUDIT_PASS.getIndex());
            //新增提交申请记录 状态为待审核
            applicationRecordDao.insert(applicationRecord);
            //存在 修改渠道商的邀请码为当前app用户邀请码
            ChannelUserEntity channelUserEntity = channelUserDao.selectById(entity.getChannelId());
            channelUserEntity.setInvitationCode(appUserEntity.getInvitationCode());
            channelUserEntity.setGmtModified(new Date());
            channelUserDao.updateById(channelUserEntity);
            //查询渠道商返佣记录
            BigDecimal withdrawableBalances = brokerageLogDao.findChannelUserIdBrokerageFee(String.valueOf(channelUserEntity.getUserId()),IntiterUserTypeEnums.CHANNEL_USER.getIndex());
            //用户绑定渠道商，绑定渠道商可用提现余额
            appUserEntity.setWithdrawableBalance( withdrawableBalances == null ? 0.0 :  withdrawableBalances.doubleValue());
            appUserEntity.setGmtModified(new Date());
            appUserDao.updateById(appUserEntity);
            //修改APP用户邀请记录，修改所以邀请人uid为 entity.uid的type类型为2
            appUserIntiterDao.updateTypeByInviterUid(entity.getAppUid(), IntiterUserTypeEnums.CHANNEL_USER.getIndex());
            //如果当前用户原本属于被邀请的关系，删除原邀请记录
            AppUserIntiterEntity appUserIntiterEntity = appUserIntiterDao.selectOne(new QueryWrapper<AppUserIntiterEntity>()
                    .eq("cover_inviter_user", entity.getAppId())
                    .eq("cover_inviter_user_uid", entity.getAppId()));
            if (appUserIntiterEntity != null){
                appUserIntiterDao.deleteById(appUserIntiterEntity.getId());
            }
            //新增用户渠道商绑定关系
            AppChannelIntiterEntity appChannelIntiterEntity = new AppChannelIntiterEntity();
            appChannelIntiterEntity.setAppId(entity.getAppId());
            appChannelIntiterEntity.setAppUid(entity.getAppUid());
            appChannelIntiterEntity.setChannelId(channelUserEntity.getId());
            appChannelIntiterEntity.setChannelUid(String.valueOf(channelUserEntity.getUserId()));
            appChannelIntiterEntity.setInvitationCode(appUserEntity.getInvitationCode());
            GenericityUtil.setDate(appChannelIntiterEntity);
            appChannelIntiterDao.insert(appChannelIntiterEntity);
        } catch (Exception e) {
            log.error("app用户绑定渠道商账户-新增申请记录异常，异常信息：{}", e.getMessage());
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }
        return setResultSuccess();
    }

    @Override
    @SysLogAnnotation(module = "app用户升级渠道商账户", type = "post", remark = "app用户升级渠道商账户")
    public ResponseBase appIsChannel(AppBindChannelEditEntity entity) {
        try {
            //根据appId查询用户信息
            AppUserEntity appUserEntity = appUserDao.selectById(entity.getAppId());
            if (appUserEntity == null) {
                return setResultError(I18nUtil.getMessage("user.account_error"));
            }
            ApplicationRecordEntity applicationRecordEntity = applicationRecordDao.selectOne(new QueryWrapper<ApplicationRecordEntity>()
                    .eq("app_id", entity.getAppId())
                    .eq("app_uid", entity.getAppUid())
                    .ne("status", ApplicationRecordStateEnums.AUDIT_FAIL.getIndex()));
            if (applicationRecordEntity != null) {
                return setResultError(I18nUtil.getMessage("application_record_already_exists"));
            }
            ApplicationRecordEntity applicationRecord = this.editApplicationRecord(appUserEntity, entity, ApplicationRecordTypeEnums.BECAME.getIndex());
            applicationRecord.setStatus(ApplicationRecordStateEnums.UNDER_REVIEW.getIndex());
            //新增提交申请记录 状态为待审核
            applicationRecordDao.insert(applicationRecord);
        } catch (Exception e) {
            log.error("app用户升级渠道商账户-新增申请记录异常，异常信息：{}", e.getMessage());
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }
        return setResultSuccess();
    }

    /**
     * 构建ApplicationRecordEntity对象
     *
     * @param appUserEntity
     * @param entity
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ApplicationRecordEntity editApplicationRecord(AppUserEntity appUserEntity,
                                                          AppBindChannelEditEntity entity,
                                                          Integer type) throws InvocationTargetException, IllegalAccessException {
        ApplicationRecordEntity applicationRecord = new ApplicationRecordEntity();
        applicationRecord.setAppPhone(appUserEntity.getMobileNumber());
        applicationRecord.setAppId(appUserEntity.getId());
        applicationRecord.setAppUid(appUserEntity.getUid());
        applicationRecord.setAppEmail(appUserEntity.getUserEmail());
        if (entity.getChannelId() != null) {
            applicationRecord.setChannelId(entity.getChannelId());
        }
        if (entity.getChannelUid() != null) {
            applicationRecord.setChannelUid(entity.getChannelUid());
        }
        applicationRecord.setType(type);
        GenericityUtil.setDate(applicationRecord);
        return applicationRecord;
    }

    @Override
    @SysLogAnnotation(module = "申请记录审核状态变更", type = "POST", remark = "申请记录审核状态变更")
    public ResponseBase updateStatus(ApplicationRecordReqEntity entity) {
        try {
            Integer status = entity.getStatus();
            ApplicationRecordEntity applicationRecordEntity = applicationRecordDao.selectById(entity.getId());
            AppUserEntity appUserEntity = appUserDao.selectById(applicationRecordEntity.getAppId());
            //审核通过
            if (ApplicationRecordStateEnums.AUDIT_PASS.getIndex().equals(status)) {
                if (sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("acctive",appUserEntity.getUserEmail())) != null) {
                    return setResultError(I18nUtil.getMessage("channel_is_exist"));
                }
                AppChannelIntiterEntity appChannelIntiterEntity = new AppChannelIntiterEntity();
                appChannelIntiterEntity.setAppId(applicationRecordEntity.getAppId());
                appChannelIntiterEntity.setAppUid(applicationRecordEntity.getAppUid());
                // 新增sys_user表
                this.buildSysUserEntity(appUserEntity);
                SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("acctive", appUserEntity.getUserEmail()));
                // 新增 channel_user 表
                this.buildChannelUserEntity(appUserEntity, entity, sysUserEntity);
                //修改APP用户邀请记录，修改所以邀请人uid为 entity.uid的type类型为2
                appUserIntiterDao.updateTypeAndLevelByInviterUid(applicationRecordEntity.getAppUid(), entity.getChannelLevel(), IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", sysUserEntity.getId()));
                //新增用户渠道商绑定关系
                appChannelIntiterEntity.setChannelId(channelUserEntity.getId());
                appChannelIntiterEntity.setChannelUid(String.valueOf(channelUserEntity.getUserId()));
                entity.setAppId(String.valueOf(applicationRecordEntity.getAppId()));
                entity.setAppUid(applicationRecordEntity.getAppUid());
                entity.setChannelId(String.valueOf(channelUserEntity.getId()));
                entity.setChannelUid(String.valueOf(channelUserEntity.getUserId()));
                //修改状态,同时补充渠道商信息
                applicationRecordDao.updateApplicationById(entity);
                appChannelIntiterEntity.setInvitationCode(appUserEntity.getInvitationCode());
                GenericityUtil.setDate(appChannelIntiterEntity);
                appChannelIntiterDao.insert(appChannelIntiterEntity);
               
                //发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.APP_CREATE_APPLICATION_RECORD_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						appUserEntity.getUid(), 
						appUserEntity.getUserEmail(),
                        appUserEntity.getUid(),
                        channelUserEntity.getLevelName(),
                        sysUserEntity.getGoogleSecretkey());
            } else {
                applicationRecordDao.updateStatusById(entity);
                //发送消息
                messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.APP_APPLICATION_RECORD_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						appUserEntity.getUid(), 
						appUserEntity.getUserEmail(),
                        appUserEntity.getUid());
                
                
            }
        } catch (Exception e) {
            log.error("审核记录变更异常，异常信息：{}", e.getMessage());
            throw new RuntimeException(I18nUtil.getMessage("base_error"));
        }
        return setResultSuccess();
    }

    /**
     * 构建ChannelUserEntity对象
     *
     * @param appUserEntity
     * @param entity
     * @return
     */
    public void buildChannelUserEntity(AppUserEntity appUserEntity,
                                       ApplicationRecordReqEntity entity,
                                       SysUserEntity sysUserEntity) throws InvocationTargetException, IllegalAccessException {
        ChannelUserEntity channelUserEntity = new ChannelUserEntity();
        channelUserEntity.setUserId(sysUserEntity.getId());
        channelUserEntity.setUsername(appUserEntity.getUserEmail());
        channelUserEntity.setLevelNo(entity.getChannelLevel());
        ChannelLevelEntity channelLevel = channelLevelDao.selectOne(new QueryWrapper<ChannelLevelEntity>().eq("level_no", entity.getChannelLevel()));
        channelUserEntity.setLevelName(channelLevel.getLevelName());
        channelUserEntity.setInvitationCode(appUserEntity.getInvitationCode());
        // 如果成为渠道商，默认的佣金比例是渠道商等级的直接佣金比例
        channelUserEntity.setCommissionRate(channelLevel.getDirectlyRatesFee().doubleValue());
        channelUserEntity.setCardOpeningDeductionFee(channelLevel.getPrepaidFees());
        channelUserEntity.setDeleteStatus(UserStateEnums.NORMAL.getIndex());
        GenericityUtil.setDate(channelUserEntity);
        channelUserDao.insert(channelUserEntity);
        //拉取银行卡信息存储到channel_card
        for (CardEntity cardEntity : cardDao.findList(null)) {
            ChannelCardEntity channelCardEntity = new ChannelCardEntity();
            channelCardEntity.setChannelId(channelUserEntity.getId());
            channelCardEntity.setCardId(cardEntity.getId());
            channelCardEntity.setCardUuid(cardEntity.getUuid());
            channelCardEntity.setCardType(cardEntity.getBankCardNature());
            //判断当前卡片类型 实体卡还是虚拟卡
            if (CardTypeEnums.PHYSICAL.getName().equals(cardEntity.getBankCardNature())) {
                //取消实体卡开卡费用
                channelCardEntity.setOpenFee(0.0);
            } else {
                //其他的不变
                channelCardEntity.setOpenFee(cardEntity.getOpenCardCost());
            }
            channelCardEntity.setRatesFee(cardEntity.getRechargeFee());
            channelCardEntity.setSetTime(new Date());
            channelCardEntity.setGmtModified(new Date());
            channelCardDao.insert(channelCardEntity);
        }
    }

    /**
     * 构建SysUserEntity对象
     *
     * @param appUserEntity
     * @return
     */
    public void buildSysUserEntity(AppUserEntity appUserEntity) {
        SysRoleEntity roleEntity = sysRoleDao.findRoleIdByRoleKey(ROLE_KEY);
        Integer roleId = roleEntity.getRoleId();
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setAcctive(appUserEntity.getUserEmail());
        sysUserEntity.setPassword(appUserEntity.getUserPassword());
        sysUserEntity.setUsername(appUserEntity.getUserEmail());
        sysUserEntity.setTel(appUserEntity.getMobileNumber());
        sysUserEntity.setUserState(UserStateEnums.NORMAL.getIndex());
        sysUserEntity.setCreateTime(DateUtil.DateTimeToString(new Date()));
        if (StringUtils.isNotBlank(appUserEntity.getGoogleSecretkey())) {
            sysUserEntity.setGoogleSecretkey(appUserEntity.getGoogleSecretkey());
        } else {
            //自动生成谷歌验证码
            sysUserEntity.setGoogleSecretkey(GoogleAuthenticatorUtil.createKey(appUserEntity.getUserEmail()).getKey());
        }
        sysUserDao.insert(sysUserEntity);
        sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("acctive", appUserEntity.getUserEmail()));
        //绑定用户权限
        sysUserDao.addUserOrRole(sysUserEntity.getId(), roleId);
    }
}
