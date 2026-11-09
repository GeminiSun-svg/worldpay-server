package com.worldpay.entity.baidu;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份证识别结果
 */
@Schema(description = "身份证识别结果")
public class IdCardResult {
    
    @JsonProperty("log_id")
    @Schema(description = "请求日志ID")
    private Long logId;
    
    @JsonProperty("image_status")
    @Schema(description = "图像状态")
    private String imageStatus; // normal-正常，reversed_side-身份证正反面颠倒
    
    @JsonProperty("idcard_number_type")
    @Schema(description = "证件号码类型")
    private Integer idcardNumberType; // 身份证号码类型：0-18位身份证，1-15位身份证
    
    @JsonProperty("words_result_num")
    @Schema(description = "识别结果数量")
    private Integer wordsResultNum;
    
    @JsonProperty("words_result")
    @Schema(description = "识别结果")
    private Map<String, IdCardField> wordsResult;
    
    // 风险类型（如果开启了detect_risk）
    @JsonProperty("risk_type")
    @Schema(description = "风险类型")
    private String riskType;
    
    // 方向（如果开启了detect_direction）
    @Schema(description = "图像方向")
    private Integer direction;
    
    // 卡片质量（如果开启了detect_quality）
    @JsonProperty("card_quality")
    @Schema(description = "卡片质量")
    private Map<String, Object> cardQuality;
    
    // Getter和Setter方法
    public Long getLogId() {
        return logId;
    }
    
    public void setLogId(Long logId) {
        this.logId = logId;
    }
    
    public String getImageStatus() {
        return imageStatus;
    }
    
    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }
    
    public Integer getIdcardNumberType() {
        return idcardNumberType;
    }
    
    public void setIdcardNumberType(Integer idcardNumberType) {
        this.idcardNumberType = idcardNumberType;
    }
    
    public Integer getWordsResultNum() {
        return wordsResultNum;
    }
    
    public void setWordsResultNum(Integer wordsResultNum) {
        this.wordsResultNum = wordsResultNum;
    }
    
    public Map<String, IdCardField> getWordsResult() {
        return wordsResult;
    }
    
    public void setWordsResult(Map<String, IdCardField> wordsResult) {
        this.wordsResult = wordsResult;
    }
    
    public String getRiskType() {
        return riskType;
    }
    
    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }
    
    public Integer getDirection() {
        return direction;
    }
    
    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    
    public Map<String, Object> getCardQuality() {
        return cardQuality;
    }
    
    public void setCardQuality(Map<String, Object> cardQuality) {
        this.cardQuality = cardQuality;
    }
    
    // 便捷方法：获取姓名
    public String getName() {
        if (wordsResult != null && wordsResult.containsKey("姓名")) {
            return wordsResult.get("姓名").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取身份证号
    public String getIdNumber() {
        if (wordsResult != null && wordsResult.containsKey("公民身份号码")) {
            return wordsResult.get("公民身份号码").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取性别
    public String getGender() {
        if (wordsResult != null && wordsResult.containsKey("性别")) {
            return wordsResult.get("性别").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取民族
    public String getNation() {
        if (wordsResult != null && wordsResult.containsKey("民族")) {
            return wordsResult.get("民族").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取出生日期
    public String getBirthday() {
        if (wordsResult != null && wordsResult.containsKey("出生")) {
            return wordsResult.get("出生").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取地址
    public String getAddress() {
        if (wordsResult != null && wordsResult.containsKey("住址")) {
            return wordsResult.get("住址").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取签发机关（反面）
    public String getIssuingAuthority() {
        if (wordsResult != null && wordsResult.containsKey("签发机关")) {
            return wordsResult.get("签发机关").getWords();
        }
        return null;
    }
    
    // 便捷方法：获取有效期（反面）
    public String getValidityPeriod() {
        if (wordsResult != null && wordsResult.containsKey("有效期限")) {
            return wordsResult.get("有效期限").getWords();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "IdCardResult{" +
                "logId=" + logId +
                ", imageStatus='" + imageStatus + '\'' +
                ", name='" + getName() + '\'' +
                ", idNumber='" + getIdNumber() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", nation='" + getNation() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", riskType='" + riskType + '\'' +
                '}';
    }
    
    /**
     * 身份证字段信息
     */
    public static class IdCardField {
        
        @Schema(description = "字段内容")
        private String words;  // 字段内容
        
        @Schema(description = "位置信息")
        private Location location;  // 位置信息
        
        public String getWords() {
            return words;
        }
        
        public void setWords(String words) {
            this.words = words;
        }
        
        public Location getLocation() {
            return location;
        }
        
        public void setLocation(Location location) {
            this.location = location;
        }
        
        @Override
        public String toString() {
            return "IdCardField{" +
                    "words='" + words + '\'' +
                    ", location=" + location +
                    '}';
        }
    }
    
    /**
     * 位置信息
     */
    public static class Location {
        
        @Schema(description = "左坐标")
        private Integer left;   // 左坐标
        @Schema(description = "上坐标")
        private Integer top;    // 上坐标
        @Schema(description = "宽度")
        private Integer width;  // 宽度
        @Schema(description = "高度")
        private Integer height; // 高度
        
        public Integer getLeft() {
            return left;
        }
        
        public void setLeft(Integer left) {
            this.left = left;
        }
        
        public Integer getTop() {
            return top;
        }
        
        public void setTop(Integer top) {
            this.top = top;
        }
        
        public Integer getWidth() {
            return width;
        }
        
        public void setWidth(Integer width) {
            this.width = width;
        }
        
        public Integer getHeight() {
            return height;
        }
        
        public void setHeight(Integer height) {
            this.height = height;
        }
        
        @Override
        public String toString() {
            return "Location{" +
                    "left=" + left +
                    ", top=" + top +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}