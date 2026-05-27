package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ProductMapper {

    //获取物料信息进行显示
    @Select("select * from product where deleteSign = 0 and parentId = #{parentId} limit #{begin},#{size}")
    List<Product>selectByPid(@Param("parentId") int parentId,@Param("begin") int begin,@Param("size") int size);
    //查询总数
    @Select("select count(*) from product where deleteSign = 0 and parentId = #{parentId} ")
    int totalCounts(@Param("parentId") int parentId);
    //新增物料
    void add(Product product);

    //获取物料信息进行回显
    @Select("select * from product where id=#{id}")
    List<Product> selectById(@Param("id") int id);

    //物料信息修改
    @Update("update product set name=#{name},url=#{url},material_number=#{materialNumber},brand=#{brand},description=#{description},unit = #{unit},priceUnit = #{priceUnit} where id = #{id}")
    void updateById(Product product);

    //删除物料
//    @Delete("delete from product where id =#{id}")
    @Update("update product set deleteSign = 1 where id = #{id}")
    void deleteById(@Param("id") int id);

    //真正删除
    @Delete("delete from product where id =#{id}")
    void deleteReally(@Param("id") int id);

    //重新启用
    @Update("update product set deleteSign = 0 where id = #{id}")
    void enableProduct(@Param("id") int id);

    //新增物料的同时增加后来添加的属性
//    void addAttribute(@Param("attributes") List<AttributeName>attributes);

    //查询该分类下是否有物料
    @Select("select count(*) from product where parentId = #{parentId}")
    boolean selectByPrentId(@Param("parentId") int parentId);


    //模糊查询(搜索)
    @Select("select * from product where deleteSign = 0 and concat(product.name,product.description,product.brand,product.material_number) LIKE CONCAT('%',#{str},'%')  limit #{begin},#{size}")
    List<Product> search(@Param("begin") int begin, @Param("size")int size, @Param("str") String str);
    //查询总数
    @Select("select count(*) from product  where deleteSign = 0 and concat(product.name,product.description,product.brand,product.material_number) LIKE CONCAT('%',#{str},'%')")
    int totalCount(@Param("str") String str);

    //模糊查询(搜索已弃用的物料信息)
    @Select("select * from product where deleteSign = 1 and concat(product.name,product.description,product.brand,product.material_number) LIKE CONCAT('%',#{str},'%')")
    List<Product> searchAbandoned(@Param("str") String str);

    //查询该分类下的所有物料( )
    @Select("select * from product where deleteSign = 0 and parentId = #{parentId}")
    List<Product>selectAllInSort(@Param("parentId") int parentId);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    @Select("select * from product where parentId = #{parentId} and name = #{name}")
    List<Product>selectAllInSortDeleteSign(@Param("parentId") int parentId,@Param("name") String name);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    @Select("select * from product where parentId = #{parentId} and name = #{name} and id !=#{id}")
    List<Product>selectAllInSortDeleteSignUpdate(@Param("parentId") int parentId,@Param("name") String name,@Param("id") int id);


    //查询该分类下所有的物料信息(包含分类下的分类)，分页查询
    List<Product>selectAllInSortLimit(@Param("maps") List<Map<String,Object>> maps, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);
    //查询该分类下所有的物料信息数量，配合分页使用

    int selectAllInSortLimitCount(@Param("maps") List<Map<String,Object>> maps);

    //查询该分类下所有的物料信息(只有当前分类下的)，分页查询
    List<Product>selectAllInSortLimitOnly(@Param("parentId") int parentId, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);
    //查询该分类下所有的物料信息数量，配合分页使用
    @Select("   select count(*) from product where deleteSign = 0 and  parentId =#{parentId}")
    int selectAllInSortLimitCountOnly(@Param("parentId") int parentId);

    //根据物料信息查询所有的物料信息，分页查询
    List<Product>selectLimitByProduct(@Param("products") List<Product>products, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);


    //根据已有的数据进行排序，分页查询
    List<Product>selectAllInSortLimitOrderBy(@Param("products")List<Product> products,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);



    //查询是否有物料存在
    @Select("select count(*) from product")
    boolean ifProduct();

    //查询物料号是否重复(新增物料的时候)
    @Select("select count(*) from product where parentId = #{parentId} and material_number = #{materialNumber}")
    boolean selectMN( @Param("parentId") int parentId, @Param("materialNumber") String materialNumber);

    //查询是否跟弃用的物料号重复
    @Select("select * from product where parentId = #{parentId} and material_number = #{materialNumber} and name = #{name} and deleteSign = 1")
    List<Product> selectMNAbandoned( @Param("parentId") int parentId, @Param("materialNumber") String materialNumber,@Param("name") String name);

    //查询物料号是否重复(更新物料的时候)
    @Select("select count(*) from product where parentId = #{parentId} and material_number = #{materialNumber} and id !=#{id}")
    boolean selectUpMN( @Param("parentId") int parentId, @Param("materialNumber") String materialNumber,@Param("id") int id);

    //根据sortId查询是否有物料存在
    @Select("select count(*) from product where parentId = #{parentId}")
    boolean selectIfProduct(@Param("parentId") int parentId);

    //查询当前分类下有没有物料名称相同的物料(更新)
    @Select("select count(*) from product where parentId = #{parentId} and name=#{name} and  id != #{id}")
    boolean selectNameIfExist(Product product);

    //查询当前分类下有没有物料名称相同的物料(添加)
    @Select("select count(*) from product where parentId = #{parentId} and name=#{name}")
    boolean selectAddNameIfExist(Product product);

    //查询所有的物料信息
    @Select("select * from product where deleteSign = 0")
    List<Product> selectAll();

    //查询已经弃用的物料信息
    @Select("select * from product where deleteSign = 1 limit #{begin},#{size}")
    List<Product> selectAbandonedProduct(@Param("begin") int begin,@Param("size") int size);
    @Select("select count(*) from product where deleteSign = 1")
    int selectAbandonedCount();

    //查询该分类最后插入的物料号，为了获取最后两位流水码
    @Select(" select material_number as materialNumber from product where id in (SELECT max(id)  from product where parentId = #{sortId})  ")
    List<Map<String,Object>> selectMaterialNumber(@Param("sortId") int sortId);

    //根据已有的物料id，筛选出来在某个分类下的物料id
     List<Integer> selectPidBySidAndPid(@Param("sortId") int sortId,@Param("productsId") List<Integer> productsId);


    //下面是excel表导入用到的
    //查询该分类下是否有产品
    @Select("select* from product where deleteSign = 0 and parentId = #{parentId}")
    List<Product> selectProductExistExcel(@Param("parentId") int parentId);



    //数量页面使用
    //查询总数

    //查询总数
    @Select("select  count(*) from product where deleteSign = 0")
    int selectTotalCount();

    //搜索，模糊查询
//    @Select("select id from product where concat(product.name,product.brand,product.description) like CONCAT('%',#{str},'%') and parentId = #{parentId}")
    //不带单位%str%
    List<Integer> searchProduct(@Param("str") String str,@Param("parentId") int parentId);





    //带单位%str
    List<Integer> searchProductUnit(@Param("str") String str,@Param("parentId") int parentId);

    //查询product基本信息
    List<Product>selectProductByAllId(@Param("integers") List<Integer>integers);

    //在搜索结果中搜索
    List<Integer>selectInSelectP(@Param("products") List<Product>products,@Param("str") String str);

//    //查询该分类下的所有物料,根据价格倒叙，分页
//    @Select("select * from product  where parentId = #{parentId} order by brand +0 desc limit #{begin},#{size} ")
//    List<Product>selectAllInSortByBrandDesc(@Param("parentId") int parentId,@Param("begin") int begin,@Param("size") int size);
//    //总数配合分页使用
//    //查询该分类下的所有物料,根据价格正序，分页
//    @Select("select * from product  where parentId = #{parentId} order by brand +0 limit #{begin},#{size}  ")
//    List<Product>selectAllInSortByBrandAsc(@Param("parentId") int parentId,@Param("begin") int begin,@Param("size") int size);
//
//    //查询该分类下的所有物料,根据库存数量倒叙，分页
//    @Select("select * from product  where parentId = #{parentId} order by number +0 desc limit #{begin},#{size}")
//    List<Product>selectAllInSortByNumberDesc(@Param("parentId") int parentId,@Param("begin") int begin,@Param("size") int size);
//    //查询该分类下的所有物料,根据库存数量正序，分页
//    @Select("select * from product  where parentId = #{parentId} order by number +0  limit #{begin},#{size}")
//    List<Product>selectAllInSortByNumberAsc(@Param("parentId") int parentId,@Param("begin") int begin,@Param("size") int size);


    //筛选后的结果，按价格倒叙
    List<Product>BrandDesc(@Param("integers") List<Integer>integers);


    //筛选后的结果，按价格正序
    List<Product>BrandAsc(@Param("integers") List<Integer>integers);


    //查询该分类下的所有物料,根据库存数量倒叙
    List<Product>NumberDesc(@Param("integers") List<Integer>integers);



    //查询该分类下的所有物料,根据库存数量正序
    List<Product>NumberAsc(@Param("integers") List<Integer>integers);



    //查询product基本信息根据价格正序，搜索
    List<Product>selectProductByBrandAsc(@Param("integers") List<Integer>integers);

    //查询product基本信息根据价格正序，倒叙
    List<Product>selectProductByBrandDesc(@Param("integers") List<Integer>integers);


    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectProductByNumberAsc(@Param("integers") List<Integer>integers);

    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectProductByNumberDesc(@Param("integers") List<Integer>integers);


    //根据本地储存在数据查询产品信息在前端显示
    Product selectProductByLocation(@Param("products") Product products);


    //根据物料id查询content 、mapping、range
    @Select("select c.name,material_number,c.att_name_id,productId,content,c.id,sort_id,begin_location,end_location,length,attribute_function.`range` from (select * from(select product.parentId,name,material_number,attribute_content.parentId as attNameId,productId,content from product LEFT JOIN attribute_content on product.id = attribute_content.productId where product.id=#{id} and deleteSign = 0) as a LEFT JOIN mapping on mapping.sort_id = a.parentId and mapping.att_name_id = a.attNameId) as c LEFT JOIN attribute_function on c.att_name_id = attribute_function.att_name_id")
    List<Map<String,Object>> jz(@Param("id") int id);

    //根据id循环更新物料号
    void updateMaterialNumberById(@Param("products") List<Product>products);

    //查询物料信息（添加BOM表的时候查看）
    @MapKey("id")
    List<Map<String,Object>>selectProductWhenAddBom(@Param("id") int id,@Param("vault") int vault);










//--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //入库出库操作
    void vault (@Param("applicationContents")List<ApplicationContent> applicationContents);

//显示页面使用--------------------------------------------------------------------------------------------------------------------------------------------------------------
    //搜索，模糊查询（搜索产品仓库的）
    //不带单位%str%
    List<Integer> searchFinProduct(@Param("str") String str,@Param("parentId") int parentId);

    //带单位%str
    List<Integer> searchFinProductUnit(@Param("str") String str,@Param("parentId") int parentId);

    //查询product基本信息
    List<Product>selectFinProductByAllId(@Param("integers") List<Integer>integers);

    //根据已有的数据进行排序，分页查询
    List<Product>selectAllInFinSortLimitOrderBy(@Param("products")List<Product> products,@Param("ifs") int ifs ,
                                             @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                             @Param("numberDesc") int numberDesc);

    //在搜索结果中搜索
    List<Integer>selectInSelectFP(@Param("products") List<Product>products,@Param("str") String str);


    //查询product基本信息根据价格正序，搜索
    List<Product>selectFinProductByBrandAsc(@Param("integers") List<Integer>integers);

    //查询product基本信息根据价格正序，倒叙
    List<Product>selectFinProductByBrandDesc(@Param("integers") List<Integer>integers);


    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectFinProductByNumberAsc(@Param("integers") List<Integer>integers);

    //查询product基本信息根据库存数量正序，搜索
    List<Product>selectFinProductByNumberDesc(@Param("integers") List<Integer>integers);

    //根据物料信息查询所有的物料信息，分页查询
    List<Product>selectLimitByFinProduct(@Param("products") List<Product>products, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);


//导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    @Select("select * from product where material_number = #{materialNumber} and deleteSign = 0")
    List<Product> selectProductByMaterialNumber(@Param("materialNumber") String materialNumber);



//    ----------------------------------------------------------------------------------------------------
//根据物料id和仓库查询价格
    double selectPrice(@Param("productId") int productId,@Param("vault") int vault);



    //根据物料号查询物料的信息
    @MapKey("id")
    List<Map<String,Object>> selectByMaterialNumber(@Param("materialNumber") String materialNumber);

    //根据物料号更新产品的数量
   void updateNumberByName(@Param("products") List<Product>products);

   //根据物料名称查询产品数据
   @Select("select * from product where name = #{name} and deleteSign = 0")
   List<Product> selectProductByName(@Param("name") String name);

   //采购入库，仓库入库时更新价格;只有零件库的
    void updatePrice(@Param("applicationContents") List<ApplicationContent> applicationContents);



    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    //结存使用
    @Select("select id,name,material_number,brand,number from product ")
    List<Product> selectJieCun();


    // 查询产品信息
    @Select("SELECT id, name, material_number, unit FROM product WHERE id = #{id}")
    Map<String, Object> selectProductInfo(@Param("id") int id);

}
