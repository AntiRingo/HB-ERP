package com.hongbang.service.impl;

import com.hongbang.mapper.FinAttributeValueMapper;
import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.FinAttributeValue;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.FinAttributeValueService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinAttributeValueServiceImpl implements FinAttributeValueService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //根据属性ID去查询该属性下是否有编码表存在
    @Override
    public boolean selectIfCode(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectIfCode(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //分页查询
    public PageBean<FinAttributeValue> selectAttributeValueLimit(int finAttNameId, int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<FinAttributeValue> finAttributeValues = mapper.selectAttributeValueLimit(finAttNameId, begin, size);
        int i = mapper.totalCount(finAttNameId);
        PageBean<FinAttributeValue> pageBean = new PageBean<>();
        pageBean.setRows(finAttributeValues);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;


    }


    //回显
    @Override
    public List<FinAttributeValue>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        List<FinAttributeValue> finAttributeValues = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeValues;
    }


    //判断属性值是否重复

    public boolean selectValueExist(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectValueExist(finAttributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
    //判断编码值是否重复

    public boolean selectCodeExist(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectCodeExist(finAttributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //修改
    @Override
    public void update(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        mapper.update(finAttributeValue);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查看当前属性名下面都有什么属性值
    @Override
    public List<FinAttributeValue> selectByAttNameId(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        List<FinAttributeValue> finAttributeValues = mapper.selectByAttNameId(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeValues;
    }

    //删除
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //判断属性值是否重复
    @Override
    public boolean selectValueExistAdd(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectValueExistAdd(finAttributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //判断编码值是否重复
    @Override
    public boolean selectCodeExistAdd(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectCodeExistAdd(finAttributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    @Override
    public void add(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        mapper.add(finAttributeValue);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据nameId,attValue去查询编码
    @Override
    public  List<FinAttributeValue> selectCode(FinAttributeValue finAttributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        List<FinAttributeValue> finAttributeValues = mapper.selectCode(finAttributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeValues;
    }


    //循环加入编码，属性值作为编码的功能
    public void addCodeAuto (List<FinAttributeValue> finAttributeValues){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        mapper.addCodeAuto(finAttributeValues);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //输入联想
    @Override
    public List<Map<String,Object>>inputLX(String str, int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.inputLX(str, finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //显示页面使用---------------------------------------------------------------------------------------------------------------------------
    //查看当前属性下的所有属性值
   public List<AttributeValue> selectByAttNameIdDisplay( int finAttNameId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinAttributeValueMapper mapper = sqlSession.getMapper(FinAttributeValueMapper.class);
       //调用mapper
       List<AttributeValue> attributeValues = mapper.selectByAttNameIdDisplay(finAttNameId);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeValues;
   }



}
