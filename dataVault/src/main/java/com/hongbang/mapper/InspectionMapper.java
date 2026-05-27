package com.hongbang.mapper;

import com.hongbang.pojo.Inspection;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface InspectionMapper {
    //添加

    void add(@Param("inspections") List<Inspection> inspections);

   //更新list
    void updateList(@Param("inspections") List<Inspection> inspections);

    //删除

    //通过或者不通过
    @Update("update inspection set pass_rate = #{passRate},notes=#{notes},pass = #{pass} where id = #{id}")
    void updatePass(Inspection inspection);

    //已经质检过的质检单查询质检信息
    @Select("select * from inspection where app_id = #{appId}")
    List<Inspection> selectZjInformation(@Param("appId") int appId);






        // 插入质检申请记录
        @Insert("INSERT INTO arrival_qc_record (content_id, qc_application_id, operator_id, qc_quantity, operation_time) " +
                "VALUES (#{contentId}, #{qcApplicationId}, #{operatorId}, #{qcQuantity}, NOW())")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        int insertQCRequest(Map<String, Object> params);

        // 创建质检申请单
        @Insert("INSERT INTO application_form (order_number, date, sort, user_id, complete_status,sort_two,reason,circulation, notes) " +
                "VALUES (#{orderNumber}, NOW(), 4, #{userId}, 3,16,null,27, #{notes})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        int insertQCApplication(Map<String, Object> params);



        // 查询质检记录
       @MapKey("id")
        List<Map<String, Object>> selectQCRecords(Map<String, Object> params);

        // 获取物料质检状态统计
        @Select("SELECT " +
                "COALESCE(SUM(qc.qc_quantity), 0) as total_qc_requested, " +
                "COALESCE(SUM(CASE WHEN f.complete_status = 1 THEN qc.qc_quantity ELSE 0 END), 0) as total_qc_completed, " +
                "COALESCE(COUNT(DISTINCT qc.qc_application_id), 0) as qc_application_count " +
                "FROM arrival_qc_record qc " +
                "LEFT JOIN application_form f ON qc.qc_application_id = f.id " +
                "WHERE qc.content_id = #{contentId}")
        Map<String, Object> selectQCStats(@Param("contentId") int contentId);

        // 获取可申请质检的数量
        @Select("SELECT " +
                "COALESCE(SUM(qc.qc_quantity), 0) as total_qc_requested, " +
                "ac.app_number as required_quantity, " +
                "ac.actual_number as actual_quantity, " +
                "ac.actual_number - COALESCE(SUM(qc.qc_quantity), 0) as available_quantity " +
                "FROM arrival_qc_record qc " +
                "RIGHT JOIN application_content ac ON qc.content_id = ac.id " +
                "WHERE ac.id = #{contentId} " +
                "GROUP BY ac.id, ac.app_number, ac.actual_number")
        Map<String, Object> selectAvailableQCQuantity(@Param("contentId") int contentId);

        // 获取已申请质检数量
        @Select("SELECT COALESCE(SUM(qc_quantity), 0) as total_qc_requested " +
                "FROM arrival_qc_record WHERE content_id = #{contentId}")
        double selectQCRequestedQuantity(@Param("contentId") int contentId);

        // 获取质检申请单列表
        @Select("SELECT f.id, f.order_number, f.date, f.sort, f.user_id, f.complete_status, f.notes, " +
                "       u.name as user_name, " +
                "       COUNT(DISTINCT qc.content_id) as material_count, " +
                "       COALESCE(SUM(qc.qc_quantity), 0) as total_qc_quantity " +
                "FROM application_form f " +
                "LEFT JOIN arrival_qc_record qc ON f.id = qc.qc_application_id " +
                "LEFT JOIN user u ON f.user_id = u.id " +
                "WHERE f.sort = 4 " +
                "GROUP BY f.id, f.order_number, f.date, f.sort, f.user_id, f.complete_status, f.notes, u.name " +
                "ORDER BY f.date DESC")
        List<Map<String, Object>> selectQCApplications();

        // 获取质检申请单详情
        @Select("SELECT f.id, f.order_number, f.date, f.sort, f.user_id, f.complete_status, f.notes, " +
                "       u.name as user_name, u.department as user_department " +
                "FROM application_form f " +
                "LEFT JOIN user u ON f.user_id = u.id " +
                "WHERE f.id = #{qcApplicationId} AND f.sort = 4")
        Map<String, Object> selectQCApplicationById(@Param("qcApplicationId") int qcApplicationId);

        // 获取质检申请单的物料列表
        @Select("SELECT qc.*, ac.product_id, ac.app_number, ac.actual_number, ac.vault, ac.app_price, p.name as product_name, " +
                "       p.material_number, u.name as operator_name, f.order_number as qc_order_number, f.date as qc_date " +
                "FROM arrival_qc_record qc " +
                "LEFT JOIN application_content ac ON qc.content_id = ac.id " +
                "LEFT JOIN product p ON ac.product_id = p.id " +
                "LEFT JOIN user u ON qc.operator_id = u.id " +
                "LEFT JOIN application_form f ON qc.qc_application_id = f.id " +
                "WHERE qc.qc_application_id = #{qcApplicationId}")
        List<Map<String, Object>> selectQCMaterialsByApplication(@Param("qcApplicationId") int qcApplicationId);

        // 更新质检申请单状态
        @Update("UPDATE application_form SET complete_status = #{status}, notes = #{notes} " +
                "WHERE id = #{qcApplicationId} AND sort = 4")
        int updateQCApplicationStatus(@Param("qcApplicationId") int qcApplicationId,
                                      @Param("status") int status,
                                      @Param("notes") String notes);

        // 检查质检申请单是否存在
        @Select("SELECT COUNT(*) FROM application_form WHERE id = #{qcApplicationId} AND sort = 4")
        int checkQCApplicationExists(@Param("qcApplicationId") int qcApplicationId);

        // 获取质检进度统计
        @Select("SELECT " +
                "COUNT(DISTINCT f.id) as total_applications, " +
                "COUNT(DISTINCT CASE WHEN f.complete_status = 1 THEN f.id END) as completed_applications, " +
                "COUNT(DISTINCT CASE WHEN f.complete_status = 5 THEN f.id END) as pending_applications, " +
                "COALESCE(SUM(qc.qc_quantity), 0) as total_qc_quantity, " +
                "COUNT(DISTINCT qc.content_id) as total_materials " +
                "FROM application_form f " +
                "LEFT JOIN arrival_qc_record qc ON f.id = qc.qc_application_id " +
                "WHERE f.sort = 4")
        Map<String, Object> selectQCProgressStats();

        // 获取物料质检详情
        @Select("SELECT " +
                "ac.id, ac.application_id, ac.product_id, ac.app_number, ac.actual_number, ac.vault, ac.app_price, " +
                "p.name as product_name, p.material_number, " +
                "COALESCE(SUM(qc.qc_quantity), 0) as qc_requested_quantity, " +
                "ac.actual_number - COALESCE(SUM(qc.qc_quantity), 0) as qc_available_quantity, " +
                "COUNT(qc.id) as qc_request_count " +
                "FROM application_content ac " +
                "LEFT JOIN product p ON ac.product_id = p.id " +
                "LEFT JOIN arrival_qc_record qc ON ac.id = qc.content_id " +
                "WHERE ac.id = #{contentId} " +
                "GROUP BY ac.id, ac.application_id, ac.product_id, ac.app_number, ac.actual_number, ac.vault, ac.app_price, p.name, p.material_number")
        Map<String, Object> selectQCDetailsByContent(@Param("contentId") int contentId);

        // 生成质检单号
        @Select("SELECT CONCAT('QC', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(COUNT(*)+1, 4, '0')) as order_number " +
                "FROM application_form WHERE DATE(date) = CURDATE() AND sort = 4")
        String generateQCOrderNumber();

        // 检查是否有待处理的质检申请单
        @Select("SELECT f.id, f.order_number, f.complete_status " +
                "FROM application_form f " +
                "WHERE f.sort = 4 AND f.complete_status = 5 " +
                "ORDER BY f.date DESC " +
                "LIMIT 1")
        Map<String, Object> getPendingQCApplication();

        // 获取申请单的实际数量
        @Select("SELECT actual_number FROM application_content WHERE id = #{contentId}")
        Double getActualNumber(@Param("contentId") int contentId);

        // 获取申请单的申请数量
        @Select("SELECT app_number FROM application_content WHERE id = #{contentId}")
        Double getAppNumber(@Param("contentId") int contentId);

        // 获取产品信息
        @Select("SELECT p.id, p.name, p.material_number FROM product p " +
                "INNER JOIN application_content ac ON p.id = ac.product_id " +
                "WHERE ac.id = #{contentId}")
        Map<String, Object> getProductInfo(@Param("contentId") int contentId);

        // 根据物料ID获取质检记录
        @Select("SELECT qc.*, f.order_number as qc_order_number, f.date as qc_date, " +
                "       u.name as operator_name, u.department as operator_department " +
                "FROM arrival_qc_record qc " +
                "LEFT JOIN application_form f ON qc.qc_application_id = f.id " +
                "LEFT JOIN user u ON qc.operator_id = u.id " +
                "WHERE qc.content_id = #{contentId} " +
                "ORDER BY qc.operation_time DESC")
        List<Map<String, Object>> getQCRecordsByContent(@Param("contentId") int contentId);



    // 批量申请质检
 @MapKey("id")
    int batchInsertQCRequest(@Param("items") List<Map<String, Object>> items);

    // 批量检查可申请数量
@MapKey("id")
    List<Map<String, Object>> batchSelectAvailableQCQuantity(@Param("contentIds") List<Integer> contentIds);

//更新质检单申请书来那个
    @Update("update arrival_qc_record set qc_quantity =  #{number} where content_id = #{contentId} and operation_time = #{time} ")
    void updateQuantity(@Param("number") double number, @Param("contentId") int contentId, @Param("time")String time);

}
