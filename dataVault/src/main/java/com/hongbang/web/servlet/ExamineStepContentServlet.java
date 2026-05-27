package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.service.ExamineStepContentService;
import com.hongbang.service.impl.ExamineStepContentServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/examineStepContent/*")
public class ExamineStepContentServlet extends BaseServlet {
    //获取service
    ExamineStepContentService examineStepContentService = new ExamineStepContentServiceImpl();

   public void selectByAppId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String appFormId = bufferedReader.readLine();
       //调用service
       List<Map<String, Object>> maps = examineStepContentService.selectByAppId(Integer.parseInt(appFormId));
       //转化为JSON数据
       String s = JSON.toJSONString(maps);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }

    //删除的时候查询这个步骤有没有用到
    public void selectIfUse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        boolean b = examineStepContentService.selectIfUse(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }
}
