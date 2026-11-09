package com.worldpay.entity.baidu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "文件信息")
public class FileInfo {
	
	@Schema(description = "文件序号")
	private int index;
	
    @Schema(description = "文件路径")
    private String path;

}
