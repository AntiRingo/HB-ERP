package com.hongbang.service.impl;

import com.hongbang.mapper.BalanceMapper;
import com.hongbang.pojo.Balance;
import com.hongbang.service.BalanceService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class BalanceServiceImpl implements BalanceService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //添加信息
    public void addBalance(List<Balance> balanceList){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        mapper.addBalance(balanceList);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询一段时间内的数据
    public List<Map<String,Object>> selectAllByTime(String startTime, String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        List<Map<String,Object>> balances = mapper.selectAllByTime(startTime, endTime);
        //提交事务
        sqlSession.close();
        //返回值
        return balances;
    }

    //根据时间段，productId，vault查询剩余量
    public Balance selectNumber(int productId,int vault,  String startTime,  String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        Balance balance = mapper.selectNumber(productId, vault, startTime, endTime);
        //释放资源
        sqlSession.close();
        //返回值
        return balance;
    }


    //查询上期结存
    public Balance selectLast(int productId,int vault, String startTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        Balance balance = mapper.selectLast(productId, vault, startTime);
        //释放资源
        sqlSession.close();
        //返回值
        return balance;
    }


    //删除
    public void delete(String time){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        mapper.delete(time);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //查询最新的出库数量以及最新的价格
    public List<Map<String,Object>> selectNewPriceAndNumber(int productId, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BalanceMapper mapper = sqlSession.getMapper(BalanceMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectNewPriceAndNumber(productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }
}
