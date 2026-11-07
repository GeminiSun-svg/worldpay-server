package com.worldpay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.system.TollAddressDao;
import com.worldpay.entity.system.TollAddressEntity;
import com.worldpay.service.TollAddressService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class TollAddressServiceImpl extends BaseApiService implements TollAddressService{
	
	@Autowired
	private TollAddressDao tollAddressDao;

	@Override
	public ResponseBase findAll() {
		List<TollAddressEntity> list = tollAddressDao.selectList(null);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase add(@RequestBody TollAddressEntity entity) {
		try {
			TollAddressEntity addressEntity = tollAddressDao.findType(entity.getAddressType());
			if(addressEntity != null) {
				return setResultError("此类型钱包地址已存在");
			}
			GenericityUtil.setDate(entity);
			tollAddressDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody TollAddressEntity entity) {
		try {
			TollAddressEntity addressEntity = tollAddressDao.selectById(entity.getId());
			if(addressEntity != null) {
				tollAddressDao.updateById(entity);
				return setResultSuccess();
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody TollAddressEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<TollAddressEntity> list = tollAddressDao.findList(entity);
		// 获取总记录数
		PageInfo<TollAddressEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase delete(Integer id) {
		try {
			TollAddressEntity addressEntity = tollAddressDao.selectById(id);
			if(addressEntity != null) {
				tollAddressDao.deleteById(id);
				return setResultSuccess();
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
