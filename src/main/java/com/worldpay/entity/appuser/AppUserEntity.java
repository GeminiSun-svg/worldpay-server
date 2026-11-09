package com.worldpay.entity.appuser;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * App 用户实体
 * 对应旧表: app_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_user")
@Schema(description = "App用户")
public class AppUserEntity extends PageQueryHelperEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Integer id;

    /**
     * 三方 UID（UCard 返回）
     */
    @Schema(description = "用户ID")
    private String uid;

    /**
     * 用户邮箱（登录账号）
     */
    @Schema(description = "用户邮箱")
    private String userEmail;

    /**
     * 登录密码（MD5 哈希，兼容旧版）
     */
    @Schema(description = "登录密码")
    private String userPassword;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobileNumber;

    /**
     * 手机区号
     */
    @Schema(description = "手机区号")
    private String mobilePrefix;

    /**
     * 用户状态：1-正常，其他-冻结
     */
    @Schema(description = "用户状态")
    private Integer userState;

    /**
     * 支付密码（MD5 哈希）
     */
    @Schema(description = "支付密码")
    private String payPassword;

    /**
     * Google Authenticator 密钥
     */
    @Schema(description = "谷歌验证密钥")
    private String googleSecretkey;

    /**
     * KYC 状态
     */
    @Schema(description = "KYC状态")
    private Integer kycState;

    /**
     * KYC 状态名称
     */
    @Schema(description = "KYC状态名称")
    private String kycStateName;

    /**
     * kyc申请结果描述
     */
    @Schema(description = "KYC审核结果")
    private String kycAuditResult;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    private String invitationCode;

    /**
     * 被邀请人的邀请码
     */
    @TableField(exist = false)
    @Schema(description = "邀请人邀请码")
    private String toInvitationCode;

    /**
     * 钱包可用余额
     */
    @Schema(description = "钱包余额")
    private Double walletBalance;

    /**
     * 冻结资产
     */
    @Schema(description = "冻结余额")
    private Double freezeBalance;

    /**
     * 开卡临时冻结
     */
    @Schema(description = "开卡冻结金额")
    private Double openFreezeBalance;

    /**
     * 可提现余额（佣金）
     */
    @Schema(description = "可提现余额")
    private Double withdrawableBalance;

    /**
     * 钱包入口是否开启
     */
    @Schema(description = "钱包入口状态")
    private Boolean walletState;

    /**
     * Jpush三方id
     */
    @Schema(description = "推送注册ID")
    private String registrationId;

    /**
     * tron链usdt充值地址
     */
    @Schema(description = "TRON充值地址")
    private String tronUsdtAcount;

    /**
     * 推送设备 ID
     */
    @Schema(description = "推送设备ID")
    private String cid;

    /**
     * 登录设备名称
     */
    @TableField(exist = false)
    @Schema(description = "登录设备名称")
    private String deviceName;

    /**
     * 创建时间（兼容旧字段 setTime）
     */
    @TableField(value = "setTime", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date setTime;

    /**
     * 修改时间（兼容旧字段 gmtModified）
     */
    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private Date gmtModified;

    // ========== 非数据库字段 ==========

    /**
     * 前端判断是否设置了 Google 验证
     */
    @TableField(exist = false)
    @Schema(description = "谷歌验证状态")
    private Boolean googleState;

    /**
     * 谷歌验证码
     */
    @TableField(exist = false)
    @Schema(description = "谷歌验证码")
    private String googleCode;

    /**
     * 前端判断是否设置了支付密码
     */
    @TableField(exist = false)
    @Schema(description = "支付密码状态")
    private Boolean payPasswordState;

    /**
     * 邀请人标签
     */
    @TableField(exist = false)
    @Schema(description = "邀请人标签")
    private Integer invitationLable;

    /**
     * 是否已绑定渠道商
     */
    @TableField(exist = false)
    @Schema(description = "是否绑定渠道商")
    private Boolean bandingChannel;

    /**
     * 绑定的渠道商 UID
     */
    @TableField(exist = false)
    @Schema(description = "绑定渠道商UID")
    private String bandingChannelUid;

    /**
     * iOS 钱包入口状态
     */
    @TableField(exist = false)
    @Schema(description = "iOS钱包状态")
    private Integer iosWalledState;

    /**
     * iOS USDT 充值入口状态
     */
    @TableField(exist = false)
    @Schema(description = "iOS充值状态")
    private Integer iosTopUpState;

    /**
     * 邮箱验证码（非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "邮箱验证码")
    private String emailCode;
}
