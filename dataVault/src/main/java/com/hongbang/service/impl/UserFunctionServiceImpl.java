package com.hongbang.service.impl;

import com.hongbang.mapper.UserFunctionMapper;
import com.hongbang.pojo.UserFunction;
import com.hongbang.pojo.UserFunctionTwo;
import com.hongbang.service.UserFunctionService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class UserFunctionServiceImpl implements UserFunctionService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //循环添加用户的功能信息
    public void addAll23(List<UserFunction> userFunctions,List<UserFunctionTwo> userFunctionTwos){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        mapper.addAll(userFunctions);
        mapper.addAllUserFunctionTwo(userFunctionTwos);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询用户的权限信息
   public List<UserFunction>selectByUserId(int userId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
       //调用mapper
       List<UserFunction> userFunctions = mapper.selectByUserId(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return userFunctions;
   }

    //循环更新用户的功能信息
  public  void updateAll23(List<UserFunction>userFunctions, List<UserFunctionTwo> userFunctionTwos){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
      //调用mapper
      mapper.updateAll(userFunctions);
      mapper.updateUserFunctionTwo(userFunctionTwos);
      //提交事务
      sqlSession.commit();
      //释放资源
      sqlSession.close();
  }


    //查询用户是否拥有物料分类管理的权限（添加物料的部分有用到这一功能）
   public boolean selectFunction2(int userId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
       //调用mapper
       boolean b = mapper.selectFunction2(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查询详细权限user_function_two
   public List<Map<String,Object>> selectUserFunctionTwoByUserId(int userId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectUserFunctionTwoByUserId(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //根据userID删除userFunction,userFunctionTwo
    public void deleteUserFunction(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        mapper.deleteUserFunction(userId);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    public void deleteUserFunctionTwo(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        mapper.deleteUserFunctionTwo(userId);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询用户的出库权限
    public boolean selectCkQx(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectCkQx(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询用户的入库权限
   public boolean selectRkQx(int userId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
       //调用mapper
       boolean b = mapper.selectRkQx(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }
    //查询用户的采购权限
    public boolean selectCgQx(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectCgQx(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询用户是否具有审核物料信息的权限
    public boolean selectShPQx(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectShPQx(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询用户的质检权限
    public boolean selectZjQx(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectZjQx(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询用户的订购权限
    public boolean selectDgQx(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserFunctionMapper mapper = sqlSession.getMapper(UserFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectDgQx(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

}
