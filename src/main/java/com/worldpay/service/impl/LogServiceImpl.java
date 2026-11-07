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
import com.worldpay.dao.log.SysLogDao;
import com.worldpay.entity.system.SysLogEntity;
import com.worldpay.service.LogService;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class LogServiceImpl extends BaseApiService implements LogService{
	

	@Autowired
	private SysLogDao sysLogDao;

	@Override
	@SysLogAnnotation(module = "日志管理", type = "post", remark = "获取日志列表")
	public ResponseBase findList(SysLogEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<SysLogEntity> list = sysLogDao.findList(entity);
		// 获取总记录数
		PageInfo<SysLogEntity> info = new PageInfo<>(list);
		return setResultSuccess(info,I18nUtil.getMessage("base_success"));
	}

}
