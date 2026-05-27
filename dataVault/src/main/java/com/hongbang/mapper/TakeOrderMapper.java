package com.hongbang.mapper;

import com.hongbang.pojo.TakeOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TakeOrderMapper {

    //给新申请的申请单添加状态
    @Insert("insert into take_order values (#{id},#{appFormId},#{user},#{takeOrderStatus})")
    void   addTakeOrder(TakeOrder takeOrder);

    //接单
    @Update("update take_order set user = #{user},take_order_status = 1 where app_form_id = #{appFormId}")
    void updateTakeOrder(TakeOrder takeOrder);

    //检查是否已经被接单了,返回true就是未接单
    @Select("select count(*) from take_order where app_form_id = #{appFormId} and take_order_status = 0")
    boolean selectIfJd(@Param("appFormId") int appFormId);

    //部长查看所有的接单情况
//    @Select("select a.*,userName,name,sort_two as sortTwo,user_id as userId,if(take_order_status=0,'未接单','已接单')  as jd\n" +
//            "from (select application_form.*,take_order.take_order_status,user\n" +
//            "from application_form left join take_order on app_form_id = application_form.id where take_order.id>0  and  circulation_boss = 1\n" +
//            ") as a LEFT JOIN `user` on `user`.id = a.`user` where sort !=2 ORDER BY date desc limit #{begin},#{size}")
    @MapKey("")
    List<Map<String,Object>> selectAllTakeOrder(@Param("begin") int begin,@Param("size") int size,@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("dg") boolean dg);

    int selectAllTakeOrderCount(@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("dg") boolean dg);

}
