package com.hongbang.service;

import com.hongbang.pojo.AttributeFunction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface AttributeFunctionService {

    //根据attributeNameId/属性名id查询当前属性的编码设置
    List<AttributeFunction> selectByAttNameId(int attNameId);

    //新增配置
    void  add(AttributeFunction attributeFunction);

    //修改
    void update(AttributeFunction attributeFunction);

    //根据id修改唯一值表示
    void updateUnique(int uniqueCode, int attNameId,int displayCode);

    //查看是值的并且已经开启值为编码的功能的属性
    List<Map<String,Object>> selectAutoCode();

    //查看当前分类下是值的并且已经开启值为编码的功能的属性
    List<Map<String,Object>> selectAutoCodeBySort(int parentId);

    //查看是否已经开启值作为编码的功能
    boolean selectIfAutoCode(int attNameId);

    //查看是否已经开启顺序编码的功能
    boolean selectIfOrderCode(int attNameId);

    //查询当前映射下的所有属性值作为编码的开启关闭情况
    List<AttributeFunction> selectAllAttributeFunction(int sortId);

    //查询是否有设置这个功能，没设置的话执行添加操作
    boolean selectIfExist(@Param("attNameId") int attNameId);


    //下面是excel导入用到的
    //查询是否存在
    boolean selectExistExcel(AttributeFunction attributeFunction);


    //循环添加操作
    void addExcel(List<AttributeFunction>attributeFunctions);

    //循环更新function操作
    void updateExcel(List<AttributeFunction> attributeFunctions);
}
