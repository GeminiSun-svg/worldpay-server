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
import com.worldpay.dao.system.IpWhitelistDao;
import com.worldpay.entity.system.IpWhitelistEntity;
import com.worldpay.service.IpWhitelistService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
@CrossOrigin
public class IpWhitelistServiceImpl extends BaseApiService implements IpWhitelistService{
	
	@Autowired
	private IpWhitelistDao ipWhitelistDao;

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "post", remark = "查询列表")
	public ResponseBase findUserCardAssets(@RequestBody IpWhitelistEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<IpWhitelistEntity> list = ipWhitelistDao.findList(entity);
		PageInfo<IpWhitelistEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "post", remark = "新增")
	public ResponseBase add(@RequestBody IpWhitelistEntity entity) {
		try {
			IpWhitelistEntity ipWhitelist = ipWhitelistDao.findIp(entity.getIpSite());
			if(ipWhitelist != null) {
				return setResultError(I18nUtil.getMessage("base_info_exist"));
			}
			GenericityUtil.setDate(entity);
			ipWhitelistDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "get", remark = "删除")
	public ResponseBase delete(Integer id) {
		try {
			IpWhitelistEntity entity = ipWhitelistDao.selectById(id);
			if(entity != null) {
				ipWhitelistDao.deleteById(id);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else{
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
