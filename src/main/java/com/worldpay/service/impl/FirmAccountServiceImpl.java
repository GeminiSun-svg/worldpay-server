package com.worldpay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.dao.firmaccount.FirmAccountDao;
import com.worldpay.entity.firm.FirmAccountEntity;
import com.worldpay.service.FirmAccountService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class FirmAccountServiceImpl extends BaseApiService implements FirmAccountService{
	
	@Autowired
	private FirmAccountDao firmAccountDao;

	@Override
	@SysLogAnnotation(module = "公司银行账户", type = "post", remark = "新增公司银行账户")
	public ResponseBase add(@RequestBody FirmAccountEntity entity) {
		try {
			FirmAccountEntity accountEntity = firmAccountDao.selectOne(null);
			if(accountEntity != null) {
				return setResultError(I18nUtil.getMessage("base_info_exist"));
			}
			GenericityUtil.setDate(entity);
			firmAccountDao.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "公司银行账户", type = "post", remark = "编辑公司银行账户")
	public ResponseBase update(@RequestBody FirmAccountEntity entity) {
		try {
			FirmAccountEntity accountEntity = firmAccountDao.selectById(entity.getId());
			if(accountEntity != null) {
				firmAccountDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "公司银行账户", type = "get", remark = "删除公司银行账户")
	public ResponseBase delete(Integer id) {
		try {
			FirmAccountEntity entity = firmAccountDao.selectById(id);
			if(entity != null) {
				firmAccountDao.deleteById(id);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "公司银行账户", type = "get", remark = "查询公司银行账户")
	public ResponseBase find() {
		try {
			FirmAccountEntity entity = firmAccountDao.selectOne(null);
			return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "公司银行账户", type = "post", remark = "公司银行账户列表")
	public ResponseBase findList(FirmAccountEntity entity) {
		PageHelper.startPage(Constants.PAGENUMBER, Constants.PAGESIZE);
		List<FirmAccountEntity> list = firmAccountDao.selectList(null);
		PageInfo<FirmAccountEntity> info = new PageInfo<>(list);
		return setResultSuccess(info, I18nUtil.getMessage("base_success"));
	}

}
