package com.worldpay.dao.appuser;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.appuser.AppUserFileEntity;

@Repository
public interface AppUserFileDao extends BaseMapper<AppUserFileEntity>{

	@Select("select * from app_user_file where uid = #{uid}")
	List<AppUserFileEntity> findUid(@Param("uid") String uid);

	@Delete("delete from app_user_file where uid = #{uid} and document_type = #{documentType}")
	void deleteOne(@Param("uid")String uid, @Param("documentType")Integer documentType);

}
