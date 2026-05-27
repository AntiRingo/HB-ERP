package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.ProductExamine;
import com.hongbang.pojo.User;
import com.hongbang.service.ProductExamineService;
import com.hongbang.service.impl.ProductExamineServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/productExamine/*")
public class ProductExamineServlet extends BaseServlet {
    ProductExamineService productExamineService = new ProductExamineServiceImpl();

//    //添加审核的物料信息
//    public void addProductExamine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //获取前端传来的数据
//    }


    //审核
    public void updateStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        ProductExamine productExamine = JSON.parseObject(s, ProductExamine.class);
        //查询正在登录的人设置审核人
        //查询正在登录的人的信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        String s1 = JSON.toJSONString(user);
        User user1 = JSON.parseObject(s1, User.class);
        productExamine.setExamineId(user1.getId());



        //调用service
        productExamineService.updateStatus(productExamine);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }
    //查询所有审核内容
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<ProductExamine> productExamines = productExamineService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(productExamines);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有数据（带物料号、申请人、审核人）
    public void selectAllData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map<String, Object>> maps = productExamineService.selectAllData();
        //转化为JSON数据
        String s = JSON.toJSONString(maps,SerializerFeature.WriteMapNullValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据物料id查询产品的属性
    public void selectAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String productId = bufferedReader.readLine();
        String vault = request.getParameter("vault");

        //调用service
        List<Map<String, Object>> maps = productExamineService.selectAttribute(Integer.parseInt(productId),Integer.parseInt(vault));
        //转化为JSON数据
        String s = JSON.toJSONString(maps, SerializerFeature.WriteMapNullValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询自己申请的物料信息
    public void selectMyApplications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //查询正在登录的人的信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");

        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int id = user1.getId();
            //调用service
            List<Map<String, Object>> maps = productExamineService.selectMyApplications(id);
            //转化为JSON数据
            String s = JSON.toJSONString(maps, SerializerFeature.WriteMapNullValue);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }
        else {
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }

    }

    //删除
    public void deleteApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();

        //调用service
        productExamineService.deleteApplication(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


}
