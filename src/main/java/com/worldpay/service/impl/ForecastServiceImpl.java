package com.worldpay.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.forecast.ForecastSlideshowDao;
import com.worldpay.dao.forecast.ForecastUserDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.dao.wallet.WalletLogDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.forecast.ForecastSlideshowEntity;
import com.worldpay.entity.forecast.ForecastUserRecordEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.enums.*;
import com.worldpay.service.ForecastService;
import com.worldpay.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预测模块实现
 *
 * @author GeminiSun
 * @date 2026/3/9
 */
@Slf4j
@Service
@Transactional
@CrossOrigin
public class ForecastServiceImpl extends BaseApiService implements ForecastService {

    @Autowired
    private ForecastUserDao forecastUserDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private WalletLogDao walletLogDao;

    @Autowired
    private MessageTemplateServiceImpl messageTemplateServiceImpl;


    @Value("${forecast.apikey:}")
    private String apikey;

    @Value("${forecast.url:}")
    private String url;

    @Value("${forecast.secret:}")
    private String secret;

    @Resource
    private ForecastSlideshowDao forecastSlideshowDao;


    // API 路径常量
    private static final String CREATE_USER_URL = "merchant/createuser";
    private static final String LOGIN_FORECAST_URL = "merchant/login";

    // 响应码常量
    private static final int CODE_SUCCESS = 1;
    // 用户已存在，需登录
    private static final int CODE_USER_EXISTS = 1401;

    @Override
    public ResponseBase forecastLogin(HttpServletRequest request) {
        try {
            // 1. 从 token 获取用户名
            String username = extractUsernameFromToken(request);
            // 2. 获取本地用户信息（系统用户或 App 用户）
            LocalUserInfo localUser = getLocalUserInfo(username);
            // 3. 检查预测用户记录是否存在
            ForecastUserRecordEntity forecastUser = forecastUserDao.selectOne(
                    new QueryWrapper<ForecastUserRecordEntity>().eq("user_name", username));
            // 4. 根据是否存在记录执行对应逻辑
            if (forecastUser == null) {
                return handleUserNotExist(username, localUser);
            } else {
                return handleUserExist(username);
            }
        } catch (Exception e) {
            log.error("预测功能登录异常", e);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
    }

    @Override
    public ResponseBase getForecastSlideshow(HttpServletRequest request) {
        String language = LanguageContext.getLanguage();
        List<ForecastSlideshowEntity> forecastSlideshows = forecastSlideshowDao.selectList(new QueryWrapper<ForecastSlideshowEntity>().eq("language", language));
        return setResultSuccess(forecastSlideshows,I18nUtil.getMessage("base_success"));
    }

    @Override
    public ResponseBase forecastLoginV2(HttpServletRequest request) {
        try {
            // 1. 从 token 获取用户名
            String username = extractUsernameFromToken(request);
            // 2. 获取本地用户信息（系统用户或 App 用户）
            LocalUserInfo localUser = getLocalUserInfo(username);
            // 3. 检查预测用户记录是否存在
            ForecastUserRecordEntity forecastUser = forecastUserDao.selectOne(
                    new QueryWrapper<ForecastUserRecordEntity>().eq("user_name", username));
            // 4. 根据是否存在记录执行对应逻辑
            if (forecastUser == null) {
                return handleUserNotExist(username, localUser);
            } else {
                return handleUserExistV2(username);
            }
        } catch (Exception e) {
            log.error("预测功能登录异常", e);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
    }

    @Override
    public String callback(String json) {
        log.info("入参{}", json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        try {
            //校验sign
            boolean flag = checkSing(jsonObject);
            if (!flag) {
                log.error("sign校验失败");
                return "error";
            }
            //充值用户
            String userName = jsonObject.getString("userid");
            //充值金额
            Double amount = jsonObject.getDouble("money");
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", userName));
            //修改用户钱包余额
            Integer type = jsonObject.getInteger("type");
            Double walletBalance = appUserEntity.getWalletBalance();
            if (ForecastEnums.TOP_UP.getIndex().equals(type)) {
                appUserEntity.setWalletBalance(walletBalance - amount);
                //钱包记录表添加记录
                saveWalletLogEntity(appUserEntity, amount, TradeTypeEnums.EXPENDITURE.getIndex(), WalletTitleTypeEnums.FORECAST_TOP_UP);
            } else {
                appUserEntity.setWalletBalance(walletBalance + amount);
                saveWalletLogEntity(appUserEntity, amount, TradeTypeEnums.INCOME.getIndex(), WalletTitleTypeEnums.FORECAST_WITHDRAWAL);
            }
            appUserEntity.setGmtModified(new Date());
            appUserDao.updateById(appUserEntity);
            //异步发送消息推送
            syncMessage(appUserEntity, amount, ForecastEnums.TOP_UP.getIndex().equals(type)
                    ? WalletTitleTypeEnums.FORECAST_TOP_UP : WalletTitleTypeEnums.FORECAST_WITHDRAWAL);
            return "success";
        } catch (Exception e) {
            log.error("预测充值回调记录异常，异常信息：", e);
            return "error";
        }
    }

    @Override
    public ResponseBase saveSlideshow(@RequestBody ForecastSlideshowEntity entity) {
        try {
            GenericityUtil.setDate(entity);
            forecastSlideshowDao.insert(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return setResultSuccess();
    }

    /**
     * 校验sign
     *
     * @param jsonObject entity
     * @return boolean
     */
    private boolean checkSing(JSONObject jsonObject) {
        String inSign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> params = gson.fromJson(String.valueOf(jsonObject), type);
        String sign = SignUtils.makeSign(params, secret);
        return sign.equals(inSign);
    }


    /**
     * 构建钱包记录
     *
     * @param appUserEntity        用户信息
     * @param amount               金额
     * @param tradeType            收入 or 支出
     * @param walletTitleTypeEnums 钱包操作类型
     * @throws InvocationTargetException error
     * @throws IllegalAccessException    error
     */
    public void saveWalletLogEntity(AppUserEntity appUserEntity, Double amount, Integer tradeType, WalletTitleTypeEnums walletTitleTypeEnums) throws InvocationTargetException, IllegalAccessException {
        String orderId = OrderCodeFactory.getOrderCode(Long.parseLong(appUserEntity.getUid()));
        WalletLogEntity walletLogEntity = new WalletLogEntity();
        walletLogEntity.setOrderNo(orderId);
        walletLogEntity.setOutOrderNo(orderId);
        walletLogEntity.setUserId(appUserEntity.getId());
        walletLogEntity.setPrimevalMoney(appUserEntity.getWalletBalance());
        walletLogEntity.setPrimevalMoneyUnit(TransCurrencyEnums.USD.getName());
        walletLogEntity.setTitle(walletTitleTypeEnums.getCnName());
        walletLogEntity.setOperateType(walletTitleTypeEnums.getIndex());
        walletLogEntity.setTradeType(tradeType);
        walletLogEntity.setRealMoney(BigDecimal.valueOf(amount));
        walletLogEntity.setServiceCharge(BigDecimal.valueOf(0));
        walletLogEntity.setFormName(appUserEntity.getUserEmail());
        walletLogEntity.setFormAccount(appUserEntity.getUid());
        walletLogEntity.setToName(appUserEntity.getUserEmail());
        walletLogEntity.setToAccount(appUserEntity.getUid());
        walletLogEntity.setStatus(OrderStateEnums.POSTED.getIndex().toString());
        GenericityUtil.setDate(walletLogEntity);
        walletLogDao.insert(walletLogEntity);
    }

    /**
     * 推送app消息，以及邮件消息
     *
     * @param appUserEntity        app用户信息
     * @param amount               金额
     * @param walletTitleTypeEnums 充值 or 提现
     */
    public void syncMessage(AppUserEntity appUserEntity, Double amount, WalletTitleTypeEnums walletTitleTypeEnums) {
      //发送通知
		messageTemplateServiceImpl.unifiedSendAsync(
				MessageEnums.FORECAST_AMOUNT_ZH.getIndex(), 
				LanguageContext.getLanguage(),
				appUserEntity.getUid(), 
				appUserEntity.getUserEmail(),
                appUserEntity.getUid(),
                walletTitleTypeEnums.getCnName(),
                amount);
    }

    /**
     * 从请求头中提取用户名
     */
    private String extractUsernameFromToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        if (username == null) {
            throw new RuntimeException(I18nUtil.getMessage("user.not_null"));
        }
        return username;
    }

    /**
     * 根据用户名查询本地用户（系统用户或 APP 用户）
     */
    private LocalUserInfo getLocalUserInfo(String username) {
        // 优先查询系统用户
        SysUserEntity sysUser = sysUserDao.selectOne(
                new QueryWrapper<SysUserEntity>().eq("acctive", username).eq("user_state", "1"));
        if (sysUser != null) {
            return LocalUserInfo.of(sysUser.getId().toString(), UserIdentityEnums.SYSTEM.getIndex());
        }
        // 再查询 APP 用户
        AppUserEntity appUser = appUserDao.selectOne(
                new QueryWrapper<AppUserEntity>().eq("user_email", username).eq("user_state", "1"));
        if (appUser != null) {
            return LocalUserInfo.of(appUser.getUid(), UserIdentityEnums.APP.getIndex());
        }
        // 用户不存在
        throw new RuntimeException(I18nUtil.getMessage("user.not_null"));
    }

    /**
     * 处理用户不存在于预测表的情况（需创建账号）
     */
    private ResponseBase handleUserNotExist(String username, LocalUserInfo localUser) throws InvocationTargetException, IllegalAccessException {
        // 构建请求参数
        Map<String, String> headers = buildHeaders();
        JSONObject body = buildRequestBody(username);

        // 调用创建用户接口
        String reqUrl = url + CREATE_USER_URL;
        String createResult = HttpUtil.doPostNative(reqUrl, headers, body.toJSONString());
        JSONObject response = JSONObject.parseObject(createResult);

        if (response == null) {
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        int code = response.getIntValue("code");
        if (code == CODE_SUCCESS) {
            // 创建成功，保存记录
            String polygonAddress = response.getJSONObject("data").getString("polygon_address");
            String polProxyAddress = response.getJSONObject("data").getString("pol_proxyAddress");
            saveForecastUser(username, localUser, polygonAddress, polProxyAddress);
            log.info("创建账号成功，username: {}", username);
            return setResultSuccess(I18nUtil.getMessage("forecast_success"));
        } else if (code == CODE_USER_EXISTS) {
            // 用户已存在，执行登录
            return performLogin(username);
        } else {
            log.warn("创建账号失败，code: {}, response: {}", code, createResult);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
    }


    /**
     * 处理用户已存在预测表的情况（直接登录）
     */
    private ResponseBase handleUserExistV2(String username) {
        return performLoginV2(username);
    }

    /**
     * 执行登录请求，返回登录 URL
     */
    private ResponseBase performLoginV2(String username) {
        // 参数校验
        if (StringUtils.isBlank(username)) {
            log.warn("登录失败，用户名为空");
            return setResultError(I18nUtil.getMessage("base_error"));
        }


        // 构建请求并发送
        Map<String, String> headers = buildHeaders();
        JSONObject body = buildRequestBody(username);
        String loginResult;
        try {
            loginResult = HttpUtil.doPostNative(url + LOGIN_FORECAST_URL, headers, body.toJSONString());
        } catch (Exception e) {
            log.error("HTTP请求异常", e);
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        // 解析响应
        JSONObject response;
        try {
            response = JSONObject.parseObject(loginResult);
        } catch (Exception e) {
            log.error("解析登录响应失败: {}", loginResult, e);
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        if (response == null || response.getIntValue("code") != CODE_SUCCESS) {
            log.warn("登录失败，response: {}", loginResult);
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        JSONObject data = response.getJSONObject("data");
        if (data == null) {
            log.warn("登录响应中 data 为空，response: {}", loginResult);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        String loginUrl = data.getString("url");
        if (StringUtils.isBlank(loginUrl)) {
            log.warn("登录响应中 url 为空，response: {}", loginResult);
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        // 查询用户信息
        AppUserEntity appUserEntity;
        try {
            appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        } catch (Exception e) {
            log.error("查询用户信息失败，username: {}", username, e);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        if (appUserEntity == null) {
            log.warn("用户不存在，username: {}", username);
            return setResultError(I18nUtil.getMessage("base_error"));
        }

        log.info("登录成功，username: {}, loginUrl: {}", username, loginUrl);
        return setResultSuccess(loginUrl, "success");
    }

    /**
     * 处理用户已存在预测表的情况（直接登录）
     */
    private ResponseBase handleUserExist(String username) {
        return performLogin(username);
    }

    /**
     * 执行登录请求，返回登录 URL
     */
    private ResponseBase performLogin(String username) {
        Map<String, String> headers = buildHeaders();
        JSONObject body = buildRequestBody(username);

        String loginResult = HttpUtil.doPostNative(url + LOGIN_FORECAST_URL, headers, body.toJSONString());
        JSONObject response = JSONObject.parseObject(loginResult);

        if (response == null || response.getIntValue("code") != CODE_SUCCESS) {
            log.warn("登录失败，response: {}", loginResult);
            return setResultError(I18nUtil.getMessage("base_error"));
        }
        String loginUrl = response.getJSONObject("data").getString("url");
        log.info("登录成功，username: {},loginUrl: {}", username, loginUrl);
        return setResultSuccess(loginUrl, "success");
    }

    /**
     * 构建请求头（包含 apikey）
     */
    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("apikey", apikey);
        return headers;
    }

    /**
     * 构建请求体（包含 sign 和 userid）
     */
    private JSONObject buildRequestBody(String username) {
        Map<String, String> headers = new HashMap<>();
        headers.put("userid", username);
        String sign = SignUtils.makeSign(headers, secret);

        JSONObject body = new JSONObject();
        body.put("sign", sign);
        body.put("userid", username);
        return body;
    }

    /**
     * 保存预测用户记录
     */
    private void saveForecastUser(String username, LocalUserInfo localUser, String polygonAddress, String polProxyAddress)
            throws InvocationTargetException, IllegalAccessException {
        ForecastUserRecordEntity record = new ForecastUserRecordEntity();
        record.setUserId(localUser.getUserId());
        record.setUserType(localUser.getUserType());
        record.setUsername(username);
        record.setPolygonAddress(polygonAddress);
        record.setPolProxyAddress(polProxyAddress);
        record.setWalletAccount(0.0);
        GenericityUtil.setDate(record);
        forecastUserDao.insert(record);
    }

    /**
     * 本地用户信息内部类（简化传递）
     */
    @lombok.Value
    private static class LocalUserInfo {
        String userId;
        Integer userType;

        public static LocalUserInfo of(String userId, Integer userType) {
            return new LocalUserInfo(userId, userType);
        }
    }
}