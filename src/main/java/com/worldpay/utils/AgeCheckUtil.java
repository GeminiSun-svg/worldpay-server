package com.worldpay.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeCheckUtil {
	
	 public static boolean isAdult(String birthdayStr) {
	        // 1. 定义日期格式
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        // 2. 解析字符串为 LocalDate
	        LocalDate birthday = LocalDate.parse(birthdayStr, formatter);

	        // 3. 获取当前日期
	        LocalDate today = LocalDate.now();

	        // 4. 计算年龄
	        Period period = Period.between(birthday, today);

	        // 5. 判断是否满18岁
	        return period.getYears() >= 18;
	    }
	 
	 public static void main(String[] args) {
		 String birthday = "2015-12-01";
	        if (isAdult(birthday)) {
	            System.out.println("已满18周岁 ✅");
	        } else {
	            System.out.println("未满18周岁 ❌");
	        }
	}


}
