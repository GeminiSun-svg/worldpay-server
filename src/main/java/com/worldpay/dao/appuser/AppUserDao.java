package com.worldpay.dao.appuser;

import java.util.List;

import com.worldpay.api.response.AppUserChannelEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserEntity;
import com.worldpay.entity.response.AdminStatEntity;
import com.worldpay.query.PublicQueryEntity;

@Repository
public interface AppUserDao extends BaseMapper<AppUserEntity>{

	
	@Select("select * from app_user where user_email = #{userEmail}")
	AppUserEntity findByEmail(@Param("userEmail") String userEmail);

	@Select("select * from app_user where uid = #{uid}")
	AppUserEntity findByUid(@Param("uid") String uid);

	// 查询总用户数
	@Select("SELECT COUNT(*) FROM app_user")
	int countTotalUsers();

	// 查询时间段内新增用户数
	@Select("SELECT COUNT(*) FROM app_user WHERE create_time BETWEEN #{begTime} AND #{endTime}")
	int countUsersByTime(@Param("begTime") String begTime, @Param("endTime") String endTime);

	// 查询总钱包余额
	@Select("SELECT SUM(wallet_balance) FROM app_user")
    double countTotalWalletBalance();

	@Select("<script>"
			+ "select au.*, "
			+ "aui.intiter_user_type as invitationLable, "
			+ "aui.inviter_user_uid as superName "
			+ "from app_user au "
			+ "LEFT JOIN app_user_intiter aui on au.uid = aui.cover_inviter_user_uid "
			+ "where 1=1 "  // 关键：用于简化动态条件拼接，避免第一个条件前多余的AND
			+ "<if test='userEmail != null'>"
			+ "  and au.user_email = #{userEmail}"
			+ "</if>"
			+ "<if test='uid != null'>"
			+ "  and au.uid = #{uid}"
			+ "</if>"
			+ "<if test='mobileNumber != null'>"
			+ "  and au.mobile_number = #{mobileNumber}"
			+ "</if>"
			+ "<if test='userState != null'>"
			+ "  and au.user_state = #{userState}"
			+ "</if>"
			+ "<if test='kycState != null'>"
			+ "  and au.kyc_state = #{kycState}"
			+ "</if>"
			+ "<if test='walletState != null'>"
			+ "  and au.wallet_state = #{walletState}"
			+ "</if> "
			+ "order by setTime desc"
			+ "</script>")
	List<AppUserChannelEntity> findList(AppUserEntity entity);

	@Select("select uid from app_user")
	List<String> finduIdList();

	@Select("select ifnull(count(*),0) from app_user where "
			+ "date_format(setTime, '%Y-%m-%d') >= #{begTime} and date_format(setTime, '%Y-%m-%d') <= #{endTime}")
	Integer findUserNum(PublicQueryEntity entity);

	@Select({
			"<script>",
			"SELECT SUM(wallet_balance) AS walletTotal, SUM(freeze_balance) AS walletFreezeTotal FROM app_user",
			"<where>",
			"<if test='uidList != null and uidList.size() > 0'>",
			"uid IN <foreach collection='uidList' item='uId' open='(' separator=',' close=')'>#{uId}</foreach>",
			"</if>",
			"</where>",
			"</script>"
	})
	AdminStatEntity findWalletTotal(@Param("uidList") List<String> uidList);

	@Select("select * from app_user where tron_usdt_acount = #{originalAddress}")
	AppUserEntity findAddress(@Param("originalAddress") String originalAddress);

	@Select("select * from app_user where invitation_code = #{invitationCode}")
	AppUserEntity findInvitationCode(@Param("invitationCode")String invitationCode);

	@Select("select * from app_user where kyc_state = #{kycState}")
	List<AppUserEntity> findKyc(@Param("kycState") Integer index);

	@Select("select kyc_state from app_user where uid = #{uid}")
	Integer findKycState(@Param("uid") String uid);

}
