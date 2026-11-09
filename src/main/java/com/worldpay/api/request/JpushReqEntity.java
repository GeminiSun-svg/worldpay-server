package com.worldpay.api.request;

import java.util.List;
import java.util.Map;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "极光推送请求")
public class JpushReqEntity {
	
	/** 标题 */
    @Schema(description = "推送消息标题")
    private String title;
    
    /** 消息体 */
    @Schema(description = "推送消息内容")
    private String msg;
    
    /** 是否广播 true-是 false-否 */
    @Schema(description = "是否广播推送")
    private boolean broadcasting;
    
    /** 别名（用户ID），一次最多1000个 */
    @Schema(description = "别名（用户ID）列表")
    private List<String> aliasList;
    
    /** 注册ID（设备ID），一次最多1000个，优先于别名推送 */
    @Schema(description = "设备注册ID列表")
    private List<String> registrationIdList;
    
    /** 附加参数 */
    @Schema(description = "推送附加参数")
    private Map<String, Object> extrasMap;

}
