package com.hongbang.service.impl;

import com.hongbang.mapper.ExamineTypeCanMapper;
import com.hongbang.pojo.ExamineTypeCan;
import com.hongbang.service.ExamineTypeCanService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ExamineTypeCanServiceImpl implements ExamineTypeCanService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //更新修改数量的权限
    public void addExamineCan(ExamineTypeCan examineTypeCan){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineTypeCanMapper mapper = sqlSession.getMapper(ExamineTypeCanMapper.class);
        //调用mapper
        mapper.addExamineCan(examineTypeCan);
        //提交事务
        sqlSession.commit();
        //释放资源s
        sqlSession.close();

    }


    //更新修改数量的权限
    public void  updateExamineCan(ExamineTypeCan examineTypeCan){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineTypeCanMapper mapper = sqlSession.getMapper(ExamineTypeCanMapper.class);
        //调用mapper
        mapper.updateExamineCan(examineTypeCan);
        //提交事务
        sqlSession.commit();
        //释放资源s
        sqlSession.close();
    }

    //先删除，后添加
    public void updatePermissions(ExamineTypeCan examineTypeCan){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineTypeCanMapper mapper = sqlSession.getMapper(ExamineTypeCanMapper.class);
        //调用mapper，先删除
        mapper.deleteExamineCan(examineTypeCan);
        //调用mapper，再添加
        mapper.addExamineCan(examineTypeCan);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();


    }

    //根据步骤ID查询数据
    public ExamineTypeCan selectByExamineId (int examineId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineTypeCanMapper mapper = sqlSession.getMapper(ExamineTypeCanMapper.class);
        //调用mapper
        ExamineTypeCan examineTypeCan = mapper.selectByExamineId(examineId);
        //释放资源
        sqlSession.close();
        //返回值
        return examineTypeCan;
    }
}
