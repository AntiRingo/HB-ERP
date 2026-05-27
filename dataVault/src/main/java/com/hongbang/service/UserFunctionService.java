package com.hongbang.service;

import com.hongbang.pojo.UserFunction;
import com.hongbang.pojo.UserFunctionTwo;

import java.util.List;
import java.util.Map;

public interface UserFunctionService {
    //循环添加用户的功能信息
    void addAll23(List<UserFunction> userFunctions,List<UserFunctionTwo> userFunctionTwos);

    //查询用户的权限信息
    List<UserFunction>selectByUserId(int userId);

    //循环更新用户的功能信息
    void updateAll23(List<UserFunction>userFunctions,List<UserFunctionTwo>userFunctionTwos);

    //查询用户是否拥有物料分类管理的权限（添加物料的部分有用到这一功能）
    boolean selectFunction2(int userId);

    //查询详细权限user_function_two
    List<Map<String,Object>> selectUserFunctionTwoByUserId(int userId);

    //根据userID删除userFunction,userFunctionTwo
    void deleteUserFunction(int userId);

    void deleteUserFunctionTwo(int userId);

    //查询用户的出库权限
    boolean selectCkQx(int userId);

    //查询用户的入库权限
    boolean selectRkQx(int userId);

    //查询用户的采购权限
    boolean selectCgQx(int userId);

    //查询用户是否具有审核物料信息的权限
    boolean selectShPQx(int userId);

    //查询用户的质检权限
    boolean selectZjQx(int userId);

    //查询用户的订购权限
    boolean selectDgQx(int userId);

}
