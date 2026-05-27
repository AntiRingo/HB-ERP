package com.hongbang.service;

import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.FinBomTitle;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;

import java.util.List;
import java.util.Map;

public interface FinBomService {


    //根据fin_id查询是否存在BOM表信息
   PageBean<Map<String,Object>> selectByPage(int finId);



    //循环添加BOM表信息
    void add( List<FinBom>finBoms,int finId);


    //查询当前BOM表下的所有产品信息，只要产品库的
    List<FinBom>selectBomVault(int finId);

    //查询某个物料是否在BOM表中被使用
    boolean ifUsed(int productId,int vault);

    //查询当前BOM表下的所有产品信息
    List<FinBom>selectBomVaultAll(int finId);

    //查询当前BOM表下的内容
    List<FinBom> selectBomContent(int finProductId);

 //查询BOM表中物料信息的属性信息
  List<Map<String,Object>> selectAttribute(int bomTitleId);

 //查询物料信息
  List<Product> selectProduct(List<FinBom> finBoms);

 //查询BOM表的价格和数量（零件）
 int selectPriceLj(int finProductId);

 //查询该BOM表中的产品信息
 List<Map<String,Object>>selectFinFromBom(int finProductId);

 //查询BOM表中的物料信息
 FinBom selectProductById(int bomTitleId,int productId,int vault);


 int totalCount(int finId);

 void addOther(FinBomTitle finBomTitle,List<FinBom> finBoms);

}
