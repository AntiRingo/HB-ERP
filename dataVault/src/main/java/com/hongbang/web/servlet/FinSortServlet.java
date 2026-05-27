package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.FinMapping;
import com.hongbang.pojo.FinSort;
import com.hongbang.pojo.Mapping;
import com.hongbang.pojo.Sort;
import com.hongbang.service.FinSortService;
import com.hongbang.service.impl.FinSortServiceImpl;
import com.hongbang.service.impl.SortServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/finSort/*")
public class FinSortServlet extends BaseServlet {

    FinSortService finSortService = new FinSortServiceImpl();
    //获取其他级分类
   public void selectOtherLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //获取前端传来的id
       BufferedReader bufferedReader = request.getReader();
       String parentId = bufferedReader.readLine();
       //调用service
       List<FinSort> finSorts = finSortService.selectOtherLevel(Integer.parseInt(parentId));

       //转化为json数据
       String s = JSON.toJSONString(finSorts, SerializerFeature.WriteNullStringAsEmpty);

       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
    }

    //查询该数据属于那个分类,传的是该数据的parentId
    public void selectOfSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的parentId
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinSort> finSorts = finSortService.selectOfSort(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(finSorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //返回上一级分类
    public void selectLastLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前段传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinSort> finSorts = finSortService.selectLastLevel(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(finSorts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //获取一级分类
    public void selectOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service
        List<FinSort> finSorts = finSortService.selectOneLevel();
        //转化为json数据
        String s = JSON.toJSONString(finSorts, SerializerFeature.WriteNullStringAsEmpty);
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
        int i = finSortService.selectLevel(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(i);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //添加分类信息的时候验证分类名是否重复
    public void selectNameAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的分类名
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String name = bufferedReader.readLine();

        //调用service
        boolean b = finSortService.selectNameAdd(name);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询编码是否存在
    public void ifCodeExistAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的code

        request.setCharacterEncoding("utf8");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String parentId = request.getParameter("parentId");

        //调用service
        boolean b = finSortService.ifCodeExistAdd(code, Integer.parseInt(level), Integer.parseInt(parentId));

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //添加分类
    public void addSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //json数据序列化
        FinSort finSort = JSON.parseObject(s, FinSort.class);

        //调用service
        finSortService.addSort(finSort);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //删除分类
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finSortService.deleteById(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //根据id查询该分类的数据
    public void selectSortById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinSort> finSorts = finSortService.selectSortById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(finSorts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有子集
    public void selectAllDown(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finSortService.selectAllDown(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //更新分类信息时验证分类名是否重复
    public void  selectNameUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的名称和id
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String finSortName = bufferedReader.readLine();
        String id = request.getParameter("id");

        //调用service
        boolean b = finSortService.selectNameUpdate(finSortName, Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查询编码是否存在
    public void ifCodeExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的code

        request.setCharacterEncoding("utf8");
        String finSortCode = request.getParameter("code");
        String finSortLevel = request.getParameter("level");
        String id = request.getParameter("id");
        String parentId = request.getParameter("parentId");
        //调用service
        boolean b = finSortService.ifCodeExist(finSortCode, Integer.parseInt(finSortLevel), Integer.parseInt(id), Integer.parseInt(parentId));
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
        FinSort finSort = JSON.parseObject(s, FinSort.class);
        //调用service
        finSortService.updateSort(finSort);
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
        FinSort finSort = JSON.parseObject(s, FinSort.class);
        //调用service
        finSortService.updateSortExceptCode(finSort);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查询该level级下的所有分类信息
    public void selectByLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的level
        BufferedReader bufferedReader = request.getReader();
        String level = bufferedReader.readLine();
        //调用service
        List<FinSort> finSorts = finSortService.selectByLevel(Integer.parseInt(level));
        //转化为JSON数据
        String s = JSON.toJSONString(finSorts);
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
        List<FinSort> finSorts = JSONArray.parseArray(s, FinSort.class);
        //调用service
        finSortService.updateCodes(finSorts);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //检查是否有id为0的数据
    public void select0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        boolean b = finSortService.select0();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    public  void update0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        finSortService.update0();
        //响应成功表示
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询id为0的分类，也就是公共属性使用的单独分类
    public void selectId0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinSort> finSorts = finSortService.selectId0();
        //转化为JSON数据
        String s = JSON.toJSONString(finSorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询该分类中是否有数据
    public void selectExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        boolean b = finSortService.selectExist(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询列表中的所有数据
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinSort> finSorts = finSortService.selectAll();
        //转化为json数据
        String s = JSON.toJSONString(finSorts);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    public void selectLast(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map> sorts = finSortService.selectLast();
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有的一级分类
    public void selectOne (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinSort> finSorts = finSortService.selectOne();
        //转化为JSON数据
        String s = JSON.toJSONString(finSorts);
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
        int i = finSortService.selectNumber(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //  显示页面要用  -----------------------------------------------------------------------------------------------------------------------
    //查询所有的一级分类（list<sort>是对的）

    public void selectOneDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //调用service
        List<Sort> sorts = finSortService.selectOneDisplay();
        //转化为json数据
        String s = JSON.toJSONString(sorts);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //获取其他级分类
    public void selectOtherLevelDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = finSortService.selectOtherLevelDisplay(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id查询该分类信息
    public void selectSortByIdDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = finSortService.selectSortByIdDisplay(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(sorts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询该数据属于那个分类,传的是该数据的parentId
    public void selectOfSortDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Sort> sorts = finSortService.selectOfSortDisplay(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(sorts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询该分类下是否有物料
    public void selectIfProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        boolean b = finSortService.selectIfProduct(Integer.parseInt(parentId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }
}
