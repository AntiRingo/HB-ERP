package com.hongbang.mapper;

import com.hongbang.pojo.Invoice;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface InvoiceMapper {
    //插入多张发票信息
    void insertAllInvoice(@Param("invoices") List<Invoice> invoices);

    //根据appId查询发票信息
@Select("select * from invoice where app_id = #{appId}")
    List<Invoice> selectFp(@Param("appId") int appId);

    //查询未审核发票的申请单信息
    @MapKey("id")
    List<Map<String,Object>> selectWsFp(@Param("maps") Map<String,Object> maps);

    //通过审核发票信息
    @Update("update invoice set status = 1 ,time = #{time} where app_id = #{appId} and status = 0")
    void updatePass(@Param("appId") int appId,@Param("time") String time);

    //不通过发票信息
    @Update("update invoice set status = 2 where app_id =#{appId}  and time = #{time} and status = 0")
    void updateNoPass(@Param("appId") int appId,@Param("time") String time);


    //查询一段时间内的已通过审核的发票
    @Select("select * from invoice where time between #{beginTime} and #{endTime} and status = 1")
    List<Invoice> selectFpByTime(@Param("beginTime") String beginTime,@Param("endTime") String endTime);
}
