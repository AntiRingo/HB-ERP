package com.hongbang.mapper;

import com.hongbang.pojo.ReturnNumber;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReturnNumberMapper {
    @Select("select number from return_number where cgId = #{cgId}")
    Double sumByCgId(@Param("cgId") int cgId);

    @Insert("insert into return_number values (#{id},#{number},#{cgId},#{type})")
    void insert(ReturnNumber returnNumber);

    double sumByCgIdAndType(@Param("type") int type,@Param("cgId") int cgId);
}
