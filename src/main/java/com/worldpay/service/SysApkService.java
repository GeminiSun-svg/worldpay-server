package com.worldpay.service;
import org.springframework.web.multipart.MultipartFile;

import com.worldpay.base.ResponseBase;
import com.worldpay.entity.system.SysApkEntity;

import io.swagger.v3.oas.annotations.Operation;

public interface SysApkService {

	ResponseBase add(String dataStr,MultipartFile file);

	ResponseBase update(String dataStr,MultipartFile file);

	ResponseBase findList(SysApkEntity entity);

	ResponseBase release(Integer id);

	ResponseBase findLastVersion();

}
