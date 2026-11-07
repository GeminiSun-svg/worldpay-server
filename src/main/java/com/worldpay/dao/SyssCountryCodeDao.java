package com.worldpay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SyssCountryCodeEntity;

@Repository
public interface SyssCountryCodeDao extends BaseMapper<SyssCountryCodeEntity>{

	@Select("select * from sys_country_code where language = #{language}")
	List<SyssCountryCodeEntity> findLanguage(@Param("language") String language);


}
