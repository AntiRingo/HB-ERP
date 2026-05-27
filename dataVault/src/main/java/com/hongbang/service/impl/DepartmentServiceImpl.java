package com.hongbang.service.impl;

import com.hongbang.mapper.DepartmentMapper;
import com.hongbang.pojo.Department;
import com.hongbang.service.DepartmentService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加部门
    @Override
    public void add(Department department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        mapper.add(department);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除部门
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //修改部门
    @Override
    public void update(Department department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        mapper.update(department);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询所有(管理员)
    @Override
    public List<Department>selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        List<Department> departments = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return departments;
    }

    //查询所有部门（总经理）
    public List<Department>selectAllTwo(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        List<Department> departments = mapper.selectAllTwo();
        //释放资源
        sqlSession.close();
        //返回值
        return departments;
    }

    //根据id查询数据
    @Override
    public List<Department>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        List<Department> departments = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return departments;
    }

    //添加部门判断部门名称是否重复
   public boolean addIfExist(String departmentName){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
       //调用mapper
       boolean b = mapper.addIfExist(departmentName);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //修改部门时判断部门名称是否重复
   public boolean updateIfExist(Department department){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
       //调用mapper
       boolean b = mapper.updateIfExist(department);
       //释放资源
       sqlSession.close();
       //返回值
       return b;

   }

    //查询部门（选择审核步骤是使用）
    public List<Department>selectDepartment(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        List<Department> departments = mapper.selectDepartment();
        //释放资源
        sqlSession.close();
        //返回值
        return departments;
    }

    //查询可以查看BOM表的部门
    public List<Department> selectBomDepartment(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        //调用mapper
        List<Department> departments = mapper.selectBomDepartment();
        //释放资源
        sqlSession.close();
        //返回值
        return departments;
    }

}
