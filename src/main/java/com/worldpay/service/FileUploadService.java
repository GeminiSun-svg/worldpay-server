package com.worldpay.service;

import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.ResponseBase;

import io.swagger.v3.oas.annotations.Operation;

public interface FileUploadService {

ResponseBase uploadImage(MultipartFile file);

ResponseBase apiUploadImage(String uid,Integer type,MultipartFile file);

}
