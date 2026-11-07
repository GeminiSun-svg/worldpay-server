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
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.product.ProductTypeDao;
import com.worldpay.entity.product.ProductTypeEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.ProductTypeService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;

@Service
@Transactional
public class ProductTypeServiceImpl extends BaseApiService implements ProductTypeService{
	
	@Autowired
	private ProductTypeDao productTypeDao;

	@Override
	@SysLogAnnotation(module = "理财产品分类管理", type = "post", remark = "新增理财产品类型")
	public ResponseBase add(ProductTypeEntity entity) {
		try {
			GenericityUtil.setDate(entity);
			productTypeDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "理财产品分类管理", type = "post", remark = "理财产品类型列表")
	public ResponseBase findList(ProductTypeEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<ProductTypeEntity> list = productTypeDao.findList(entity);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
			}
		}
		PageInfo<ProductTypeEntity> info = new PageInfo<>(list);
		return setResultSuccess(info, I18nUtil.getMessage("base_success"));
	}

	@Override
	@SysLogAnnotation(module = "理财产品分类管理", type = "get", remark = "删除新增理财产品类型")
	public ResponseBase delete(Integer id) {
		try {
			ProductTypeEntity entity = productTypeDao.selectById(id);
			if(entity != null) {
				productTypeDao.deleteById(id);
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
	@SysLogAnnotation(module = "理财产品分类管理", type = "get", remark = "理财产品类型列表")
	public ResponseBase findAll() {
		String language = LanguageContext.getLanguage();
		ProductTypeEntity entity = new ProductTypeEntity();
		entity.setLanguage(language);
		List<ProductTypeEntity> list = productTypeDao.findList(entity);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

	@Override
	public ResponseBase pcFindAll(String language) {
		ProductTypeEntity entity = new ProductTypeEntity();
		if(language != null && language.length() > 0) {
			entity.setLanguage(language);			
		}
		List<ProductTypeEntity> list = productTypeDao.findList(entity);
		return setResultSuccess(list, I18nUtil.getMessage("base_success"));
	}

}
