package com.hongbang.mapper;

import com.hongbang.pojo.AttributeFunction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface AttributeFunctionMapper {
    //根据attributeNameId/属性名id查询当前属性的编码设置
    @Select("select * from attribute_function  where att_name_id = #{attNameId}")
    List<AttributeFunction> selectByAttNameId(@Param("attNameId") int attNameId);

    //新增配置
    @Insert("insert into attribute_function values (#{id},#{attNameId},#{range},#{autoCode},#{serialCode},#{uniqueCode},#{displayCode})")
    void  add(AttributeFunction attributeFunction);

    //修改
    @Update("update attribute_function set `range`=#{range},auto_code = #{autoCode},serial_code = #{serialCode} where att_name_id = #{attNameId}")
    void update(AttributeFunction attributeFunction);

    //根据id修改唯一值表示
    @Update("update  attribute_function set unique_code = #{uniqueCode},display_code = #{displayCode} where att_name_id = #{attNameId}")
    void updateUnique(@Param("uniqueCode") int uniqueCode,@Param("attNameId") int attNameId,@Param("displayCode") int displayCode);

    //查看是值的并且已经开启值为编码的功能的属性
    @Select("select * from attribute_function,attribute_name where att_name_id=attribute_name.id and`range`=0 and (auto_code=1 or auto_code=2)")
    List<Map<String,Object>> selectAutoCode();

    //查看当前分类下是值，并且已经开启值为编码的属性
    @Select("select * from attribute_function,attribute_name where att_name_id=attribute_name.id and`range`=0 and (auto_code=1 or auto_code=2) and attribute_name.parentId = #{parentId}")
    List<Map<String,Object>> selectAutoCodeBySortId(@Param("parentId") int parentId);

    //查看是否已经开启值作为编码的功能
    @Select("select count(*) from attribute_function where auto_code=1 and  `range`=0 and att_name_id = #{attNameId}")
    boolean selectIfAutoCode(@Param("attNameId") int attNameId);

    //查询是否开启了顺序编码的功能
    @Select("select count(*) from attribute_function where auto_code=2  and att_name_id = #{attNameId}")
    boolean selectIfOrderCode(@Param("attNameId") int attNameId);

    //查询当前映射下的所有属性值作为编码的开启关闭情况
    @Select("SELECT * from attribute_function where att_name_id in (select att_name_id from mapping where sort_id = #{sortId})")
    List<AttributeFunction> selectAllAttributeFunction(@Param("sortId") int sortId);

    //查询是否有设置这个功能，没设置的话执行添加操作
    @Select("select count(*) from attribute_function where att_name_id = #{attNameId}")
    boolean selectIfExist(@Param("attNameId") int attNameId);


    //下面是excel导入用到的
    //查询是否存在
    @Select("select count(*) from attribute_function where att_name_id = #{attNameId}")
    boolean selectExistExcel(AttributeFunction attributeFunction);

    //循环添加操作
    void addExcel(@Param("attributeFunctions") List<AttributeFunction>attributeFunctions);

    //循环更新function操作
    void updateExcel(@Param("attributeFunctions") List<AttributeFunction> attributeFunctions);


}
