package com.worldpay.api.response;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "图片上传响应")
public class UploadImageApiEntity {

	@Schema(description = "证件文件ID")
	private String documentFileId;

	@Schema(description = "证件文件地址")
	private String documentFileUrl;

	@Schema(description = "文件地址")
	private String fileUrl;

}
