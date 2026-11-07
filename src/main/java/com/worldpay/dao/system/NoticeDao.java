package com.worldpay.dao.system;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.NoticeEntity;

@Repository
public interface NoticeDao extends BaseMapper<NoticeEntity>{

	@Select("select * from notice where title = #{title} and status=0 limit 1")
	NoticeEntity findName(@Param("title") String title, @Param("status") String status);

    @Select("select * from notice where title = #{title} and status=0 limit 1")
    NoticeEntity findContent(@Param("title") String configName);

	@Select("select * from notice where status = #{status}")
	List<NoticeEntity> findNormalInfo(@Param("status") Integer status);

	@Select("<script>"
			+ "select * from msg_notice where 1=1 "
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'title != null'> and title like '%${title}%'</if>"
            + "<if test = 'type != null'> and type = #{type}</if>"
            + "<if test = 'status != null'> and status = #{status}</if>"
			+ " order by setTime desc"
			+ "</script>")
	List<NoticeEntity> findList(NoticeEntity entity);

	
}
