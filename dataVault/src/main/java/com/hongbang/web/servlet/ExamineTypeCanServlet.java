package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.ExamineTypeCan;
import com.hongbang.service.ExamineTypeCanService;
import com.hongbang.service.impl.ExamineTypeCanServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/examineTypeCan/*")
public class ExamineTypeCanServlet extends BaseServlet {
  //获取service
    ExamineTypeCanService examineTypeCanService = new ExamineTypeCanServiceImpl();


    public void updatePermissions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        ExamineTypeCan examineTypeCan = JSON.parseObject(s, ExamineTypeCan.class);
        //调用service
        examineTypeCanService.updatePermissions(examineTypeCan);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //根据步骤ID查询数据
    public void selectByExamineId (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        ExamineTypeCan examineTypeCan = examineTypeCanService.selectByExamineId(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(examineTypeCan);
        //相应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }
}
