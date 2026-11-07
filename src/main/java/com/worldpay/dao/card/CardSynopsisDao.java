package com.worldpay.dao.card;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardSynopsisEntity;

@Repository
public interface CardSynopsisDao extends BaseMapper<CardSynopsisEntity>{

	@Select("select * from card_synopsis where id in "
			+ "(select synopsis_id from card_synopsis_join "
			+ "where card_id = #{cardId} and language = #{language}) limit 1")
	CardSynopsisEntity findByCardId(@Param("cardId") String cardId,@Param("language") String language);

	@Select("<script>"
			+ "select * from card_synopsis where 1=1"
			+ "<if test = 'title != null'> and title like  like '%${title}%'</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ " order by language"
			+ "</script>")
	List<CardSynopsisEntity> findList(CardSynopsisEntity entity);

	@Select("select id from card_synopsis where id in (select synopsis_id from card_synopsis_join where card_id = #{cardId})")
	List<Integer> queryByCardId(@Param("cardId") String cardId);


}
