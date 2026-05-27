package com.hongbang.service;

import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.PageBean;

import java.util.List;
import java.util.Map;

public interface AttributeValueService {


    //新增
    void add(AttributeValue attributeValue);

    //修改
    void update(AttributeValue attributeValue);

    //删除
    void delete(int id);

    //回显
    List<AttributeValue> selectById(int id);

    //查看当前属性名下面都有什么属性值
    List<AttributeValue> selectByAttNameId(int attNameId);

    //判断属性值是否重复
    boolean selectValueExist(AttributeValue attributeValue);

    //判断编码值是否重复
    boolean selectCodeExist(AttributeValue attributeValue);

    //判断属性值是否重复
    boolean selectValueExistAdd(AttributeValue attributeValue);

    //判断编码值是否重复
    boolean selectCodeExistAdd(AttributeValue attributeValue);

    //输入联想
    List<Map<String,Object>>inputLX(String str,int attNameId);

    //根据nameId,attValue去查询编码
    List<AttributeValue> selectCode(AttributeValue attributeValue);

    //根据属性ID去查询该属性下是否有编码表存在
    boolean selectIfCode(int attNameId);

    //循环加入编码，属性值作为编码的功能
    void addCodeAuto (List<AttributeValue> attributeValues);


    //分页查询
    PageBean<AttributeValue> selectAttributeValueLimit(int attNameId, int currentPage, int pageSize);


    //模糊查询
    PageBean<AttributeValue> search(String str, int currentPage,int size);




    //下面是excel导入用到的
    //循环加入编码
    void addCodeExcel(List<AttributeValue> attributeValues);

    //查询属性值是否重复
    boolean selectValueExistExcel(AttributeValue attributeValue);


    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，属性值是否与数据库中的重复
    boolean selectValueCodeExistExcel(AttributeValue attributeValue);

    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，编码是否与数据库中的重复
    boolean selectValueCodesExistExcel(AttributeValue attributeValue);



    List<Map<String,Object>> select1();

    List<AttributeValue> select2();


    //查询所有数据
    List<AttributeValue> selectAll();

}
