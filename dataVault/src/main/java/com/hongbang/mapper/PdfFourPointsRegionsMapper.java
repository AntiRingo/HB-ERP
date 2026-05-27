package com.hongbang.mapper;

import com.hongbang.pojo.PdfFourPointsRegions;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PdfFourPointsRegionsMapper {
    //添加
    @Insert("insert into pdf_four_points_regions values (#{id},#{excelId},#{point1X},#{point1Y},#{point2X},#{point2Y},#{point3X},#{point3Y},#{point4X},#{point4Y}) ")
    void add(PdfFourPointsRegions pdfFourPointsRegions);

    //删除
    @Delete("delete from pdf_four_points_regions where excel_id = #{excelId}")
    void delete(@Param("excelId") int excelId);

    //查询
    @Select("select * from  pdf_four_points_regions where excel_id = #{excelId}")
    PdfFourPointsRegions selectByExcelId(@Param("excelId") int excelId);
}
