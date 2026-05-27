package com.hongbang.service.impl;

import com.hongbang.mapper.ExamineStepContentMapper;
import com.hongbang.pojo.ExamineStepContent;
import com.hongbang.service.ExamineAllTypeService;
import com.hongbang.service.ExamineStepContentService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class ExamineStepContentServiceImpl implements ExamineStepContentService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //循环添加
   public void add(List<ExamineStepContent> examineStepContents){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineStepContentMapper mapper = sqlSession.getMapper(ExamineStepContentMapper.class);
       //调用mapper

       mapper.add(examineStepContents);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //根据申请单查询审核信息
   public List<Map<String,Object>> selectByAppId (int appFormId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineStepContentMapper mapper = sqlSession.getMapper(ExamineStepContentMapper.class);
       //调用mapper
       List<Map<String,Object>> examineStepContents = mapper.selectByAppId(appFormId);
       //释放资源
       sqlSession.close();
       //返回值
       return examineStepContents;
   }

    //更新
    public void update(int result,int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineStepContentMapper mapper = sqlSession.getMapper(ExamineStepContentMapper.class);
        //调用mapper
        mapper.update(result,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //删除的时候查询这个步骤有没有用到
    public boolean selectIfUse(int examineStepId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineStepContentMapper mapper = sqlSession.getMapper(ExamineStepContentMapper.class);
        //调用mapper
        boolean b = mapper.selectIfUse(examineStepId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

}
