package com.hongbang.mapper;

import com.hongbang.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinAttributeMapper {
//分类属性名--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该分类下的属性
    @Select("select * from fin_attribute_name where fin_sort_id = #{finSortId} and fin_att_public=0")
    List<FinAttributeName> selectAttributeNameByParentId(@Param("finSortId") int finSortId);

    //查询公共属性
    @Select("select * from fin_attribute_name where fin_sort_id = 0 and fin_att_public = 1")
    List<FinAttributeName> selectPublic();

    //根据id查询属性信息进行回显
    @Select("select * from fin_attribute_name where id =#{id}")
    List<FinAttributeName> selectAttributeNameById(@Param("id") int id);

    //查询该分类中的所有后添加的属性
    @Select("select * from fin_attribute_name where fin_sort_id = #{finSortId}")
    List<FinAttributeName>selectAttributeName(@Param("finSortId") int finSortId);

    //添加属性
//    @Insert("insert into attribute_name values ( #{id},#{parentId},#{name})")
    void add(FinAttributeName finAttributeName);

    //根据删除属性信息
    @Delete("delete from fin_attribute_name where id = #{id}")
    void deleteById(@Param("id") int id);

    //根据id更新属性信息
    @Update("update fin_attribute_name set fin_att_name = #{finAttName},fin_att_unit = #{finAttUnit} where id =#{id}")
    void updateAttributeNameById(FinAttributeName finAttributeName);
    //查询属性名表中是长度是否存在
    @Select("select fin_att_length from fin_attribute_name where id = #{id}")
    int selectIfLengthExist(@Param("id") int id);

    //根据id查询要修改的属性的信息进行回显
    @Select("select * from fin_attribute_name where id = #{id}")
    List<FinAttributeName>selectById(@Param("id") int id);

    //判断该属性是否是范围类型的
    @Select("select count(*) from fin_attribute_function where fin_att_name_id= #{id} and `range`=1")
    boolean selectIfRange(@Param("id") int id);

    //根据id加入length
    @Update("update fin_attribute_name set fin_att_length=#{finAttLength} where id = #{id}")
    void updateLength(@Param("finAttLength") int finAttLength,@Param("id") int id);

    //根据id删除属性信息
    @Delete("delete from fin_attribute_name where id = #{id}")
    void deleteAttributeNameById(@Param("id") int id);


    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    @Select("select count(*) from fin_attribute_name where fin_att_name = #{finAttName} and id!=#{id}")
    boolean selectUpdateNameExist(FinAttributeName finAttributeName);

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    @Select("select count(*) from fin_attribute_name where fin_att_name = #{finAttName}")
    boolean selectNameExist(@Param("finAttName") String finAttName);

    //添加公共属性
    void addPublic(FinAttributeName finAttributeName);

    //更新物料前查询信息用来构成输入框
    @Select("select fin_attribute_name.fin_att_name,fin_attribute_content.id,fin_attribute_content. fin_att_content,fin_att_unit,fin_attribute_content.fin_att_name_id FROM fin_attribute_name,fin_attribute_content where fin_attribute_name.id=fin_attribute_content.fin_att_name_id and fin_attribute_content.fin_att_name_id=#{finAttNameId} and fin_product_id=#{finProductId}")
    List<Map<String,Object>>selectBeforeContentUpdate(@Param("finAttNameId")int finAttNameId, @Param("finProductId") int finProductId);

    //查询时唯一标志的内容是否重复（添加时使用）
    @Select("select count(*) from fin_attribute_content where fin_att_name_id = #{parentId} and fin_att_content = #{content}")
    boolean selectUniqueContent(@Param("parentId") int parentId,@Param("content") String content);

    //查询时唯一标志的内容是否重复（修改时使用）
    @Select("select count(*) from fin_attribute_content where fin_att_name_id = #{parentId} and fin_att_content = #{content} and fin_product_id !=#{productId}")
    boolean selectUniqueContentUpdate(@Param("parentId") int parentId,@Param("content") String content,@Param("productId") int productId);


    //查询该属性内容有多少个，开启唯一值功能时判断使用
    @Select("SELECT count(*) from fin_attribute_content where fin_att_name_id = #{parentId} ")
    int selectAttributeContentCount(@Param("parentId") int parentId);

    //查询不重复的属性内容有多少个。开启唯一值功能时判断使用
    @Select("select count(*) from (SELECT DISTINCT  fin_att_content   from fin_attribute_content where fin_att_name_id = #{parentId}) as a")
    int selectAttributeContentDistinct(@Param("parentId") int parentId);


    //根据content 和 sortID查询productId；第二段
    List<Integer> selectPidByContentPid(@Param("parentId") int parentId,@Param("content") String content,@Param("productsId") List<Integer>productIds);

    @MapKey("")
        //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    List<FinAttributeContent> selectContentByLsAndPid(@Param("attNameId") int attNameId,@Param("productsId") List<Integer> productsId);

    //查询属性值以及编码是否被物料用到
    @Select("select count(*) from fin_attribute_content where fin_att_name_id= #{parentId} and fin_att_content = #{content}")
    boolean selectIfUse(@Param("parentId") int parentId,@Param("content") String content);






//分类属性内容--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该属性中是否有属性值被用到物料上
    @Select("select count(*) from fin_attribute_content where fin_att_name_id = #{finAttNameId}")
    boolean selectIfUseByParentId(@Param("finAttNameId") int finAttNameId);

    //查询该属性填写的所有内容是否为空
    @Select("select * from fin_attribute_content where fin_att_name_id = #{finAttNameId} ")
    List<FinAttributeContent>selectContentNull(@Param("finAttNameId") int finAttNameId);

    //增加完属性后，循环向content表中添加数据
    void addContentAfterName(@Param("finAttributeContents")List<FinAttributeContent>finAttributeContents);


    //查询当前物料属性中所有被用到的信息
    @Select("select * from fin_attribute_content where fin_att_name_id = #{finAttNameId}")
    List<FinAttributeContent>selectACByParentId(@Param("finAttNameId") int finAttNameId);

    /**
     * 批量插入数据
     * @param finAttributeContents
     * @return
     */
    void addAttributeContent (@Param("finAttributeContents")List<FinAttributeContent>finAttributeContents);
    /**
            * 批量更新数据
     * @param finAttributeContents
     * @return
             */
    void updateAttributeContent (@Param("finAttributeContents")List<FinAttributeContent>finAttributeContents);

    //查询该属性书否存在（更新时使用）
    @Select("select count(*) from fin_attribute_content where fin_att_name_id = #{finAttNameId} and fin_product_id = #{finProductId} and fin_att_content = #{finAttContent}")
    boolean selectAttributeContentIfExist(FinAttributeContent finAttributeContent);


//    显示页面使用(List<AttributeName>是对的)-----------------------------------------------------------------------------------------------------------------------
//查询当前分类的上级属性和公共属性
List<AttributeName> selectLastAndPublic(@Param("sorts") List<Sort>sorts);

    //去重查询该属性的所有已使用的属性
    @Select("select DISTINCT fin_att_content as content from fin_attribute_content where fin_attribute_content.fin_product_id in " +
            "(select id from fin_product where deleteSign = 0 and id in (select distinct fin_product_id from fin_attribute_content where fin_att_name_id = #{parentId} and fin_att_content !='')" +
            " and fin_product.fin_sort_id = #{sortId}) and fin_att_name_id = #{parentId}")
    List<Map<String,Object>> selectAllUsed(@Param("parentId") int parentId,@Param("sortId") int sortId);

    //去重查询该属性的所有已使用的属性(下面所有物料的)
    @MapKey("maps.id_list")
    List<Map<String,Object>> selectAllUsedByList(@Param("parentId") int parentId,@Param("maps") List<Map<String,Object>> maps);



    //一个一个条件的筛选
    List<Integer> selectOneByOneDisplay(@Param("attributeContents")List<AttributeContent>attributeContents);

    //根据产品id查询该产品的所有后添加的属性
    @Select("select id, fin_att_name_id as parentId, fin_product_id as productId, fin_att_content as content from fin_attribute_content where fin_product_id = #{productId}")
    List<AttributeContent> selectByProductIdDisplay(@Param("productId") int productId);

    //查询该分类中的所有后添加的属性
    @Select("select id, fin_sort_id as parentId, fin_att_name as name, fin_att_unit as unit, fin_att_length as length, fin_att_public as public_application from fin_attribute_name where fin_sort_id = #{finSortId}")
    List<AttributeName>selectAttributeNameDisplay(@Param("finSortId") int finSortId);


    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListProductDisplay(@Param("products")List<Product>products);


    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    @Select("select fin_attribute_content.id,fin_attribute_content.fin_att_name_id as parentId,fin_product_id as productId,fin_att_content as content,fin_att_name as  name,fin_att_unit as unit\n" +
            "from fin_attribute_content LEFT JOIN fin_attribute_name on fin_attribute_content.fin_att_name_id = fin_attribute_name.id  where fin_attribute_content.fin_product_id= #{id}")
    List<Map<String,Object>> selectAbandonedFinAttribute(@Param("id") int id);

    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
    void afterAddPublicIfFinProductExist(@Param("parentId") int parentId, @Param("products") List<FinProduct> products) ;

    //根据属性名和分类id查询属性id
    @Select("select * from fin_attribute_name where  fin_att_name =#{name} and fin_sort_id = #{parentId}")
    List<FinAttributeName> selectNameIdExcel(@Param("name") String name,@Param("parentId") int parentId);

    //只更新单位
    @Update("update fin_attribute_name set fin_att_unit = #{finAttUnit} where id =#{id}")
    void updateUnit(FinAttributeName finAttributeName);

    //查询属性名是否存在
    @Select("select count(*) from fin_attribute_name where fin_sort_id = #{finSortId} and fin_att_name = #{finAttName}")
    boolean  selectAttNameIfExistExcel(FinAttributeName finAttributeName);

    //添加属性名信息后，获取刚添加的id
    void addAttNameExcelAndId(FinAttributeName finAttributeName);

    //添加属性名信息后，如果有物料，添加空的属性
    void addAttributeContentExcel (@Param("attributeContents")List<FinAttributeContent>attributeContents);

    //搜索功能,带单位
    List<Integer> searchContentUnit1(@Param("str") String str,@Param("parentId") int parentId,@Param("unit") String unit);


    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    @MapKey("id")
    List<Map<String,Object>> selectIfUseBySort(@Param("finAttributeValues")List<FinAttributeValue> finAttributeValues);



}
