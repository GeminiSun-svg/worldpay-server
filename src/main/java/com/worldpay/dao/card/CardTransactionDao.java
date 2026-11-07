package com.worldpay.dao.card;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardTransactionEntity;
import com.worldpay.entity.response.AdminStatEntity;
import com.worldpay.query.PublicQueryEntity;

@Repository
public interface CardTransactionDao extends BaseMapper<CardTransactionEntity>{

	@Select("select * from card_transaction where request_orderid = #{requestOrderId}")
	CardTransactionEntity findOrderNum(@Param("requestOrderId") String requestOrderId);

	@Select("select * from card_transaction where user_bankcard_id = #{userBankcardId}")
	List<CardTransactionEntity> findUserBankcardId(@Param("userBankcardId") Integer userBankcardId);

	@Select("<script>"
			+ "select * from card_transaction where 1=1"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardId != null'> and card_id = #{cardId}</if>"
			+ "<if test = 'userBankcardId != null'> and user_bankcard_id = #{userBankcardId}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'transType != null'> and trans_type = #{transType}</if>"
			+ "<if test = 'requestOrderId != null'> and request_orderid = #{requestOrderId}</if>"
			+ "<if test = 'orderState != null'> and order_state = #{orderState}</if>"
			+ "<if test = 'userEmail != null'> and uid in(select uid from app_user where user_email = #{userEmail})</if>"
			+ " order by setTime desc"
			+ "</script>")
	List<CardTransactionEntity> findList(CardTransactionEntity entity);

	@Select("select * from card_transaction where order_state = #{orderState}")
	List<CardTransactionEntity> findState(@Param("orderState") String orderState);

	@Select("select ifnull(SUM(local_currency_amt),0) as cardTopupTotal, ifnull(SUM(handling_fees),0) as cardTopupFee  "
			+ "from card_transaction where trans_type =#{type} and order_state = #{status} "
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	AdminStatEntity findCardTopup(PublicQueryEntity entity);

	@Select("select IFNULL(SUM(local_currency_amt),0) as cardConsumeTotal, IFNULL(SUM(handling_fees),0) as cardConsumeFee"
			+ " from card_transaction where trans_type  != #{type} "
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	AdminStatEntity findCardConsume(PublicQueryEntity entity);


	@Select("select ifnull(SUM(local_currency_amt),0) as cardTopupTotal, ifnull(SUM(handling_fees),0) as cardTopupFee  "
			+ "from card_transaction where trans_type =#{type} and order_state = #{status} "
			+ "and handling_fees != CAST(local_currency_amt AS DECIMAL(18,6))"
			+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	AdminStatEntity findFees(PublicQueryEntity entity);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(SUM(local_currency_amt), 0) AS cardTopupTotal,",
			"       IFNULL(SUM(handling_fees), 0) AS cardTopupFee",
			"FROM card_transaction",
			"WHERE trans_type = #{type}",
			"  AND order_state = #{status}",
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
	AdminStatEntity findCardChannelTopup(PublicQueryEntity entity);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(SUM(local_currency_amt), 0) AS cardTopupTotal,",
			"       IFNULL(SUM(handling_fees), 0) AS cardTopupFee",
			"FROM card_transaction",
			"WHERE trans_type = #{type}",
			"  AND order_state = #{status}",
			"  AND handling_fees != CAST(local_currency_amt AS DECIMAL(18,6))",
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
	AdminStatEntity findChannelFees(PublicQueryEntity entity);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(SUM(local_currency_amt), 0) AS cardConsumeTotal,",
			"       IFNULL(SUM(handling_fees), 0) AS cardConsumeFee",
			"FROM card_transaction",
			"WHERE trans_type != #{type}",
			"  AND order_state = #{status}",
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
	AdminStatEntity findChannelCardConsume(PublicQueryEntity entity);

	@Select("<script>"
			+ "select * from card_transaction where trans_type != 'topup' "
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardId != null'> and card_id = #{cardId}</if>"
			+ "<if test = 'userBankcardId != null'> and user_bankcard_id = #{userBankcardId}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'requestOrderId != null'> and request_orderid = #{requestOrderId}</if>"
			+ "<if test = 'orderState != null'> and order_state = #{orderState}</if>"
			+ "<if test = 'userEmail != null'> and uid in(select uid from app_user where user_email = #{userEmail})</if>"
			+ " order by setTime desc"
			+ "</script>")
	List<CardTransactionEntity> findPcList(CardTransactionEntity entity);

	@Update("update card_transaction set trans_type = #{transTypeNew} where trans_type = #{transTypeLod}")
	void updateTransTypeLalbe(@Param("transTypeLod") String transTypeLod,@Param("transTypeNew") String transTypeNew);

	// ===================== 消费洞察聚合查询 =====================
	// 统计规则：只统计成功订单(posted)；金额取 local_currency_amt 绝对值；AUTH消费 TOPUP充值 REFUND退款

	String INSIGHT_SUMS = "IFNULL(SUM(CASE WHEN trans_type = 'AUTH' THEN ABS(CAST(local_currency_amt AS DECIMAL(18,6))) END),0) AS consumeTotal, "
			+ "IFNULL(SUM(CASE WHEN trans_type = 'TOPUP' THEN ABS(CAST(local_currency_amt AS DECIMAL(18,6))) END),0) AS topupTotal, "
			+ "IFNULL(SUM(CASE WHEN trans_type = 'REFUND' THEN ABS(CAST(local_currency_amt AS DECIMAL(18,6))) END),0) AS refundTotal, "
			+ "COUNT(*) AS totalCount ";

	String INSIGHT_WHERE = "FROM card_transaction WHERE uid = #{uid} AND order_state = 'posted' "
			+ "AND DATE_FORMAT(setTime,'%Y-%m-%d') &gt;= #{startDate} AND DATE_FORMAT(setTime,'%Y-%m-%d') &lt;= #{endDate} "
			+ "<if test='userBankcardId != null'>AND user_bankcard_id = #{userBankcardId} </if>";

	/** 区间合计（消费/充值/退款/笔数） */
	@Select("<script>SELECT " + INSIGHT_SUMS + INSIGHT_WHERE + "</script>")
	com.worldpay.entity.card.CardInsightEntity insightOverview(@Param("uid") String uid,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userBankcardId") Integer userBankcardId);

	/** 按日聚合 */
	@Select("<script>SELECT DATE_FORMAT(setTime,'%Y-%m-%d') AS dateLabel, " + INSIGHT_SUMS + INSIGHT_WHERE
			+ "GROUP BY DATE_FORMAT(setTime,'%Y-%m-%d') ORDER BY dateLabel DESC</script>")
	java.util.List<com.worldpay.entity.card.CardInsightEntity> insightByDay(@Param("uid") String uid,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userBankcardId") Integer userBankcardId);

	/** 按周聚合（以当周周一为标签） */
	@Select("<script>SELECT DATE_FORMAT(DATE_SUB(setTime, INTERVAL WEEKDAY(setTime) DAY),'%Y-%m-%d') AS dateLabel, "
			+ INSIGHT_SUMS + INSIGHT_WHERE
			+ "GROUP BY DATE_FORMAT(DATE_SUB(setTime, INTERVAL WEEKDAY(setTime) DAY),'%Y-%m-%d') ORDER BY dateLabel DESC</script>")
	java.util.List<com.worldpay.entity.card.CardInsightEntity> insightByWeek(@Param("uid") String uid,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userBankcardId") Integer userBankcardId);

	/** 按月聚合 */
	@Select("<script>SELECT DATE_FORMAT(setTime,'%Y-%m') AS dateLabel, " + INSIGHT_SUMS + INSIGHT_WHERE
			+ "GROUP BY DATE_FORMAT(setTime,'%Y-%m') ORDER BY dateLabel DESC</script>")
	java.util.List<com.worldpay.entity.card.CardInsightEntity> insightByMonth(@Param("uid") String uid,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userBankcardId") Integer userBankcardId);

	/** 按季度聚合（每三个月，标签形如 2026-Q1） */
	@Select("<script>SELECT CONCAT(YEAR(setTime), '-Q', QUARTER(setTime)) AS dateLabel, " + INSIGHT_SUMS + INSIGHT_WHERE
			+ "GROUP BY YEAR(setTime), QUARTER(setTime) ORDER BY MIN(setTime) DESC</script>")
	java.util.List<com.worldpay.entity.card.CardInsightEntity> insightByQuarter(@Param("uid") String uid,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userBankcardId") Integer userBankcardId);

}
