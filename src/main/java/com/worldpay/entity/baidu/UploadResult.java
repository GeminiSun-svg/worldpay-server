package com.worldpay.entity.baidu;

import com.worldpay.entity.appuser.AppUserFileEntity;

import lombok.Data;

@Data
public class UploadResult {
	
	 private boolean success;
     private AppUserFileEntity frontEntity;
     private AppUserFileEntity backEntity;
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
