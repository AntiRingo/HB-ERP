package com.hongbang.mapper;

import com.hongbang.pojo.Log;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface LogMapper {

    //查询入库日志标题

//    @Select(" select Z.*,user.userName as appUserName from (select * from (SELECT * FROM ( (select date,log_number,order_number,userName,product.material_number,product.`name`,product.vault,b.url,product.id,appUser from\n" +
//            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,url,appUser from\n" +
//            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url,application_form.user_id as appUser from log\n" +
//            "              left join application_form on log.app_form_id=application_form.id where application_form.sort=0 ) \n" +
//            "            as a LEFT JOIN `user` on a.manager=user.id  ) as b LEFT JOIN product ON b.product_id= product.id and product.vault=b.vault)  union all \n" +
//            "            (select date,log_number,order_number,userName,fin_product.fin_material_number,fin_product.fin_product_name,fin_product.vault,b.url,fin_product.id,appUser from\n" +
//            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,a.url,appUser from\n" +
//            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url,application_form.user_id as appUser from log  \n" +
//            "            left join application_form on log.app_form_id=application_form.id where application_form.sort=0 ) as a \n" +
//            "            LEFT JOIN `user` on a.manager=user.id ) as b \n" +
//            "            LEFT JOIN fin_product ON b.product_id= fin_product.id and fin_product.vault=b.vault) ORDER BY date  DESC ) AS M) AS N  where LENGTH(N.material_number ) >0 limit #{begin},#{size}) as Z left join user on Z.appUser = user.id  ")

    @Select("SELECT Z.*, user.userName as appUserName FROM (SELECT * FROM (SELECT a.date, a.log_number, a.order_number, a.userName, p.material_number, p.name, a.vault, a.url, p.id, a.appUser FROM (SELECT log.date, log.log_number, application_form.order_number, u.userName, log.product_id, log.vault, log.url, application_form.user_id as appUser FROM log INNER JOIN application_form ON log.app_form_id = application_form.id LEFT JOIN `user` u ON log.manager = u.id WHERE application_form.sort = 0) a INNER JOIN product p ON a.product_id = p.id AND a.vault = p.vault WHERE LENGTH(p.material_number) > 0 UNION ALL SELECT a.date, a.log_number, a.order_number, a.userName, fp.fin_material_number, fp.fin_product_name, a.vault, a.url, fp.id, a.appUser " +
            "FROM (SELECT log.date, log.log_number, application_form.order_number, u.userName, log.product_id, log.vault, log.url, application_form.user_id as appUser FROM log INNER JOIN application_form ON log.app_form_id = application_form.id LEFT JOIN `user` u ON log.manager = u.id WHERE application_form.sort = 0) a INNER JOIN fin_product fp ON a.product_id = fp.id AND a.vault = fp.vault WHERE LENGTH(fp.fin_material_number) > 0) " +
            "AS all_data ORDER BY date DESC LIMIT #{begin}, #{size}) as Z LEFT JOIN user ON Z.appUser = user.id")
    List<Map<String,Object>> storageRecordTitle(@Param("begin") int begin,@Param("size") int size);

    @Select("SELECT count(*) FROM (SELECT * FROM (SELECT a.date, a.log_number, a.order_number, a.userName, p.material_number, p.name, a.vault, a.url, p.id, a.appUser FROM (SELECT log.date, log.log_number, application_form.order_number, u.userName, log.product_id, log.vault, log.url, application_form.user_id as appUser FROM log INNER JOIN application_form ON log.app_form_id = application_form.id LEFT JOIN `user` u ON log.manager = u.id WHERE application_form.sort = 0) a INNER JOIN product p ON a.product_id = p.id AND a.vault = p.vault WHERE LENGTH(p.material_number) > 0 UNION ALL SELECT a.date, a.log_number, a.order_number, a.userName, fp.fin_material_number, fp.fin_product_name, a.vault, a.url, fp.id, a.appUser FROM " +
            "(SELECT log.date, log.log_number, application_form.order_number, u.userName, log.product_id, log.vault, log.url, application_form.user_id as appUser FROM log INNER JOIN application_form ON log.app_form_id = application_form.id LEFT JOIN `user` u ON log.manager = u.id WHERE application_form.sort = 0) a INNER JOIN fin_product fp ON a.product_id = fp.id AND a.vault = fp.vault WHERE LENGTH(fp.fin_material_number) > 0) " +
            "AS all_data ORDER BY date DESC ) as Z LEFT JOIN user ON Z.appUser = user.id")
    int storageRecordTitleCount();



    //查询出库日志标题
    @Select(" select Z.*,user.userName as appUserName from (select * from (SELECT * FROM ( (select date,log_number,order_number,userName,product.material_number,product.`name`,product.vault,b.url,product.id,appUser from\n" +
            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,url,appUser from\n" +
            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url,application_form.user_id as appUser from log\n" +
            "              left join application_form on log.app_form_id=application_form.id where application_form.sort=1 ) \n" +
            "            as a LEFT JOIN `user` on a.manager=user.id  ) as b LEFT JOIN product ON b.product_id= product.id and product.vault=b.vault)  union all \n" +
            "            (select date,log_number,order_number,userName,fin_product.fin_material_number,fin_product.fin_product_name,fin_product.vault,b.url,fin_product.id,appUser from\n" +
            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,a.url,appUser from\n" +
            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url,application_form.user_id as appUser from log  \n" +
            "            left join application_form on log.app_form_id=application_form.id where application_form.sort=1 ) as a \n" +
            "            LEFT JOIN `user` on a.manager=user.id ) as b \n" +
            "            LEFT JOIN fin_product ON b.product_id= fin_product.id and fin_product.vault=b.vault) ORDER BY date  DESC ) AS M) AS N  where LENGTH(N.material_number ) >0 limit #{begin},#{size}) as Z left join user on Z.appUser = user.id  ")
    List<Map<String,Object>> outboundRecordTitle(@Param("begin") int begin,@Param("size") int size);

    @Select("select count(*) from (SELECT * FROM ( (select date,log_number,order_number,userName,product.material_number,product.`name`,product.vault,b.url from\n" +
            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,url from\n" +
            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url from log\n" +
            "              left join application_form on log.app_form_id=application_form.id where application_form.sort=1 ) \n" +
            "            as a LEFT JOIN `user` on a.manager=user.id ) as b LEFT JOIN product ON b.product_id= product.id and product.vault=b.vault)  union all \n" +
            "            (select date,log_number,order_number,userName,fin_product.fin_material_number,fin_product.fin_product_name,fin_product.vault,b.url from\n" +
            "            (SELECT date,product_id,vault,log_number,order_number,`user`.userName,a.url from\n" +
            "            (select log.date,log.manager,log.product_id,log.vault,log.log_number,application_form.order_number,log.url from log  \n" +
            "            left join application_form on log.app_form_id=application_form.id where application_form.sort=1 ) as a \n" +
            "            LEFT JOIN `user` on a.manager=user.id ) as b \n" +
            "            LEFT JOIN fin_product ON b.product_id= fin_product.id and fin_product.vault=b.vault) ) AS M) AS N  where LENGTH(N.material_number ) >0")
    int outboundRecordTitleCount();

    //根据申请单号查询物料号


    //查询该物料是否存在出入库记录
    @Select("select count(*) from log where product_id = #{productId} and vault = #{vault}")
    boolean selectIfLog(@Param("productId") int productId,@Param("vault") int vault);

    //查询操作人(出库)
    @Select("SELECT username,id from `user` right JOIN  (select DISTINCT manager from log LEFT JOIN application_form on log.app_form_id = application_form.id where (application_form.sort = 1 or application_form.sort = 2)) as a on a.manager = `user`.id")
    List<Map<String,Object>> selectManagerCK();

    //查询操作人(入库)
    @Select("SELECT username,id from `user` right JOIN  (select DISTINCT manager from log LEFT JOIN application_form on log.app_form_id = application_form.id where application_form.sort = 0) as a on a.manager = `user`.id")
    List<Map<String,Object>> selectManagerRK();


    //日志筛选
    List<Log> screen(@Param("maps") Map<String,Object> maps, @Param("begin") int begin, @Param("size") int size);
    int screenCount(@Param("maps") Map<String,Object> maps);

    //日志筛选后查询各个信息
    @MapKey("id")
    List<Map<String,Object>> screenInformation(@Param("logs") List<Log> logs);

    //日志模糊查询（根据物料号和订单号）
    @MapKey("id")
    List<Map<String,Object>> search(@Param("str") String str,@Param("sort") int sort,@Param("begin") int begin,@Param("size") int size);


    int searchCount(@Param("str") String str,@Param("sort") int sort);


//在一段时间内统计已经存在的物料出库还是入库的总量
    @MapKey("id")
    List<Map<String,Object>> Statistics(@Param("maps") Map<String,Object> maps,@Param("begin") int begin,@Param("size") int size);

    int StatisticsCount(@Param("maps") Map<String,Object> maps);


    //查询某个申请单中是否存在未签字的日志(让申请人签字)
    @Select("select count(*) from log where (length(url)=0 or url is null) and app_form_id = #{appFormId}")
    boolean selectWqz(@Param("appFormId") int appFormId);

    //查询一个申请人是否存在未签字的申请单
    @Select("select count(*) from log where (length(url)=0 or url is null) and app_form_id in (select id from application_form where user_id = #{userId} and complete_status !=2 and sort = 1) ")
    boolean selectIfQzByUserId(@Param("userId") int userId);

    //申请人签字
    @Update("update log set url = #{url} where app_form_id = #{appFormId} and (length(url) = 0 or url is null)")
    void updateQz(@Param("url") String url,@Param("appFormId") int appFormId);

    //查询该申请单需要签字的产品内容
    @MapKey("")
    List<Map<String,Object>>selectQzProduct(@Param("appFormId") int appFormId);











}
