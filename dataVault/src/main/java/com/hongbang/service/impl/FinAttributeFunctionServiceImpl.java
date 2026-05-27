package com.hongbang.service.impl;

import com.hongbang.mapper.AttributeFunctionMapper;
import com.hongbang.mapper.FinAttributeFunctionMapper;
import com.hongbang.pojo.AttributeFunction;
import com.hongbang.pojo.FinAttributeFunction;
import com.hongbang.service.FinAttributeFunctionService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinAttributeFunctionServiceImpl implements FinAttributeFunctionService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增配置
    public void  add(FinAttributeFunction finAttributeFunction){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        mapper.add(finAttributeFunction);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据attributeNameId/属性名id查询当前属性的编码设置
    public List<FinAttributeFunction> selectByAttNameId(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        List<FinAttributeFunction> finAttributeFunctions = mapper.selectByAttNameId(finAttNameId);

        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeFunctions;
    }
    //查询是否有设置这个功能，没设置的话执行添加操作
    public boolean selectIfExist(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectIfExist(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //修改
    public void update(FinAttributeFunction finAttributeFunction){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        mapper.update(finAttributeFunction);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }



    //根据id修改唯一值表示
    public void updateUnique( int uniqueCode,int attNameId,int displayCode){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        mapper.updateUnique(uniqueCode,attNameId,displayCode);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查看是值的并且已经开启值为编码的功能的属性
    public List<Map<String,Object>> selectAutoCode(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAutoCode();
        //释放资源
        sqlSession.close();
        //返回值
        return  maps;
    }


    //查看是否已经开启值作为编码的功能
    public boolean selectIfAutoCode(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectIfAutoCode(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查看是否已经开启顺序编码的功能
    public boolean selectIfOrderCode(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectIfOrderCode(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询当前映射下的所有属性值作为编码的开启关闭情况
    public List<FinAttributeFunction> selectAllAttributeFunction(int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
        //调用mapper
        List<FinAttributeFunction> finAttributeFunctions = mapper.selectAllAttributeFunction(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeFunctions;
    }

    //显示页面使用( List<AttributeFunction>是对的)------------------------------------------------------------------------------------------------------

    //根据attributeNameId/属性名id查询当前属性的编码设置
   public List<AttributeFunction> selectByAttNameIdDisplay(int finAttNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinAttributeFunctionMapper mapper = sqlSession.getMapper(FinAttributeFunctionMapper.class);
       //调用mapper
       List<AttributeFunction> attributeFunctions = mapper.selectByAttNameIdDisplay(finAttNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeFunctions;
   }

}
