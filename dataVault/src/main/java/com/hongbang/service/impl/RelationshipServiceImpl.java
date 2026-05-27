package com.hongbang.service.impl;

import com.hongbang.mapper.RelationshipMapper;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Relationship;
import com.hongbang.service.RelationshipService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class RelationshipServiceImpl implements RelationshipService {

    //创建工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //添加
    public void addRelationship(Relationship relationship){
        //获取service
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        RelationshipMapper mapper = sqlSession.getMapper(RelationshipMapper.class);
        //调用mapper
        mapper.addRelationship(relationship);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //根据新的查询旧的
    public List<ApplicationForm> selectOldByNew(int newAppFormId){
        //获取service
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        RelationshipMapper mapper = sqlSession.getMapper(RelationshipMapper.class);
        //调用mapper
        List<ApplicationForm>  applicationForms = mapper.selectOldByNew(newAppFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return applicationForms;
    }

    //根据新的id去查询旧的id
    public Integer selectOld(int newAppFormId){
        //获取service
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        RelationshipMapper mapper = sqlSession.getMapper(RelationshipMapper.class);
        //调用mapper
        int i = mapper.selectOld(newAppFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }


    //循环添加多条
    public void addRelationshipList(List<Relationship> relationships){
        //获取service
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        RelationshipMapper mapper = sqlSession.getMapper(RelationshipMapper.class);
        //调用mapper
        mapper.addRelationshipList(relationships);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

}
