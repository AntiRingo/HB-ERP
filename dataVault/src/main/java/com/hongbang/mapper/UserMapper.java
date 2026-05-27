package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //添加用户
    void add(User user);

//    //修改用户权限
//    @Update("update user set wareHouse=#{wareHouse},purchase=#{purchase},manage = #{manage} where id=#{id}")
//    void updatePurview(User user);

    //查找所有用户信息
    @Select("select * from user ")
    List<User>selectAll();

    //删除用户信息
    @Delete("delete from user where id =#{id}")
    void delete(@Param("id") int id);

    //根据id查询该用户的信息
    @Select("select * from user where id = #{id}")
    User selectById(@Param("id") int id);

    //验证用户名密码是否存在(登录)
    @Select("select * from user where binary userName=#{userName} and binary passWord = #{passWord}")
    User select(@Param("userName") String userName, @Param("passWord") String passWord);

    //最该权限人员查询用户信息除了自己的(包括部门名称)
    @Select("select * from user INNER JOIN department on user.department=department.id and user.id !=#{id} and user.level>#{level}" )
   List<Map<String,Object>>UserNoME(@Param("id") int id,@Param("level") int level);

    //新增用户的时候检测登录名是否重复
    @Select("select count(*) from user where binary username = #{username}")
    boolean selectUserExist(@Param("username") String username);

    //超级管理员修改用户信息验证登录名是否重复，本身除外
    @Select("select count(*) from user where userName not in (select userName from user where id = #{id} ) and userName=#{userName}")
    boolean selectUserExistUpdate(@Param("id") int id, @Param("userName") String userName);

    //管理员修改用户信息
    @Update("update user set userName= #{userName},department=#{department},name=#{name},age=#{age},sex=#{sex},level = #{level},exitTime = #{exitTime} where id =#{id}")
    void adminUpdateUser(User user);

    //用户查询自己的信息：姓名、年龄、性别、部门、等级
    @Select("select name,age,sex,department,level from user where id = #{id}")
   List<Map<String,Object>>userSelectPersonal(@Param("id") int id);

    //用户更新个人基本信息：姓名、年龄、性别
    @Update("update user set name=#{name},age=#{age},sex=#{sex} where id = #{id}")
    void userUpdatePersonal(User user);

    //根据id修改密码
    @Update("update user set passWord = #{passWord} where id =#{id}")
    void updatePassword( @Param("passWord") String passWord, @Param("id") int id);

    //根据ID修改二级密码
    @Update("update user set secondary_password = #{secondaryPassword} where id = #{id}")
    void updateSecondaryPassword(@Param("secondaryPassword") String secondaryPassword,@Param("id") int id);

    //部门管理人查询自己部门的信息
    @Select("select * from user INNER JOIN department on user.department=department.id and user.department = #{department} and user.id !=#{id} and user.id!=1")
    List<Map<String,Object>> managerSelectUser(@Param("department") int department,@Param("id") int id);

    //根据userid查询该用户所拥有的功能模块（一级）
    @Select("SELECT * from module where id in (SELECT module_id from `function` where id in (select function_id from user_function where user_id = #{user_id} and open=1 )) order by reorder")
    List<Module>selectModuleByUserId(@Param("user_id") int user_id);

    //根据userID查询该用户所拥有的功能（二级）
    @Select("SELECT * from `function` where id in (select function_id from user_function where user_id = #{user_id} and open = 1) order by reorder ")
    List<Function> selectUserFunctionByUserId(@Param("user_id") int user_id);

    //根据用户ID查询用户的级别
    @Select("select level from user where id = #{id}")
    int selectLevelById(@Param("id") int id);

    //查询所有的功能。一级
    @Select("select * from module where id in (select module_id from `function` where depart_id != 14 )order by reorder")
    List<Module>selectAllModule();

    //查询所有的功能。二级,此功能在functionmapper里

    //查询所有的功能。三级
    @Select("select * from function_two where function_id in (select id from `function` where depart_id != 14)")
    List<FunctionTwo>selectAllFunctionThree();

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（一级），根据部门ID查询
    @Select("select * from module where id in (SELECT module_id from `function` where depart_id = #{depart_id} )order by reorder")
    List<Module>selectBZModule(@Param("depart_id") int depart_id);

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（二级），根据部门ID查询
    @Select("SELECT * from `function` where depart_id = #{departId} order by reorder ")
    List<Function>selectBZFunction(@Param("departId") int departId);

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（三级），根据部门ID查询
    @Select("select * from function_two where function_id in (SELECT id from `function` where depart_id =#{depart_id} )")
    List<FunctionTwo>selectBZFunctionTwo(@Param("depart_id") int depart_id);

    //查询该部门下是否有人员存在
    @Select("select count(*) from user where department = #{department} and user.id !=1")
    boolean selectIfUserDepartment(@Param("department") int department);

    //心跳使用
    @Select("select count(*) from user")
    int heart();

    //根据用户ID查询保留时间
    @Select("select exitTime from user where id = #{id}")
    Map<String,Object> selectExitTime(@Param("id") int id);

    //查询该部门下是否已经存在部长级别的账号了
    @Select("select count(*) from user where level = 3 and department = #{department}")
    boolean selectLevelTwoExist(@Param("department") int department);

    //查询该部门下是否已经存在部长级别的账号了(更新时使用)
    @Select("select count(*) from user where level = 3 and department = #{department} and id != #{id}")
    boolean updateLevelTwoExist(@Param("department") int department,@Param("id") int id);


    //根据用户id查询用户名，名称，部门
    @Select("select userName,name,departmentName,user.id from user LEFT JOIN department on department.id = user.department where user.id = #{id}")
    List<Map<String,Object>> selectDepartById (@Param("id") int id);

    //重置密码为默认状态
    @Update("update user set passWord = 'hb8888' where id = #{id}")
    void resetPassWord(@Param("id") int id);

    //根据用户ID以及用户输入的二级密码来判断二级密码是否正确
    @Select("select count(*) from user where id = #{id} and secondary_password = #{secondaryPassword}")
    boolean secondaryPasswordExamine(@Param("id") int id,@Param("secondaryPassword") String secondaryPassword);


    //测试30秒是否有用
    void executeLongQuery();


    //根据多个用户ID查询
    @MapKey("id")
 List<Map<String,Object>> selectDepartByIds (@Param("applicationForms") List<ApplicationForm> applicationForms);

    //查询部门下所有的人员信息
   @Select("select * from user where department = #{department}")
 List<User>selectDepartmentUser(@Param("department") int department);





    // 查询用户信息（根据您的表结构）
    @Select("SELECT id, name, username, department FROM user WHERE id = #{id}")
    Map<String, Object> selectUserInfo(@Param("id") int id);

    //查询所以的用户信息
    @Select("SELECT id, name, username, department FROM user")
    List<Map<String, Object>>selectAllUser();



}
