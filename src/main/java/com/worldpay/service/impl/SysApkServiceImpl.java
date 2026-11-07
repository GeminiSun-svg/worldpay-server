package com.worldpay.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.dao.system.SysApkDao;
import com.worldpay.entity.system.SysApkEntity;
import com.worldpay.enums.FilePathEnums;
import com.worldpay.enums.PublicEnums;
import com.worldpay.service.SysApkService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.S3FileUploadUtil;

@Service
@Transactional
public class SysApkServiceImpl extends BaseApiService implements SysApkService{
	
	@Autowired
	private SysApkDao sysApkDao;

	@Override
	@SysLogAnnotation(module = "应用更新配置", type = "post", remark = "新增版本更新")
	public ResponseBase add(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				SysApkEntity entity = JSONObject.parseObject(dataStr, SysApkEntity.class);
				if(entity.getPlatform().equals(PublicEnums.ONE.getIndex())) {//安卓平台
					if(file == null) {
						return setResultError(I18nUtil.getMessage("sys_apk_check"));
					}
					//上传亚马逊
					String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.APK.getName());
					if(fileUrl != null && fileUrl.length() > 0) {
						entity.setApkUrl(fileUrl);
					}
				}
				entity.setState(PublicEnums.ONE.getIndex());
				GenericityUtil.setDate(entity);
				sysApkDao.insert(entity);
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
	@SysLogAnnotation(module = "应用更新配置", type = "post", remark = "编辑版本更新")
	public ResponseBase update(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				SysApkEntity entity = JSONObject.parseObject(dataStr, SysApkEntity.class);
				SysApkEntity apkEntity = sysApkDao.selectById(entity.getId());
				if(apkEntity != null) {
					if(entity.getPlatform().equals(PublicEnums.ONE.getIndex())) {
						if(file != null) {
							//上传亚马逊
							String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.APK.getName());
							if(fileUrl != null && fileUrl.length() > 0) {
								entity.setApkUrl(fileUrl);
							}	
						}
					}
					sysApkDao.updateById(entity);
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "应用更新配置", type = "post", remark = "查询列表")
	public ResponseBase findList(SysApkEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<SysApkEntity> list = sysApkDao.findList(entity);
			PageInfo<SysApkEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "应用更新配置", type = "get", remark = "查询最新版本(app检测更新)")
	public ResponseBase findLastVersion() {
		try {
			//查询最新发布安卓包
			SysApkEntity androidDowlod = sysApkDao.findNew(PublicEnums.ONE.getIndex(),PublicEnums.TOW.getIndex());
			//查询最新发布苹果包
			SysApkEntity iosDowlod = sysApkDao.findNew(PublicEnums.TOW.getIndex(),PublicEnums.TOW.getIndex());
			Map<String, Object> map = new HashMap<>();
			map.put("androidDowlod", androidDowlod);
			map.put("iosDowlod", iosDowlod);
			return setResultSuccess(map, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	@SysLogAnnotation(module = "应用更新配置", type = "get", remark = "发布")
	public ResponseBase release(Integer id) {
		try {
			SysApkEntity apkEntity = sysApkDao.selectById(id);
			if(apkEntity != null) {
				if(PublicEnums.TOW.getIndex().equals(apkEntity.getState())) {
					return setResultError(I18nUtil.getMessage("sys_apk_version_check"));
				}
				if(PublicEnums.ONE.getIndex().equals(apkEntity.getPlatform())) {//安卓
					//下架所有安卓apk
					sysApkDao.updateState(PublicEnums.ONE.getIndex(),PublicEnums.THREE.getIndex());
				}else if(PublicEnums.TOW.getIndex().equals(apkEntity.getPlatform())) {//ios
					//下架所有ios
					sysApkDao.updateState(PublicEnums.TOW.getIndex(),PublicEnums.THREE.getIndex());
				}
				apkEntity.setState(PublicEnums.TOW.getIndex());
				sysApkDao.updateById(apkEntity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	
}
