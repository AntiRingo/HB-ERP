package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.User;
import com.hongbang.service.ExamineLogService;
import com.hongbang.service.impl.ExamineLogServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/examineLog/*")
public class ExamineLogServlet extends BaseServlet{
    //设置service
    ExamineLogService examineLogService = new ExamineLogServiceImpl();


    //查询日志记录
    public void selectExamineLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String currentPage = request.getParameter("currentPage");
        //JSON序列化
        Map map = JSON.parseObject(s, Map.class);
        int size = 50;
        int level = 0;
        //查看登录的账号等级
        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s2 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s2, User.class);
        int level1 = user1.getLevel();
        if (level1==2){
            level=2;
        }
        //获取部门
        int department = user1.getDepartment();

        //调用service
        PageBean<Map<String, Object>> pageBean = examineLogService.selectExamineLog(map, Integer.parseInt(currentPage), size,level,department);
        //转化为josn数据
        String s1 = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);


    }

    //查询审核人
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        int level=0;
        //json序列化
        Map map = JSON.parseObject(s, Map.class);
        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s2 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s2, User.class);
        int level1 = user1.getLevel();
        if (level1==2){
            level=2;
        }
        int department = user1.getDepartment();
        //调用service
        List list = examineLogService.selectUser(map, level,department);
        //转化为json数据
        String s1 = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //根据账号等级查询申请单信息 Map<String,Object> maps,int begin, int size, int department,int level
    public void  selectAppByLevel (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;

        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s2 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s2, User.class);
        if (user1==null){
            int level = user1.getLevel();
            int department = user1.getDepartment();

            //调用service
            PageBean pageBean = examineLogService.selectAppByLevel(map, Integer.parseInt(currentPage), pageSize, department, level);

            //转化为json数据
            String s1 = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        else {
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");

        }



    }

    //根据申请单id查询审核记录
    public void selectLogByAppId (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = examineLogService.selectLogByAppId(Integer.parseInt(appId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据申请单id查询否决的有多少个
    public void selectExamineStatusCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        boolean b = examineLogService.selectExamineStatusCount(Integer.parseInt(appId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }

    //根据等级查询申请人
    public void  selectAppUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s2 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s2, User.class);
        if (user1==null){
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }
        else {
            int level = user1.getLevel();
            int department = user1.getDepartment();
            //调用service
            List maps = examineLogService.selectAppUser(map,level, department);
            //转化为json数据
            String s1 = JSON.toJSONString(maps);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }


    }

    //根据等级查询部门
    public void selectAppDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s2 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s2, User.class);
        int level = user1.getLevel();
        int department = user1.getDepartment();
        //调用service
        List maps = examineLogService.selectAppDepartment(map,level, department);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }
}
