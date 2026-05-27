package com.hongbang.mapper;

import com.hongbang.pojo.PdfExcel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PdfExcelMapper {

    //添加
    void addPdfExcel(@Param("pdfExcels") List<PdfExcel> pdfExcels);

    //查询数据
    @Select("select * from pdf_excel where bom_title_id = #{bomTitleId} and type IN ('.pdf', '.PDF')")
    List<PdfExcel> selectpdf(@Param("bomTitleId") int bomTitleId);


    //查询Excel数据
    @Select("select * from pdf_excel where bom_title_id = #{bomTitleId} and type IN ('.xls', '.xlsx', '.xlsm', '.xlsb', '.csv')")
    List<PdfExcel> selectAllExcel(@Param("bomTitleId") int bomTitleId);


    //查询pdf对应的excel
    @Select("select * from pdf_excel where bom_title_id = #{bomTitleId} and time = (select time from pdf_excel where id = #{id}) and type not in ('.pdf', '.PDF') ")
    List<PdfExcel> selectExcel(@Param("bomTitleId") int bomTitleId,@Param("id") int id);

    //增加缩放尺寸和原点坐标
    @Update("update pdf_excel set scale = #{scale},originCanvasX = #{originCanvasX},originCanvasY = #{originCanvasY},pdfScale = #{pdfScale},rotation = #{rotation},flippedX=#{isFlippedX},flippedY=#{isFlippedY} where id = #{id}")
    void updateXY(@Param("scale") String scale,@Param("originCanvasX") double originCanvasX,@Param("originCanvasY") double originCanvasY,@Param("id") int id,@Param("pdfScale") String pdfScale,@Param("rotation") int rotation,@Param("isFlippedX") boolean isFlippedX,@Param("isFlippedY") boolean isFlippedY);

    //根据id查询pdf
    @Select("select * from pdf_excel where id = #{id}")
    PdfExcel selectpdfById(@Param("id") int id);

    //删除pdf以及excel
    @Delete("delete from  pdf_excel where id = #{id}")
    void deletePdfExcel(@Param("id") int id,@Param("bomTitleId") int bomTitleId);

    //根据路径寻找ID
    @Select("select id from pdf_excel where url = #{url}")
    int selectIdByUrl(@Param("url") String url);

    //更新配对情况
    @Update("update pdf_excel set fileId = #{fileId},paired = 1 where id =#{id}")
    void updatePaired(@Param("fileId") int fileId,@Param("id") int id);



    //取消配对情况
    @Update("update pdf_excel set paired = 0,fileId = 0 where id = #{id} ")
    void removePaired(@Param("id") int id);

    //查询是否已经配对,以及配对信息
    @Select("select count(*) from pdf_excel where id = #{id} and paired = 1")
    boolean selectIfPaired(@Param("id") int id);

    @Select("select url from pdf_excel where id in (select fileId from pdf_excel where id = #{id} and paired = 1)")
    String selectExcelUrlByPdfId(@Param("id") int id);


}
