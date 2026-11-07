package com.worldpay.service.impl;
import com.worldpay.filter.JWTAuthenticationFilter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.worldpay.aop.SysLogAnnotation;
import com.worldpay.api.response.AppUserChannelEntity;
import com.worldpay.base.BaseApiService;
import com.worldpay.base.ResponseBase;
import com.worldpay.config.LanguageContext;
import com.worldpay.constants.Constants;
import com.worldpay.dao.appuser.AppUserDao;
import com.worldpay.dao.appuser.AppUserFileDao;import com.worldpay.dao.channel.ChannelUserDao;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.appuser.AppUserFileEntity;
import com.worldpay.entity.channel.ChannelUserEntity;
import com.worldpay.enums.IntiterUserTypeEnums;
import com.worldpay.enums.KycImgTypeEnums;
import com.worldpay.enums.MessageEnums;
import com.worldpay.enums.UserStateEnums;
import com.worldpay.query.UpdatePwdEntity;
import com.worldpay.service.UserService;
import com.worldpay.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@CrossOrigin
public class UserServiceImpl extends BaseApiService implements UserService{

	@Autowired
	private AppUserDao appUserDao;

	@Autowired
	private GoogleAuthenticatorUtil googleAuthenticator;

	@Autowired
	private AppUserFileDao appUserFileDao;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private MessageTemplateServiceImpl messageTemplateServiceImpl;

    @Autowired
    private ChannelUserDao channelUserDao;

	@Override
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "签发谷歌验证密钥")
	public ResponseBase IssueGoogleSecretkey(String uid) {
		try {
			String key = GoogleAuthenticatorUtil.createKey(uid).getKey();
			return setResultSuccess(key, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "修改谷歌验证密钥")
	public ResponseBase upGoogleSecretkey(String uid, String googleSecretkey) {
		try {
			AppUserEntity entity = appUserDao.findByUid(uid);
			if(entity != null) {
				entity.setGoogleSecretkey(googleSecretkey);
				entity.setGmtModified(new Date());
				appUserDao.updateById(entity);
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}
			return setResultError(I18nUtil.getMessage("base_error"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "校验谷歌验证密钥")
	public ResponseBase checkGoogleSecretkey(String uid, String googleCode) {
		try {
			AppUserEntity entity = appUserDao.findByUid(uid);
			if(entity != null && entity.getGoogleSecretkey() != null && entity.getGoogleSecretkey().length() > 0) {
				Boolean temp = googleAuthenticator.verifyCode(entity.getGoogleSecretkey(), Integer.parseInt(googleCode));
				if(temp) {
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
	@SysLogAnnotation(module = "app用户管理", type = "post", remark = "修改密码")
	public ResponseBase updaePwd(@RequestBody UpdatePwdEntity entity,HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
			String username = token.getName();
			AppUserEntity appUser = appUserDao.findByEmail(username);
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			if(userEntity != null && appUser != null) {
				if(!appUser.getUid().equals(userEntity.getUid())) {
					return setResultError(I18nUtil.getMessage("purview_error_null"));
				}
				if (!DigestUtils.md5DigestAsHex((entity.getFormerPassword()).getBytes())
						.equals(userEntity.getUserPassword())) {
					return setResultError(I18nUtil.getMessage("old_password_error"));
				}
				if(entity.getEmailCode() != null) {
					ResponseBase emailCodeData = this.checkEmailCode(userEntity.getUserEmail(),entity.getEmailCode());
					if(!Constants.HTTP_RES_CODE_200.equals(emailCodeData.getRtncode())) {
						return setResultError(I18nUtil.getMessage("verification_error"));
					}
				}else {
					return setResultError(I18nUtil.getMessage("verification_null"));
				}
				userEntity.setUserPassword(DigestUtils.md5DigestAsHex((entity.getNewPassword()).getBytes()));
				appUserDao.updateById(userEntity);
				
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.LOGIN_PASSWORD_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						entity.getNewPassword());
				
				
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
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "注销账户")
	public ResponseBase logout(String uid,HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
			String username = token.getName();
			AppUserEntity userEntity = appUserDao.findByEmail(username);
//			AppUserEntity entity = appUserDao.findByUid(uid);
			if(userEntity != null ) {
				if(!uid.equals(userEntity.getUid())) {
					return setResultError(I18nUtil.getMessage("purview_error_null"));
				}
				userEntity.setUserState(UserStateEnums.LOGOUT.getIndex());
				appUserDao.updateById(userEntity);
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
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "退出登录")
	public ResponseBase signOut(HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
			String username = token.getName();
			AppUserEntity userEntity = appUserDao.findByEmail(username);
			if(userEntity != null) {
				redisUtil.del(Constants.APP_PACKAGE_NAME + userEntity.getUserEmail());
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_success"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "app用户管理", type = "post", remark = "用户列表")
	public ResponseBase findList(@RequestBody AppUserEntity entity) {
		try {
			PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
			List<AppUserChannelEntity> list = appUserDao.findList(entity);
			for (AppUserChannelEntity appUserChannel : list) {
				if (appUserChannel.getInvitationLable() == null){
					continue;
				}
				if (appUserChannel.getInvitationLable().equals(IntiterUserTypeEnums.CHANNEL_USER.getIndex())){
					//渠道商
					String userId = appUserChannel.getSuperName();
					ChannelUserEntity channelUserEntity = channelUserDao.selectOne(new QueryWrapper<ChannelUserEntity>().eq("user_id", userId));
					if (channelUserEntity != null){
						appUserChannel.setSuperName(channelUserEntity.getUsername());
					}
				} else if (appUserChannel.getInvitationLable().equals(IntiterUserTypeEnums.APP_USER.getIndex())) {
					//c端
					String uid = appUserChannel.getSuperName();
					AppUserEntity appUserEntity = appUserDao.selectOne(new QueryWrapper<AppUserEntity>().eq("uid", uid));
					if(appUserEntity != null) {
						appUserChannel.setSuperName(appUserEntity.getUserEmail());
					}else{
						appUserChannel.setSuperName("");
					}
				}
			}
			PageInfo<AppUserChannelEntity> info = new PageInfo<>(list);
			return setResultSuccess(info, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "钱包入口开启关闭")
	public ResponseBase upWalletState(Integer id, Boolean state) {
		try {
			AppUserEntity userEntity = appUserDao.selectById(id);
			if(userEntity != null) {
				userEntity.setWalletState(state);
				appUserDao.updateById(userEntity);
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
	@SysLogAnnotation(module = "app用户管理", type = "get", remark = "查询用户kyc认证信息")
	public ResponseBase findKycFile(String uid) {
		try {
			List<AppUserFileEntity> list = appUserFileDao.findUid(uid);
			if(list != null && list.size() >0) {
				for (int i = 0; i < list.size(); i++) {
					if(KycImgTypeEnums.FRONT_PHOTO.getIndex().equals(list.get(i).getDocumentType())) {//正面
						list.get(i).setFrontPhotoId(list.get(i).getDocumentFileId());
						list.get(i).setFrontPhotoUrl(list.get(i).getDocumentFileUrl());
					}else if(KycImgTypeEnums.BACK_PHOTO.getIndex().equals(list.get(i).getDocumentType())) {//反面
						list.get(i).setBackPhotoId(list.get(i).getDocumentFileId());
						list.get(i).setBackPhotoUrl(list.get(i).getDocumentFileUrl());
					}else if(KycImgTypeEnums.HANDHELD_PHOTO.getIndex().equals(list.get(i).getDocumentType())) {//手持
						list.get(i).setHandheldPhotoId(list.get(i).getDocumentFileId());
						list.get(i).setHandheldPhotoUrl(list.get(i).getDocumentFileUrl());
					}
				}
			}
			return setResultSuccess(list, I18nUtil.getMessage("base_success"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase forgetPasswrod(String userEmail,String passwrod,String emailCode) {
		try {
			AppUserEntity entity = appUserDao.findByEmail(userEmail);
			if(entity != null) {
				String temp = null;
				if(redisUtil.get(entity.getUserEmail()) != null) {
					temp = redisUtil.get(userEmail).toString();
				}
				if(temp != null && emailCode.equals(temp)) {
					entity.setUserPassword(DigestUtils.md5DigestAsHex((passwrod).getBytes()));
					appUserDao.updateById(entity);
					
					//发送通知
					messageTemplateServiceImpl.unifiedSendAsync(
							MessageEnums.LOGIN_PASSWORD_ZH.getIndex(), 
							LanguageContext.getLanguage(),
							entity.getUid(), 
							userEmail);
					
					return setResultSuccess(I18nUtil.getMessage("base_success"));
				}else {
					return setResultError(I18nUtil.getMessage("verification_error"));
				}
			}else {
				return setResultError(I18nUtil.getMessage("account_error"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase upPayPasswrod(@RequestBody UpdatePwdEntity entity,HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
			String username = token.getName();
			AppUserEntity appUser = appUserDao.findByEmail(username);
			AppUserEntity userEntity = appUserDao.findByUid(entity.getUid());
			if(userEntity != null && appUser != null) {
				if(!appUser.getUid().equals(userEntity.getUid())) {
					return setResultError(I18nUtil.getMessage("purview_error_null"));
				}
				if(userEntity.getPayPassword() != null && userEntity.getPayPassword().length() > 0) {
					if (!DigestUtils.md5DigestAsHex((entity.getFormerPassword()).getBytes())
							.equals(userEntity.getPayPassword())) {
						return setResultError(I18nUtil.getMessage("old_password_error"));
					}
				}
				if(entity.getEmailCode() != null) {
					ResponseBase emailCodeData = this.checkEmailCode(userEntity.getUserEmail(),entity.getEmailCode());
					if(!Constants.HTTP_RES_CODE_200.equals(emailCodeData.getRtncode())) {
						return setResultError(I18nUtil.getMessage("verification_error"));
					}
				}else {
					return setResultError(I18nUtil.getMessage("verification_null"));
				}
				userEntity.setPayPassword(DigestUtils.md5DigestAsHex((entity.getNewPassword()).getBytes()));
				appUserDao.updateById(userEntity);
				
				//发送通知
				messageTemplateServiceImpl.unifiedSendAsync(
						MessageEnums.PAY_PASSWORD_ZH.getIndex(), 
						LanguageContext.getLanguage(),
						entity.getUid(), 
						username);
				
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("base_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public ResponseBase checkEmailCode(String userEmail,String emailCode) {
		try {
			String temp = null;
			if(redisUtil.get(userEmail) != null) {
				temp = redisUtil.get(userEmail).toString();
			}
			if(temp != null && emailCode.equals(temp)) {
				return setResultSuccess(I18nUtil.getMessage("base_success"));
			}else {
				return setResultError(I18nUtil.getMessage("verification_error"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
