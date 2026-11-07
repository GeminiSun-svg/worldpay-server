package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppChannelIntiterDao;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.brokerage.BrokerageLogDao;
import com.worldpay.dao.system.SysUserDao;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.brokerage.BrokerageLogEntity;
import com.worldpay.entity.system.SysUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.service.BrokerageLogService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CrossOrigin
public class BrokerageLogServiceImpl extends BaseApiService implements BrokerageLogService {

    @Autowired
    private BrokerageLogDao brokerageLogDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private AppChannelIntiterDao appChannelIntiterDao;

    @Override
    public ResponseBase findAll(BrokerageLogEntity entity, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtil.getUserNameByJwt(token);
        AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("user_email", username));
        List<BrokerageLogEntity> list = new ArrayList<>();
        if (appUserEntity != null) {
            entity.setIntiterUserType(null);
            entity.setInviterUserUid(appUserEntity.getUid());
            list.addAll(findAllByInviterUid(entity));
            AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("app_uid", appUserEntity.getUid()));
            if (appChannelIntiterEntity != null) {
                entity.setIntiterUserType(IntiterUserTypeEnums.CHANNEL_USER.getIndex());
                entity.setInviterUserUid(appChannelIntiterEntity.getChannelUid());
                list.addAll(findAllByInviterUid(entity));
            }
        } else {
            list.addAll(findAllByInviterUid(entity));
            Integer type = entity.getIntiterUserType();
            if (type != null && type.equals(IntiterUserTypeEnums.CHANNEL_USER.getIndex())) {
                AppChannelIntiterEntity appChannelIntiterEntity = appChannelIntiterDao.selectOne(new QueryWrapper<AppChannelIntiterEntity>().eq("channel_uid", entity.getInviterUserUid()));
                if (appChannelIntiterEntity != null) {
                    entity.setInviterUserUid(appChannelIntiterEntity.getAppUid());
                    entity.setIntiterUserType(null);
                    list.addAll(findAllByInviterUid(entity));
                }
            }
        }
        List<BrokerageLogEntity> pageList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());
        PageInfo<BrokerageLogEntity> info = new PageInfo<>(pageList);
        info.setTotal(list.size());
        return setResultSuccess(info);
    }

    public List<BrokerageLogEntity> findAllByInviterUid(BrokerageLogEntity entity) {
        List<BrokerageLogEntity> list = brokerageLogDao.selectLogList(entity.getInviterUserUid(),entity.getCoverInviterUserUid(),entity.getIntiterUserType());
        for (BrokerageLogEntity brokerageLogEntity : list) {
            String coverInviterUserUid = brokerageLogEntity.getCoverInviterUserUid();
            SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("id", coverInviterUserUid));
            if (sysUserEntity != null){
                brokerageLogEntity.setEmail(sysUserEntity.getUsername());
            }
            AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("uid", coverInviterUserUid));
            if (appUserEntity != null){
                brokerageLogEntity.setEmail(appUserEntity.getUserEmail());
            }
        }
        return list;
    }

    @Override
    public ResponseBase findByChannelId(Integer channelId) {
        brokerageLogDao.findSumListByChannelId(channelId);
        return setResultSuccess();
    }

    @Override
    public ResponseBase findBrokerageLogById(Integer id) {
        BrokerageLogEntity brokerageLogEntity = brokerageLogDao.selectById(id);
        return setResultSuccess(brokerageLogEntity);
    }
}
