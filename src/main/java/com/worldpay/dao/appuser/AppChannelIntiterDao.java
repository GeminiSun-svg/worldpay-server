package com.worldpay.dao.appuser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppChannelIntiterEntity;
import com.worldpay.entity.channel.ChannelUserEntity;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 类描述：数据层
 *
 * @author GeminiSun
 * @date 2026/2/4 9:45
 */
@Repository
public interface AppChannelIntiterDao extends BaseMapper<AppChannelIntiterEntity> {

	@Select("select * from app_channel_intiter where channel_uid = #{currentUserUId}")
	AppChannelIntiterEntity findChannalId(@Param("currentUserUId") Integer currentUserUId);

	@Select("select * from channel_user where user_id = (select channel_uid from app_channel_intiter where app_id = #{appId})")
	ChannelUserEntity findByappId(@Param("appId") Integer appId);
}
