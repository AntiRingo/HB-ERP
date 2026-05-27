package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationReceiveBatch;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 功能：操作 分批到货记录表
 * 作用：新增批次、查询批次、统计总到货数量
 */
public interface ApplicationReceiveBatchMapper {
    int insert(ApplicationReceiveBatch batch);                         // 新增批次
    List<ApplicationReceiveBatch> selectBatchByContentId(Integer contentId); // 查询所有批次
    BigDecimal sumReceiveNum(@Param("contentId") Integer contentId, @Param("isDeleted") Integer isDeleted); // 统计总数量
}