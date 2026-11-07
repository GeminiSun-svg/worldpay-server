package com.worldpay.dao.card;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardEntity;

@Repository
public interface CardDao extends BaseMapper<CardEntity>{

	@Select("select * from card_data where id = #{id} limit 1")
	CardEntity findById(@Param("id") Integer id);
	@Select("<script>"
			+ "select * from card_data where 1=1 "
			+ "<if test = 'bankCardNature != null'> and bankCardNature = #{bankCardNature}</if>"
			+ "<if test = 'bankCardSource != null'> and bankCardSource = #{bankCardSource}</if>"
			+ "<if test = 'bankCardType != null'> and bankCardType = #{bankCardType}</if>"
			+ "<if test = 'categoryId != null'> and categoryId = #{categoryId}</if>"
			+ "<if test = 'ccy != null'> and ccy = #{ccy}</if>"
			+ "<if test = 'enable != null'> and enable = #{enable}</if>"
			+ "<if test = 'cardState != null'> and card_state = #{cardState}</if>"
			+ "order by card_state"
			+ "</script>")
	List<CardEntity> findList(CardEntity entity);

	@Select("select * from card_data where uuid = #{uuid}")
	CardEntity findByuuId(@Param("uuid") String uuid);

	@Update("update card_data set img = #{img} where uuid = #{uuid}")
	void updateByImage(CardEntity cardEntity);

	@Update("update card_data set list_img = #{listImg} where uuid = #{uuid}")
	void updateByImageList(CardEntity cardEntity);
	
	@Update("update card_data set card_state = #{cardState} where uuid = #{uuid}")
	void updateState(@Param("uuid") String uuid,@Param("cardState") Integer cardState);

}
