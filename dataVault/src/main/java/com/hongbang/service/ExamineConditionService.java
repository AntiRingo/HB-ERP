package com.hongbang.service;

import com.hongbang.pojo.ExamineCondition;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamineConditionService {

    //查询所有的条件
    List<ExamineCondition> selectAll();
}
