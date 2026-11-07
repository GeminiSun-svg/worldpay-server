package com.worldpay.service;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysUserEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface SysUserService {

ResponseBase findList(SysUserEntity entity);

ResponseBase signUp(String user,MultipartFile faceFile);

ResponseBase findToken(HttpServletRequest request);

ResponseBase delUser(Integer userId);

ResponseBase updateUserState( Integer userId, Integer newState);

ResponseBase updateUser( String user,MultipartFile file);

ResponseBase resetPwd(Integer userId,String password);

ResponseBase resetUserPwd(Integer userId,String password);

ResponseBase verifyPwd(Integer userId,String password);

ResponseBase updateDeviceModel(SysUserEntity entity);

ResponseBase generateToken();

ResponseBase upGoogleSecretkey(Integer userId,String googleSecretkey);

ResponseBase IssueGoogleSecretkey(String userName);

ResponseBase getPasswordOrKey();

}
