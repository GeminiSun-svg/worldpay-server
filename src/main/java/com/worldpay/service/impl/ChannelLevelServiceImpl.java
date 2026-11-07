package com.worldpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.brokerage.ChannelLevelDao;
import com.worldpay.entity.brokerage.ChannelLevelEntity;
import com.worldpay.service.ChannelLevelService;
import com.worldpay.utils.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChannelLevelServiceImpl extends BaseApiService implements ChannelLevelService {

    @Autowired
    private ChannelLevelDao channelLevelDao;

    @Override
    public ResponseBase findAll(ChannelLevelEntity entity) {
        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        List<ChannelLevelEntity> list = channelLevelDao.selectList(new QueryWrapper<ChannelLevelEntity>().eq("delete_state", 1));
        PageInfo<ChannelLevelEntity> info = new PageInfo<>(list);
        return setResultSuccess(info);
    }

    @Override
    public ResponseBase add(ChannelLevelEntity entity) {
        LambdaQueryWrapper<ChannelLevelEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChannelLevelEntity::getLevelName, entity.getLevelName())
                .or()
                .eq(ChannelLevelEntity::getLevelNo, entity.getLevelNo());
        // 检查是否存在
        if (channelLevelDao.selectCount(wrapper) > 0) {
            return setResultError(I18nUtil.getMessage("grade_name_or_grade_number_already_exists"));
        }
        entity.setDeleteState(1);
        entity.setApplyState(1);
        entity.setSetTime(new Date());
        entity.setGmtModified(new Date());
        int flag = channelLevelDao.insert(entity);
        return flag > 0 ? setResultSuccess(I18nUtil.getMessage("base_success")) : setResultError(I18nUtil.getMessage("base_error"));
    }

    @Override
    public ResponseBase update(ChannelLevelEntity entity) {
        entity.setGmtModified(new Date());
        int flag = channelLevelDao.updateById(entity);
        return flag > 0 ? setResultSuccess(I18nUtil.getMessage("base_success")) : setResultError(I18nUtil.getMessage("base_error"));
    }

    @Override
    public ResponseBase updateStatus(int id, int applyState) {
        channelLevelDao.updateApplyStatusById(id, applyState);
        return setResultSuccess(I18nUtil.getMessage("base_success"));
    }

    @Override
    public ResponseBase delete(int id) {
        channelLevelDao.updateDeleteStatusById(id);
        return setResultSuccess(I18nUtil.getMessage("base_success"));
    }
}
