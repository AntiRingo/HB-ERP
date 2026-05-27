package com.hongbang.service;

import com.hongbang.pojo.ExamineCondition;
import com.hongbang.pojo.ExamineConditionContent;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ExamineConditionContentService {

    //添加审核条件的内容
    void add(ExamineConditionContent examineConditionContent);

    //修改审核条件的内容
    void update(ExamineConditionContent examineConditionContent);

    //删除审核条件的内容
    void delete(int id);

    //根据step_id查询出，该步骤拥有什么条件和内容
    List<Map<String,Object>> selectByStep(int examineStepId);

    //根据stepId查询该步骤所拥有的条件
    List<Integer> selectCondition(int id);

    //根据stepId查询该步骤所拥有的条件(带条件名称的)
    List<Map<String,Object>>selectConditionByStepId(int id);

}
