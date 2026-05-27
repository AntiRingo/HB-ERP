package com.hongbang.filter;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.User;
import com.hongbang.pojo.UserSession;
import com.hongbang.web.servlet.UserServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(urlPatterns = {"/department.html","/index.html","/product.html","/Purchase.html","/sort.html","/sortAdd.html","/sortUpdate.html","/applicationForm.html"
,"/attributeAdd.html","/attributeValue.html","/basicEncoding.html","/fileImport.html","/level.html","/personalManage.html","/personalPasswordManage.html",
"/publicApplication.html","/userManage.html","/addbom.html"})
public class UserLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;


        //1、判断session中是否有user
        HttpSession session = req.getSession();
        if (session !=null){
            Object username = session.getAttribute("username");

            if (username!=null){
                Map<String, UserSession> sessionMap = UserServlet.sessionMap;

                UserSession storedSession = sessionMap.get(username);
                // 检测Session ID是否匹配
                if (storedSession != null && !storedSession.getSessionId().equals(session.getId())) {
                    System.out.println("异地登录");

                    return;
                }
                //放行
                filterChain.doFilter(request,response);

            }
            else {

                response.getWriter().write("<script type=\"text/javascript\">window.top.location=\"userlogin.html\";</script>");
            }


        }
        else {

            response.getWriter().write("<script type=\"text/javascript\">window.top.location=\"userlogin.html\";</script>");
        }


//        String s = JSON.toJSONString(user);
//        User user1 = JSON.parseObject(s, User.class);
//        //2、判断user是否为null,如果不为null说明用户登录过了
//        ServletContext application=session.getServletContext();
//
//        Map<String, String> loginMap = (Map<String, String>)application.getAttribute("loginMap");
//        if(loginMap==null){
//            loginMap = new HashMap<>();
//            response.getWriter().write("<script type=\"text/javascript\">window.top.location=\"userlogin.html\";</script>");
//        }
//        for(String key:loginMap.keySet()) {
//            if (user1.getUserName().equals(key)){
//                //放行
//                filterChain.doFilter(request,response);
//
//            }else {
//                response.getWriter().write("<script type=\"text/javascript\">window.top.location=\"userlogin.html\";</script>");
//
//            }
//        }
//        if (user!=null){
//            //放行
//            filterChain.doFilter(request,response);
//
//        }else {
//
//            response.getWriter().write("<script type=\"text/javascript\">window.top.location=\"userlogin.html\";</script>");
//
//        }

    }


    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {

    }


}
