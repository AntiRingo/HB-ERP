package com.hongbang.service;

import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.TakeOrder;

import java.util.Map;

public interface TakeOrderService {

    //给新申请的申请单添加状态
    void   addTakeOrder(TakeOrder takeOrder);

    //接单
    void updateTakeOrder(TakeOrder takeOrder);

    //检查是否已经被接单了,返回true就是未接单
    boolean selectIfJd(int appFormId);

    //部长查看所有的接单情况
    PageBean<Map<String,Object>> selectAllTakeOrder(int begin, int size, boolean ck,  boolean rk, boolean cg, boolean zj,boolean dg);
}
