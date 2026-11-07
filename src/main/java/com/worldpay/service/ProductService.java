package com.worldpay.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.product.ProductEntity;
import com.worldpay.entity.product.ProductOrderEntity;
import com.worldpay.query.ProductPayEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface ProductService {

	ResponseBase add(String dataStr,MultipartFile file);

	ResponseBase update(String dataStr,MultipartFile file);

	ResponseBase OnAndOff(Integer id,Integer type,String language);

	ResponseBase findList(ProductEntity entity);

	ResponseBase pcFindList(ProductEntity entity);

	ResponseBase pay(ProductPayEntity entity);

	ResponseBase userAssets(String uid);

	ResponseBase orderList(ProductOrderEntity entity);

	ResponseBase orderFinish(Integer id);

	ResponseBase delete(Integer id);

	ResponseBase findById(Integer id);

}
