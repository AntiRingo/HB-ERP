package com.hongbang.service;

import com.hongbang.pojo.FinProduct;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;

import java.util.List;
import java.util.Map;

public interface FinProductService {
    //查询该分类下是否有物料
    boolean selectByParentId(int parentId);


    //查询该分类下的所有物料( )
    List<FinProduct> selectAllInSort(int parentId);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    List<FinProduct>selectAllInSortDeleteSign(int parentId, String name);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    List<FinProduct>selectAllInSortDeleteSignUpdate(int parentId,String name, int id);

    //查询是否有物料存在
    boolean ifProduct();

    //查询所有的物料信息
    List<FinProduct> selectAll();

    //新增物料
    void add(FinProduct finProduct,int userId);

    //删除物料
    void deleteById(int id);


    //重新启用
    void enableFinProduct(int id);

    //真正删除
    void deleteReally(int id);

    //模糊查询(搜索已弃用的物料信息，不分页)
    List<Product> searchAbandoned(String str);

    //根据已有的物料id，筛选出来在某个分类下的物料id
    List<Integer> selectPidBySidAndPid(int sortId, List<Integer> productsId);

    //查询当前分类下有没有物料名称相同的物料(添加)
    boolean selectAddNameIfExist(FinProduct finProduct);

    //查询物料号是否重复(新增物料的时候)
    boolean selectMN( int finSortId, String finMaterialNumber);

    //获取物料信息进行显示
    PageBean<FinProduct> selectByPid(int finSortId, int begin, int size);

    //获取物料信息进行回显
    List<FinProduct> selectById( int id);

    //获取物料信息进行回显
    List<Product> selectByIdAsProduct( int id);

    //物料信息修改
    void updateById(FinProduct finProduct);

    //查询当前分类下有没有物料名称相同的物料(更新)
    boolean selectNameIfExist(FinProduct finProduct);

    //查询物料号是否重复(更新物料的时候)
    boolean selectUpMN( int finSortId, String finMaterialNumber, int id);

    //根据sortId查询是否有物料存在
    boolean selectIfProduct(int finSortId);

    //查询（添加BOM表信息时查询两个表中的数据）
    PageBean<Map<String,Object>> searchFromTwoTable(String str,int begin, int size);

//    //查询copy内容
//    List<Product> selectCopyContent(List<FinBom> finBoms);

    //查询该分类下的所有产品信息，不包括自己
    List<FinProduct> selectCopyBySort( int id);

    //查询当前分类下的信息不包括自己
    List<FinProduct> selectSearchCopyBySort(int id,String str);


    //显示页面使用list<Product>是正确的   ------------------------------------------------------------------------------------------------------------------------


    //查询该分类下的所有物料( )
    List<Product> selectAllInSortDisplay( int finSortId);


    //查询product基本信息
    List<Product>selectProductByAllIdDisplay(List<Integer>integers);

    //筛选后的结果，按价格正序
    List<Product>BrandAscDisplay( List<Integer>integers);


    //筛选后的结果，按价格倒叙
    List<Product>BrandDescDisplay( List<Integer>integers);


    //查询该分类下的所有物料,根据库存数量倒叙
    List<Product>NumberDescDisplay( List<Integer>integers);



    //查询该分类下的所有物料,根据库存数量正序
    List<Product>NumberAscDisplay( List<Integer>integers);

    //查询该分类下所有的物料信息，分页查询
    PageBean<Product>selectAllInSortLimitDisplay(List<Map<String,Object>> maps,int begin,int size,int ifs,int price,int priceDesc,int number,int numberDesc);

    //查询该分类下所有的物料信息，分页查询
    PageBean<Product>selectAllInSortLimitDisplayOnly(int parentId,int begin,int size,int ifs,int price,int priceDesc,int number,int numberDesc);



    //查询已经弃用的物料信息
    PageBean<Product> selectAbandonedFinProduct(int begin,int size);

    //导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    List<FinProduct> selectProductByMaterialNumber(String materialNumber);

//    搜索使用--------------------------------------------
//搜索功能，不带单位
    List<Integer> searchProduct(String str,int parentId);

    //搜索功能带单位
    List<Integer> searchProductUnit(String str,int parentId);



    //更新价格
    void updatePrice(String price,int id);

}
