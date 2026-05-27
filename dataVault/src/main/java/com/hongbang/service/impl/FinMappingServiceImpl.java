package com.hongbang.service.impl;

import com.hongbang.mapper.FinMappingMapper;
import com.hongbang.mapper.MappingMapper;
import com.hongbang.pojo.FinMapping;
import com.hongbang.pojo.Mapping;
import com.hongbang.service.FinMappingService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinMappingServiceImpl implements FinMappingService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询是否存在于映射中
    public boolean selectLengthIfExist(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        boolean b = mapper.selectLengthIfExist(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询长度
    public List<Map<String,Object>> selectLength(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectLength(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //在映射表中根据分类id查询
    public List<FinMapping>selectBySortId(int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        List<FinMapping> finMappings = mapper.selectBySortId(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return finMappings;
    }


    //查看当前分类下的映射
    public List<Map<String,Object>> selectMapping(int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectMapping(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据sortId和attNameId查询要删除的id
    public List<FinMapping> selectBySA(int finSortId, int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        List<FinMapping> finMappings = mapper.selectBySA(finSortId, finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return finMappings;
    }


    //删除映射
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据attNameId查询该属性书否在映射中被使用
    public List<FinMapping> selectIfUse (int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        List<FinMapping> finMappings = mapper.selectIfUse(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return finMappings;
    }


    //新增映射
    @Override
    public void add(FinMapping finMapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        mapper.add(finMapping);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查看除了当前映射外是否还有其他映射使用该属性
    public boolean selectIfOtherUse(int id,int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        boolean b = mapper.selectIfOtherUse(id, finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }



    //更新映射
    public void update(FinMapping finMapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        mapper.update(finMapping);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询映射是否存在
    public boolean selectMapIfExistExcel(FinMapping finMapping){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        boolean b = mapper.selectMapIfExistExcel(finMapping);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //循环添加映射
    public void addMapExcel(List<FinMapping> finMappings){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        mapper.addMapExcel(finMappings);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除映射信息
    public void deleteMapExcelNextAdd(List<FinMapping>finMappings){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinMappingMapper mapper = sqlSession.getMapper(FinMappingMapper.class);
        //调用mapper
        mapper.deleteMapExcelNextAdd(finMappings);
        mapper.addMapExcel(finMappings);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

}
