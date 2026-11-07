package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.HelpEntity;

@Repository
public interface HelpDao extends BaseMapper<HelpEntity>{

	@Select("<script>"
			+ "select * from help_data where 1=1 "
			+ "<if test ='language != null'> and language = #{language}</if>"
			+ "<if test ='articleName != null'> and article_name like '%${articleName}%'</if>"
			+ "</script>")
	List<HelpEntity> findList(HelpEntity entity);

}
