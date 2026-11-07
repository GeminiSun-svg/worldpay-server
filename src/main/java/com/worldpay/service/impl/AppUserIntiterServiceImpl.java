package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.UserIntiterEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserIntiterDao;
import com.worldpay.dao.appuser.AppUserIosWalletStateDao;
import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserIntiterEntity;
import com.worldpay.entity.appuser.AppUserIosWalletStateEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.enums.OpenStateEnums;
import com.worldpay.service.AppUserIntiterService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
@Transactional
@CrossOrigin
public class AppUserIntiterServiceImpl extends BaseApiService implements AppUserIntiterService {

    @Autowired
    private AppUserIntiterDao appUserIntiterDao;

    @Autowired
    private ChannelUserDao channelUserDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private AppUserIosWalletStateDao appUserIosWalletStateDao;

    @Value("${invitation.default_code:}")
    private String INVITATION_DEFAULT_CODE;

    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;

    @Override
    @SysLogAnnotation(module = "用户邀请关系查询", type = "POST", remark = "用户邀请关系查询")
    public ResponseBase findAll(@RequestBody AppUserIntiterEntity entity, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        List<AppUserIntiterEntity> list = new ArrayList<>();
        //针对app用户
        if (appUserEntity != null) {
            entity.setInviterUser(appUserEntity.getId());
            entity.setInviterUserUid(appUserEntity.getUid());
            entity.setIntiterUserType(null);
            list.addAll(findAllByInviterUid(entity));
            AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", appUserEntity.getUid()));
            if (appChannelIntiterEntity != null) {
                entity.setInviterUser(appChannelIntiterEntity.getChannelId());
                entity.setInviterUserUid(appChannelIntiterEntity.getChannelUid());
                list.addAll(findAllByInviterUid(entity));
            }
        } else {
            //针对后台用户
            list.addAll(findAllByInviterUid(entity));
            Integer type = entity.getIntiterUserType();
            if (type != null && type.equals(IntiterUserTypeEnums.CHANNEL_USER.getIndex())) {
                AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("channel_uid", entity.getInviterUserUid()));
                if (appChannelIntiterEntity != null) {
                    //说明为渠道商用户
                    entity.setInviterUser(appChannelIntiterEntity.getAppId());
                    entity.setInviterUserUid(appChannelIntiterEntity.getAppUid());
                    entity.setIntiterUserType(null);
                    list.addAll(findAllByInviterUid(entity));
                }
            }
        }
        List<AppUserIntiterEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<AppUserIntiterEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info);
    }

    /**
     * 数据查询
     * @param entity
     * @return
     */
    public List<AppUserIntiterEntity> findAllByInviterUid(AppUserIntiterEntity entity) {
        List<AppUserIntiterEntity> list = appUserIntiterDao.selectAppUserIntiters(entity.getIntiterUserType(), entity.getInviterUserUid(), entity.getCoverInviterUserUid());
        for (AppUserIntiterEntity appUserIntiterEntity : list) {
            String coverInviterUserUid = appUserIntiterEntity.getCoverInviterUserUid();
            SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("id", coverInviterUserUid));
            if (sysUserEntity != null) {
                appUserIntiterEntity.setEmail(sysUserEntity.getUsername());
            }
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("uid", coverInviterUserUid));
            if (appUserEntity != null) {
                appUserIntiterEntity.setEmail(appUserEntity.getUserEmail());
            }
        }
        return list;
    }

    @Override
    public ResponseBase findChannelInvitationCodeByUserId(Integer userId) {
        ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", userId));
        if (channelUserEntity != null) {
            return setResultSuccess(channelUserEntity);
        }
        return setResultError(I18nUtil.getMessage("base_error"));
    }

    @Override
    public ResponseBase findById(Integer id) {
        AppUserIntiterEntity appUserIntiterEntity = appUserIntiterDao.selectById(id);
        return setResultSuccess(appUserIntiterEntity);
    }


    @Override
    @SysLogAnnotation(module = "添加用户邀请关系", type = "POST", remark = "添加用户邀请关系")
    public ResponseBase insertAppUserIntiter(@RequestBody UserIntiterEntity userIntiterEntity) {
        try {
            //构建邀请记录
            AppUserIntiterEntity appUserIntiterEntity = new AppUserIntiterEntity();
            //根据传入的邀请user_id信息，查询邀请人完整信息
            ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", userIntiterEntity.getInviterUser()));
            //封装邀请人信息
            appUserIntiterEntity.setInviterUser(channelUserEntity.getId());
            appUserIntiterEntity.setInviterUserUid(String.valueOf(channelUserEntity.getUserId()));
            //根据传入的被邀请人用户名，查询被邀请人完整信息
            String coverInviterUser = userIntiterEntity.getCoverInviterUser();
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", coverInviterUser));
            //如果用户信息不存在，提示用户不存在
            if (appUserEntity == null) {
                return setResultError(I18nUtil.getMessage("user.not_null"));
            }
            //封装渠道商等级信息
            appUserIntiterEntity.setIntiterUserLevel(channelUserEntity.getLevelNo());
            appUserIntiterEntity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
            //封装被邀请人信息
            appUserIntiterEntity.setCoverInviterUser(appUserEntity.getId());
            appUserIntiterEntity.setCoverInviterUserUid(appUserEntity.getUid());
            GenericityUtil.setDate(appUserIntiterEntity);
            //判断是否已经被邀请
            QueryWrapper<AppUserIntiterEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("cover_inviter_user", appUserEntity.getId());
            wrapper.eq("cover_inviter_user_uid", appUserEntity.getUid());
            AppUserIntiterEntity appUserInviter = appUserIntiterDao.selectOne(wrapper);
            if (appUserInviter != null) {
                //说明已经被邀请，可以修改邀请人信息
                appUserInviter.setIntiterUserLevel(channelUserEntity.getLevelNo());
                appUserInviter.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                appUserInviter.setInviterUser(channelUserEntity.getId());
                appUserInviter.setInviterUserUid(String.valueOf(channelUserEntity.getUserId()));
                appUserInviter.setGmtModified(new Date());
                appUserIntiterDao.updateById(appUserInviter);
                log.info("{}已经被邀请,修改后邀请人Id{}信息", appUserEntity.getUid(), channelUserEntity.getUserId());
            } else {
                //说明未被邀请，可以新增
                appUserIntiterDao.insert(appUserIntiterEntity);
                log.info("当前用户{}未被{}邀请，添加邀请记录", appUserEntity.getUid(), channelUserEntity.getUserId());
            }
            if (channelUserEntity != null && INVITATION_DEFAULT_CODE.equals(channelUserEntity.getInvitationCode())) {
                AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(appUserEntity.getUid());
                if (stateEntity != null) {
                    stateEntity.setWalletState(OpenStateEnums.CLOSURE.getIndex());
                    stateEntity.setTopUpState(OpenStateEnums.CLOSURE.getIndex());
                    appUserIosWalletStateDao.updateById(stateEntity);
                } else {
                    AppUserIosWalletStateEntity userIosWalletStateEntity = new AppUserIosWalletStateEntity();
                    userIosWalletStateEntity.setAppUserId(appUserEntity.getId());
                    userIosWalletStateEntity.setAppUserUid(appUserEntity.getUid());
                    userIosWalletStateEntity.setTopUpState(OpenStateEnums.CLOSURE.getIndex());
                    userIosWalletStateEntity.setWalletState(OpenStateEnums.CLOSURE.getIndex());
                    GenericityUtil.setDate(userIosWalletStateEntity);
                    appUserIosWalletStateDao.insert(userIosWalletStateEntity);
                }
            } else {
                AppUserIosWalletStateEntity stateEntity = appUserIosWalletStateDao.findUid(appUserEntity.getUid());
                if (stateEntity != null) {
                    stateEntity.setWalletState(OpenStateEnums.OPEN.getIndex());
                    stateEntity.setTopUpState(OpenStateEnums.OPEN.getIndex());
                    appUserIosWalletStateDao.updateById(stateEntity);
                } else {
                    AppUserIosWalletStateEntity userIosWalletStateEntity = new AppUserIosWalletStateEntity();
                    userIosWalletStateEntity.setAppUserId(appUserEntity.getId());
                    userIosWalletStateEntity.setAppUserUid(appUserEntity.getUid());
                    userIosWalletStateEntity.setTopUpState(OpenStateEnums.OPEN.getIndex());
                    userIosWalletStateEntity.setWalletState(OpenStateEnums.OPEN.getIndex());
                    GenericityUtil.setDate(userIosWalletStateEntity);
                    appUserIosWalletStateDao.insert(userIosWalletStateEntity);
                }
            }
            return setResultSuccess();
        } catch (Exception e) {
            log.error("归纳用户信息异常：{}", e.toString());
            return setResultError(I18nUtil.getMessage("base_error"));
        }
    }
}
