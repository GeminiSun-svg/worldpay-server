package com.worldpay.dao.appuser;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserIosWalletStateEntity;

@Repository
public interface AppUserIosWalletStateDao extends BaseMapper<AppUserIosWalletStateEntity>{

	@Select("select * from app_user_ios_wallet_state where app_user_uid = #{uid}")
	AppUserIosWalletStateEntity findUid(@Param("uid") String uid);

}
