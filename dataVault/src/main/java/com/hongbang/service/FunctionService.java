package com.hongbang.service;

import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FunctionService {

    //查询所有功能列表
    List<Function> selectAllFunction();

    //查询通用功能
    List<Function> selectTY();

    //查询通用功能 （一级）
    List<Module> selectModuleTY();

    //根据functiontwoid查询该功能模块下都有什么权限
    List<FunctionTwo> selectFunctionTwo(int functionId);

}
