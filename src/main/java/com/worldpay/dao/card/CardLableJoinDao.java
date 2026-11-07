package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardLableJoinEntity;

@Repository
public interface CardLableJoinDao extends BaseMapper<CardLableJoinEntity>{

	@Delete("delete from card_lable_join where card_id = #{cardId}")
	void deleteByCardId(@Param("cardId") String cardId);

	@Delete("delete from card_lable_join where lable_id = #{lableId}")
	void deleteByLableId(@Param("lableId") Integer lableId);

}
