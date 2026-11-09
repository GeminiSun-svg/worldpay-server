package com.worldpay.api.request;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @category 获取钱包充值地址入参
 * @author Hlin
 *
 */
@Data
@Schema(description = "获取钱包充值地址请求")
public class GetWalletAddressEntity {
    @Schema(description = "钱包用户ID")
    private String uid;

    @Schema(description = "用户邮箱地址")
    private String email;

    @Schema(description = "接口请求签名")
    private String sign;
}
