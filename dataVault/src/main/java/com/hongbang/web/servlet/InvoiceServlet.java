package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.FormRk;
import com.hongbang.pojo.Invoice;
import com.hongbang.service.InvoiceService;
import com.hongbang.service.impl.InvoiceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet("/invoice/*")
public class InvoiceServlet extends BaseServlet {
    //获取service
    InvoiceService service = new InvoiceServiceImpl();

    //根据appId查询发票信息
    public void selectFp (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Invoice> invoices = service.selectFp(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(invoices);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //插入多张发票信息
    public void insertAllInvoice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        JSONArray jsonArray = jsonArrays.get(0);
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s0 = jsonArray.toJSONString();
        String s1 = jsonArray1.toJSONString();
        List<FormRk> formRkList = JSONArray.parseArray(s0, FormRk.class);
        List<Invoice> invoices = JSONArray.parseArray(s1, Invoice.class);
        //调用service
        service.insertAllInvoice(formRkList,invoices);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询未审核发票的申请单信息
    public void selectWsFp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.getParameter("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        //调用service
        List list = service.selectWsFp(map);
        //转化为json数据
        String s1 = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+s1+"");
    }

    //通过审核发票信息
    public void updatePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        //调用service
        service.updatePass(Integer.parseInt(appId),formattedTime);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //不通过发票信息
    public void updateNoPass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        //调用service
        service.updateNoPass(Integer.parseInt(appId),formattedTime);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

}
