package com.worldpay.dao.wallet;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.wallet.WalletToWebRatesEntity;

@Repository
public interface WalletToWebRatesDao extends BaseMapper<WalletToWebRatesEntity>{

	@Select("select * from wallet_to_web_rates limit 1")
	WalletToWebRatesEntity findRates();

}
