package com.worldpay.service;

import jakarta.servlet.http.HttpServletRequest;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.query.UpdatePwdEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface UserService {

ResponseBase IssueGoogleSecretkey(String uid);

ResponseBase upGoogleSecretkey(String uid,String googleSecretkey);

ResponseBase checkGoogleSecretkey(String uid,String googleCode);

ResponseBase updaePwd(UpdatePwdEntity entity,HttpServletRequest request);

ResponseBase logout(String uid,HttpServletRequest request);

ResponseBase signOut(HttpServletRequest request);

ResponseBase findList(AppUserEntity entity);

ResponseBase upWalletState(Integer id,Boolean state);

ResponseBase findKycFile(String uid);

ResponseBase forgetPasswrod(String userEmail,String passwrod,String emailCode);

ResponseBase upPayPasswrod(UpdatePwdEntity entity,HttpServletRequest request);
	

}
