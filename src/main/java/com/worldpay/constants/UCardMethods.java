package com.worldpay.constants;

/**
 * worldPay 聚合三方接口路径（仅保留新文档接口，不兼容旧 openapi）
 * 文档：https://jf86pwbggg.apifox.cn/
 */
public interface UCardMethods {

    /** 查询商户可用卡产品列表 GET */
    String MERCHANT_BANKCARD_TEMPLATE_LIST = "/api/bankcard/merchant/card/list";

    /** 用户注册 POST */
    String USER_REGISTER = "/api/user/register";

    /** 申请银行卡 POST */
    String APPLY_BANKCARD = "/api/bankcard/apply";

    /** 银行卡充值 POST */
    String RECHARGE_BANKCARD = "/api/bankcard/recharge";

    /** 设置 Pin POST */
    String SET_BANKCARD_PIN = "/api/bankcard/setPin";

    /** 查询银行卡余额 POST */
    String QUERY_BANKCARD_BALANCE = "/api/bankcard/getBalance";

    /** 查询银行卡信息 POST */
    String BANKCARD_INFO = "/api/bankcard/info";

    /** 商户资产 GET */
    String MERCHANT_ASSET = "/api/merchant/balance";

    /** 更新银行卡状态 POST */
    String UPDATE_CARD_STATUS = "/api/bankcard/update/status";

    /** 注销银行卡 POST */
    String CLOSE_CARD = "/api/bankcard/close";

    /** 提交 KYC POST */
    String USER_KYC_APPLY = "/api/user/kyc/apply";

    /** 查询 KYC 状态 GET */
    String USER_KYC_STATUS = "/api/user/kyc/status";

    /** 银行卡激活 POST */
    String BANKCARD_ACTIVE = "/api/bankcard/active";

    /** 文件上传 POST */
    String UPLOAD_FILE = "/api/file/upload";
}
