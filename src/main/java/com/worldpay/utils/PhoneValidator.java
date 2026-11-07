package com.worldpay.utils;import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;

public class PhoneValidator {
	
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    
    /**
     * 核心方法：校验手机号是否对应该国的有效手机号
     * 兼容 NANP 国家（美、加等）的 FIXED_LINE_OR_MOBILE 类型
     */
    public static boolean isValidMobile(String phoneNumber, String countryCode) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        try {
            PhoneNumber parsed = phoneUtil.parse(phoneNumber, countryCode);
            
            // 1. 必须是有效号码
            if (!phoneUtil.isValidNumber(parsed)) {
                return false;
            }
            
            // 2. 必须是手机类型或固话/手机混合类型
            PhoneNumberType type = phoneUtil.getNumberType(parsed);
            return type == PhoneNumberType.MOBILE ||
                   type == PhoneNumberType.FIXED_LINE_OR_MOBILE;
                   
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        // 测试
        System.out.println("中国 13800138000: " + isValidMobile("+8613800138000", "CN"));
        System.out.println("印度 9448376473: " + isValidMobile("+919448376473", "IN"));
        System.out.println("美国 2025550123: " + isValidMobile("+12025550123", "US"));
        System.out.println("美国 2025550173: " + isValidMobile("+12025550173", "US"));
        System.out.println("美国 6502530000: " + isValidMobile("+16502530000", "US"));
        // 土耳其手机号测试
        boolean test1 = isValidMobile("5311234567", "TR");  // 无空格
        boolean test2 = isValidMobile("+90 531 123 4567", "TR"); // 带空格
        boolean test3 = isValidMobile("1283459223", "TR");    // 本地格式（含0前缀）
        
        System.out.println("+905311234567: " + test1);
        System.out.println("+90 531 123 4567: " + test2);
        System.out.println("05311234567: " + test3);
    }
}