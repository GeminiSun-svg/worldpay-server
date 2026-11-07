package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardSynopsisJoinEntity;

@Repository
public interface CardSynopsisJoinDao extends BaseMapper<CardSynopsisJoinEntity>{

	@Delete("delete from card_synopsis_join where card_id = #{cardId}")
	void deleteByCardId(@Param("cardId") String cardId);

	@Delete("delete from card_synopsis_join where synopsis_id = #{synopsisId}")
	void deleteBySynopsisId(@Param("synopsisId") Integer synopsisId);

}
