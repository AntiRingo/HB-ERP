package com.hongbang.web.servlet;

import com.hongbang.pojo.UserSetting;
import com.hongbang.service.UserSettingService;
import com.hongbang.service.impl.UserSettingServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/userSetting/*")
public class UserSettingServlet extends BaseServlet {

    //调用service
    UserSettingService userSettingService = new UserSettingServiceImpl();
    //获取IP地址
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0]; // 多层代理取第一个IP
    }
    //添加用户配置信息
   public void addSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //接收前端传来的数据
       String sort = request.getParameter("sort");
       String infoHead = request.getParameter("infoHead");
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();

       //获取IP地址
       String ip =getClientIp(request);


       //创建配置类
       UserSetting userSetting = new UserSetting(0,sort,ip,infoHead,s);
       //查询这这个用户的这个类型的配置信息是否存在
       boolean b = userSettingService.selectIfExist(ip, sort, infoHead);

       if (b){
           //存在，执行更新操作
           userSettingService.updateSetting(userSetting);
           //响应成功标识
           response.setContentType("text/json;charset=utf-8");
           response.getWriter().write("success");
       }
       else {
           //不存在执行添加操作
           userSettingService.addSetting(userSetting);
           //响应成功标识
           response.setContentType("text/json;charset=utf-8");
           response.getWriter().write("success");

       }

   }




    //修改用户配置信息
    public void updateSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据

    }

    //读取用户配置
   public void  selectSetting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //结合前端传来的数据

       String sort = request.getParameter("sort");
       String infoHead = request.getParameter("infoHead");


       //获取IP地址
       String ip =getClientIp(request);
       //读取用户配置
       UserSetting userSetting = userSettingService.selectSetting(ip, sort, infoHead);
       //转化为json数据
       if (userSetting!=null){
           //响应数据
           response.setContentType("text/json;charset=utf-8");
           response.getWriter().write(String.valueOf(userSetting.getInfo()));
       }



    }
}
