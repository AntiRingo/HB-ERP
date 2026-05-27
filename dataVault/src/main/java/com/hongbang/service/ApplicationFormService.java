package com.hongbang.service;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.PageBean;

import java.util.List;
import java.util.Map;

public interface ApplicationFormService {

//    //添加申请表
//    void add(ApplicationForm applicationForm);

    //删除申请表
    void delete(int id);

    //查询申请单信息（包含申请人信息）
    PageBean<Map<String,Object>> selectAllApplication(int sort, int currentPage, int pageSize,int ckId,boolean a,boolean ck,boolean rk,boolean cg,boolean zj,boolean dg);


    //根据用户ID查询该用户的申请单
    PageBean<ApplicationForm> selectByUserId(Map<String,Object> maps, int currentPage, int pageSize);


    //更改申请单为撤销状态
    void updateRevoke(int id);

    //查看是否有新增的申请信息(入库申请)
    List<Integer> selectIfNewApplicationRk();

    //查看是否有新增的申请信息(出库申请)
    List<Integer> selectIfNewApplicationCk();


    //点击查看后将申请单从新增状态改为取货中状态
    void updateStatusTwo(int id);

    //申请单筛选
   PageBean<Map<String,Object>> screen(Map<String,Object> maps,int begin,int size,int ckId,boolean a);


    //根据申请类型获取筛选人
    List<Map<String,Object>> screenUserId(int sort,int ckId,boolean a);


    //模糊查询
   PageBean<Map<String,Object>>searchOrderNumber(String str,int sort,int begin,int size,int ckId,boolean a);

    //将申请单状态修改为已出库
    void updateStatusFour(int id);

    //查询是否存在已出库状态的申请单
    boolean selectPickUp(int userId);

    //修改申请单状态
    void updateStatus( int status,int id);

    //根据id查询申请单信息
    List<ApplicationForm> selectById(int id);

    //根据用户id查询该用户的申请单（已完成的和已撤销的除外）
    List<ApplicationForm> selectAppFormNotCompleteAndDelete(int userId);



    //仓库查看已经审核通过的申请
    List<ApplicationForm>depotExamine();



    //部长查看需要自己审核的申请单
    PageBean<ApplicationForm>ministerExamine( int department,int minister,int currentPage,int pageSize);

    //部长筛选
    PageBean<ApplicationForm> ministerScreen(Map<String,Object> maps,int begin,int size,int department);


    //部长显示本部门的已经申请的人员（筛选使用）
    List<Map<String,Object>> ministerScreenUser(Map<String,Object> maps,int department);

    //部长显示申请部门(筛选使用)
    List<Map<String,Object>> ministerScreenDepartment(Map<String,Object> maps,int department);



    //根据申请单id查询是否总价钱
    List<Map<String,Object>> selectPriceAndCount(List<ApplicationForm>applicationForms);


    //根据id查询申请单状态
    List<Map<String,Object>>selectComplete(int id);

    //更新申请单上的审核部门
    void updateCirculation(int circulation,int id);

    //查询审核流程
    List<Map<String,Object>>selectLc(int appFormId);

    //修改circulationBoss,这个表示都审核完了，下一个该总经理了
    void updateCirculationBoss(int id);

    //更新不通过的原因
    void updateRefuse(String refuse,int id);


    //查询是否有新的申请单
    List<Integer> selectIfExamine(int sort,int department);

    //查询申请单的发票是否已经全部上传并审核完成
    boolean selectInvoiceSign(int id);


    //查询是否存在已出库状态的申请单
    List<Integer> selectPickUpAppId(int userId);


    //根据申请单id查询总价（只包含零件，成品价格在前端查询）
    List<Map<String,Object>> selectPriceAndCountLj(List<ApplicationForm>applicationForms);


    //接单使用（查询所有出库，入库的并且是待查看状态的，并且是未接单状态的申请单）
    List<ApplicationForm> selectTakeOrder(boolean ck,boolean rk,boolean cg,boolean zj,boolean dg);

    PageBean<ApplicationForm> selectDgByUserId(int userId,int currentPage, int size);



    //查询自己申请的质检单
    PageBean<ApplicationForm>selectMyZj(int userId,int currentPage, int size);


    //申购单（即采购单）不需要进行接单，该类型的申请单由采购部部长进行拆分
    PageBean<Map<String,Object>> selectAllCgApplication(int begin, int size, boolean ck, boolean rk, boolean cg,boolean zj,Map<String,Object> maps);

    //订购单，这个需要自己接单
    PageBean<Map<String,Object>>selectAllDgApplication(int begin,int size,boolean ck, boolean rk, boolean cg,  boolean zj, Map<String,Object> maps, int userId);

    // 获取申请单状态
    int getApplicationStatus(int applicationId);

    // 获取申请单详细信息
    ApplicationForm getApplicationById(int applicationId);

    // 更新申请单状态
    boolean updateStatusNew(int applicationId, int status);

    // 获取申请单状态（包含详细信息）
    Map<String, Object> getApplicationStatusDetail(int applicationId);

}
