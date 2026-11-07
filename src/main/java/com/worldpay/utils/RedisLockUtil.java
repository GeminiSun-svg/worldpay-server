package com.worldpay.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockUtil {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final String LOCK_PREFIX = "lock:wallet:";
	private static final long LOCK_EXPIRE = 30000; // 30秒

	/**
	 * 获取分布式锁
	 */
	public boolean tryLock(String key, String value) {
		return redisTemplate.opsForValue().setIfAbsent(LOCK_PREFIX + key, value, LOCK_EXPIRE, TimeUnit.MILLISECONDS);
	}

	/**
	 * 释放锁
	 */
	public void unlock(String key, String value) {
		String currentValue = redisTemplate.opsForValue().get(LOCK_PREFIX + key);
		if (value.equals(currentValue)) {
			redisTemplate.delete(LOCK_PREFIX + key);
		}
	}

	/**
	 * 预扣款到Redis
	 */
	public boolean preDeductBalance(String uid, Double amount) {
		String key = "pre_deduct:" + uid;
		// 使用原子操作确保并发安全
		return redisTemplate.opsForValue().increment(key, amount * -1) >= 0;
	}

	/**
	 * 获取预扣款金额
	 */
	public Double getPreDeductAmount(String uid) {
		String key = "pre_deduct:" + uid;
		String value = redisTemplate.opsForValue().get(key);
		return value != null ? Double.parseDouble(value) : 0;
	}
}
