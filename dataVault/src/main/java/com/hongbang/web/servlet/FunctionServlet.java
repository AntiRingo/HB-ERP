package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.service.FunctionService;
import com.hongbang.service.impl.FunctionServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/function/*")
public class FunctionServlet extends BaseServlet {
    //获取service
    FunctionService functionService = new FunctionServiceImpl();

    //查询所有功能列表
    public  void selectAllFunction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //调用service
        List<Function> functions = functionService.selectAllFunction();
        //转化为json数据
        String s = JSON.toJSONString(functions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询通用功能
    public void selectTY(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Function> functions = functionService.selectTY();
        //转化为json数据
        String s = JSON.toJSONString(functions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询通用功能 （一级）
    public void selectModuleTY(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Module> modules = functionService.selectModuleTY();
        //转化为json数据
        String s = JSON.toJSONString(modules);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据functiontwoid查询该功能模块下都有什么权限
    public void selectFunctionTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FunctionTwo> functionTwos = functionService.selectFunctionTwo(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(functionTwos);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }
}
