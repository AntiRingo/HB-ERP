package com.hongbang.service.impl;

import com.hongbang.mapper.CodeMapper;
import com.hongbang.mapper.FinCodeMapper;
import com.hongbang.pojo.Code;
import com.hongbang.pojo.FinCode;
import com.hongbang.service.FinCodeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class FinCodeServiceImpl implements FinCodeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询最大分级
    public int maxLevel(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        int i = mapper.maxLevel();
        //释放资源
        sqlSession.close();
        //返回值
        return i;

    }

    //查询该分类等级是否有规则存在
    public boolean ifCode(int finSortLevel) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        boolean b = mapper.ifCode(finSortLevel);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //获取规则并回显
    public List<FinCode> selectCode(int finSortLevel) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        List<FinCode> finCodes = mapper.selectCode(finSortLevel);
        //释放资源
        sqlSession.close();
        //返回值
        return finCodes;
    }

    //添加code
    public void addCode(FinCode finCode) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        mapper.addCode(finCode);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询这条规则是不是在使用
    public   boolean selectIfLevel(int finAttLevel){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        boolean b = mapper.selectIfLevel(finAttLevel);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //删除分级编码规则
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询所有
    @Override
    public List<FinCode>selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        List<FinCode> finCodes = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return finCodes;
    }

    //查询一共有几条规则
    @Override
    public int selectCount(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        int i = mapper.selectCount();
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //更新规则
    @Override
    public void updateCode(FinCode finCode) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinCodeMapper mapper = sqlSession.getMapper(FinCodeMapper.class);
        //调用mapper
        mapper.updateCode(finCode);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

}
