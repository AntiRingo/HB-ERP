package com.hongbang.service.impl;

import com.hongbang.mapper.CodeMapper;
import com.hongbang.pojo.Code;
import com.hongbang.service.CodeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class CodeServiceImpl implements CodeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询该分类等级是否有规则存在
    @Override
    public boolean ifCode(int level) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        boolean b = mapper.ifCode(level);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //添加code
    @Override
    public void addCode(Code code) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        mapper.addCode(code);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //获取规则并回显

    @Override
    public List<Code> selectCode(int level) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        List<Code> codes = mapper.selectCode(level);
        //释放资源
        sqlSession.close();
        //返回值
        return codes;
    }

    //更新规则
    @Override
    public void updateCode(Code code) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        mapper.updateCode(code);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询所有
    @Override
    public List<Code>selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        List<Code> codes = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return codes;
    }

    //删除分级编码规则
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询一共有几条规则
    @Override
  public int selectCount(){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
      //调用mapper
      int i = mapper.selectCount();
      //释放资源
      sqlSession.close();
      //返回值
      return i;
  }

  //查询这条规则是不是在使用
 public   boolean selectIfLevel(int level){
     //获取session
     SqlSession sqlSession = factory.openSession();
     //获取mapper
     CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
     //调用mapper
     boolean b = mapper.selectIfLevel(level);
     //释放资源
     sqlSession.close();
     //返回值
     return b;
 }

 //查询最大级数
    public int maxLevel(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        CodeMapper mapper = sqlSession.getMapper(CodeMapper.class);
        //调用mapper
        int i = mapper.maxLevel();
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

}
