package com.hongbang.service;

import com.hongbang.pojo.MaterialReturnReceive;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MaterialReturnReceiveService {
    int saveReturnReceive(MaterialReturnReceive receive);



    //查询未分配的信息
    @MapKey("id")
    PageBean<Map<String,Object>> selectNoFp(@Param("page") int page, @Param("pageSize") int pageSize);


    //根据contentId查询申请人都有谁（现在的contentID是质检单）
    List<Map<String,Object>> selectAppUser (int contentId, int productId,  int vault);

    //更新让步接收修改人
    void updateReceiveUser(int receiveUser,int id);

    //根据product_id,vault查询物料信息
    Product selectProductOne(int id, int vault);

    //查询质检单的退货以及让步情况
    List<MaterialReturnReceive> selectReturnReceiveByAppId(int id);

    //查询所有的退货信息
    PageBean<Map<String,Object>>selectTh(int page,int pageSize);


    //设置退货后是否继续申购物料信息
    void updateContinue(int status,int id);
}