package com.hongbang.service.impl;

import com.hongbang.mapper.PdfExcelMapper;
import com.hongbang.pojo.PdfExcel;
import com.hongbang.service.PdfExcelService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class PdfExcelServiceImpl implements PdfExcelService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加
    public void addPdfExcel(List<PdfExcel> pdfExcels){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
        //调用mapper
        mapper.addPdfExcel(pdfExcels);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询数据
   public List<PdfExcel> selectpdf(int bomTitleId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
       //调用mapper
       List<PdfExcel> selectpdf = mapper.selectpdf(bomTitleId);
       //释放资源
       sqlSession.close();
       //返回值
       return selectpdf;
   }


    //查询pdf对应的excel
    public List<PdfExcel> selectExcel( int bomTitleId, int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
        //调用mapper
        List<PdfExcel> pdfExcels = mapper.selectExcel(bomTitleId, id);
        //释放资源
        sqlSession.close();
        //返回值
        return pdfExcels;
    }


    //增加缩放尺寸和原点坐标
   public void updateXY(String scale,double originCanvasX, double originCanvasY,int id,String pdfScale,int rotation,boolean isFlippedX,boolean isFlippedY,String excelUrl){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
       //调用mapper
       mapper.updateXY(scale,originCanvasX,originCanvasY,id,pdfScale,rotation,isFlippedX,isFlippedY);
       //通过路径寻找ID

       int excelId = mapper.selectIdByUrl(excelUrl);
        //更新配对情况
       mapper.updatePaired(excelId,id);
       mapper.updatePaired(id,excelId);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //根据id查询pdf
    public PdfExcel selectpdfById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
        //调用mapper
        PdfExcel pdfExcel = mapper.selectpdfById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return pdfExcel;
    }

    //删除pdf以及excel
    public void deletePdfExcel(int id,  int bomTitleId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);

        //查询要删除的文件路径
        //查询pdf
        PdfExcel pdfExcel = mapper.selectpdfById(id);


        //查询配对的ID
        int fileId = pdfExcel.getFileId();

        //取消配对
        mapper.removePaired(id);
        mapper.removePaired(fileId);


        mapper.deletePdfExcel(id,bomTitleId);
//
        sqlSession.commit();
        sqlSession.close();
    }


    //查询Excel数据

    public List<PdfExcel> selectAllExcel( int bomTitleId){
        //获取session
        SqlSession sqlSession = factory.openSession();
       //获取mapper
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
        //调用马mapper
        List<PdfExcel> pdfExcels = mapper.selectAllExcel(bomTitleId);
        //释放资源
        sqlSession.close();
        return pdfExcels;


    }

    //查询是否已经配对,以及配对信息
    public String selectExcelUrlByPdfId(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        PdfExcelMapper mapper = sqlSession.getMapper(PdfExcelMapper.class);
        boolean b = mapper.selectIfPaired(id);
        if (b){
            //存在查询数据
            return mapper.selectExcelUrlByPdfId(id);
        }
        else {
            //不存在返回
            return "false";

        }

    }
}
