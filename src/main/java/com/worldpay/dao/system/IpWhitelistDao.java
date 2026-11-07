package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.IpWhitelistEntity;

@Repository
public interface IpWhitelistDao extends BaseMapper<IpWhitelistEntity>{

	@Select("select * from sys_ip where ip_site = #{ipAddress}")
	IpWhitelistEntity findIp(@Param("ipAddress") String ipAddress);

	@Select("<script>"
			+ "select * from sys_ip where 1=1 "
			+ "<if test = 'ipSite != null'> and ip_site = #{ipSite}</if>"
			+ "</script>")
	List<IpWhitelistEntity> findList(IpWhitelistEntity entity);

	@Select("select * from sys_ip where set_user = #{setUser}")
	IpWhitelistEntity findIpBySetUser(@Param("setUser") Integer setUser);

}
