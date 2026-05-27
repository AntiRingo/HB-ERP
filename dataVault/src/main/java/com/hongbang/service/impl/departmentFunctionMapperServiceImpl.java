package com.hongbang.service.impl;

import com.hongbang.mapper.DepartmentFunctionMapper;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.service.DepartmentFunctionService;
import com.hongbang.service.DepartmentService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class departmentFunctionMapperServiceImpl implements DepartmentFunctionService {
//获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //    根据登录的账户等级查询拥有的模块
    public List<Module> selectModule(int level, int departId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentFunctionMapper mapper = sqlSession.getMapper(DepartmentFunctionMapper.class);
        //调用mapper
        List<Module> modules = mapper.selectModule(level, departId);
        //释放资源
        sqlSession.close();
        //返回值
        return modules;
    }

    //根据登录的账户等级查询拥有的模块下的功能信息
    public List<Function> selectFunction(int level, int departId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentFunctionMapper mapper = sqlSession.getMapper(DepartmentFunctionMapper.class);
        //获取mapper
        List<Function> functions = mapper.selectFunction(level, departId);
        //释放资源
        sqlSession.close();
        //返回值
        return functions;
    }

    //    查询具体功能
    public List<FunctionTwo> selectFunctionTwo(int level, int departId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        DepartmentFunctionMapper mapper = sqlSession.getMapper(DepartmentFunctionMapper.class);
        //调用mapper
        List<FunctionTwo> functionTwos = mapper.selectFunctionTwo(level, departId);
        //释放资源
        sqlSession.close();
        //返回值
        return functionTwos;
    }

}
