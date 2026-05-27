package com.hongbang.service;

import com.hongbang.pojo.Balance;

import java.util.List;
import java.util.Map;

public interface BalanceService {

    //添加信息
    void addBalance(List<Balance> balanceList);

    //查询一段时间内的数据
    List<Map<String,Object>> selectAllByTime(String startTime, String endTime);

    //根据时间段，productId，vault查询剩余量
    Balance selectNumber(int productId,int vault,  String startTime,  String endTime);


    //查询上期结存
    Balance selectLast(int productId,int vault, String startTime);


    //删除
    void delete(String time);

    //查询最新的出库数量以及最新的价格
    List<Map<String,Object>> selectNewPriceAndNumber(int productId, int vault);
}
