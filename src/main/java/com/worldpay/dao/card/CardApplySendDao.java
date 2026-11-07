package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardApplySendEntity;

@Repository
public interface CardApplySendDao extends BaseMapper<CardApplySendEntity>{

	@Select("select * from card_apply_send where apply_id = #{applyId} ")
	CardApplySendEntity findByApplyId(@Param("applyId") Integer applyId);

}
