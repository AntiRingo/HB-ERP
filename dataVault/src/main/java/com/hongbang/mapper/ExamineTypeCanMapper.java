package com.hongbang.mapper;

import com.hongbang.pojo.ExamineTypeCan;
import org.apache.ibatis.annotations.*;

public interface ExamineTypeCanMapper {

    //新增修改数量的权限
    @Insert("insert into examine_type_can values(#{id},#{examineId},#{canPrice},#{canNumber}) ")
    void addExamineCan(ExamineTypeCan examineTypeCan);


    //更新修改数量的权限
    @Update("update examine_type_can set can_price = #{canPrice},can_number = #{canNUmber} where examine_id = #{examineId}")
   void  updateExamineCan(ExamineTypeCan examineTypeCan);

    //删除权限，根据步骤删除
    @Delete("delete from examine_type_can where examine_id = #{examineId}")
    void deleteExamineCan(ExamineTypeCan examineTypeCan);

    //根据步骤ID查询数据
    @Select("select * from examine_type_can where examine_id = #{examineId}")
    ExamineTypeCan selectByExamineId (@Param("examineId") int examineId);

}
