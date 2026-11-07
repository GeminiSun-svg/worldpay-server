package com.worldpay.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.card.CardLableDao;
import com.worldpay.dao.card.CardLableJoinDao;
import com.worldpay.entity.card.CardLableEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.CardLableService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class CardLableServiceImpl extends BaseApiService implements CardLableService{
	
	@Autowired
	private CardLableDao cardLableDao;
	
	@Autowired
	private CardLableJoinDao cardLableJoinDao;

	@Override
	@SysLogAnnotation(module = "银行卡标签", type = "post", remark = "新增标签")
	public ResponseBase add(CardLableEntity entity) {
		try {
			CardLableEntity cardLableEntity = cardLableDao.findName(entity.getName());
			if(cardLableEntity != null) {
				return setResultError(I18nUtil.getMessage("lable_check"));
			}
			GenericityUtil.setDate(entity);
			cardLableDao.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡标签", type = "post", remark = "删除标签")
	public ResponseBase delete(Integer id) {
		try {
			cardLableDao.deleteById(id);
			cardLableJoinDao.deleteByLableId(id);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡标签", type = "post", remark = "标签列表")
	public ResponseBase findList(CardLableEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<CardLableEntity> list = cardLableDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
				}
			}
			PageInfo<CardLableEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡标签", type = "get", remark = "标签列表")
	public ResponseBase findAll() {
		List<CardLableEntity> list = cardLableDao.selectList(null);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
			}
		}
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

}
