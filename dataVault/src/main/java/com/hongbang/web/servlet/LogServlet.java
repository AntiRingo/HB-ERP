package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.LogService;
import com.hongbang.service.impl.LogServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/log/*")
public class LogServlet extends BaseServlet {
    LogService logService = new LogServiceImpl();

    //入库日志标题查询
    public void storageRecordTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();

        int pageSize=50;
        //调用service
        PageBean<Map<String, Object>> pageBean = logService.storageRecordTitle(Integer.parseInt(s1),pageSize);
        //转化为json数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //出库日志标题查询
    public  void outboundRecordTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        int pageSize=50;
        //调用service
        PageBean<Map<String, Object>> pageBean = logService.outboundRecordTitle(Integer.parseInt(s1), pageSize);
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询该物料是否存在出入库记录
    public void selectIfLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String productId = bufferedReader.readLine();
        String vault = request.getParameter("vault");
        //调用service
        boolean b = logService.selectIfLog(Integer.parseInt(productId), Integer.parseInt(vault));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");


    }

    //查询操作人(出库)
    public void selectManagerCK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map<String, Object>> maps = logService.selectManagerCK();
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询操作人(入库)
    public void selectManagerRK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map<String, Object>> maps = logService.selectManagerRK();
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //日志筛选
    public void screen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        String currentPage = request.getParameter("currentPage");
        int size = 50;
        //调用service
        PageBean screen = logService.screen(map, Integer.parseInt(currentPage), size);
        //转化为josn数据
        String s1 = JSON.toJSONString(screen);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //日志模糊查询（根据物料号和订单号）
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String sort = request.getParameter("sort");
        String currentPage = request.getParameter("currentPage");
        int pageSize=50;
        //调用service
        PageBean<Map<String, Object>> search = logService.search(str, Integer.parseInt(sort), Integer.parseInt(currentPage), pageSize);
        //转化为json数据
        String s = JSON.toJSONString(search);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //在一段时间内统计已经存在的物料出库还是入库的总量
    public void  Statistics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        String currentPage = request.getParameter("currentPage");
        int size = 50;
        //调用service
        PageBean statistics = logService.Statistics(map, Integer.parseInt(currentPage), size);
        //转化为JSON数据
        String s1 = JSON.toJSONString(statistics);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询某个申请单中是否存在未签字的日志(让申请人签字)
    public void selectWqz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appFormId = bufferedReader.readLine();
        //调用service
        boolean b = logService.selectWqz(Integer.parseInt(appFormId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }

    //申请人签字
    public void updateQz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
      //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String url = bufferedReader.readLine();
        String appFormId = request.getParameter("appFormId");

        //调用service
        logService.updateQz(url, Integer.parseInt(appFormId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询该申请单需要签字的产品内容
    public void selectQzProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appFormId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = logService.selectQzProduct(Integer.parseInt(appFormId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询一个申请人是否存在未签字的申请单
    public void selectIfQzByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        boolean b = logService.selectIfQzByUserId(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


}
