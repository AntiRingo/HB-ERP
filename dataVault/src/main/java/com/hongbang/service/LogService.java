package com.hongbang.service;

import com.hongbang.pojo.PageBean;

import java.util.List;
import java.util.Map;

public interface LogService {


    //查询入库日志标题
    PageBean<Map<String,Object>>  storageRecordTitle(int begin,int size);

    //查询出库日志标题
    PageBean<Map<String,Object>>  outboundRecordTitle(int begin,int size);


    //查询该物料是否存在出入库记录
    boolean selectIfLog(int productId,int vault);

    //查询操作人(出库)
    List<Map<String,Object>> selectManagerCK();

    //查询操作人(入库)
    List<Map<String,Object>> selectManagerRK();

    //日志筛选
    PageBean<Map<String,Object>>screen(Map<String,Object> maps, int begin, int size);

    //日志模糊查询（根据物料号和订单号）
   PageBean<Map<String,Object>> search(String str,int sort, int begin, int size);

    //在一段时间内统计已经存在的物料出库还是入库的总量
    PageBean<Map<String,Object>> Statistics( Map<String,Object>maps, int currentPage,int pageSize);

    //查询某个申请单中是否存在未签字的日志(让申请人签字)
    boolean selectWqz(int appFormId);

    //申请人签字
    void updateQz(String url,int appFormId);

    //查询该申请单需要签字的产品内容
    List<Map<String,Object>>selectQzProduct(int appFormId);

    //查询一个申请人是否存在未签字的申请单
    boolean selectIfQzByUserId(int userId);
}
