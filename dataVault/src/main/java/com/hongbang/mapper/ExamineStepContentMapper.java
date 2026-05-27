package com.hongbang.mapper;

import com.hongbang.pojo.ExamineStepContent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface ExamineStepContentMapper {

    //循环添加
    void add(@Param("examineStepContents") List<ExamineStepContent> examineStepContents);

    //根据申请单ID查询审核信息
    @Select("select examine_step_content.*,type_id,department_id,step from examine_step_content LEFT JOIN " +
            "examine_all_type on examine_step_content.examine_step_id = examine_all_type.id where app_form_id = #{appFormId} order by step")
    List<Map<String,Object>>selectByAppId (@Param("appFormId") int appFormId);

    //更新
    @Update("update examine_step_content set result = #{result} where id =#{id}")
    void update(@Param("result") int result,@Param("id") int id);

    //删除的时候查询这个步骤有没有用到
    @Select("select count(*) from examine_condition_content where examine_step_id = #{examineStepId}")
    boolean selectIfUse(@Param("examineStepId") int examineStepId);

}
