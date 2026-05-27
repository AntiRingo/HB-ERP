package com.hongbang.service;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //添加用户
    void add(User user);

    //修改用户权限
//    void updatePurview(User user);

    //查找所有用户信息
    List<User>selectAll();

    //删除用户信息
    void delete(int id);

    //根据id查询该用户的信息
    User selectById(int id);

    //登录验证
    User login(String userName,String passWord);

    //最高权限人员查询用户信息除了自己的
    List<Map<String,Object>>userNoMe(int id,int level);

    //新增管理员的时候检测登录名是否重复
    boolean selectUserExist(String username);

    //超级管理员修改用户信息验证登录名是否重复，本身除外
    boolean selectUserExistUpdate(int id,String userName);

    //管理员修改用户信息
    void adminUpdateUser(User user);

    //用户查询自己的信息：姓名、年龄、性别、部门
    List<Map<String,Object>>userSelectPersonal(int id);

    //用户更新个人基本信息：姓名、年龄、性别
    void userUpdatePersonal(User user);

    //根据id修改密码
    void updatePassword(String passWord, int id);

    //部门管理人查询自己部门的信息
    List<Map<String,Object>> managerSelectUser(int department,int id);

    //根据userid查询该用户所拥有的模块
    List<Module>selectModuleByUserId(int userId);

    //根据userID查询该用户所拥有的功能
    List<Function> selectUserFunctionByUserId(int userId);

    //根据用户ID查询用户的级别
    int selectLevelById(int id);

    //查询所有的功能。一级
    List<Module>selectAllModule();

    //查询所有的功能。三级
    List<FunctionTwo>selectAllFunctionThree();


    //部长查询所有的功能，包括自己部门的所有功能和通用功能（一级），根据部门ID查询
    List<Module>selectBZModule(int depart_id);

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（二级），根据部门ID查询
    List<Function>selectBZFunction(int departId);

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（三级），根据部门ID查询
    List<FunctionTwo>selectBZFunctionTwo(int depart_id);

    //查询该部门下是否有人员存在
    boolean selectIfUserDepartment(int department);

    //心跳使用
    int heart();


    //根据用户ID查询保留时间
    Map<String,Object> selectExitTime(int id);

    //查询该部门下是否已经存在部长级别的账号了
    boolean selectLevelTwoExist(int department);

    //查询该部门下是否已经存在部长级别的账号了(更新时使用)
    boolean updateLevelTwoExist(int department,int id);

    //根据用户id查询用户名，名称，部门
    List<Map<String,Object>> selectDepartById (int id);

    //重置密码为默认状态
    void resetPassWord(int id);

    //根据用户ID以及用户输入的二级密码来判断二级密码是否正确
    boolean secondaryPasswordExamine(int id,String secondaryPassword);

    //根据ID修改二级密码
    void updateSecondaryPassword(String secondaryPassword,int id);

    //测试30秒是否有用
    void executeLongQuery();


    //根据多个用户ID查询
    List<Map<String,Object>> selectDepartByIds (List<ApplicationForm> applicationForms);


    //查询部门下所有的人员信息
    List<User>selectDepartmentUser(int department);


    // 查询用户信息（根据您的表结构）
    Map<String, Object> selectUserInfo(int id);

    //查询所以的用户信息
    List<Map<String, Object>>selectAllUser();

}
