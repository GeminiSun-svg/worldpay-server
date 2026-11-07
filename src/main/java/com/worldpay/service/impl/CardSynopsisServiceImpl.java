package com.worldpay.service.impl;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

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
import com.worldpay.dao.card.CardSynopsisDao;
import com.worldpay.dao.card.CardSynopsisJoinDao;
import com.worldpay.entity.card.CardSynopsisEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.CardSynopsisService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class CardSynopsisServiceImpl extends BaseApiService implements CardSynopsisService{
	
	@Autowired
	private CardSynopsisDao cardSynopsisDao;
	
	@Autowired
	private CardSynopsisJoinDao cardSynopsisJoinDao;

	@Override
	@SysLogAnnotation(module = "银行卡简介管理", type = "post", remark = "新增简介")
	public ResponseBase add(@RequestBody CardSynopsisEntity entity) {
		try {
			GenericityUtil.setDate(entity);
			cardSynopsisDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡简介管理", type = "post", remark = "编辑简介")
	public ResponseBase update(@RequestBody @Valid CardSynopsisEntity entity) {
		try {
			CardSynopsisEntity synopsisEntity = cardSynopsisDao.selectById(entity.getId());
			if(synopsisEntity != null) {
				entity.setGmtModified(new Date());
				cardSynopsisDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡简介管理", type = "get", remark = "删除简介")
	public ResponseBase delete(Integer id) {
		try {
			cardSynopsisDao.deleteById(id);
			cardSynopsisJoinDao.deleteBySynopsisId(id);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡简介管理", type = "post", remark = "简介列表")
	public ResponseBase findList(@RequestBody CardSynopsisEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<CardSynopsisEntity> list = cardSynopsisDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
				}
			}
			PageInfo<CardSynopsisEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "银行卡简介管理", type = "post", remark = "简介列表")
	public ResponseBase findAll() {
		List<CardSynopsisEntity> list = cardSynopsisDao.selectList(null);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
			}
		}
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

}
