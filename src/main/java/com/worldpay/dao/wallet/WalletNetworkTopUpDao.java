package com.worldpay.dao.wallet;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.wallet.WalletNetworkTopUpEntity;

@Repository
public interface WalletNetworkTopUpDao extends BaseMapper<WalletNetworkTopUpEntity>{

	@Select("<script>"
			+ "select * from wallet_network_topup where 1=1"
			+ "<if test = 'uid != null'> and uid = #{uid}</if>"
			+ "<if test = 'userEmail != null'> and user_email = #{userEmail}</if>"
			+ "<if test = 'userTel != null'> and user_tel = #{userTel}</if>"
			+ "<if test = 'orderNo != null'> and order_no = #{orderNo}</if>"
			+ "<if test = 'networkType != null'> and network_type = #{networkType}</if>"
			+ "<if test = 'walletAddress != null'> and wallet_address = #{walletAddress}</if>"
			+ "<if test = 'orderState != null'> and order_state = #{orderState}</if> "
			+ "order by order_state,setTime desc"
			+ "</script>")
	List<WalletNetworkTopUpEntity> findList(WalletNetworkTopUpEntity entity);

}
