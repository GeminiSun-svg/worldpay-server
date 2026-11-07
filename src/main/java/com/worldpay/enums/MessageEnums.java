package com.worldpay.enums;


/**
 * @category 通知消息
 * @author Hlin
 *
 */
public enum MessageEnums {
	//开卡成功通知
	SEND_CODE_ZH(1000, "zh","发送验证码","您的账户有新的验证活动{}。以下是你的 WorldPay 验证码。请注意，该验证码将在 10 分钟后过期，请尽快完成验证。{}WorldPay"),
	SEND_CODE_EN(1000, "en","Send verification code", "There is new verification activity on your account {}. Below is your WorldPay verification code. Please note that this code will expire in 10 minutes, so please complete the verification as soon as possible. {}WorldPay"),
	SEND_CODE_TW(1000, "tw","發送驗證碼", "您的帳戶有新的驗證活動{}。以下是你的 WorldPay 驗證碼。請注意，該驗證碼將在 10 分鐘後過期，請盡快完成驗證。 {}WorldPay"),
	//开卡成功通知
	OPEN_CARD_ZH(1001, "zh","银行卡开卡激活通知","银行卡开卡激活成功，银行卡号:{}"),
	OPEN_CARD_EN(1001, "en","Bank card activation notification", "Bank card activation successful, bank card number: {}"),
	OPEN_CARD_TW(1001, "tw","銀行卡開卡啟動通知", "銀行卡開卡啟動成功，銀行卡號:{}"),
	//充值到账通知
	TOP_UP_ZH(1002, "zh","充值到账通知","银行卡：{}充值到账成功，充值金额：{}，余额：{}"),
	TOP_UP_EN(1002, "en","Recharge arrival notification", "Bank card: {} recharge successfully received, recharge amount: {}, balance: {}"),
	TOP_UP_TW(1002, "tw","儲值到帳通知", "銀行卡：{}儲值到帳成功，儲值金額：{}，餘額：{}"),
	//银行卡消费通知
	CONSUME_ZH(1003, "zh","银行卡消费通知","您的银行卡：{}有一笔消费记录，消费金额：{},手续费：{}，余额：{}"),
	CONSUME_EN(1003, "en","Bank card consumption notification", "Your bank card: {} has a consumption record, consumption amount: {}, handling fee: {}, balance: {}"),
	CONSUME_TW(1003, "tw","金融卡消費通知", "您的銀行卡：{}有一筆消費紀錄，消費金額：{},手續費：{}，餘額：{}"),
	//注册成功通知
	REGISTER_ZH(1004, "zh","注册成功通知","您的账号{}已注册成功"),
	REGISTER_EN(1004, "en","Successful registration notification", "Your account {} has been successfully registered"),
	REGISTER_TW(1004, "tw","註冊成功通知", "您的帳號{}已註冊成功"),
	//注册成功通知
	LOGIN_ZH(1005, "zh","登录成功通知","您的账号 {} 检测到新的登录活动，登录设备：{},登录IP：{}。请确认是否为本人操作，如非本人操作，请及时修改密码以保障账户安全"),
	LOGIN_EN(1005, "en","Successful login notification", "Your account {} has detected a new login activity, login device: {}, login IP: {}. Please confirm whether it is your own operation. If not, please change your password in time to protect your account security."),
	LOGIN_TW(1005, "tw","登入成功通知", "您的帳號 {} 偵測到新的登入活動，登入裝置：{},登入IP：{}。請確認是否為本人操作，如非本人操作，請及時修改密碼以保障帳戶安全"),
	//登录密码变动通知
	LOGIN_PASSWORD_ZH(1006, "zh","登录密码变动通知","您的登录密码已修改成功，新密码：{}，请记录好您的登录密码"),
	LOGIN_PASSWORD_EN(1006, "en","Login password change notification", "Your login password has been changed successfully. New password: {}. Please record your login password."),
	LOGIN_PASSWORD_TW(1006, "tw","登入密碼變更通知", "您的登入密碼已修改成功，新密碼：{}，請記錄您的登入密碼"),
	//支付密码变动通知
	PAY_PASSWORD_ZH(1007, "zh","支付密码变动通知","您已成功设置支付密码，支付密码：{}，请记录好您的登录密码"),
	PAY_PASSWORD_EN(1007, "en","Payment password change notification", "You have successfully set your payment password. Payment password: {}. Please record your login password."),
	PAY_PASSWORD_TW(1007, "tw","支付密碼變動通知", "您已成功設定付款密碼，付款密碼：{}，請記錄您的登入密碼"),
	//银行卡申请通知
	APPLY_CARD_ZH(1008, "zh","银行卡开卡申请通知","您的开卡申请已提交审核，请耐心等待"),
	APPLY_CARD_EN(1008, "en","Bank card application notification", "Your card application has been submitted for review. Please wait patiently."),
	APPLY_CARD_TW(1008, "tw","銀行卡開卡申請通知", "您的開卡申請已提交審核，請耐心等待"),
	//钱包余额变动通知
	WALLET_ZH(1009, "zh","钱包余额变动通知","您的钱包有一笔新的交易，交易金额：{}，当前可用余额：{}，不可用余额：{}"),
	WALLET_EN(1009, "en","Wallet balance change notification", "Your wallet has a new transaction, transaction amount: {}, current available balance: {}, unavailable balance: {}"),
	WALLET_TW(1009, "tw","錢包餘額變動通知", "您的錢包有一筆新的交易，交易金額：{}，目前可用餘額：{}，不可用餘額：{}"),
	//kyc认证通过通知
	KYC_SUCCESS_ZH(1010, "zh","KYC认证成功通知","您的实体卡申请KYC认证已审核通过，您的卡片不久将会邮寄到你身边"),
	KYC_SUCCESS_EN(1010, "en","KYC certification success notification", "Your physical card application KYC certification has been reviewed and approved, and your card will be mailed to you soon"),
	KYC_SUCCESS_TW(1010, "tw","KYC認證成功通知", "您的實體卡申請KYC認證已審核通過，您的卡片不久後將會郵寄到您身邊"),
	//kyc认证失败通知
//	KYC_ERROR_ZH(1011, "zh","KYC认证失败通知","您的实体卡申请KYC认证未通过审核，认证失败原因：{}"),
//	KYC_ERROR_EN(1011, "en","KYC certification failure notification", "Your physical card application for KYC certification failed to pass the review. The reason for the failure is: {}"),
//	KYC_ERROR_TW(1011, "tw","KYC認證失敗通知", "您的實體卡申請KYC認證未通過審核，認證失敗原因：{}"),
	//实体卡分配通知
	CARD_ASSIGN_ZH(1012, "zh","银行卡分配通知","您申请的银行卡片已进行分配，银行卡号：{}，卡片不久将会邮寄到你身边"),
	CARD_ASSIGN_EN(1012, "en","Bank card allocation notification", "The bank card you applied for has been allocated. Bank card number: {}. The card will be mailed to you soon."),
	CARD_ASSIGN_TW(1012, "tw","金融卡分配通知", "您申請的銀行卡片已進行分配，銀行卡號：{}，卡片不久將會郵寄到你身邊"),
	//实体卡发货通知
	CARD_SHIPPING_ZH(1013, "zh","银行卡发货通知","您申请的银行卡片已发货，快递单号：{}，卡片不久将会邮寄到你身边"),
	CARD_SHIPPING_EN(1013, "en","Bank Card Shipping Notice", "The bank card you requested has been shipped. Tracking number: {}. The card will be mailed to you soon."),
	CARD_SHIPPING_TW(1013, "tw","銀行卡出貨通知", "您申請的銀行卡已出貨，快遞單號：{}，卡片不久將會郵寄到你身邊"),
	//银行卡申请驳回通知
	APPLY_CARD_ERROR_ZH(1014, "zh","银行卡开卡申请驳回通知","您提交的银行卡开卡申请，经审核，申请被驳回，驳回愿意：{}"),
	APPLY_CARD_ERROR_EN(1014, "en","Notice of rejection of bank card application", "The bank card application you submitted has been rejected after review. The reason for rejection is: {}"),
	APPLY_CARD_ERROR_TW(1014, "tw","銀行卡開卡申請駁回通知", "您提交的銀行卡開卡申請，經審核，申請被駁回，駁回願意：{}"),
	//用户交易验证码
	TRANSACTION_CODE_ZH(1015, "zh","用户交易验证码","您的交易验证码是：{}，该验证码仅用于本次交易，请勿泄露给他人。如非本人操作，请及时联系平台客服。"),
	TRANSACTION_CODE_EN(1015, "en","User transaction verification code", "Your transaction verification code is: {}. This verification code is only used for this transaction and should not be disclosed to others. If this is not your operation, please contact the platform customer service immediately."),
	TRANSACTION_CODE_TW(1015, "tw","用戶交易驗證碼", "您的交易驗證碼是：{}，此驗證碼僅用於本交易，請勿洩漏給他人。如非本人操作，請及時聯絡平台客服。"),
	//机器人开卡通知
	ROBOT_OPEN_CARD_ZH(1016, "zh","开卡审核","用户开卡申请通知，用户{}提交了{}开卡申请，请尽快前往后台进行审核处理"),
	ROBOT_OPEN_CARD_EN(1016, "en","Card opening review", "User card application notification: User {} submitted a card application for {}. Please go to the backend for review and processing as soon as possible."),
	ROBOT_OPEN_CARD_TW(1016, "tw","開卡審核", "用戶開卡申請通知，用戶{}提交了{}開卡申請，請盡快前往後台進行審核處理"),
	
	ROBOT_OPEN_CARD_BANK_ZH(1017, "zh","转账开卡审核","用户银行转账开卡申请通知，用户：{}提交了：{}开卡申请，用户使用银行卡进行转账处理，请进行金额核对后，尽快前往后台进行审核处理"),
	ROBOT_OPEN_CARD_BANK_EN(1017, "en","Transfer card opening review", "User bank transfer card application notification, user: {} submitted: {} card application, the user uses a bank card to transfer funds, please verify the amount and go to the backend for review and processing as soon as possible"),
	ROBOT_OPEN_CARD_BANK_TW(1017, "tw","轉帳開卡審核", "用戶銀行轉帳開卡申請通知，用戶：{}提交了：{}開卡申請，用戶使用銀行卡進行轉帳處理，請進行金額核對後，盡快前往後台進行審核處理"),
	
	//机器人充值通知
	ROBOT_TOP_UP_ZH(1018, "zh","银行卡充值","用户银行卡充值申请通知，用户：邮箱：{}，UID：{}，进行了银行卡充值转账，请尽快前往后台进行银行卡充值处理"),
	ROBOT_TOP_UP_EN(1018, "en","Bank card top-up", "User bank card recharge application notification, user: Email: {}, UID: {}, has made a bank card recharge transfer, please go to the backend as soon as possible to process the bank card recharge."),
	ROBOT_TOP_UP_TW(1018, "tw","金融卡儲值", "用戶銀行卡儲值申請通知，用戶：郵箱：{}，UID：{}，進行了銀行卡充值轉賬，請盡快前往後台進行銀行卡充值處理"),

	ROBOT_TOP_UP_WALLET_ZH(1019, "zh","钱包充值","用户钱包充值申请通知，用户：邮箱：{}，UID：{}，进行了银行卡充值转账，请尽快前往后台进行钱包充值处理"),
	ROBOT_TOP_UP_WALLET_EN(1019, "en","Wallet Recharge", "User wallet recharge application notification, user: Email: {}, UID: {}, has made a bank card recharge transfer, please go to the backend as soon as possible to process the wallet recharge"),
	ROBOT_TOP_UP_WALLET_TW(1019, "tw","錢包儲值", "用戶錢包儲值申請通知，用戶：郵箱：{}，UID：{}，進行了銀行卡充值轉賬，請盡快前往後台進行錢包充值處理"),

	//KYC认证提交通知
	KYC_SUBMIT_ZH(1020, "zh","KYC认证提交通知","您的kyc申请已提交，请耐心等待"),
	KYC_SUBMIT_EN(1020, "en","KYC certification submission notice", "Your KYC application has been submitted, please wait patiently"),
	KYC_SUBMIT_TW(1020, "tw","KYC認證提交通知", "您的kyc申請已提交，請耐心等待"),
	
	//kyc认证通过机器人消息
	ROBOT_KYC_SUCCESS_ZH(1021, "zh","KYC认证成功发货通知","用户kyc认证已通过；邮箱：{}，UID：{}，请尽快前往后台进行银行卡发货操作"),
	ROBOT_KYC_SUCCESS_EN(1021, "en","KYC certification successful delivery notification", "User kyc authentication has been passed; Email: {}, UID: {}, please go to the backend as soon as possible to perform bank card delivery operations"),
	ROBOT_KYC_SUCCESS_TW(1021, "tw","KYC認證成功出貨通知", "使用者kyc認證已通過；信箱：{}，UID：{}，請盡快前往後台進行銀行卡出貨操作"),
	//商户资产不足消息
	ROBOT_MERCHANT_ZH(1022, "zh","商户资产余额不足通知","商户账户余额不足。当前可用余额为 {} 元，本次用户操作需充值 {} 元。请尽快完成充值避免用户充值失败。"),
	ROBOT_MERCHANT_EN(1022, "en","Merchant Asset Balance Insufficient Notification", "The merchant's account balance is insufficient. The current available balance is {} yuan, and the user needs to recharge {} yuan for this transaction. Please complete the recharge as soon as possible to avoid recharge failure."),
	ROBOT_MERCHANT_TW(1022, "tw","商戶資產餘額不足通知", "商戶帳戶餘額不足。目前可用餘額為 {} 元，本次用戶操作需儲值 {} 元。請盡快完成儲值避免用戶儲值失敗。"),
	//商户充值记录
	ROBOT_RECHARGE_HISTORY_ZH(1023, "zh","商户余额充值通知","商户余额充值通知。充值到账金额：{}，交易哈希：{}，充值到账时间：{}。"),
	ROBOT_RECHARGE_HISTORY_EN(1023, "en","Merchant balance recharge notification", "Merchant balance recharge notification. Recharge amount received: {}, Recharge result: {}, Merchant's current available balance: {}, Frozen balance: {}."),
	ROBOT_RECHARGE_HISTORY_TW(1023, "tw","商家餘額儲值通知", "商戶餘額儲值通知。儲值到帳金額：{}，儲值結果 {} 元，商家目前可用餘額：{}，凍結餘額：{}。"),
	
	//申请关闭银行卡消息
	CARD_CLOSE_PROCESSING_ZH(1024, "zh","银行卡注销申请通知","您的银行卡：{}，注销申请已提交，请耐心等待。"),
	CARD_CLOSE_PROCESSING_EN(1024, "en","Bank card cancellation application notification", "Your bank card: {}, cancellation application has been submitted, please wait patiently."),
	CARD_CLOSE_PROCESSING_TW(1024, "tw","銀行卡註銷申請通知", "您的銀行卡：{}，註銷申請已提交，請耐心等待。"),
	//银行卡注销成功消息
	CARD_CLOSE_SUCCESS_ZH(1025, "zh","银行卡注销成功通知","您的银行卡：{}，已成功注销，银行卡余额已退回你的钱包账户，退回金额：{}。"),
	CARD_CLOSE_SUCCESS_EN(1025, "en","Bank card cancellation successful notification", "Your bank card: {} has been successfully cancelled. The bank card balance has been refunded to your wallet account. Refund amount: {}."),
	CARD_CLOSE_SUCCESS_TW(1025, "tw","銀行卡註銷成功通知", "您的銀行卡：{}，已成功註銷，銀行卡餘額已退回您的錢包帳戶，退回金額：{}。"),
	//钱包地址变更消息
	WALLET_RESET_ADDRES_ZH(1026, "zh","钱包地址变更通知","为保障您的资金账户安全，您的钱包地址由：{}，变更为：{}，请妥善保管新钱包地址，切勿将资金充值到原始地址！！！"),
	WALLET_RESET_ADDRES_EN(1026, "en","Wallet address change notification", "To ensure the security of your funds, your wallet address has changed from {} to {}. Please keep your new wallet address safe and never deposit funds into the original address!"),
	WALLET_RESET_ADDRES_TW(1026, "tw","錢包地址變更通知", "為確保您的資金帳戶安全，您的錢包地址由：{}，變更為：{}，請妥善保管新錢包地址，切勿將資金充值到原始地址！ ！ ！"),
	//银行卡密码变更通知
	CARD_RESET_PIN_ZH(1027, "zh","银行卡支付密码变更通知","您的银行卡：{}，支付密码设置成功，请妥善保管您的银行卡密码"),
	CARD_RESET_PIN_EN(1027, "en","Bank card payment password change notification", "Your bank card: {}, payment password successfully set. Please keep your bank card password safe."),
	CARD_RESET_PIN_TW(1027, "tw","銀行卡支付密碼變更通知", "您的銀行卡：{}，付款密碼設定成功，請妥善保管您的銀行卡密碼"),
	//佣金收入通知
	BROKERAGE_INCOME_ZH(1028, "zh","佣金收入通知","您有一笔新的佣金收入，佣金金额：{}，来源用户：{}"),
	BROKERAGE_INCOME_EN(1028, "en","Commission income notification", "You have received a new commission payment. Commission amount: {}, Source user: {}"),
	BROKERAGE_INCOME_TW(1028, "tw","佣金收入通知", "您有一筆新的佣金收入，佣金金額：{}，來源用戶：{}"),
	//钱包充值订单审核拒绝通知
	WALLET_TOPUP_ZH(1029, "zh","钱包充值订单审核拒绝通知","您提交的钱包充值订单申请，经审核，申请被驳回，驳回愿意：{}"),
	WALLET_TOPUP_EN(1029, "en","Wallet top-up order rejection notification", "Your wallet top-up order application has been rejected after review. Reasons for rejection: {}"),
	WALLET_TOPUP_TW(1029, "tw","Wallet top-up order rejection notification", "您提交的錢包充值訂單申請，經審核，申請被駁回，駁回願意：{}"),

	WALLET_NETWORK_TOPUP_ZH(1030, "zh","钱包充值申请通知","用户钱包充值申请通知，用户：邮箱：{}，UID：{}，进行了:{}充值转账，订单编号：{}，请尽快前往后台进行钱包充值处理"),
	WALLET_NETWORK_TOPUP_EN(1030, "en","Wallet top-up application notification", "Notification of user wallet top-up request: User: Email: {}, UID: {}, made a top-up transfer of {}, order number: {}. Please go to the backend to process the wallet top-up as soon as possible."),
	WALLET_NETWORK_TOPUP_TW(1030, "tw","錢包儲值申請通知", "用戶錢包儲值申請通知，用戶：郵箱：{}，UID：{}，進行了:{}充值轉賬，訂單編號：{}，請盡快前往後台進行錢包充值處理"),

	APP_CREATE_APPLICATION_RECORD_ZH(1031, "zh","渠道商申请审批结果通知","渠道商申请审批结果通知，用户邮箱：{}，uid：{}，渠道商等级:{},谷歌私钥：{},申请通过"),
	APP_CREATE_APPLICATION_RECORD_EN(1031, "en","Channel Partner Application Approval Result Notification", "Channel Partner Application Approval Result Notification, UserEmail: {}, UID: {}, Channel Partner Level: {}, Google's private key: {}, Application Approved."),
	APP_CREATE_APPLICATION_RECORD_TW(1031, "tw","渠道商申請審批結果通知", "渠道商申請審批結果通知，用戶郵箱：{}，UID：{}，渠道商等級:{}，谷歌私鑰：{}，申請通過"),

	APP_APPLICATION_RECORD_ZH(1032, "zh","渠道商申请审批结果通知","渠道商申请审批结果通知，邮箱：{}，UID：{}，申请驳回"),
	APP_APPLICATION_RECORD_EN(1032, "en","Channel Partner Application Approval Result Notification", "Channel Partner Application Approval Result Notification, Email: {}, UID: {}, Application rejected."),
	APP_APPLICATION_RECORD_TW(1032, "tw","渠道商申請審批結果通知", "渠道商申請審批結果通知，郵箱：{}，UID：{}，申請驳回"),

	FORECAST_AMOUNT_ZH(1033, "zh","预测账户变更通知","预测账户变更通知，邮箱：{}，UID：{}，预测{}金额：{}"),
	FORECAST_AMOUNT_EN(1033, "en","Predicted Account Change Notification", "Predicted Account Change Notification, Email: {}, UID: {}, Predicted {} Amount: {}"),
	FORECAST_AMOUNT_TW(1033, "tw","預測账户變更通知", "預測账户變更通知，郵箱：{}，UID：{}，預測{}金額：{}"),
	
	// 钱包提现成功消息
	WALLET_WITHDRAWAL_SUCCESS_ZH(1034, "zh", "钱包提现到账通知", "您的钱包提现申请已完成，到账金额{}，接收钱包地址：{}"),
	
	// 提现审核拒绝通知
	WALLET_WITHDRAWAL_ERROR_ZH(1035, "zh", "钱包提现申请拒绝通知", "您的钱包提现申已拒绝，钱包地址：{}，拒绝原因：{}"),

	// 机器人提现审核通知
	ROBOT_WITHDRAWAL_REVIEW_ZH(1036, "zh", "提现审核通知", "用户：{}，进行了提现申请，请及时登录后台进行操作审核。"),
	
	ROBOT_KYC_ERROR_ZH(1011, "zh","KYC认证失败通知","用户：{}，KYC认证未通过审核，请前往管理端重新提交,认证失败原因：{}"),

	ROBOT_KYC_NULL_ZH(1037, "zh","KYC认证提交失败通知","用户：{}，KYC认证未提交成功，请前往管理端重新提交，提交失败原因：{}");
	
	private Integer index;

	private String language;
	
	private String title;

	private String msg;

	private MessageEnums(Integer index, String language, String title, String msg) {
		this.index = index;
		this.language = language;
		this.title = title;
		this.msg = msg;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
