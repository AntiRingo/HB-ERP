package com.hongbang.service.impl;

import com.hongbang.mapper.FunctionMapper;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.service.FunctionService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FunctionServiceImpl implements FunctionService {
//获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询所有功能列表
    public List<Function> selectAllFunction(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FunctionMapper mapper = sqlSession.getMapper(FunctionMapper.class);
        //调用mapper
        List<Function> functions = mapper.selectAllFunction();
        //释放资源
        sqlSession.close();
        //返回值
        return functions;
    }


    //查询通用功能
   public List<Function> selectTY(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FunctionMapper mapper = sqlSession.getMapper(FunctionMapper.class);
       //调用mapper
       List<Function> functions = mapper.selectTY();
       //释放资源
       sqlSession.close();
       //返回值
       return functions;
   }

    //查询通用功能 （一级）
   public List<Module> selectModuleTY(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FunctionMapper mapper = sqlSession.getMapper(FunctionMapper.class);
       //调用mapper
       List<Module> modules = mapper.selectModuleTY();
       //释放资源
       sqlSession.close();
       //返回值
       return modules;
   }

    //根据functiontwoid查询该功能模块下都有什么权限
   public List<FunctionTwo> selectFunctionTwo(int functionId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FunctionMapper mapper = sqlSession.getMapper(FunctionMapper.class);
       //调用mapper
       List<FunctionTwo> functionTwos = mapper.selectFunctionTwo(functionId);
       //释放资源
       sqlSession.close();
       //返回值
       return functionTwos;
   }
}
