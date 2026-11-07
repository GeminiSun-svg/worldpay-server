package com.worldpay.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.system.HelpDao;
import com.worldpay.entity.system.HelpEntity;
import com.worldpay.enums.FilePathEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.service.HelpService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.S3FileUploadUtil;

@Service
@Transactional
public class HelpServiceImpl extends BaseApiService implements HelpService{
	
	@Autowired
	private HelpDao helpDao;

	@Override
	public ResponseBase add(HelpEntity entity) {
		try {
			entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
			MultipartFile file = com.worldpay.utils.FileUploadUtil.saveHtmlToFile(entity.getArticleContent(),FilePathEnums.HELP.getValue());
			String url = S3FileUploadUtil.fileUploadHtml(file, FilePathEnums.HELP.getValue());
			entity.setArticleUrl(url);
			GenericityUtil.setDate(entity);
			helpDao.insert(entity);
			return setResultSuccess(I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	

	@Override
	public ResponseBase update(HelpEntity entity) {
		try {
			HelpEntity helpEntity = helpDao.selectById(entity.getId());
			if(helpEntity != null) {
				entity.setLanguageName(LanguageEnums.getLable(entity.getLanguage()));
				MultipartFile file = com.worldpay.utils.FileUploadUtil.saveHtmlToFile(entity.getArticleContent(),FilePathEnums.HELP.getValue());
				String url = S3FileUploadUtil.fileUploadHtml(file, FilePathEnums.HELP.getValue());
				entity.setArticleUrl(url);
				GenericityUtil.setDate(entity);
				helpDao.updateById(entity);
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
	public ResponseBase delete(Integer id) {
		try {
			HelpEntity helpEntity = helpDao.selectById(id);
			if(helpEntity != null) {
				helpDao.deleteById(id);
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
	public ResponseBase findList(HelpEntity entity) {
		try {
			String languag = LanguageContext.getLanguage();
			entity.setLanguage(languag);
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<HelpEntity> list = helpDao.findList(entity);
			PageInfo<HelpEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase pcFindList(HelpEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<HelpEntity> list = helpDao.findList(entity);
			PageInfo<HelpEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findById(Integer id) {
		try {
			HelpEntity entity = helpDao.selectById(id);
			return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	


}
