package com.hongbang.mapper;

import com.hongbang.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface AttributeMapper {
    //查询所有属性信息
    @Select("select * from attribute_name ")
    List<AttributeName> selectAll();




    //添加属性
//    @Insert("insert into attribute_name values ( #{id},#{parentId},#{name})")
    void add(AttributeName attributeName);

    //查询该分类中的所有后添加的属性
    @Select("select * from attribute_name where parentId = #{parentId}")
    List<AttributeName>selectAttributeName(@Param("parentId") int parentId);

    //根据id查询要修改的属性的信息进行回显
    @Select("select * from attribute_name where id = #{id}")
    List<AttributeName>selectById(@Param("id") int id);

    //根据id修改属性的信息
    @Update("update attribute_name set name=#{name} where id = #{id}")
    void updateById(AttributeName attributeName);

    //根据删除属性信息
    @Delete("delete from attribute_name where id = #{id}")
    void deleteById(int id);

    //只更新单位
    @Update("update attribute_name set unit = #{unit} where id =#{id}")
    void updateUnit(AttributeName attributeName);

    /**
     * 批量插入数据
     * @param attributeContents
     * @return
     */
    void addAttributeContent (@Param("attributeContents")List<AttributeContent>attributeContents);

    //更新物料前查询信息用来构成输入框
    @Select("select attribute_name.name,attribute_content.id,attribute_content. content,unit,attribute_content.parentId FROM attribute_name,attribute_content where attribute_name.id=attribute_content.parentId and attribute_content.parentId=#{parentId} and productId=#{productId}")
    List<Map<String,Object>>selectBeforeContentUpdate(@Param("parentId")int parentId,@Param("productId") int productId);

    //根据添加后的属性id获取该属性所在分类下的物料id；即content的productid
    @Select("select id from product where parentId in(select attribute_name.parentId FROM attribute_name where id=#{id})")
    List<Map<String,Object>> selectProductIdByParentId(@Param("id") int id);

    //增加完属性后，循环向content表中添加数据
    void addContentAfterName(@Param("attributeContents")List<AttributeContent>attributeContents);

    /**
     * 批量更新数据
     * @param attributeContents
     * @return
     */
    void updateAttributeContent (@Param("attributeContents")List<AttributeContent>attributeContents);

    //新增分类属性
    @Insert("insert into attribute_name values (#{id},#{parentId},#{name},#{unit},#{length},#{publicApplication})")
    void addSortAttribute(AttributeName attributeName);

    //查询该分类下的属性
    @Select("select * from attribute_name where parentId = #{parentId} and public_application=0")
    List<AttributeName>selectAttributeNameByParentId(@Param("parentId") int parentId);

    //根据id查询属性信息进行回显
    @Select("select * from attribute_name where id =#{id}")
    List<AttributeName> selectAttributeNameById(@Param("id") int id);

    //根据id更新属性信息
    @Update("update attribute_name set name = #{name},unit = #{unit} where id =#{id}")
    void updateAttributeNameById(AttributeName attributeName);

    //根据id删除属性信息
    @Delete("delete from attribute_name where id = #{id}")
    void deleteAttributeNameById(@Param("id") int id);

    //根据id加入length
    @Update("update attribute_name set length=#{length} where id = #{id}")
    void updateLength(@Param("length") int length,@Param("id") int id);

    //查询属性名表中是长度是否存在
    @Select("select length from attribute_name where id = #{id}")
    int selectIfLengthExist(@Param("id") int id);

    //查询属性值以及编码是否被物料用到 (BINARY)区分大小写
    @Select("select count(*) from attribute_content where parentId = #{parentId} and BINARY content = #{content}")
    boolean selectIfUse(@Param("parentId") int parentId,@Param("content") String content);

    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    @MapKey("id")
   List<Map<String,Object>> selectIfUseBySort(@Param("attributeValues")List<AttributeValue> attributeValues);

    //查询该属性中是否有属性值被用到物料上
    @Select("select count(*) from attribute_content where parentId = #{parentId}")
    boolean selectIfUseByParentId(@Param("parentId") int parentId);

    //判断该属性是否是范围类型的
    @Select("select count(*) from attribute_function where att_name_id= #{id} and `range`=1")
    boolean selectIfRange(@Param("id") int id);

    //查询当前物料属性中所有被用到的信息
    @Select("select * from attribute_content where parentId = #{parentId}")
    List<AttributeContent>selectACByParentId(@Param("parentId") int parentId);

    //根据产品id查询该产品的所有后添加的属性
    @Select("select * from attribute_content where productId = #{productId}")
    List<AttributeContent> selectByProductId(@Param("productId") int productId);

    //查询该属性书否存在（更新时使用）
    @Select("select count(*) from attribute_content where parentId = #{parentId} and productId = #{productId} and content = #{content}")
    boolean selectAttributeContentIfExist(AttributeContent attributeContent);

    //添加公共属性
//    @Insert("insert into attribute_name set id = #{id},parentId = #{parentId},name=#{name},unit=#{unit},length = #{length},public_application = #{publicApplication}")
    void addPublic(AttributeName attributeName);



    //查询公共属性
    @Select("select * from attribute_name where parentId = 0 and public_application =1")
    List<AttributeName> selectPublic();

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    @Select("select count(*) from attribute_name where name = #{name}")
    boolean selectNameExist(@Param("name") String name);

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    @Select("select count(*) from attribute_name where name = #{name} and id!=#{id}")
    boolean selectUpdateNameExist(AttributeName attributeName);

    //查询该属性填写的所有内容是否为空
    @Select("select * from attribute_content where attribute_content.parentId = #{parentId} ")
    List<AttributeContent>selectContentNull(@Param("parentId") int parentId);

    //查询当前分类的上级属性和公共属性
    List<AttributeName> selectLastAndPublic(@Param("sorts") List<Sort>sorts);

    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    @Select("select attribute_content.id,attribute_content.parentId,productId,content,name,unit\n" +
            "from attribute_content LEFT JOIN attribute_name on attribute_content.parentId = attribute_name.id  where attribute_content.productId = #{id}")
    List<Map<String,Object>> selectAbandonedAttribute(@Param("id") int id);

    //查询时唯一标志的内容是否重复（添加时使用）
    @Select("select count(*) from attribute_content where parentId = #{parentId} and content = #{content}")
    boolean selectUniqueContent(@Param("parentId") int parentId,@Param("content") String content);

    //查询时唯一标志的内容是否重复（修改时使用）
    @Select("select count(*) from attribute_content where parentId = #{parentId} and content = #{content} and productId !=#{productId}")
    boolean selectUniqueContentUpdate(@Param("parentId") int parentId,@Param("content") String content,@Param("productId") int productId);

    //查询该属性内容有多少个，开启唯一值功能时判断使用
    @Select("SELECT count(*) from attribute_content where parentId = #{parentId} ")
    int selectAttributeContentCount(@Param("parentId") int parentId);

    //查询不重复的属性内容有多少个。开启唯一值功能时判断使用
    @Select("select count(*) from (SELECT DISTINCT  content   from attribute_content where parentId = #{parentId}) as a")
    int selectAttributeContentDistinct(@Param("parentId") int parentId);

    //流水码功能：查询该分类下最后插入的一个流水码
    @Select("select content as maxContent from attribute_content where id in(select MAX(id) from attribute_content where productId in (SELECT id from product where parentId = #{sortId}) and parentId =#{attNameId})")
    List<Map<String,Object>> selectMaxSerialCode(@Param("sortId") int sortId,@Param("attNameId") int attNameId);





    //下面是excel加载文件时使用的

    //查询属性名是否存在
    @Select("select count(*) from attribute_name where parentId = #{parentId} and name = #{name}")
    boolean  selectAttNameIfExistExcel(AttributeName attributeName);

    //循环添加属性名信息
    void addAttNameExcel(@Param("attributeNames") List<AttributeName>attributeNames);

    //添加属性名信息后，获取刚添加的id
    void addAttNameExcelAndId(AttributeName attributeName);

    //添加属性名信息后，如果有物料，添加空的属性
    void addAttributeContentExcel (@Param("attributeContents")List<AttributeContent>attributeContents);

    //根据属性名和分类id查询属性id
    @Select("select * from attribute_name where  name =#{name} and parentId = #{parentId}")
    List<AttributeName> selectNameIdExcel(@Param("name") String name,@Param("parentId") int parentId);

    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
    void afterAddPublicIfProductExist(@Param("parentId") int parentId, @Param("products") List<Product> products) ;

    //根据content 和 sortID查询productId；第一段
    @Select("select productId from attribute_content where parentId = #{parentId} and content = #{content}")
    List<Integer> selectPidByContent(@Param("parentId") int parentId,@Param("content") String content);

    //根据content 和 sortID查询productId；第二段
    List<Integer> selectPidByContentPid(@Param("parentId") int parentId,@Param("content") String content,@Param("productsId") List<Integer>productIds);

    @MapKey("")
    //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    List<AttributeContent> selectContentByLsAndPid(@Param("attNameId") int attNameId,@Param("productsId") List<Integer> productsId);












    //数量显示页面
    //根据属性名id和物料id查询这个属性值的内容
    @Select("select * from attribute_content where parentId = #{parentId} and productId = #{productId}")
    List<AttributeContent>selectContent(@Param("parentId") int parentId,@Param("productId") int productId);

    //根据选中的内容查询产品id
    @Select("select * from attribute_content where productId in(select productId from attribute_content where content = #{content}) ")
    List<AttributeContent> selectProductId(@Param("content") String content);

    //动态查询
    List<Product> selectAnd(@Param("attributeContents")List<AttributeContent>attributeContents);

    //一个一个条件的筛选
    List<Integer> selectOneByOne(@Param("attributeContents")List<AttributeContent>attributeContents);

    //智能筛选
    List<AttributeContent> IntelligentFiltering(@Param("attributeContents")List<AttributeContent>attributeContents);



    //搜索功能,不带单位
    List<Integer> searchContent(@Param("str") String str,@Param("parentId") int parentId);


    //搜索功能,带单位
    List<Integer> searchContentUnit1(@Param("str") String str,@Param("parentId") int parentId,@Param("unit") String unit);

    //在搜索结果中搜索，不带单位
    List<Integer>selectInSelect(@Param("str") String str,@Param("products")List<Product>products);


    //在搜索结果中搜索，带单位
    List<Integer>selectInSelectUnit(@Param("str") String str,@Param("products")List<Product>products,@Param("unit") String unit);

    //去重查询该属性的所有已使用的属性
    @Select("select DISTINCT content from attribute_content where attribute_content.productId in " +
            "(select id from product where deleteSign = 0 and id in (select distinct productId from attribute_content where parentId = #{parentId} and content !='')" +
            " and product.parentId = #{sortId}) and parentId = #{parentId}")
    List<Map<String,Object>> selectAllUsed(@Param("parentId") int parentId,@Param("sortId") int sortId);


    //去重查询该属性的所有已使用的属性(下面所有物料的)
    @MapKey("maps.id_list")
    List<Map<String,Object>> selectAllUsedByList(@Param("parentId") int parentId,@Param("maps") List<Map<String,Object>> maps);

    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListProduct(@Param("products")List<Product>products);





// 在显示页面使用   ========================================================================================================================
    //搜索功能,不带单位，产品仓库
    List<Integer> searchFinContent(@Param("str") String str,@Param("parentId") int parentId);


    //搜索功能,带单位
    List<Integer> searchFinContentUnit1(@Param("str") String str,@Param("parentId") int parentId,@Param("unit") String unit);

    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListFinProduct(@Param("products")List<Product>products);

    //在搜索结果中搜索，不带单位
    List<Integer>selectInFinSelect(@Param("str") String str,@Param("products")List<Product>products);


    //在搜索结果中搜索，带单位
    List<Integer>selectInFinSelectUnit(@Param("str") String str,@Param("products")List<Product>products,@Param("unit") String unit);



//    根据申请单内容查询属性信息
    @MapKey("id")
    List<Map<String,Object>> selectByAppProductIdAndVault(@Param("applicationContents")List<ApplicationContent>applicationContents);


    //查询属性信息(品牌封装)
    @MapKey("id")
    List<Map<String,Object>>selectBrandFz(@Param("id") int id,@Param("vault") int vault);

    //查询所有属性信息
    @MapKey("id")
    List<Map<String,Object>>selectAllAttribute(@Param("id") int id,@Param("vault") int vault);


}
