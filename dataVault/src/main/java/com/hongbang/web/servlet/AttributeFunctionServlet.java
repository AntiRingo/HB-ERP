package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.AttributeFunction;
import com.hongbang.service.AttributeFunctionService;
import com.hongbang.service.impl.AttributeFunctionServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/attributeFunction/*")
public class AttributeFunctionServlet extends BaseServlet {
 //service
    AttributeFunctionService attributeFunctionService = new AttributeFunctionServiceImpl();

    //根据attributeNameId/属性名id查询当前属性的编码设置
    public void selectByAttNameId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeFunction> attributeFunctions = attributeFunctionService.selectByAttNameId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeFunctions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //新增配置
   public void  add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();
       //JSON序列化
       AttributeFunction attributeFunction = JSON.parseObject(s, AttributeFunction.class);
       //调用service
       attributeFunctionService.add(attributeFunction);
       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }

    //修改
   public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();

       //JSON序列化
       AttributeFunction attributeFunction = JSON.parseObject(s, AttributeFunction.class);

       //调用service
       attributeFunctionService.update(attributeFunction);
       //响应成功标识
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write("success");
   }



    //查看是值的并且已经开启值为编码的功能的属性
    public void selectAutoCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map<String, Object>> maps = attributeFunctionService.selectAutoCode();
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查看当前分类下是值的并且已经开启值为编码的功能的属性
    public void selectAutoCodeBySortId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = attributeFunctionService.selectAutoCodeBySort(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查看是否已经开启值作为编码的功能
    public void selectIfAutoCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeFunctionService.selectIfAutoCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //查看是否已经开启顺序编码的功能
    public void selectIfOrderCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeFunctionService.selectIfOrderCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询当前映射下的所有属性值作为编码的开启关闭情况
    public void  selectAllAttributeFunction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeFunction> attributeFunctions = attributeFunctionService.selectAllAttributeFunction(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeFunctions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询是否有设置这个功能，没设置的话执行添加操作
    public void  selectIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeFunctionService.selectIfExist(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

}
