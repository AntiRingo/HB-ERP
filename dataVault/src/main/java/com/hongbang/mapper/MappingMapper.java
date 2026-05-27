package com.hongbang.mapper;

import com.hongbang.pojo.Mapping;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface MappingMapper {
    //新增映射
    @Insert("insert into mapping values(#{id},#{attNameId},#{sortId},#{beginLocation},#{endLocation},#{length}) ")
    void add(Mapping mapping);

    //查看当前分类下的映射,以及映射名
    @Select("SELECT *from mapping left JOIN attribute_name ON  attribute_name.id=mapping.att_name_id where mapping.sort_id= #{sortId}")
    List<Map<String,Object>> selectMapping(@Param("sortId") int sortId);

    //在映射表中查询根据分类id
    @Select("select * from mapping where sort_id = #{sortId}")
    List<Mapping>selectBySortId(@Param("sortId") int sortId);

    //根据sortId和attNameId查询要删除的id
    @Select("select * from mapping where sort_id=#{sortId} and att_name_id = #{attNameId}")
    List<Mapping> selectBySA(@Param("sortId") int sortId,@Param("attNameId") int attNameId);


    //删除映射
    @Delete("delete from  mapping where id=#{id}")
    void delete(@Param("id") int id);

    //更新映射
    @Update("update mapping set begin_location=#{beginLocation},end_location=#{endLocation},length=#{length} where id =#{id}")
    void update(Mapping mapping);

    //查询该属性是否存在于映射中,如果没有添加属性值的时候让设置
    @Select("select count(*) from mapping where att_name_id = #{attNameId}")
    boolean selectLengthIfExist(@Param("attNameId") int attNameId);

    //查询长度
    @Select("select length from mapping where att_name_id = #{attNameId}")
    List<Map<String,Object>> selectLength(@Param("attNameId") int attNameId);

    //根据attNameId查询该属性书否在映射中被使用
    @Select("select * from mapping where att_name_id = #{attNameId}")
    List<Mapping> selectIfUse(@Param("attNameId") int attNameId);

    //查看除了当前映射外是否还有其他映射使用该属性
    @Select("select count(*) from mapping where id!=#{id} and att_name_id = #{attNameId}")
    boolean selectIfOtherUse(@Param("id") int id,@Param("attNameId") int attNameId);

    //查看当前属性是否在当前映射中被使用
    @Select("select count(*) from mapping where sort_id = #{sortId} and att_name_id = #{attNameId}")
    boolean selectBySN(Mapping mapping);


    //下面是excel表加载时使用的
    //查询映射是否存在
    @Select("select count(*) from mapping where sort_id = #{sortId}")
    boolean selectMapIfExistExcel(Mapping mapping);

    //循环添加映射
    void addMapExcel(@Param("mappings") List<Mapping>mappings);

    //循环更新映射
    void updateMapExcel(@Param("mappings") List<Mapping>mappings);

    //删除映射信息
    void deleteMapExcelNextAdd(@Param("mappings") List<Mapping>mappings);



}
