package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.UpdateNumberOfTimes;
import com.hongbang.service.UpdateNumberOfTimesService;
import com.hongbang.service.impl.UpdateNumberOfTimesServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/updateNumberOfTimes/*")
public class UpdateNumberOfTimesServlet extends BaseServlet {
  //获取service
    UpdateNumberOfTimesService updateNumberOfTimesService = new UpdateNumberOfTimesServiceImpl();

    //添加
   public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();
       //json序列化
       UpdateNumberOfTimes updateNumberOfTimes = JSON.parseObject(s, UpdateNumberOfTimes.class);
       //调用service
       updateNumberOfTimesService.add(updateNumberOfTimes);
       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }

   //修改
   public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //获取前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();
       //json序列化
       UpdateNumberOfTimes updateNumberOfTimes = JSON.parseObject(s, UpdateNumberOfTimes.class);
       int productId = updateNumberOfTimes.getProductId();
       UpdateNumberOfTimes updateNumberOfTimes1 = updateNumberOfTimesService.selectByPid(productId);
       String s1 = JSON.toJSONString(updateNumberOfTimes1);

       UpdateNumberOfTimes updateNumberOfTimes2 = JSON.parseObject(s1, UpdateNumberOfTimes.class);
       int updateNumber = updateNumberOfTimes2.getUpdateNumber();

       updateNumberOfTimes.setUpdateNumber(updateNumber+1);
       //调用service
       updateNumberOfTimesService.update(updateNumberOfTimes);
       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }


    //判断是否有记录
    public void selectIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = updateNumberOfTimesService.selectIfExist(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //判断是否有数量更新
    public void selectIfExist1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = updateNumberOfTimesService.selectIfExist1(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }
}
