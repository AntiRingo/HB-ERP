package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.*;
import com.hongbang.service.FinBomService;
import com.hongbang.service.FinBomTitleService;
import com.hongbang.service.FinProductService;
import com.hongbang.service.impl.FinBomServiceImpl;
import com.hongbang.service.impl.FinBomTitleServiceImpl;
import com.hongbang.service.impl.FinProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/finBom/*")
public class FinBomServlet extends BaseServlet {
    FinBomService finBomService = new FinBomServiceImpl();
    FinProductService finProductService = new FinProductServiceImpl();
    FinBomTitleService finBomTitleService = new FinBomTitleServiceImpl();


    //分页查询BOM表
    public void selectByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
//
//        String currentPage = request.getParameter("currentPage");
//
//
//        int pageSize=25;
        //调用service
        PageBean<Map<String,Object>> pageBean = finBomService.selectByPage(Integer.parseInt(s));

        //转化为JSON数据
        String s1 = JSON.toJSONString(pageBean,SerializerFeature.WriteMapNullValue);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //添加BOM表信息
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String finId = request.getParameter("finId");


        //JSON数据序列化
        List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);

        finBomService.add(finBoms,Integer.parseInt(finId));
        boolean b1 = finBomTitleService.selectIfAuthor(Integer.parseInt(finId));
        if (!b1){
            //查询当前登录的用户信息
            HttpSession session = request.getSession();
            Object username = session.getAttribute("username");
            String s2 = JSON.toJSONString(username);
            User user1 = JSON.parseObject(s2, User.class);
            int id = user1.getId();
            finBomTitleService.updateAuthor(id, Integer.parseInt(finId));
        }



        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");


    }

    //添加BOM表信息
    public void addOther(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        FinBomTitle finBomTitle = JSON.parseObject(s1, FinBomTitle.class);
        //查询当前登录的用户信息
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();
        finBomTitle.setAuthor(id);
        //设置作者
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s3 = JSONArray.toJSONString(jsonArray1);
        List<FinBom> finBoms = JSONArray.parseArray(s3, FinBom.class);
        finBomService.addOther(finBomTitle,finBoms);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");


    }

    //查询当前BOM表下的所有产品信息，只要产品库的
    public void  selectBomVault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinBom> finBoms = finBomService.selectBomVault(Integer.parseInt(s));

        //转化为JSON数据
        String s1 = JSON.toJSONString(finBoms);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询某个物料是否在BOM表中被使用
    public void  ifUsed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String vault = request.getParameter("vault");
        //调用service
        boolean b = finBomService.ifUsed(Integer.parseInt(s), Integer.parseInt(vault));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }


    //查询当前BOM表下的所有产品信息，只要产品库的
    public void  selectBomVaultAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinBom> finBoms = finBomService.selectBomVaultAll(Integer.parseInt(s));

        //转化为JSON数据
        String s1 = JSON.toJSONString(finBoms);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询当前BOM表下的内容
    public void selectBomContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        //调用service
        List<FinBom> finBoms = finBomService.selectBomContent(Integer.parseInt(finProductId));
        //转化为JSON数据
        String s = JSON.toJSONString(finBoms);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询BOM表中物料信息的属性信息
    public void selectAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finBomService.selectAttribute(Integer.parseInt(finProductId));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询物料信息
    public void selectProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);
        //调用service
        List<Product> products = finBomService.selectProduct(finBoms);
        //转化为json数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询BOM表的价格和数量（零件）
    public void  selectPriceLj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        //调用service
        int i = finBomService.selectPriceLj(Integer.parseInt(finProductId));

        //转化为josn数据
        String s = JSON.toJSONString(i);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询该BOM表中的产品信息
    public void selectFinFromBom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        //调用service
        List<Map<String,Object>> list = finBomService.selectFinFromBom(Integer.parseInt(finProductId));
        //转化为json数据
        String s = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询BOM表中的物料信息
    public void selectProductById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String bomTitleId = request.getParameter("bomTitleId");
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");
        //调用service
        FinBom finBom = finBomService.selectProductById(Integer.parseInt(bomTitleId), Integer.parseInt(productId), Integer.parseInt(vault));
        //转化为json数据
        String s = JSON.toJSONString(finBom, SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

}
