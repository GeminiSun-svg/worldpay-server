package com.worldpay.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.SyssCountryCodeDao;
import com.worldpay.dao.system.SysFaCountryDao;
import com.worldpay.entity.response.DicEntity;
import com.worldpay.entity.system.SysFaCountryEntity;
import com.worldpay.entity.system.SyssCountryCodeEntity;
import com.worldpay.enums.ApplicationRecordStateEnums;
import com.worldpay.enums.KycStateEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.NetworkTypeEnums;
import com.worldpay.enums.OpenCardStateEnums;
import com.worldpay.enums.PayTypeEnums;
import com.worldpay.enums.SysConfigTypeEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.enums.WalletTitleTypeEnums;
import com.worldpay.service.DicService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class DicServiceImpl extends BaseApiService implements DicService{
	
	@Autowired
	private SyssCountryCodeDao syssCountryCodeDao;
	
	@Autowired
	private SysFaCountryDao sysFaCountryDao;

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询语言列表")
	public ResponseBase getLanguage() {
		List<DicEntity> list = LanguageEnums.getLableList();
		return setResultSuccess(list,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询国家地区代码")
	public ResponseBase getCountryCode() {
		String language = LanguageContext.getLanguage();
		List<SyssCountryCodeEntity> list = syssCountryCodeDao.findLanguage(language);
		return setResultSuccess(list,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "国家省市级联")
	public ResponseBase findCountry(Integer parentId) {
		String language = LanguageContext.getLanguage();
		if(parentId == null) {
			parentId = 0;
		}
		List<SysFaCountryEntity> list = sysFaCountryDao.findParent(parentId);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if(LanguageEnums.ZH_CN.getName().equals(language)) {
					list.get(i).setName(list.get(i).getCname());
				}else if(LanguageEnums.EN_US.getName().equals(language)){
					list.get(i).setName(list.get(i).getEname());
				}else if(LanguageEnums.ZH_TW.getName().equals(language)){
					list.get(i).setName(list.get(i).getTname());
				}
				list.get(i).setEname(null);
				list.get(i).setCname(null);
				list.get(i).setTname(null);
			}
		}
		return setResultSuccess(list,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "银行卡状态")
	public ResponseBase findCountry() {
		List<DicEntity> list = OpenCardStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "充值方式")
	public ResponseBase findPayType() {
		List<DicEntity> list = PayTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "kyc认证状态")
	public ResponseBase findKycState() {
		List<DicEntity> list = KycStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询用户状态")
	public ResponseBase findUserState() {
		List<DicEntity> list = UserStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询协议类型")
	public ResponseBase findProtocolType() {
		List<DicEntity> list = SysConfigTypeEnums.getProtocolType();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询外部链接类型")
	public ResponseBase findJumpAddress() {
		List<DicEntity> list = SysConfigTypeEnums.getJumpAddress();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "字典管理", type = "get", remark = "查询系统配置类型")
	public ResponseBase findSysConfigType() {
		List<DicEntity> list = SysConfigTypeEnums.getLists();
		return setResultSuccess(list,I18nUtil.getMessage("base_success"));
	}
	
	@Override
	public ResponseBase findNetwokList() {
		List<DicEntity> list =NetworkTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}
	
	@Override
	public ResponseBase getNetwokList() {
		List<Map<String, Object>> list =NetworkTypeEnums.getListLable();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}


	@Override
	public ResponseBase findRecordState() {
		List<DicEntity> list = ApplicationRecordStateEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase getWalletOperationType() {
		List<DicEntity> list = WalletTitleTypeEnums.getList();
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}
	
	
}
