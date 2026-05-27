package com.hongbang.mapper;

import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DepartmentFunctionMapper {

//    根据登录的账户等级查询拥有的模块
    @MapKey("")
    List<Module> selectModule(@Param("level") int level, @Param("departId") int departId);

    //根据登录的账户等级查询拥有的模块下的功能信息
    List<Function> selectFunction(@Param("level") int level, @Param("departId") int departId);

//    查询具体功能
    List<FunctionTwo> selectFunctionTwo(@Param("level") int level,@Param("departId") int departId);

}
