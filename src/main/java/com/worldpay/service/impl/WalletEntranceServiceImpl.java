package com.worldpay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;

import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.SysStateDao;
import com.worldpay.entity.system.SysStateEntity;
import com.worldpay.service.WalletEntranceService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class WalletEntranceServiceImpl extends BaseApiService implements WalletEntranceService{
	
	@Autowired
	private SysStateDao sysStateDao;

	@Override
	@SysLogAnnotation(module = "钱包入口配置", type = "get", remark = "查询")
	public ResponseBase findList() {
		List<SysStateEntity> list = sysStateDao.selectList(null);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "钱包入口配置", type = "get", remark = "开启/关闭")
	public ResponseBase operate(Boolean type,String userName) {
		try {
			SysStateEntity stateEntity = sysStateDao.selectOne(null);
			if(stateEntity != null) {
				stateEntity.setWalletState(type);
				stateEntity.setOperateUser(userName);
				sysStateDao.updateById(stateEntity);
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
