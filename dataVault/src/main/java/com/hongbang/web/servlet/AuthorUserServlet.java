package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.*;
import com.hongbang.service.AuthorUserService;
import com.hongbang.service.impl.AuthorUserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/authorUser/*")
public class AuthorUserServlet extends BaseServlet {
    //获取service
    AuthorUserService authorUserService = new AuthorUserServiceImpl();
    //查询自己管理的人员
    public void selectByAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s3 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s3, User.class);
        int id = user1.getId();
        List<UserPickerData> userPickerData = authorUserService.selectByAuthor(id);
        //转化为json数据
        String s1 = JSON.toJSONString(userPickerData);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //添加新的未审核的管理人员审核信息
    public void addAuthorUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        AuthorUserReview authorUserReview = JSON.parseObject(s1, AuthorUserReview.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<AuthorUser> authorUsers = JSONArray.parseArray(s2, AuthorUser.class);
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        authorUserReview.setTimes(formattedTime);
        //获取登录用户id
        //从session中获取登录用户的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s3 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s3, User.class);
        int id = user1.getId();

        //设置申请id，管理人id
        authorUserReview.setApplicant(id);
        for (int i = 0; i < authorUsers.size(); i++) {
            authorUsers.get(i).setAuthorId(id);
        }
        //调用service
        authorUserService.addAuthorUser(authorUserReview,authorUsers);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }
}
