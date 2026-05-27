package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.Code;
import com.hongbang.service.CodeService;
import com.hongbang.service.impl.CodeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@WebServlet("/code/*")
public class CodeServlet extends BaseServlet {
    //获取service
    CodeService codeService = new CodeServiceImpl();

    //查询该分类是否有规则存在
    public void ifCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = codeService.ifCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //添加code
    public void addCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的消息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        Code code = JSON.parseObject(s, Code.class);
        //调用service
        codeService.addCode(code);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //查询规则并回显
    public void selectCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的消息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        List<Code> codes = codeService.selectCode(Integer.parseInt(s));

        //转化为JSON数据
        String s1 = JSON.toJSONString(codes, SerializerFeature.WriteNullStringAsEmpty);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //更新规则
    public void updateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        Code code = JSON.parseObject(s, Code.class);

        //调用service
        codeService.updateCode(code);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询所有
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Code> codes = codeService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(codes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //删除规则
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        codeService.delete(Integer.parseInt(s));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查询有几条规则
    public void selectCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        int i = codeService.selectCount();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //查询这条规则是不是在使用
    public void selectIfLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的level
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = codeService.selectIfLevel(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询最大级数
    public void maxLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        int i = codeService.maxLevel();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }
}
