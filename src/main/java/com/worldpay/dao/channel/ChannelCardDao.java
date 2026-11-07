package com.worldpay.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.api.response.ChannelCardResponse;
import com.worldpay.entity.channel.ChannelCardEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChannelCardDao extends BaseMapper<ChannelCardEntity>{

	@Select("select * from channel_card where channel_id = #{channelId} and card_uuid = #{cardUuid}")
	ChannelCardEntity findOne(@Param("channelId") Integer channelId,@Param("cardUuid") String cardUuid);

	@Select({
			"<script>",
			"SELECT",
			"  cc.*,",
			"  cu.user_name AS username,",
			" su.acctive AS acctive",
			"FROM channel_card cc",
			"LEFT JOIN channel_user cu ON cc.channel_id = cu.id",
			"LEFT JOIN sys_user su ON su.id = cu.user_id",
			"<where>",
			"  <if test='userId != null and userId != \"\"'>",
			"    AND cu.user_id = #{userId}",
			"  </if>",
			"  <if test='cardType != null and cardType != \"\"'>",
			"    AND cc.card_type = #{cardType}",
			"  </if>",
			"  <if test='acctive != null and acctive != \"\"'>",
			"    AND su.acctive = #{acctive}",
			"  </if>",
			"  <if test='cardId != null and cardId != \"\"'>",
			"    AND cc.card_id = #{cardId}",
			"  </if>",
			"</where>",
			"</script>"
	})
	List<ChannelCardResponse> selectChannelCardById(@Param("userId") Integer userId, @Param("cardType") String cardType, @Param("acctive") String acctive,@Param("cardId") Integer cardId);

	@Select({
			"<script>",
			"SELECT",
			"  cc.*,",
			"  cu.user_name AS username,",
			"  su.acctive AS acctive",
			"FROM channel_card cc",
			"LEFT JOIN channel_user cu ON cc.channel_id = cu.id",
			"LEFT JOIN sys_user su ON su.id = cu.user_id",
			"<where>",
			"  <if test='userIds != null and userIds.size() > 0'>",
			"    AND cu.user_id IN",
			"    <foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
			"      #{id}",
			"    </foreach>",
			"  </if>",
			"  <if test='cardType != null and cardType != \"\"'>",
			"    AND cc.card_type = #{cardType}",
			"  </if>",
			"  <if test='acctive != null and acctive != \"\"'>",
			"    AND su.acctive = #{acctive}",
			"  </if>",
			"  <if test='cardId != null and cardId != \"\"'>",
			"    AND cc.card_id = #{cardId}",
			"  </if>",
			"</where>",
			"</script>"
	})
	List<ChannelCardResponse> selectChannelCardByUserIds(@Param("userIds") List<String> userIds, @Param("cardType") String cardType, @Param("acctive") String acctive,@Param("cardId") Integer cardId);

	@Select({"SELECT",
			"  cc.*,",
			"  cu.user_name AS username",
			"FROM channel_card cc",
			"LEFT JOIN channel_user cu ON cc.channel_id = cu.id where cc.id = #{id}"
	})
	ChannelCardResponse selectCardInfoById(@Param("id") Integer id);

	@Delete("<script>" +
			"DELETE FROM channel_card " +
			"WHERE channel_id = #{userId} " +
			"AND card_id IN " +
			"<foreach collection='cardIds' item='cardId' open='(' close=')' separator=','>" +
			"   #{cardId}" +
			"</foreach>" +
			"</script>")
    void deleteByUserIdAndCardIds(@Param("userId") Integer userId, @Param("cardIds") Set<Integer> cardIds);

}
