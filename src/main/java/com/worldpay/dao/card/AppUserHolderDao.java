package com.worldpay.dao.card;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserHolderEntity;

@Repository
public interface AppUserHolderDao extends BaseMapper<AppUserHolderEntity>{

	@Select("select * from app_user_holder where uid = #{uid}")
	List<AppUserHolderEntity> findByUid(@Param("uid") String uid);

}
