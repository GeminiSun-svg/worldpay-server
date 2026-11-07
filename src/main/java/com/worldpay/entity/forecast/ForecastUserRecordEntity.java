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
@Schema(description = "")

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastUserRecordEntity extends PageQueryHelperEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "")

    private Integer id;

    @TableField("user_id")
    @Schema(description = "")

    private String userId;

    @TableField("user_name")
    @Schema(description = "")

    private String username;

    @TableField("polygon_address")
    @Schema(description = "")

    private String polygonAddress;

    @TableField("pol_proxyAddress")
    @Schema(description = "")

    private String polProxyAddress;

    @TableField("wallet_account")
    @Schema(description = "")

    private Double walletAccount;

    @TableField("user_type")
    @Schema(description = "")

    private Integer userType;

    @TableField("setTime")
    @Schema(description = "")

    private Date setTime;

    @TableField("gmtModified")
    @Schema(description = "")

    private Date gmtModified;
}
