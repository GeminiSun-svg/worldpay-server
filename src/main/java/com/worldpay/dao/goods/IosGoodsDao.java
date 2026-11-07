package com.worldpay.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.goods.IosGoodsEntity;

@Repository
public interface IosGoodsDao extends BaseMapper<IosGoodsEntity>{

	@Select("select * from ios_goods where goods_num = #{num}")
	IosGoodsEntity findByNum(@Param("num") String num);

	@Select("<script>"
			+ "select * from ios_goods where 1=1 "
			+ "<if test ='goodsName != null'> and goods_name = #{goodsName}</if>"
			+ "<if test ='goodsNum != null'> and goods_num = #{goodsNum}</if>"
			+ "</script>")
	List<IosGoodsEntity> findList(IosGoodsEntity entity);

}
