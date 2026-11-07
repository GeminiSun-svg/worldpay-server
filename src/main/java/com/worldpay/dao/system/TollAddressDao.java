package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.TollAddressEntity;

@Repository
public interface TollAddressDao extends BaseMapper<TollAddressEntity>{

	@Select("<script>"
			+ "select * from toll_address where 1=1 "
			+ "<if test = 'addressType != null'> and address_type = #{addressType}</if>"
			+ "<if test = 'addressSite != null'> and address_site = #{addressSite}</if>"
			+ "</script>")
	List<TollAddressEntity> findList(TollAddressEntity entity);

	@Select("select * from toll_address where address_type = #{addressType}")
	TollAddressEntity findType(@Param("addressType") String addressType);

}
