package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.OutboundType;
import com.hongbang.service.OutBoundTypeService;
import com.hongbang.service.impl.OutBoundTypeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/outBoundType/*")
public class OutBoundTypeServlet extends BaseServlet {
    //获取service
    OutBoundTypeService outBoundTypeService = new OutBoundTypeServiceImpl();

    //查询出库类型
    public void selectOutBound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectOutBound();
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询入库类型
    public void selectInBound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectInBound();
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有类型
    public void selectAllType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectAllType();
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }



    //根据id查询
    public void selectById (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectById(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询采购类型
    public void selectCgBound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectCgBound();
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询质检类型
    public void  selectZjBound(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<OutboundType> outboundTypes = outBoundTypeService.selectZjBound();
        //转化为json数据
        String s = JSON.toJSONString(outboundTypes);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


}
