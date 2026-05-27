package com.hongbang.service;

import com.hongbang.pojo.ProductExamine;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProductExamineService {

    //添加审核的物料信息
    void addProductExamine(ProductExamine productExamine);

    //审核
    void updateStatus(ProductExamine productExamine);

    //查询所有审核内容
    @Select("select * from product_examine")
    List<ProductExamine> selectAll();


    //查询所有数据（带物料号、申请人、审核人）
    List<Map<String,Object>>selectAllData();

    //根据物料id查询产品的属性
    List<Map<String,Object>>selectAttribute(int productId,int vault);

    //查询自己申请的物料信息
    List<Map<String,Object>>selectMyApplications(int userId);

    //删除
    void deleteApplication(int id);


}
