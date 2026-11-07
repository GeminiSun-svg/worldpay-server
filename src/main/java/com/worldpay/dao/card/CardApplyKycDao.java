package com.worldpay.dao.card;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardApplyKycEntity;

@Repository
public interface CardApplyKycDao extends BaseMapper<CardApplyKycEntity>{

	@Select("select * from card_apply_kyc where apply_id = #{applyId}")
	CardApplyKycEntity findByApplyId(@Param("applyId") Integer applyId);

	@Select("SELECT * FROM card_apply_kyc " +
	        "WHERE uid = #{uid} " +
	        "AND paperwork_type IS NOT NULL " +
	        "AND TRIM(paperwork_type) <> '' " +
	        "LIMIT 1")
	CardApplyKycEntity findUid(@Param("uid")String uid);

}
