package com.worldpay.service;


import com.worldpay.base.ResponseBase;
import com.worldpay.entity.product.ProductTypeEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface ProductTypeService {

ResponseBase add(ProductTypeEntity entity);

ResponseBase findList(ProductTypeEntity entity);

ResponseBase delete(Integer id);

ResponseBase findAll();

ResponseBase pcFindAll(String language);

}
