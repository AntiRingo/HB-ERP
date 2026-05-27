package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.MaterialReturnReceive;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;
import com.hongbang.service.MaterialReturnReceiveService;
import com.hongbang.service.impl.MaterialReturnReceiveServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

@WebServlet("/returnReceive/*")
public class MaterialReturnReceiveServlet extends BaseServlet {
    MaterialReturnReceiveService service = new MaterialReturnReceiveServiceImpl();

    public void saveReturnReceive(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String param = bufferedReader.readLine();
        JSONObject json = JSONObject.parseObject(param);

        //封装退货/让步对象
        MaterialReturnReceive receive = new MaterialReturnReceive();
        receive.setApplicationContentId(json.getInteger("applicationContentId"));
        receive.setProductId(json.getInteger("productId"));
        receive.setVault(json.getInteger("vault"));
        receive.setHandleType(json.getInteger("handleType"));
        receive.setReturnQty(json.getBigDecimal("returnQty"));
        receive.setContinuePurchase(json.getInteger("continuePurchase"));
        receive.setHandleRemark(json.getString("handleRemark"));
        receive.setApplicationId(json.getInteger("applicationId"));

        //调用service

        int rows = service.saveReturnReceive(receive);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        System.out.println((rows > 0));
        response.getWriter().write(String.valueOf(rows > 0));
    }

    //查询未分配的信息

    public void  selectNoFp(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        String currentPage = request.getParameter("pageNum");

        int pageSize = 20;
        //调用service
        PageBean<Map<String, Object>> pageBean = service.selectNoFp(Integer.parseInt(currentPage), pageSize);
        //转化为json数据
        String s = JSON.toJSONString(pageBean);
        //相应数据

        response.setContentType("text/json;charset=utf-8");

        response.getWriter().write(s);
    }

    //根据contentId查询申请人都有谁（现在的contentID是质检单）
    public void selectAppUser (HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        String contentId = request.getParameter("contentId");
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");

        //带澳用service
        List<Map<String, Object>> maps = service.selectAppUser(Integer.parseInt(contentId), Integer.parseInt(productId), Integer.parseInt(vault));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //相应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //更新让步接收修改人
    public void updateReceiveUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        String receiveUser = request.getParameter("receive_user");
        String id = request.getParameter("id");
        System.out.println(receiveUser);
        System.out.println(id);
        //调用service
        service.updateReceiveUser(Integer.parseInt(receiveUser),Integer.parseInt(id));
        //相应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }
    //根据product_id,vault查询物料信息
    public void selectProductOne(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端数据
        String id = request.getParameter("id");
        String vault = request.getParameter("vault");
        Product product = service.selectProductOne(Integer.parseInt(id), Integer.parseInt(vault));
        String s = JSON.toJSONString(product);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询质检单的退货以及让步情况
    public void selectReturnReceiveByAppId(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<MaterialReturnReceive> materialReturnReceives = service.selectReturnReceiveByAppId(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(materialReturnReceives);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询所有的退货信息
    public void selectTh(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        String currentPage = request.getParameter("pageNum");

        int pageSize = 20;
        //调用service
        PageBean<Map<String, Object>> pageBean = service.selectTh(Integer.parseInt(currentPage), pageSize);
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //设置退货后是否继续申购物料信息
    public void updateContinue(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //接收前端传来的数据
        String id = request.getParameter("id");
        String continuePurchase = request.getParameter("continuePurchase");
        //调用service
        service.updateContinue(Integer.parseInt(continuePurchase),Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

}
