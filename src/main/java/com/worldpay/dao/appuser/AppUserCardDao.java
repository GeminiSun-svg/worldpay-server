package com.worldpay.dao.appuser;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserCardEntity;
import com.worldpay.query.PublicQueryEntity;

@Repository
public interface AppUserCardDao extends BaseMapper<AppUserCardEntity>{

	@Select("select * from app_user_card where user_bankcard_id = #{userBankcardId}")
	AppUserCardEntity findUserBankcardId(@Param("userBankcardId") Integer userBankcardId);

	@Select("select ifnull(sum(balance),0) from app_user_card where uid = #{uid}")
	Double findUserCardAssets(@Param("uid") String uid);

	@Select("<script>"
			+ "select * from app_user_card where status IN (3,1)"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardType != null'> and card_type = #{cardType}</if>"
			+ "<if test = 'status != null'> and status = #{status}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'userEmail != null'> and apply_id in (select apply_id from card_apply_man where user_email = #{userEmail})</if>"
			+ "<if test = 'userTel != null'> and apply_id in (select apply_id from card_apply_man where user_tel = #{userTel})</if>"
			+ "<if test = 'userName != null'> and apply_id in (select apply_id from card_apply_man where user_name = #{userName})</if> "
			+ "ORDER BY FIELD(status, "
				+ "    3, "
		        + "	   1"
		        + ")"
			+ "</script>")
	List<AppUserCardEntity> findList(AppUserCardEntity entity);

	@Select("select * from app_user_card where user_bankcard_id = #{userBankcardId} and uid = #{uid}")
	AppUserCardEntity findUserBankcardIdOrUid(@Param("userBankcardId") Integer userBankcardId,@Param("uid") String uid);

	@Select("select COUNT(*) from app_user_card")
	int findCount();

	@Select("select * from app_user_card where apply_id = #{applyId}")
	AppUserCardEntity findApplyId(@Param("applyId") Integer applyId);

	@Select("select * from app_user_card where logistics_num = #{logisticsNum}")
	AppUserCardEntity findLogisticsNum(@Param("logisticsNum") String logisticsNum);

	@Select("SELECT ifnull(count(*),0) from (select ifnull(count(*),0) from app_user_card "
			+ "where date_format(setTime, '%Y-%m-%d') >= #{begTime} "
			+ "and date_format(setTime, '%Y-%m-%d') <= #{endTime} GROUP BY uid) t")
	Integer findOpenCardUserNum(PublicQueryEntity entity);

	@Select({
			"<script>",
			"SELECT SUM(balance) AS balanceTotal FROM app_user_card",
			"<where>",
			"<if test='uidList != null and uidList.size() > 0'>",
			"uid IN <foreach collection='uidList' item='uId' open='(' separator=',' close=')'>#{uId}</foreach>",
			"</if>",
			"</where>",
			"</script>"
	})
	Double findCardTotal(@Param("uidList") List<String> uidList);

//	@Select("select * from app_user_card where user_bankcard_id = #{userBankcardId} and card_no = #{cardNo}")
//	AppUserCardEntity findUserBankcardIdOrOrderNum(@Param("userBankcardId") Integer userBankcardId, @Param("cardNo") String cardNo);

	@Select({
			"<script>",
			"<![CDATA[",
			"SELECT IFNULL(COUNT(*), 0)",
			"FROM (",
			"  SELECT IFNULL(COUNT(*), 0)",
			"  FROM app_user_card",
			"  WHERE uid IN",
			"]]>",
			"    <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
			"      #{id}",
			"    </foreach>",
			"<![CDATA[",
			"    AND DATE_FORMAT(setTime, '%Y-%m-%d') >= #{begTime}",
			"    AND DATE_FORMAT(setTime, '%Y-%m-%d') <= #{endTime}",
			"  GROUP BY uid",
			") t",
			"]]>",
			"</script>"
	})
	Integer findOpenCardChannelUserNum(PublicQueryEntity entity);

	@Select({
			"<script>",
			"SELECT SUM(balance)",
			"FROM app_user_card",
			"WHERE uid IN",
			"  <foreach collection='uid' item='id' open='(' separator=',' close=')'>",
			"    #{id}",
			"  </foreach>",
			"</script>"
	})
	Double findChannelCardTotal(PublicQueryEntity entity);

	@Select("select * from app_user_card where uid = #{uid}")
	List<AppUserCardEntity> findUid(@Param("uid") String uid);


	@Select("<script>"
			+ "select * from app_user_card where status IN (3, 6,2,4)"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardType != null'> and card_type = #{cardType}</if>"
			+ "<if test = 'status != null'> and status = #{status}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'userEmail != null'> and apply_id in (select apply_id from card_apply_man where user_email = #{userEmail})</if>"
			+ "<if test = 'userTel != null'> and apply_id in (select apply_id from card_apply_man where user_tel = #{userTel})</if>"
			+ "<if test = 'userName != null'> and apply_id in (select apply_id from card_apply_man where user_name = #{userName})</if> "
			+ "ORDER BY FIELD(status, "
		        + "    3, "
		        + "    6, "
		        + "    2, "
		        + "    4"
		        + ")"
			+ "</script>")
	List<AppUserCardEntity> findStateList(AppUserCardEntity entity);


	@Select("<script>"
			+ "select * from app_user_card where 1=1 "
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'cardType != null'> and card_type = #{cardType}</if>"
			+ "<if test = 'status != null'> and status = #{status}</if>"
			+ "<if test = 'cardNo != null'> and card_no = #{cardNo}</if>"
			+ "<if test = 'userEmail != null'> and apply_id in (select apply_id from card_apply_man where user_email = #{userEmail})</if>"
			+ "<if test = 'userTel != null'> and apply_id in (select apply_id from card_apply_man where user_tel = #{userTel})</if>"
			+ "<if test = 'userName != null'> and apply_id in (select apply_id from card_apply_man where user_name = #{userName})</if> "
			+ "ORDER BY setTime desc"
			+ "</script>")
	List<AppUserCardEntity> findPcList(AppUserCardEntity entity);

	@Update("update app_user_card set status = #{newState} where status = #{oldState}")
	void updateStateLable(@Param("oldState") String oldState,@Param("newState") String newState);

}
