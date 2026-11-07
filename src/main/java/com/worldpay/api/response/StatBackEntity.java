package com.worldpay.api.response;


import com.worldpay.entity.wallet.WalletLogEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StatBackEntity {

    @Schema(description = "")
private Double amount;

    @Schema(description = "")
private Integer totalOrderCount;

    @Schema(description = "")
private Integer rechargeUserCount;

    @Schema(description = "")
private BigDecimal totalOutAmount;

    @Schema(description = "")
private BigDecimal totalInAmount;

    @Schema(description = "")
private Integer pendingOrderCount;

    @Schema(description = "")
private Integer confirmedOrderCount;

    @Schema(description = "")
private Integer failedOrderCount;

    @Schema(description = "")
private List<WalletLogEntity> orderList;

}
