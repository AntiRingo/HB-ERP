package com.hongbang.service;

import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.Mapping;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MappingService {
    //新增映射
    void add(Mapping mapping);

    //查看当前分类下的映射
    List<Map<String,Object>> selectMapping(int sortId);

    //在映射表中根据分类id查询
    List<Mapping>selectBySortId(int sortId);

    //根据sortId和attNameId查询要删除的id
    List<Mapping> selectBySA(int sortId,int attNameId);

    //删除映射
    void delete(int id);

    //更新映射
    void update(Mapping mapping);

    //查询是否存在于映射中
    boolean selectLengthIfExist(int attNameId);

    //查询长度
    List<Map<String,Object>> selectLength(int attNameId);

    //根据attNameId查询该属性书否在映射中被使用
    List<Mapping> selectIfUse(int attNameId);

    //查看除了当前映射外是否还有其他映射使用该属性
    boolean selectIfOtherUse(int id,int attNameId);

    //查看当前属性是否在当前映射中被使用
    boolean selectBySN(Mapping mapping);


    //下面是excel表加载时使用的
    //查询映射是否存在
    boolean selectMapIfExistExcel(Mapping mapping);


    //循环添加映射
    void addMapExcel(List<Mapping>mappings);

    //循环更新映射
    void updateMapExcel(@Param("mappings") List<Mapping>mappings);

    //删除映射信息
    void deleteMapExcelNextAdd(@Param("mappings") List<Mapping>mappings);
}
