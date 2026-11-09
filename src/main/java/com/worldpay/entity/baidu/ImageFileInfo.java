package com.worldpay.entity.baidu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "图片文件信息")
public class ImageFileInfo {
	
	 @Schema(description = "文件序号")
	 private final int index;
     @Schema(description = "文件路径")
     private final String path;
     
     public ImageFileInfo(int index, String path) {
         this.index = index;
         this.path = path;
     }

}
