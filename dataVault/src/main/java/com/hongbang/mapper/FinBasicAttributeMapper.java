package com.hongbang.mapper;

import com.hongbang.pojo.BasicAttribute;
import com.hongbang.pojo.FinBasicAttribute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FinBasicAttributeMapper {
    @Select("select * from fin_basic_attribute")
    List<FinBasicAttribute> selectAll();

    //查询是否属性名与系统公共属性重复
    @Select("select count(*) from fin_basic_attribute where name = #{name}")
    boolean selectExist(@Param("name") String name);
}
