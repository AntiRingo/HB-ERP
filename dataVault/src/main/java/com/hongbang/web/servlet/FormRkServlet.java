package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Product;
import com.hongbang.service.FormRkService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.FormRkServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/formRk/*")
public class FormRkServlet extends BaseServlet {
   //获取service
    FormRkService formRkService = new FormRkServiceImpl();
    ProductService productService = new ProductServiceImpl();
    //根据时间查询出库信息
    public void selectCkFormListBy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受前端数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        //调用service
        List list = formRkService.selectRkFormListBy(map);
        //转化为json数据
        String s1 = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询最新一批的申请信息
    public void selectNewApp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appFormId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = formRkService.selectNewApp(Integer.parseInt(appFormId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //结存
    public void jieCun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        //调用service，查询所有物料信息
        List<Product> products = productService.selectJieCun();
        //根据物料id去查在这一段时间内的价格，如果这一时间段内没有，就去查这个时间段之前的最新的那一条


    }
}
