package com.hongbang.mapper;

import com.hongbang.pojo.Model;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ModelMapper {
    //查询机型
    @Select("select * from model")
    List<Model> selectModel();

    //查询机型是否存在
    @Select("select count(*) from model where model_name = #{modelName}")
    boolean selectModelExist(@Param("modelName") String modelName);
}
