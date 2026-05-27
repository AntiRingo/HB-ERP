package com.hongbang.mapper;

import com.hongbang.pojo.Balance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BalanceMapper {
    //添加信息
    void addBalance(@Param("balanceList")List<Balance> balanceList);

    //查询一段时间内的数据
    @Select(" select b.*,product.`name`,product.material_number,product.unit from (select product_id,vault, sum(today_in) as todayIn,SUM(today_out) as todayOut,SUM(today_in_number) as todayInNumber,SUM(today_out_number) as todayOutNumber\n" +
            "from balance where time between #{startTime} and #{endTime} GROUP BY product_id,vault) as b left join product  on b.product_id = product.id and b.vault = product.vault WHERE product.id IS NOT NULL ")
    List<Map<String,Object>> selectAllByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    //根据时间段，productId，vault查询剩余量
    @Select("select * from balance where product_id = #{productId} and vault = #{vault} and time between #{startTime} and #{endTime} order by time desc limit 1 ")
    Balance selectNumber(@Param("productId") int productId,@Param("vault") int vault,@Param("startTime") String startTime,@Param("endTime") String endTime);

    //查询上期结存
    @Select("select * from balance where product_id = #{productId} and vault = #{vault} and time < #{startTime} order by time desc limit 1 ")
    Balance selectLast(@Param("productId") int productId,@Param("vault") int vault,@Param("startTime") String startTime);

    //删除
    @Delete("delete from balance where time = #{time}")
    void delete(@Param("time") String time);

    //查询最新的出库数量以及最新的价格
//    @Select(" select a.*,b.ck_total from " +
//            "(select c.*,app_number from ( select * from form_rk where (id,product_id) in (select MAX(id),product_id from form_rk group by product_id)) as c LEFT JOIN application_content on c.app_form_id = application_content.application_id and c.product_id = application_content.product_id) as a " +
//            "LEFT JOIN (select * from form_ck where (id,product_id) in (select max(id),product_id from form_ck  group by product_id)) as b on a.app_form_id = b.app_form_id and a.product_id = b.product_id where a.product_id = #{productId} and a.vault = #{vault} and ((app_number > ck_total) or ck_total is null) ")
//    List<Map<String,Object>>selectNewPriceAndNumber(@Param("productId") int productId,@Param("vault") int vault);

    @Select("select a.*,b.ck_total from ( select d.*,application_content.app_number from (select * from form_rk where (id,app_form_id,product_id) in (\n" +
            "select max(id),app_form_id,product_id from form_rk GROUP BY app_form_id,product_id)\n" +
            ") as d  LEFT JOIN application_content on app_form_id = application_id and d.product_id = application_content.product_id )as a left JOIN (select * from form_ck where (id,app_form_id,product_id) in (\n" +
            "SELECT max(id),app_form_id,product_id from form_ck GROUP BY app_form_id,product_id\n" +
            ") ) as b on a.app_form_id = b.app_form_id  and  a.product_id = b.product_id  where a.product_id = #{productId} and a.vault = #{vault} and ((app_number > ck_total) or ck_total is null) ")
    List<Map<String,Object>>selectNewPriceAndNumber(@Param("productId") int productId,@Param("vault") int vault);
}
