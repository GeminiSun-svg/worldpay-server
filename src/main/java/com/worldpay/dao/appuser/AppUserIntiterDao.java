package com.worldpay.dao.appuser;


import com.worldpay.query.PublicQueryEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserIntiterEntity;

import java.util.List;

@Repository
public interface AppUserIntiterDao extends BaseMapper<AppUserIntiterEntity> {

    @Select("select * from app_user_intiter where cover_inviter_user = #{coverInviterUser}")
    AppUserIntiterEntity findCoverInviterUser(@Param("coverInviterUser") Integer coverInviterUser);

    @Select("select * from app_user_intiter where cover_inviter_user_uid = #{coverInviterUserUid}")
    AppUserIntiterEntity findByUidCoverInviterUser(@Param("coverInviterUserUid") String coverInviterUserUid);

    @Select("select * from app_user_intiter where cover_inviter_user_uid = #{coverInviterUserUid}")
    AppUserIntiterEntity findCoverInviterUserUid(@Param("coverInviterUserUid") String coverInviterUserUid);

    @Select({
            "<script>",
            "SELECT *",
            "FROM app_user_intiter ",
            "<where>",
            "  <if test='intiterUserType != null'>",
            "    intiter_user_type = #{intiterUserType}",
            "  </if>",
            "  <if test='inviterUserUid != null'>",
            "    AND inviter_user_uid = #{inviterUserUid}",
            "  </if>",
            "  <if test='coverInviterUserUid != null'>",
            "    AND cover_inviter_user_uid = #{coverInviterUserUid}",
            "  </if>",
            "</where>",
            "ORDER BY setTime DESC",
            "</script>"
    })
    List<AppUserIntiterEntity> selectAppUserIntiters(
            @Param("intiterUserType") Integer intiterUserType,
            @Param("inviterUserUid") String inviterUserUid,
            @Param("coverInviterUserUid") String coverInviterUserUid);

    @Select("select cover_inviter_user_uid from app_user_intiter where inviter_user_uid = #{userId} "
            + "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
    List<String> findByUserId(PublicQueryEntity entity);

    @Select("select cover_inviter_user_uid from app_user_intiter where inviter_user_uid = #{userId}")
    List<String> findByUserIds(PublicQueryEntity entity);

    @Select("SELECT * FROM app_user_intiter WHERE cover_inviter_user_uid = #{currentCoverUid}")
    AppUserIntiterEntity queryByCoverUid(@Param("currentCoverUid") String currentCoverUid);

	 @Select("select * from app_user_intiter where inviter_user_uid = #{currentUid} "
	    		+ "and date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	List<AppUserIntiterEntity> findInviterUserList(@Param("currentUid") String currentUid,@Param("begTime") String begTime,@Param("endTime") String endTime);

    @Select("select * from app_user_intiter where inviter_user_uid = #{currentUid} ")
    List<AppUserIntiterEntity> findInviterUserListNoTime(@Param("currentUid") String currentUid);

    @Update("update app_user_intiter set " +
            "intiter_user_type = #{type}," +
            "gmtModified = NOW() " +
            "where inviter_user_uid = #{appUid}")
    void updateTypeByInviterUid(@Param("appUid") String appUid, @Param("type") Integer type);

    @Update("update app_user_intiter set " +
            "intiter_user_type = #{type}," +
            "intiter_user_level = #{channelLevel}," +
            "gmtModified = NOW() " +
            "where inviter_user_uid = #{appUid}")
    void updateTypeAndLevelByInviterUid(@Param("appUid")String appUid, @Param("channelLevel") Integer channelLevel, @Param("type") Integer type);


}
