package com.hongbang.service;

import com.hongbang.pojo.FormRk;

import java.util.List;
import java.util.Map;

public interface FormRkService {
    //新增入库条目信息
    void add(List<FormRk> formRks);

    //根据时间查询入库信息
    List<Map<String,Object>> selectRkFormListBy(Map<String,Object> maps);



    //查询最新一批的申请信息
    List<Map<String,Object>> selectNewApp(int appFormId);

    //每天查询入库的情况
    List<Map<String,Object>> selectTodayRk(String startTime,String endTime);

    //查询该申请单的物料是否今天有发票存在
    List<FormRk> selectFpByTime(int appFormId, int productId, int vault,String startTime,String endTime);

    //查询今天之前的最新价格信息
    FormRk selectLastFormRk(int productId, int vault,  String actualPrice,String rkTime);


    //查询某个物料信息在这段时间内的入库数量
    String selectSumRk(String startTime,String endTime,int productId,int vault);

    //查询某个物料信息在这段时间内的退货入库数量
    String selectThSumRk(String startTime,String endTime,int productId,int vault);





    //查询在某个时间段以后的某个物料信息的入库所有的数据
    List<Map<String,Object>> selectAppNumberAfterTime(int productId, int vault,String rkTime);

    //查询所有申请单中入库的某个物料信息的申请单
    List<Map<String,Object>> selectAppNumberAllTime(int productId, int vault);



    //查询一段时间内的最新价格（没有出库信息的时候用）
    FormRk selectNew(String startTime, String endTime,int productId, int vault, int appFormId);


    //查询在一个时间之前的的最新的价格(不是发票价格)
    FormRk selectNewPriceNoFp(int productId, int vault,int appFormId,String rkTime);

    //查询某个申请单中的某个物料信息的入库情况
    Map<String,Object> selectByAppIdAndId(int appFormId, int productId,int vault);

    //查询今天的所有发票信息
    List<FormRk> selectFpToday( String startTime, String endTime);

    //查询该申请单中的物料信息在小于该发票时间的最新价格，可能存在多张发票，也可能不存在，，所以都要查
    FormRk selectNewPriceSort(int productId,int vault, int appFormId,  String rkTime);



    //查询这个申请单的入库时间，然后去查询所有的入库时间大于这个申请单并且物料id是product的,入库数量以及价格信息
    List<Map<String,Object>> selectFormRkAndAppNumberByTime(int productId,int vault, int appFormId);


    //根据appFormId,productId,vault,rk_time查询最新价格，包括发票价格
    double selectPrice(int appFormId, int productId,  int vault);

    //根据productId查询所有的入库信息(包括数量)
    List<Map<String,Object>>selectByProductId( int productId);


    //查询上次的价格(要获取第二个，存在发票的时候才能用)
    List<FormRk>selectLastPrice( int appFormId,int productId, int vault);

    //查询某个物料还未从入库单出完的所有入库单信息
    List<Map<String,Object>> selectAllCanCkByProductId(int productId, int vault);

}
