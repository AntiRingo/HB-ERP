package com.hongbang.mapper;

import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.FinProduct;

import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface FinProductMapper {
    //查询该分类下是否有物料
    @Select("select count(*) from fin_product where deleteSign = 0 and fin_sort_id = #{parentId}")
    boolean selectByParentId(@Param("parentId") int parentId);

    //查询该分类下的所有物料( )
    @Select("select * from fin_product where deleteSign = 0 and fin_sort_id = #{finSortId}")
    List<FinProduct> selectAllInSort(@Param("finSortId") int finSortId);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    @Select("select * from fin_product where fin_sort_id = #{parentId} and fin_product_name = #{name}")
    List<FinProduct>selectAllInSortDeleteSign(@Param("parentId") int parentId,@Param("name") String name);

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    @Select("select * from fin_product where fin_sort_id = #{parentId} and fin_product_name = #{name} and id !=#{id}")
    List<FinProduct>selectAllInSortDeleteSignUpdate(@Param("parentId") int parentId,@Param("name") String name,@Param("id") int id);

    //查询是否有物料存在
    @Select("select count(*) from fin_product where deleteSign = 0")
    boolean ifProduct();

    //查询所有的物料信息
    @Select("select * from fin_product where deleteSign = 0")
    List<FinProduct> selectAll();

    //新增物料
    void add(FinProduct finProduct);

    //删除物料
//    @Delete("delete from fin_product where id =#{id}")
    @Update("update fin_product set deleteSign = 1 where id = #{id}")
    void deleteById(@Param("id") int id);

    //重新启用
    @Update("update fin_product set deleteSign = 0 where id = #{id}")
    void enableFinProduct(@Param("id") int id);

    //真正删除
    @Delete("delete from fin_product where id =#{id}")
    void deleteReally(@Param("id") int id);

    //查询当前分类下有没有物料名称相同的物料(添加)
    @Select("select count(*) from fin_product where fin_sort_id = #{finSortId} and fin_product_name=#{finProductName}")
    boolean selectAddNameIfExist(FinProduct finProduct);

    //查询物料号是否重复(新增物料的时候)
    @Select("select count(*) from fin_product where fin_sort_id = #{finSortId} and fin_material_number = #{finMaterialNumber}")
    boolean selectMN( @Param("finSortId") int finSortId, @Param("finMaterialNumber") String finMaterialNumber);

    //获取物料信息进行显示
    @Select("select * from fin_product where deleteSign = 0 and fin_sort_id = #{finSortId} limit #{begin},#{size}")
    List<FinProduct>selectByPid(@Param("finSortId") int finSortId,@Param("begin") int begin,@Param("size") int size);

    //查询总数
    @Select("select count(*) from fin_product where deleteSign = 0 and fin_sort_id = #{finSortId} ")
    int totalCounts(@Param("finSortId") int finSortId);

    //获取物料信息进行回显
    @Select("select * from fin_product where id=#{id}")
    List<FinProduct> selectById(@Param("id") int id);

    //获取物料信息进行回显
    @Select("select id, fin_sort_id as parentId, fin_product_name as name, url, fin_material_number as material_number, price, fin_description as description, fin_number as number, vault,deleteSign,unit,priceUnit  from fin_product where id=#{id}")
    List<Product> selectByIdAsProduct(@Param("id") int id);


    //根据已有的物料id，筛选出来在某个分类下的物料id
    List<Integer> selectPidBySidAndPid(@Param("sortId") int sortId,@Param("productsId") List<Integer> productsId);


    //物料信息修改
    @Update("update fin_product set fin_product_name=#{finProductName},url=#{url},fin_material_number=#{finMaterialNumber},price=#{price},fin_description=#{finDescription},unit = #{unit},priceUnit = #{priceUnit} where id = #{id}")
    void updateById(FinProduct finProduct);

    //查询当前分类下有没有物料名称相同的物料(更新)
    @Select("select count(*) from fin_product where fin_sort_id = #{finSortId} and fin_product_name=#{finProductName} and  id != #{id}")
    boolean selectNameIfExist(FinProduct finProduct);


    //查询物料号是否重复(更新物料的时候)
    @Select("select count(*) from fin_product where fin_sort_id = #{finSortId} and fin_material_number = #{finMaterialNumber} and id !=#{id}")
    boolean selectUpMN( @Param("finSortId") int finSortId, @Param("finMaterialNumber") String finMaterialNumber,@Param("id") int id);


    //根据sortId查询是否有物料存在
    @Select("select count(*) from fin_product where fin_sort_id = #{finSortId}")
    boolean selectIfProduct(@Param("finSortId") int finSortId);


    //查询（添加BOM表信息时查询两个表中的数据）
    @Select("(select * from product where concat(product.name,product.description,product.material_number) LIKE CONCAT('%',#{str},'%') ) UNION ALL(select * from fin_product where concat(fin_product_name,fin_description,fin_material_number) LIKE CONCAT('%',#{str},'%') ) LIMIT #{begin},#{size}")
    List<Map<String,Object>> searchFromTwoTable(@Param("str") String str, @Param("begin") int begin, @Param("size") int size);

   @Select(" select sum(a.b) as num from((select count(*) as b from product where concat(product.name,product.description,product.material_number) LIKE CONCAT('%',#{str},'%')UNION ALL(select count(*) as b from fin_product where concat(fin_product_name,fin_description,fin_material_number) LIKE CONCAT('%',#{str},'%') ) ) as a)")
    int totalCount(@Param("str") String str);

   //查询copy内容
    List<Product> selectCopyContent(@Param("finBoms")List<FinBom> finBoms);

    //查询该分类下的所有产品信息，不包括自己
    @Select("select * from fin_product where fin_sort_id in (select fin_sort_id from fin_product where id = #{id}) and deleteSign=0 and id !=#{id}  ")
    List<FinProduct> selectCopyBySort(@Param("id") int id);

    //查询当前分类下的信息不包括自己
    @Select("select * from fin_product where concat(fin_product_name,fin_description,fin_material_number) LIKE CONCAT('%',#{str},'%') and fin_sort_id in(select fin_sort_id from fin_product where id = #{id}) and id !=#{id} and deleteSign = 0")
    List<FinProduct> selectSearchCopyBySort(@Param("id") int id,@Param("str") String str);
//显示页面使用list<Product>是正确的   ------------------------------------------------------------------------------------------------------------------------


    //查询该分类下的所有物料( )
    @Select("select id, fin_sort_id as parentId, fin_product_name as name, url, fin_material_number as material_number, price, fin_description as description, fin_number as number, vault,deleteSign,unit,priceUnit from fin_product where deleteSign = 0 and fin_sort_id = #{finSortId}")
    List<Product> selectAllInSortDisplay(@Param("finSortId") int finSortId);



    //查询product基本信息
    List<Product>selectProductByAllIdDisplay(@Param("integers") List<Integer>integers);

    //筛选后的结果，按价格正序
    List<Product>BrandAscDisplay(@Param("integers") List<Integer>integers);

    //筛选后的结果，按价格倒叙
    List<Product>BrandDescDisplay(@Param("integers") List<Integer>integers);


    //查询该分类下的所有物料,根据库存数量倒叙
    List<Product>NumberDescDisplay(@Param("integers") List<Integer>integers);



    //查询该分类下的所有物料,根据库存数量正序
    List<Product>NumberAscDisplay(@Param("integers") List<Integer>integers);

    //查询该分类下所有的物料信息，分页查询
    List<Product>selectAllInSortLimitDisplay(@Param("maps") List<Map<String,Object>> maps, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                      @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                      @Param("numberDesc") int numberDesc);
    //查询该分类下所有的物料信息数量，配合分页使用

    int selectAllInSortLimitCount(@Param("maps") List<Map<String,Object>> maps);

    //查询该分类下所有的物料信息，分页查询
    List<Product>selectAllInSortLimitDisplayOnly(@Param("parentId") int parentId, @Param("begin") int begin, @Param("size") int size,@Param("ifs") int ifs ,
                                             @Param("price") int price, @Param("priceDesc") int priceDesc, @Param("number") int number,
                                             @Param("numberDesc") int numberDesc);
    //查询该分类下所有的物料信息数量，配合分页使用
    @Select("select count(*) from fin_product where deleteSign = 0 and fin_sort_id = #{parentId}")
    int selectAllInSortLimitCountOnly(@Param("parentId") int parentId);


    //查询已经弃用的物料信息
    @Select("select id, fin_sort_id as parentId, fin_product_name as name, url, fin_material_number as material_number, price, fin_description as description, fin_number as number, vault,deleteSign,unit,priceUnit from fin_product where deleteSign = 1 limit #{begin},#{size}")
    List<Product> selectAbandonedFinProduct(@Param("begin") int begin,@Param("size") int size);
    @Select("select count(*) from fin_product where deleteSign = 1")
    int selectAbandonedFinCount();

    //模糊查询(搜索已弃用的物料信息)
    @Select("select id, fin_sort_id as parentId, fin_product_name as name, url, fin_material_number as material_number, price, fin_description as description, fin_number as number, vault,deleteSign,unit,priceUnit from fin_product where deleteSign = 1 and concat(fin_product.fin_product_name,fin_product.fin_description,fin_product.price,fin_product.fin_material_number) LIKE CONCAT('%',#{str},'%')")
    List<Product> searchAbandoned(@Param("str") String str);

//导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    @Select("select * from fin_product where fin_material_number = #{materialNumber} and deleteSign = 0")
    List<FinProduct> selectProductByMaterialNumber(@Param("materialNumber") String materialNumber);


//    搜索--------------------------------------------------------------------------------
    //不带单位%str%
    List<Integer> searchProduct(@Param("str") String str,@Param("parentId") int parentId);

    //带单位%str
    List<Integer> searchProductUnit(@Param("str") String str,@Param("parentId") int parentId);

    //更新价格
    @Update("update fin_product set price = #{price} where id = #{id}")
    void updatePrice(@Param("price") String price,@Param("id") int id);





}
