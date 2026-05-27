package com.hongbang.mapper;

import com.hongbang.pojo.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DepartmentMapper {
    //添加部门
    @Insert("insert into department values (#{id},#{departmentName})")
    void add(Department department);

    //删除部门
    @Delete("delete from department where id = #{id}")
    void delete(@Param("id") int id);

    //修改部门
    @Update("update department set departmentName = #{departmentName} where id = #{id}")
    void update(Department department);

    //管理员查询所有部门
    @Select("select * from department where id !=14 and id !=1")
    List<Department> selectAll();

    //总经理查询所有部门
    @Select("select * from department where id !=14 and id !=1 and id !=30")
    List<Department> selectAllTwo();

    //根据id查询数据进行回显
    @Select("select * from department where id =#{id}")
    List<Department>selectById(@Param("id") int id);

    //添加部门判断部门名称是否重复
    @Select("select count(*) from department where departmentName = #{departmentName}")
    boolean addIfExist(@Param("departmentName") String departmentName);

    //修改部门时判断部门名称是否重复
    @Select("select count(*) from department where departmentName = #{departmentName} and id != #{id}")
    boolean updateIfExist(Department department);

    //查询部门（选择审核步骤是使用）
    @Select("select * from department where id !=1 and id !=14")
    List<Department>selectDepartment();

    //查询可以查看BOM表的部门
    @Select("select id,departmentName as name from department where id = 21 or id = 23 or id = 24 or id = 22 or id =19")
    List<Department> selectBomDepartment();

}
