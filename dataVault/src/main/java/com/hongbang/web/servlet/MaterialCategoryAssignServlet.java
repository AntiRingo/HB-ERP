package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Sort;
import com.hongbang.service.MaterialCategoryAssignService;
import com.hongbang.service.impl.MaterialCategoryAssignServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/materialCategoryAssign/*")
public class MaterialCategoryAssignServlet extends BaseServlet {
    //获取service
    MaterialCategoryAssignService materialCategoryAssignService = new MaterialCategoryAssignServiceImpl();

    // 1. 查询分类下已分配的用户ID
    public void listAssignedUserIdsByCategoryId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        String id = request.getParameter("id");

        //调用service
        List<Long> longs = materialCategoryAssignService.listAssignedUserIdsByCategoryId(id);
        //转化为json数据
        String s = JSON.toJSONString(longs);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    // 2. 删除并添加该分类的所有分配
    public void deleteByCategoryId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据：分类id
        String categoryId = request.getParameter("categoryId");
        //接收前端传来的用户id数组（字符串格式：18,20,21）
        String userIdsStr = request.getParameter("userIds");

        //将字符串转为 List<Long>
        List<Long> userIds = null;
        if (userIdsStr != null && !userIdsStr.isEmpty()) {
            userIds = Arrays.stream(userIdsStr.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }

        //调用service（先删后插）
        materialCategoryAssignService.deleteByCategoryId(categoryId, userIds);

        //响应成功
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    // 一次性获取所有分配关系
    public void getAllAssignMap(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, List<Long>> map = materialCategoryAssignService.getAllAssignMap();
        String json = JSON.toJSONString(map);
        response.getWriter().write(json);
    }




    // 4. 查询所有已分配的分类ID
    public void   listAssignedCategoryIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //调用service
        List<String> strings = materialCategoryAssignService.listAssignedCategoryIds();
        //转化为json数据
        String s = JSON.toJSONString(strings);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询所有存在物料信息的分类
    public void  selectAllSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Sort> sorts = materialCategoryAssignService.selectAllSort();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }
}
