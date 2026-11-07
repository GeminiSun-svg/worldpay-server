package com.worldpay.dao.merchant;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.merchant.RechargeHistoryEntity;

@Repository
public interface RechargeHistoryDao extends BaseMapper<RechargeHistoryEntity>{

	@Select("<script>"
			+ "select * from merchants_recharge_history where 1=1 "
			+ "<if test = 'begTime != null'> and date_format(setTime, '%Y-%m-%d') &gt;= #{begTime}</if>"
			+ "<if test = 'endTime != null'> and date_format(setTime, '%Y-%m-%d') &lt;= #{endTime}</if>"
			+ "group by orderNo"
			+ "</script>")
	List<RechargeHistoryEntity> findList(RechargeHistoryEntity entity);

	@Select("select * from merchants_recharge_history where orderNo = #{orderNo} limit 1")
	RechargeHistoryEntity findOrderNo(@Param("orderNo") String orderNo);

}
