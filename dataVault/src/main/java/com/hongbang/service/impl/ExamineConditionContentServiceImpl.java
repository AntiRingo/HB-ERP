package com.hongbang.service.impl;


import com.hongbang.mapper.ExamineConditionContentMapper;
import com.hongbang.pojo.ExamineCondition;
import com.hongbang.pojo.ExamineConditionContent;
import com.hongbang.service.ExamineConditionContentService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class ExamineConditionContentServiceImpl implements ExamineConditionContentService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加审核条件的内容
   public void add(ExamineConditionContent examineConditionContent){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
       //调用mapper
       mapper.add(examineConditionContent);
       //提交事务
       sqlSession.close();
       //释放资源
       sqlSession.close();

   }

    //修改审核条件的内容
    public void update(ExamineConditionContent examineConditionContent){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
        //调用mapper
        mapper.update(examineConditionContent);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //删除审核条件的内容
    public void delete(int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据step_id查询出，该步骤拥有什么条件和内容
    public List<Map<String,Object>> selectByStep(int examineStepId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectByStep(examineStepId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据stepId查询该步骤所拥有的条件
    public List<Integer> selectCondition(int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectCondition(id);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //根据stepId查询该步骤所拥有的条件(带条件名称的)
    public List<Map<String,Object>>selectConditionByStepId(int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionContentMapper mapper = sqlSession.getMapper(ExamineConditionContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectConditionByStepId(id);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


}
