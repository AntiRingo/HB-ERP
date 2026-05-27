package com.hongbang.mapper;

import com.hongbang.pojo.ExamineAllType;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ExamineAllTypeMapper {

    //根据出入库类型查询审核步骤
    @Select("select * from examine_all_type where type_id = #{typeId} and delete_sign = 0 order by step ")
    List<ExamineAllType>selectStep(@Param("typeId") int typeId);

    //添加步骤
    void add(ExamineAllType examineAllTypes);

    //删除
    @Delete("update examine_all_type set delete_sign = 1,step = step-1 where id = #{id} ")
    void delete(@Param("id") int id);

    //修改
    @Update("update examine_all_type set type_id = #{typeId},department_id=#{departmentId},level=#{level},step=#{step} where id = #{id}")
    void update(ExamineAllType examineAllType);

    //查询审核步骤中是否已经存在该部门了
    @Select("select count(*) from examine_all_type where department_id = #{departmentId} and type_id = #{typeId} and delete_sign = 0")
    boolean selectDepartExist(ExamineAllType examineAllType);

    //查询审核步骤中是否已经存在该部门了(修改时使用)
    @Select("select count(*) from examine_all_type where department_id = #{departmentId} and type_id = #{typeId} and id != #{id} and delete_sign = 0")
    boolean selectDepartExistUpdate(ExamineAllType examineAllType);

    //查看该类型的审核流程有几步
    @Select("select count(*) from examine_all_type where type_id = #{typeId} and delete_sign = 0")
    int selectExistCount(@Param("typeId") int typeId);

    //删除步骤后改类型大于这个步骤的-1    ;  and delete_sign = 0
    @Update("update examine_all_type set step = step -1 where step >#{step} and type_id = #{typeId} ")
    void updateStep(@Param("step") int step,@Param("typeId") int typeId);

    //插入步骤，大于等于该步骤的都+1
    @Update("update examine_all_type set step = step + 1 where step >=#{step} and type_id = #{typeId} ")
    void updateStepAdd(@Param("step") int step,@Param("typeId") int typeId);

    //根据id查询
    @Select("select * from examine_all_type where id = #{id}")
    ExamineAllType selectById(@Param("id") int id);

    //插入
    void insert(ExamineAllType examineAllTypes);

    //根据步骤id查询关联的部门信息
    @Select("select examine_all_type.*,departmentName from examine_all_type left join department on examine_all_type.department_id = department.id where examine_all_type.id = #{id}")
    List<Map<String,Object>>selectDepartmentName(@Param("id") int id);



}
