package com.hongbang.mapper;

import com.hongbang.pojo.AttributeFunction;
import com.hongbang.pojo.FinAttributeFunction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface FinAttributeFunctionMapper {

    //新增配置
    @Insert("insert into fin_attribute_function values (#{id},#{finAttNameId},#{range},#{autoCode},#{serialCode},#{uniqueCode},#{displayCode})")
    void  add(FinAttributeFunction finAttributeFunction);

    //根据attributeNameId/属性名id查询当前属性的编码设置
    @Select("select * from fin_attribute_function  where fin_att_name_id = #{finAttNameId}")
    List<FinAttributeFunction> selectByAttNameId(@Param("finAttNameId") int finAttNameId);

    //查询是否有设置这个功能，没设置的话执行添加操作
    @Select("select count(*) from fin_attribute_function where fin_att_name_id = #{finAttNameId}")
    boolean selectIfExist(@Param("finAttNameId") int finAttNameId);

    //修改
    @Update("update fin_attribute_function set `range`=#{range},auto_code = #{autoCode},serial_code=#{serialCode} where fin_att_name_id = #{finAttNameId}")
    void update(FinAttributeFunction finAttributeFunction);

    //根据id修改唯一值表示
    @Update("update  fin_attribute_function set unique_code = #{uniqueCode},display_code=#{displayCode} where fin_att_name_id = #{finAttNameId}")
    void updateUnique(@Param("uniqueCode") int uniqueCode,@Param("finAttNameId") int finAttNameId,@Param("displayCode") int displayCode);

    //查看是值的并且已经开启值为编码的功能的属性
    @Select("select * from fin_attribute_function,fin_attribute_name where fin_att_name_id=fin_attribute_name.id and`range`=0 and (auto_code=1 or auto_code=2)")
    List<Map<String,Object>> selectAutoCode();

    //查询是否开启了顺序编码的功能
    @Select("select count(*) from fin_attribute_function where auto_code=2  and fin_att_name_id = #{finAttNameId}")
    boolean selectIfOrderCode(@Param("finAttNameId") int finAttNameId);

    //查看是否已经开启值作为编码的功能
    @Select("select count(*) from fin_attribute_function where auto_code=1 and  `range`=0 and fin_att_name_id = #{finAttNameId}")
    boolean selectIfAutoCode(@Param("finAttNameId") int finAttNameId);

    //查询当前映射下的所有属性值作为编码的开启关闭情况
    @Select("SELECT * from fin_attribute_function where fin_att_name_id in (select fin_att_name_id from fin_mapping where fin_sort_id = #{finSortId})")
    List<FinAttributeFunction> selectAllAttributeFunction(@Param("finSortId") int finSortId);

//显示页面使用( List<AttributeFunction>是对的)------------------------------------------------------------------------------------------------------

    //根据attributeNameId/属性名id查询当前属性的编码设置
    @Select("select id, fin_att_name_id as att_name_id, `range`, auto_code,serial_code,unique_code,display_code from fin_attribute_function  where fin_att_name_id = #{finAttNameId}")
    List<AttributeFunction> selectByAttNameIdDisplay(@Param("finAttNameId") int finAttNameId);
}
