package com.worldpay.dao.brokerage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.worldpay.entity.brokerage.ChannelLevelEntity;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelLevelDao extends BaseMapper<ChannelLevelEntity> {

    @Update("update channel_level set delete_state = 0,gmtModified = NOW() where id = #{id}")
    void updateDeleteStatusById(@Param("id") int id);

    @Update("update channel_level set apply_state = #{applyState},gmtModified = NOW() where id = #{id}")
    void updateApplyStatusById(@Param("id")int id, @Param("applyState") int applyState);

    @Select("select * from channel_level cl left join channel_user cu on cl.level_no = cu.level_no where cu.user_id = #{uid}")
    ChannelLevelEntity findChannelLevel(@Param("uid") String uid);

   
	@Select("select ifnull(directly_rates_fee,0) from channel_level where level_no = #{levelNo}")
	BigDecimal findRate(@Param("levelNo") Integer levelNo);
}
