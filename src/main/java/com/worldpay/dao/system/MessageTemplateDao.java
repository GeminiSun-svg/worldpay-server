package com.worldpay.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.system.MessageTemplateEntity;

@Repository
public interface MessageTemplateDao extends BaseMapper<MessageTemplateEntity>{

	@Select("select * from message_template where message_code = #{messageCode} and language = #{language}")
	MessageTemplateEntity findOne(@Param("messageCode") Integer messageCode,@Param("language") String language);

	@Select("<script>"
			+ "select * from message_template where 1=1"
			+ "<if test = 'messageCode != null'> and message_code = #{messageCode}</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "</script>")
	List<MessageTemplateEntity> findList(MessageTemplateEntity entity);

}
