package com.worldpay.api.request;
import lombok.Data;

/**
 * @category 获取钱包充值地址入参
 * @author Hlin
 *
 */
@Data
public class GetWalletAddressEntity {
    private String uid;

    private String email;

    private String sign;
}
