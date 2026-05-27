package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.ExamineConditionContent;
import com.hongbang.service.ExamineConditionContentService;
import com.hongbang.service.impl.ExamineConditionContentServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/examineConditionContent/*")
public class ExamineConditionContentServlet extends BaseServlet {
    //获取service
    ExamineConditionContentService examineConditionContentService = new ExamineConditionContentServiceImpl();


    //添加审核条件的内容
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收起前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        ExamineConditionContent examineConditionContent = JSON.parseObject(s, ExamineConditionContent.class);
        //调用service
        examineConditionContentService.add(examineConditionContent);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }
    //修改审核条件的内容
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        ExamineConditionContent examineConditionContent = JSON.parseObject(s, ExamineConditionContent.class);
        //调用service
        examineConditionContentService.update(examineConditionContent);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");


    }

    //删除审核条件的内容
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        examineConditionContentService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据step_id查询出，该步骤拥有什么条件和内容
    public void selectByStep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = examineConditionContentService.selectByStep(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据stepId查询该步骤所拥有的条件
    public void  selectCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Integer> list = examineConditionContentService.selectCondition(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }




}
