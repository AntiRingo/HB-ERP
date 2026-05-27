package com.hongbang.mapper;

import com.hongbang.pojo.BasicAttribute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BasicAttributeMapper {
    @Select("select * from basic_attribute")
    List<BasicAttribute> selectAll();

    //查询是否属性名与系统公共属性重复
    @Select("select count(*) from basic_attribute where name = #{name}")
    boolean selectExist(@Param("name") String name);
}
