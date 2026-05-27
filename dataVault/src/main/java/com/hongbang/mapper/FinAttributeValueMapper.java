package com.hongbang.mapper;

import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.FinAttributeValue;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinAttributeValueMapper {

    //根据属性ID去查询该属性下是否有编码表存在
    @Select("select count(*) from fin_attribute_value where fin_att_name_Id = #{finAttNameId}")
    boolean selectIfCode(@Param("finAttNameId") int finAttNameId);

    //分页查询
    @Select("select * from fin_attribute_value where fin_att_name_id = #{finAttNameId} limit #{begin},#{size}")
    List<FinAttributeValue> selectAttributeValueLimit(@Param("finAttNameId") int finAttNameId, @Param("begin") int begin, @Param("size") int size);
    @Select("select count(*)  from fin_attribute_value where fin_att_name_id = #{finAttNameId}")
    int totalCount(@Param("finAttNameId") int finAttNameId);


    //回显
    @Select("select * from fin_attribute_value where id = #{id}")
    List<FinAttributeValue> selectById(@Param("id") int id);

    //判断属性值是否重复
    @Select("select count(*)from fin_attribute_value where id !=#{id} and fin_att_name_id = #{finAttNameId} and   BINARY fin_att_value =#{finAttValue}")
    boolean selectValueExist(FinAttributeValue finAttributeValue);

    //修改
    @Update("update fin_attribute_value set fin_att_name_id = #{finAttNameId},code=#{code},fin_att_value = #{finAttValue} where id = #{id}")
    void update(FinAttributeValue finAttributeValue);


    //查看当前属性下的所有属性值
    @Select("select * from fin_attribute_value where fin_att_name_id = #{finAttNameId}")
    List<FinAttributeValue> selectByAttNameId(@Param("finAttNameId") int finAttNameId);

    //删除
    @Delete("delete from fin_attribute_value where id = #{id}")
    void delete(@Param("id") int id);


    //判断编码值是否重复
    @Select("select count(*)from fin_attribute_value where id !=#{id} and fin_att_name_id = #{finAttNameId}  and BINARY code =#{code}")
    boolean selectCodeExist(FinAttributeValue finAttributeValue);


    //判断属性值是否重复
    @Select("select count(*)from fin_attribute_value where  fin_att_name_id = #{finAttNameId} and  BINARY fin_att_value = #{finAttValue}")
    boolean selectValueExistAdd(FinAttributeValue finAttributeValue);

    //判断编码值是否重复
    @Select("select count(*)from fin_attribute_value where fin_att_name_id = #{finAttNameId}  and  BINARY code = #{code}")
    boolean selectCodeExistAdd(FinAttributeValue finAttributeValue);

    //添加
    @Insert("insert into fin_attribute_value values (#{id},#{finAttNameId},#{code},#{finAttValue})")
    void add(FinAttributeValue finAttributeValue);

    //根据nameId,attValue去查询编码
    @Select("select * from fin_attribute_value where fin_att_name_id = #{finAttNameId} and  BINARY fin_att_value =#{finAttValue}")
    List<FinAttributeValue> selectCode(FinAttributeValue finAttributeValue);

    /**
     * 批量插入数据
     * @param finAttributeValues
     * @return
     */
    void addCodeAuto (@Param("finAttributeValues")List<FinAttributeValue> finAttributeValues);


    //输入联想
    @Select("select fin_att_value,code from fin_attribute_value where concat(fin_att_value) LIKE CONCAT('%',#{str},'%')and fin_attribute_value.fin_att_name_id = #{finAttNameId} ")
    List<Map<String,Object>>inputLX(@Param("str") String str, @Param("finAttNameId") int finAttNameId);


    //显示页面使用---------------------------------------------------------------------------------------------------------------------------
    //查看当前属性下的所有属性值
    @Select("select id, fin_att_name_Id as att_name_id, code, fin_att_value as att_value from fin_attribute_value where fin_att_name_id = #{finAttNameId}")
    List<AttributeValue> selectByAttNameIdDisplay(@Param("finAttNameId") int finAttNameId);
}
