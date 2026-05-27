package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.service.FormCkService;
import com.hongbang.service.impl.FormCkServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/formCk/*")
public class FormCkServlet extends BaseServlet {
   //获取service
    FormCkService formCkService = new FormCkServiceImpl();

    //根据时间查询出库信息
    public void selectCkFormListBy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受前端数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        //调用service
        List list = formCkService.selectCkFormListBy(map);
        //转化为json数据
        String s1 = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }
}
