package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.PdfFourPointsRegions;
import com.hongbang.service.PdfFourPointsRegionsService;
import com.hongbang.service.impl.PdfFourPointsRegionsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet( "/pdfFourPointsRegions/*")
public class PdfFourPointsRegionsServlet extends BaseServlet {
   //添加service
    PdfFourPointsRegionsService pdfFourPointsRegionsService = new PdfFourPointsRegionsServiceImpl();



    //添加
   public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       //接受前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();

       String excelId = request.getParameter("excelId");
       JSONArray array = JSON.parseArray(s);

       PdfFourPointsRegions pdfFourPointsRegions = new PdfFourPointsRegions(0,Integer.parseInt(excelId),array.getJSONObject(0).getDoubleValue("x"),array.getJSONObject(0).getDoubleValue("y"),array.getJSONObject(1).getDoubleValue("x"),array.getJSONObject(1).getDoubleValue("y"),array.getJSONObject(2).getDoubleValue("x"),array.getJSONObject(2).getDoubleValue("y"),array.getJSONObject(3).getDoubleValue("x"),array.getJSONObject(3).getDoubleValue("y"));
       pdfFourPointsRegionsService.add(pdfFourPointsRegions);

       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");


   }

   //删除
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        pdfFourPointsRegionsService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询
    public  void  selectByExcelId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String excelId = bufferedReader.readLine();
        //调用service
        PdfFourPointsRegions region = pdfFourPointsRegionsService.selectByExcelId(Integer.parseInt(excelId));
        if (region!=null){
            JSONArray pointsArray = new JSONArray();

            // 添加第一个点
            JSONObject point1 = new JSONObject();
            point1.put("x", region.getPoint1X());
            point1.put("y", region.getPoint1Y());
            pointsArray.add(point1);

            // 添加第二个点
            JSONObject point2 = new JSONObject();
            point2.put("x", region.getPoint2X());
            point2.put("y", region.getPoint2Y());
            pointsArray.add(point2);

            // 添加第三个点
            JSONObject point3 = new JSONObject();
            point3.put("x", region.getPoint3X());
            point3.put("y", region.getPoint3Y());
            pointsArray.add(point3);

            // 添加第四个点
            JSONObject point4 = new JSONObject();
            point4.put("x", region.getPoint4X());
            point4.put("y", region.getPoint4Y());
            pointsArray.add(point4);


            //相应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(pointsArray.toJSONString());
        }


    }
}
