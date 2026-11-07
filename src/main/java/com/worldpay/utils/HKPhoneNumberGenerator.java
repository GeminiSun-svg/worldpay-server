package com.worldpay.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.worldpay.entity.baidu.KycDefaultData;

/**
 * 香港手机号生成工具类
 * 香港手机号规则：
 * 1. 共8位数字
 * 2. 以5、6、9开头
 * 3. 第二位不能是0（部分运营商规则）
 * 
 * @author yourname
 * @date 2026-04-27
 */
public class HKPhoneNumberGenerator {
    
    private static final Random RANDOM = new Random();
    
    // 香港手机号有效首位数字
    private static final int[] VALID_FIRST_DIGITS = {5, 6, 9};
    
    // 第二位数字（避免过于特殊的号码，可选限制）
    private static final int[] VALID_SECOND_DIGITS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    
    /**
     * 生成单个随机香港手机号
     * @return 8位手机号（不加区号）
     */
    public static String generate() {
        StringBuilder phone = new StringBuilder();
        
        // 第一位：5、6、9中随机选一个
        phone.append(VALID_FIRST_DIGITS[RANDOM.nextInt(VALID_FIRST_DIGITS.length)]);
        
        // 第二位：0-9随机
        phone.append(VALID_SECOND_DIGITS[RANDOM.nextInt(VALID_SECOND_DIGITS.length)]);
        
        // 剩余6位：0-9随机
        for (int i = 0; i < 6; i++) {
            phone.append(RANDOM.nextInt(10));
        }
        
        String num = phone.toString();
        
        // 校验，如果不符合则重新生成
        if (PhoneValidator.isValidMobile(num, KycDefaultData.COUNTRY_CODE)) {
            return num;
        }
        
        // 递归重新生成
        return generate();
    }
    
    /**
     * 生成带香港区号的手机号
     * @return 格式：+852XXXXXXXX
     */
    public static String generateWithAreaCode() {
        return "+852" + generate();
    }
    
    /**
     * 生成带国际区号格式的手机号（00852）
     * @return 格式：00852XXXXXXXX
     */
    public static String generateWithInternationalCode() {
        return "00852" + generate();
    }
    
    /**
     * 生成格式化的手机号（带空格）
     * @return 格式：XXXX XXXX
     */
    public static String generateFormatted() {
        String phone = generate();
        return phone.substring(0, 4) + " " + phone.substring(4);
    }
    
    /**
     * 批量生成手机号
     * @param count 生成数量
     * @return 手机号列表
     */
    public static List<String> generateBatch(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }
        
        List<String> phones = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            phones.add(generate());
        }
        return phones;
    }
    
    /**
     * 批量生成去重的手机号
     * @param count 生成数量
     * @return 去重后的手机号列表
     */
    public static List<String> generateBatchDistinct(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("生成数量必须大于0");
        }
        if (count > 80000000) {
            throw new IllegalArgumentException("生成数量不能超过8千万（香港手机号总数上限）");
        }
        
        List<String> phones = new ArrayList<>(count);
        java.util.HashSet<String> set = new java.util.HashSet<>();
        
        while (set.size() < count) {
            set.add(generate());
        }
        
        phones.addAll(set);
        return phones;
    }
    
    /**
     * 验证手机号是否合法
     * @param phone 手机号（8位数字）
     * @return true=合法，false=不合法
     */
    public static boolean isValid(String phone) {
        if (phone == null || phone.length() != 8) {
            return false;
        }
        
        // 检查是否全是数字
        if (!phone.matches("\\d{8}")) {
            return false;
        }
        
        // 检查首位是否为5、6、9
        char firstChar = phone.charAt(0);
        if (firstChar != '5' && firstChar != '6' && firstChar != '9') {
            return false;
        }
        
        return true;
    }
    
    /**
     * 生成指定前缀的手机号
     * @param prefix 前缀（1-7位数字）
     * @return 完整的8位手机号
     */
    public static String generateWithPrefix(String prefix) {
        if (prefix == null || prefix.length() >= 8) {
            throw new IllegalArgumentException("前缀长度必须小于8位");
        }
        
        if (!prefix.matches("\\d+")) {
            throw new IllegalArgumentException("前缀只能包含数字");
        }
        
        // 验证第一位是否符合规则
        if (prefix.length() > 0) {
            char firstDigit = prefix.charAt(0);
            if (firstDigit != '5' && firstDigit != '6' && firstDigit != '9') {
                throw new IllegalArgumentException("手机号首位必须是5、6或9");
            }
        }
        
        StringBuilder phone = new StringBuilder(prefix);
        int remainingLength = 8 - prefix.length();
        
        for (int i = 0; i < remainingLength; i++) {
            phone.append(RANDOM.nextInt(10));
        }
        
        return phone.toString();
    }
    
    /**
     * 使用示例
     */
    public static void main(String[] args) {
        // 测试生成
        System.out.println("========== 香港手机号生成测试 ==========");
        
        // 1. 生成单个手机号
        System.out.println("普通手机号: " + generate());
        
        // 2. 生成带区号的
        System.out.println("带区号(+852): " + generateWithAreaCode());
        System.out.println("带区号(00852): " + generateWithInternationalCode());
        
        // 3. 生成格式化
        System.out.println("格式化: " + generateFormatted());
        
        // 4. 批量生成10个
        System.out.println("\n批量生成10个手机号:");
        List<String> phones = generateBatch(10);
        for (int i = 0; i < phones.size(); i++) {
            System.out.printf("  %d. %s %s%n", i + 1, phones.get(i), 
                isValid(phones.get(i)) ? "✓" : "✗");
        }
        
        // 5. 生成指定前缀
        System.out.println("\n指定前缀'5'开头: " + generateWithPrefix("5"));
        System.out.println("指定前缀'512'开头: " + generateWithPrefix("512"));
        
        // 6. 验证功能
        System.out.println("\n验证测试:");
        System.out.println("51234567 是否合法? " + isValid("51234567"));
        System.out.println("41234567 是否合法? " + isValid("41234567"));
        System.out.println("1234567 是否合法? " + isValid("1234567"));
        
        // 7. 批量生成去重的100个
        System.out.println("\n生成100个不重复的手机号:");
        List<String> distinctPhones = generateBatchDistinct(100);
        System.out.println("实际生成数量: " + distinctPhones.size());
        System.out.println("是否重复: " + (distinctPhones.size() != new java.util.HashSet<>(distinctPhones).size()));
    }
}