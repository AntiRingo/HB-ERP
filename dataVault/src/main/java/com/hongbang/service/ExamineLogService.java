package com.hongbang.service;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.ExamineLog;
import com.hongbang.pojo.PageBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ExamineLogService {

    //新建审核日志
    void addLog(ExamineLog examineLog);

    //查询日志记录
    PageBean<Map<String,Object>> selectExamineLog(Map<String,Object> maps, int begin, int size,int level,int department);

    //查询审核人
    List<Map<String,Object>> selectUser(Map<String,Object> maps,int level,int department);

    //根据账号等级查询申请单信息
    PageBean<ApplicationForm> selectAppByLevel( Map<String,Object> maps,int begin, int size, int department,int level );

    //根据申请单id查询审核记录
    List<Map<String,Object>>selectLogByAppId(int appFormId);


    //根据申请单id查询否决的有多少个
    boolean selectExamineStatusCount(int appFormId);

    //根据等级查询申请人
    List<Map<String,Object>> selectAppUser(Map<String,Object> maps,int level,int department);

    //根据等级查询部门
    List<Map<String,Object>> selectAppDepartment(Map<String,Object> maps,int level,int department);
}
