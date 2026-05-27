package com.hongbang.service.impl;

import com.hongbang.mapper.ExamineLogMapper;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.ExamineLog;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.ExamineLogService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class ExamineLogServiceImpl implements ExamineLogService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //新建审核日志
    public void addLog(ExamineLog examineLog){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        mapper.addLog(examineLog);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }
    //查询日志记录
    public PageBean<Map<String,Object>> selectExamineLog(Map<String,Object> maps, int currentPage, int pageSize,int level,int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //设置页码
        int size = pageSize;
        int begin = (currentPage - 1)*size;
        //调用service
        List<Map<String, Object>> maps1 = mapper.selectExamineLog(maps, begin, size,level,department);
        int i = mapper.selectExamineLogCount(maps,level,department);
        //设置pageBean
        PageBean<Map<String, Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps1);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //查询审核人
    public List<Map<String,Object>> selectUser(Map<String,Object> maps,int level,int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        List<Map<String, Object>> maps1 = mapper.selectUser(maps, level,department);
        //释放资源
        sqlSession.close();
        //返回值
        return maps1;
    }

    //根据账号等级查询申请单信息
   public PageBean<ApplicationForm> selectAppByLevel( Map<String,Object> maps,int currentPage, int pageSize, int department,int level ){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
       //设置页码
       int size = pageSize;
       int begin = (currentPage - 1)*size;
       //调用service
       List<ApplicationForm> applicationForms = mapper.selectAppByLevel(maps, begin, size, department, level);
       int i = mapper.selectAppByLevelCount(maps, department, level);
       //设置pageBean
       PageBean<ApplicationForm> pageBean = new PageBean<>();
       pageBean.setRows(applicationForms);
       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;

   }

    //根据申请单id查询审核记录
   public List<Map<String,Object>>selectLogByAppId(int appFormId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //调用mapper
       ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectLogByAppId(appFormId);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //根据申请单id查询否决的有多少个
    public boolean selectExamineStatusCount(int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        boolean b = mapper.selectExamineStatusCount(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //根据等级查询申请人
    public List<Map<String,Object>> selectAppUser( Map<String,Object> maps, int level, int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        List<Map<String, Object>> maps1 = mapper.selectAppUser(maps,level, department);
        //释放资源
        sqlSession.close();
        //返回值
        return maps1;
    }
    //根据等级查询部门
    public List<Map<String,Object>> selectAppDepartment( Map<String,Object> maps, int level, int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineLogMapper mapper = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        List<Map<String, Object>> maps1 = mapper.selectAppDepartment(maps,level, department);
        //释放资源
        sqlSession.close();
        //返回值
        return maps1;
    }
}
