package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.TakeOrder;
import com.hongbang.pojo.User;
import com.hongbang.service.TakeOrderService;
import com.hongbang.service.UserFunctionService;
import com.hongbang.service.impl.TakeOrderServiceImpl;
import com.hongbang.service.impl.UserFunctionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/takeOrder/*")
public class TakeOrderServlet extends BaseServlet {
        TakeOrderService takeOrderService = new TakeOrderServiceImpl();
       UserFunctionService userFunctionService = new UserFunctionServiceImpl();
    //接单
    public void updateTakeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        TakeOrder takeOrder = JSON.parseObject(s, TakeOrder.class);
        //调用service
        takeOrderService.updateTakeOrder(takeOrder);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //检查是否已经被接单了,返回true就是未接单
    public void selectIfJd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        boolean b = takeOrderService.selectIfJd(Integer.parseInt(appId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");

    }

    //部长查看所有的接单情况
    public void selectAllTakeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String currentPage = request.getParameter("currentpage");
        int page = 15;
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);

//            //查询出入库权限
//            //查询出库入库权限
            boolean rk = userFunctionService.selectRkQx(user1.getId());
            boolean ck = userFunctionService.selectCkQx(user1.getId());
            //查询采购权限
            boolean cg = userFunctionService.selectCgQx(user1.getId());
            //查询质检权限
            boolean zj = userFunctionService.selectZjQx(user1.getId());
            //查询订购权限
            boolean dg = userFunctionService.selectDgQx(user1.getId());


            //调用service
            PageBean<Map<String, Object>> pageBean = takeOrderService.selectAllTakeOrder(Integer.parseInt(currentPage), page,ck,rk,cg,zj,dg);
            //转化为json数据
            String s = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }

    }
}
