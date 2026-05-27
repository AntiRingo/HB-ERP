package com.hongbang.service;

import com.hongbang.pojo.*;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface AttributeService {

    //查询所有属性信息
    List<AttributeName> selectAll();

    //在该分类下添加属性
    void add(AttributeName attributeName);

    //查询该分类下的所有属性
    List<AttributeName> selectAttributeName (int parentId);

    //根据id查询要修改的信息进行回显
    List<AttributeName> selectById(int id);

    //修改属性信息根据id
    void updateById(AttributeName attributeName);

    //根据id删除属性信息
    void deleteById(int id);

    //只更新单位
    void updateUnit(AttributeName attributeName);

    //跟随者物料的添加进而添加该物料后添加的属性信息
    void addAttributeContent (List<AttributeContent>list);

    //更新物料前查询信息用来构成输入框
    List<Map<String,Object>>selectBeforeContentUpdate(int parentId,int productId);

    //根据添加后的属性id获取该属性所在分类下的物料id；即content的productid
    List<Map<String,Object>> selectProductIdByParentId(int id);

    //增加完属性后，循环向content表中添加数据
    void addContentAfterName(List<AttributeContent>attributeContents);

    //更新完基本属性后更新后添加的属性
    void updateAttributeContent (List<AttributeContent>list);

    //新增分类属性
    void addSortAttribute(AttributeName attributeName);

    //查询该分类下的属性
    List<AttributeName>selectAttributeNameByParentId( int parentId);

    //根据id获取属性信息进行回显
    List<AttributeName> selectAttributeNameById( int id);

    //根据id更新属性信息
    void updateAttributeNameById(AttributeName attributeName);

    //根据id删除属性信息
    void deleteAttributeNameById(int id);

    //根据ID更新length
    void updateLength(int length,int id);

    //在属性名表中查询长度是否存在
    int selectIfLengthExist(int id);

    //查询属性值以及编码是否被物料用到
    boolean selectIfUse(int parentId,String content);

    //查询该属性中是否有属性值被用到物料上
    boolean selectIfUseByParentId(int parentId);

    //判断该属性是否是范围类型的
    boolean selectIfRange(int id);

    //查询当前物料属性中所有被用到的信息
    List<AttributeContent>selectACByParentId(int parentId);

    //根据产品id查询该产品的所有后添加的属性
    List<AttributeContent> selectByProductId(int productId);

    //查询该属性书否存在（更新时使用）
    boolean selectAttributeContentIfExist(AttributeContent attributeContent);

    //添加公共属性
    void addPublic(AttributeName attributeName);

    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
    void afterAddPublicIfProductExist( int parentId,  List<Product> products) ;

    //查询公共属性
    List<AttributeName> selectPublic();

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    boolean selectNameExist(String name);

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    boolean selectUpdateNameExist(AttributeName attributeName);

    //查询该属性填写的所有内容是否为空
    List<AttributeContent>selectContentNull(int parentId);

    //查询当前分类的上级属性和公共属性
    List<AttributeName> selectLastAndPublic(List<Sort>sorts);

    //查询时唯一标志的内容是否重复（添加时使用）
    boolean selectUniqueContent(int parentId, String content);

    //查询时唯一标志的内容是否重复（修改时使用）
    boolean selectUniqueContentUpdate(int parentId,String content, int productId);


    //查询该属性内容有多少个，开启唯一值功能时判断使用
    int selectAttributeContentCount(int parentId);

    //查询不重复的属性内容有多少个。开启唯一值功能时判断使用
    int selectAttributeContentDistinct(int parentId);


    //流水码功能：查询该分类下最大的流水码
    List<Map<String,Object>> selectMaxSerialCode(int sortId,int attNameId);

    //根据content 和 sortID查询productId;第一段
    List<Integer> selectPidByContent(int parentId, String content);

    //根据content 和 sortID查询productId；第二段
    List<Integer> selectPidByContentPid( int parentId,String content,List<Integer>productsId);

    //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    List<AttributeContent> selectContentByLsAndPid(int attNameId,List<Integer> productsId);



    //下面是excel导入时使用的
    //查询属性名是否存在
    boolean  selectAttNameIfExistExcel(AttributeName attributeName);

    //循环添加属性名信息
    void addAttNameExcel(List<AttributeName>attributeNames);


    //添加属性名信息后，获取刚添加的id
    void addAttNameExcelAndId(AttributeName attributeName);

    //添加属性名信息后，如果有物料，添加空的属性
    void addAttributeContentExcel (List<AttributeContent>attributeContents);

    //根据属性名和分类id查询属性id
    List<AttributeName> selectNameIdExcel(String name,int id);


    //数量显示页面
    //根据属性名id和物料id查询这个属性值的内容
    List<AttributeContent>selectContent( int parentId, int productId);


    //根据选中的内容查询产品id
    List<AttributeContent>selectProductId( String content);




    //动态查询
    List<Product> selectAnd(List<AttributeContent>attributeContents);


    //一个一个条件的筛选
    List<Integer> selectOneByOne(List<AttributeContent>attributeContents);

    //智能筛选
    List<AttributeContent> IntelligentFiltering(List<AttributeContent>attributeContents);


    //搜索功能,不带单位
    List<Integer> searchContent(String str,int parentId);


    //搜索功能,带单位
    List<Integer> searchContentUnit1(String str,int parentId,String uint);


    //在搜索结果中搜索，不带单位
    List<Integer>selectInSelect(String str,List<Product>products);


    //在搜索结果中搜索，带单位
    List<Integer>selectInSelectUnit(String str,List<Product>products, String unit);

    //去重查询该属性的所有已使用的属性
    List <Map<String,Object>> selectAllUsed(int parentId,int sortId);
    //去重查询该属性的所有已使用的属性(所有分类下的物料)
    List<Map<String,Object>> selectAllUsedByList(int parentId,List<Map<String,Object>> maps);


    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListProduct(List<Product>products);


    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    List<Map<String,Object>> selectAbandonedAttribute(int id);


//    在显示页面使用=====================================================================================================================
    //搜索功能,不带单位
    List<Integer> searchFinContent(String str,int parentId);


    //搜索功能,带单位
    List<Integer> searchFinContentUnit1(String str,int parentId,String uint);

    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListFinProduct(List<Product>products);

    //在搜索结果中搜索，不带单位
    List<Integer>selectInFinSelect(String str,List<Product>products);


    //在搜索结果中搜索，带单位
    List<Integer>selectInFinSelectUnit(String str,List<Product>products, String unit);


    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    List<Map<String,Object>> selectIfUseBySort(List<AttributeValue> attributeValues);






    //    根据申请单内容查询属性信息
    List<Map<String,Object>> selectByAppProductIdAndVault(List<ApplicationContent>applicationContents);


    //查询属性信息(品牌封装)
    @MapKey("id")
    List<Map<String,Object>>selectBrandFz(int id, int vault);

    //查询所有属性信息
    @MapKey("id")
    List<Map<String,Object>>selectAllAttribute(int id, int vault);


}
