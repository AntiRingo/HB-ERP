package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Department;
import com.hongbang.service.DepartmentService;
import com.hongbang.service.impl.DepartmentServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet( "/department/*")
public class DepartmentServlet extends BaseServlet {

    //获取service
    DepartmentService departmentService = new DepartmentServiceImpl();

    //添加部门
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        Department department = JSON.parseObject(s, Department.class);
        //调用service
        departmentService.add(department);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //删除部门
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        departmentService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //修改部门
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        Department department = JSON.parseObject(s, Department.class);
        //调用service
        departmentService.update(department);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询所有数据(管理员)
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Department> departments = departmentService.selectAll();
        //转化为json数据
        String s = JSON.toJSONString(departments);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有数据（总经理）
    public void selectAllTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Department> departments = departmentService.selectAllTwo();
        //转化为json数据
        String s = JSON.toJSONString(departments);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id查询数据
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Department> departments = departmentService.selectById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(departments);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //添加部门判断部门名称是否重复
    public void addIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        boolean b = departmentService.addIfExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //修改部门时判断部门名称是否重复
    public void updateIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Department department = JSON.parseObject(s, Department.class);
        //调用service
        boolean b = departmentService.updateIfExist(department);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    //查询部门（选择审核步骤是使用）
    public void selectDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Department> departments = departmentService.selectDepartment();
        //转化为json数据
        String s = JSON.toJSONString(departments);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

}
