package com.worldpay.dao.appuser;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserWebAddressEntity;

@Repository
public interface AppUserWebAddressDao extends BaseMapper<AppUserWebAddressEntity>{

	@Select("select * from app_user_webaddress where uid = #{uid}")
	AppUserWebAddressEntity findByUid(@Param("uid") String uid);

}
