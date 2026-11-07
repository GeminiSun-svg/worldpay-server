package com.worldpay.service.impl;
import com.worldpay.utils.api.UCardApi;
import com.worldpay.filter.JWTAuthenticationFilter;
import com.worldpay.dao.wallet.WithdrawalRecordDao;
import com.worldpay.entity.wallet.WithdrawalRecordEntity;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.worldpay.api.request.UserRegisterEntity;
import com.worldpay.api.response.UserAddressReq;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.*;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.template.EmailTemplateDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.*;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.system.EmailTemplateEntity;
import com.worldpay.entity.system.SysStateEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.*;
import com.worldpay.service.AppUserService;
import com.worldpay.utils.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@CrossOrigin
public class AppUserServiceImpl extends BaseApiService implements AppUserService {

    private static final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);
    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private EmailTemplateDao emailTemplateDao;

    @Autowired
    private SysStateDao sysStateDao;

    @Autowired
    private ChannelUserDao channelUserDao;

    @Autowired
    private AppUserIntiterDao appUserIntiterDao;

    @Autowired
    private AppUserIosWalletStateDao appUserIosWalletStateDao;

    @Autowired
    private AppUserCardDao appUserCardDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoogleAuthenticatorUtil googleAuthenticator;
    
    @Autowired
    private BaiDuApiServiceImpl baiDuApiServiceImpl;
    
    @Autowired
    private MessageTemplateServiceImpl messageTemplateServiceImpl;
    
    @Autowired
    private WalletServicelmpl walletServicelmpl;

    @Autowired
    private WithdrawalRecordDao withdrawalRecordDao;
    
    @Autowired
    private AppUserWebAddressDao appUserWebAddressDao;

    @Autowired
    private IpUtil ipUtil;

    @Value("${invitation.default_code:}")
    private String INVITATION_DEFAULT_CODE;
    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;
    @Autowired
    private WalletLogDao walletLogDao;

    @Override
    public ResponseBase signUp(@RequestBody AppUserEntity entity) {
        try {
            if (entity.getToInvitationCode() != null && !entity.getToInvitationCode().isEmpty()) {
                //查询邀请码是否存在
                AppUserEntity userEntity = appUserDao.findInvitationCode(entity.getToInvitationCode());
                ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                if (userEntity == null && channelUserEntity == null) {
                    return setResultError(I18nUtil.getMessage("invitationcode_null"));
                }
            } else {
                return setResultError(I18nUtil.getMessage("invitationcode_null"));
            }
            if (entity.getEmailCode() != null) {
                ResponseBase emailCodeData = this.checkEmailCode(entity.getUserEmail(), entity.getEmailCode());
                if (!Constants.HTTP_RES_CODE_200.equals(emailCodeData.getRtncode())) {
                    return setResultError(I18nUtil.getMessage("verification_error"));
                }
            } else {
                return setResultError(I18nUtil.getMessage("verification_null"));
            }
            AppUserEntity appUserEntity = appUserDao.findByEmail(entity.getUserEmail());
            if (appUserEntity != null) {
                return setResultError(I18nUtil.getMessage("email_registered"));
            }
            boolean walletState = true;
            SysStateEntity stateEntity = sysStateDao.selectOne(null);
            if (stateEntity != null) {
                walletState = stateEntity.getWalletState();
            }
            entity.setUserPassword(DigestUtils.md5DigestAsHex((entity.getUserPassword()).getBytes()));
            entity.setUserState(UserStateEnums.NORMAL.getIndex());
            entity.setWalletState(walletState);
            entity.setKycState(KycStateEnums.WAIT_APPROVE.getIndex());
            entity.setKycStateName(KycStateEnums.WAIT_APPROVE.getName());
            GenericityUtil.setDate(entity);
            int temp = appUserDao.insert(entity);
            // 调用三方注册
            UserRegisterEntity apiEntity = new UserRegisterEntity();
			apiEntity.setEmail(entity.getUserEmail());
            ResponseBase resData = UCardApi.postData(null, UCardMethods.USER_REGISTER, apiEntity, null);
            if (resData.getRtncode() == Constants.HTTP_RES_CODE_200) {
                String apiData = resData.getData().toString();
                JSONObject object = JSONObject.parseObject(apiData);
                String uid = object.get("uid").toString();
                entity.setUid(uid);
                entity.setMobileNumber(apiEntity.getMobileNumber());
                entity.setMobilePrefix(apiEntity.getMobilePrefix());
                entity.setInvitationCode(RandomSuffixInviteCodeUtil.generateUniqueCode(entity.getId(), 4, 6));
                appUserDao.updateById(entity);
                //新增邀请记录关系
                this.saveIntiter(entity);
                
                //发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.REGISTER_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						uid, 
						entity.getUserEmail());
                
                String token = Jwts.builder()
                        // 设置主题
                        .setSubject(entity.getUserEmail())
                        // 设置到期时间
                        .setExpiration(new Date(System.currentTimeMillis() + Constants.USER_JWT_EXPIRE_TIME))
                        // 选择 加密算法和私钥
                        .signWith(Constants.jwtKey()).compact();
                redisUtil.set(Constants.APP_PACKAGE_NAME + entity.getUserEmail(),
                        Constants.AUTH_HEADER_START_WITH + token, Constants.USER_REDIS_EXPIRE_TIME);
                // 事务提交后再执行身份证识别并且填写的邀请码不是888888
                if (temp > 0 && !INVITATION_DEFAULT_CODE.equals(entity.getToInvitationCode())) {
                    final String finalUid = uid;
                    final String currentLanguage = LanguageContext.getLanguage();
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            // 事务已提交，数据库中有uid了
                            log.info("事务提交后执行身份证识别, uid: {}", finalUid);
                            baiDuApiServiceImpl.idCardDiscern(finalUid,currentLanguage);
                        }
                    });
                }
                return setResultSuccess(Constants.AUTH_HEADER_START_WITH + token, I18nUtil.getMessage("base_success"));
            } else {
                appUserDao.deleteById(entity.getId());
            }
            return resData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase login(@RequestBody AppUserEntity entity, HttpServletRequest req) {
        try {
            AppUserEntity appUserEntity = appUserDao.findByEmail(entity.getUserEmail());
            if (appUserEntity != null) {
                if (!UserStateEnums.NORMAL.getIndex().equals(appUserEntity.getUserState())) {
                    return setResultError(I18nUtil.getMessage("user.account_error"));
                }
                if (entity.getUserPassword() != null
                        && DigestUtils.md5DigestAsHex((entity.getUserPassword()).getBytes())
                        .equals(appUserEntity.getUserPassword())) {
                    String token = Jwts.builder()
                            // 设置主题
                            .setSubject(appUserEntity.getUserEmail())
                            // 设置到期时间
                            .setExpiration(new Date(System.currentTimeMillis() + Constants.USER_JWT_EXPIRE_TIME))
                            // 选择 加密算法和私钥
                            .signWith(Constants.jwtKey()).compact();
                    redisUtil.set(Constants.APP_PACKAGE_NAME + appUserEntity.getUserEmail(),
                            Constants.AUTH_HEADER_START_WITH + token, Constants.USER_REDIS_EXPIRE_TIME);
                    //推送id绑定
//                    PushToUser.binding(appUserEntity.getUid(), entity.getCid());
                    if(entity.getCid() != null ) {
                    	appUserEntity.setCid(entity.getCid());
                    	appUserDao.updateById(appUserEntity);
                    }
                    //发送通知
    				messageTemplateServiceImpl.unifiedSendAsync(
    						MessageEnums.LOGIN_ZH.getIndex(), 
    						LanguageContext.getLanguage(),
    						appUserEntity.getUid(), 
    						entity.getUserEmail(),
                            entity.getDeviceName(),
                            ipUtil.getClientIp(req));

                    return setResultSuccess(Constants.AUTH_HEADER_START_WITH + token, I18nUtil.getMessage("base_success"));
                } else {
                    return setResultError(I18nUtil.getMessage("user.password_error"));
                }
            } else {
                return setResultError(I18nUtil.getMessage("user.account_error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase findToken(HttpServletRequest request) {
        try {
            String header = request.getHeader(Constants.HEADER_AUTH);
            if (header == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
            }
            UsernamePasswordAuthenticationToken userData = JWTAuthenticationFilter.getAuthentication(request);
            if (userData == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
            } else {
                String key = userData.getName();
                String redisKey = Constants.APP_PACKAGE_NAME + key;
                if (redisUtil.get(redisKey) == null || redisUtil.get(redisKey).toString().length() < 1) {
                    return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
                }
                if (!redisUtil.get(redisKey).equals(header)) {
                    return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
                }
            }
            String username = userData.getName();
            AppUserEntity entity = appUserDao.findByEmail(username);
            if (entity != null && entity.getGoogleSecretkey() != null && entity.getGoogleSecretkey().length() > 0) {
                entity.setGoogleState(true);
            } else {
                entity.setGoogleState(false);
            }
            if (entity != null && entity.getPayPassword() != null && entity.getPayPassword().length() > 0) {
                entity.setPayPasswordState(true);
            } else {
                entity.setPayPasswordState(false);
            }
            //判断是否有邀请人标记
            String invitationCode = entity.getInvitationCode();
            if (invitationCode == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("invitation_code_null"), null);
            }
            //判断是否绑定渠道商账户
            AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                    new QueryWrapper<AppChannelIntiterEntity>()
                            .eq("app_uid", entity.getUid()));
            if (appChannelIntiterEntity != null) {
                //已绑定
                entity.setBandingChannel(true);
                //绑定的渠道商的uid
                entity.setBandingChannelUid(appChannelIntiterEntity.getChannelUid());
            } else {
                entity.setBandingChannel(false);
            }
//			entity.setGoogleSecretkey(null);
            entity.setPayPassword(null);
            entity.setUserPassword(null);
            //查询用户邀请人
            AppUserIntiterEntity userIntiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
            if (userIntiterEntity != null) {
                entity.setInvitationLable(userIntiterEntity.getIntiterUserType());
            } else {
                entity.setInvitationLable(IntiterUserTypeEnums.NOT_USER.getIndex());
            }
            return setResultSuccess(entity);
        } catch (Exception e) {
        	return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
        }
    }

    @Override
    public ResponseBase sendEmailCode(@Email(message = "Invalid email format")
                                      @NotNull(message = "Email cannot be null") String userEmail) {
        try {
            String language = LanguageContext.getLanguage();
            EmailTemplateEntity templateEntity = emailTemplateDao.findByNum(MessageEnums.SEND_CODE_ZH.getIndex(), language);
            if (templateEntity != null && templateEntity.getTemplateContent() != null
                    && templateEntity.getTemplateContent().length() > 0) {
                String code = OrderCodeFactory.getRandomStr(6);
                // System.out.println("验证码*************："+code);
//				//添加动态数据
                String htmlContent = MessageFormatUtils.format(
                        templateEntity.getTemplateContent(),
                        userEmail,
                        code);
                //组装html内容
                String html = MessageFormatUtils.saveHtml(htmlContent, language);
                EmailUtil.sendEmail(userEmail, templateEntity.getTemplateSubject(), html);
//				MimeMessage message = mailSender.createMimeMessage();
//				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//				helper.setFrom(sendEmail);
//				helper.setTo(userEmail);
//				helper.setSubject(templateEntity.getTemplateSubject());
//				// 设置邮件内容为 HTML 格式
//				helper.setText(html, true);
//				mailSender.send(message);
                redisUtil.set(userEmail, code, Constants.CODE_EXPIRE_TIME);
                return setResultSuccess(I18nUtil.getMessage("send_success"));
            } else {
                return setResultError(I18nUtil.getMessage("Template_null"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase checkEmailCode(String userEmail, String emailCode) {
        try {
            String temp = null;
            if (redisUtil.get(userEmail) != null) {
                temp = redisUtil.get(userEmail).toString();
            }
            if (temp != null && emailCode.equals(temp)) {
                return setResultSuccess();
            } else {
                return setResultError(I18nUtil.getMessage("verification_error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase userinfo(HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
            String username = token.getName();
            AppUserEntity entity = appUserDao.findByEmail(username);
            AppUserIntiterEntity userIntiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
            if (userIntiterEntity != null) {
                entity.setInvitationLable(userIntiterEntity.getIntiterUserType());
            } else {
                entity.setInvitationLable(IntiterUserTypeEnums.NOT_USER.getIndex());
            }
            //ios端钱包、usdt充值入口状态查询
            AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(entity.getUid());
            if (stateEntity != null) {
                entity.setIosWalledState(stateEntity.getWalletState());
                entity.setIosTopUpState(stateEntity.getTopUpState());
            } else {
                //查询用户是否已申请银行卡，如果申请银行卡则开启钱包入口
                List<AppUserCardEntity> userCardEntities = appUserCardDao.findUid(entity.getUid());
                if (userCardEntities != null && userCardEntities.size() > 0) {
                    entity.setIosWalledState(OpenStateEnums.OPEN.getIndex());
                } else {
                    entity.setIosWalledState(OpenStateEnums.CLOSURE.getIndex());
                }
                entity.setIosTopUpState(OpenStateEnums.OPEN.getIndex());
                AppUserIntiterEntity intiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
                if (intiterEntity != null) {
                    //查询渠道商邀请码
                    ChannelUserEntity channelUserEntity = channelUserDao.selectById(intiterEntity.getInviterUser());
                    if (channelUserEntity != null && INVITATION_DEFAULT_CODE.equals(channelUserEntity.getInvitationCode())) {
                        entity.setIosWalledState(OpenStateEnums.CLOSURE.getIndex());
                        entity.setIosTopUpState(OpenStateEnums.CLOSURE.getIndex());
                    }
                }
                AppUserIosWalletStateEntity userIosWalletStateEntity = new AppUserIosWalletStateEntity();
                userIosWalletStateEntity.setAppUserId(entity.getId());
                userIosWalletStateEntity.setAppUserUid(entity.getUid());
                userIosWalletStateEntity.setTopUpState(entity.getIosTopUpState());
                userIosWalletStateEntity.setWalletState(entity.getIosWalledState());
                GenericityUtil.setDate(userIosWalletStateEntity);
                appUserIosWalletStateDao.insert(userIosWalletStateEntity);
            }
            return setResultSuccess(entity);
        } catch (Exception e) {
        	return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
        }
    }

    @SuppressWarnings("static-access")
    public ResponseBase checkGoogleSecretkey(String uid, Integer googleCode) {
        try {
            AppUserEntity entity = appUserDao.findByUid(uid);
            if (entity != null && entity.getGoogleSecretkey() != null && entity.getGoogleSecretkey().length() > 0) {
                Boolean temp = googleAuthenticator.verifyCode(entity.getGoogleSecretkey(), googleCode);
                if (temp) {
                    return setResultSuccess(true);
                }
            }
            return setResultSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase googleCode(String email) {
        try {
            AppUserEntity entity = appUserDao.findByEmail(email);
            if (entity != null && entity.getGoogleSecretkey() != null) {
                return setResultSuccess(true);
            }
            return setResultSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseBase cashAmount(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        Double withdrawableBalance = appUserEntity.getWithdrawableBalance();
        return setResultSuccess(withdrawableBalance == null ? 0.0 : withdrawableBalance);
    }

    @Override
    public ResponseBase withdrawalToWallet(AppUserEntity entity, HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String username = JwtUtil.getUserNameByJwt(token);
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
            if (appUserEntity == null) {
                return setResultError(I18nUtil.getMessage("user.not_null"));
            }
            String payPassword = appUserEntity.getPayPassword();
            if (payPassword == null) {
                return setResultError(I18nUtil.getMessage("pay_password_null"));
            }
            String paymentCode = entity.getPayPassword();
            if (paymentCode == null) {
                return setResultError(I18nUtil.getMessage("pay_password_null"));
            }
            if (!DigestUtils.md5DigestAsHex(paymentCode.getBytes()).equals(payPassword)) {
                return setResultError(I18nUtil.getMessage("pay_password_error"));
            }
            //钱包金额
            Double walletBalance = appUserEntity.getWalletBalance();
            //可提现金额
            Double withdrawableBalance = appUserEntity.getWithdrawableBalance();
            appUserEntity.setWalletBalance((walletBalance == null ? 0.0 : walletBalance) + (withdrawableBalance == null ? 0.0 : withdrawableBalance));
            appUserEntity.setWithdrawableBalance(0.0);
            appUserEntity.setGmtModified(new Date());
            appUserDao.updateById(appUserEntity);
            //钱包交易记录
            buildWalletLog(appUserEntity,withdrawableBalance);
            //提现记录
            buildWithDrawalRecord(appUserEntity,walletBalance,withdrawableBalance);
            log.info("用户{}，提现金额：{}成功", appUserEntity.getUserEmail(), withdrawableBalance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return setResultSuccess();
    }

    /**
     * 构建提现记录
     * @param appUserEntity 用户信息
     * @param walletBalance 钱包金额
     * @param withdrawableBalance 提现余额
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    void buildWithDrawalRecord(AppUserEntity appUserEntity,Double walletBalance,Double withdrawableBalance) throws InvocationTargetException, IllegalAccessException {
        WithdrawalRecordEntity withdrawalRecordEntity = new WithdrawalRecordEntity();
        withdrawalRecordEntity.setAppId(appUserEntity.getId());
        withdrawalRecordEntity.setAppUid(appUserEntity.getUid());
        withdrawalRecordEntity.setUserEmail(appUserEntity.getUserEmail());
        withdrawalRecordEntity.setBeforeWithdrawalWalletBalance(walletBalance);
        Double afterWalletBalance = walletBalance + withdrawableBalance;
        withdrawalRecordEntity.setAfterWithdrawalWalletBalance(afterWalletBalance);
        withdrawalRecordEntity.setWithdrawalWalletAmount(withdrawableBalance);
        GenericityUtil.setDate(withdrawalRecordEntity);
        withdrawalRecordDao.insert(withdrawalRecordEntity);
    }


    /**
     * 新增钱包交易记录
     * @param appUserEntity 用户信息
     * @param withdrawableBalance 交易金额
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    void buildWalletLog(AppUserEntity appUserEntity,Double withdrawableBalance) throws InvocationTargetException, IllegalAccessException {
        String orderId = OrderCodeFactory.getOrderCode(Long.parseLong(appUserEntity.getUid()));
        WalletLogEntity walletLogEntity = new WalletLogEntity();
        walletLogEntity.setOrderNo(orderId);
        walletLogEntity.setOutOrderNo(orderId);
        walletLogEntity.setUid(appUserEntity.getUid());
        walletLogEntity.setUserId(appUserEntity.getId());
        walletLogEntity.setPrimevalMoney(appUserEntity.getWalletBalance());
        walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
        walletLogEntity.setTitle(WalletTitleTypeEnums.COMMISSION_WITHDRAWAL.getCnName());
        walletLogEntity.setOperateType(WalletTitleTypeEnums.COMMISSION_WITHDRAWAL.getIndex());
        walletLogEntity.setTradeType(TradeTypeEnums.INCOME.getIndex());
        walletLogEntity.setRealMoney(BigDecimal.valueOf(withdrawableBalance == null ? 0.0 : withdrawableBalance));
        walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
        walletLogEntity.setFormName(appUserEntity.getUserEmail());
        walletLogEntity.setFormAccount(appUserEntity.getUid());
        walletLogEntity.setToName(appUserEntity.getUserEmail());
        walletLogEntity.setToAccount(appUserEntity.getUid());
        walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
        GenericityUtil.setDate(walletLogEntity);
        walletLogDao.insert(walletLogEntity);
    }

    public AppUserIntiterEntity saveIntiter(AppUserEntity entity) {
        try {
            if (entity.getToInvitationCode() != null && entity.getToInvitationCode().length() > 0) {
                AppUserIosWalletStateEntity stateEntity = new AppUserIosWalletStateEntity();
                stateEntity.setAppUserId(entity.getId());
                stateEntity.setAppUserUid(entity.getUid());
                stateEntity.setWalletState(OpenStateEnums.CLOSURE.getIndex());
                //处理邀请关系
                AppUserIntiterEntity intiterEntity = new AppUserIntiterEntity();
                //被邀请人信息
                intiterEntity.setCoverInviterUser(entity.getId());
                intiterEntity.setCoverInviterUserUid(entity.getUid());
                AppUserEntity userEntity = appUserDao.findInvitationCode(entity.getToInvitationCode());
                boolean isBanding = false;
                if (userEntity != null) {
                    intiterEntity.setInviterUser(userEntity.getId());
                    intiterEntity.setInviterUserUid(userEntity.getUid());
                    //存在申请记录并且申请通过
                    AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                            new QueryWrapper<AppChannelIntiterEntity>()
                                    .eq("app_uid", entity.getUid()));
                    if (appChannelIntiterEntity != null) {
                        isBanding = true;
                        intiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                        ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                        if (channelUserEntity != null) {
                            intiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
                        }
                    } else {
                        intiterEntity.setIntiterUserType(IntiterUserTypeEnums.APP_USER.getIndex());
                    }
                }
                if (!isBanding) {
                    ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                    if (channelUserEntity != null) {
                        intiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
                        intiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                        intiterEntity.setInviterUser(channelUserEntity.getId());
                        intiterEntity.setInviterUserUid(channelUserEntity.getUserId().toString());
                    }
                }
                GenericityUtil.setDate(intiterEntity);
                appUserIntiterDao.insert(intiterEntity);
                return intiterEntity;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public ResponseBase signUpV2(@RequestBody AppUserEntity entity) {
        try {
            if (entity.getToInvitationCode() != null && !entity.getToInvitationCode().isEmpty()) {
                //查询邀请码是否存在
                AppUserEntity userEntity = appUserDao.findInvitationCode(entity.getToInvitationCode());
                ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                if (userEntity == null && channelUserEntity == null) {
                    return setResultError(I18nUtil.getMessage("invitationcode_null"));
                }
            } else {
                return setResultError(I18nUtil.getMessage("invitationcode_null"));
            }
            if (entity.getEmailCode() != null) {
                ResponseBase emailCodeData = this.checkEmailCode(entity.getUserEmail(), entity.getEmailCode());
                if (!Constants.HTTP_RES_CODE_200.equals(emailCodeData.getRtncode())) {
                    return setResultError(I18nUtil.getMessage("verification_error"));
                }
            } else {
                return setResultError(I18nUtil.getMessage("verification_null"));
            }
            AppUserEntity appUserEntity = appUserDao.findByEmail(entity.getUserEmail());
            if (appUserEntity != null) {
                return setResultError(I18nUtil.getMessage("email_registered"));
            }
            boolean walletState = true;
            SysStateEntity stateEntity = sysStateDao.selectOne(null);
            if (stateEntity != null) {
                walletState = stateEntity.getWalletState();
            }
            entity.setUserPassword(DigestUtils.md5DigestAsHex((entity.getUserPassword()).getBytes()));
            entity.setUserState(UserStateEnums.NORMAL.getIndex());
            entity.setWalletState(walletState);
            entity.setKycState(KycStateEnums.WAIT_APPROVE.getIndex());
            entity.setKycStateName(KycStateEnums.WAIT_APPROVE.getName());
            GenericityUtil.setDate(entity);
            int temp = appUserDao.insert(entity);
            // 调用三方注册
            UserRegisterEntity apiEntity = new UserRegisterEntity();
            apiEntity.setEmail(entity.getUserEmail());
//            apiEntity.setMobileNumber(OrderCodeFactory.getRandomStr(11));
//            apiEntity.setMobilePrefix(Constants.MOBILEPREFIX);
            ResponseBase resData = UCardApi.postData(null, UCardMethods.USER_REGISTER, apiEntity, null);
            if (resData.getRtncode() == Constants.HTTP_RES_CODE_200) {
                String apiData = resData.getData().toString();
                JSONObject object = JSONObject.parseObject(apiData);
                String uid = object.get("uid").toString();
                entity.setUid(uid);
                entity.setMobileNumber(apiEntity.getMobileNumber());
                entity.setMobilePrefix(apiEntity.getMobilePrefix());
                entity.setInvitationCode(RandomSuffixInviteCodeUtil.generateUniqueCode(entity.getId(), 4, 6));
                appUserDao.updateById(entity);
                //新增邀请记录关系
                this.saveIntiterV2(entity);
              
                //发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.REGISTER_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						entity.getUserEmail());
                
                String token = Jwts.builder()
                        // 设置主题
                        .setSubject(entity.getUserEmail())
                        // 设置到期时间
                        .setExpiration(new Date(System.currentTimeMillis() + Constants.USER_JWT_EXPIRE_TIME))
                        // 选择 加密算法和私钥
                        .signWith(Constants.jwtKey()).compact();
                redisUtil.set(Constants.APP_PACKAGE_NAME + entity.getUserEmail(),
                        Constants.AUTH_HEADER_START_WITH + token, Constants.USER_REDIS_EXPIRE_TIME);
             // 事务提交后再执行身份证识别
                if (temp > 0 && !INVITATION_DEFAULT_CODE.equals(entity.getToInvitationCode())) {
                    final String finalUid = uid;
                    final String currentLanguage = LanguageContext.getLanguage();
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            // 事务已提交，数据库中有uid了
                            log.info("事务提交后执行身份证识别, uid: {}", finalUid);
                            baiDuApiServiceImpl.idCardDiscern(finalUid,currentLanguage);
                        }
                    });
                }
                return setResultSuccess(Constants.AUTH_HEADER_START_WITH + token, I18nUtil.getMessage("base_success"));
            } else {
                appUserDao.deleteById(entity.getId());
            }
            return resData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public AppUserIntiterEntity saveIntiterV2(AppUserEntity entity) {
        try {
            if (entity.getToInvitationCode() != null && entity.getToInvitationCode().length() > 0) {
                AppUserIosWalletStateEntity stateEntity = new AppUserIosWalletStateEntity();
                stateEntity.setAppUserId(entity.getId());
                stateEntity.setAppUserUid(entity.getUid());
                //判断邀请码是否是默认邀请码，是默认邀请码则关闭USDT充值入口
                if (INVITATION_DEFAULT_CODE.equals(entity.getToInvitationCode())) {//默认邀请码
                    stateEntity.setTopUpState(OpenStateEnums.CLOSURE.getIndex());
                    stateEntity.setWalletState(OpenStateEnums.CLOSURE.getIndex());
                } else {
                    stateEntity.setTopUpState(OpenStateEnums.OPEN.getIndex());
                    stateEntity.setWalletState(OpenStateEnums.OPEN.getIndex());
                }
                GenericityUtil.setDate(stateEntity);
                appUserIosWalletStateDao.insert(stateEntity);
                //处理邀请关系
                AppUserIntiterEntity intiterEntity = new AppUserIntiterEntity();
                boolean isBanding = false;
                AppUserEntity userEntity = appUserDao.findInvitationCode(entity.getToInvitationCode());
                if (userEntity != null) {
                    //存在绑定关系
                    AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                            new QueryWrapper<AppChannelIntiterEntity>()
                                    .eq("app_uid", entity.getUid()));
                    if (appChannelIntiterEntity != null) {
                        isBanding = true;
                        intiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                        ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                        if (channelUserEntity != null) {
                            intiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
                        }
                    }
                    intiterEntity.setCoverInviterUser(entity.getId());
                    intiterEntity.setCoverInviterUserUid(entity.getUid());
                    intiterEntity.setIntiterUserType(IntiterUserTypeEnums.APP_USER.getIndex());
                    intiterEntity.setInviterUser(userEntity.getId());
                    intiterEntity.setInviterUserUid(userEntity.getUid());
                }
                if (!isBanding) {
                    ChannelUserEntity channelUserEntity = channelUserDao.findInvitationCode(entity.getToInvitationCode());
                    if (channelUserEntity != null) {
                        intiterEntity.setCoverInviterUser(entity.getId());
                        intiterEntity.setCoverInviterUserUid(entity.getUid());
                        intiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
                        intiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                        intiterEntity.setInviterUser(channelUserEntity.getId());
                        intiterEntity.setInviterUserUid(channelUserEntity.getUserId().toString());
                    }
                }
                GenericityUtil.setDate(intiterEntity);
                appUserIntiterDao.insert(intiterEntity);
                return intiterEntity;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public ResponseBase findTokenV2(HttpServletRequest request) {
        try {
            String header = request.getHeader(Constants.HEADER_AUTH);
            if (header == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
            }
            UsernamePasswordAuthenticationToken userData = JWTAuthenticationFilter.getAuthentication(request);
            if (userData == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
            } else {
                String key = userData.getName();
                String redisKey = Constants.APP_PACKAGE_NAME + key;
                if (redisUtil.get(redisKey) == null || redisUtil.get(redisKey).toString().length() < 1) {
                    return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
                }
                if (!redisUtil.get(redisKey).equals(header)) {
                    return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
                }
            }
            String username = userData.getName();
            AppUserEntity entity = appUserDao.findByEmail(username);
            if (entity != null && entity.getGoogleSecretkey() != null && entity.getGoogleSecretkey().length() > 0) {
                entity.setGoogleState(true);
            } else {
                entity.setGoogleState(false);
            }
            if (entity != null && entity.getPayPassword() != null && entity.getPayPassword().length() > 0) {
                entity.setPayPasswordState(true);
            } else {
                entity.setPayPasswordState(false);
            }
            //判断是否有邀请人标记
            String invitationCode = entity.getInvitationCode();
            if (invitationCode == null) {
                return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("invitation_code_null"), null);
            }
            //判断是否绑定渠道商账户
            AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(
                    new QueryWrapper<AppChannelIntiterEntity>()
                            .eq("app_uid", entity.getUid()));
            if (appChannelIntiterEntity != null) {
                //已绑定
                entity.setBandingChannel(true);
                //绑定的渠道商的uid
                entity.setBandingChannelUid(appChannelIntiterEntity.getChannelUid());
            } else {
                entity.setBandingChannel(false);
            }
//			entity.setGoogleSecretkey(null);
            entity.setPayPassword(null);
            entity.setUserPassword(null);
            //查询用户邀请人
            AppUserIntiterEntity userIntiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
            if (userIntiterEntity != null) {
                entity.setInvitationLable(userIntiterEntity.getIntiterUserType());
            } else {
                entity.setInvitationLable(IntiterUserTypeEnums.NOT_USER.getIndex());
            }
            //ios端钱包、usdt充值入口状态查询
            AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(entity.getUid());
            if (stateEntity != null) {
                entity.setIosWalledState(stateEntity.getWalletState());
                entity.setIosTopUpState(stateEntity.getTopUpState());
            } else {
                //查询用户是否已申请银行卡，如果申请银行卡则开启钱包入口
//                List<AppUserCardEntity> userCardEntities = appUserCardDao.findUid(entity.getUid());
//                if (userCardEntities != null && userCardEntities.size() > 0) {
//                    entity.setIosWalledState(OpenStateEnums.OPEN.getIndex());
//                } else {
//                    entity.setIosWalledState(OpenStateEnums.CLOSURE.getIndex());
//                }
            	entity.setIosWalledState(OpenStateEnums.OPEN.getIndex());
                entity.setIosTopUpState(OpenStateEnums.OPEN.getIndex());
                AppUserIntiterEntity intiterEntity = appUserIntiterDao.findByUidCoverInviterUser(entity.getUid());
                if (intiterEntity != null) {
                    //查询渠道商邀请码
                    ChannelUserEntity channelUserEntity = channelUserDao.selectById(intiterEntity.getInviterUser());
                    if (channelUserEntity != null && INVITATION_DEFAULT_CODE.equals(channelUserEntity.getInvitationCode())) {
                        entity.setIosWalledState(OpenStateEnums.CLOSURE.getIndex());
                        entity.setIosTopUpState(OpenStateEnums.CLOSURE.getIndex());
                    }
                }
                AppUserIosWalletStateEntity userIosWalletStateEntity = new AppUserIosWalletStateEntity();
                userIosWalletStateEntity.setAppUserId(entity.getId());
                userIosWalletStateEntity.setAppUserUid(entity.getUid());
                userIosWalletStateEntity.setTopUpState(entity.getIosTopUpState());
                userIosWalletStateEntity.setWalletState(entity.getIosWalledState());
                GenericityUtil.setDate(userIosWalletStateEntity);
                appUserIosWalletStateDao.insert(userIosWalletStateEntity);
            }
            return setResultSuccess(entity);
        } catch (Exception e) {
        	e.printStackTrace();
        	return setResult(Constants.HTTP_RES_CODE_403, I18nUtil.getMessage("token_error"), null);
        }
    }

	@Override
	public ResponseBase findKycState(String uid) {
		try {
			Integer temp = appUserDao.findKycState(uid);
			return setResultSuccess(temp, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase kycRetry(String uid) {
		String currentLanguage = LanguageContext.getLanguage();
		baiDuApiServiceImpl.idCardDiscern(uid,currentLanguage);
		return setResultSuccess();
	}
	
	
	public ResponseBase checkPayPasswrod(String uid, String password) {
		try {
			if (password == null || password.isEmpty()) {
				return setResultError(I18nUtil.getMessage("pay_password_entry"));
			}
			AppUserEntity userEntity = appUserDao.findByUid(uid);
			if (userEntity != null) {
				if (userEntity.getPayPassword() != null && userEntity.getPayPassword().length() > 0) {
					if (!DigestUtils.md5DigestAsHex((password).getBytes())
							.equals(userEntity.getPayPassword())) {
						return setResultError(I18nUtil.getMessage("pay_password_error"));
					}
				} else {
					return setResult(Constants.HTTP_RES_CODE_602, I18nUtil.getMessage("pay_password_null"), null);
				}
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("user.account_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findAddress(String uid) {
		List<UserAddressReq> list = new ArrayList<UserAddressReq>();
		AppUserWebAddressEntity addressEntity = appUserWebAddressDao.findByUid(uid);
		if(addressEntity != null) {
			list = walletServicelmpl.formatAddress(addressEntity);			
		}
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase kycRetryAll() {
		List<AppUserEntity> list = appUserDao.selectList(null);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String currentLanguage = LanguageContext.getLanguage();
				baiDuApiServiceImpl.idCardDiscern(list.get(i).getUid(),currentLanguage);
			}
		}
		return setResultSuccess();

	}

}
