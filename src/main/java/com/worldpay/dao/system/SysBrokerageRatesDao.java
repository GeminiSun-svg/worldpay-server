package com.worldpay.dao.system;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SysBrokerageRatesEntity;

@Repository
public interface SysBrokerageRatesDao extends BaseMapper<SysBrokerageRatesEntity>{

	@Select("select * from sys_brokerage_rates limit 1")
	SysBrokerageRatesEntity findOne();

}
