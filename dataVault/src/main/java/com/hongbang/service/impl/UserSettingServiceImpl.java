package com.hongbang.service.impl;

import com.hongbang.mapper.UserSettingMapper;
import com.hongbang.pojo.UserSetting;
import com.hongbang.service.UserSettingService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class UserSettingServiceImpl implements UserSettingService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加用户配置信息
    public void addSetting(UserSetting userSetting){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserSettingMapper mapper = sqlSession.getMapper(UserSettingMapper.class);
        //调用mapper
        mapper.addSetting(userSetting);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //修改用户配置信息
    public void updateSetting(UserSetting userSetting){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserSettingMapper mapper = sqlSession.getMapper(UserSettingMapper.class);
        //调用mapper
        mapper.updateSetting(userSetting);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //读取用户配置
    public UserSetting selectSetting(String user, String sort, String infoHead){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserSettingMapper mapper = sqlSession.getMapper(UserSettingMapper.class);
        //调用mapper
        UserSetting userSetting = mapper.selectSetting(user, sort, infoHead);
        //释放资源
        sqlSession.close();
        //返回值
        return userSetting;
    }

    //查询配置是否存在
    public boolean selectIfExist(String user, String sort, String infoHead){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        UserSettingMapper mapper = sqlSession.getMapper(UserSettingMapper.class);
        //调用mapper
        boolean b = mapper.selectIfExist(user, sort, infoHead);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
}
