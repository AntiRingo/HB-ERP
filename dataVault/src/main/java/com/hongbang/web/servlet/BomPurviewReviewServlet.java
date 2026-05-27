package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.ApprovalRequests;
import com.hongbang.pojo.BomPurviewReview;
import com.hongbang.service.BomPurviewReviewService;
import com.hongbang.service.FinBomTitleService;
import com.hongbang.service.impl.BomPurviewReviewServiceImpl;
import com.hongbang.service.impl.FinBomTitleServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/bomPurviewReview/*")
public class BomPurviewReviewServlet extends BaseServlet {

    //查询未审核的数据
  BomPurviewReviewService bomPurviewReviewService = new BomPurviewReviewServiceImpl();
  FinBomTitleService finBomTitleService = new FinBomTitleServiceImpl();
    public void selectNoReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //调用service
        List<ApprovalRequests> approvalRequests = bomPurviewReviewService.selectNoReview();
        //转化为json数据
        String s = JSON.toJSONString(approvalRequests);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //通过审核
    public void updateStatus (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String status = request.getParameter("status");//要修改的状态
        String id = request.getParameter("id");//权限申请单id
        //根据申请单id寻找BOM表TitleId
        BomPurviewReview bomPurviewReview = bomPurviewReviewService.selectById(Integer.parseInt(id));
        int bomId = bomPurviewReview.getBomId();

        //调用service
        bomPurviewReviewService.updateStatus(Integer.parseInt(status),Integer.parseInt(id), bomId);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //    最新的审核作者管理哪些用户的 -------------------------------------------------------------------------------------------------------------------------------------------

    //查询全部的申请单内容
    public void selectAllAuthorAndUserReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<ApprovalRequests> approvalRequests = bomPurviewReviewService.selectAllAuthorAndUserReview();
        //转化为JSON数据
        String s = JSONArray.toJSONString(approvalRequests);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //通过审核
    public void updateStatusAuthorReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String status = request.getParameter("status");//申请单状态
        String appId = request.getParameter("appId");//申请单Id
        //调用service
        bomPurviewReviewService.updateStatusAuthorReview(Integer.parseInt(status), Integer.parseInt(appId));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

}
