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
import com.worldpay.dao.wallet.WalletTransfetRatesDao;
import com.worldpay.entity.wallet.WalletTransfetRatesEntity;
import com.worldpay.service.WalletTransfetRatesService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class WalletTransfetRatesServiceImpl extends BaseApiService implements WalletTransfetRatesService{
	
	@Autowired
	private WalletTransfetRatesDao walletTransfetRatesDao;

	@Override
	public ResponseBase findList() {
		PageHelper.startPage(Constants.PAGENUMBER, Constants.PAGESIZE);
		List<WalletTransfetRatesEntity> list = walletTransfetRatesDao.selectList(null);
		PageInfo<WalletTransfetRatesEntity> info = new PageInfo<>(list);
		return setResultSuccess(info, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase add(WalletTransfetRatesEntity entity) {
		try {
			WalletTransfetRatesEntity ratesEntity = walletTransfetRatesDao.selectOne(null);
			if(ratesEntity != null) {
				return setResultError(I18nUtil.getMessage("base_info_exist"));
			}
			GenericityUtil.setDate(entity);
			walletTransfetRatesDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(WalletTransfetRatesEntity entity) {
		try {
			WalletTransfetRatesEntity ratesEntity = walletTransfetRatesDao.selectById(entity.getId());
			if(ratesEntity != null) {
				walletTransfetRatesDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
