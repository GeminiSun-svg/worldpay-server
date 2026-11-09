package com.worldpay.api.response;


import com.worldpay.entity.wallet.WalletLogEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "充值统计响应")
public class StatBackEntity {

    @Schema(description = "金额")
	private Double amount;

    @Schema(description = "订单总数")
	private Integer totalOrderCount;

    @Schema(description = "充值用户数")
	private Integer rechargeUserCount;

    @Schema(description = "总出款金额")
	private BigDecimal totalOutAmount;

    @Schema(description = "总入款金额")
	private BigDecimal totalInAmount;

    @Schema(description = "待处理订单数")
	private Integer pendingOrderCount;

    @Schema(description = "已确认订单数")
	private Integer confirmedOrderCount;

    @Schema(description = "失败订单数")
	private Integer failedOrderCount;

    @Schema(description = "订单列表")
	private List<WalletLogEntity> orderList;

}
