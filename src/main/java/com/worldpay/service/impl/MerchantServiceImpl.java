package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.RechargeEntity;
import com.worldpay.api.response.MerchantApiEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.merchant.MerchantDao;
import com.worldpay.entity.merchant.MerchantEntity;
import com.worldpay.service.MerchantService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class MerchantServiceImpl extends BaseApiService implements MerchantService {

	@Autowired
	private MerchantDao merchantDao;

	@Override
	@SysLogAnnotation(module = "商户管理", type = "get", remark = "查询商户信息")
	public ResponseBase findInfo() {
		try {
			boolean exists = true;
			MerchantEntity entity = merchantDao.selectOne(null);
			if (entity == null) {
				exists = false;
				entity = new MerchantEntity();
			}
			// 新三方仅提供商户资产接口
			ResponseBase assetData = UCardApi.postData(null, UCardMethods.MERCHANT_ASSET, null, null);
			if (Constants.HTTP_RES_CODE_200.equals(assetData.getRtncode()) && assetData.getData() != null) {
				MerchantApiEntity merchant = JSONObject.parseObject(assetData.getData().toString(), MerchantApiEntity.class);
				if (merchant != null) {
					entity.setAvailableAmount(merchant.getAvailableAmount());
					entity.setCurrency(merchant.getCurrency());
					entity.setFrozenAmount(merchant.getFrozenAmount());
				}
			} else {
				return assetData;
			}
			if (exists) {
				merchantDao.updateById(entity);
			} else {
				merchantDao.insert(entity);
			}
			return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "商户管理", type = "post", remark = "充值记录")
	public ResponseBase rechargeList(RechargeEntity entity) {
		// 新三方文档未提供商户充值地址/充值记录查询接口
		return setResultError("当前三方未开放商户充值记录接口");
	}

}
