package com.hongbang.service.impl;

import com.hongbang.mapper.BomAttributeSettingMapper;
import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.BomAttributeSetting;
import com.hongbang.service.BomAttributeSettingService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BomAttributeSettingServiceImpl implements BomAttributeSettingService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增
    public void  addAttributeSetting(BomAttributeSetting bomAttributeSetting){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomAttributeSettingMapper mapper = sqlSession.getMapper(BomAttributeSettingMapper.class);
        //调用mapper
        mapper.addAttributeSetting(bomAttributeSetting);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }
    //删除
    public void deleteAttributeSetting(int attributeId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomAttributeSettingMapper mapper = sqlSession.getMapper(BomAttributeSettingMapper.class);
        //调用mapper
        mapper.deleteAttributeSetting(attributeId);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询数据
    public List<AttributeName> selectAll(){
    //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomAttributeSettingMapper mapper = sqlSession.getMapper(BomAttributeSettingMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;

    }
}
