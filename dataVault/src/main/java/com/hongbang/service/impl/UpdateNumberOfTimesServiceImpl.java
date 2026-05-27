package com.hongbang.service.impl;

import com.hongbang.mapper.UpdateNumberOfTimesMapper;
import com.hongbang.pojo.UpdateNumberOfTimes;
import com.hongbang.service.UpdateNumberOfTimesService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class UpdateNumberOfTimesServiceImpl implements UpdateNumberOfTimesService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增
    public void add(UpdateNumberOfTimes updateNumberOfTimes){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UpdateNumberOfTimesMapper mapper = sqlSession.getMapper(UpdateNumberOfTimesMapper.class);
        //调用mapper
        mapper.add(updateNumberOfTimes);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //修改
   public void update(UpdateNumberOfTimes updateNumberOfTimes){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UpdateNumberOfTimesMapper mapper = sqlSession.getMapper(UpdateNumberOfTimesMapper.class);
       //调用mapper
       mapper.update(updateNumberOfTimes);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //获取当前的数据
   public UpdateNumberOfTimes selectByPid(int productId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UpdateNumberOfTimesMapper mapper = sqlSession.getMapper(UpdateNumberOfTimesMapper.class);
       //调用mapper
       UpdateNumberOfTimes updateNumberOfTimes = mapper.selectByPid(productId);
       //释放资源
       sqlSession.close();
       //返回值
       return updateNumberOfTimes;
   }

    //判断是否有记录
   public boolean selectIfExist(int productId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UpdateNumberOfTimesMapper mapper = sqlSession.getMapper(UpdateNumberOfTimesMapper.class);
       //调用mapper
       boolean b = mapper.selectIfExist(productId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //判断是否有记录
    public boolean selectIfExist1(int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UpdateNumberOfTimesMapper mapper = sqlSession.getMapper(UpdateNumberOfTimesMapper.class);
        //调用mapper
        boolean b = mapper.selectIfExist1(productId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

}
