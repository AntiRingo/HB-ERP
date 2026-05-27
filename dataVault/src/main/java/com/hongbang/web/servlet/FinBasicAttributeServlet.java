package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.BasicAttribute;
import com.hongbang.pojo.FinBasicAttribute;
import com.hongbang.service.BasicAttributeService;
import com.hongbang.service.FinBasicAttributeService;
import com.hongbang.service.impl.BasicAttributeServiceImpl;
import com.hongbang.service.impl.FinBasicAttributeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/finBasicAttribute/*")
public class FinBasicAttributeServlet extends BaseServlet {
   FinBasicAttributeService finBasicAttributeService = new FinBasicAttributeServiceImpl();

    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<FinBasicAttribute> finBasicAttributes = finBasicAttributeService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(finBasicAttributes);
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
        boolean b = finBasicAttributeService.selectExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


}
