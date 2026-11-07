package com.worldpay.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.card.AppUserHolderDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserHolderEntity;
import com.worldpay.enums.SexEnums;
import com.worldpay.service.CardholderService;
import com.worldpay.utils.AgeCheckUtil;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class CardholderServiceImpl extends BaseApiService implements CardholderService{
	
	@Autowired
	private AppUserHolderDao cardholderDao;
	
	@Autowired
	private AppUserDao appUserDao;

	@Override
	@SysLogAnnotation(module = "持卡人信息", type = "POST", remark = "新增持卡人")
	public ResponseBase add(@RequestBody @Valid AppUserHolderEntity entity) {
		try {
			if(entity.getUserSexNum() != null) {
				if(SexEnums.MALE.getIndex().equals(entity.getUserSexNum())) {
					entity.setUserSex(SexEnums.MALE.getName());
				}else if(SexEnums.FEMALE.getIndex().equals(entity.getUserSexNum())) {
					entity.setUserSex(SexEnums.FEMALE.getName());
				}
			}
			
			AppUserEntity appUserEntity = appUserDao.findByUid(entity.getUid());
			if(appUserEntity != null) {
				//年龄校验
				if(entity.getUserBirthday() != null && entity.getUserBirthday().length() > 0) {
					if(AgeCheckUtil.isAdult(entity.getUserBirthday())) {
						GenericityUtil.setDate(entity);
						cardholderDao.insert(entity);
						return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
					}else {
						return setResultError(I18nUtil.getMessage("holder_age_error"));
					}
				}else {
					return setResultError(I18nUtil.getMessage("holder_birth_error"));
				}
			}else {
				return setResultError(I18nUtil.getMessage("user.not_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "持卡人信息", type = "POST", remark = "编辑持卡人")
	public ResponseBase update(@RequestBody @Valid AppUserHolderEntity entity) {
		try {
			AppUserHolderEntity cardholderEntity = cardholderDao.selectById(entity.getId());
			if(cardholderEntity != null) {
				AppUserEntity appUserEntity = appUserDao.findByUid(entity.getUid());
				if(appUserEntity != null) {
					entity.setGmtModified(new Date());
					cardholderDao.updateById(entity);
					return setResultSuccess();
				}else {
					return setResultError(I18nUtil.getMessage("user.not_null"));
				}
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "持卡人信息", type = "get", remark = "删除持卡人")
	public ResponseBase delete(Integer id) {
		try {
			cardholderDao.deleteById(id);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "持卡人信息", type = "get", remark = "查询持卡人信息")
	public ResponseBase findByUid(String uid) {
		try {
			List<AppUserHolderEntity> list = cardholderDao.findByUid(uid);
			return setResultSuccess(list, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "持卡人信息", type = "get", remark = "查询持卡人信息")
	public ResponseBase findById(Integer id) {
		try {
			AppUserHolderEntity entity = cardholderDao.selectById(id);
			return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
