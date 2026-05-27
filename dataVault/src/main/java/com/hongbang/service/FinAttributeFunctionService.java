package com.hongbang.service;

import com.hongbang.pojo.AttributeFunction;
import com.hongbang.pojo.FinAttributeFunction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface FinAttributeFunctionService {
    //新增配置
    void  add(FinAttributeFunction finAttributeFunction);

    //根据attributeNameId/属性名id查询当前属性的编码设置
    List<FinAttributeFunction> selectByAttNameId(int finAttNameId);

    //查询是否有设置这个功能，没设置的话执行添加操作
    boolean selectIfExist(int finAttNameId);

    //修改
    void update(FinAttributeFunction finAttributeFunction);

    //根据id修改唯一值表示
    void updateUnique( int uniqueCode,int attNameId,int displayCode);

    //查看是值的并且已经开启值为编码的功能的属性
    List<Map<String,Object>> selectAutoCode();

    //查看是否已经开启值作为编码的功能
    boolean selectIfAutoCode(int finAttNameId);

    //查看是否已经开启顺序编码的功能
    boolean selectIfOrderCode(int finAttNameId);


    //查询当前映射下的所有属性值作为编码的开启关闭情况
    List<FinAttributeFunction> selectAllAttributeFunction(int finSortId);

    //显示页面使用( List<AttributeFunction>是对的)------------------------------------------------------------------------------------------------------

    //根据attributeNameId/属性名id查询当前属性的编码设置
    List<AttributeFunction> selectByAttNameIdDisplay(int finAttNameId);
}
