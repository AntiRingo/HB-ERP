package com.hongbang.service;

import java.util.List;
import java.util.Map;

public interface ProcurementService {

    // 开始采购
    Map<String, Object> startProcurement(int contentId, int operatorId);

    // 记录到货
    Map<String, Object> recordArrival(int contentId, int operatorId, double batchQuantity, String notes);

    // 完成采购
    Map<String, Object> completeProcurement(int contentId, int operatorId);

    // 获取物料采购记录
    List<Map<String, Object>> getMaterialProcurementRecords(int contentId);

    // 检查是否已开始采购
    boolean isProcurementStarted(int contentId);

    // 获取当前已到货数量
    double getCurrentArrivedQuantity(int contentId);

    // 获取需求数量
    double getRequiredQuantity(int contentId);

    // 获取物料到货状态
    Map<String, Object> getMaterialArrivalStatus(int contentId);


//    // 获取累计到货数量
//    double getTotalArrivedQuantity(int contentId);
}