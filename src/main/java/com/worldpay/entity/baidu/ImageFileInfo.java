package com.worldpay.entity.baidu;

import lombok.Data;

@Data
public class ImageFileInfo {
	
	 private final int index;
     private final String path;
     
     public ImageFileInfo(int index, String path) {
         this.index = index;
         this.path = path;
     }

}
