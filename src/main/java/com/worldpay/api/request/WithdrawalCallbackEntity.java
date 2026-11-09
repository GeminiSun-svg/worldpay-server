package com.worldpay.api.request;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @category 提现回调
 * @author Hlin
 *
 */
@Data
@Schema(description = "提现回调请求")
public class WithdrawalCallbackEntity {
	
	@Schema(description = "链上交易哈希")
	private String txHash;

	@Schema(description = "外部系统订单号")
	private String outOrderNo;
	
	@Schema(description = "平台提现订单号")
	private String orderNo;
	
	@Schema(description = "提现回调状态")
	private String status;
	
    @Schema(description = "提现钱包地址")
    private String address;
    
    @Schema(description = "区块链网络类型")
    private String chain;
    
    @Schema(description = "接口请求签名")
    private String sign;
}
