package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationForm;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ApplicationFormMapper {

    //提交申请表
    void add(ApplicationForm applicationForm);

    //删除申请表
    @Delete("delete  from application_form where id = #{id}")
    void delete(@Param("id") int id);

    //查询申请单信息（包含申请人信息）
    @MapKey("")
    List<Map<String,Object>> selectAllApplication(@Param("sort") int sort,@Param("begin") int begin,@Param("size") int size,@Param("ckId") int ckId,@Param("level") boolean level,@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("dg") boolean dg );


    int count(@Param("sort") int sort,@Param("ckId") int ckId,@Param("level") boolean level,@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("dg") boolean dg);

    //根据用户ID查询该用户的申请单
    List<ApplicationForm> selectByUserId(@Param("maps") Map<String,Object> maps,@Param("begin") int begin,@Param("size") int size);

   int selectByUserIdCount(@Param("maps") Map<String,Object> maps);

    //更改申请单为撤销状态
    @Update("update application_form set complete_status = 2 where id = #{id}")
    void updateRevoke(@Param("id") int id);

//    //根据ID查询申请单信息
//    @Select("select * from application_form where id = #{id}")
//    List<ApplicationForm> applicationForm(@Param("id") int id);

    //查看是否有新增的申请信息(入库申请)
   List<Integer> selectIfNewApplicationRk();

    //查看是否有新增的申请信息(出库申请)
    List<Integer> selectIfNewApplicationCk();

    //点击查看后将申请单从新增状态改为取货中状态
    @Update("update application_form set complete_status = 0 where id = #{id}")
    void updateStatusTwo(@Param("id") int id);

    //申请单筛选
    @MapKey("")
    List<Map<String,Object>> screen(@Param("maps") Map<String,Object> maps,@Param("begin") int begin,@Param("size") int size,@Param("ckId") int ckId,@Param("level") boolean level);
    int screenCount(@Param("maps") Map<String,Object> maps,@Param("ckId") int ckId,@Param("level") boolean level);

    //根据筛选结果获取筛选人名
    @MapKey("id")
    List<Map<String,Object>> screenUserName(@Param("applicationForms") List<Map<String,Object>> applicationForms);

    //根据申请类型获取筛选人
    @MapKey("id")
    List<Map<String,Object>> screenUserId(@Param("sort") int sort,@Param("ckId") int ckId,@Param("level") boolean level);

    //模糊查询
@MapKey("id")
    List<Map<String,Object>>searchOrderNumber(@Param("str") String str,@Param("sort") int sort,@Param("begin") int begin,@Param("size") int size,@Param("ckId") int ckId,@Param("level") boolean level);

    int searchOrderNumberCount(@Param("str") String str,@Param("sort") int sort,@Param("ckId") int ckId,@Param("level") boolean level);

    //将申请单状态修改为已出库
    @Update("update application_form set complete_status = 4 where id = #{id}")
    void updateStatusFour(@Param("id") int id);

    //查询是否存在已出库状态的申请单
    @Select("select count(*) from application_form where complete_status = 4 and user_id = #{userId}")
    boolean selectPickUp(@Param("userId") int userId);

    //修改申请单状态
    @Update("update application_form set complete_status = #{status} where id = #{id}")
    int updateStatus(@Param("status") int status, @Param("id") int id);

    //根据id查询申请单信息
    @Select("select * from application_form where id = #{id}")
    List<ApplicationForm> selectById(@Param("id") int id);

    //根据用户id查询该用户的申请单（已完成的和已撤销的除外）
    @Select("select * from application_form where user_id = #{userId} and complete_status !=1 and complete_status !=2")
    List<ApplicationForm> selectAppFormNotCompleteAndDelete(@Param("userId") int userId);



    //仓库查看已经审核通过的申请
    @Select("select * from application_form where id in (\n" +
            "select c.id from (\n" +
            "select id,SUM(price*app_number) as totalPrice,count(id) as count from (\n" +
            "select a.*,product.brand as price from (select  application_form.id,product_id,vault,app_number from application_form LEFT JOIN application_content on application_form.id = application_content.application_id) as a LEFT JOIN product on a.product_id = product.id and a.vault = product.vault where LENGTH(product.id)>0\n" +
            "UNION ALL\n" +
            "select a.*,price from (select  application_form.id,product_id,vault,app_number from application_form LEFT JOIN application_content on application_form.id = application_content.application_id) as a LEFT JOIN fin_product on a.product_id = fin_product.id and a.vault = fin_product.vault where LENGTH(fin_product.id)>0)" +
            " as b  GROUP BY id) as c LEFT JOIN examine on c.id = examine.app_form_id where ((count=1 and totalPrice>300) or (count>1 and totalPrice >1000) and minister = 1 ) or ((count=1 and totalPrice<=300) or (count>1 and totalPrice<=1000) and minister = 1)) and complete_status !=2")
            List<ApplicationForm>depotExamine();



    //部长查看需要自己审核的申请单

    List<ApplicationForm>ministerExamine(@Param("department") int department,@Param("minister") int minister,@Param("begin") int begin,@Param("size") int size);

    int ministerExamineCount(@Param("department") int department,@Param("minister") int minister);




    //部长筛选（新）
    List<ApplicationForm> ministerScreen(@Param("maps") Map<String,Object> maps,@Param("begin") int begin,@Param("size") int size,@Param("department") int department);
    int ministerScreenCount(@Param("maps") Map<String,Object> maps,@Param("department") int department);

    //查询是否有新的申请单
    List<Integer> selectIfExamine(@Param("sort") int sort,@Param("department") int department);

    //部长显示申请人员（筛选使用）
    @MapKey("")
    List<Map<String,Object>> ministerScreenUser(@Param("maps") Map<String,Object> maps,@Param("department") int department);

    //部长显示申请部门（筛选使用）
    @MapKey("")
    List<Map<String,Object>> ministerScreenDepartment(@Param("maps") Map<String,Object> maps,@Param("department") int department);


    //根据申请单id查询是否总价钱
    @MapKey("id")
    List<Map<String,Object>> selectPriceAndCount(@Param("applicationForms") List<ApplicationForm>applicationForms);

    //根据申请单id查询总价（只包含零件，成品价格在前端查询）
    @MapKey("id")
    List<Map<String,Object>> selectPriceAndCountLj(@Param("applicationForms") List<ApplicationForm>applicationForms);

    //根据id查询申请单状态
    @Select("select complete_status from application_form where id = #{id}")
    List<Map<String,Object>>selectComplete(@Param("id") int id);

    //更新申请单上的审核部门
    @Update("update application_form set circulation = #{circulation} where id = #{id}")
    void updateCirculation(@Param("circulation") int circulation,@Param("id") int id);

    //查询审核流程
    @Select("SELECT b.*,departmentName from (\n" +
            "SELECT a.*,minister from (\n" +
            "SELECT app_form_id,step,department_id,result,examine_step_content.id from examine_step_content LEFT JOIN examine_all_type on examine_step_id =examine_all_type.id\n" +
            ") as a LEFT JOIN examine on examine.app_form_id = a.app_form_id) as b LEFT JOIN department on department.id = department_id where b.app_form_id = #{appFormId} order by id")
    List<Map<String,Object>>selectLc(@Param("appFormId") int appFormId);

    //修改circulationBoss,这个表示都审核完了
    @Update("update application_form set circulation_boss = 1 where id = #{id}")
    void updateCirculationBoss(@Param("id") int id);

    //更新不通过的原因
    @Update("update application_form set refuse = #{refuse} where id = #{id}")
    void updateRefuse(@Param("refuse") String refuse, @Param("id") int id);

    //查询申请单的发票是否已经全部上传并审核完成
    @Select("select count(*) from application_form where id = #{id} and sort_two=9 and invoice_sign = 1")
    boolean selectInvoiceSign(@Param("id") int id);

    //查询是否存在已出库状态的申请单
    @Select("select id from application_form where complete_status = 4 and user_id = #{userId}")
    List<Integer> selectPickUpAppId(@Param("userId") int userId);


    //接单使用（查询所有出库，入库的并且是待查看状态的，并且是未接单状态的申请单）

    List<ApplicationForm> selectTakeOrder(@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("dg") boolean dg);


    //查询订购单信息(根据用户ID查询自己提交的申请单)
    @Select("select application_form.* from application_form LEFT JOIN take_order on application_form.id = take_order.app_form_id where sort = 3 and complete_status = 1  order by date desc limit #{begin},#{size} ")
    List<ApplicationForm> selectDgByUserId(@Param("userId") int userId,@Param("begin") int begin,@Param("size") int size);
    //查询数量
    @Select("select count(*) from application_form where sort =3 and user_id = #{userId} ")
    int selectDgByUserIdCount(@Param("userId") int userId);

    //查询自己申请的质检单
    @Select("select * from application_form where sort_two = 16 and user_id=#{userId}  order by date desc limit #{begin},#{size}")
    List<ApplicationForm>selectMyZj(@Param("userId") int userId,@Param("begin") int begin,@Param("size") int size);

    //查询自己申请的质检单
    @Select("select count(*) from application_form where sort_two = 16 and user_id=#{userId}")
    int selectMyZjCount(@Param("userId") int userId);


    //申购单（即采购单）不需要进行接单，该类型的申请单由采购部部长进行拆分
    @MapKey("id")
    List<Map<String,Object>>selectAllCgApplication(@Param("begin") int begin,@Param("size") int size,@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("maps") Map<String,Object> maps);
    int selectAllCgApplicationCount(@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("maps") Map<String,Object> maps);

    //订购单，这个需要自己接单
    @MapKey("id")
    List<Map<String,Object>>selectAllDgApplication(@Param("begin") int begin,@Param("size") int size,@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("maps") Map<String,Object> maps,@Param("userId") int userId);
    int selectAllDgApplicationCount(@Param("ck") boolean ck,@Param("rk") boolean rk,@Param("cg") boolean cg,@Param("zj") boolean zj,@Param("maps") Map<String,Object> maps,@Param("userId") int userId);

    // 获取申请单状态
    @Select("SELECT complete_status as status FROM application_form WHERE id = #{id}")
    Integer getApplicationStatus(@Param("id") int applicationId);

    // 获取申请单详细信息（包含状态）
    @Select("SELECT * FROM application_form WHERE id = #{id}")
    ApplicationForm getApplicationById(@Param("id") int applicationId);

    // 更新申请单状态
    @Update("UPDATE application_form SET complete_status = #{status} WHERE id = #{id}")
    boolean updateStatusNew(@Param("id") int applicationId, @Param("status") int status);












}
