package com.worldpay.query;
import com.baomidou.mybatisplus.annotation.TableField;
import com.worldpay.constants.Constants;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * @category 分页条件
 * @author csz
 *
 */
@Data
@Schema(description = "")
public class PageQueryHelperEntity {
	
	@TableField(exist = false)
	@Schema(description = "")
private Integer pageNumber = Constants.PAGENUMBER;
	
	@TableField(exist = false)
	@Schema(description = "")
private Integer pageSize =Constants.PAGESIZE;

	
	
	

}
