package com.worldpay.dao.wallet;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.wallet.WalletToWebLogEntity;

@Repository
public interface WalletToWebLogDao extends BaseMapper<WalletToWebLogEntity>{

	@Select("select * from wallet_to_web_log where order_no = #{requestOrderNum}")
	WalletToWebLogEntity findOrderNum(@Param("requestOrderNum") String requestOrderNum);

	@Select("<script>"
			+ "select * from wallet_to_web_log where 1=1"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'orderNo != null'> and order_no = #{orderNo}</if>"
			+ "<if test = 'userEmail != null'> and user_email = #{userEmail}</if>"
			+ "<if test = 'networkType != null'> and network_type = #{networkType}</if>"
			+ "<if test = 'walletAddress != null'> and wallet_address = #{walletAddress}</if>"
			+ "<if test = 'applyState != null'> and apply_state = #{applyState}</if>"
			+ "order by setTime desc"
			+ "</script>")
	List<WalletToWebLogEntity> findList(WalletToWebLogEntity entity);

}
