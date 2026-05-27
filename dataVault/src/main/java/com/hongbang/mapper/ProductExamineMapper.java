package com.hongbang.mapper;

import com.hongbang.pojo.ProductExamine;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ProductExamineMapper {
    //添加审核的物料信息
    void addProductExamine(ProductExamine productExamine);

    //审核
    @Update("update product_examine set status = #{status} , note = #{note},examine_id = #{examineId} where id = #{id}")
    void updateStatus(ProductExamine productExamine);

    //查询所有审核内容
    @Select("select * from product_examine")
    List<ProductExamine> selectAll();

    //查询所有数据（带物料号、申请人、审核人）
//    @Select("select b.*,userName as examineName from ( select a.*,userName as appName from (select product_examine.*,product.name,material_number from product_examine left join product on product_examine.product_id = product.id)  as a left join user on a.user_id = user.id) as b  left join user on user.id=b.examine_id")
    @MapKey("id")
    List<Map<String,Object>>selectAllData();

    //根据物料id查询产品的属性
//    @Select(" select a.*,attribute_name.name,unit from (  select attribute_content.* from (select id from product where id =#{productId} and vault = #{vault}) as c left join attribute_content on attribute_content.productId = c.id ) as a LEFT JOIN attribute_name on a.parentId = attribute_name.id")
    @MapKey("id")
    List<Map<String,Object>>selectAttribute(@Param("productId") int productId,@Param("vault") int vault);

    //根据id查询
    @Select("select * from product_examine where id = #{id}")
    ProductExamine selectById(@Param("id") int id);


    //查询自己申请的物料信息
    @MapKey("id")
    List<Map<String,Object>>selectMyApplications(@Param("userId") int userId);

    //删除
    @Delete("delete from product_examine where id = #{id}")
    void deleteApplication(@Param("id") int id);






}
