package com.hongbang.service.impl;

import com.alibaba.fastjson.JSON;
import com.hongbang.mapper.BasicAttributeMapper;
import com.hongbang.pojo.BasicAttribute;
import com.hongbang.service.BasicAttributeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BasicAttributeServiceImpl implements BasicAttributeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    public List<BasicAttribute> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BasicAttributeMapper mapper = sqlSession.getMapper(BasicAttributeMapper.class);
        //调用mapper
        List<BasicAttribute> basicAttributes = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回数据
        return basicAttributes;

    }


    //查询是否属性名与系统公共属性重复
    public boolean selectExist(String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BasicAttributeMapper mapper = sqlSession.getMapper(BasicAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectExist(name);
        //释放资源
        sqlSession.close();
        //返回数据
        return b;
    }
}
