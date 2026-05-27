package com.hongbang.service;

import com.hongbang.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinAttributeService {

//分类属性名--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该分类下的属性
    List<FinAttributeName> selectAttributeNameByParentId(int finSortId);

    //查询公共属性
    List<FinAttributeName> selectPublic();

    //根据id查询属性信息进行回显
    List<FinAttributeName> selectAttributeNameById(int id);

    //查询该分类中的所有后添加的属性
    List<FinAttributeName>selectAttributeName(int finSortId);

    //添加属性
    void add(FinAttributeName finAttributeName);

    //根据删除属性信息
    void deleteById(int id);

    //根据id更新属性信息
    void updateAttributeNameById(FinAttributeName finAttributeName);

    //查询属性名表中是长度是否存在
    int selectIfLengthExist(int id);

    //根据id查询要修改的属性的信息进行回显
    List<FinAttributeName>selectById( int id);

    //判断该属性是否是范围类型的
    boolean selectIfRange(int id);

    //根据id加入length
    void updateLength(int finAttLength,int id);


    //根据id删除属性信息
    @Delete("delete from fin_attribute_name where id = #{id}")
    void deleteAttributeNameById(int id);

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    boolean selectUpdateNameExist(FinAttributeName finAttributeName);

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    boolean selectNameExist( String finAttName);

    //添加公共属性
    void addPublic(FinAttributeName finAttributeName);



    //更新物料前查询信息用来构成输入框
    List<Map<String,Object>>selectBeforeContentUpdate(int finAttNameId, int finProductId);


    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    List<Map<String,Object>> selectAbandonedFinAttribute(int id);

    //查询时唯一标志的内容是否重复（添加时使用）
    boolean selectUniqueContent(int parentId, String content);

    //查询时唯一标志的内容是否重复（修改时使用）
    boolean selectUniqueContentUpdate( int parentId, String content, int productId);

    //查询该属性内容有多少个，开启唯一值功能时判断使用
    int selectAttributeContentCount(int parentId);

    //查询不重复的属性内容有多少个。开启唯一值功能时判断使用
    int selectAttributeContentDistinct(int parentId);

//分类属性内容--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该属性中是否有属性值被用到物料上
    boolean selectIfUseByParentId(int finAttNameId);

    //查询该属性填写的所有内容是否为空
    List<FinAttributeContent>selectContentNull(int finAttNameId);

    //增加完属性后，循环向content表中添加数据
    void addContentAfterName(List<FinAttributeContent>finAttributeContents);


    //查询当前物料属性中所有被用到的信息
    List<FinAttributeContent>selectACByParentId(int finAttNameId);

    /**
     * 批量插入数据
     * @param finAttributeContents
     * @return
     */
    void addAttributeContent (List<FinAttributeContent>finAttributeContents);

    /**
            * 批量更新数据
     * @param finAttributeContents
     * @return
             */
    void updateAttributeContent (List<FinAttributeContent>finAttributeContents);

    //查询该属性书否存在（更新时使用）
    boolean selectAttributeContentIfExist(FinAttributeContent finAttributeContent);



    //    显示页面使用(List<AttributeName>是对的)-----------------------------------------------------------------------------------------------------------------------
//查询当前分类的上级属性和公共属性
    List<AttributeName> selectLastAndPublic(List<Sort>sorts);

    //去重查询该属性的所有已使用的属性

    List<Map<String,Object>> selectAllUsed(int parentId,int sortId);
    //去重查询该属性的所有已使用的属性(下面所有物料的)

    List<Map<String,Object>> selectAllUsedByList(int parentId, List<Map<String,Object>> maps);


    //一个一个条件的筛选
    List<Integer> selectOneByOneDisplay(List<AttributeContent>attributeContents);

    //根据产品id查询该产品的所有后添加的属性
    List<AttributeContent> selectByProductIdDisplay(int productId);

    //查询该分类中的所有后添加的属性
    List<AttributeName>selectAttributeNameDisplay( int finSortId);

    //根据数据查询所有的后添加的属性
    List<AttributeContent> selectByListProductDisplay(List<Product>products);


    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
    void afterAddPublicIfFinProductExist( int parentId, List<FinProduct> products) ;

    //根据属性名和分类id查询属性id
    List<FinAttributeName> selectNameIdExcel(String name,int parentId);

    //只更新单位
    void updateUnit(FinAttributeName finAttributeName);

    //查询属性名是否存在
    boolean  selectAttNameIfExistExcel(FinAttributeName finAttributeName);

    //添加属性名信息后，获取刚添加的id
    void addAttNameExcelAndId(FinAttributeName finAttributeName);

    //添加属性名信息后，如果有物料，添加空的属性
    void addAttributeContentExcel (List<FinAttributeContent>finAttributeContents);



    //根据content 和 sortID查询productId；第二段
    List<Integer> selectPidByContentPid( int parentId,String content,List<Integer>productsId);

    //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    List<FinAttributeContent> selectContentByLsAndPid(int attNameId,List<Integer> productsId);


    //查询属性值以及编码是否被物料用到
    boolean selectIfUse(int parentId,String content);


    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    List<Map<String,Object>> selectIfUseBySort(List<FinAttributeValue> finAttributeValues);
}
