package com.worldpay.dao.card;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardLableEntity;

@Repository
public interface CardLableDao extends BaseMapper<CardLableEntity>{

	@Select("select * from card_lable where id in (select lable_id from card_lable_join where card_id = #{id} and language = #{language})")
	List<CardLableEntity> findByCardId(@Param("id") String id,@Param("language") String language);

	@Select("select * from card_lable where name = #{name}")
	CardLableEntity findName(@Param("name") String name);

	@Select("<script>"
			+ "select * from card_lable where 1=1"
			+ "<if test = 'name != null'> and name = #{name}</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "</script>")
	List<CardLableEntity> findList(CardLableEntity entity);

	@Select("select id from card_lable where id in (select lable_id from card_lable_join where card_id = #{uuid})")
	List<Integer> queryByCardId(@Param("uuid") String uuid);




}
