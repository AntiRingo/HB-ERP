package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.AttributeValueService;
import com.hongbang.service.impl.AttributeValueServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/attributeValue/*")
public class AttributeValueServlet extends BaseServlet {
//获取service
    AttributeValueService attributeValueService = new AttributeValueServiceImpl();

    //添加
   public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();

       //JSON数据序列化
       AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);

       //调用service
       attributeValueService.add(attributeValue);
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
        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
        //调用service
        attributeValueService.update(attributeValue);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //删除
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        attributeValueService.delete(Integer.parseInt(s));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeValue> attributeValues = attributeValueService.selectById(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查看当前属性名下面都有什么属性值

    public void  selectByAttNameId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
//        System.out.println(s);
        //调用service
        List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(Integer.parseInt(s));
//        System.out.println(attributeValues);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //判断属性值是否重复
   public void  selectValueExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前段传来的数据
       BufferedReader bufferedReader = request.getReader();
       String s = bufferedReader.readLine();
       //JSON序列化
       AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
       //调用service
       boolean b = attributeValueService.selectValueExist(attributeValue);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(String.valueOf(b));
   }

    //判断编码值是否重复
    public void  selectCodeExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
        //调用service
        boolean b = attributeValueService.selectCodeExist(attributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //判断属性值是否重复
    public void  selectValueExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //JSON序列化
        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);

        //调用service
        boolean b = attributeValueService.selectValueExistAdd(attributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //判断编码值是否重复
    public void  selectCodeExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
        //调用service
        boolean b = attributeValueService.selectCodeExistAdd(attributeValue);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //输入联想
    public void inputLX(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String attNameId = request.getParameter("attNameId");

        //调用service
        List<Map<String, Object>> maps = attributeValueService.inputLX(str, Integer.parseInt(attNameId));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //根据nameId,attValue去查询编码
    public  void selectCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);

        //调用service
        List<AttributeValue> attributeValues = attributeValueService.selectCode(attributeValue);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据属性ID去查询该属性下是否有编码表存在
    public void  selectIfCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeValueService.selectIfCode(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //循环加入编码，属性值作为编码的功能
    public void addCodeAuto (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的信息
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<AttributeValue> attributeValues = JSONArray.parseArray(s, AttributeValue.class);
        //调用service
        attributeValueService.addCodeAuto(attributeValues);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //分页查询
    public void   selectAttributeValueLimit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        String currentPage = request.getParameter("currentPage");


        int pageSize=200;
        //调用service
        PageBean<AttributeValue> pageBean = attributeValueService.selectAttributeValueLimit(Integer.parseInt(s1), Integer.parseInt(currentPage), pageSize);
        //转化为json数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }



    //模糊查询
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String currentPage = request.getParameter("currentPage");
        int size = 50;
        //调用service
        PageBean<AttributeValue> search = attributeValueService.search(str, Integer.parseInt(currentPage), size);
        //转化为json数据
        String s = JSON.toJSONString(search);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);


    }

     public void select1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
         List<Map<String, Object>> maps = attributeValueService.select1();

         String s = JSON.toJSONString(maps);
         response.setContentType("text/json;charset=utf-8");
         response.getWriter().write(s);
     }

    public void select2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<AttributeValue> maps = attributeValueService.select2();

        String s = JSON.toJSONString(maps);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }




    //查询所有数据
    public void  selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //调用service
        List<AttributeValue> attributeValues = attributeValueService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(attributeValues);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }
}
