package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Model;
import com.hongbang.service.ModelService;
import com.hongbang.service.impl.ModelServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet( "/model/*")
public class ModelServlet extends BaseServlet {
    //获取service
    ModelService modelService = new ModelServiceImpl();

   public void selectModel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
       List<Model> models = modelService.selectModel();
       //转化为json数据
       String s = JSON.toJSONString(models);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }

    //查询机型是否存在
    public void selectModelExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       request.setCharacterEncoding("utf-8");
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = modelService.selectModelExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }
}
