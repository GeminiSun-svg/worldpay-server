package com.worldpay.service.impl;

import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.dao.system.SysInfoDao;

import com.worldpay.entity.system.SysInfoEntity;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.NoticeStateEnums;
import com.worldpay.enums.SysConfigTypeEnums;
import com.worldpay.service.SysInfoService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.S3FileUploadUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
public class SysInfoServiceImpl extends BaseApiService implements SysInfoService{
	
	@Autowired
	private SysInfoDao SysInfoDao;

	@Override
	@SysLogAnnotation(module = "配置接口", type = "POST", remark = "查询所有配置")
	public ResponseBase findAll(SysInfoEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<SysInfoEntity> list = SysInfoDao.findList(entity);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setLanguageName(LanguageEnums.getLable(list.get(i).getLanguage()));
				}
			}
			PageInfo<SysInfoEntity> info = new PageInfo<>(list);
			return setResultSuccess(info,I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "配置接口", type = "GET", remark = "查询配置")
	public ResponseBase findById(Integer id) {
		SysInfoEntity entity = SysInfoDao.selectById(id);
		return setResultSuccess(entity,I18nUtil.getMessage("base_success"));
	}

   

    @Override
    @SysLogAnnotation(module = "配置接口", type = "POST", remark = "新增配置")
    public ResponseBase add(SysInfoEntity entity) {
        try {
        	entity.setConfigTypeName(SysConfigTypeEnums.getType(entity.getConfigType()));
        	entity.setConfigLable(SysConfigTypeEnums.getLable(entity.getConfigType()));
        	entity.setConfigName(SysConfigTypeEnums.getLanguageName(entity.getConfigType(), entity.getLanguage()));
        	entity.setStatus(NoticeStateEnums.NORMAL.getIndex());
        	MultipartFile file = com.worldpay.utils.FileUploadUtil.saveHtmlToFile(entity.getConfigContent(),SysConfigTypeEnums.getType(entity.getConfigType()));
//			String url = this.fileUploadUtil(file,SysConfigTypeEnums.getType(entity.getConfigType()));
			String url = S3FileUploadUtil.fileUpload(file, SysConfigTypeEnums.getType(entity.getConfigType()));
			entity.setConfigUrl(url);
            GenericityUtil.setDate(entity);
            SysInfoDao.insert(entity);
            return setResultSuccess(I18nUtil.getMessage("base_success"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @SysLogAnnotation(module = "配置接口", type = "POST", remark = "编辑配置信息")
    public ResponseBase update(SysInfoEntity entity) {
        try {
            if(entity.getId() == null){
                return setResultError(I18nUtil.getMessage("base_data_null"));
            }
            SysInfoEntity infoEntity = SysInfoDao.selectById(entity.getId());
            if(infoEntity != null) {
            	entity.setConfigTypeName(SysConfigTypeEnums.getType(entity.getConfigType()));
            	entity.setConfigLable(SysConfigTypeEnums.getLable(entity.getConfigType()));
//            	entity.setConfigName(SysConfigTypeEnums.getName(entity.getConfigType()));
            	entity.setConfigName(SysConfigTypeEnums.getLanguageName(entity.getConfigType(), entity.getLanguage()));
            	MultipartFile file = com.worldpay.utils.FileUploadUtil.saveHtmlToFile(entity.getConfigContent(),SysConfigTypeEnums.getType(entity.getConfigType()));
    			String url = S3FileUploadUtil.fileUpload(file, SysConfigTypeEnums.getType(entity.getConfigType()));
    			entity.setConfigUrl(url);
                SysInfoDao.updateById(entity);
                return setResultSuccess(I18nUtil.getMessage("base_success"));
            }else {
                return setResultError(I18nUtil.getMessage("base_data_null"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @SysLogAnnotation(module = "配置接口", type = "GET", remark = "删除配置")
    public ResponseBase delete(Integer id) {
        try {
            SysInfoEntity entity = SysInfoDao.selectById(id);
            if(entity != null) {
//                entity.setDelFlag(RoleStateEnums.ROLE_DEL_FLAG_DELETE.getIndex());
                SysInfoDao.deleteById(id);
                return setResultSuccess(I18nUtil.getMessage("base_success"));
            }else {
                return setResultError(I18nUtil.getMessage("base_data_null"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

	@Override
	@SysLogAnnotation(module = "配置接口", type = "POST", remark = "配置停用启用")
	public ResponseBase changeStatus(Integer id, Integer statusType) {
		try {
			SysInfoEntity entity = SysInfoDao.selectById(id);
			if(entity != null) {
				entity.setStatus(statusType);
				SysInfoDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_data_null"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "配置接口", type = "get", remark = "查询配置（1：用户协议；2：隐私协议；3：关于我们；4：联系我们；5：客服；7：帮助中心）")
	public ResponseBase findConfigInfo(Integer configType) {
		String language = LanguageContext.getLanguage();
		SysInfoEntity entity = SysInfoDao.findContent(configType,NoticeStateEnums.NORMAL.getIndex(),language);
		return setResultSuccess(entity, I18nUtil.getMessage("base_success"));
	}
	
}
