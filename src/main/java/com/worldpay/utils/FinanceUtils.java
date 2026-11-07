package com.worldpay.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinanceUtils {

	/**
	 * @param principal 本金
	 * @param annualRate 年利率
	 * @param days 持有天数
	 * @return
	 */
//	public static double calculateProfit(double principal, double annualRate, int days) {
//		if (principal < 0 || annualRate < 0 || days < 0) {
//			throw new IllegalArgumentException("参数不能为负数");
//		}
//		// 日利率 = 年利率 / 365
//		double dailyRate = annualRate / 365.0;
//		return principal * dailyRate * days;
//	}
	
	public static double calculateProfit(double principal, double annualRate, int days) {
	    if (principal < 0 || annualRate < 0 || days < 0) {
	        throw new IllegalArgumentException("参数不能为负数");
	    }
	    double dailyRate = annualRate / 365.0;
	    double result = principal * dailyRate * days;

	    // 保留 6 位小数，四舍五入
	    return new BigDecimal(result)
	            .setScale(6, RoundingMode.HALF_UP)
	            .doubleValue();
	}

	public static void main(String[] args) {
		double principal = 1000.0;
		double annualRate = 0.05; // 5%
		int days = 30;

		double profit = calculateProfit(principal, annualRate, days);
		System.out.printf("本金 %.2f 元，年利率 %.2f%%，投资 %d 天，收益约为：%.2f 元%n", principal, annualRate * 100, days, profit);
	}

}
