package com.hongbang.service;

import com.hongbang.pojo.FinMapping;
import com.hongbang.pojo.Mapping;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinMappingService {

    //查询该属性是否存在于映射中,如果没有添加属性值的时候让设置
    boolean selectLengthIfExist(int finAttNameId);

    //查询长度
    List<Map<String,Object>> selectLength(int finAttNameId);

    //在映射表中查询根据分类id
    List<FinMapping>selectBySortId(int finSortId);

    //查看当前分类下的映射,以及映射名
    List<Map<String,Object>> selectMapping( int finSortId);

    //根据sortId和attNameId查询要删除的id
    List<FinMapping> selectBySA(int finSortId,int finAttNameId);

    //删除映射
    void delete(int id);

    //根据attNameId查询该属性书否在映射中被使用
    List<FinMapping> selectIfUse(int finAttNameId);

    //新增映射
    void add(FinMapping finMapping);

    //查看除了当前映射外是否还有其他映射使用该属性
    boolean selectIfOtherUse(int id,int finAttNameId);


    //更新映射
    void update(FinMapping finMapping);

    //查询映射是否存在
    boolean selectMapIfExistExcel(FinMapping finMapping);

    //循环添加映射
    void addMapExcel( List<FinMapping>mappings);

    //删除映射信息
    void deleteMapExcelNextAdd(List<FinMapping>mappings);


}
