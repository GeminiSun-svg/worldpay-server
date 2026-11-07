package com.worldpay.dao.card;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserCardOpenEntity;
import com.worldpay.query.PublicQueryEntity;

@Repository
public interface AppUserCardOpenDao extends BaseMapper<AppUserCardOpenEntity>{

	@Select("select * from app_user_card_open where uid = #{uid} and user_bankcard_id = #{userBankcardId} and open_state = #{openState} limit 1")
	AppUserCardOpenEntity findState(@Param("uid") String uid,@Param("userBankcardId") Integer userBankcardId,@Param("openState") Integer openState);

	@Select("select * from app_user_card_open where order_num = #{orderId}")
	AppUserCardOpenEntity findOrderNum(@Param("orderId") String orderId);

//	@Select("select ifnull(sum(open_card_amount),0) from app_user_card_open where open_state = #{openState}")
//	BigDecimal findSum(@Param("openState") Integer openState);

	@Select("select ifnull(sum(open_card_amount),0) from app_user_card_open where open_state = #{openState} "
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	BigDecimal findSum(PublicQueryEntity entity);

	@Select("<script>"
			+ "select ifnull(sum(open_card_amount),0) from app_user_card_open where open_state = #{openState} "
			+ "and date_format(setTime, '%Y-%m-%d') &gt;= #{begTime} and date_format(setTime, '%Y-%m-%d') &lt;= #{endTime}"
			+ "<if test = 'uid != null'>"
			+ " and uid in "
			+ "<foreach collection='uid' item='id' open='(' separator=',' close=')'>"
			+ " #{id}"
			+ "</foreach>"
			+ "</if>"
			+ "</script>")
	BigDecimal findChannelOpenCardFee(PublicQueryEntity entity);

}
