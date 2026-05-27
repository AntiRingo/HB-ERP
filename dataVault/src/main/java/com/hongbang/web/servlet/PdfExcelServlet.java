package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.PdfExcel;
import com.hongbang.service.PdfExcelService;
import com.hongbang.service.impl.PdfExcelServiceImpl;
import com.hongbang.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/pdfExcel/*")
public class PdfExcelServlet extends BaseServlet {

    PdfExcelService service = new PdfExcelServiceImpl();


    public void selectPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String bomTitleId = bufferedReader.readLine();
        //调用service
        List<PdfExcel> selectpdf = service.selectpdf(Integer.parseInt(bomTitleId));
        //转化为JSON数据
        String s = JSON.toJSONString(selectpdf);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询Excel数据

    public void selectAllExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String bomTitleId = bufferedReader.readLine();
        //调用service
        List<PdfExcel> selectpdf = service.selectAllExcel(Integer.parseInt(bomTitleId));
        //转化为JSON数据
        String s = JSON.toJSONString(selectpdf);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询pdf对应的excel
    public void selectExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String id = request.getParameter("id");
        String bomTitleId = request.getParameter("bomTitleId");
        //调用service
        List<PdfExcel> pdfExcels = service.selectExcel(Integer.parseInt(bomTitleId), Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(pdfExcels);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //增加缩放尺寸和原点坐标
    public void updateXY(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{


        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String excelUrl = bufferedReader.readLine();
        System.out.println(excelUrl);

        String scale = request.getParameter("scale");
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String id = request.getParameter("id");
        String pdfScale = request.getParameter("pdfScale");
        String rotation = request.getParameter("rotation");
        String isFlippedX = request.getParameter("isFlippedX");
        String isFlippedY = request.getParameter("isFlippedY");




        //调用service
        service.updateXY(scale,Double.parseDouble(x),Double.parseDouble(y),Integer.parseInt(id),pdfScale,Integer.parseInt(rotation),Boolean.parseBoolean(isFlippedX),Boolean.parseBoolean(isFlippedY), excelUrl);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据id查询pdf
    public void selectpdfById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        PdfExcel pdfExcel = service.selectpdfById(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(pdfExcel);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //删除pdf和excel
    public void deletePdfExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的pdf的id
        String id = request.getParameter("id");
        String bomTitleId = request.getParameter("bomTitleId");
        System.out.println(id);
        System.out.println(bomTitleId);

      //调用service

        //查询要删除的文件路径
        //查询pdf
        PdfExcel pdfExcel = service.selectpdfById(Integer.parseInt(id));

        String url1 = pdfExcel.getUrl();


        String filePath1 = "./"+url1;
// 需要先获取绝对路径
        String absolutePath1 = getServletContext().getRealPath(filePath1);

        //删除文件
        FileUtil.delFile(absolutePath1);
        service.deletePdfExcel(Integer.parseInt(id),Integer.parseInt(bomTitleId));
        //取消配对
        //更新
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");


    }

    //查询是否已经配对以及配对的Excel路径
    public void  selectExcelUrlByPdfId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        String s = service.selectExcelUrlByPdfId(Integer.parseInt(id));
        if (!s.equals("false")){
            String s1 = JSON.toJSONString(s);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);

        }
    }
}
