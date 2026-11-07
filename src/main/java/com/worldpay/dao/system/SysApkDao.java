package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SysApkEntity;

@Repository
public interface SysApkDao extends BaseMapper<SysApkEntity>{

	@Select("<script>"
			+ "select * from sys_apk where 1=1 "
			+ "<if test = 'platform != null'> and platform = #{platform}</if>"
			+ "<if test = 'state != null'> and state = #{state}</if>"
			+ "<if test = 'updateType != null'> and update_type = #{updateType}</if>"
			+ "<if test = 'appVersion != null'> and app_version = #{appVersion}</if>"
			+ " order by state"
			+ "</script>")
	List<SysApkEntity> findList(SysApkEntity entity);

	@Select("select * from sys_apk where platform = #{platform} and state = #{state} order by gmt_modified desc")
	SysApkEntity findNew(@Param("platform") Integer platform,@Param("state") Integer state);

	@Update("update sys_apk set state = #{state} where platform = #{platform}")
	void updateState(@Param("platform") Integer platform,@Param("state") Integer state);

}
