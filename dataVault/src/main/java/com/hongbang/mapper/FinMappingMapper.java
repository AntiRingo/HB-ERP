package com.hongbang.mapper;

import com.hongbang.pojo.FinMapping;
import com.hongbang.pojo.Mapping;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinMappingMapper {

    //查询该属性是否存在于映射中,如果没有添加属性值的时候让设置
    @Select("select count(*) from fin_mapping where fin_att_name_id = #{finAttNameId}")
    boolean selectLengthIfExist(@Param("finAttNameId") int finAttNameId);


    //查询长度
    @Select("select length from fin_mapping where fin_att_name_id = #{finAttNameId}")
    List<Map<String,Object>> selectLength(@Param("finAttNameId") int finAttNameId);

    //在映射表中查询根据分类id
    @Select("select * from fin_mapping where fin_sort_id = #{finSortId}")
    List<FinMapping>selectBySortId(@Param("finSortId") int finSortId);

    //查看当前分类下的映射,以及映射名
    @Select("SELECT *from fin_mapping left JOIN fin_attribute_name ON  fin_attribute_name.id=fin_Mapping.FIN_att_name_id where FIN_mapping.FIN_sort_id= #{finSortId}")
    List<Map<String,Object>> selectMapping(@Param("finSortId") int finSortId);

    //根据sortId和attNameId查询要删除的id
    @Select("select * from fin_mapping where fin_sort_id=#{finSortId} and fin_att_name_id = #{finAttNameId}")
    List<FinMapping> selectBySA(@Param("finSortId") int finSortId,@Param("finAttNameId") int finAttNameId);

    //删除映射
    @Delete("delete from  fin_mapping where id=#{id}")
    void delete(@Param("id") int id);

    //根据attNameId查询该属性书否在映射中被使用
    @Select("select * from fin_mapping where fin_att_name_id = #{finAttNameId}")
    List<FinMapping> selectIfUse(@Param("finAttNameId") int finAttNameId);

    //新增映射
    @Insert("insert into  fin_mapping values(#{id},#{finAttNameId},#{finSortId},#{beginLocation},#{endLocation},#{length}) ")
    void add(FinMapping finMapping);


    //查看除了当前映射外是否还有其他映射使用该属性
    @Select("select count(*) from fin_mapping where id!=#{id} and fin_att_name_id = #{finAttNameId}")
    boolean selectIfOtherUse(@Param("id") int id,@Param("finAttNameId") int finAttNameId);


    //更新映射
    @Update("update fin_mapping set begin_location=#{beginLocation},end_location=#{endLocation},length=#{length} where id =#{id}")
    void update(FinMapping finMapping);

    //查询映射是否存在
    @Select("select count(*) from fin_mapping where fin_sort_id = #{finSortId}")
    boolean selectMapIfExistExcel(FinMapping finMapping);

    //循环添加映射
    void addMapExcel(@Param("mappings") List<FinMapping>mappings);

    //删除映射信息
    void deleteMapExcelNextAdd(@Param("mappings") List<FinMapping>mappings);



}
