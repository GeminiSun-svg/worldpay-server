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
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.card.CardCloseDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.card.CardCloseEntity;
import com.worldpay.enums.ReviewStatusEnums;
import com.worldpay.service.CardCloseService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class CardCloseServiceImpl extends BaseApiService implements CardCloseService{
	
	@Autowired
	private CardCloseDao cardCloseDao;
	
	@Autowired
	private AppUserDao appUserDao;
	

	@Override
	public ResponseBase findUserCardList(@RequestBody CardCloseEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			
			List<CardCloseEntity> list = cardCloseDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setReviewStatusName(ReviewStatusEnums.getName(list.get(i).getReviewStatus()));
					AppUserEntity userEntity = appUserDao.findByUid(list.get(i).getUid());
					list.get(i).setAppUserData(userEntity);
				}
			}
			PageInfo<CardCloseEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
