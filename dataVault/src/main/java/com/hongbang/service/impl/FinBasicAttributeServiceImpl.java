package com.hongbang.service.impl;

import com.hongbang.mapper.BasicAttributeMapper;
import com.hongbang.mapper.FinBasicAttributeMapper;
import com.hongbang.pojo.BasicAttribute;
import com.hongbang.pojo.FinBasicAttribute;
import com.hongbang.service.BasicAttributeService;
import com.hongbang.service.FinBasicAttributeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FinBasicAttributeServiceImpl implements FinBasicAttributeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    public List<FinBasicAttribute> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBasicAttributeMapper mapper = sqlSession.getMapper(FinBasicAttributeMapper.class);
        //调用mapper
        List<FinBasicAttribute> finBasicAttributes = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回数据
        return finBasicAttributes;

    }


    //查询是否属性名与系统公共属性重复
    public boolean selectExist(String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBasicAttributeMapper mapper = sqlSession.getMapper(FinBasicAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectExist(name);
        //释放资源
        sqlSession.close();
        //返回数据
        return b;
    }
}
