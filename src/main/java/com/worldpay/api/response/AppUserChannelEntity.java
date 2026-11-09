package com.worldpay.api.response;
import java.util.Date;
import com.worldpay.query.PageQueryHelperEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "App端渠道绑定信息响应")
public class AppUserChannelEntity extends PageQueryHelperEntity{
	
	@Schema(description = "主键ID")
	private Integer id;
	
	@Schema(description = "用户ID")
	private String uid;
	
	@Schema(description = "登录密码")
	private String userPassword;
	
	@Schema(description = "支付密码")
	private String payPassword;
	
	@Schema(description = "用户邮箱")
	private String userEmail;
	
	@Schema(description = "手机号")
	private String mobileNumber;
	
	@Schema(description = "手机号国际区号")
	private String mobilePrefix;
	
	@Schema(description = "钱包状态")
	private Boolean walletState;
	
	@Schema(description = "谷歌验证密钥")
	private String googleSecretkey;
	
	@Schema(description = "用户状态")
	private Integer userState;
	
	@Schema(description = "邀请码")
	private String invitationCode;
	
	@Schema(description = "钱包余额")
	private Double walletBalance;
	
	@Schema(description = "冻结余额")
	private Double freezeBalance;
	
	@Schema(description = "开卡冻结余额")
	private Double openFreezeBalance;
	
	@Schema(description = "推送注册ID")
	private String registrationId;

	@Schema(description = "TRON链USDT地址")
	private String tronUsdtAcount;
	
	@Schema(description = "创建时间")
	private Date setTime;
	
	@Schema(description = "修改时间")
	private Date gmtModified;
	
	@Schema(description = "KYC认证状态")
	private Integer kycState;
	
	@Schema(description = "KYC认证状态名称")
	private String kycStateName;
	
	@Schema(description = "KYC审核结果备注")
	private String kycAuditResult;
	
	@Schema(description = "客户端ID")
	private String cid;
	
	@Schema(description = "邮箱验证码")
	private String emailCode;
	
	@Schema(description = "谷歌验证码")
	private String googleCode;
	
	@Schema(description = "谷歌验证绑定状态")
	private boolean googleState;
	
	@Schema(description = "设备名称")
	private String deviceName;

	@Schema(description = "支付密码设置状态")
	private boolean payPasswordState;
	
	@Schema(description = "邀请人的邀请码")
	private String toInvitationCode;
	
	@Schema(description = "邀请人类型标签")
	private Integer invitationLable;

	@Schema(description = "上级渠道商名称")
	private String superName;
	

}
