package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.BomPurview;
import com.hongbang.pojo.BomPurviewReview;
import com.hongbang.pojo.User;
import com.hongbang.service.BomPurviewService;
import com.hongbang.service.FinBomService;
import com.hongbang.service.FinBomTitleService;
import com.hongbang.service.impl.BomPurviewServiceImpl;
import com.hongbang.service.impl.FinBomServiceImpl;
import com.hongbang.service.impl.FinBomTitleServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/bomPurview/*")
public class BomPurviewServlet extends BaseServlet {
    //获取service
    BomPurviewService bomPurviewService = new BomPurviewServiceImpl();
    FinBomTitleService finBomTitleService = new FinBomTitleServiceImpl();
    FinBomService finBomService = new FinBomServiceImpl();

    public void addPurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        String finBomTitleId = request.getParameter("finBomTitleId");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        BomPurviewReview bomPurviewReview = JSON.parseObject(s1, BomPurviewReview.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<BomPurview> bomPurviews = JSONArray.parseArray(s2, BomPurview.class);
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        bomPurviewReview.settimes(formattedTime);
        //获取登录用户id
        //从session中获取登录用户的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s3 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s3, User.class);
        int id = user1.getId();
        bomPurviewReview.setApplicant(id);

        //调用service

        bomPurviewService.addPurview(bomPurviewReview,bomPurviews, Integer.parseInt(finBomTitleId));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    public void addAuthorUserPurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        String finBomTitleId = request.getParameter("finBomTitleId");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        BomPurviewReview bomPurviewReview = JSON.parseObject(s1, BomPurviewReview.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<BomPurview> bomPurviews = JSONArray.parseArray(s2, BomPurview.class);
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        System.out.println(formattedTime);
        bomPurviewReview.settimes(formattedTime);
        //获取登录用户id
        //从session中获取登录用户的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s3 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s3, User.class);
        int id = user1.getId();
        bomPurviewReview.setApplicant(id);

        //调用service

        bomPurviewService.addPurview(bomPurviewReview,bomPurviews, Integer.parseInt(finBomTitleId));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }



    //查询某个BOM表中某个用户是否有查看权限
    public void  selectHavePurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //获取用户id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();
        //调用service

        //查询是否是作者
        boolean b = finBomTitleService.selectIsAuthor(id, Integer.parseInt(s));
        //查询是否拥有权限
       boolean b1= bomPurviewService.selectHavePurview(Integer.parseInt(s), id);
        //没有权限，查询BOM表是否有内容是空的
        int i = finBomService.totalCount(Integer.parseInt(s));
        //查询是否有作者
        boolean b2 = finBomTitleService.selectIfAuthor(Integer.parseInt(s));
        if (b || b1  || !b2 ){
          response.setContentType("text/json;charset=utf-8");
          response.getWriter().write("success");
      }


    }

    //查询是否有编辑权限
    public void  selectHaveCanEditPurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //获取用户id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();
        //调用service

        //查询是否是作者
        boolean b = finBomTitleService.selectIsAuthor(id, Integer.parseInt(s));
        //查询是否拥有编写权限
        boolean b1 = bomPurviewService.selectHaveCanEditPurview(Integer.parseInt(s), id);
        //没有权限，查询BOM表是否有内容是空的
        int i = finBomService.totalCount(Integer.parseInt(s));
        //查询是否有作者
        boolean b2 = finBomTitleService.selectIfAuthor(Integer.parseInt(s));
        if (b || b1 || !b2) {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }
    }

}
