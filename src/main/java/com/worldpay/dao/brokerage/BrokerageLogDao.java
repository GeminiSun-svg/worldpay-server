package com.worldpay.dao.brokerage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.brokerage.BrokerageLogEntity;
import com.worldpay.query.PublicQueryEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BrokerageLogDao extends BaseMapper<BrokerageLogEntity> {

    @Select({
            "<script>",
            "SELECT *",
            "FROM brokerage_log",
            "<where>",
            "  <if test='inviterUserUid != null'>",
            "    Inviter_user_uid = #{inviterUserUid}",
            "  </if>",
            "  <if test='coverInviterUserUid != null'>",
            "    AND cover_Inviter_user_uid = #{coverInviterUserUid}",
            "  </if>",
            "  <if test='intiterUserType != null'>",
            "    AND intiter_user_type = #{intiterUserType}",
            "  </if>",
            "</where>",
            "ORDER BY setTime DESC",
            "</script>"
    })
    List<BrokerageLogEntity> selectLogList(
            @Param("inviterUserUid") String inviterUserUid,
            @Param("coverInviterUserUid") String coverInviterUserUid,
            @Param("intiterUserType") Integer intiterUserType);

    @Select("select sum(brokerage_fee) from brokerage_log where Inviter_user_uid = #{channelId}")
    void findSumListByChannelId(@Param("channelId") Integer channelId);

    @Select({
            "<script>",
            "<![CDATA[",
            "SELECT IFNULL(SUM(brokerage_fee), 0) AS openCardFee",
            "FROM brokerage_log",
            "WHERE Inviter_user_uid = #{userId}", // 恒真条件，保证后续AND条件拼接语法合法（可选，此处为了健壮性保留）
            "]]>",
            "<![CDATA[",
            "  AND DATE_FORMAT(setTime, '%Y-%m-%d') >= #{begTime}",
            "  AND DATE_FORMAT(setTime, '%Y-%m-%d') <= #{endTime}",
            "]]>",
            "</script>"
    })
    BigDecimal findChannelBrokerageFee(PublicQueryEntity entity);

    @Select("select sum(brokerage_fee) from brokerage_log where Inviter_user_uid = #{userId} and intiter_user_type = #{type}")
    BigDecimal findChannelUserIdBrokerageFee(@Param("userId") String userId, @Param("type") Integer type);

    @Select("SELECT IFNULL(SUM(brokerage_fee),0) from brokerage_log where Inviter_user = (select channel_id from app_channel_intiter where app_uid = #{uid})")
	Double findSumChannel(@Param("uid") String uid);

}
