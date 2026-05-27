package com.hongbang.service;

import com.hongbang.pojo.Department;

import java.util.List;

public interface DepartmentService {

    //添加部门
     void add(Department department);

     //删除部门
    void delete(int id);

    //修改部门
    void update(Department department);

    //查询所有数据(管理员)
    List<Department>selectAll();

    //查询所有数据(总经理)
    List<Department>selectAllTwo();

    //根据id查询信息进行回显
    List<Department>selectById(int id);

    //添加部门判断部门名称是否重复
    boolean addIfExist(String departmentName);

    //修改部门时判断部门名称是否重复
    boolean updateIfExist(Department department);

    //查询部门（选择审核步骤是使用）
    List<Department>selectDepartment();

    //查询可以查看BOM表的部门
    List<Department> selectBomDepartment();
}
