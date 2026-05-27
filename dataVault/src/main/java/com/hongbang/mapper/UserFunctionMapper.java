package com.hongbang.mapper;

import com.hongbang.pojo.UserFunction;
import com.hongbang.pojo.UserFunctionTwo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserFunctionMapper {


    //循环添加用户的功能信息
    void addAll(@Param("userFunctions")List<UserFunction>userFunctions);

    //循环添加用户功能具体信息userfunctiontwo
    void addAllUserFunctionTwo(@Param("userFunctionTwos") List<UserFunctionTwo> userFunctionTwos);

    //查询用户的权限信息
    @Select("select * from user_function where user_id = #{userId}")
    List<UserFunction>selectByUserId(@Param("userId") int userId);

    //循环更新用户的功能信息
    void updateAll(@Param("userFunctions")List<UserFunction>userFunctions);

    //查询用户是否拥有物料分类管理的权限（添加物料的部分有用到这一功能）
    @Select("select count(*) from user_function where user_id = #{userId} and function_id = 2 and open = 1")
    boolean selectFunction2(@Param("userId") int userId);

    //查询详细权限user_function_two
    @Select("select * from user_function_two where user_id = #{userId}")
    List<Map<String,Object>> selectUserFunctionTwoByUserId(@Param("userId") int userId);

    //循环更新userfunctiontwo
    void updateUserFunctionTwo(@Param("userFunctionTwos") List<UserFunctionTwo> userFunctionTwos);

    //根据userID删除userFunction,userFunctionTwo
    @Delete("delete  from user_function where user_id = #{userId} ")
    void deleteUserFunction(@Param("userId") int userId);
    @Delete("delete from user_function_two where user_id = #{userId}")
    void deleteUserFunctionTwo(@Param("userId") int userId);

    //查询用户的出库权限
    @Select("select count(*) from user_function where user_id = #{userId} and  function_id = 7 and open = 1")
    boolean selectCkQx(@Param("userId") int userId);

    //查询用户的入库权限
    @Select("select count(*) from user_function where user_id = #{userId} and  function_id = 6 and open = 1")
    boolean selectRkQx(@Param("userId") int userId);

    //查询用户的采购权限
    @Select("select count(*) from user_function where user_id = #{userId} and function_id = 34 and open = 1")
    boolean selectCgQx(@Param("userId") int userId);

    //查询用户的质检权限
    @Select("select count(*) from user_function where user_id = #{userId} and function_id = 42 and open = 1")
    boolean selectZjQx(@Param("userId") int userId);

    //查询用户的订购权限
    @Select("select count(*) from user_function where user_id = #{userId} and function_id = 46 and open = 1")
    boolean selectDgQx(@Param("userId") int userId);


    //查询用户是否具有审核物料信息的权限
    @Select("select count(*) from user_function where user_id = #{userId} and function_id = 38 and open = 1")
    boolean selectShPQx(@Param("userId") int userId);


}
