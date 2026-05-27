package com.hongbang.service.impl;

import com.hongbang.mapper.OutBoundTypeMapper;
import com.hongbang.pojo.OutboundType;
import com.hongbang.service.OutBoundTypeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class OutBoundTypeServiceImpl implements OutBoundTypeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询出库类型
    public List<OutboundType> selectOutBound(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
        //调用mapper
        List<OutboundType> outboundTypes = mapper.selectOutBound();
        //释放资源
        sqlSession.close();
        //返回值
        return outboundTypes;
    }

    //查询入库类型
    public List<OutboundType> selectInBound(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
        //调用mapper
        List<OutboundType> outboundTypes = mapper.selectInBound();
        //释放资源
        sqlSession.close();
        //返回值
        return outboundTypes;


    }

    //查询所有类型
    public List<OutboundType> selectAllType(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
        //调用mapper
        List<OutboundType> outboundTypes = mapper.selectAllType();
        //释放资源
        sqlSession.close();
        //返回值
        return outboundTypes;
    }




    //根据id查询
    public List<OutboundType>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
        //调用mapper
        List<OutboundType> outboundTypes = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return outboundTypes;
    }

    //查询采购类型
   public List<OutboundType> selectCgBound(){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
       //调用mapper
       List<OutboundType> outboundTypes = mapper.selectCgBound();
       //释放资源
       sqlSession.close();
       //返回值
       return outboundTypes;
   }

    //查询质检类型
    public List<OutboundType> selectZjBound(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        OutBoundTypeMapper mapper = sqlSession.getMapper(OutBoundTypeMapper.class);
        //调用mapper
        List<OutboundType> outboundTypes = mapper.selectZjBound();
        //释放资源
        sqlSession.close();
        //返回值
        return outboundTypes;
    }
}
