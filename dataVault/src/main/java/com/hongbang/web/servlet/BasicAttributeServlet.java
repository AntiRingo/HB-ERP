package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.BasicAttribute;
import com.hongbang.service.BasicAttributeService;
import com.hongbang.service.impl.BasicAttributeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/basicAttribute/*")
public class BasicAttributeServlet extends BaseServlet {
   BasicAttributeService basicAttributeService = new BasicAttributeServiceImpl();

    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<BasicAttribute> basicAttributes = basicAttributeService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(basicAttributes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询是否属性名与系统公共属性重复
    public void selectExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = basicAttributeService.selectExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


}
