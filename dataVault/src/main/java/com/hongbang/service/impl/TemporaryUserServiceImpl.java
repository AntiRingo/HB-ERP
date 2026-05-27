package com.hongbang.service.impl;

import com.hongbang.mapper.TemporaryUserMapper;
import com.hongbang.pojo.TemporaryUser;
import com.hongbang.service.TemporaryUserService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class TemporaryUserServiceImpl implements TemporaryUserService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加临时用户的信息
    public void addTemporaryUser(TemporaryUser temporaryUser){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        mapper.addTemporaryUser(temporaryUser);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除临时用户信息
    public void deleteByTime(String time){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        mapper.deleteByTime(time);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询临时账户用户名是否存在
    public boolean selectExistUserName(String userName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        boolean b = mapper.selectExistUserName(userName);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询所有的临时账户细信息
    public List<Map<String,Object>> selectAllTemporaryUser(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllTemporaryUser();
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询该部门下所有的临时账户信息
    public List<Map<String,Object>> selectAllTemporaryUserByDepartment(int departmentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllTemporaryUserByDepartment(departmentId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据id删除临时账户
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //更新到期时间
    public void updateOverTime(String overTime,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
        //调用mapper
        mapper.updateOverTime(overTime,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //验证临时账户是否正确，部门也要正确
   public boolean loginExist(TemporaryUser temporaryUser){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       TemporaryUserMapper mapper = sqlSession.getMapper(TemporaryUserMapper.class);
       //调用mapper
       boolean b = mapper.loginExist(temporaryUser);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }
}
