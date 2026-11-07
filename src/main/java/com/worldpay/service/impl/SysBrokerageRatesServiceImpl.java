package com.worldpay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.system.SysBrokerageRatesDao;
import com.worldpay.entity.system.SysBrokerageRatesEntity;
import com.worldpay.service.SysBrokerageRatesService;
import com.worldpay.utils.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.worldpay.base.BaseApiService.setResultError;
import static com.worldpay.base.BaseApiService.setResultSuccess;

@Service
@Transactional
@CrossOrigin
public class SysBrokerageRatesServiceImpl implements SysBrokerageRatesService {

    @Autowired
    private SysBrokerageRatesDao sysBrokerageRatesDao;

    @Override
    public ResponseBase findAll(SysBrokerageRatesEntity entity) {

        PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
        List<SysBrokerageRatesEntity> list = sysBrokerageRatesDao.selectList(null);
        PageInfo<SysBrokerageRatesEntity> info = new PageInfo<>(list);
        return setResultSuccess(info, I18nUtil.getMessage("base_success"));

    }

    @Override
    public ResponseBase add(SysBrokerageRatesEntity entity) {
        entity.setSetTime(new Date());
        entity.setGmtModified(new Date());
        int flag = sysBrokerageRatesDao.insert(entity);
        return flag > 0 ? setResultSuccess(I18nUtil.getMessage("base_success")) : setResultError(I18nUtil.getMessage("base_error"));
    }

    @Override
    public ResponseBase update(SysBrokerageRatesEntity entity) {
        entity.setGmtModified(new Date());
        int flag = sysBrokerageRatesDao.updateById(entity);
        return flag > 0 ? setResultSuccess(I18nUtil.getMessage("base_success")) : setResultError(I18nUtil.getMessage("base_error"));
    }
}
