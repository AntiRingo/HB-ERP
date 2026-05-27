package com.hongbang.service;

import com.hongbang.pojo.PdfFourPointsRegions;

public interface PdfFourPointsRegionsService {

    //添加
    void add(PdfFourPointsRegions pdfFourPointsRegions);

    //删除
    void delete(int id);


    //查询
    PdfFourPointsRegions selectByExcelId(int excelId);
}
