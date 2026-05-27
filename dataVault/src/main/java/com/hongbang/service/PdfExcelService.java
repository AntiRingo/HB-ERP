package com.hongbang.service;

import com.hongbang.pojo.PdfExcel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PdfExcelService {

    //添加
   void addPdfExcel(List<PdfExcel> pdfExcels);

    //查询数据
    List<PdfExcel> selectpdf(int bomTitleId);

    //查询pdf对应的excel
    List<PdfExcel> selectExcel( int bomTitleId, int id);


    //增加缩放尺寸和原点坐标
    void updateXY(String scale,double originCanvasX,double originCanvasY,int id,String pdfScale,int rotation,boolean isFlippedX,boolean isFlippedY,String excelUrl);

    //根据id查询pdf
    PdfExcel selectpdfById(int id);

    //删除pdf以及excel
    void deletePdfExcel(int id,  int bomTitleId);


 //查询Excel数据
 @Select("select * from pdf_excel where bom_title_id = #{bomTitleId} and type IN ('.xls', '.xlsx', '.xlsm', '.xlsb', '.csv')")
 List<PdfExcel> selectAllExcel(int bomTitleId);

 //查询是否已经配对,以及配对信息
 String selectExcelUrlByPdfId(int id);
}
