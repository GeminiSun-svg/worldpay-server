package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SysFaCountryEntity;

@Repository
public interface SysFaCountryDao extends BaseMapper<SysFaCountryEntity>{

	@Select("select * from sys_fa_country where parentId = #{parentId}")
	List<SysFaCountryEntity> findParent(@Param("parentId") Integer parentId);

}
