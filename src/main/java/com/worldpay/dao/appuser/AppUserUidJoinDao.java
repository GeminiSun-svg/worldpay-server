package com.worldpay.dao.appuser;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserUidJoinEntity;

@Repository
public interface AppUserUidJoinDao extends BaseMapper<AppUserUidJoinEntity>{

	@Select("select new_uid from app_user_uid_join where old_uid = #{uId}")
	String findByNewUid(@Param("uId") String uId);

}
