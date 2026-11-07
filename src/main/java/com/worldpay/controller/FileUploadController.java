package com.worldpay.controller;

import com.worldpay.base.ResponseBase;
import com.worldpay.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/card/api/file")
@Tag(name = "FileUpload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload-image")
    @Operation(summary = "上传图片")
    public ResponseBase uploadImage(MultipartFile file) {
        return fileUploadService.uploadImage(file);
    }

    @PostMapping("/api-upload-image")
    @Operation(summary = "上传图片（API）")
    public ResponseBase apiUploadImage(String uid,Integer type,MultipartFile file) {
        return fileUploadService.apiUploadImage(uid, type, file);
    }
}
