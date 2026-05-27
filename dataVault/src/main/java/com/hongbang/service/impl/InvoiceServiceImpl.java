package com.hongbang.service.impl;

import com.hongbang.mapper.FormRkMapper;
import com.hongbang.mapper.InvoiceMapper;
import com.hongbang.pojo.FormRk;
import com.hongbang.pojo.Invoice;
import com.hongbang.service.InvoiceService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class InvoiceServiceImpl implements InvoiceService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //根据appId查询发票信息
    public List<Invoice> selectFp(int appId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InvoiceMapper mapper = sqlSession.getMapper(InvoiceMapper.class);
        //调用mapper
        List<Invoice> invoices = mapper.selectFp(appId);
        //释放资源
        sqlSession.close();
        //返回值
        return invoices;
    }


    //插入多张发票信息
    public void insertAllInvoice(List<FormRk> formRks,List<Invoice> invoices){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InvoiceMapper mapper = sqlSession.getMapper(InvoiceMapper.class);
        FormRkMapper mapper1 = sqlSession.getMapper(FormRkMapper.class);
        // 获取服务器当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        for (int i = 0; i < formRks.size(); i++) {
            FormRk formRk = formRks.get(i);
            formRk.setRkTime(formattedTime);
        }
        //调用mapper
        mapper1.add(formRks);
        mapper.insertAllInvoice(invoices);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询未审核发票的申请单信息
   public List<Map<String,Object>> selectWsFp(Map<String,Object> maps){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       InvoiceMapper mapper = sqlSession.getMapper(InvoiceMapper.class);
       //调用mapper
       List<Map<String, Object>> maps1 = mapper.selectWsFp(maps);
       //释放资源
       sqlSession.close();
       //返回值
       return maps1;
   }

    //通过审核发票信息
    public void updatePass(int appId,String time){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InvoiceMapper mapper = sqlSession.getMapper(InvoiceMapper.class);
        //调用mapper
        mapper.updatePass(appId,time);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //不通过发票信息
    public void updateNoPass( int appId,String time){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InvoiceMapper mapper = sqlSession.getMapper(InvoiceMapper.class);
        //调用mapper
        mapper.updateNoPass(appId,time);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


}
