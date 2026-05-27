package com.hongbang.service.impl;

import com.hongbang.mapper.AttributeFunctionMapper;
import com.hongbang.pojo.AttributeFunction;
import com.hongbang.service.AttributeFunctionService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class AttributeFunctionServiceImpl implements AttributeFunctionService {

//获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //根据attributeNameId/属性名id查询当前属性的编码设置
     public  List<AttributeFunction> selectByAttNameId(int attNameId){
        //获取session
         SqlSession sqlSession = factory.openSession();
         //获取mapper
         AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
         //调用mapper
         List<AttributeFunction> attributeFunctions = mapper.selectByAttNameId(attNameId);
         //释放资源
         sqlSession.close();
         //返回值
         return attributeFunctions;
     }

    //新增配置
    public void  add(AttributeFunction attributeFunction){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        mapper.add(attributeFunction);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //修改
    public void update(AttributeFunction attributeFunction){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        mapper.update(attributeFunction);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id修改唯一值表示
   public void updateUnique(int uniqueCode, int attNameId,int displayCode){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
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
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAutoCode();
        //释放资源
        sqlSession.close();
        //返回值
        return  maps;
    }

    //查看当前分类下是值的并且已经开启值为编码的功能的属性
    public List<Map<String,Object>> selectAutoCodeBySort( int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAutoCodeBySortId(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return  maps;
    }


    //查看是否已经开启值作为编码的功能
   public boolean selectIfAutoCode(int attNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
       //调用mapper
       boolean b = mapper.selectIfAutoCode(attNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查看是否已经开启顺序编码的功能
    public boolean selectIfOrderCode(int attNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        boolean b = mapper.selectIfOrderCode(attNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询当前映射下的所有属性值作为编码的开启关闭情况
   public List<AttributeFunction> selectAllAttributeFunction(int sortId){
         //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
       //调用mapper
       List<AttributeFunction> attributeFunctions = mapper.selectAllAttributeFunction(sortId);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeFunctions;
   }


    //查询是否有设置这个功能，没设置的话执行添加操作
   public boolean selectIfExist(int attNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
       //调用mapper
       boolean b = mapper.selectIfExist(attNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //下面是excel导入用到的
    //查询是否存在
   public boolean selectExistExcel(AttributeFunction attributeFunction){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
       //调用mapper
       boolean b = mapper.selectExistExcel(attributeFunction);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //循环添加操作
   public void addExcel(List<AttributeFunction>attributeFunctions){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
       //调用mapper
       mapper.addExcel(attributeFunctions);

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
    }

    //循环更新function操作
    public void updateExcel(List<AttributeFunction> attributeFunctions){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeFunctionMapper mapper = sqlSession.getMapper(AttributeFunctionMapper.class);
        //调用mapper
        mapper.updateExcel(attributeFunctions);

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }
}
