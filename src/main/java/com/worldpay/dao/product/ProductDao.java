package com.worldpay.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.product.ProductEntity;

@Repository
public interface ProductDao extends BaseMapper<ProductEntity>{

	@Select("<script>"
			+ "select * from finance_product where 1=1 "
			+ "<if test = 'deleteState != null'> and delete_state = #{deleteState}</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "<if test = 'productName != null'> and product_name like '%${productName}%'</if>"
			+ "<if test = 'productTypeId != null'> and product_type_id = #{productTypeId}</if>"
			+ "<if test = 'fundsType != null'> and funds_type = #{fundsType}</if>"
			+ "<if test = 'productStateId != null'> and product_state_id = #{productStateId}</if>"
			+ "<if test = 'whetherCurrent != null'> and whether_current = #{whetherCurrent}</if>"
			+ "order by language"
			+ "</script>")
	List<ProductEntity> findList(ProductEntity entity);

}
