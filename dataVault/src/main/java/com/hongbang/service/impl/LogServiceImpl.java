package com.hongbang.service.impl;

import com.hongbang.mapper.ApplicationContentMapper;
import com.hongbang.mapper.ApplicationFormMapper;
import com.hongbang.mapper.LogMapper;
import com.hongbang.pojo.Log;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.LogService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class LogServiceImpl implements LogService {
    //工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询入库日志标题
   public PageBean<Map<String,Object>> storageRecordTitle(int currentPage,int pageSize){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       LogMapper mapper = sqlSession.getMapper(LogMapper.class);
       int size=pageSize;
       int begin=(currentPage-1)*size;
       //调用mapper
       List<Map<String, Object>> maps = mapper.storageRecordTitle(begin,size);

       int i = mapper.storageRecordTitleCount();
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
      pageBean.setRows(maps);

       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;

   }

    //查询出库日志标题
    public  PageBean<Map<String,Object>>  outboundRecordTitle(int currentPage,int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;
        //调用mapper
        List<Map<String, Object>> maps = mapper.outboundRecordTitle(begin,size);
        int i = mapper.outboundRecordTitleCount();

        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setTotalCount(i);
        pageBean.setRows(maps);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //查询该物料是否存在出入库记录
    public boolean selectIfLog(int productId,int vault){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        //调用mapper
        boolean b = mapper.selectIfLog(productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询操作人(出库)
    public List<Map<String,Object>> selectManagerCK(){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        List<Map<String, Object>> maps = mapper.selectManagerCK();
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询操作人(入库)
   public List<Map<String,Object>> selectManagerRK(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       LogMapper mapper = sqlSession.getMapper(LogMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectManagerRK();
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //日志筛选
   public PageBean<Map<String,Object>>screen(Map<String,Object> maps, int currentPage, int pageSize){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       LogMapper mapper = sqlSession.getMapper(LogMapper.class);
       int size = pageSize;

       int begin=(currentPage-1)*size;
       List<Map<String, Object>> maps1 = null;
       //调用mapper
       List<Log> screen = mapper.screen(maps, begin, size);



       int i = mapper.screenCount(maps);
       if (screen.size()>0){
           maps1 = mapper.screenInformation(screen);
       }
       
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
       pageBean.setRows(maps1);
       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;
   }


    //日志模糊查询（根据物料号和订单号）
    public PageBean<Map<String,Object>> search(String str,int sort, int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        int size = pageSize;

        int begin=(currentPage-1)*size;
        //调用mapper
        List<Map<String, Object>> search = mapper.search(str,sort, begin, size);
        int i = mapper.searchCount(str,sort);
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(search);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //在一段时间内统计已经存在的物料出库还是入库的总量
    public PageBean<Map<String,Object>> Statistics( Map<String,Object>maps,int currentPage,int pageSize){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        //设置页码
        int size = pageSize;
        int begin = (currentPage - 1)*size;
        //调用mapper
        List<Map<String, Object>> statistics = mapper.Statistics(maps, begin, size);
        int i = mapper.StatisticsCount(maps);

        //设置pageBean
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(statistics);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;

    }

    //查询某个申请单中是否存在未签字的日志(让申请人签字)
    public boolean selectWqz(int appFormId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        //调用mapper
        boolean b = mapper.selectWqz(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //申请人签字
    public void updateQz(String url,int appFormId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        ApplicationContentMapper mapper1 = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper2 = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.updateQz(url,appFormId);
        //查询该申请单是否已经完成了
        boolean b = mapper1.selectComplete(appFormId);
        if (b){
            //未完成，更新申请单为处理中
            mapper2.updateStatus(0, appFormId);

        }
        else {
            //完成，更新申请单为已完成
            mapper2.updateStatus(1, appFormId);
        }
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询该申请单需要签字的产品内容
    public List<Map<String,Object>>selectQzProduct(int appFormId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectQzProduct(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询一个申请人是否存在未签字的申请单
    public boolean selectIfQzByUserId(int userId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        LogMapper mapper = sqlSession.getMapper(LogMapper.class);
        //调用mapper
        boolean b = mapper.selectIfQzByUserId(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
}
