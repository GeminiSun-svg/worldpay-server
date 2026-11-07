package com.worldpay.service.impl;
import com.worldpay.utils.api.UCardApi;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.utils.FileUploadUtil;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.response.UploadImageApiEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserFileDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserFileEntity;
import com.worldpay.enums.FilePathEnums;
import com.worldpay.enums.KycImgTypeEnums;
import com.worldpay.service.FileUploadService;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.I18nUtil;
import com.worldpay.utils.S3FileUploadUtil;

@Service
@Transactional
@CrossOrigin
public class FileUploadServiceImpl extends BaseApiService implements FileUploadService{
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private AppUserFileDao appUserFileDao;
	
	@Override
	@SysLogAnnotation(module = "通用文件上传", type = "post", remark = "上传文件")
	public ResponseBase uploadImage(MultipartFile file) {
		try {
			if(file != null) {
				String fileUrl = S3FileUploadUtil.fileUpload(file, FilePathEnums.RICHTEXT.getName());
				if(fileUrl != null && fileUrl.length() > 0) {
					return setResultSuccess(fileUrl, I18nUtil.getMessage("base_success"));
				}
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	@SysLogAnnotation(module = "通用文件上传", type = "post", remark = "三方kyc通用上传文件")
	public ResponseBase apiUploadImage(String uid,Integer type, MultipartFile file) {
		try {
			if(file == null) {
				return setResultError(I18nUtil.getMessage("file_check"));
			}
			// 获取文件名
	        String fileName = file.getOriginalFilename();
	        if (fileName == null) {
	            return setResultError(I18nUtil.getMessage("file_name_check"));
	        }
	        // 转小写，防止大小写问题
	        String lowerName = fileName.toLowerCase();
	        // 允许的文件后缀
	        List<String> allowedExt = Arrays.asList(".png", ".pdf", ".jpg", ".jpeg");
	        // 检查文件扩展名
	        boolean valid = allowedExt.stream().anyMatch(lowerName::endsWith);
	        if (!valid) {
	            return setResultError(I18nUtil.getMessage("file_type_check"));
	        }
	        AppUserEntity entity = appUserDao.findByUid(uid);
			if(entity != null) {
	            byte[] fileBytes = file.getBytes();
	            File tempFile = FileUploadUtil.createTempFileFromBytes(fileBytes, file.getOriginalFilename());
				//调用三方
				ResponseBase base = UCardApi.postFormFile(uid, Constants.ID_CARD, tempFile, UCardMethods.UPLOAD_FILE);
				if(Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
					UploadImageApiEntity uploadImage = JSONObject.parseObject(base.getData().toString(), UploadImageApiEntity.class);
					AppUserFileEntity fileEntity = new AppUserFileEntity();
					fileEntity.setDocumentFileId(uploadImage.getDocumentFileId());
					fileEntity.setDocumentFileUrl(uploadImage.getFileUrl());	
					fileEntity.setUid(uid);
					fileEntity.setCertType(1);
					fileEntity.setDocumentType(type);
					fileEntity.setDocumentTypeName(KycImgTypeEnums.getName(type));

					if(KycImgTypeEnums.FRONT_PHOTO.getIndex().equals(type)) {//正面
						fileEntity.setFrontPhotoId(fileEntity.getDocumentFileId());
						fileEntity.setFrontPhotoUrl(fileEntity.getDocumentFileUrl());
					}else if(KycImgTypeEnums.BACK_PHOTO.getIndex().equals(type)) {//反面
						fileEntity.setBackPhotoId(fileEntity.getDocumentFileId());
						fileEntity.setBackPhotoUrl(fileEntity.getDocumentFileUrl());
					}else if(KycImgTypeEnums.HANDHELD_PHOTO.getIndex().equals(type)) {//手持
						fileEntity.setHandheldPhotoId(fileEntity.getDocumentFileId());
						fileEntity.setHandheldPhotoUrl(fileEntity.getDocumentFileUrl());
					}
					//删除原有kyc图
					appUserFileDao.deleteOne(uid,type);
					GenericityUtil.setDate(fileEntity);
					appUserFileDao.insert(fileEntity);
					return setResultSuccess(fileEntity, I18nUtil.getMessage("base_success"));
				}else {
					return base;
				}
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			return setResultError(I18nUtil.getMessage("base_error"));
		}
	}
	

	@SysLogAnnotation(module = "通用文件上传", type = "post", remark = "三方kyc通用上传文件")
	public ResponseBase pathUploadImage(String uid, Integer type, String filePath) {
	    try {
	        if (filePath == null || filePath.isEmpty()) {
	            return setResultError(I18nUtil.getMessage("file_check"));
	        }
	        
	        // 创建File对象
	        File file = new File(filePath);
	        if (!file.exists() || !file.isFile()) {
	            return setResultError("文件不存在: " + filePath);
	        }
	        
	        // 获取文件名
	        String fileName = file.getName();
	        if (fileName == null || fileName.isEmpty()) {
	            return setResultError(I18nUtil.getMessage("file_name_check"));
	        }
	        
	        // 转小写，防止大小写问题
	        String lowerName = fileName.toLowerCase();
	        // 允许的文件后缀
	        List<String> allowedExt = Arrays.asList(".png", ".pdf", ".jpg", ".jpeg");
	        // 检查文件扩展名
	        boolean valid = allowedExt.stream().anyMatch(lowerName::endsWith);
	        if (!valid) {
	            return setResultError(I18nUtil.getMessage("file_type_check"));
	        }
	      //调用三方
	        ResponseBase base = UCardApi.postFormFile(uid, Constants.ID_CARD, file, UCardMethods.UPLOAD_FILE);
			if(Constants.HTTP_RES_CODE_200.equals(base.getRtncode())) {
				UploadImageApiEntity uploadImage = JSONObject.parseObject(base.getData().toString(), UploadImageApiEntity.class);
				AppUserFileEntity fileEntity = new AppUserFileEntity();
				fileEntity.setDocumentFileId(uploadImage.getDocumentFileId());
				fileEntity.setDocumentFileUrl(uploadImage.getFileUrl());	
				fileEntity.setUid(uid);
				fileEntity.setCertType(1);
				fileEntity.setDocumentType(type);
				fileEntity.setDocumentTypeName(KycImgTypeEnums.getName(type));

				if(KycImgTypeEnums.FRONT_PHOTO.getIndex().equals(type)) {//正面
					fileEntity.setFrontPhotoId(fileEntity.getDocumentFileId());
					fileEntity.setFrontPhotoUrl(fileEntity.getDocumentFileUrl());
				}else if(KycImgTypeEnums.BACK_PHOTO.getIndex().equals(type)) {//反面
					fileEntity.setBackPhotoId(fileEntity.getDocumentFileId());
					fileEntity.setBackPhotoUrl(fileEntity.getDocumentFileUrl());
				}else if(KycImgTypeEnums.HANDHELD_PHOTO.getIndex().equals(type)) {//手持
					fileEntity.setHandheldPhotoId(fileEntity.getDocumentFileId());
					fileEntity.setHandheldPhotoUrl(fileEntity.getDocumentFileUrl());
				}
				//删除原有kyc图
				appUserFileDao.deleteOne(uid,type);
				GenericityUtil.setDate(fileEntity);
				appUserFileDao.insert(fileEntity);
				return setResultSuccess(fileEntity, I18nUtil.getMessage("base_success"));
			}else {
				return base;
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	        return setResultError(I18nUtil.getMessage("base_error"));
	    }
	}
	
}
