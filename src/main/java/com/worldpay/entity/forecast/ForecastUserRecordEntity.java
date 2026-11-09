package com.worldpay.entity.forecast;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.worldpay.query.PageQueryHelperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 类描述：预测用户记录表
 *
 * @author GeminiSun
 * @date 2026/3/9 13:54
 */
@Data
@TableName("forecast_user_record")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "预测用户记录")

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastUserRecordEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")

    private Integer id;

    @TableField("user_id")
    @Schema(description = "用户ID")

    private String userId;

    @TableField("user_name")
    @Schema(description = "用户名")

    private String username;

    @TableField("polygon_address")
    @Schema(description = "Polygon地址")

    private String polygonAddress;

    @TableField("pol_proxyAddress")
    @Schema(description = "POL代理地址")

    private String polProxyAddress;

    @TableField("wallet_account")
    @Schema(description = "钱包余额")

    private Double walletAccount;

    @TableField("user_type")
    @Schema(description = "用户类型")

    private Integer userType;

    @TableField("setTime")
    @Schema(description = "创建时间")

    private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "修改时间")

    private Date gmtModified;
}
