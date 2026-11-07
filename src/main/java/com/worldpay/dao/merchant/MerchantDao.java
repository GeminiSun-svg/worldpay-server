package com.worldpay.dao.merchant;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.merchant.MerchantEntity;

@Repository
public interface MerchantDao extends BaseMapper<MerchantEntity>{

}
