package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.service.RelationshipService;
import com.hongbang.service.impl.RelationshipServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/relationship/*")
public class RelationshipServlet extends BaseServlet {

    RelationshipService relationshipService = new RelationshipServiceImpl();

    //根据新的查询旧的
    public void selectOldByNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<ApplicationForm> applicationForms = relationshipService.selectOldByNew(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(applicationForms);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

}
