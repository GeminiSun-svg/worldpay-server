package com.worldpay.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.worldpay.dao.system.KycFileIndexDao;
import com.worldpay.entity.baidu.KycFileIndexEntity;

@Service
@Transactional
public class KycFileIndexServiceImpl {

	@Autowired
	private KycFileIndexDao kycFileIndexDao;

	private static final String IDCARD_INDEX_KEY = "idcard_current_index";

	private static final Integer DEFAULT_INDEX = 100;

	/**
	 * 获取身份证识别索引
	 */
	public int getIdCardIndex() {
		LambdaQueryWrapper<KycFileIndexEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(KycFileIndexEntity::getConfigKey, IDCARD_INDEX_KEY);
		KycFileIndexEntity config = kycFileIndexDao.selectOne(wrapper);
		if (config == null) {
			// 如果没有配置，初始化默认值
			return initDefaultIndex();
		}

		try {
			return config.getConfigValue();
		} catch (NumberFormatException e) {
			return DEFAULT_INDEX;
		}
	}

	/**
	 * 更新身份证识别索引
	 */
	@Transactional
	public void updateIdCardIndex(int index) {
		LambdaQueryWrapper<KycFileIndexEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(KycFileIndexEntity::getConfigKey, IDCARD_INDEX_KEY);
		KycFileIndexEntity config = kycFileIndexDao.selectOne(wrapper);

		if (config == null) {
			// 不存在则插入
			config = new KycFileIndexEntity();
			config.setConfigKey(IDCARD_INDEX_KEY);
			config.setConfigValue(index);
			config.setConfigInfo("身份证识别当前索引");
			kycFileIndexDao.insert(config);
		} else {
			// 存在则更新
			config.setConfigValue(index);
			kycFileIndexDao.updateById(config);
		}
	}

	/**
	 * 初始化默认索引
	 */
	private int initDefaultIndex() {
		KycFileIndexEntity config = new KycFileIndexEntity();
		config.setConfigKey(IDCARD_INDEX_KEY);
		config.setConfigValue(DEFAULT_INDEX);
		config.setConfigInfo("身份证识别当前索引");
		kycFileIndexDao.insert(config);
		return DEFAULT_INDEX;
	}
}
