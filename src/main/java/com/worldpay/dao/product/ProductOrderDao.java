package com.worldpay.dao.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.product.ProductOrderEntity;

@Repository
public interface ProductOrderDao extends BaseMapper<ProductOrderEntity>{

	@Select("select ifnull(sum(order_monery),0) from finance_product_order where uid = #{uid} and order_state = #{orderState}")
	Double findUserSum(@Param("uid") String uid,@Param("orderState") String orderState);

	@Select("<script>"
			+ "select * from finance_product_order where 1=1 "
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'productId != null'> and product_id = #{productId}</if>"
			+ "<if test = 'orderNum != null'> and order_num = #{orderNum}</if>"
			+ "<if test = 'orderState != null'> and order_state = #{orderState}</if>"
			+ "<if test = 'productType != null'> and product_type = #{productType}</if>"
			+ "<if test = 'fundsType != null'> and funds_type = #{fundsType}</if>"
			+ "<if test = 'whetherCurrent != null'> and whether_current = #{whetherCurrent}</if>"
			+ " order by pay_time desc"
			+ "</script>")
	List<ProductOrderEntity> findList(ProductOrderEntity entity);

	@Select("select * from finance_product_order where order_state = #{orderState} "
			+ "and whether_current = #{whetherCurrent} and expire_time <= #{expireTime}")
	List<ProductOrderEntity> findExpireOrder(@Param("orderState") String orderState,
			@Param("whetherCurrent") Integer whetherCurrent,@Param("expireTime") Date expireTime);

}
