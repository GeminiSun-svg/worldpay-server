package com.worldpay.dao.record;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.api.request.ApplicationRecordReqEntity;
import com.worldpay.entity.appuser.ApplicationRecordEntity;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 类描述： 申请记录
 *
 * @author GeminiSun
 * @date 2026/2/4 10:17
 */
@Repository
public interface ApplicationRecordDao extends BaseMapper<ApplicationRecordEntity> {

    @Update("update application_record set status = #{status},gmtModified = NOW() where id = #{id}")
    void updateStatusById(ApplicationRecordReqEntity entity);

    @Update("update application_record set status = #{status},channel_id = #{channelId},channel_uid = #{channelUid},gmtModified = NOW() where id = #{id}")
    void updateApplicationById(ApplicationRecordReqEntity entity);
}
