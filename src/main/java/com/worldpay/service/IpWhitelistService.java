package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.IpWhitelistEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface IpWhitelistService {

ResponseBase findUserCardAssets(IpWhitelistEntity entity);

ResponseBase add(IpWhitelistEntity entity);

ResponseBase delete(Integer id);

}
