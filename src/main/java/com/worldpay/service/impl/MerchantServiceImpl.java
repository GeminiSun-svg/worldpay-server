package com.worldpay.service.impl;

import com.worldpay.utils.api.UCardApi;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.request.RechargeEntity;
import com.worldpay.api.response.MerchantAddressEntity;
import com.worldpay.api.response.MerchantApiEntity;
import com.worldpay.api.response.RechargeResEntity;
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
public class MerchantServiceImpl extends BaseApiService implements MerchantService{
	
	@Autowired
	private MerchantDao merchantDao;

	@Override
	@SysLogAnnotation(module = "商户管理", type = "get", remark = "查询商户信息")
	public ResponseBase findInfo() {
		try {
			Boolean temp = true;
			MerchantEntity entity = merchantDao.selectOne(null);
			if (entity == null) {
				temp = false;
				entity = new MerchantEntity();
		    }
			ResponseBase assetData = UCardApi.postData(null, UCardMethods.MERCHANT_ASSET, new Object(), null);
			if(Constants.HTTP_RES_CODE_200.equals(assetData.getRtncode())) {
				MerchantApiEntity merchant = JSONObject.parseObject(assetData.getData().toString(), MerchantApiEntity.class);
				if(merchant != null) {
					entity.setAvailableAmount(merchant.getAvailableAmount());
					entity.setCurrency(merchant.getCurrency());
					entity.setFrozenAmount(merchant.getFrozenAmount());
				}
			}
			ResponseBase info = UCardApi.postData(null, UCardMethods.MERCHANT_RECHARGE_INFO, new Object(), null);
			if(Constants.HTTP_RES_CODE_200.equals(info.getRtncode())) {
				List<MerchantAddressEntity> list = JSONArray.parseArray(info.getData().toString(), MerchantAddressEntity.class);
				if(list != null && list.size() > 0) {
					entity.setAddress(list.get(0).getAddressInfo().get(0).getAddress());
					entity.setNetwork(list.get(0).getAddressInfo().get(0).getName());
					entity.setSymbol(list.get(0).getSymbol());
				}
			}
			if(temp) {
				//更新商户资产信息
				merchantDao.updateById(entity);
			}else {
				//新增商户资产信息
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
		try {
			ResponseBase resData = UCardApi.postData(null, UCardMethods.MERCHANT_RECHARGE_RECORDS, entity, null);
			if(Constants.HTTP_RES_CODE_200.equals(resData.getRtncode())) {
				List<RechargeResEntity> list = JSONArray.parseArray(resData.getData().toString(), RechargeResEntity.class);
				return setResultSuccess(list,I18nUtil.getMessage("base_success"));
			}else {
				return resData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
