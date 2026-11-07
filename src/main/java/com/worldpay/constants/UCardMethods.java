package com.worldpay.constants;

public interface UCardMethods {

    // system status
    public static String SYS_CLOCK = "/sys/clock";

    // 银行卡信息列表
    public static String BANKCARD_TEMPLATE_LIST = "/bankcard/template/list";
    
    public static String MERCHANT_BANKCARD_TEMPLATE_LIST = "/bankcard/merchant/template/list";

    // 用户注册
    public static String USER_REGISTER = "/user/register";

    // 申请银行卡
    public static String APPLY_BANKCARD = "/bankcard/apply";
    

    // 银行卡充值
    public static String RECHARGE_BANKCARD = "/bankcard/recharge";

    // set card atm pin
    public static String SET_BANKCARD_PIN = "/bankcard/setPin";

    public static String GET_PIN = "/bankcard/getPin";

    // query bankcard transactions
    public static String QUERY_BANKCARD_TRANSACTIONS = "/bankcard/transactions";

    // 查询银行卡余额
    public static String QUERY_BANKCARD_BALANCE = "/bankcard/balance";

    // 查询银行卡信息
//    public static String QUERY_BANKCARD_INFO = "/bankcard/cardInfo";
    
    public static String BANKCARD_INFO = "/bankcard/info";

    // 商户资产信息
    public static String MERCHANT_ASSET = "/merchant/balance";
    
    public static String MERCHANT_BALANCE = "/merchant/balance";

    public static String MERCHANT_RECHARGE_INFO = "/merchant/rechargeInfo";

    public static String MERCHANT_HISTORY_LOGS = "/merchant/HistoryLogs";

    public static String MERCHANT_RECHARGE_RECORDS = "/merchant/rechargeList";

    public static String ACTIVE_CARDS ="/bankcard/activatecard";

    // update bankcard status
    public static String UPDATE_CARD_STATUS = "/bankcard/update/status";

    // close bankcard
    public static String CLOSE_CARD = "/bankcard/close";

    public static String CARD_ORDER = "/bankcard/cardOrderInfo";

    public static String SET_USER_INFO = "/user/setUserInfo";

    public static String KYC_CHECK = "/user/kycCheck";

    public static String KYC_STATUS = "/user/kycStatus";

    public static String APPROVE_3DSAUTH = "/bankcard/approve/3dsAuth";

    public static String REJECT_3DSAUTH = "/bankcard/reject/3dsAuth";

    public static String QUERY_3DSAUTH = "/bankcard/query/3dsAuth";

    public static String USD_TO_EUR = "/bankcard/getEURAmount";

    public static String USD_RECHARGE = "/user/usdRechargeInfo";



    /*************UCards***************/
    //kyc申请
    public static String UCARD_KYC_APPLY = "/UCard/kyc/apply";
    
    public static String USER_KYC_APPLY = "/user/kyc/apply";
    
    //kyc认证状态查询
    public static String UCARD_KYC_STATUS = "/UCard/kyc/status";
    
    public static String USER_KYC_STATUS = "/user/kyc/status";
    
    
    
    //实体卡分配
    public static String UCARD_ASSIGN_CARD = "/UCard/assign";

    public static String UCARD_ACTIVATE_CARD = "/UCard/active";
    
    //实体卡激活
    public static String BANKCARD_ACTIVE = "/bankcard/active";

    //文件上传
    public static String UCARD_UPLOAD_FILE = "/upload/uCardFile";
    
    public static String UPLOAD_FILE = "/upload/file";

    public static String UPDATE_NO_PIN_PAYMENT = "/bankcard/update/noPinAmount";



    /**************Wholder***************/
    public static String SETWHolderInfo = "/user/setWHolderInfo";

    public static String UPDATE_W_HOLDER_INFO = "/user/updateWHolderInfo";

    /*************WSCards***************/
    public static String WSCARD_LIST_REGION = "/WSCard/holder/region/list";

    public static String WSCARD_LIST_CITY = "/WSCard/holder/city/list";

    public static String WSCARD_MOBILE_AREA = "/WSCard/holder/mobile/area/list";


    /**************WS***************/
    public static String CREATE_WS_CARDHOLDER = "/WSCard/holder/apply";

    public static String UPDATE_WS_CARDHOLDER = "/WSCard/holder/update";

    public static String QUERY_WS_CARDHOLDER = "/WSCard/holder/query";

    /**************SCards***************/
    public static String SCARD_ADD_EMAIL = "/SCard/add/email";

    public static String SCARD_UPDATE_EMAIL = "/SCard/update/email";

    public static String SCARD_GET_EMAIL = "/SCard/get/email";

    public static String SCARD_DELETE_EMAIL = "/SCard/delete/email";

    /**************SRCards***************/
    public static String SRCARD_ADD_EMAIL = "/SRCard/add/email";

    public static String SRCARD_UPDATE_EMAIL = "/SRCard/update/email";

    public static String SRCARD_GET_EMAIL = "/SRCard/get/email";

    public static String SRCARD_DELETE_EMAIL = "/SRCard/delete/email";

    /**************实体卡申请相关***************/
    public static String CARD_DELIVERY_REGION = "/bankcard/ph/delivery/region";
    public static String CARD_DELIVERY_ADD = "/bankcard/ph/deliveryAddress/add";
    public static String CARD_DELIVERY_UPDATE = "/bankcard/ph/deliveryAddress/update";
    public static String APPLY_BANKCARD_V2 = "/bankcard/apply/v2";
    public static String DELIVERY_LIST = "/bankcard/delivery/list";

    public static String REMIT_ORDER_CREATE = "/remit/order/create";
    public static String REMIT_ORDER_GET = "/remit/order/getByOrderNo";
    public static String REMIT_ORDER_FREEZE_ACCOUNT = "/remit/order/freezeAccountByOrderNo";
    public static String REMIT_ORDER_NOTIFY = "/remit/order/remitResultNotify";
    
    //用于查询邮寄地区列表
    public static String DELIVERY_REGION = "/delivery/region";
    
    //添加邮寄地址
    public static String DELIVERY_ADDRESS_ADD = "/delivery/address/add";
    
    public static String DELIVERY_ADDRESS_UPDATE = "/delivery/address/update";

    

}
