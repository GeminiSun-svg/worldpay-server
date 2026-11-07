package com.worldpay.dao.system;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SysInfoEntity;

@Repository
public interface SysInfoDao extends BaseMapper<SysInfoEntity>{

	@Select("select * from sys_info where config_name = #{configName} and status=0 limit 1")
	SysInfoEntity findName(@Param("configName") String name, @Param("status") String status);

    @Select("select * from sys_info where config_type = #{configType} and status = #{status} and language = #{language} limit 1")
    SysInfoEntity findContent(@Param("configType") Integer configType,@Param("status") Integer status,@Param("language") String language);

	@Select("select * from sys_info where status = #{status}")
	List<SysInfoEntity> findNormalInfo(@Param("status") Integer status);

	@Select("<script>"
			+ "select * from sys_info where 1=1 "
			+ "<if test = 'configLable != null'> and config_lable = #{configLable}</if>"
			+ "<if test = 'configType != null'> and config_type = #{configType}</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "<if test = 'configName != null'> and config_name like '%${configName}%'</if>"
			+ "<if test = 'status != null'> and status = #{status}</if>"
			+ " order by config_type"
			+ "</script>")
	List<SysInfoEntity> findList(SysInfoEntity entity);

	
}
