package com.hongbang.mapper;

import com.hongbang.pojo.ExamineCondition;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamineConditionMapper {

    //查询所有的条件
    @Select("select * from examine_condition ")
    List<ExamineCondition>selectAll();
}
