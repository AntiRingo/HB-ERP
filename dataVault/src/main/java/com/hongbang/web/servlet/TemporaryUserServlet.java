package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.TemporaryUser;
import com.hongbang.pojo.User;
import com.hongbang.service.TemporaryUserService;
import com.hongbang.service.impl.TemporaryUserServiceImpl;
import org.apache.ibatis.annotations.Param;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/temporaryUser/*")
public class TemporaryUserServlet extends BaseServlet {
    //创建service
    TemporaryUserService temporaryUserService = new TemporaryUserServiceImpl();


    //创建临时用户
    public void addTemporaryUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //数据序列化
        TemporaryUser temporaryUser = JSON.parseObject(s, TemporaryUser.class);
        //调用service
        temporaryUserService.addTemporaryUser(temporaryUser);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //定时删除
    public void deleteByTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String time = bufferedReader.readLine();
        //调用service
        temporaryUserService.deleteByTime(time);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询用户名是否重复
    public void selectExistUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userName = bufferedReader.readLine();
        //调用service
        boolean b = temporaryUserService.selectExistUserName(userName);
        if (b){
            //存在
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }
    }


    //查询所有的临时账户细信息(分部长或者是总经理)
   public void selectAllTemporaryUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    //获取登录的用户信息
       request.setCharacterEncoding("utf-8");
       response.setContentType("text/html;charset=utf-8");
       HttpSession session = request.getSession();
       Object username = session.getAttribute("username");
       if (username!=null){
           String s = JSON.toJSONString(username);

           User user = JSON.parseObject(s, User.class);
           int level = user.getLevel();
           if (level==2){
               //总经理级别，调用service
               List<Map<String, Object>> maps = temporaryUserService.selectAllTemporaryUser();
               //转化为json数据
               String s1 = JSON.toJSONString(maps);
               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s1);
           }
           else if (level==3){
               //部长级别,获取部门，调用service
               int department = user.getDepartment();
               List<Map<String, Object>> maps = temporaryUserService.selectAllTemporaryUserByDepartment(department);
               //转化为json数据
               String s1 = JSON.toJSONString(maps);
               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s1);
           }
       }
   }


    //根据id删除临时账户
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        temporaryUserService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //更新到期时间
    public void updateOverTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String overTime = bufferedReader.readLine();
        String id = request.getParameter("id");
        //调用service
        temporaryUserService.updateOverTime(overTime, Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //验证临时账户是否正确，部门也要正确
    public void loginExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //数据序列化
        TemporaryUser temporaryUser = JSON.parseObject(s, TemporaryUser.class);
        //查看当前登录的人的部门
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        if (username != null) {
            String s1 = JSON.toJSONString(username);

            User user = JSON.parseObject(s1, User.class);
            int department = user.getDepartment();
            temporaryUser.setDepartment(department);
            //调用service
            boolean b = temporaryUserService.loginExist(temporaryUser);
            //响应数据
            if (b){
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write("success");
            }

        }
    }



}
