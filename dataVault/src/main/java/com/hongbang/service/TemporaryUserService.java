package com.hongbang.service;

import com.hongbang.pojo.TemporaryUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TemporaryUserService {

    //添加临时用户的信息
    void addTemporaryUser(TemporaryUser temporaryUser);

    //删除临时用户信息
    void deleteByTime(String time);

    //查询临时账户用户名是否存在
    boolean selectExistUserName(String userName);


    //查询所有的临时账户细信息
    List<Map<String,Object>> selectAllTemporaryUser();

    //查询该部门下所有的临时账户信息
    List<Map<String,Object>> selectAllTemporaryUserByDepartment(int departmentId);

    //根据id删除临时账户
    void delete(int id);

    //更新到期时间
    void updateOverTime(String overTime,int id);

    //验证临时账户是否正确，部门也要正确
    boolean loginExist(TemporaryUser temporaryUser);
}
