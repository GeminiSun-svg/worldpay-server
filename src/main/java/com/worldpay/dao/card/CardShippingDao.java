package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardShippingEntity;

@Repository
public interface CardShippingDao extends BaseMapper<CardShippingEntity>{

//	@Select("select * from card_shipping where logistics_num = #{logisticsNum}")
//	CardShippingEntity findLogisticsNum(@Param("logisticsNum") String logisticsNum);

	@Select("select * from card_shipping where apply_id = #{applyId}")
	CardShippingEntity findByApplyId(@Param("applyId") Integer applyId);

}
