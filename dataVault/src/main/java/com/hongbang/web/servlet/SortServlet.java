package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.Sort;
import com.hongbang.service.SortService;
import com.hongbang.service.impl.SortServiceImpl;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/sort/*")
public class SortServlet extends BaseServlet {
    SortService sortService = new SortServiceImpl();


    //获取一级分类
    public void selectOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<Sort> sorts = sortService.selectOneLevel();
        //转化为json数据
        String s = JSON.toJSONString(sorts, SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //获取其他级分类
    public void selectOtherLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //获取前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        if (parentId!=null){
            List<Sort> sorts = sortService.selectOtherLevel(Integer.parseInt(parentId));
            //转化为json数据
            String s = JSON.toJSONString(sorts,SerializerFeature.WriteNullStringAsEmpty);

            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }


    }


    //返回上一级分类
    public void selectLastLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = sortService.selectLastLevel(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(sorts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询列表中的所有数据
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectAll();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //添加分类
    public void addSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        Sort sort = JSON.parseObject(s, Sort.class);
        //调用service
        sortService.addSort(sort);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //查询该分类中是否有数据
    public void selectExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        boolean b = sortService.selectExist(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //修改分类
    public void updateSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        Sort sort = JSON.parseObject(s, Sort.class);
        //调用service
        sortService.updateSort(sort);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    public void selectLast(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map> sorts = sortService.selectLast();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询该数据属于那个分类,传的是该数据的parentId
    public void selectOfSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的parentId
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = sortService.selectOfSort(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id查询该分类的数据
    public void selectSortById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = sortService.selectSortById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(sorts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据parentId查询level
    public void  selectLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的parentId
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();

        //调用service
        int i = sortService.selectLevel(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(i);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //删除分类
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        sortService.deleteById(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询编码是否存在,更新时
    public void ifCodeExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的code

        request.setCharacterEncoding("utf8");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String id = request.getParameter("id");
        String parentId = request.getParameter("parentId");
        //调用service
        boolean b = sortService.ifCodeExist(code, Integer.parseInt(level),Integer.parseInt(id), Integer.parseInt(parentId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //一级分类修改的时候验证编码是否重复，查询零件库以及产品库两个库
    public void ifCodeExistFromTwoTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的code

        request.setCharacterEncoding("utf8");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String id = request.getParameter("id");
        String parentId = request.getParameter("parentId");
        //调用service
        boolean b = sortService.ifCodeExistFromTwoTable(code, Integer.parseInt(level),Integer.parseInt(id), Integer.parseInt(parentId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询编码是否存在，添加时
    public void ifCodeExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的code

        request.setCharacterEncoding("utf8");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String parentId = request.getParameter("parentId");

        //调用service
        boolean b = sortService.ifCodeExistAdd(code, Integer.parseInt(level), Integer.parseInt(parentId));

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //一级分类添加的是否验证编码是否重复，查询零件库以及产品库两个库
   public void  ifCodeExistAddFromTwoTable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的code

       request.setCharacterEncoding("utf8");
       String code = request.getParameter("code");
       String level = request.getParameter("level");
       String parentId = request.getParameter("parentId");

       //调用service
       boolean b = sortService.ifCodeExistAddFromTwoTable(code, Integer.parseInt(level), Integer.parseInt(parentId));

       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(String.valueOf(b));
   }

    //添加分类信息的时候验证分类名是否重复
    public void selectNameAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的分类名
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String name = bufferedReader.readLine();

        //调用service
        boolean b = sortService.selectNameAdd(name);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //更新分类信息时验证分类名是否重复
    public void  selectNameUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的名称和id
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String name = bufferedReader.readLine();
        String id = request.getParameter("id");

        //调用service
        boolean b = sortService.selectNameUpdate(name, Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    public void getData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Sort> data = sortService.getData(Integer.parseInt(s));
        String s1 = JSON.toJSONString(data);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询所有子集
    public void selectAllDown(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = sortService.selectAllDown(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该level级下的所有分类信息
    public void selectByLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的level
        BufferedReader bufferedReader = request.getReader();
        String level = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = sortService.selectByLevel(Integer.parseInt(level));
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //循环更新编码
    public void updateCodes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        List<Sort> sorts = JSONArray.parseArray(s, Sort.class);
        //调用service
        sortService.updateCodes(sorts);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述

    public  void updateSortExceptCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        Sort sort = JSON.parseObject(s, Sort.class);
        //调用service
        sortService.updateSortExceptCode(sort);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //检查是否有id为0的数据
    public void select0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        boolean b = sortService.select0();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    public  void update0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        sortService.update0();
        //响应成功表示
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询id为0的分类，也就是公共属性使用的单独分类
    public void selectId0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectId0();
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


public void  addSortFourExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf8");
    BufferedReader bufferedReader = request.getReader();
    String s = bufferedReader.readLine();
    List<Sort> sorts = JSONArray.parseArray(s, Sort.class);
    sortService.addSortFourExcel(sorts);

}


    //--------------------------------------------------------------------------------------------------------------------------------------------------
    //下面是数量页面用到的

    //查询有物料的分类
    public void selectHaveProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectHaveProduct();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询所有二级分类
    public void selectTwoLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectTwoLevel();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询分类下的物料数量
    public void selectNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        int i = sortService.selectNumber(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //查询所有的一级分类
    public void selectOne (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectOne();
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有的四级分类
    public void selectAllLevelFour(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Sort> sorts = sortService.selectAllLevelFour();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据产品信息查询分类
    public void selectByProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");

        List<Sort> sorts = sortService.selectByProduct(Integer.parseInt(productId), Integer.parseInt(vault));
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }
}
