package com.worldpay.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.product.ProductTypeEntity;

@Repository
public interface ProductTypeDao extends BaseMapper<ProductTypeEntity>{

	@Select("<script>"
			+ "select * from finance_product_type where 1=1 "
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "<if test = 'name != null'> and name = #{name}</if>"
			+ "<if test = 'whetherCurrent != null'> and whether_current = #{whetherCurrent}</if>"
			+ "</script>")
	List<ProductTypeEntity> findList(ProductTypeEntity entity);

}
