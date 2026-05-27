package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.MaterialReturnReplace;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.User;
import com.hongbang.service.MaterialReturnReplaceService;
import com.hongbang.service.impl.MaterialReturnReplaceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/materialReturnReplace/*")
public class MaterialReturnReplaceServlet extends BaseServlet {

    MaterialReturnReplaceService materialReturnReplaceService = new MaterialReturnReplaceServiceImpl();

    //查询需要自己处理的让步接收信息

    public void  selectRangBuByUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //查询正在登陆的用户
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if(user!=null){
            String s3 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s3, User.class);
            //接收前端传来的数据
            String currentPage = request.getParameter("pageNum");

            int pageSize = 20;
            //调用service
            PageBean<Map<String, Object>> pageBean = materialReturnReplaceService.selectRangBuByUser(user1.getId(), Integer.parseInt(currentPage), pageSize);

            //转化为json数据
            String s = JSON.toJSONString(pageBean);
            //相应数据

            response.setContentType("text/json;charset=utf-8");

            response.getWriter().write(s);
        }

    }

    //插入让步接收修改信息
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 1. 设置编码（必须加，否则中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        // 2. 读取完整请求体（不是只读一行）
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();

        // 3. 解析JSON
        MaterialReturnReplace materialReturnReplace = JSON.parseObject(json, MaterialReturnReplace.class);


        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);



        // 4. 执行业务
        materialReturnReplaceService.add(materialReturnReplace,user);

        // 5. 返回成功
        response.getWriter().write("success");
    }


    public void getByReceiveId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 设置编码与响应类型
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        // 获取前端传递的让步单id
        String receiveIdStr = request.getParameter("receiveId");
        MaterialReturnReplace record = null;
        try {
            Integer receiveId = Integer.parseInt(receiveIdStr);
            // 调用service查询更换记录表
            record = materialReturnReplaceService.getByReceiveId(receiveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查到数据返回实体，没查到返回null，前端直接判断即可
        response.getWriter().write(JSON.toJSONString(record));
    }

}
