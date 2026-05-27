package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.FinAttributeValue;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.FinAttributeValueService;
import com.hongbang.service.impl.FinAttributeValueServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/finAttributeValue/*")
public class FinAttributeValueServlet extends BaseServlet {
    FinAttributeValueService finAttributeValueService = new FinAttributeValueServiceImpl();

    //根据属性ID去查询该属性下是否有编码表存在
    public void  selectIfCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finAttributeValueService.selectIfCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //分页查询
    public void   selectAttributeValueLimit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        String currentPage = request.getParameter("currentPage");


        int pageSize=200;
        //调用service
        PageBean<FinAttributeValue> pageBean = finAttributeValueService.selectAttributeValueLimit(Integer.parseInt(s1), Integer.parseInt(currentPage), pageSize);
        //转化为json数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinAttributeValue> finAttributeValues = finAttributeValueService.selectById(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finAttributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //判断属性值是否重复
    public void  selectValueExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);
        //调用service
        boolean b = finAttributeValueService.selectValueExist(finAttributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //修改
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);
        //调用service
        finAttributeValueService.update(finAttributeValue);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查看当前属性名下面都有什么属性值

    public void  selectByAttNameId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
//        System.out.println(s);
        //调用service
        List<FinAttributeValue> finAttributeValues = finAttributeValueService.selectByAttNameId(Integer.parseInt(s));
//        System.out.println(attributeValues);
        //转化为JSON数据
        String s1 = JSON.toJSONString(finAttributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //删除
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        finAttributeValueService.delete(Integer.parseInt(s));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //判断属性值是否重复
    public void  selectValueExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);

        //调用service
        boolean b = finAttributeValueService.selectValueExistAdd(finAttributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }
    //判断编码值是否重复
    public void  selectCodeExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);
        //调用service
        boolean b = finAttributeValueService.selectCodeExist(finAttributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //判断编码值是否重复
    public void  selectCodeExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);
//        System.out.println(finAttributeValue);
        //调用service
        boolean b = finAttributeValueService.selectCodeExistAdd(finAttributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //添加
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //JSON数据序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);

        //调用service
        finAttributeValueService.add(finAttributeValue);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //根据nameId,attValue去查询编码
    public  void selectCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeValue finAttributeValue = JSON.parseObject(s, FinAttributeValue.class);

        //调用service
        List<FinAttributeValue> finAttributeValues = finAttributeValueService.selectCode(finAttributeValue);
        //转化为JSON数据
        String s1 = JSON.toJSONString(finAttributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //循环加入编码，属性值作为编码的功能
    public void addCodeAuto (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<FinAttributeValue> finAttributeValues = JSONArray.parseArray(s, FinAttributeValue.class);
        //调用service
        finAttributeValueService.addCodeAuto(finAttributeValues);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //输入联想
    public void inputLX(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String attNameId = request.getParameter("attNameId");

        //调用service
        List<Map<String, Object>> maps = finAttributeValueService.inputLX(str, Integer.parseInt(attNameId));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //显示页面使用---------------------------------------------------------------------------------------------------------------------------
    //查看当前属性下的所有属性值
    public void selectByAttNameIdDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
//        System.out.println(s);
        //调用service
        List<AttributeValue> attributeValues = finAttributeValueService.selectByAttNameIdDisplay(Integer.parseInt(s));
//        System.out.println(attributeValues);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }



}
