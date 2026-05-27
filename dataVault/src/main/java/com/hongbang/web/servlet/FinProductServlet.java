package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.*;
import com.hongbang.service.FinAttributeService;
import com.hongbang.service.FinProductService;
import com.hongbang.service.FinSortService;
import com.hongbang.service.impl.FinAttributeServiceImpl;
import com.hongbang.service.impl.FinProductServiceImpl;
import com.hongbang.service.impl.FinSortServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/finProduct/*")
public class FinProductServlet extends BaseServlet {
    FinProductService finProductService = new FinProductServiceImpl();
    FinAttributeService finAttributeService = new FinAttributeServiceImpl();
    FinSortService finSortService = new FinSortServiceImpl();


    //查询该分类下是否有物料
    public void selectByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的parentid
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();

        //调用service
        boolean b = finProductService.selectByParentId(Integer.parseInt(parentId));

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }


    //查询该分类下的所有物料( )
    public void selectAllInSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FinProduct> finProducts = finProductService.selectAllInSort(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(finProducts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用 )
    public void selectAllInSortDeleteSign(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        FinProduct product = JSON.parseObject(s, FinProduct.class);
        //调用service
        List<FinProduct> products = finProductService.selectAllInSortDeleteSign(product.getFinSortId(),product.getFinProductName());
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下的所有物料(更新物料信息时查询是否跟已启用的物料信息比较时使用 )
    public void selectAllInSortDeleteSignUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        FinProduct product = JSON.parseObject(s, FinProduct.class);
        //调用service
        List<FinProduct> products = finProductService.selectAllInSortDeleteSignUpdate(product.getFinSortId(),product.getFinProductName(),product.getId());
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }



    //查询是否有物料存在
    public void ifProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        boolean b = finProductService.ifProduct();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查询所有的物料信息
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<FinProduct> finProducts = finProductService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(finProducts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //在当前分类下添加物料
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //数据序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        if (username!=null){
            String a = JSON.toJSONString(username);
            User user = JSON.parseObject(a, User.class);
            int id1 = user.getId();
            //调用service
            finProductService.add(finProduct,id1);
            int id = finProduct.getId();

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+id+"");
        }
        else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("登录超时，请重新登录！");
        }



    }


    //删除物料信息
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finProductService.deleteById(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //重新启用
    public void enableFinProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finProductService.enableFinProduct(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //真正删除
    public void deleteReally(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finProductService.deleteReally(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //模糊查询(搜索已弃用的物料信息，不分页)
    public void searchAbandoned(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();

        //调用service
        List<Product> products = finProductService.searchAbandoned(str);
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询当前分类下有没有物料名称相同的物料(添加)
    public void selectAddNameIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        //调用service
        boolean b = finProductService.selectAddNameIfExist(finProduct);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    //查询物料号是否重复(新增物料的时候)
    public void selectMN(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        int parentId = finProduct.getFinSortId();
        String materialNumber = finProduct.getFinMaterialNumber();
        //调用service
        boolean b = finProductService.selectMN(parentId, materialNumber);
        //返回数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询该分类下的物料
    public void selectByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的ParentId
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();

        String currentPage = request.getParameter("currentPage");

        int pageSize=20;

        //调用service
        PageBean<FinProduct> finProductPageBean = finProductService.selectByPid(Integer.parseInt(parentId), Integer.parseInt(currentPage), pageSize);
        //转化为json数据
        String s = JSON.toJSONString(finProductPageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);


    }

    //获取物料信息进行回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinProduct> finProducts = finProductService.selectById(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(finProducts,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //获取物料信息进行回显
    public void selectByIdAsProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Product> products = finProductService.selectByIdAsProduct(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //修改物料信息
    public void updateById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json数据序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        //调用service
        finProductService.updateById(finProduct);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询当前分类下有没有物料名称相同的物料(更新)
    public void selectNameIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        //调用service
        boolean b = finProductService.selectNameIfExist(finProduct);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    //查询物料号是否重复(更新物料的时候)

    public void selectUpMN(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinProduct finProduct = JSON.parseObject(s, FinProduct.class);
        int parentId = finProduct.getFinSortId();
        String materialNumber = finProduct.getFinMaterialNumber();
        int id = finProduct.getId();
        //调用service
        boolean b = finProductService.selectUpMN(parentId, materialNumber,id);
        //返回数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }



    //根据分类id查询该分类下是否有产品
    public void  selectIfProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = finProductService.selectIfProduct(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询（添加BOM表信息时查询两个表中的数据）
    public void searchFromTwoTable (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //接收信息
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();

        String currentPage = request.getParameter("currentPage");

        int size=20;
        //调用service
        PageBean<Map<String, Object>> pageBean = finProductService.searchFromTwoTable(str, Integer.parseInt(currentPage), size);
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //显示页面使用list<Product>是正确的   ------------------------------------------------------------------------------------------------------------------------


    //查询该分类下的所有物料( )
    public void selectAllInSortDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Product> products = finProductService.selectAllInSortDisplay(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }



    //查询product基本信息
    public void selectProductByAllIdDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收parentId(N哪个分类下的无聊)
        String parentId = request.getParameter("parentId");

        //转化为数组
        List<Integer> list = JSONArray.parseArray(s, Integer.class);

        List<Product> products = finProductService.selectProductByAllIdDisplay(list);

        List<Product> apps=new ArrayList<>();
        List<Map<String, Object>> maps = finSortService.selectAllDown(Integer.parseInt(parentId));
        Map<String,Object> map = new HashMap<>();
        map.put("id_list", parentId);
        maps.add(map);

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int parentId1 = product.getParentId();
            for (int j = 0; j < maps.size(); j++) {
                Map<String, Object> map1 = maps.get(j);
                Object id_list = map1.get("id_list");
                if (Integer.parseInt((String) id_list)==parentId1){
                    apps.add(product);
                    break;

                }
            }



        }

        //转化为JSON数据
        String s1 = JSON.toJSONString(apps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //筛选后的结果，按价格正序
    public void BrandAscDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Integer> list = JSONArray.parseArray(s, Integer.class);


        //调用service
        List<Product> products = finProductService.BrandAscDisplay(list);

        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询该分类下所有的物料信息，分页查询
    public void selectAllInSortLimitDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据，parentID
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //接收当前页码
        String currentpage = request.getParameter("currentPage");
        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");
        int size=25;

        List<Map<String, Object>> maps = finSortService.selectAllDown(Integer.parseInt(parentId));
        Map<String,Object> map = new HashMap<>();
        map.put("id_list", parentId);
        maps.add(map);
        //调用service
        PageBean<Product> pageBean = finProductService.selectAllInSortLimitDisplay(maps, Integer.parseInt(currentpage), size,Integer.parseInt(ifs),
                Integer.parseInt(price),Integer.parseInt(priceDesc),Integer.parseInt(number),Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询该分类下所有的物料信息，分页查询
    public void selectAllInSortLimitDisplayOnly(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据，parentID
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();
        //接收当前页码
        String currentpage = request.getParameter("currentPage");
        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");


        int size=25;
        //调用service
        PageBean<Product> pageBean = finProductService.selectAllInSortLimitDisplayOnly(Integer.parseInt(parentId), Integer.parseInt(currentpage), size,Integer.parseInt(ifs),
                Integer.parseInt(price),Integer.parseInt(priceDesc),Integer.parseInt(number),Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }



    //根据该分类下的所有物料信息获取物料后添加的属性，分页查询
    public void selectAllInSortContentLimitDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的该页的物料信息数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        List<Product> products = JSONArray.parseArray(s, Product.class);
        //根据当前物料信息查询后添加的属性信息,然后将物料信息和属性值信息合并到一个数组当中

        ArrayList<List> ob = new ArrayList<>();
        ob.add(products);
        if (products.size()>0){
            List<AttributeContent> attributeContents1 = finAttributeService.selectByListProductDisplay(products);
            ob.add(attributeContents1);
            //转化为JSON数据
            String s1 = JSON.toJSONString(ob);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        else {
            String s1 = "";
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }


    }


    //查询已经弃用的物料信息
    public void selectAbandonedFinProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String currentPage = request.getParameter("currentPage");
        int pageSize=20;

        //调用service
        PageBean<Product> products = finProductService.selectAbandonedFinProduct(Integer.parseInt(currentPage),pageSize);
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }



    //查询该分类下的所有产品信息，不包括自己
    public void selectCopyBySort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<FinProduct> finProducts = finProductService.selectCopyBySort(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(finProducts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询当前分类下的信息不包括自己
    public void selectSearchCopyBySort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf8");
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //接收前端传来的数据
        String str = request.getParameter("str");
        //调用service
        List<FinProduct> finProducts = finProductService.selectSearchCopyBySort(Integer.parseInt(id),str);
        //转化为JSON数据
        String s = JSON.toJSONString(finProducts);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //更新价格
    public void updatePrice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String price = request.getParameter("price");
        String finProductId = request.getParameter("finProductId");
        //调用service
        finProductService.updatePrice(price, Integer.parseInt(finProductId));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



}
