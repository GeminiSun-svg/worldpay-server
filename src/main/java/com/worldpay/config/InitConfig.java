package com.worldpay.config;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.worldpay.dao.appuser.AppUserCardDao;
import com.worldpay.dao.card.CardTransactionDao;

@Component
public class InitConfig {
	
	@Autowired
	private AppUserCardDao appUserCardDao;
	
	@Autowired
	private CardTransactionDao cardTransactionDao;
	
	@PostConstruct
    public void init() {
		appUserCardDao.updateStateLable("TBA","1");
		appUserCardDao.updateStateLable("ACTIVE","3");
		appUserCardDao.updateStateLable("CLOSE_PROCESSING","6");
		appUserCardDao.updateStateLable("CLOSE","7");
		appUserCardDao.updateStateLable("ACTIVE_PROCESSING","2");
		appUserCardDao.updateStateLable("INACTIVE","4");
		cardTransactionDao.updateTransTypeLalbe("topup","TOPUP");
    }

}
