package com.hongbang.service.impl;

import com.hongbang.mapper.AttributeValueMapper;
import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.AttributeValueService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class AttributeValueServiceImpl implements AttributeValueService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //新增
    @Override
    public void add(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        mapper.add(attributeValue);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //修改
    @Override
    public void update(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        mapper.update(attributeValue);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //回显
    @Override
    public List<AttributeValue>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        List<AttributeValue> attributeValues = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeValues;
    }


    //查看当前属性名下面都有什么属性值
    @Override
    public List<AttributeValue> selectByAttNameId(int attNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        List<AttributeValue> attributeValues = mapper.selectByAttNameId(attNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeValues;
    }


    //判断属性值是否重复
    @Override
   public boolean selectValueExist(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectValueExist(attributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //判断编码值是否重复
    @Override
   public boolean selectCodeExist(AttributeValue attributeValue){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
       //调用mapper
       boolean b = mapper.selectCodeExist(attributeValue);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //判断属性值是否重复
    @Override
    public boolean selectValueExistAdd(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectValueExistAdd(attributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //判断编码值是否重复
    @Override
    public boolean selectCodeExistAdd(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectCodeExistAdd(attributeValue);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //输入联想
    @Override
  public List<Map<String,Object>>inputLX(String str, int attNameId){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
      //调用mapper
      List<Map<String, Object>> maps = mapper.inputLX(str, attNameId);
      //释放资源
      sqlSession.close();
      //返回值
      return maps;
  }

    //根据nameId,attValue去查询编码
    @Override
  public  List<AttributeValue> selectCode(AttributeValue attributeValue){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
      //调用mapper
      List<AttributeValue> attributeValues = mapper.selectCode(attributeValue);
      //释放资源
      sqlSession.close();
      //返回值
      return attributeValues;
  }

    //根据属性ID去查询该属性下是否有编码表存在
     @Override
   public boolean selectIfCode(int attNameId){
         //获取session
         SqlSession sqlSession = factory.openSession();
         //获取mapper
         AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
         //调用mapper
         boolean b = mapper.selectIfCode(attNameId);
         //释放资源
         sqlSession.close();
         //返回值
         return b;
     }

    //循环加入编码，属性值作为编码的功能
    public void addCodeAuto (List<AttributeValue> attributeValues){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        mapper.addCodeAuto(attributeValues);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //分页查询
    public PageBean<AttributeValue> selectAttributeValueLimit(int attNameId, int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<AttributeValue> attributeValues = mapper.selectAttributeValueLimit(attNameId,begin, size);
        int i = mapper.totalCount(attNameId);
        PageBean<AttributeValue> pageBean = new PageBean<>();
        pageBean.setRows(attributeValues);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;


    }

    //模糊查询
   public PageBean<AttributeValue> search(String str, int currentPage,int size){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
       int begin = (currentPage-1)*size;
       //调用mapper
       List<AttributeValue> search = mapper.search(str, begin, size);
       int i = mapper.searchCount(str, begin, size);
       //封装pagebean
       PageBean<AttributeValue> pageBean = new PageBean<>();
       pageBean.setRows(search);
       pageBean.setTotalCount(i);
       //响应数据
       return pageBean;

   }





    //下面是excel导入用到的
    //循环加入编码
    public void addCodeExcel(List<AttributeValue> attributeValues){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        mapper.addCodeExcel(attributeValues);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询属性值是否重复
   public boolean selectValueExistExcel(AttributeValue attributeValue){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
       //调用mapper
       boolean b = mapper.selectValueExistExcel(attributeValue);
       //释放资源
       sqlSession.close();
       //返回
       return b;
   }

    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，属性值是否与数据库中的重复
   public boolean selectValueCodeExistExcel(AttributeValue attributeValue){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
       //调用mapper
       boolean b = mapper.selectValueCodeExistExcel(attributeValue);
       //释放资源
       sqlSession.close();
       //返回
       return b;
   }

    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，编码是否与数据库中的重复
    public boolean selectValueCodesExistExcel(AttributeValue attributeValue){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        //调用mapper
        boolean b = mapper.selectValueCodesExistExcel(attributeValue);
        //释放资源
        sqlSession.close();
        //返回
        return b;
    }

    public List<Map<String,Object>> select1(){
        SqlSession sqlSession = factory.openSession();
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        List<Map<String, Object>> maps = mapper.select1();
        sqlSession.close();
        return maps;
    }

    public List<AttributeValue> select2(){
        SqlSession sqlSession = factory.openSession();
        AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
        List<AttributeValue> attributeValues = mapper.select2();
        sqlSession.close();
        return attributeValues;
    }


    //查询所有数据
   public List<AttributeValue> selectAll(){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeValueMapper mapper = sqlSession.getMapper(AttributeValueMapper.class);
       //调用mapper
       List<AttributeValue> attributeValues = mapper.selectAll();
       //释放资源
       sqlSession.close();
       //返回值
       return attributeValues;
   }

}
