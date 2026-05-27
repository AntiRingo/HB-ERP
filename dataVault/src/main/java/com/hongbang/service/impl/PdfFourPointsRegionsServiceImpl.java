package com.hongbang.service.impl;


import com.hongbang.mapper.PdfFourPointsRegionsMapper;
import com.hongbang.pojo.PdfFourPointsRegions;
import com.hongbang.service.PdfFourPointsRegionsService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class PdfFourPointsRegionsServiceImpl implements PdfFourPointsRegionsService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //添加
    public void add(PdfFourPointsRegions pdfFourPointsRegions){
        //获取sqlSession
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfFourPointsRegionsMapper mapper = sqlSession.getMapper(PdfFourPointsRegionsMapper.class);
        //调用mapper
        mapper.add(pdfFourPointsRegions);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfFourPointsRegionsMapper mapper = sqlSession.getMapper(PdfFourPointsRegionsMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询
    public  PdfFourPointsRegions selectByExcelId(int excelId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfFourPointsRegionsMapper mapper = sqlSession.getMapper(PdfFourPointsRegionsMapper.class);
        //调用mapper
        PdfFourPointsRegions pdfFourPointsRegions = mapper.selectByExcelId(excelId);
        //释放资源
        sqlSession.close();
        //返回值
        return pdfFourPointsRegions;
    }
}
