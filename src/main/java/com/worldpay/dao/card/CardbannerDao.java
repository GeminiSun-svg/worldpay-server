package com.worldpay.dao.card;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.card.CardBannerEntity;

@Repository
public interface CardbannerDao extends BaseMapper<CardBannerEntity>{

	@Select("select * from card_banner where id = #{id}")
	List<CardBannerEntity> findById(@Param("id") String id);

    @Select("<script>"
            + "select * from card_banner where 1=1"
            + "<if test = 'title != null'> and title like '%${title}%'</if>"
            + "<if test = 'language != null'> and language = #{language}</if>"
            + "</script>")
    List<CardBannerEntity> findList(CardBannerEntity entity);

    @Select("select * from card_banner where status = #{status}")
    List<CardBannerEntity> findNormalInfo(@Param("status") Integer status);

}
