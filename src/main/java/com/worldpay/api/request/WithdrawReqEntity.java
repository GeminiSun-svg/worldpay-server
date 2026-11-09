package com.worldpay.api.request;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @category 提现申请对象
 * @author Hlin
 *
 */
@Data
@Schema(description = "提现申请请求")
public class WithdrawReqEntity {
	
	@Schema(description = "区块链网络类型")
	private String chain;//网络类型
	
	@Schema(description = "提现订单编号")
	private String orderNo;//订单号
	
	@Schema(description = "提现金额数量")
	private String amount;//提现金额
	
	@Schema(description = "提现代币类型")
	private String coin;//代币类型
	
	@Schema(description = "提现用户ID")
	private String uid;//用户id
		
	@Schema(description = "提现钱包地址")
	private String address;//钱包地址
	

}
