package com.hongbang.mapper;

import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FunctionMapper {
    //查询所有的功能信息
    @Select("select * from function where depart_id != 14")
    List<Function> selectAllFunction();

    //查询通用功能（二级）
    @Select("select * from function where depart_id = 14")
    List<Function> selectTY();

    //查询通用功能 （一级）
    @Select("select * from module where id in (select module_id from `function` where depart_id = 14)")
    List<Module> selectModuleTY();

    //根据functiontwoid查询该功能模块下都有什么权限
    @Select("select * from function_two where function_two.function_id = #{functionId}")
    List<FunctionTwo> selectFunctionTwo(@Param("functionId") int functionId);


}
