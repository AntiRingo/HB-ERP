package com.hongbang.service;

import com.hongbang.pojo.Inspection;

import java.util.List;
import java.util.Map;

public interface InspectionService {
    //添加
    void add(List<Inspection> inspection);

    //通过或者不通过
    void updatePass(Inspection inspection);

    //已经质检过的质检单查询质检信息
    List<Inspection> selectZjInformation(int appId);





    /**
     * 获取质检记录
     */
    Map<String, Object> getMaterialQCInspectionRecords(Map<String, Object> params);

    /**
     * 获取质检状态
     */
    Map<String, Object> getMaterialQCInspectionStatus(Map<String, Object> params);

    /**
     * 获取可申请质检数量
     */
    Map<String, Object> getAvailableQCQuantity(Map<String, Object> params);

    /**
     * 获取已申请质检数量
     */
    Map<String, Object> getQCRequestedQuantity(Map<String, Object> params);

    /**
     * 获取质检申请单列表
     */
    Map<String, Object> getQCApplications(Map<String, Object> params);

    /**
     * 获取质检申请单详情
     */
    Map<String, Object> getQCApplicationDetail(Map<String, Object> params);

    /**
     * 更新质检申请单状态
     */
    Map<String, Object> updateQCApplicationStatus(Map<String, Object> params);

    /**
     * 获取质检进度统计
     */
    Map<String, Object> getQCProgressStats(Map<String, Object> params);

    /**
     * 批量申请质检
     */
    Map<String, Object> batchRequestQCInspection(Map<String, Object> params);

    /**
     * 批量获取可申请质检数量
     */
    Map<String, Object> batchGetAvailableQCQuantity(Map<String, Object> params);

}
