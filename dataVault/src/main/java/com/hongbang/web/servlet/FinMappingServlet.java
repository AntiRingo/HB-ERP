package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.FinMapping;
import com.hongbang.pojo.Mapping;
import com.hongbang.service.FinAttributeService;
import com.hongbang.service.FinMappingService;
import com.hongbang.service.impl.FinAttributeServiceImpl;
import com.hongbang.service.impl.FinMappingServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/finMapping/*")
public class FinMappingServlet extends BaseServlet {
    FinMappingService finMappingService = new FinMappingServiceImpl();
    FinAttributeService finAttributeService = new FinAttributeServiceImpl();

    //查看是否存在于映射中
    public void  selectLengthIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finMappingService.selectLengthIfExist(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询长度
    public void selectLength(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finMappingService.selectLength(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //在映射表中根据sortid查询数据
    public void selectBySortId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinMapping> finMappings = finMappingService.selectBySortId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finMappings);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询当前分类下的已经存在的映射
    public void selectMapping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finMappingService.selectMapping(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }



    //根据sortid和attNameId查询要删除的信息
    public void selectBySA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String attNameId = bufferedReader.readLine();

        String parentId = request.getParameter("parentId");


        //调用service
        List<FinMapping> finMappings = finMappingService.selectBySA(Integer.parseInt(parentId), Integer.parseInt(attNameId));
        //转化为JSON数据
        String s = JSON.toJSONString(finMappings);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //删除映射
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finMappingService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //根据attNameId查询该属性书否在映射中被使用
    public void  selectIfUse (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinMapping> finMappings = finMappingService.selectIfUse(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finMappings);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //新增映射
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        FinMapping finMapping = JSON.parseObject(s, FinMapping.class);
        int attNameId = finMapping.getFinAttNameId();
        int length = finMapping.getLength();
        //调用service
        finMappingService.add(finMapping);
        finAttributeService.updateLength(length, attNameId);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查看除了当前映射外是否还有其他映射使用该属性
    public void selectIfOtherUse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        String attNameId = request.getParameter("attNameId");

        //调用service
        boolean b = finMappingService.selectIfOtherUse(Integer.parseInt(id), Integer.parseInt(attNameId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }
    //更新映射
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn数据序列化
        FinMapping finMapping = JSON.parseObject(s, FinMapping.class);

        int attNameId = finMapping.getFinAttNameId();
        int length = finMapping.getLength();
        //调用service
        finMappingService.update(finMapping);
        finAttributeService.updateLength(length, attNameId);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



}
