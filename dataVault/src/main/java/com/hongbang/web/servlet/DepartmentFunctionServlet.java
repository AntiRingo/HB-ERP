package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.pojo.User;
import com.hongbang.service.DepartmentFunctionService;
import com.hongbang.service.impl.departmentFunctionMapperServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/departmentFunction/*")
public class DepartmentFunctionServlet extends BaseServlet {
    //获取service
    DepartmentFunctionService departmentFunctionService = new departmentFunctionMapperServiceImpl();

    //    根据登录的账户等级查询拥有的模块
   public void selectModule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   //接收前端传来的数据
       String level = request.getParameter("level");
       String department = request.getParameter("departId");

       //调用service
           List<Module> modules = departmentFunctionService.selectModule(Integer.parseInt(level), Integer.parseInt(department));
           //转化为json数据
           String s = JSON.toJSONString(modules);
           //响应数据
           response.setContentType("text/json;charset=utf-8");
           response.getWriter().write(s);


    }

    //根据登录的账户等级查询拥有的模块下的功能信息
    public void  selectFunction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        String level = request.getParameter("level");
        String department = request.getParameter("departId");
            //调用service
            List<Function> functions = departmentFunctionService.selectFunction(Integer.parseInt(level), Integer.parseInt(department));
            //转化为json数据
            String s = JSON.toJSONString(functions);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);


    }

    //    查询具体功能
    public void selectFunctionTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String level = request.getParameter("level");
        String department = request.getParameter("departId");

            //调用service
            List<FunctionTwo> functionTwos = departmentFunctionService.selectFunctionTwo(Integer.parseInt(level), Integer.parseInt(department));
            //转化为json数据
            String s = JSON.toJSONString(functionTwos);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);

    }
}
