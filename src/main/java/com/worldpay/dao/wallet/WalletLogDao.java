package com.worldpay.dao.wallet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.api.response.StatBackEntity;import com.worldpay.entity.wallet.WalletLogEntity;
import com.worldpay.query.PublicQueryEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletLogDao extends BaseMapper<WalletLogEntity> {


    @Delete("DELETE FROM wallet_log WHERE id = #{id}")
    int deleteWalletLogById(@Param("id") Integer id);


    @Select("SELECT * FROM wallet_log WHERE id = #{id}")
    WalletLogEntity findWalletLogById(@Param("id") Integer id);

    @Select("<script>" +
            "SELECT * FROM wallet_log " +
            "WHERE 1=1 " +
            "<if test='orderNo != null'>AND order_no = #{orderNo}</if> " +
            "<if test='tradeType != null'>AND trade_type = #{tradeType}</if> " +
            "<if test='operateType != null'>AND operate_type = #{operateType}</if> " +
            "<if test='userId != null'>AND user_id = #{userId}</if> " +
            "<if test='uid != null'>AND uid = #{uid}</if>"+
            "<if test='title != null'>AND title = #{title}</if> " +
            "<if test='primevalMoney != null'>AND primeval_money = #{primevalMoney}</if> " +
            "<if test='realMoney != null'>AND real_money = #{realMoney}</if> " +
            "<if test='status != null'>AND status = #{status}</if> " +
            "<if test='userEmail != null'>AND uid in (select uid from app_user where user_email = #{userEmail})</if> " +
            "<if test='toAccount != null'>AND to_account = #{toAccount}</if> "
            + "order by setTime desc" +
            "</script>")
    List<WalletLogEntity> findWalletLogsByConditions(WalletLogEntity walletLogEntity);


    @Select("select * from wallet_log where out_order_no = #{outOrderNo} limit 1")
	WalletLogEntity findOutOrderNo(@Param("outOrderNo") String outOrderNo);
    
    @Select("select * from wallet_log where out_order_no = #{outOrderNo} and operate_type = #{operateType}")
	WalletLogEntity findOutOrderNoOrType(@Param("outOrderNo") String outOrderNo,@Param("operateType") Integer operateType);

    @Select("<script>" +
            "SELECT COALESCE(SUM(amount), 0) " +
            "FROM wallet_log " +
            "WHERE trade_type = #{tradeType} " +
            "AND status = #{status} " +
            "<if test='begTime != null and begTime != \"\" and endTime != null and endTime != \"\"'>" +
            "   AND create_time BETWEEN #{begTime} AND #{endTime}" +
            "</if>" +
            "</script>")
    double walletStatSumByTypeAndStatus(
            @Param("type") String type,
            @Param("status") String status,
            @Param("begTime") String begTime,
            @Param("endTime") String endTime
    );

    // 查询统计数据
    @Select("<script>" +
            "SELECT " +
            "COUNT(DISTINCT user_id) as rechargeUserCount, " +
            "COUNT(*) as totalOrderCount, " +
            "SUM(CASE WHEN type = 'OUT' THEN amount ELSE 0 END) as totalOutAmount, " +
            "SUM(CASE WHEN type = 'IN' THEN amount ELSE 0 END) as totalInAmount, " +
            "SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pendingOrderCount, " +
            "SUM(CASE WHEN status = 'CONFIRMED' THEN 1 ELSE 0 END) as confirmedOrderCount, " +
            "SUM(CASE WHEN status = 'FAILED' THEN 1 ELSE 0 END) as failedOrderCount " +
            "FROM wallet_log " +
            "WHERE 1=1 " +
            "<if test='begTime != null and begTime != \"\" and endTime != null and endTime != \"\"'>" +
            "   AND gmtModified BETWEEN #{begTime} AND #{endTime} " +
            "</if>" +
            "<if test='begMonth != null and begMonth != \"\" and endMonth != null and endMonth != \"\"'>" +
            "   AND DATE_FORMAT(gmtModified, '%Y-%m') BETWEEN #{begMonth} AND #{endMonth} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND status = #{status} " +
            "</if>" +
            "<if test='tradeType != null and tradeType != \"\"'>" +
            "   AND trade_type = #{tradeType} " +
            "</if>" +
            "<if test='title != null and title != \"\"'>" +
            "   AND title LIKE CONCAT('%', #{title}, '%') " +
            "</if>" +
            "ORDER BY create_time DESC " +
            "</script>")

    StatBackEntity findWalletLogStatByConditions(PublicQueryEntity entity);

    // 查询订单列表
    @Select("<script>" +
            "SELECT * FROM wallet_log " +
            "WHERE 1=1 " +
            "<if test='begTime != null and begTime != \"\" and endTime != null and endTime != \"\"'>" +
            "   AND gmtModified BETWEEN #{begTime} AND #{endTime} " +
            "</if>" +
            "<if test='begMonth != null and begMonth != \"\" and endMonth != null and endMonth != \"\"'>" +
            "   AND DATE_FORMAT(gmtModified, '%Y-%m') BETWEEN #{begMonth} AND #{endMonth} " +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "   AND status = #{status} " +
            "</if>" +
            "<if test='tradeType != null and tradeType != \"\"'>" +
            "   AND trade_type = #{tradeType} " +
            "</if>" +
            "<if test='title != null and title != \"\"'>" +
            "   AND title LIKE CONCAT('%', #{title}, '%') " +
            "</if>" +
            "ORDER BY create_time DESC " +
            "</script>")
    List<WalletLogEntity> tfindWalletLogsByConditions(PublicQueryEntity entity);


    @Select("select * from wallet_log where out_order_no = #{hash}")
	WalletLogEntity findHash(@Param("hash") String hash);
    
    @Select("select ifnull(count(*),0) from (select count(*) from wallet_log "
    		+ "where trade_type =1 and date_format(setTime, '%Y-%m-%d') >= #{begTime} "
    		+ "and date_format(setTime, '%Y-%m-%d') <= #{endTime} GROUP BY uid) t")
	Integer findTopupUserNum(PublicQueryEntity entity);

    @Select("select * from wallet_log where out_order_no = #{id}")
	WalletLogEntity findApplyId(@Param("id") String id);

    @Select({
            "<script>",
            "<![CDATA[",
            "SELECT IFNULL(COUNT(*), 0)",
            "FROM (",
            "  SELECT COUNT(*)",
            "  FROM wallet_log",
            "  WHERE uid IN",
            "]]>",
            "  <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
            "    #{id}",
            "  </foreach>",
            "<![CDATA[",
            "    AND trade_type = 1",
            "    AND DATE_FORMAT(setTime, '%Y-%m-%d') >= #{begTime}",
            "    AND DATE_FORMAT(setTime, '%Y-%m-%d') <= #{endTime}",
            "  GROUP BY uid",
            ") t",
            "]]>",
            "</script>"
    })
    Integer findTopUpChannelUserNum(PublicQueryEntity entity);
}
