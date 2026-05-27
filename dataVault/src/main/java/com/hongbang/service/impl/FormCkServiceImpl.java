package com.hongbang.service.impl;

import com.hongbang.mapper.FormCkMapper;
import com.hongbang.pojo.FormCk;
import com.hongbang.service.FormCkService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FormCkServiceImpl implements FormCkService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增出库条目信息
    public void add(List<FormCk> formCks){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        mapper.add(formCks);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据id产品id和仓库查询上一次的记录
    public FormCk selectLast(int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        FormCk formCk = mapper.selectLast(productId, vault);
       //释放资源
        sqlSession.close();
        //返回值
         return formCk;
    }

    //根据时间查询出库信息
    public List<Map<String,Object>> selectCkFormListBy(Map<String,Object> maps){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps1 = mapper.selectCkFormListBy(maps);
        //释放资源
        sqlSession.close();
        //返回值
        return maps1;
    }

    //每天查询出库的情况
    public List<Map<String,Object>> selectTodayCk(String startTime, String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectTodayCk(startTime, endTime);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询某个产品在这段时间内的总出库量
   public Map<String, Object> selectSumCk(String startTime,  String endTime,int productId, int vault){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
       //调用mapper
       Map<String, Object> maps = mapper.selectSumCk(startTime, endTime, productId, vault);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }



    //        -----------------------------------------------------
//        查询form_ck表中该物料信息的最新出库情况，要查看入库单信息
   public List<FormCk> selectNewCkById(int productId, int vault){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
       //调用mapper
       List<FormCk> formCkList = mapper.selectNewCkById(productId, vault);
       //释放资源
       sqlSession.close();
       //返回值
       return formCkList;
   }


    //根据申请单id，物料id，仓库查询出库情况
    public List<FormCk> selectNewCkByFormId(int productId, int vault, int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        List<FormCk> formCkList = mapper.selectNewCkByFormId(productId, vault, appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return formCkList;
    }


    // 查询该申请单中该物料信息上次的ckTotal是多少
    public List<FormCk> selectNewCk(int productId, int vault,int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        List<FormCk> formCkList = mapper.selectNewCk(productId, vault, appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return formCkList;
    }

    //根据日期，申请单id，物料id，仓库，查询今天的总出库量
    public double selectCkSumByForm(int appFormId, int productId, int vault,String startTime, String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
        //调用mapper
        double v = mapper.selectCkSumByForm(appFormId, productId, vault, startTime, endTime);
        //释放资源
        sqlSession.close();
        //返回值
        return v;
    }


    //根据appformid。productId，vault,时间，去查询在一段时间内的最新的ck_total
   public FormCk selectCkTotalBetween(int appFormId,int productId, int vault, String startTime, String endTime){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormCkMapper mapper = sqlSession.getMapper(FormCkMapper.class);
       //调用mapper
       FormCk formCk = mapper.selectCkTotalBetween(appFormId, productId, vault, startTime, endTime);
       //释放资源
       sqlSession.close();
       //返回值
       return formCk;
   }
}
