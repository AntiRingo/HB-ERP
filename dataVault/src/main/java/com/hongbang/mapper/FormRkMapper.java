package com.hongbang.mapper;

import com.hongbang.pojo.FormRk;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FormRkMapper {
    //新增入库条目信息
    void add(@Param("formRks") List<FormRk> formRks);

    //根据时间查询入库信息
    @MapKey("id")
    List<Map<String,Object>> selectRkFormListBy(@Param("maps") Map<String,Object> maps);

    //查询最新一批的申请信息
    @Select("select * from (\n" +
            "select * from (select a.*,application_form.sort_two from ( select form_rk.*,product.name,material_number from form_rk left join product on form_rk.product_id = product.id  and form_rk.vault=product.vault ) as a\n" +
            "        left join application_form on a.app_form_id =application_form.id\n" +
            "        union all\n" +
            "        select b.*,application_form.sort_two from ( select form_rk.*,fin_product.fin_product_name as name ,fin_material_number as material_number  from form_rk left join fin_product on form_rk.product_id = fin_product.id  and form_rk.vault=fin_product.vault ) as b\n" +
            "        left join application_form on b.app_form_id =application_form.id) as c where length(name)>0 ) as d WHERE rk_time = (SELECT MAX(rk_time) FROM form_rk where app_form_id = #{appFormId}) and app_form_id = #{appFormId}")
    List<Map<String,Object>> selectNewApp(@Param("appFormId") int appFormId);


    //每天查询入库的情况
    @Select("select app_number as number,product_id,app_form_id,actual_price,vault from (select form_rk.*,app_number from form_rk LEFT JOIn application_content on application_content.product_id = form_rk.product_id and form_rk.app_form_id = application_content.application_id where rk_time BETWEEN #{startTime} and #{endTime} ) as a where price_sort = 0  GROUP BY product_id,app_form_id,actual_price,vault,number")
    List<Map<String,Object>> selectTodayRk(@Param("startTime") String startTime,@Param("endTime") String endTime);

    //查询该申请单的物料是否今天有发票存在
    @Select("select * from form_rk where app_form_id = #{appFormId} and product_id = #{productId} and vault = #{vault} and price_sort = 1 and rk_time BETWEEN #{startTime} and #{endTime} order by rk_time desc")
    List<FormRk> selectFpByTime(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault,@Param("startTime") String startTime,@Param("endTime") String endTime);

    //查询上一次跟这次不一样价格的信息
    //查询今天之前的最新价格信息
    @Select(" select * from form_rk where id in (select max(id) from form_rk where product_id = #{productId} and vault = #{vault} and actual_price !=#{actualPrice} and rk_time < #{rkTime}) ")
    FormRk selectLastFormRk(@Param("productId") int productId,@Param("vault") int vault,@Param("actualPrice") String actualPrice,@Param("rkTime") String rkTime);

    //查询某个物料信息在这段时间内的入库数量
    @Select("select sum(app_number) from application_content where (application_id,product_id,vault) in (select product_id,vault,app_form_id from form_rk " +
            "where price_sort = 0 and rk_time between #{startTime} and #{endTime} and product_id=#{productId} and vault = #{vault} and rk_sort = 9 group by product_id,vault,app_form_id)")
        String selectSumRk(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("productId") int productId,@Param("vault") int vault);

    //查询某个物料信息在这段时间内的退货入库数量
    @Select("select sum(app_number) from application_content where (application_id,product_id,vault) in (select product_id,vault,app_form_id from form_rk " +
            "where price_sort = 0 and rk_time between #{startTime} and #{endTime} and product_id=#{productId} and vault = #{vault} and rk_sort = 8 group by product_id,vault,app_form_id)")
    String selectThSumRk(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("productId") int productId,@Param("vault") int vault);


    //查询在某个时间段以后的某个物料信息的入库所有的数据
    @Select("select a.*,application_content.app_number from (\n" +
            "select * from form_rk where product_id =#{productId} and vault = #{vault} \n" +
            ") as a LEFT JOIN application_content on a.app_form_id = application_content.application_id and a.product_id = application_content.product_id and a.vault = application_content.vault where a.price_sort = 0 and a.rk_time > #{rkTime}")
    List<Map<String,Object>> selectAppNumberAfterTime(@Param("productId") int productId,@Param("vault") int vault,@Param("rkTime") String rkTime);


    //查询所有申请单中入库的某个物料信息的申请单
    @Select("select a.*,application_content.app_number from (\n" +
            "select * from form_rk where product_id =#{productId} and vault = #{vault} \n" +
            ") as a LEFT JOIN application_content on a.app_form_id = application_content.application_id and a.product_id = application_content.product_id and a.vault = application_content.vault where a.price_sort = 0 ")
    List<Map<String,Object>> selectAppNumberAllTime(@Param("productId") int productId,@Param("vault") int vault);

    //查询一段时间内的最新价格（没有出库信息的时候用）
    @Select("select * from form_rk where rk_time between #{startTime} and #{endTime} and product_id=#{productId} and vault = #{vault} and app_form_id = #{appFormId} order by rk_time desc limit 1")
    FormRk selectNew(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId);

    //查询在一个时间之前的的最新的价格(可能是发票价也可能不是发票价)
    @Select("select * from  form_rk where product_id = #{productId} and vault = #{vault} and app_form_id = #{appFormId} and rk_time < #{rkTime} order by rk_time desc limit 1" )
    FormRk selectNewPriceNoFp(@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId,@Param("rkTime") String rkTime);

    //查询某个申请单中的某个物料信息的入库情况
    @Select("select form_rk.*,application_content.app_number from form_rk left join application_content on app_form_id = application_content.application_id where app_form_id = #{appFormId} and form_rk.product_id = #{productId} and form_rk.vault = #{vault} and price_sort = 0 limit 1")
    Map<String,Object> selectByAppIdAndId(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault);

    //查询今天的所有发票信息
    @Select("select * from form_rk where (app_form_id,product_id,rk_time) in (\n" +
            "SELECT app_form_id, product_id, MAX(rk_time) AS max_rk_time\n" +
            "    FROM form_rk where rk_time BETWEEN #{startTime} and #{endTime} and price_sort = 1 \n" +
            "    GROUP BY app_form_id, product_id)")
    List<FormRk> selectFpToday(@Param("startTime") String startTime,@Param("endTime") String endTime);

    //查询该申请单中的物料信息在小于该发票时间的最新价格，可能存在多张发票，也可能不存在，，所以都要查
    @Select("select * from  form_rk where product_id = #{productId} and vault = #{vault} and app_form_id = #{appFormId} and rk_time < #{rkTime} order by rk_time desc limit 1" )
    FormRk selectNewPriceSort(@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId,@Param("rkTime") String rkTime);

    //查询这个申请单的入库时间，然后去查询所有的入库时间大于这个申请单并且物料id是product的,入库数量以及价格信息
    @Select("select form_rk.*,app_number from form_rk left join application_content on  app_form_id = application_content.application_id and form_rk.product_id = application_content.product_id" +
            " where form_rk.product_id = #{productId} and form_rk.vault = #{vault} and rk_time >= (select rk_time from form_rk where app_form_id = #{appFormId} and price_sort = 0 limit 1) and price_sort = 0")
    List<Map<String,Object>> selectFormRkAndAppNumberByTime(@Param("productId") int productId,@Param("vault") int vault,@Param("appFormId") int appFormId);

    //根据appFormId,productId,vault,rk_time查询最新价格，包括发票价格
    @Select("select actual_price from form_rk where app_form_id = #{appFormId} and product_id = #{productId} and vault = #{vault}  order by rk_time desc limit 1")
    double selectPrice(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault);

    //根据productId查询所有的入库信息(包括数量)
    @Select("select form_rk.*,app_number from form_rk LEFT JOIN application_content on form_rk.app_form_id = application_id and form_rk.product_id = application_content.product_id where form_rk.product_id = #{productId} and  price_sort = 0")
    List<Map<String,Object>>selectByProductId(@Param("productId") int productId);

    //查询上次的价格(要获取第二个，存在发票的时候才能用)
    @Select("select * from form_rk where app_form_id = #{appFormId} and product_id = #{productId} and vault = #{vault}  order by rk_time desc limit 2")
    List<FormRk>selectLastPrice(@Param("appFormId") int appFormId,@Param("productId") int productId,@Param("vault") int vault);

    //查询某个物料还未从入库单出完的所有入库单信息
    @Select("select a.*,b.ck_total from (select form_rk.*,application_content.app_number from form_rk  left join application_content on form_rk.app_form_id = application_content.application_id and form_rk.product_id = application_content.product_id where price_sort = 0) as a LEFT JOIN  (select * from form_ck where (id,app_form_id,product_id) in (select max(id) as id,app_form_id,product_id from form_ck GROUP BY app_form_id,product_id)) as b on a.app_form_id = b.app_form_id and a.product_id = b.product_id where a.product_id = #{productId} and a.vault = #{vault} and ((app_number > ck_total) or ck_total is null)")
    List<Map<String,Object>> selectAllCanCkByProductId(@Param("productId") int productId,@Param("vault") int vault);
}
