package com.hongbang.service;

import com.hongbang.pojo.ExamineStepContent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ExamineStepContentService {

    //循环添加
    void add(List<ExamineStepContent> examineStepContents);

    //根据申请单查询审核信息
    List<Map<String,Object>>selectByAppId (int appFormId);

    //更新
    void update(int result,int id);

    //删除的时候查询这个步骤有没有用到
    boolean selectIfUse(int examineStepId);
}
