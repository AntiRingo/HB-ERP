package com.hongbang.mapper;

import com.hongbang.pojo.ProcurementOperations;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProcurementOperationsMapper {

    // 开始采购
    @Insert("INSERT INTO procurement_operations (content_id, operator_id, operation_type, operation_time, notes) " +
            "VALUES (#{content_id}, #{operator_id}, 'procurement_start', NOW(), #{notes})")
    int startProcurement(@Param("content_id") int contentId,
                         @Param("operator_id") int operatorId,
                         @Param("notes") String notes);

    // 记录到货
    @Insert("INSERT INTO procurement_operations (content_id, operator_id, operation_type, operation_time, batch_quantity, notes) " +
            "VALUES (#{content_id}, #{operator_id}, 'arrival', NOW(), #{batch_quantity}, #{notes})")
    int recordArrival(@Param("content_id") int contentId,
                      @Param("operator_id") int operatorId,
                      @Param("batch_quantity") double batchQuantity,
                      @Param("notes") String notes);

    // 完成采购
    @Insert("INSERT INTO procurement_operations (content_id, operator_id, operation_type, operation_time, notes) " +
            "VALUES (#{content_id}, #{operator_id}, 'complete', NOW(), #{notes})")
    int completeProcurement(@Param("content_id") int contentId,
                            @Param("operator_id") int operatorId,
                            @Param("notes") String notes);

    // 获取物料采购记录
    @Select("SELECT po.id as operation_id, po.operation_type, po.operation_time, " +
            "po.batch_quantity, u.name as operator_name, d.departmentName as operator_department, " +
            "po.notes, po.created_at " +
            "FROM procurement_operations po " +
            "LEFT JOIN user u ON po.operator_id = u.id " +
            "LEFT JOIN department d ON u.department = d.id " +
            "WHERE po.content_id = #{content_id} " +
            "ORDER BY po.operation_time DESC")
    List<Map<String, Object>> getMaterialProcurementRecords(@Param("content_id") int contentId);

    // 检查是否已开始采购
    @Select("SELECT COUNT(*) as count FROM procurement_operations " +
            "WHERE content_id = #{content_id} AND operation_type = 'procurement_start'")
    int checkProcurementStarted(@Param("content_id") int contentId);

    // 获取累计到货数量
    @Select("SELECT IFNULL(SUM(batch_quantity), 0) as total_arrived FROM procurement_operations " +
            "WHERE content_id = #{content_id} AND operation_type = 'arrival'")
    Double getTotalArrivedQuantity(@Param("content_id") int contentId);



    // 获取当前已到货数量 - 修正为使用actual_number字段
    @Select("SELECT IFNULL(actual_number, 0) as arrived_quantity FROM application_content WHERE id = #{content_id}")
    Double getCurrentArrivedQuantity(@Param("content_id") int contentId);

    // 更新申请单明细的已到货数量 - 修正为更新actual_number字段
    @Update("UPDATE application_content SET actual_number = #{actual_number} WHERE id = #{content_id}")
    int updateArrivedQuantity(@Param("content_id") int contentId,
                              @Param("actual_number") double actualNumber);

    // 获取需求数量 - 使用app_number字段
    @Select("SELECT IFNULL(app_number, 0) as required_quantity FROM application_content WHERE id = #{content_id}")
    Double getRequiredQuantity(@Param("content_id") int contentId);


    // 获取物料到货状态 - 简化版
    @Select("SELECT " +
            "ac.id as content_id, " +
            "ac.application_id, " +
            "ac.product_id, " +
            "p.name as product_name, " +
            "p.material_number, " +
            "p.unit, " +
            "ac.app_number as required_quantity, " +
            "ac.actual_number as current_arrived_quantity, " +
            "(ac.app_number - ac.actual_number) as remaining_quantity, " +
            "CASE " +
            "    WHEN ac.actual_number >= ac.app_number THEN 'completed' " +
            "    WHEN EXISTS ( " +
            "        SELECT 1 FROM procurement_operations po " +
            "        WHERE po.content_id = ac.id AND po.operation_type = 'procurement_start' " +
            "    ) THEN 'processing' " +
            "    ELSE 'pending' " +
            "END as procurement_status, " +
            "(SELECT COUNT(*) FROM procurement_operations po " +
            " WHERE po.content_id = ac.id AND po.operation_type = 'arrival') as arrival_count, " +
            "(SELECT u.name FROM procurement_operations po " +
            " LEFT JOIN user u ON po.operator_id = u.id " +
            " WHERE po.content_id = ac.id " +
            " ORDER BY po.operation_time DESC LIMIT 1) as last_operator_name, " +
            "(SELECT po.operation_time FROM procurement_operations po " +
            " WHERE po.content_id = ac.id " +
            " ORDER BY po.operation_time DESC LIMIT 1) as last_operation_time, " +
            "CASE " +
            "    WHEN ac.app_number > 0 THEN ROUND((ac.actual_number / ac.app_number) * 100, 2) " +
            "    ELSE 0 " +
            "END as progress_percent " +
            "FROM application_content ac " +
            "LEFT JOIN product p ON ac.product_id = p.id " +
            "WHERE ac.id = #{content_id}")
    Map<String, Object> getMaterialArrivalStatus(@Param("content_id") int contentId);



    // 根据 contentId 查询到货记录
    List<ProcurementOperations> selectByContentId(@Param("contentId") Integer contentId);

    // 根据ID更新数量
    int updateById(ProcurementOperations operations);
}