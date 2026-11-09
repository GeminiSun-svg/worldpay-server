package com.worldpay.entity.baidu;

import com.worldpay.entity.appuser.AppUserFileEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "KYC文件上传结果")
public class UploadResult {
	
	 @Schema(description = "是否成功")
	 private boolean success;
     @Schema(description = "正面照文件")
     private AppUserFileEntity frontEntity;
     @Schema(description = "反面照文件")
     private AppUserFileEntity backEntity;
     @Schema(description = "手持照文件")
     private AppUserFileEntity handheldEntity;
     
     public static UploadResult success(AppUserFileEntity front, AppUserFileEntity back, AppUserFileEntity handheld) {
         UploadResult result = new UploadResult();
         result.success = true;
         result.frontEntity = front;
         result.backEntity = back;
         result.handheldEntity = handheld;
         return result;
     }
     
     public static UploadResult failed() {
         UploadResult result = new UploadResult();
         result.success = false;
         return result;
     }
     
     public boolean isSuccess() {
         return success;
     }

}
