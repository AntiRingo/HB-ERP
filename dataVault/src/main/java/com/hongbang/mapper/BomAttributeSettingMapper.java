package com.hongbang.mapper;

import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.BomAttributeSetting;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BomAttributeSettingMapper {

    //新增
    @Insert("insert into bom_attribute_setting values (#{id},#{attributeId})")
    void  addAttributeSetting(BomAttributeSetting bomAttributeSetting);
    //删除
    @Delete("delete from bom_attribute_setting where attribute_id = #{attributeId}")
    void deleteAttributeSetting(@Param("attributeId") int attributeId);

    //查询数据
    @Select("select * from attribute_name where id in (select distinct attribute_id from bom_attribute_setting) ")
    List<AttributeName>selectAll();
}
