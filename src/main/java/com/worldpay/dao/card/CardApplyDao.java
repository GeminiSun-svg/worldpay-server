package com.worldpay.dao.card;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardApplyEntity;
import com.worldpay.query.PublicQueryEntity;

@Repository
public interface CardApplyDao extends BaseMapper<CardApplyEntity>{

	@Select("<script>"
			+ "select * from card_apply where 1=1 "
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardId != null'> and card_id = #{cardId}</if>"
			+ "<if test = 'cardType != null'> and card_type = #{cardType}</if>"
			+ "<if test = 'applyState != null'> and apply_state = #{applyState}</if>"
			+ "<if test = 'topupType != null'> and topup_type = #{topupType}</if>"
			+ "<if test = 'kycState != null'> and kyc_state = #{kycState}</if>"
			+ "order by setTime desc"
			+ "</script>")
	List<CardApplyEntity> findList(CardApplyEntity entity);

	@Select("<script>"+
			"select count(*) from card_apply where 1=1"+
			"<if test='begTime != null and begTime != \"\" and endTime != null and endTime != \"\"'>" +
			"   AND gmtModified BETWEEN #{begTime} AND #{endTime} " +
			"</if>" +
			"</script>")
	int findCount(  @Param("begTime") String begTime,
					@Param("endTime") String endTime
	);

	@Select("select * from card_apply where kyc_state = #{kycState}")
	List<CardApplyEntity> findKyc(@Param("kycState") Integer kycState);

	@Select("select * from card_apply where logistics_num = #{logisticsNum}")
	List<CardApplyEntity> findLogisticsNum(@Param("logisticsNum") String logisticsNum);

	@Select("select IFNULL(SUM(open_card_cost),0) as openCardFee from card_apply where apply_state = #{statusId} "
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	BigDecimal findOpenCardFee(PublicQueryEntity entity);

	@Select("select ifnull(count(*),0) from card_apply where card_type=#{type} "
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	Integer findOpenCardNum(PublicQueryEntity entity);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(SUM(open_card_cost), 0) AS openCardFee",
			"FROM card_apply",
			"WHERE apply_state = #{statusId}",
			"  AND uid IN",
			"]]>",
			"  <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
			"    #{id}",
			"  </foreach>",
			"<![CDATA[",
			"  AND DATE_FORMAT(setTime, '%Y-%m-%d') >= #{begTime}",
			"  AND DATE_FORMAT(setTime, '%Y-%m-%d') <= #{endTime}",
			"]]>",
			"</script>"
	})
    BigDecimal findChannelOpenCardFee(PublicQueryEntity entity);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(COUNT(*), 0)",
			"FROM card_apply",
			"WHERE card_type = #{type}",
			"  AND uid IN",
			"]]>",
			"  <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
			"    #{id}",
			"  </foreach>",
			"<![CDATA[",
			"  AND DATE_FORMAT(setTime, '%Y-%m-%d') >= #{begTime}",
			"  AND DATE_FORMAT(setTime, '%Y-%m-%d') <= #{endTime}",
			"]]>",
			"</script>"
	})
    Integer findChannelOpenCardNum(PublicQueryEntity entity);

	@Select({
			"<script>",
			"SELECT IFNULL(COUNT(*), 0)",
			"FROM card_apply",
			"WHERE card_type = #{type}",
			"  AND uid IN ",
			"  <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
			"    #{id}",
			"  </foreach>",
			"</script>"
	})
	Integer findChannelOpenCardNumNoTime(PublicQueryEntity publicQueryEntity);
}
