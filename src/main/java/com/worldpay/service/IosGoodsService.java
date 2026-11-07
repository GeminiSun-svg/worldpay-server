package com.worldpay.service;


import com.worldpay.api.request.IosPayReqEntity;
import com.worldpay.base.ResponseBase;
import com.worldpay.entity.goods.IosGoodsEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface IosGoodsService {

ResponseBase findAll();

ResponseBase findById(Integer id);

ResponseBase findByNum(String num);

ResponseBase verify(IosPayReqEntity entity);

ResponseBase add(IosGoodsEntity entity);

ResponseBase update(IosGoodsEntity entity);

ResponseBase delete(Integer id);

ResponseBase findList(IosGoodsEntity entity);

}
