package com.hongbang.mapper;

import com.hongbang.pojo.ExamineCondition;
import com.hongbang.pojo.ExamineConditionContent;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ExamineConditionContentMapper {

    //添加审核条件的内容
    @Insert("insert into  examine_condition_content values (#{id},#{examineStepId},#{examineConditionId},#{content})")
    void add(ExamineConditionContent examineConditionContent);

    //修改审核条件的内容
    @Update("update examine_condition_content set content = #{content} where id = #{id} ")
    void update(ExamineConditionContent examineConditionContent);

    //删除审核条件的内容
    @Delete("delete from examine_condition_content where id = #{id}")
    void delete(@Param("id") int id);

    //根据step_id查询出，该步骤拥有什么条件和内容
    @Select("select examine_condition_content.*,name from examine_condition_content left join  examine_condition on " +
            "examine_condition_content.examine_condition_id = examine_condition.id where examine_condition_content.examine_step_id = #{examineStepId}")
    List<Map<String,Object>> selectByStep(@Param("examineStepId") int examineStepId);

    //根据审核步骤删除审核条件内容
    @Delete("delete from examine_condition_content where examine_step_id = #{id}")
    void deleteByStep(@Param("id") int id);

    //循环添加审核内容
    void addAll(@Param("examineConditionContents") List<ExamineConditionContent> examineConditionContents);

    //根据stepId查询该步骤所拥有的条件
    @Select("select distinct examine_condition_id  from examine_condition_content where examine_step_id = #{id}")
    List<Integer> selectCondition(@Param("id") int id);

    //根据stepId查询该步骤所拥有的条件(带条件名称的)
    @Select("select examine_condition_content.*,name from examine_condition_content left join examine_condition on examine_condition.id = examine_condition_content.examine_condition_id where examine_step_id = #{id}")
    List<Map<String,Object>>selectConditionByStepId(@Param("id") int id);
}
