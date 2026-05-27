package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.mapper.ExamineAllTypeMapper;
import com.hongbang.pojo.ExamineAllType;
import com.hongbang.pojo.ExamineConditionContent;
import com.hongbang.service.ExamineAllTypeService;
import com.hongbang.service.impl.ExamineAllTypeServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/examineAllType/*")
public class ExamineAllTypeServlet extends BaseServlet {
    //获取service
    ExamineAllTypeService examineAllTypeService = new ExamineAllTypeServiceImpl();

    //根据出入库类型查询审核步骤
   public void selectStep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String typeId = bufferedReader.readLine();
       //调用service
       List<ExamineAllType> examineAllTypes = examineAllTypeService.selectStep(Integer.parseInt(typeId));
       //转化为json数据
       String s = JSON.toJSONString(examineAllTypes);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }

    //添加步骤
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        examineAllTypeService.add(s);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success" );
    }

    //删除
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        examineAllTypeService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success" );
    }


    //修改
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
       //调用service
        examineAllTypeService.update(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success" );
    }


    //查询审核步骤中是否已经存在该部门了
    public void selectDepartExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        ExamineAllType examineAllType = JSON.parseObject(s, ExamineAllType.class);
        //调用service
        boolean b = examineAllTypeService.selectDepartExist(examineAllType);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"" );
    }


    //查询审核步骤中是否已经存在该部门了(修改时使用)
    public void selectDepartExistUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        ExamineAllType examineAllType = JSON.parseObject(s, ExamineAllType.class);
        //调用service
        boolean b = examineAllTypeService.selectDepartExistUpdate(examineAllType);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"" );
    }

    //插入
    public void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        examineAllTypeService.insert(s);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success" );
    }
}
