package com.hongbang.service.impl;

import com.hongbang.mapper.ModelMapper;
import com.hongbang.pojo.Model;
import com.hongbang.service.ModelService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ModelServiceImpl implements ModelService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询机型
    public List<Model> selectModel(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ModelMapper mapper = sqlSession.getMapper(ModelMapper.class);
        //调用mapper
        List<Model> models = mapper.selectModel();
        //释放资源
        sqlSession.close();
        //返回值
        return models;
    }

    //查询机型是否存在
    public boolean selectModelExist(String modelName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ModelMapper mapper = sqlSession.getMapper(ModelMapper.class);
        //调用mapper
        boolean b = mapper.selectModelExist(modelName);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
}
