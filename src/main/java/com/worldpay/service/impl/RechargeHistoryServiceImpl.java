package com.worldpay.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.merchant.RechargeHistoryDao;
import com.worldpay.entity.merchant.RechargeHistoryEntity;
import com.worldpay.service.RechargeHistoryService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class RechargeHistoryServiceImpl extends BaseApiService implements RechargeHistoryService{
	
	@Autowired
	private RechargeHistoryDao rechargeHistoryDao;

	@Override
	public ResponseBase findList(RechargeHistoryEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<RechargeHistoryEntity> list = rechargeHistoryDao.findList(entity);
			PageInfo<RechargeHistoryEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
