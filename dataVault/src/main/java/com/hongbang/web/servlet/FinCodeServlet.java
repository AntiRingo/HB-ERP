package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.Code;
import com.hongbang.pojo.FinCode;
import com.hongbang.service.FinCodeService;
import com.hongbang.service.impl.FinCodeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/finCode/*")
public class FinCodeServlet extends BaseServlet {
    FinCodeService finCodeService = new FinCodeServiceImpl();


    public void maxLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        int i = finCodeService.maxLevel();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");


    }

    //查询该分类是否有规则存在
    public void ifCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finCodeService.ifCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查询规则并回显
    public void selectCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的消息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        List<FinCode> finCodes = finCodeService.selectCode(Integer.parseInt(s));

        //转化为JSON数据
        String s1 = JSON.toJSONString(finCodes, SerializerFeature.WriteNullStringAsEmpty);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //添加code
    public void addCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的消息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        FinCode finCode = JSON.parseObject(s, FinCode.class);
        //调用service
       finCodeService.addCode(finCode);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询这条规则是不是在使用
    public void selectIfLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的level
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finCodeService.selectIfLevel(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //删除规则
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        finCodeService.delete(Integer.parseInt(s));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询所有
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinCode> finCodes = finCodeService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(finCodes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询有几条规则
    public void selectCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        int i = finCodeService.selectCount();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //更新规则
    public void updateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        FinCode finCode = JSON.parseObject(s, FinCode.class);

        //调用service
        finCodeService.updateCode(finCode);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


}
