package com.hongbang.service.impl;

import com.hongbang.mapper.FormRkMapper;
import com.hongbang.pojo.FormRk;
import com.hongbang.service.FormRkService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FormRkServiceImpl implements FormRkService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //新增入库条目信息
    public void add(List<FormRk> formRks){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        mapper.add(formRks);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //根据时间查询入库信息
    public List<Map<String,Object>> selectRkFormListBy(Map<String,Object> maps){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps1 = mapper.selectRkFormListBy(maps);
        //释放资源
        sqlSession.close();
        //返回值
        return maps1;
    }

    //查询最新一批的申请信息
    public List<Map<String,Object>> selectNewApp(int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectNewApp(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //每天查询入库的情况
   public List<Map<String,Object>> selectTodayRk(String startTime,String endTime){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectTodayRk(startTime, endTime);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //查询该申请单的物料是否今天有发票存在
    public List<FormRk> selectFpByTime(int appFormId, int productId, int vault,String startTime,String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<FormRk> formRkList = mapper.selectFpByTime(appFormId, productId, vault, startTime, endTime);
        //释放资源
        sqlSession.close();
        //返回值
        return formRkList;
    }


    //查询今天之前的最新价格信息
    public FormRk selectLastFormRk(int productId, int vault,  String actualPrice,String rkTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        FormRk formRkList = mapper.selectLastFormRk(productId, vault, actualPrice, rkTime);
        //释放资源
        sqlSession.close();
        //返回值
        return formRkList;
    }


    //查询某个物料信息在这段时间内的入库数量
    public String selectSumRk(String startTime,String endTime,int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        String s = mapper.selectSumRk(startTime, endTime, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return s;
    }

    //查询某个物料信息在这段时间内的退货入库数量
    public String selectThSumRk(String startTime,String endTime,int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        String s = mapper.selectThSumRk(startTime, endTime, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return s;
    }

    //查询在某个时间段以后的某个物料信息的入库所有的数据
    public List<Map<String,Object>> selectAppNumberAfterTime(int productId, int vault,String rkTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAppNumberAfterTime(productId, vault, rkTime);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询所有申请单中入库的某个物料信息的申请单
    public List<Map<String,Object>> selectAppNumberAllTime(int productId, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAppNumberAllTime(productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询一段时间内的最新价格（没有出库信息的时候用）
   public FormRk selectNew(String startTime, String endTime,int productId, int vault, int appFormId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
       //调用mapper
       FormRk formRk = mapper.selectNew(startTime, endTime, productId, vault, appFormId);
       //释放资源
       sqlSession.close();
       //返回值
       return formRk;
   }


    //查询在一个时间之前的的最新的价格(不是发票价格)
   public FormRk selectNewPriceNoFp(int productId, int vault,int appFormId,String rkTime){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
       //调用mapper
       FormRk formRk = mapper.selectNewPriceNoFp(productId, vault, appFormId, rkTime);
       //释放资源
       sqlSession.close();
       //返回值
       return formRk;
   }

    //查询某个申请单中的某个物料信息的入库情况
    public  Map<String,Object> selectByAppIdAndId(int appFormId, int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        Map<String,Object> map= mapper.selectByAppIdAndId(appFormId, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return map;
    }


    //查询今天的所有发票信息
    public List<FormRk> selectFpToday( String startTime, String endTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<FormRk> formRkList = mapper.selectFpToday(startTime, endTime);
        //释放资源
        sqlSession.close();
        //返回值
        return formRkList;
    }


    //查询该申请单中的物料信息在小于该发票时间的最新价格，可能存在多张发票，也可能不存在，，所以都要查
    public FormRk selectNewPriceSort(int productId,int vault, int appFormId,  String rkTime){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        FormRk formRk = mapper.selectNewPriceSort(productId, vault, appFormId, rkTime);
        //释放资源
        sqlSession.close();
        //返回值
        return formRk;
    }



    //查询这个申请单的入库时间，然后去查询所有的入库时间大于这个申请单并且物料id是product的,入库数量以及价格信息
    public List<Map<String,Object>> selectFormRkAndAppNumberByTime(int productId,int vault, int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectFormRkAndAppNumberByTime(productId, vault, appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }



    //根据appFormId,productId,vault,rk_time查询最新价格，包括发票价格
    public double selectPrice(int appFormId, int productId,  int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        double v = mapper.selectPrice(appFormId, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return v;
    }


    //根据productId查询所有的入库信息(包括数量)
    public List<Map<String,Object>>selectByProductId( int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectByProductId(productId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询上次的价格(要获取第二个，存在发票的时候才能用)
    public List<FormRk>selectLastPrice( int appFormId,int productId, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<FormRk> formRkList = mapper.selectLastPrice(appFormId, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return formRkList;
    }

    //查询某个物料还未从入库单出完的所有入库单信息
    public List<Map<String,Object>> selectAllCanCkByProductId(int productId, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FormRkMapper mapper = sqlSession.getMapper(FormRkMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllCanCkByProductId(productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

}
