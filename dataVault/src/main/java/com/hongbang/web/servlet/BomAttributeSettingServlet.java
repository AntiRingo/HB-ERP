package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.BomAttributeSetting;
import com.hongbang.service.BomAttributeSettingService;
import com.hongbang.service.impl.BomAttributeSettingServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/bomAttributeSetting/*")
public class BomAttributeSettingServlet extends BaseServlet {
   //获取service
    BomAttributeSettingService attributeSettingService = new BomAttributeSettingServiceImpl();


    //添加
   public void addAttributeSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String id = bufferedReader.readLine();
       BomAttributeSetting bomAttributeSetting = new BomAttributeSetting(0,Integer.parseInt(id));

       //调用service
       attributeSettingService.addAttributeSetting(bomAttributeSetting);

       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }

   //删除
   public void deleteAttributeSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String id = bufferedReader.readLine();
       //调用service
       attributeSettingService.deleteAttributeSetting(Integer.parseInt(id));
       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }

    //查询数据
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //调用service
        List<AttributeName> attributeNames = attributeSettingService.selectAll();
        //转化为json数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }
}
