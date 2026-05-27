package com.hongbang.service.impl;

import com.hongbang.mapper.ExamineConditionMapper;
import com.hongbang.pojo.ExamineCondition;
import com.hongbang.service.ExamineConditionService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ExamineConditionServiceImpl implements ExamineConditionService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询所有的条件
    public List<ExamineCondition> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineConditionMapper mapper = sqlSession.getMapper(ExamineConditionMapper.class);
        //调用mapper
        List<ExamineCondition> examineConditions = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return examineConditions;
    }

}
