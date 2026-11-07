package com.worldpay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.dao.system.PayTypeSateDao;
import com.worldpay.entity.system.PayTypeSateEntity;
import com.worldpay.service.PayTypeSateService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class PayTypeSateServiceImpl extends BaseApiService implements PayTypeSateService{
	
	@Autowired
	private PayTypeSateDao PayTypeSateDao;

	@Override
	public ResponseBase findAll() {
		PayTypeSateEntity entity = PayTypeSateDao.selectOne(null);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase findManage() {
		PayTypeSateEntity entity = PayTypeSateDao.selectOne(null);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase update(PayTypeSateEntity entity) {
		try {
			PayTypeSateEntity typeSateEntity = PayTypeSateDao.selectById(entity.getId());
			if(typeSateEntity != null) {
				PayTypeSateDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(PayTypeSateEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<PayTypeSateEntity> list = PayTypeSateDao.selectList(null);
		// 获取总记录数
		PageInfo<PayTypeSateEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase add(PayTypeSateEntity entity) {
		try {
			GenericityUtil.setDate(entity);
			PayTypeSateDao.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
