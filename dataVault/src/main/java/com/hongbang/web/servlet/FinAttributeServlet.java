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

@WebServlet("/finAttribute/*")
public class FinAttributeServlet extends BaseServlet {
    FinAttributeService finAttributeService = new FinAttributeServiceImpl();
    FinAttributeFunctionService finAttributeFunctionService = new FinAttributeFunctionServiceImpl();
    FinSortService finSortService = new FinSortServiceImpl();
    FinProductService finProductService = new FinProductServiceImpl();
    FinAttributeValueService finAttributeValueService = new FinAttributeValueServiceImpl();

//分类属性名--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该分类下的属性
    public void selectAttributeNameByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader reader = request.getReader();
        String s = reader.readLine();
        //调用service
        List<FinAttributeName> finAttributeNames = finAttributeService.selectAttributeNameByParentId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finAttributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询公共属性
    public void selectPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinAttributeName> finAttributeNames = finAttributeService.selectPublic();
        //转化JSON数据
        String s = JSON.toJSONString(finAttributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据id查询属性信息进行回显
    public void selectAttributeNameById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinAttributeName> finAttributeNames = finAttributeService.selectAttributeNameById(Integer.parseInt(id));
        //转化为JSON数据

        String s = JSON.toJSONString(finAttributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询该分类下的所有属性
    public void selectAttributeName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        List<FinAttributeName> finAttributeNames = finAttributeService.selectAttributeName(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(finAttributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
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
        FinAttributeName finAttributeName = JSON.parseObject(s, FinAttributeName.class);
        //调用service
       finAttributeService.add(finAttributeName);
        //获取刚创建的属性id，拿到这个id后作为content的parentId
        int id = finAttributeName.getId();


        FinAttributeFunction finAttributeFunction = new FinAttributeFunction(0, id, 0, 0,0,Integer.parseInt(uniqueCode),Integer.parseInt(displayCode));

        finAttributeFunction.setFinAttNameId(id);

       finAttributeFunctionService.add(finAttributeFunction);

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


        List<FinAttributeContent> finAttributeContents = JSONArray.parseArray(s, FinAttributeContent.class);
        for (FinAttributeContent finAttributeContent : finAttributeContents) {
            finAttributeContent.setFinAttNameId(Integer.parseInt(id));
            finAttributeContent.setFinProductId(finAttributeContent.getId());
        }

        //调用service
       finAttributeService.addContentAfterName(finAttributeContents);
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
        finAttributeService.deleteById(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
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
        FinAttributeName finAttributeName = JSON.parseObject(s, FinAttributeName.class);
        int id = finAttributeName.getId();

        //调用service
        finAttributeService.updateAttributeNameById(finAttributeName);
        finAttributeFunctionService.updateUnique(Integer.parseInt(uniqueCode),id,Integer.parseInt(displayCode));
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
        int i = finAttributeService.selectIfLengthExist(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //根据id获取要修改的信息进行回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinAttributeName> finAttributeNames = finAttributeService.selectById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(finAttributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询该属性的属性值是否是范围的
    public void selectIfRange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finAttributeService.selectIfRange(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //根据ID在属性名表里增加长度
    public void updateLength(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeName finAttributeName = JSON.parseObject(s, FinAttributeName.class);
        //提取ID和length
        int id = finAttributeName.getId();
        int length = finAttributeName.getFinAttLength();
        //调用service
        finAttributeService.updateLength(length, id);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //根据id删除属性信息
    public void  deleteAttributeNameById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        finAttributeService.deleteAttributeNameById(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public void selectUpdateNameExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeName finAttributeName = JSON.parseObject(s, FinAttributeName.class);
        //调用service
        boolean b = finAttributeService.selectUpdateNameExist(finAttributeName);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public void selectNameExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finAttributeService.selectNameExist(s);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //添加公共属性
    public void addPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来数据
        request.setCharacterEncoding("utf8");
        String displayCode = request.getParameter("displayCode");
        String uniqueCode = request.getParameter("uniqueCode");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeName finAttributeName = JSON.parseObject(s, FinAttributeName.class);
        //调用service
        finAttributeService.addPublic(finAttributeName);
        int id = finAttributeName.getId();
        FinAttributeFunction finAttributeFunction = new FinAttributeFunction(0, id, 0, 0,0,Integer.parseInt(uniqueCode),Integer.parseInt(displayCode));
        finAttributeFunction.setFinAttNameId(id);
        finAttributeFunctionService.add(finAttributeFunction);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //查询时唯一标志的内容是否重复（添加时使用）
    public void selectUniqueContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn序列化
        FinAttributeContent attributeContent = JSON.parseObject(s, FinAttributeContent.class);
        String content = attributeContent.getFinAttContent();
        int parentId = attributeContent.getFinAttNameId();
        //调用service
        //先查询是否开启了唯一标识
        List<FinAttributeFunction> attributeFunctions = finAttributeFunctionService.selectByAttNameId(parentId);
        int uniqueCode = attributeFunctions.get(0).getUniqueCode();
        if (uniqueCode==1){
            boolean b = finAttributeService.selectUniqueContent(parentId, content);
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
        FinAttributeContent attributeContent = JSON.parseObject(s, FinAttributeContent.class);
        String content = attributeContent.getFinAttContent();
        int parentId = attributeContent.getFinAttNameId();
        int productId = attributeContent.getFinProductId();
        //调用service
        List<FinAttributeFunction> attributeFunctions = finAttributeFunctionService.selectByAttNameId(parentId);
        int uniqueCode = attributeFunctions.get(0).getUniqueCode();
        if (uniqueCode==1){
            boolean b = finAttributeService.selectUniqueContentUpdate(parentId, content,productId);
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


    //查询属性值以及编码是否被物料用到
    public void  selectIfUse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String content = bufferedReader.readLine();
        String parentId = request.getParameter("parentId");
        //调用service
        boolean b = finAttributeService.selectIfUse(Integer.parseInt(parentId), content);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }


//分类属性内容--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该属性中是否有属性值被用到物料上
    public void  selectIfUseByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        boolean b = finAttributeService.selectIfUseByParentId(Integer.parseInt(s));
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
        List<FinAttributeContent> finAttributeContents = finAttributeService.selectContentNull(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(finAttributeContents, SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询当前物料属性中所有被用到的信息
    public void  selectACByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinAttributeContent> finAttributeContents = finAttributeService.selectACByParentId(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finAttributeContents, SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //跟随者物料的添加进而添加该物料后添加的属性信息
    public void addAttributeContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数组
        BufferedReader bufferedReader = request.getReader();
        String params = bufferedReader.readLine();

        //JSON序列化
        List<FinAttributeContent> finAttributeContents = JSONArray.parseArray(params, FinAttributeContent.class);
        //调用service
        finAttributeService.addAttributeContent(finAttributeContents);
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
        List<Map<String, Object>> maps = finAttributeService.selectBeforeContentUpdate(Integer.parseInt(s), Integer.parseInt(id));
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
        List<FinAttributeContent> finAttributeContents = JSONArray.parseArray(params, FinAttributeContent.class);
//        System.out.println(attributeContents);
        //调用service
        finAttributeService.updateAttributeContent(finAttributeContents);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //查询该属性书否存在（更新时使用）
    public void  selectAttributeContentIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinAttributeContent finAttributeContent = JSON.parseObject(s, FinAttributeContent.class);
        //调用service
        boolean b = finAttributeService.selectAttributeContentIfExist(finAttributeContent);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //    显示页面使用(List<AttributeName>是对的)-----------------------------------------------------------------------------------------------------------------------
//查询当前分类的上级属性和公共属性
    public void selectLastAndPublic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Sort> sorts = JSONArray.parseArray(s, Sort.class);
        //调用service
        List<AttributeName> attributeNames = finAttributeService.selectLastAndPublic(sorts);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //去重查询该属性的所有已使用的属性
    public void selectAllUsed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收分类id
        String sortId = request.getParameter("sortId");

        //调用service
        List <Map<String,Object>> attributeContents = finAttributeService.selectAllUsed(Integer.parseInt(s), Integer.parseInt(sortId));
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
            maps= finSortService.selectAllDown(Integer.parseInt(sortId));
            Map<String,Object> map = new HashMap<>();
            map.put("id_list", sortId);
            maps.add(map);
        }



        //调用service
        List <Map<String,Object>> attributeContents = finAttributeService.selectAllUsedByList(Integer.parseInt(s), maps);
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //一个一个条件的筛选
    public void selectOneByOneDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        List<AttributeContent> attributeContents = JSONArray.parseArray(s, AttributeContent.class);
        //调用service
        List<Integer> integers = finAttributeService.selectOneByOneDisplay(attributeContents);
//        //转化为JSON数据
//        String s1 = JSON.toJSONString(integers);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(integers));
    }

    //根据产品id查询该产品的所有后添加的属性
    public void selectByProductIdDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<AttributeContent> attributeContents = finAttributeService.selectByProductIdDisplay(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(attributeContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询该分类中的所有后添加的属性
    public void selectAttributeNameDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //调用service
        List<AttributeName> attributeNames = finAttributeService.selectAttributeNameDisplay(Integer.parseInt(parentId));
        //转化为json数据
        String s = JSON.toJSONString(attributeNames);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    public void selectAbandonedFinAttribute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finAttributeService.selectAbandonedFinAttribute(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询是否可以开启唯一码功能
    public void ifUnique(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的属性id
        BufferedReader bufferedReader = request.getReader();
        String attributeNameId = bufferedReader.readLine();
        //调用service
        int i = finAttributeService.selectAttributeContentCount(Integer.parseInt(attributeNameId));
        int i1 = finAttributeService.selectAttributeContentDistinct(Integer.parseInt(attributeNameId));
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


    //根据content 和 sortID查询productId
    public void selectPidByContentByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收sortID
        String sortId = request.getParameter("sortId");
        //接收开启了映射中流水码的属性id
        String attNameId = request.getParameter("finAttNameId");
        //JSON数据序列化
        List<FinAttributeContent> finAttributeContents = JSONArray.parseArray(s, FinAttributeContent.class);
        //查询
        List<Integer> integerListResult;
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < finAttributeContents.size(); i++) {
            FinAttributeContent finAttributeContent = finAttributeContents.get(i);
            int parentId = finAttributeContent.getFinProductId();
            String content = finAttributeContent.getFinAttContent();

            integerListResult = finAttributeService.selectPidByContentPid(parentId, content, integerList);
            integerList = integerListResult;



        }
        //最后查询出来符合这些条件的物料id

        //筛选出来在当前分类下的productid
        List<Integer> list = finProductService.selectPidBySidAndPid(Integer.parseInt(sortId), integerList);

        if (list.size()>0){
            //查询出来id最大的那一个的content
            List<FinAttributeContent> finAttributeContents1 = finAttributeService.selectContentByLsAndPid(Integer.parseInt(attNameId), list);
            //转化为JSON数据
            String s1 = JSON.toJSONString(finAttributeContents1);
            //返回值
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }



    }

    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    public void selectIfUseBySort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinAttributeValue> finAttributeValues = finAttributeValueService.selectByAttNameId(Integer.parseInt(s));
        List<Map<String, Object>> maps = finAttributeService.selectIfUseBySort(finAttributeValues);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

}
