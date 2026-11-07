package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.appuser.AppUserHolderEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface CardholderService {

ResponseBase add(AppUserHolderEntity entity);

ResponseBase update(AppUserHolderEntity entity);

ResponseBase delete(Integer id);

ResponseBase findByUid(String uid);

ResponseBase findById(Integer id);

}
