package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.Mapping;
import com.hongbang.service.AttributeService;
import com.hongbang.service.MappingService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.AttributeServiceImpl;
import com.hongbang.service.impl.MappingServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

@WebServlet( "/mapping/*")
public class MappingServlet extends BaseServlet{
    MappingService mappingService = new MappingServiceImpl();
    AttributeService attributeService = new AttributeServiceImpl();

   //新增映射
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        Mapping mapping = JSON.parseObject(s, Mapping.class);
        int attNameId = mapping.getAttNameId();
        int length = mapping.getLength();
        //调用service
        mappingService.add(mapping);
        attributeService.updateLength(length, attNameId);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查询当前分类下的已经存在的映射
    public void selectMapping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = mappingService.selectMapping(Integer.parseInt(s));
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
        List<Mapping> mappings = mappingService.selectBySortId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(mappings);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //删除映射
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        mappingService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据sortid和attNameId查询要删除的信息
    public void selectBySA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String attNameId = bufferedReader.readLine();

        String parentId = request.getParameter("parentId");


        //调用service
        List<Mapping> mappings = mappingService.selectBySA(Integer.parseInt(parentId), Integer.parseInt(attNameId));
        //转化为JSON数据
        String s = JSON.toJSONString(mappings);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //更新映射
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn数据序列化
        Mapping mapping = JSON.parseObject(s, Mapping.class);

        int attNameId = mapping.getAttNameId();
        int length = mapping.getLength();
        //调用service
        mappingService.update(mapping);
        attributeService.updateLength(length, attNameId);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查看是否存在于映射中
    public void  selectLengthIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = mappingService.selectLengthIfExist(Integer.parseInt(s));
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
        List<Map<String, Object>> maps = mappingService.selectLength(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据attNameId查询该属性书否在映射中被使用
    public void  selectIfUse (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Mapping> mappings = mappingService.selectIfUse(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(mappings);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查看除了当前映射外是否还有其他映射使用该属性
    public void selectIfOtherUse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        String attNameId = request.getParameter("attNameId");

        //调用service
        boolean b = mappingService.selectIfOtherUse(Integer.parseInt(id), Integer.parseInt(attNameId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查看当前属性是否在当前映射中被使用
    public void selectBySN(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        Mapping mapping = JSON.parseObject(s, Mapping.class);
        //调用service
        boolean b = mappingService.selectBySN(mapping);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //删除映射信息
    public void deleteMapExcelNextAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        List<Mapping> mappings = JSONArray.parseArray(s, Mapping.class);
        //调用service
        mappingService.deleteMapExcelNextAdd(mappings);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

}
