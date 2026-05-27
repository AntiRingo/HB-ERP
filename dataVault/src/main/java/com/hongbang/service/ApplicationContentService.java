package com.hongbang.service;

import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ApplicationContentService {

    //批量添加申请单中所有的物料内容
    void addAll(ApplicationForm applicationForm, List<ApplicationContent>applicationContents, List<JSONArray>jsonArrays);

    void addAllCf(ApplicationForm applicationForm, List<ApplicationContent>applicationContents, List<JSONArray>jsonArrays);

    //生成添加新的订购单
    void addAllDg(ApplicationForm applicationForm,List<ApplicationContent>applicationContents);

    void addAllZj(ApplicationForm applicationForm,List<ApplicationContent>applicationContents);

    //查看申请单数据(电子仓库)
    List<ApplicationContent> selectByAppIdDz(int applicationId);

    //查看申请单数据(产品仓库)
    List<ApplicationContent> selectByAppIdCp(int applicationId);



    //查询申请单数据的详细信息（电子仓库物料信息）
    List<Map<String,Object>> selectAppContentDz(List<ApplicationContent> applicationContents);

    //查询申请单数据的详细信息（产品仓库物料信息）
    List<Map<String,Object>> selectAppContentCp(List<ApplicationContent> applicationContents);


    //更新申请单已出库信息,电子仓库，产品仓库
    void updateAllCk( List<ApplicationContent> applicationContents,List<Product>products,List<FinProduct> finProducts,List<Log>logs);

    //更新申请单已入库信息,电子仓库，产品仓库
    void updateAllRk( List<ApplicationContent> applicationContents,List<Product>products,List<FinProduct> finProducts,List<Log>logs);

    //更新申请单已经申请的质检信息
    void updateAllZj(List<ApplicationContent> applicationContents,List<Product>products,List<FinProduct> finProducts,List<Log>logs,List<Inspection>inspectionList);

    //更新质检的状态（只更新让步接收）
    void updateZbjs(List<Inspection>inspectionList);


        //查询申请单中产品的属性信息(电子仓库)
    List<Map<String,Object>>selectAttDz(int applicationId);

        //查询申请单中产品的属性信息(电子仓库)
    List<Map<String,Object>>selectAttCp(int applicationId);



//
//    //更新订单信息（撤销后提交的）
//    void updateByProveAll(ApplicationForm applicationForm,int applicationId,List<ApplicationContent>applicationContents);


    //根据id查询存放位置（电子仓库）
    List<Map<String,Object>> selectLocationDz(List<ApplicationContent>applicationContents);

        //根据id查询存放位置（产品仓库）
    List<Map<String,Object>> selectLocationCp(List<ApplicationContent>applicationContents);

    //查询该申请单要制造什么
    List<Map<String,Object>>selectWhatsThis(int applicationId);


    //点击查看自己的申请单复原过程-------------------------------------------------------------
//1.查询lastlevel=0并且fin_product_id=0的产品
    List<Map<String,Object>> selectOneLevel(int applicationId);

//2.查询lastlevel=0并且fin_product_id!=0的产品(这里查询的都是已经展开的，所以肯定是成品库中的)
    List<Map<String,Object>> selectOneLevelZk(int applicationId);
    //查询展开里面的内容
    List<Map<String,Object>>selectProductByIdZkContent( int finProductId, int applicationId,int lastLevel);
    //查询可以展开的产品
    List<Map<String,Object>> selectCanZk(int finProductId,int applicationId);







    //查询该申请单中是否存在已经有出库信息的
    boolean selectIfActualNumber(int applicationId);

    //查询申请单的所有物料信息单价
    List<Map<String,Object>> selectPrice(List<ApplicationContent>applicationContents);







    //    根据产品id，仓库，申请单id查询这个申请单中的入库信息
    List<ApplicationContent> selectRkNumberById(int applicationId,int productId,int vault);



    //根据申请ID 更新app_price
    void  updateAppPrice(List<ApplicationContent> applicationContents);

    //根据申请单ID   只更新申请数量
    void  updateAppNumber(List<ApplicationContent> applicationContents);




    //查询采购单生成的所有采购入库单的总和
    List<Map<String,Object>> selectCgAndRkSum(int appFormId);


    //查询该申请单中的全部内容
    List<ApplicationContent>selectAll(int applicationId);

    //查询该申请单是否已经完成了(返回true表示该申请单未完成)
    boolean selectIfComplete(int applicationId);

    //查询是否已经存在出入库情况
    boolean ifActualNumber( int applicationId);




    //查询所有的申请单数据（两个仓库的）
    List<Map<String,Object>>selectByAppIdAllVault(int applicationId);

    //查询质检单的质检信息
    List<Map<String,Object>> selectZjAndAppContent(int applicationId);





    // 主表操作
    ApplicationContent getContentById(Integer id);
    int saveContent(ApplicationContent content);
    int editContent(ApplicationContent content);

    // 附表操作
    ApplicationDetail getDetailByContentId(Integer contentId);
    int saveDetail(ApplicationDetail detail);
    int editDetail(ApplicationDetail detail);

    // 分批到货
    void addReceiveBatch(ApplicationReceiveBatch batch);
    List<ApplicationReceiveBatch> getBatchList(Integer contentId);
    BigDecimal countTotalReceiveNum(Integer contentId);
    void syncActualNumber(Integer contentId);

    List<Map<String, Object>> getAppAndDetail(Integer contentId);

    /**
     * 分页查询采购物料数据
     * @param currentPage 当前页码
     * @param pageSize 每页条数
     * @return 分页实体
     */
    PageBean<Map<String,Object>> selectAppAndDetailPage(Object currentPage, Object pageSize);

    // 单行自动保存入库
    void autoSaveData(Map<String,Object> saveMap);

    // 复制新增一行数据
    Integer copyRowData(Integer oldId,Integer newNum);

    // 修改物料申请数量
    void editApplicationNum(Integer id,Integer appNum,Integer addNum,String date);
}
