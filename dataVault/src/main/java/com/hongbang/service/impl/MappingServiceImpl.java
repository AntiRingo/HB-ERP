package com.hongbang.service.impl;

import com.hongbang.mapper.AttributeValueMapper;
import com.hongbang.mapper.MappingMapper;
import com.hongbang.pojo.Mapping;
import com.hongbang.service.MappingService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class MappingServiceImpl implements MappingService {

    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增映射
    @Override
    public void add(Mapping mapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        mapper.add(mapping);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查看当前分类下的映射
    public List<Map<String,Object>> selectMapping(int sortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectMapping(sortId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //在映射表中根据分类id查询
  public List<Mapping>selectBySortId(int sortId){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
      //调用mapper
      List<Mapping> mappings = mapper.selectBySortId(sortId);
      //释放资源
      sqlSession.close();
      //返回值
      return mappings;
  }


  //删除映射
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据sortId和attNameId查询要删除的id
   public List<Mapping> selectBySA(int sortId, int attNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       List<Mapping> mappings = mapper.selectBySA(sortId, attNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return mappings;
   }

   //更新映射
    public void update(Mapping mapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        mapper.update(mapping);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询是否存在于映射中
    public boolean selectLengthIfExist(int attNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        boolean b = mapper.selectLengthIfExist(attNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询长度
    public List<Map<String,Object>> selectLength(int attNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectLength(attNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据attNameId查询该属性书否在映射中被使用
   public List<Mapping> selectIfUse (int attNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       List<Mapping> mappings = mapper.selectIfUse(attNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return mappings;
   }


    //查看除了当前映射外是否还有其他映射使用该属性
   public boolean selectIfOtherUse(int id,int attNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       boolean b = mapper.selectIfOtherUse(id, attNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查看当前属性是否在当前映射中被使用
    public boolean selectBySN(Mapping mapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
        //调用mapper
        boolean b = mapper.selectBySN(mapping);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //下面是excel表加载时使用的
    //查询映射是否存在
   public boolean selectMapIfExistExcel(Mapping mapping){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       boolean b = mapper.selectMapIfExistExcel(mapping);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //循环添加映射
   public void addMapExcel(List<Mapping>mappings){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       mapper.addMapExcel(mappings);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
    }

    //循环更新映射
   public void updateMapExcel(List<Mapping>mappings){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       mapper.updateMapExcel(mappings);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
    }

    //删除映射信息
   public void deleteMapExcelNextAdd(List<Mapping>mappings){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MappingMapper mapper = sqlSession.getMapper(MappingMapper.class);
       //调用mapper
       mapper.deleteMapExcelNextAdd(mappings);
       mapper.addMapExcel(mappings);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }
}
