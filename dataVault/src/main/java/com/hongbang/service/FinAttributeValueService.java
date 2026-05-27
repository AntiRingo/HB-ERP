package com.hongbang.service;

import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.FinAttributeValue;
import com.hongbang.pojo.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinAttributeValueService {

    //根据属性ID去查询该属性下是否有编码表存在
    boolean selectIfCode(@Param("finAttNameId") int finAttNameId);

    //分页查询
    PageBean<FinAttributeValue> selectAttributeValueLimit(int attNameId, int currentPage, int pageSize);


    //回显
    List<FinAttributeValue> selectById(int id);

    //判断属性值是否重复
    boolean selectValueExist(FinAttributeValue finAttributeValue);

    //判断编码值是否重复
    boolean selectCodeExist(FinAttributeValue finAttributeValue);

    //修改
    void update(FinAttributeValue finAttributeValue);

    //查看当前属性下的所有属性值
    List<FinAttributeValue> selectByAttNameId(int finAttNameId);

    //删除
    void delete(int id);

    //判断属性值是否重复
    boolean selectValueExistAdd(FinAttributeValue finAttributeValue);

    //判断编码值是否重复
    boolean selectCodeExistAdd(FinAttributeValue finAttributeValue);

    //添加
    void add(FinAttributeValue finAttributeValue);

    //根据nameId,attValue去查询编码
    List<FinAttributeValue> selectCode(FinAttributeValue finAttributeValue);

    /**
     * 批量插入数据
     * @param finAttributeValues
     * @return
     */
    void addCodeAuto (List<FinAttributeValue> finAttributeValues);


    //输入联想
    List<Map<String,Object>>inputLX(String str, int finAttNameId);

    //显示页面使用---------------------------------------------------------------------------------------------------------------------------
    //查看当前属性下的所有属性值
    List<AttributeValue> selectByAttNameIdDisplay( int finAttNameId);



}
