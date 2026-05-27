package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.FinProduct;
import com.hongbang.pojo.Log;
import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ApplicationContentMapper {
    /**
     * 批量插入数据
     * @param applicationContent
     * @return
     */
    void add (@Param("applicationContent") List<ApplicationContent>applicationContent);

    //将临时储存就申请内容ID的fin_bom_title_id更新为0（申购单转换成质检单要记录）
    void updateBomTitleId(@Param("applicationContents") List<ApplicationContent>applicationContents);
    //将临时储存就申请内容ID的fin_bom_title_id更新为0（申购单转换成质检单要记录）
    void updateActualNumberZero(@Param("applicationContents") List<ApplicationContent>applicationContents);

    //降临时储存旧申请单ID的applicationId修改为最新的（申购单换成质检单）
    void updateApplicationId(@Param("applicationContents") List<ApplicationContent>applicationContents,@Param("applicationId")int applicationId);

    //查看申请单数据(电子仓库)
    @Select("select * from application_content where application_id = #{applicationId} and vault=0")
    List<ApplicationContent> selectByAppIdDz(@Param("applicationId") int applicationId);

    //查看申请单数据(产品仓库)
    @Select("select * from application_content where application_id = #{applicationId} and vault=1")
    List<ApplicationContent> selectByAppIdCp(@Param("applicationId") int applicationId);

    //查询所有的申请单数据（两个仓库的）
    @MapKey("id")
    List<Map<String,Object>>selectByAppIdAllVault(@Param("applicationId") int applicationId);


    //查询申请单数据的详细信息（电子仓库物料信息）
    @MapKey("id")
    List<Map<String,Object>> selectAppContentDz(@Param("applicationContents") List<ApplicationContent> applicationContents);


    //查询申请单数据的详细信息（产品仓库物料信息）
    @MapKey("id")
    List<Map<String,Object>> selectAppContentCp(@Param("applicationContents") List<ApplicationContent> applicationContents);

    @MapKey("id")
    //查询申请单中产品的属性信息(电子仓库)
    List<Map<String,Object>>selectAttDz(@Param("applicationId") int applicationId);
    @MapKey("id")
    //查询申请单中产品的属性信息(产品仓库)
    List<Map<String,Object>>selectAttCp(@Param("applicationId") int applicationId);


    //更新申请单已出入库信息信息（一键入库/出库的时候）
    void updateActual(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //查看该订单是否出入库完成
    @Select("select count(*) from application_content where application_id = #{applicationId} and app_number > application_content.actual_number")
    boolean selectComplete(@Param("applicationId") int applicationId);
    //如果查询完为false,那就证明该申请完成，更改完成状态
    @Update("update application_form set complete_status = 1 where id = #{applicationId}")
    void updateComplete(@Param("applicationId") int applicationId);

    //一键出库(电子仓库)
    void updatePtCk(@Param("products") List<Product>products);

    //一键出库（产品仓库）
    void updateFptCk(@Param("finProducts")List<FinProduct> finProducts);

    //一键入库(电子仓库)
    void updatePtRk(@Param("products") List<Product>products);

    //一键入库（产品仓库）
    void updateFptRk(@Param("finProducts") List<FinProduct> finProducts);

    //插入日志信息
    void addLog(@Param("logs") List<Log>logs);


//    //更新订单信息（撤销后提交的）
//    @Update("update application_form set date = #{date},sort = #{sort},complete_status = #{completeStatus} where id = #{id}")
//    void updateByProve(ApplicationForm applicationForm);
//
//    //根据ID删除订单中的申请内容(提交撤销的内容后要先删除之前的)
//    @Delete("delete from application_content where application_id = #{applicationId}")
//    void deleteByProve(@Param("applicationId") int applicationId);

    //根据id查询存放位置（电子仓库）
    @MapKey("productId")
    List<Map<String,Object>> selectLocationDz(@Param("applicationContents") List<ApplicationContent>applicationContents);

    @MapKey("productId")
    //根据id查询存放位置（产品仓库）
    List<Map<String,Object>> selectLocationCp(@Param("applicationContents") List<ApplicationContent>applicationContents);

    @MapKey("")
    //查询申请单的所有物料信息单价
    List<Map<String,Object>> selectPrice(@Param("applicationContents") List<ApplicationContent>applicationContents);

    //查询该申请单要制造什么
    @Select("select distinct fin_product_id,fin_product_number from application_content where application_id = #{applicationId}")
    List<Map<String,Object>>selectWhatsThis(@Param("applicationId") int applicationId);



//点击查看自己的申请单复原过程-------------------------------------------------------------
//1.查询lastlevel=0并且fin_product_id=0的产品
    @Select("select * from application_content where last_level=0 and fin_product_id = 0 and application_id = #{applicationId}")
    List<ApplicationContent> selectOneLevel(@Param("applicationId") int applicationId);
    //根据productID，vault查询产品信息
    @MapKey("")
    List<Map<String,Object>>selectProductByIdVault(@Param("applicationContents") List<ApplicationContent> applicationContents ,@Param("applicationId") int applicationId);
//2.查询lastlevel=0并且fin_product_id!=0的产品(这里查询的都是已经展开的，所以肯定是成品库中的)
    @Select("select * from application_content where last_level=0 and fin_product_id != 0 and application_id = #{applicationId}")
    List<ApplicationContent> selectOneLevelZk(@Param("applicationId") int applicationId);
    //根据fin_product_id在成品库中查询产品信息
    @MapKey("")
    List<Map<String,Object>> selectProductByIdVaultZk(@Param("applicationContents") List<ApplicationContent> applicationContents,@Param("applicationId") int applicationId);
    //查询展开里面的内容
    @MapKey("")
    List<Map<String,Object>>selectProductByIdZkContent(@Param("finProductId") int finProductId,@Param("applicationId") int applicationId,@Param("lastLevel") int lastLevel);
    //查询可以展开的产品
    @MapKey("")
    List<Map<String,Object>> selectCanZk(@Param("finProductId") int finProductId,@Param("applicationId") int applicationId);

    //查询该申请单中的全部内容
    @Select("select * from application_content where application_id = #{applicationId}")
    List<ApplicationContent>selectAll(@Param("applicationId") int applicationId);







    //查询该申请单中是否存在已经有出库信息的
    @Select("select count(*) from application_content where application_id = #{applicationId} and actual_number>0")
    boolean selectIfActualNumber(@Param("applicationId") int applicationId);


//    根据产品id，仓库，申请单id查询这个申请单中的入库信息
    @Select("select * from application_content where application_id = #{applicationId} and product_id = #{productId} and vault = #{vault}")
    List<ApplicationContent> selectRkNumberById(@Param("applicationId") int applicationId,@Param("productId") int productId,@Param("vault") int vault);

    //根据申请ID 更新app_price和申请数量
    void  updateAppPrice(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //根据申请单ID   只更新申请数量
    void  updateAppNumber(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //查询采购单生成的所有采购入库单的总和
    @Select("select product_id,fin_product_id,sum(actual_number) as actual_number\n" +
            "from (select * from application_content where application_id in (select id from application_form where id in (select new_app_form_id from relationship where old_app_form_id = #{appFormId}) and complete_status !=2 )\n" +
            ") as a GROUP BY product_id,fin_product_id")
    List<Map<String,Object>> selectCgAndRkSum(@Param("appFormId") int appFormId);

    //查看采购单是否已经全部申请入库了;返回true是还未全部申请入库
    @Select("select count(*) from application_content where application_id = #{applicationId} and actual_number<application_content.app_number")
    boolean selectIfRkOrder(@Param("applicationId") int applicationId);

    //查询采购单转话的采购入库单是否已经全部入库了
    @Select("select count(*) from application_content where application_id in(select new_app_form_id from relationship where old_app_form_id = #{applicationId}) and actual_number<application_content.app_number")
    boolean selectIfCgRkOrder(@Param("applicationId") int applicationId);


    //查询该申请单是否已经完成了(返回true表示该申请单未完成)
    @Select("select count(*) from application_content where app_number >actual_number and application_id = #{applicationId}")
    boolean selectIfComplete(@Param("applicationId") int applicationId);

    //查询是否已经存在出入库情况
    @Select("select count(*) from application_content where actual_number >0 and application_id = #{applicationId}")
    boolean ifActualNumber(@Param("applicationId") int applicationId);

    //申请订购单，循环更新采购申请单的已申请订购数量
    void updateAppDgNumber(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //申请质检单，循环更新订购申请单的已申请质检的数量
    void updateAppZjNumber(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //查询质检单的质检信息
    @Select("select * from (" +
            "select application_content.*,inspection.id as passId,pass_rate,pass,notes,product.material_number,product.`name`,unit from application_content inner join inspection on application_content.application_id = inspection.app_id and application_content.product_id = inspection.product_id and application_content.vault = inspection.vault\n" +
            "INNER JOIN product on application_content.product_id=product.id and product.vault = application_content.vault\n" +
            "\n" +
            "union all \n" +
            "\n" +
            "select application_content.*,inspection.id as passId,pass_rate,pass,notes,fin_product.fin_material_number as material_number,fin_product.fin_product_name as name,unit from application_content inner join inspection on application_content.application_id = inspection.app_id and application_content.product_id = inspection.product_id and application_content.vault = inspection.vault\n" +
            "INNER JOIN fin_product on application_content.product_id=fin_product.id and fin_product.vault = application_content.vault) as v where application_id = #{applicationId}")
    List<Map<String,Object>> selectZjAndAppContent(@Param("applicationId") int applicationId);



    @MapKey("id")
    //根据申请单查询最新的物料信息审核价格
    List<Map<String,Object>>selectNewPriceByAppId(@Param("applicationContents") List<ApplicationContent> applicationContents);

    //更新申请单的申请数量(更新退货数量)
    @Update("update application_content set application_content.app_number = app_number - #{appNumber} where id = #{id}")
    void updateReturnNumber(@Param("appNumber")BigDecimal appNumber,@Param("id") int id);










    // 根据ID查询申请单内容
    @Select("SELECT * FROM application_content WHERE id = #{id}")
    Map<String, Object> selectById(@Param("id") int id);

    // 根据申请单ID查询所有内容
    @Select("SELECT ac.*, p.name as product_name, p.material_number " +
            "FROM application_content ac " +
            "LEFT JOIN product p ON ac.product_id = p.id " +
            "WHERE ac.application_id = #{applicationId}")
    List<Map<String, Object>> selectByApplicationId(@Param("applicationId") int applicationId);

    // 更新实际数量
    @Update("UPDATE application_content SET actual_number = #{actualNumber} WHERE id = #{id}")
    int updateActualNumber(@Param("id") int id, @Param("actualNumber") double actualNumber);

    //更新申请数量（单个）
    @Update("update application_content set app_number = #{appNumber} where id = #{id}")
    void updateAppNumberOnce(@Param("appNumber") double appNumber,@Param("id") int id);

    // 查询申请单内容和物料信息
    @Select("SELECT ac.*, p.name as product_name, p.material_number, p.unit, " +
            "       f.order_number, f.sort, f.complete_status, f.user_id as applicant_id " +
            "FROM application_content ac " +
            "LEFT JOIN product p ON ac.product_id = p.id " +
            "LEFT JOIN application_form f ON ac.application_id = f.id " +
            "WHERE ac.id = #{contentId}")
    Map<String, Object> selectContentWithDetails(@Param("contentId") int contentId);

    // 批量查询申请单内容
    @MapKey("id")
    List<Map<String, Object>> selectByIds(@Param("ids") List<Integer> ids);

    // 根据物料ID查询申请单内容
    @Select("SELECT * FROM application_content WHERE product_id = #{productId}")
    List<Map<String, Object>> selectByProductId(@Param("productId") int productId);

    // 统计申请单的物料数量
    @Select("SELECT COUNT(*) as count, SUM(app_number) as total_app_number, " +
            "SUM(actual_number) as total_actual_number " +
            "FROM application_content WHERE application_id = #{applicationId}")
    Map<String, Object> selectStatsByApplication(@Param("applicationId") int applicationId);

    // 查询已到货的物料
    @Select("SELECT * FROM application_content WHERE application_id = #{applicationId} AND actual_number > 0")
    List<Map<String, Object>> selectArrivedByApplication(@Param("applicationId") int applicationId);

    //根据申请单内容ID查询单个申请单内容
    @Select("select * from application_content where id = #{id}")
    ApplicationContent selectByContentId(@Param("id") int id);

    //根据申请单Id,productId,vault查询申请内容
    @Select("select * from application_content where application_id = #{applicationId} and product_id = #{productId} and vault = #{vault}")
    ApplicationContent selectByAppProduct(@Param("applicationId") int applicationId,@Param("productId") int productId,@Param("vault") int vault);





    ApplicationContent selectByIdCg(Integer id);          // 根据ID查询
    int insert(ApplicationContent content);             // 新增
    int update(ApplicationContent content);             // 修改
    int deleteById(Integer id);                         // 删除
    List<ApplicationContent> selectList();              // 查询列表
    int updateActualNum(@Param("id") Integer id, @Param("actualNumber") BigDecimal actualNumber); // 更新实际数量

    @MapKey("id")
    List<Map<String, Object>> getAppAndDetail(Integer contentId);



    // 分页查询数据
    @MapKey("id")
    List<Map<String,Object>> selectAppAndDetailByPage(
            @Param("start") int start,
            @Param("pageSize") Object pageSize
    );

    // 查询总条数
    int selectTotalCount();

    // 自动保存主表（改名后）
    int autoSaveApplicationContent(Map<String,Object> map);

    // 插入复制的主表数据（改名后）
    int insertCopiedApplicationContent(Map<String,Object> map);

    // 更新申请数量（改名后）
    int updateApplicationNumber(@Param("id")Integer id,@Param("appNumber")Integer appNumber);

}
