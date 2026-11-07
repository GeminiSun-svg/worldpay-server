package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardApplyManEntity;

@Repository
public interface CardApplyManDao extends BaseMapper<CardApplyManEntity>{

	@Select("select * from card_apply_man where apply_id = #{applyId}")
	CardApplyManEntity findByApplyId(@Param("applyId") Integer applyId);

}
