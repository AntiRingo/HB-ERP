package com.hongbang.mapper;

import com.hongbang.pojo.FormCk;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface FormCkMapper {
        //新增出库条目信息
        void add(@Param("formCks") List<FormCk> formCks);

        //根据id产品id和仓库查询上一次的记录
        @Select("select * from form_ck where product_id = #{productId} and vault = #{vault} order by id desc limit 1")
        FormCk selectLast(@Param("productId") int productId,@Param("vault") int vault);

        //根据时间查询出库信息
        @MapKey("id")
        List<Map<String,Object>> selectCkFormListBy(@Param("maps") Map<String,Object> maps);

        //每天查询出库的情况
        @Select("select product_id,sum(ck_number) as ck_number,app_form_id,vault from form_ck where ck_time BETWEEN #{startTime} and #{endTime} GROUP BY product_id,app_form_id,vault")
        List<Map<String,Object>> selectTodayCk(@Param("startTime") String startTime,@Param("endTime") String endTime);

        //查询在这段时间内的总出库量
        @Select("select sum(ck_number) as sum,product_id,vault from form_ck where ck_time between #{startTime} and #{endTime} and  product_id = 10 and vault = 0 GROUP BY product_id,vault")
        Map<String, Object> selectSumCk(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("productId") int productId,@Param("vault") int vault);


//        -----------------------------------------------------
//        查询form_ck表中该物料信息的最新出库情况，要查看入库单信息
        @Select("select * from form_ck where product_id = #{productId} and vault = #{vault}  order by id desc")
        List<FormCk> selectNewCkById(@Param("productId") int productId,@Param("vault") int vault);

        //根据申请单id，物料id，仓库查询出库情况
        @Select("select * from form_ck where product_id = #{productId} and vault = #{vault} and app_form_id = #{appFormId}  order by ck_time desc")
        List<FormCk> selectNewCkByFormId(@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId);

        //根据日期，申请单id，物料id，仓库，查询今天的总出库量
        @Select("select sum(ck_number) from form_ck where app_form_id = #{appFormId} and product_id = #{productId} and vault = #{vault} and  ck_time BETWEEN #{startTime} and #{endTime}")
       double selectCkSumByForm(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault,@Param("startTime") String startTime,@Param("endTime") String endTime);


        // 查询该申请单中该物料信息上次的ckTotal是多少
        @Select("select * from form_ck where product_id = #{productId} and vault = #{vault} and app_form_id = #{appFormId} order by ck_time desc limit 1")
        List<FormCk> selectNewCk(@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId);


        //根据appformid。productId，vault,时间，去查询在一段时间内的最新的ck_total
        @Select("select * from form_ck where app_form_id = #{appFormId} and product_id = #{productId} and vault = #{vault} and ck_time BETWEEN #{startTime} and #{endTime} limit 1")
        FormCk selectCkTotalBetween(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault,@Param("startTime") String startTime,@Param("endTime") String endTime);




}
