package com.worldpay.dao.wallet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.wallet.WithdrawalRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 类描述：提现数据处理
 *
 * @author GeminiSun
 * @date 2026/05/19 16:12
 */
@Repository
public interface WithdrawalRecordDao extends BaseMapper<WithdrawalRecordEntity> {
}
