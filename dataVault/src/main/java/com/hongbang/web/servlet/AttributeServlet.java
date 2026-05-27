package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.hongbang.pojo.*;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/attribute/*")
public class AttributeServlet extends BaseServlet {

    AttributeService attributeService = new AttributeServiceImpl();
    AttributeFunctionService attributeFunctionService = new AttributeFunctionServiceImpl();
    ProductService productService = new ProductServiceImpl();
    SortService sortService = new SortServiceImpl();
    AttributeValueService attributeValueService = new AttributeValueServiceImpl();


    //查询所有属性信息
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<AttributeName> attributeNames = attributeService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }



    //添加新的属性(在最后一层直接添加，即该分类的后面就是物料)
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        attributeService.add(attributeName);
        //获取刚创建的属性id，拿到这个id后作为content的parentId
        int id = attributeName.getId();

        //根据添加后的属性id获取该属性所在分类下的物料id；即content的productid
        List<Map<String, Object>> maps = attributeService.selectProductIdByParentId(id);
        if (maps.size()>0){
            String s1 = JSON.toJSONString(maps);

            List<AttributeContent> attributeContents = JSONArray.parseArray(s1, AttributeContent.class);
            for (AttributeContent attributeContent : attributeContents) {
                attributeContent.setParentId(id);
                attributeContent.setProductId(attributeContent.getId());
            }

            //调用service
            attributeService.addContentAfterName(attributeContents);

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");

        }else {
            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }

    }

    //在其他层添加(即，该分类的下面还有分类)
    public void addTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       request.setCharacterEncoding("utf8");

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收uniqueCode
        String uniqueCode = request.getParameter("uniqueCode");
        String displayCode = request.getParameter("displayCode");

        //json数据序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        attributeService.add(attributeName);
        //获取刚创建的属性id，拿到这个id后作为content的parentId
        int id = attributeName.getId();

        AttributeFunction attributeFunction = new AttributeFunction(0, id, 0, 0,0,Integer.parseInt(uniqueCode),Integer.parseInt(displayCode));
        attributeFunction.setAttNameId(id);
        attributeFunctionService.add(attributeFunction);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+id+"");
    }

    public void addThree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收刚添加属性的id
        String id = request.getParameter("id");


        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        for (AttributeContent attributeContent : attributeContents) {
            attributeContent.setParentId(Integer.parseInt(id));
            attributeContent.setProductId(attributeContent.getId());
        }

        //调用service
        attributeService.addContentAfterName(attributeContents);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询该分类下的所有属性
    public void selectAttributeName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        List<AttributeName> attributeNames = attributeService.selectAttributeName(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id获取要修改的信息进行回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<AttributeName> attributeNames = attributeService.selectById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //更新属性信息
    public void updateById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        attributeService.updateById(attributeName);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据id删除属性信息
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        attributeService.deleteById(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //跟随者物料的添加进而添加该物料后添加的属性信息
    public void addAttributeContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数组
        BufferedReader bufferedReader = request.getReader();
        String params = bufferedReader.readLine();


        //JSON序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(params, AttributeContent.class);

        //调用service
        attributeService.addAttributeContent(attributeContents);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }
    private static final ValueFilter valueFilter = (o, s, o1) -> o1 == null ? "" : o1;
    //更新物料前查询数据用来构成输入框
    public void selectBeforeContentUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{



        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String id = request.getParameter("id");

        //调用service
        List<Map<String, Object>> maps = attributeService.selectBeforeContentUpdate(Integer.parseInt(s), Integer.parseInt(id));
        //转化为json数据
        String s1 = JSON.toJSONString(maps,valueFilter);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //更新基本属性后更新后添加的属性
    public void updateAttributeContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数组
        BufferedReader bufferedReader = request.getReader();
        String params = bufferedReader.readLine();

        //JSON序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(params, AttributeContent.class);
//        System.out.println(attributeContents);
        //调用service
        attributeService.updateAttributeContent(attributeContents);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //新增分类属性
    public void addSortAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的属性
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        attributeService.addSortAttribute(attributeName);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询该分类下的属性
    public void selectAttributeNameByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader reader = request.getReader();
        String s = reader.readLine();
        //调用service
        List<AttributeName> attributeNames = attributeService.selectAttributeNameByParentId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据id查询属性信息进行回显
    public void selectAttributeNameById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<AttributeName> attributeNames = attributeService.selectAttributeNameById(Integer.parseInt(id));
        //转化为JSON数据

        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id更新属性信息
    public void updateAttributeNameById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收uniqueCode
        String uniqueCode = request.getParameter("uniqueCode");
        String displayCode = request.getParameter("displayCode");
        //json序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        int id = attributeName.getId();

        //调用service
        attributeService.updateAttributeNameById(attributeName);
        attributeFunctionService.updateUnique(Integer.parseInt(uniqueCode),id, Integer.parseInt(displayCode));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据id删除属性信息
    public void  deleteAttributeNameById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        attributeService.deleteAttributeNameById(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //在属性名表中查询长度是否存在
    public void selectIfLengthExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        int i = attributeService.selectIfLengthExist(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //根据ID在属性名表里增加长度
    public void updateLength(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //提取ID和length
        int id = attributeName.getId();
        int length = attributeName.getLength();
        //调用service
        attributeService.updateLength(length, id);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询属性值以及编码是否被物料用到
    public void  selectIfUse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String content = bufferedReader.readLine();
        String parentId = request.getParameter("parentId");
        //调用service
        boolean b = attributeService.selectIfUse(Integer.parseInt(parentId), content);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    //查询该属性中是否有属性值被用到物料上
    public void  selectIfUseByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeService.selectIfUseByParentId(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询该属性的属性值是否是范围的
    public void selectIfRange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeService.selectIfRange(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询当前物料属性中所有被用到的信息
    public void  selectACByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeContent> attributeContents = attributeService.selectACByParentId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents, SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据产品id查询该产品的所有后添加的属性
    public void selectByProductId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeContent> attributeContents = attributeService.selectByProductId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询该属性书否存在（更新时使用）
    public void  selectAttributeContentIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeContent attributeContent = JSON.parseObject(s, AttributeContent.class);
        //调用service
        boolean b = attributeService.selectAttributeContentIfExist(attributeContent);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //添加公共属性
    public void addPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String uniqueCode = request.getParameter("uniqueCode");
        String displayCode = request.getParameter("displayCode");

        //JSON序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        attributeService.addPublic(attributeName);
        int id = attributeName.getId();
        AttributeFunction attributeFunction = new AttributeFunction(0, id, 0, 0,0,Integer.parseInt(uniqueCode),Integer.parseInt(displayCode));
        attributeFunction.setAttNameId(id);
        attributeFunctionService.add(attributeFunction);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询公共属性
    public void selectPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<AttributeName> attributeNames = attributeService.selectPublic();

        //转化JSON数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public void selectNameExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = attributeService.selectNameExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public void selectUpdateNameExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
        //调用service
        boolean b = attributeService.selectUpdateNameExist(attributeName);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查询该属性填写的所有内容是否为空
    public void selectContentNull(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeContent> attributeContents = attributeService.selectContentNull(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(attributeContents,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //数量显示页面
    //根据属性名id和物料id查询这个属性值的内容
    public void selectContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        String productId = request.getParameter("productId");
        //调用service
        List<AttributeContent> attributeContents = attributeService.selectContent(Integer.parseInt(s), Integer.parseInt(productId));
        //转化为json数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //根据选中的内容查询产品id
    public void selectProductId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeContent> attributeContents = attributeService.selectProductId(s);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //动态查询
    public void selectAnd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //转化为json数据
        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        //调用service
        List<Product> products = attributeService.selectAnd(attributeContents);
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //一个一个条件的筛选
    public void selectOneByOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        //调用service
        List<Integer> integers = attributeService.selectOneByOne(attributeContents);
//        //转化为JSON数据
//        String s1 = JSON.toJSONString(integers);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(integers));

    }


    //智能筛选
    public void   IntelligentFiltering(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        if (attributeContents.size()>0){
            //调用service
            List<AttributeContent> attributeContents1 = attributeService.IntelligentFiltering(attributeContents);
            //转化为JSON数据
            String s1 = JSON.toJSONString(attributeContents1);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        else {
            //转化为JSON数据
            String s1 = "";
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }

    }


    //去重查询该属性的所有已使用的属性
    public void selectAllUsed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收分类id
        String sortId = request.getParameter("sortId");

        //调用service
        List <Map<String,Object>> attributeContents = attributeService.selectAllUsed(Integer.parseInt(s), Integer.parseInt(sortId));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //去重查询该属性的所有已使用的属性(该分类下所有的物料信息)
    public void selectAllUsedByList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收分类id
        String sortId = request.getParameter("sortId");
        String dq = request.getParameter("dq");

        List<Map<String, Object>> maps=new ArrayList<>();
        if (!dq.equals("undefined")){
            //存在，只查询当前自己分类下的属性
            Map<String,Object> map = new HashMap<>();
            map.put("id_list", sortId);
            maps.add(map);
        }
        else {
            maps= sortService.selectAllDown(Integer.parseInt(sortId));
            Map<String,Object> map = new HashMap<>();
            map.put("id_list", sortId);
            maps.add(map);
        }




        //调用service
        List <Map<String,Object>> attributeContents = attributeService.selectAllUsedByList(Integer.parseInt(s), maps);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询当前分类的上级属性和公共属性
    public void selectLastAndPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Sort> sorts = JSONArray.parseArray(s, Sort.class);
        //调用service
        List<AttributeName> attributeNames = attributeService.selectLastAndPublic(sorts);
        //转化为json数据
        String s1 = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    public void selectAbandonedAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = attributeService.selectAbandonedAttribute(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询时唯一标志的内容是否重复（添加时使用）
    public void selectUniqueContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn序列化
        AttributeContent attributeContent = JSON.parseObject(s, AttributeContent.class);
        String content = attributeContent.getContent();
        int parentId = attributeContent.getParentId();
        //调用service
        //先查询是否开启了唯一标识
        List<AttributeFunction> attributeFunctions = attributeFunctionService.selectByAttNameId(parentId);
        int uniqueCode = attributeFunctions.get(0).getUniqueCode();
        if (uniqueCode==1){
            boolean b = attributeService.selectUniqueContent(parentId, content);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+b+"");
        }
        else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+false+"");
        }


    }

    //查询时唯一标志的内容是否重复（修改时使用）
    public void selectUniqueContentUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn序列化
        AttributeContent attributeContent = JSON.parseObject(s, AttributeContent.class);
        String content = attributeContent.getContent();
        int parentId = attributeContent.getParentId();
        int productId = attributeContent.getProductId();
        //调用service
        List<AttributeFunction> attributeFunctions = attributeFunctionService.selectByAttNameId(parentId);
        int uniqueCode = attributeFunctions.get(0).getUniqueCode();
        if (uniqueCode==1){
            boolean b = attributeService.selectUniqueContentUpdate(parentId, content,productId);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+b+"");
        }
        else {
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+false+"");
        }


    }


    //查询是否可以开启唯一码功能
    public void ifUnique(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的属性id
        BufferedReader bufferedReader = request.getReader();
        String attributeNameId = bufferedReader.readLine();
        //调用service
        int i = attributeService.selectAttributeContentCount(Integer.parseInt(attributeNameId));
        int i1 = attributeService.selectAttributeContentDistinct(Integer.parseInt(attributeNameId));
        if (i==i1){
            //不存在重复的
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+false+"");
        }
        else {
            //存在重复的
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+true+"");
        }

    }


    //流水码功能：查询该分类下最大的流水码
    public void selectMaxSerialCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String sortId = request.getParameter("sortId");
        String attNameId = request.getParameter("attNameId");
        //调用service
        List<Map<String, Object>> maps = attributeService.selectMaxSerialCode(Integer.parseInt(sortId), Integer.parseInt(attNameId));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //根据content 和 sortID查询productId
    public void selectPidByContentByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收sortID
        String sortId = request.getParameter("sortId");
        //接收开启了映射中流水码的属性id
        String attNameId = request.getParameter("attNameId");
        //JSON数据序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        //查询
        List<Integer> integerListResult;
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < attributeContents.size(); i++) {
            AttributeContent attributeContent = attributeContents.get(i);
            int parentId = attributeContent.getParentId();
            String content = attributeContent.getContent();

                integerListResult = attributeService.selectPidByContentPid(parentId, content, integerList);
                integerList = integerListResult;



        }
        //最后查询出来符合这些条件的物料id

        //筛选出来在当前分类下的productid
        List<Integer> list = productService.selectPidBySidAndPid(Integer.parseInt(sortId), integerList);

        if (list.size()>0){
        //查询出来id最大的那一个的content
         List<AttributeContent> attributeContents1 = attributeService.selectContentByLsAndPid(Integer.parseInt(attNameId), list);
        //转化为JSON数据
         String s1 = JSON.toJSONString(attributeContents1);
            //返回值
            response.setContentType("text/json;charset=utf-8");
             response.getWriter().write(s1);
        }



    }

    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    public void selectIfUseBySort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的分类ID
        BufferedReader bufferedReader = request.getReader();
        String attNameId = bufferedReader.readLine();
        //调用service
        List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(Integer.parseInt(attNameId));
        List<Map<String, Object>> maps = attributeService.selectIfUseBySort(attributeValues);
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //    根据申请单内容查询属性信息
    public void  selectByAppProductIdAndVault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s, ApplicationContent.class);
        //调用service
        List<Map<String, Object>> maps = attributeService.selectByAppProductIdAndVault(applicationContents);

        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //相应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询属性信息(品牌和封装)
    public void selectBrandFz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Product product = JSON.parseObject(s, Product.class);
        int id = product.getId();
        int vault = product.getVault();
        //调用service
        List<Map<String, Object>> maps = attributeService.selectBrandFz(id, vault);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询所有属性信息
    public void selectAllAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Product product = JSON.parseObject(s, Product.class);
        int id = product.getId();
        int vault = product.getVault();
        //调用service
        List<Map<String, Object>> maps = attributeService.selectAllAttribute(id, vault);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询所有属性信息(被分配人处理让步接收使用)
    public void selectAllAttributeString(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");
        //调用service
        List<Map<String, Object>> maps = attributeService.selectAllAttribute(Integer.parseInt(productId), Integer.parseInt(vault));
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


}
