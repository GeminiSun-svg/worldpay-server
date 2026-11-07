package com.worldpay.dao.channel;

import com.worldpay.api.response.ChannelUserInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.channel.ChannelUserEntity;

import java.util.List;

@Repository
public interface ChannelUserDao extends BaseMapper<ChannelUserEntity>{

	@Select("select * from channel_user where invitation_code = #{invitationCode}")
	ChannelUserEntity findInvitationCode(@Param("invitationCode") String invitationCode);

	@Select("<script>" +
			"SELECT \n" +
			"    cu.*,\n" +
			"    su.tel AS tel,\n" +
			"    su.acctive AS acctive,\n" +
			"    su.google_secretkey AS googleSecretkey,\n" +
			"    si.ip_site AS ipWhite,\n" +
			"    cl.level_name AS levelName\n" +
			"FROM channel_user cu\n" +
			"LEFT JOIN channel_level cl ON cl.level_no = cu.level_no\n" +
			"LEFT JOIN sys_user su ON cu.user_id = su.id\n" +
			"LEFT JOIN sys_ip si ON si.set_user = su.id \n" +
			"WHERE su.user_state = 1\n" +
			"<if test='acctive != null'>\n" +
			"    AND su.acctive = #{acctive}\n" +
			"</if>\n" +
			"<if test='levelNo != null'>\n" +
			"    AND cl.level_no = #{levelNo}\n" +
			"</if>\n" +
			"<if test='invitationCode != null and invitationCode != \"\"'>\n" +
			"    AND cu.invitation_code = #{invitationCode}\n" +
			"</if>\n" +
			"order by cu.setTime desc" +
			"</script>")
    List<ChannelUserEntity> selectchannelUsers(ChannelUserEntity entity);

	@Select({
			"<script>",
			"SELECT ",
			"    cu.*,",
			"    su.tel AS tel,",
			"    su.acctive AS acctive,",
			"    su.google_secretkey AS googleSecretkey,",
			"    si.ip_site AS ipWhite,",
			"    cl.level_name AS levelName",
			"FROM channel_user cu",
			"LEFT JOIN channel_level cl ON cl.level_no = cu.level_no",
			"LEFT JOIN sys_user su ON cu.user_id = su.id",
			"LEFT JOIN sys_ip si ON si.set_user = su.id",
			"WHERE su.user_state = 1",
			// 核心：动态判断，userId非空时才添加su.user_id相关条件
			"<if test='userId != null'>",
			"  AND su.id = #{userId}",
			"</if>",
			"</script>"
	})
	List<ChannelUserEntity> selectchannelUsersByUid(@Param("userId") String userId);

    @Select("select user_id as userId,user_name as channelName from channel_user where delete_status = 1")
	List<ChannelUserInfoEntity> findChannelUserInfo();

	@Select("SELECT \n" +
			"    cu.*,\n" +
			"    su.tel AS tel,\n" +
			"    su.acctive AS acctive,\n" +
			"    su.google_secretkey AS googleSecretkey,\n" +
			"    si.ip_site AS ipWhite,\n" +
			"    cl.level_name AS levelName\n" +
			"FROM channel_user cu\n" +
			"LEFT JOIN channel_level cl ON cl.level_no = cu.level_no\n" +
			"LEFT JOIN sys_user su ON cu.user_id = su.id\n" +
			"LEFT JOIN sys_ip si ON si.set_user = su.id \n" +
			"LEFT JOIN app_user_intiter aui on cu.user_id = aui.cover_inviter_user_uid \n" +
			"WHERE su.user_state = 1 and aui.cover_inviter_user_uid in " +
			"(select cover_inviter_user_uid from app_user_intiter where inviter_user_uid = #{userId}) order by cu.setTime desc")
	List<ChannelUserEntity> selectChannelUsersByCoverUid(@Param("userId") String userId);

	@Select({
			"<script>",
			"SELECT ",
			"    cu.*,",
			"    su.tel AS tel,",
			"    su.acctive AS acctive,",
			"    su.google_secretkey AS googleSecretkey,",
			"    si.ip_site AS ipWhite,",
			"    cl.level_name AS levelName",
			"FROM channel_user cu",
			"LEFT JOIN channel_level cl ON cl.level_no = cu.level_no",
			"LEFT JOIN sys_user su ON cu.user_id = su.id",
			"LEFT JOIN sys_ip si ON si.set_user = su.id",
			"WHERE su.user_state = 1",
			"<if test='userIds != null and userIds.size() > 0'>",
			"  AND cu.user_id IN",
			"  <foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
			"    #{id}",
			"  </foreach>",
			"</if>",
			"<if test='entity.acctive != null'>",
			"  AND su.acctive = #{entity.acctive}",
			"</if>",
			"<if test='entity.levelNo != null'>",
			"  AND cl.level_no = #{entity.levelNo}",
			"</if>",
			"<if test='entity.invitationCode != null and entity.invitationCode != \"\"'>",
			"  AND cu.invitation_code = #{entity.invitationCode}",
			"</if>",
			"ORDER BY cu.setTime DESC",
			"</script>"
	})
	List<ChannelUserEntity> selectChannelUsersByUserIds(@Param("userIds") List<String> userIds, @Param("entity") ChannelUserEntity entity);

	@Select("select count(*) from channel_user cu " +
			"left join channel_level cl on cu.level_no = cl.level_no " +
			"where cu.user_id = #{userId} and cu.level_no = (select max(level_no) from channel_level where delete_state = 1)")
	int findChannelByChannelLevelNo(@Param("userId") Integer userId);

	@Update("update channel_user set delete_status = 0 where user_id = #{userId}")
    void updateState(@Param("userId") int userId);

	@Select("select cu.* from channel_user cu " +
			"left join app_channel_intiter aci " +
			"on cu.user_id = aci.channel_uid " +
			"where aci.app_uid = #{uid}")
    ChannelUserEntity findChannelByAppUid(@Param("uid") String uid);

	@Select("select " +
			"cu.*,su.tel as tel,su.acctive as acctive,su.google_secretkey as googleSecretkey " +
			"from channel_user cu " +
			"left join sys_user su on cu.user_id = su.id " +
			"where cu.id = #{id}")
	ChannelUserEntity selectChannelInfoById(@Param(("id")) String id);

	@Select("select commission_rate from channel_user where user_id = #{inviterUserUid}")
    Double selectCommissionRate(@Param("inviterUserUid") String inviterUserUid);

}
