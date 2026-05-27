package com.hongbang.service;

import com.hongbang.pojo.FormCk;

import java.util.List;
import java.util.Map;

public interface FormCkService {
    //新增出库条目信息
    void add(List<FormCk> formCks);

    //根据id产品id和仓库查询上一次的记录
    FormCk selectLast(int productId,int vault);


    //根据时间查询出库信息
    List<Map<String,Object>> selectCkFormListBy(Map<String,Object> maps);

    //每天查询出库的情况
    List<Map<String,Object>> selectTodayCk(String startTime, String endTime);

    //查询某个产品在这段时间内的总出库量
    Map<String, Object> selectSumCk(String startTime,  String endTime,int productId, int vault);

    //        -----------------------------------------------------
//        查询form_ck表中该物料信息的最新出库情况，要查看入库单信息
    List<FormCk> selectNewCkById(int productId, int vault);

    //根据申请单id，物料id，仓库查询出库情况
    List<FormCk> selectNewCkByFormId(int productId, int vault, int appFormId);


    //根据日期，申请单id，物料id，仓库，查询今天的总出库量
    double selectCkSumByForm(int appFormId, int productId, int vault,String startTime, String endTime);

    // 查询该申请单中该物料信息上次的ckTotal是多少
    List<FormCk> selectNewCk(int productId, int vault,int appFormId);


    //根据appformid。productId，vault,时间，去查询在一段时间内的最新的ck_total
    FormCk selectCkTotalBetween(int appFormId,int productId, int vault, String startTime, String endTime);
}
