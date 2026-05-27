package com.hongbang.service;

import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    //查询物料数据
    PageBean<Product> selectByPid( int parentId,int begin,int size);

    //获取信息进行回显
    List<Product> selectById(int id);

    //修改物料信息
    void updateById(Product product);

    //添加新物料
    void add(Product product,int userId);

    //删除物料
    void deleteById(int id);

    //真正删除
    void deleteReally(int id);

    //重新启用
    void enableProduct(int id);

    //查询该分类下是否有物料
    boolean selectByParentId(int parentId);

    //模糊查询(搜索功能)
    PageBean<Product> search(int currentPage, int size, String str);

    //模糊查询(搜索已弃用的物料信息，不分页)
    List<Product> searchAbandoned(String str);

    //查询该分类下的所有物料( )
    List<Product>selectAllInSort(int parentId);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时实用 )
    List<Product>selectAllInSortDeleteSign(int parentId,String name);

    //查询该分类下的所有物料(修改物料信息时查询是否跟已启用的物料信息比较时实用 )
    List<Product>selectAllInSortDeleteSignUpdate(int parentId, String name,int id);


    //查询该分类下所有的物料信息，分页查询(包含分类下的分类的物料信息)
    PageBean<Product>selectAllInSortLimit(List<Map<String,Object>> maps,int begin,int size,int ifs,int price,int priceDesc,int number,int numberDesc);

    //查询该分类下所有的物料信息，分页查询(只包含当前分类下的物料信息)
    PageBean<Product>selectAllInSortLimitOnly(int parentId,int begin,int size,int ifs,int price,int priceDesc,int number,int numberDesc);


    //根据已有的数据进行排序，分页查询
    List<Product>selectAllInSortLimitOrderBy(List<Product>products,int ifs,int price,int priceDesc,int number,int numberDesc);


    //根据物料信息查询所有的物料信息，分页查询
    PageBean<Product>selectLimitByProduct(List<Product>products,  int begin, int size, int ifs ,
                                       int price,int priceDesc, int number,
                                      int numberDesc);
    //根据物料信息查询所有的物料信息，分页查询
    PageBean<Product>selectLimitByFinProduct(List<Product>products,  int begin, int size, int ifs ,
                                          int price,int priceDesc, int number,
                                          int numberDesc);




    //查询是否有物料存在
    boolean ifProduct();

    //查询物料号是否重复(新增物料的时候)
    boolean selectMN(int parentId,String materialNumber);

    //查询是否跟弃用的物料号重复
    List<Product> selectMNAbandoned( int parentId,  String materialNumber,String name);

    //查询物料号是否重复(更新物料的时候)
    boolean selectUpMN(int parentId,String materialNumber,int id);

    //根据分类id查询该分类下是否有产品
    boolean selectIfProduct(int parentId);

    //查询当前分类下有没有物料名称相同的物料
    boolean selectNameIfExist(Product product);

    //查询当前分类下有没有物料名称相同的物料(添加)
    boolean selectAddNameIfExist(Product product);


    //查询所有的物料信息
    List<Product> selectAll();


    //查询已经弃用的物料信息
    PageBean<Product> selectAbandonedProduct(int begin,int size);

    //查询该分类最后插入的物料号，为了获取最后两位流水码
    List<Map<String,Object>> selectMaterialNumber(int sortId);

    //根据已有的物料id，筛选出来在某个分类下的物料id
    List<Integer> selectPidBySidAndPid(int sortId,List<Integer> productsId);



    //下面是excel表导入用到的
    //查询该分类下是否有产品

    List<Product> selectProductExistExcel(int parentId);



    //数量页面使用
    //查询总数
    int selectTotalCount();


    //搜索功能，不带单位
    List<Integer> searchProduct(String str,int parentId);

    //搜索功能带单位
    List<Integer> searchProductUnit(String str,int parentId);

    //查询product基本信息
    List<Product>selectProductByAllId(List<Integer>integers);

    //在搜索结果中搜索
    List<Integer>selectInSelectP(List<Product>products,String str);




//    //查询该分类下的所有物料,根据价格倒叙
//    List<Product>selectAllInSortByBrandDesc(int parentId);
//    //查询该分类下的所有物料,根据价格正序
//    List<Product>selectAllInSortByBrandAsc(int parentId);
//
//    //查询该分类下的所有物料,根据库存数量倒叙
//    List<Product>selectAllInSortByNumberDesc(int parentId);
//    //查询该分类下的所有物料,根据库存数量正序
//    List<Product>selectAllInSortByNumberAsc(int parentId);


    //筛选后的结果，按价格倒叙
    List<Product>BrandDesc(List<Integer>integers);

    //筛选后的结果，按价格正序
    List<Product>BrandAsc(List<Integer>integers);

    //查询该分类下的所有物料,根据库存数量倒叙
    List<Product>NumberDesc(List<Integer>integers);

    //查询该分类下的所有物料,根据库存数量正序
    List<Product>NumberAsc(List<Integer>integers);


    //查询product基本信息根据价格正序，搜索
    List<Product>selectProductByBrandAsc(List<Integer>integers);

    //查询product基本信息根据价格正序，倒叙
    List<Product>selectProductByBrandDesc(List<Integer>integers);


    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectProductByNumberAsc(List<Integer>integers);

    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectProductByNumberDesc(List<Integer>integers);

    //根据本地储存在数据查询产品信息在前端显示
     Product selectProductByLocation(Product products);


    //根据物料id查询content 和mapping
    List<Map<String,Object>> jz(int id);


    //根据id循环更新物料号
    void updateMaterialNumberById(List<Product>products);


//--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //入库出库操作
    void vault (List<ApplicationContent> applicationContents);

//显示页面使用----------------------------------------------------------------------------------------------------------------------------------------------------------------
    //搜索功能，不带单位
    List<Integer> searchFinProduct(String str,int parentId);

    //搜索功能带单位
    List<Integer> searchFinProductUnit(String str,int parentId);


    //查询product基本信息
    List<Product>selectFinProductByAllId(List<Integer>integers);

    //根据已有的数据进行排序，分页查询
    List<Product>selectAllInFinSortLimitOrderBy(List<Product>products,int ifs,int price,int priceDesc,int number,int numberDesc);




    //在搜索结果中搜索
    List<Integer>selectInSelectFP(List<Product>products,String str);

    //查询product基本信息根据价格正序，搜索
    List<Product>selectFinProductByBrandAsc(List<Integer>integers);

    //查询product基本信息根据价格正序，倒叙
    List<Product>selectFinProductByBrandDesc(List<Integer>integers);


    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectFinProductByNumberAsc(List<Integer>integers);

    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectFinProductByNumberDesc(List<Integer>integers);

    //查询物料信息（添加BOM表的时候查看）
    List<Map<String,Object>>selectProductWhenAddBom(int id,int vault);

    //导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    List<Product> selectProductByMaterialNumber(String materialNumber);


//---------------------------------------------------------------------------------
//根据物料id和仓库查询价格
    double selectPrice(int productId,int vault);


    //根据物料号查询物料的信息
    List<Map<String,Object>> selectByMaterialNumber(String materialNumber);


    //根据物料号更新产品的数量
    void updateNumberByName(List<Product>products);

    //根据物料名称查询产品数据
    List<Product> selectProductByName(String name);



    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    //结存使用
    List<Product> selectJieCun();
}
