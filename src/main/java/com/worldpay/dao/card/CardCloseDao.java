package com.worldpay.dao.card;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardCloseEntity;

@Repository
public interface CardCloseDao extends BaseMapper<CardCloseEntity>{

	@Select("select * from card_close where request_order_id = #{requestOrderId}")
	CardCloseEntity findOrderId(@Param("requestOrderId") String requestOrderId);

	@Select("<script>"
			+ "select * from card_close where 1=1 "
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardType != null'> and card_type = #{cardType}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'requestOrderId != null'> and request_order_id = #{requestOrderId}</if>"
			+ "<if test = 'reviewStatus != null'> and review_status = #{reviewStatus}</if>"
			+ " order by setTime desc"
			+ "</script>")
	List<CardCloseEntity> findList(CardCloseEntity entity);

	@Select("select * from card_close where user_bankcard_id = #{userBankcardId} limit 1")
	CardCloseEntity findUserBankcardId(@Param("userBankcardId") Integer userBankcardId);

	@Select("select * from card_close where user_bankcard_id = #{userBankcardId}")
	CardCloseEntity findUserBankCardId(@Param("userBankcardId") Integer userBankcardId);

//	@Select("select * from card_close where card_no = #{cardNo} limit 1")
//	CardCloseEntity findCardNo(@Param("cardNo") String cardNo);

}
