package com.worldpay.dao.log;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.SysLogEntity;

@Repository
public interface SysLogDao extends BaseMapper<SysLogEntity>{

	
	
	@Select("<script>"
            + "select * from sys_log where 1=1 "
            + "<if test = 'userName != null' > and userName = #{userName}</if>"
            + "<if test = 'moduleName != null' > and moduleName = #{moduleName}</if>"
            + "<if test = 'reqUrl != null' > and reqUrl = #{reqUrl}</if>"
            + "</script>")
	List<SysLogEntity> findList(SysLogEntity entity);

}
