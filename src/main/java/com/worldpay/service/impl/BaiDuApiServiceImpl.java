package com.worldpay.service.impl;


import org.springframework.stereotype.Service;
import com.worldpay.utils.AddressExtractor;
import com.worldpay.utils.api.UCardApi;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.worldpay.api.request.KycApplyEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.constants.UCardMethods;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.card.CardApplyKycDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserFileEntity;
import com.worldpay.entity.baidu.AddressInfo;
import com.worldpay.entity.baidu.FileInfo;
import com.worldpay.entity.baidu.IdCardResult;
import com.worldpay.entity.baidu.ImageFileInfo;
import com.worldpay.entity.baidu.KycDataWrapper;
import com.worldpay.entity.baidu.KycDefaultData;
import com.worldpay.entity.baidu.NameParts;
import com.worldpay.entity.baidu.ProcessedData;
import com.worldpay.entity.baidu.UploadResult;
import com.worldpay.entity.card.CardApplyKycEntity;
import com.worldpay.enums.KycStateEnums;
import com.worldpay.enums.LanguageEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.RobotRoleEnums;
import com.worldpay.utils.BaiduAuthApiUtil;
import com.worldpay.utils.GenericityUtil;
import com.worldpay.utils.HKPhoneNumberGenerator;

import cn.hutool.extra.pinyin.PinyinUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BaiDuApiServiceImpl extends BaseApiService{

    @Value("${baidu.diskSite:}")
    private String DISK_SITE;

    @Autowired
    private KycFileIndexServiceImpl sysConfigService;

    @Autowired
    private FileUploadServiceImpl fileUploadServiceImpl;
    
    @Autowired
    private AppUserDao appUserDao;
    
    @Autowired
    private CardApplyKycDao cardApplyKycDao;
    
    @Autowired
    private MessageTemplateServiceImpl messageTemplateServiceImpl;

    // 内存缓存，减少数据库查询
    private static volatile int cachedIndex = -1;

    public ResponseBase getToken() {
        try {
            String token = BaiduAuthApiUtil.getAccessToken();
            log.info("获取百度Token成功: {}", token);
            return setResultSuccess(token);
        } catch (IOException e) {
            log.error("获取百度Token失败", e);
            return setResultError("获取Token失败");
        }
    }

    @Async("asyncExecutor")
    public void idCardDiscern(String uid, String currentLanguage) {
        log.info("开始身份证识别流程, uid: {}", uid);
        try {
        	LanguageContext.setLanguage(currentLanguage);
            // 1. 验证用户
            AppUserEntity user = validateUser(uid);
            if (user == null) return;
            
            // 2. 获取图片文件
            ImageFileInfo fileInfo = getValidImageFile();
            if (fileInfo == null) {
            	//发送飞机消息告知客服管理端重新提交kyc认证
    			messageTemplateServiceImpl.robotSendAsync(
    					MessageEnums.ROBOT_KYC_NULL_ZH.getIndex(),
    					LanguageEnums.ZH_CN.getName(),
    					RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
    					uid,
    					"未找到有效的身份证图片文件");
                log.error("未找到有效的身份证图片文件, uid: {}", uid);
                return;
            }
            // 3. 上传图片
            UploadResult uploadResult = uploadImages(uid, fileInfo);
            if (!uploadResult.isSuccess()) {
            	//发送飞机消息告知客服管理端重新提交kyc认证
    			messageTemplateServiceImpl.robotSendAsync(
    					MessageEnums.ROBOT_KYC_NULL_ZH.getIndex(),
    					LanguageEnums.ZH_CN.getName(),
    					RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
    					uid,
    					"身份证图片上传失败");
                log.error("身份证图片上传失败, uid: {}", uid);
                updateCurrentIndex(fileInfo.getIndex() + 1);
                return;
            }
            // 4. 识别身份证
            IdCardResult cardResult = recognizeIdCard(fileInfo.getPath());
            if (cardResult == null) {
            	//发送飞机消息告知客服管理端重新提交kyc认证
    			messageTemplateServiceImpl.robotSendAsync(
    					MessageEnums.ROBOT_KYC_NULL_ZH.getIndex(),
    					LanguageEnums.ZH_CN.getName(),
    					RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
    					uid,
    					"身份证无法提取有效信息");
                log.error("身份证识别失败, uid: {}", uid);
                updateCurrentIndex(fileInfo.getIndex() + 1);
                return;
            }
            // 5. 处理识别数据
            ProcessedData processedData = processIdCardData(cardResult);
            // 6. 更新索引
            updateCurrentIndex(fileInfo.getIndex() + 1);
            
            // 7. 提交KYC申请
            submitKycApplication(uid, user, uploadResult, processedData, cardResult);
            log.info("身份证识别流程完成, uid: {}", uid);
            
        } catch (Exception e) {
            log.error("身份证识别流程异常, uid: {}", uid, e);
            throw new RuntimeException("身份证识别失败: " + e.getMessage(), e);
        }
    }

    // ==================== 核心业务方法 ====================

    /**
     * 验证用户是否存在
     */
    private AppUserEntity validateUser(String uid) {
        AppUserEntity user = appUserDao.findByUid(uid);
        if (user == null) {
            log.warn("用户不存在, uid: {}", uid);
        }
        return user;
    }

    
    /**
     * 获取有效的图片文件（需要1.jpg、2.jpg、3.jpg三个文件都存在）
     */
    private ImageFileInfo getValidImageFile() {
        int next = getCurrentIndex();
        
        // 检查当前索引
        if (checkAllImagesExist(next)) {
            return new ImageFileInfo(next, DISK_SITE + next + "/1.jpg");
        }
        
        // 查找后续索引
        for (int i = 1; i <= 100; i++) {
            int checkIndex = next + i;
            if (checkAllImagesExist(checkIndex)) {
                log.info("自动查找到完整图片组，索引: {}", checkIndex);
                return new ImageFileInfo(checkIndex, DISK_SITE + checkIndex + "/1.jpg");
            }
        }
        
        log.error("未找到完整的身份证图片文件组（需要1.jpg、2.jpg、3.jpg），起始序号: {}", next);
        return null;
    }

    /**
     * 检查指定索引下的三个图片文件是否都存在
     */
    private boolean checkAllImagesExist(int index) {
        File file1 = new File(DISK_SITE + index + "/1.jpg");
        File file2 = new File(DISK_SITE + index + "/2.jpg");
        File file3 = new File(DISK_SITE + index + "/3.jpg");
        
        return file1.exists() && file2.exists() && file3.exists();
    }
    

    /**
     * 上传三张身份证图片
     */
    private UploadResult uploadImages(String uid, ImageFileInfo fileInfo) {
        String basePath = DISK_SITE + fileInfo.getIndex();
        
        // 上传三张图片
        ResponseBase frontResp = fileUploadServiceImpl.pathUploadImage(uid, 1, fileInfo.getPath());
        ResponseBase backResp = fileUploadServiceImpl.pathUploadImage(uid, 2, basePath + "/2.jpg");
        ResponseBase handheldResp = fileUploadServiceImpl.pathUploadImage(uid, 3, basePath + "/3.jpg");
        
        // 检查上传结果
        if (!isUploadSuccess(frontResp, backResp, handheldResp)) {
            log.error("图片上传失败 - front: {}, back: {}, handheld: {}", 
                frontResp.getRtncode(), backResp.getRtncode(), handheldResp.getRtncode());
            return UploadResult.failed();
        }
        
        // 解析上传结果
        AppUserFileEntity frontEntity = parseToFileEntity(frontResp.getData());
        AppUserFileEntity backEntity = parseToFileEntity(backResp.getData());
        AppUserFileEntity handheldEntity = parseToFileEntity(handheldResp.getData());
        
        return UploadResult.success(frontEntity, backEntity, handheldEntity);
    }

    /**
     * 检查上传是否全部成功
     */
    private boolean isUploadSuccess(ResponseBase... responses) {
        for (ResponseBase resp : responses) {
            if (!Constants.HTTP_RES_CODE_200.equals(resp.getRtncode())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解析响应数据为文件实体
     */
    private AppUserFileEntity parseToFileEntity(Object data) {
        return JSONObject.parseObject(JSON.toJSONString(data), AppUserFileEntity.class);
    }

    /**
     * 调用百度API识别身份证
     */
    private IdCardResult recognizeIdCard(String imagePath) {
        try {
            log.info("开始识别身份证图片: {}", imagePath);
            String result = BaiduAuthApiUtil.idcard(imagePath, "front");
            IdCardResult cardResult = JSONObject.parseObject(result, IdCardResult.class);
            
            if (cardResult == null || cardResult.getIdNumber() == null) {
                log.error("身份证识别结果为空");
                return null;
            }
            
            return cardResult;
            
        } catch (Exception e) {
            log.error("调用百度API识别身份证失败", e);
            return null;
        }
    }

    /**
     * 处理身份证识别数据
     */
    private ProcessedData processIdCardData(IdCardResult cardResult) {
        String formattedBirthday = formatBirthday(cardResult.getBirthday());
        NameParts nameParts = splitChineseName(cardResult.getName());
        AddressInfo addressInfo = AddressExtractor.extractAll(cardResult.getAddress());
        
        log.info("数据处理完成 - 生日: {}, 姓名: {}, 地址: {}", 
            formattedBirthday, nameParts.getFirstName() + nameParts.getLastName(), addressInfo);
        
        return new ProcessedData(formattedBirthday, nameParts, addressInfo);
    }

    /**
     * 提交KYC申请
     */
    private void submitKycApplication(String uid, AppUserEntity user, UploadResult uploadResult,
                                       ProcessedData processedData, IdCardResult cardResult) {
        // 构建KYC数据
        KycDataWrapper kycWrapper = buildKycData(uid, user, uploadResult, processedData, cardResult);
        
        // 调用KYC接口
        log.info("开始提交KYC申请, uid: {}", uid);
        ResponseBase response = UCardApi.postData(uid, UCardMethods.USER_KYC_APPLY, kycWrapper.getKycData(), null);
        if (Constants.HTTP_RES_CODE_200.equals(response.getRtncode())) {
            // 更新用户状态
            updateUserKycStatus(user);
            // 保存申请记录
            saveKycApplyRecord(uid, uploadResult, cardResult, kycWrapper.getKycData());
            log.info("KYC申请提交成功, uid: {}", uid);
        } else {
        	//发送kyc认证失败消息
        	messageTemplateServiceImpl.unifiedSendAsync(
					MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(), 
					LanguageContext.getLanguage(),
					uid,
					response.getMsg());
        	//发送飞机消息告知客服管理端重新提交kyc认证
			messageTemplateServiceImpl.robotSendAsync(
					MessageEnums.ROBOT_KYC_ERROR_ZH.getIndex(),
					LanguageEnums.ZH_CN.getName(),
					RobotRoleEnums.CUSTOMER_SERVICE.getIndex(),
					uid,
					response.getMsg());
            log.error("KYC申请提交失败, uid: {}, response: {}", uid, response.getRtncode(),response.getMsg());
        }
    }

    /**
     * 构建KYC请求数据
     */
    private KycDataWrapper buildKycData(String uid, AppUserEntity user, UploadResult uploadResult,
                                         ProcessedData processedData, IdCardResult cardResult) {
    	
    	KycApplyEntity kycData = new KycApplyEntity();
		kycData.setAreaCode(KycDefaultData.AREA_CODE);
		kycData.setBirthday(processedData.getFormattedBirthday());
		kycData.setCertType(KycDefaultData.CERTTYPE);
		kycData.setCountryCode(KycDefaultData.COUNTRY_CODE);
		kycData.setEmail(user.getUserEmail());
		kycData.setIdBackUrl(uploadResult.getBackEntity().getDocumentFileUrl());
		kycData.setIdNo(cardResult.getIdNumber());
		kycData.setIdUrl(uploadResult.getFrontEntity().getDocumentFileUrl());
		kycData.setLastName(processedData.getNameParts().getLastName());
		kycData.setFirstName(processedData.getNameParts().getFirstName());
		kycData.setNationCode(KycDefaultData.COUNTRY_CODE);
		kycData.setPhone(HKPhoneNumberGenerator.generate());
        
        return new KycDataWrapper(kycData);
    }

    /**
     * 更新用户KYC状态
     */
    private void updateUserKycStatus(AppUserEntity user) {
        user.setKycState(KycStateEnums.PROCESS_APPROVE.getIndex());
        user.setKycStateName(KycStateEnums.PROCESS_APPROVE.getName());
        appUserDao.updateById(user);
        log.info("用户KYC状态已更新: {}", user.getKycStateName());
    }

    /**
     * 保存KYC申请记录
     */
    private void saveKycApplyRecord(String uid, UploadResult uploadResult,
                                     IdCardResult cardResult, KycApplyEntity kycData) {
      try {
    	  CardApplyKycEntity kycEntity = new CardApplyKycEntity();
          
          kycEntity.setBackPhotoId(uploadResult.getBackEntity().getDocumentFileId());
          kycEntity.setBackPhotoUrl(uploadResult.getBackEntity().getDocumentFileUrl());
          kycEntity.setExpirationTime(KycDefaultData.IDENTIFICATIONEXPIRYDATE);
          kycEntity.setFrontPhotoId(uploadResult.getFrontEntity().getDocumentFileId());
          kycEntity.setFrontPhotoUrl(uploadResult.getFrontEntity().getDocumentFileUrl());
          kycEntity.setHandheldPhotoId(uploadResult.getHandheldEntity().getDocumentFileId());
          kycEntity.setHandheldPhotoUrl(uploadResult.getHandheldEntity().getDocumentFileUrl());
          kycEntity.setPaperworkNum(cardResult.getIdNumber());
          kycEntity.setPaperworkType(KycDefaultData.IDENTIFICATIONTYPE);
          kycEntity.setUid(uid);
          GenericityUtil.setDate(kycEntity);
          
          cardApplyKycDao.insert(kycEntity);
          log.info("KYC申请记录已保存, uid: {}", uid);
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException();
	}
    }

    // ==================== 辅助方法 ====================

    /**
     * 获取当前索引（带缓存）
     */
    private int getCurrentIndex() {
        if (cachedIndex > 0) {
            return cachedIndex;
        }
        int index = sysConfigService.getIdCardIndex();
        cachedIndex = index;
        return index;
    }

    /**
     * 更新索引（同时更新数据库和缓存）
     */
    private void updateCurrentIndex(int index) {
        sysConfigService.updateIdCardIndex(index);
        cachedIndex = index;
        log.info("数据库索引已更新为: {}", index);
    }

    /**
     * 查找下一个存在的文件
     */
    @SuppressWarnings("unused")
	private FileInfo findNextExistingFile(int startIndex) {
        String basePath = DISK_SITE;
        for (int i = 0; i < 100; i++) {
            int checkIndex = startIndex + i;
            String imagePath = basePath + checkIndex + "/1.jpg";
            File file = new File(imagePath);
            if (file.exists() && file.isFile()) {
                FileInfo info = new FileInfo();
                info.setIndex(checkIndex);
                info.setPath(imagePath);
                return info;
            }
        }
        return null;
    }

    /**
     * 格式化生日 yyyyMMdd -> yyyy-MM-dd
     */
    private String formatBirthday(String birthday) {
        if (birthday == null || birthday.isEmpty()) {
            return "";
        }

        if (birthday.length() == 8 && birthday.matches("\\d{8}")) {
            return String.format("%s-%s-%s", 
                birthday.substring(0, 4),
                birthday.substring(4, 6),
                birthday.substring(6, 8));
        }

        if (birthday.length() == 10 && birthday.contains("-")) {
            return birthday;
        }

        return birthday;
    }

    /**
     * 分割中文姓名
     */
    private NameParts splitChineseName(String chineseName) {
        NameParts nameParts = new NameParts();

        if (chineseName == null || chineseName.isEmpty()) {
            return nameParts;
        }

        int length = chineseName.length();

        if (length == 2 || length == 3) {
            // 2-3字姓名：第一个字为姓，其余为名
            String lastName = chineseName.substring(0, 1);
            String firstName = chineseName.substring(1);
            nameParts.setLastName(convertToPinyin(lastName));
            nameParts.setFirstName(convertToPinyin(firstName));
        } else if (length > 3) {
            // 4字及以上：前两字为姓
            String lastName = chineseName.substring(0, 2);
            String firstName = chineseName.substring(2);
            nameParts.setLastName(convertToPinyin(lastName));
            nameParts.setFirstName(convertToPinyin(firstName));
        } else {
            nameParts.setFirstName(convertToPinyin(chineseName));
        }

        return nameParts;
    }

    /**
     * 中文转拼音（首字母大写）
     */
    private String convertToPinyin(String chinese) {
        if (chinese == null || chinese.isEmpty()) {
            return "";
        }

        StringBuilder pinyin = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            String py = PinyinUtil.getPinyin(c);
            if (py != null && !py.isEmpty()) {
                pinyin.append(py.substring(0, 1).toUpperCase())
                      .append(py.substring(1).toLowerCase());
            }
        }
        return pinyin.toString();
    }


    /**
     * 重置索引（管理接口）
     */
    public ResponseBase resetIndex(int newIndex) {
        if (newIndex < 0) {
            return setResultError("索引不能为负数");
        }
        updateCurrentIndex(newIndex);
        return setResultSuccess("索引已重置为: " + newIndex);
    }

    /**
     * 获取当前索引状态（管理接口）
     */
    public ResponseBase getCurrentIndexStatus() {
        JSONObject result = new JSONObject();
        result.put("currentIndex", getCurrentIndex());
        result.put("cachedIndex", cachedIndex);
        return setResultSuccess(result);
    }
   
}