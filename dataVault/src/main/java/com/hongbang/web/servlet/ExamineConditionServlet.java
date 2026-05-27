package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.ExamineCondition;
import com.hongbang.service.ExamineConditionService;
import com.hongbang.service.impl.ExamineConditionServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/examineCondition/*")
public class ExamineConditionServlet extends BaseServlet {
    //获取service
    ExamineConditionService examineConditionService = new ExamineConditionServiceImpl();

    //查询所有的条件
   public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
       List<ExamineCondition> examineConditions = examineConditionService.selectAll();
       //转化为json数据
       String s = JSON.toJSONString(examineConditions);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }


}
