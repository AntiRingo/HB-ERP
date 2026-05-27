package com.hongbang.web.servlet;

import com.hongbang.service.UserService;
import com.hongbang.service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GreyStartServlet extends HttpServlet {
    // Servlet的init方法会在Tomcat启动的时候执行
    @Override
    public void init() throws ServletException {
        UserService service = new UserServiceImpl();
        // 需要实现的功能
        // 创建定时器
        Timer timer = new Timer();
        // 创建定时器任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
              service.heart();
            }
        };

        timer.scheduleAtFixedRate(task, new Date(), 3600000); // 每一小时执行一次
    }
}
