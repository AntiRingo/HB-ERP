package com.hongbang.service.impl;

import com.hongbang.mapper.TakeOrderMapper;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.TakeOrder;
import com.hongbang.service.TakeOrderService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class TakeOrderServiceImpl implements TakeOrderService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //给新申请的申请单添加状态
    public void   addTakeOrder(TakeOrder takeOrder){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TakeOrderMapper mapper = sqlSession.getMapper(TakeOrderMapper.class);
        //调用mapper
        mapper.addTakeOrder(takeOrder);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //接单
    public void updateTakeOrder(TakeOrder takeOrder){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TakeOrderMapper mapper = sqlSession.getMapper(TakeOrderMapper.class);
        //调用mapper
        mapper.updateTakeOrder(takeOrder);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //检查是否已经被接单了,返回true就是未接单
    public boolean selectIfJd(int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TakeOrderMapper mapper = sqlSession.getMapper(TakeOrderMapper.class);
        //调用mapper
        boolean b = mapper.selectIfJd(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //部长查看所有的接单情况
    public PageBean<Map<String,Object>> selectAllTakeOrder(int currentPage, int pageSize,boolean ck,  boolean rk, boolean cg, boolean zj,boolean dg){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        TakeOrderMapper mapper = sqlSession.getMapper(TakeOrderMapper.class);
        int size = pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllTakeOrder(begin, size,ck,rk,cg,zj,dg);
        int i = mapper.selectAllTakeOrderCount(ck,rk,cg,zj,dg);
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }
}
