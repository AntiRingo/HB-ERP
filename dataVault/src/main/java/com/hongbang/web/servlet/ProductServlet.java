package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.*;
import com.hongbang.service.AttributeService;
import com.hongbang.service.FinProductService;
import com.hongbang.service.ProductService;
import com.hongbang.service.SortService;
import com.hongbang.service.impl.AttributeServiceImpl;
import com.hongbang.service.impl.FinProductServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;
import com.hongbang.service.impl.SortServiceImpl;
import com.hongbang.util.SqlEscapeUtil;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet("/product/*")
public class ProductServlet extends BaseServlet {

    ProductService productService = new ProductServiceImpl();
    AttributeService attributeService = new AttributeServiceImpl();
    FinProductService finProductService = new FinProductServiceImpl();
    SortService sortService = new SortServiceImpl();


    //在当前分类下添加物料
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //数据序列化
        Product product = JSON.parseObject(s, Product.class);
        //查询正在登录的人的信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if(user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);

            //调用service
            productService.add(product,user1.getId());
            int id = product.getId();

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+id+"");
        }
        else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("登录超时，请重新登录！");
        }



    }

    //获取物料信息进行回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Product> products = productService.selectById(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(products,SerializerFeature.WriteNullStringAsEmpty);
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
        Product product = JSON.parseObject(s, Product.class);
        //调用service
        productService.updateById(product);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询该分类下的物料
    public void selectByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的ParentId
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();

        String currentPage = request.getParameter("currentPage");

        int pageSize=20;

        //调用service
        PageBean<Product> products = productService.selectByPid(Integer.parseInt(parentId), Integer.parseInt(currentPage),pageSize);
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);


    }

    //删除物料信息
    public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        productService.deleteById(Integer.parseInt(id));
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
        productService.deleteReally(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //重新启用
    public void enableProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        productService.enableProduct(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //查询该分类下是否有物料
    public void selectByParentId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的parentid
        BufferedReader bufferedReader = request.getReader();
        String parentId = bufferedReader.readLine();

        //调用service
        boolean b = productService.selectByParentId(Integer.parseInt(parentId));

      //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }


    //模糊查询
    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        //接收信息
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();

        String currentPage = request.getParameter("currentPage");

        int size=20;
        //调用service
        PageBean<Product> search = productService.search(Integer.parseInt(currentPage), size, str);
        //转化为json数据
        String s = JSON.toJSONString(search);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //查询该分类下的所有物料( )
    public void selectAllInSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Product> products = productService.selectAllInSort(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用 )
    public void selectAllInSortDeleteSign(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Product product = JSON.parseObject(s, Product.class);
        //调用service
        List<Product> products = productService.selectAllInSortDeleteSign(product.getParentId(),product.getName());
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

        Product product = JSON.parseObject(s, Product.class);
        //调用service
        List<Product> products = productService.selectAllInSortDeleteSignUpdate(product.getParentId(),product.getName(),product.getId());
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下所有的物料信息，分页查询(包含分类中分类的物料信息)
    public void selectAllInSortLimit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        List<Map<String, Object>> maps = sortService.selectAllDown(Integer.parseInt(parentId));
        Map<String,Object> map = new HashMap<>();
        map.put("id_list", parentId);
        maps.add(map);

        int size=25;
        //调用service
        PageBean<Product> pageBean = productService.selectAllInSortLimit(maps, Integer.parseInt(currentpage), size,Integer.parseInt(ifs),
                Integer.parseInt(price),Integer.parseInt(priceDesc),Integer.parseInt(number),Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询该分类下所有的物料信息，分页查询（只包含自己分类下的物料信息）
    public void selectAllInSortLimitOnly(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        PageBean<Product> pageBean = productService.selectAllInSortLimitOnly(Integer.parseInt(parentId), Integer.parseInt(currentpage), size,Integer.parseInt(ifs),
                Integer.parseInt(price),Integer.parseInt(priceDesc),Integer.parseInt(number),Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

//筛选分页
    public void selectLimitByProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据，parentID
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Product> products = JSONArray.parseArray(s, Product.class);

        //接收当前页码
        String currentpage = request.getParameter("currentPage");
        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");

        int size=25;
        //调用service
        PageBean<Product> pageBean = productService.selectLimitByProduct(products, Integer.parseInt(currentpage), size, Integer.parseInt(ifs),
                Integer.parseInt(price), Integer.parseInt(priceDesc), Integer.parseInt(number), Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s1 = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

//筛选分页
    public void selectLimitByFinProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据，parentID
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Product> products = JSONArray.parseArray(s, Product.class);

        //接收当前页码
        String currentpage = request.getParameter("currentPage");
        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");

        int size=25;
        //调用service
        PageBean<Product> pageBean = productService.selectLimitByFinProduct(products, Integer.parseInt(currentpage), size, Integer.parseInt(ifs),
                Integer.parseInt(price), Integer.parseInt(priceDesc), Integer.parseInt(number), Integer.parseInt(numberDesc));
        //转化为JSON数据
        String s1 = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据该分类下的所有物料信息获取物料后添加的属性，分页查询
    public void selectAllInSortContentLimit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的该页的物料信息数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        List<Product> products = JSONArray.parseArray(s, Product.class);
        //根据当前物料信息查询后添加的属性信息,然后将物料信息和属性值信息合并到一个数组当中

        ArrayList<List> ob = new ArrayList<>();
        ob.add(products);
        if (products.size()>0){
            List<AttributeContent> attributeContents1 = attributeService.selectByListProduct(products);
            ob.add(attributeContents1);
            //转化为JSON数据
            String s1 = JSON.toJSONString(ob, SerializerFeature.WriteNullStringAsEmpty);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }



    }


    //查询是否有物料存在
    public void ifProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        boolean b = productService.ifProduct();
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
        Product product = JSON.parseObject(s, Product.class);
        int parentId = product.getParentId();
        String materialNumber = product.getMaterialNumber();
        //调用service
        boolean b = productService.selectMN(parentId, materialNumber);
        //返回数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询与弃用的物料号是否重复

    public void selectMNAbandoned(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        Product product = JSON.parseObject(s, Product.class);
        int parentId = product.getParentId();
        String materialNumber = product.getMaterialNumber();
        String name = product.getName();
        //调用service
        List<Product> products = productService.selectMNAbandoned(parentId, materialNumber,name);
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //返回数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询物料号是否重复(更新物料的时候)

    public void selectUpMN(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        Product product = JSON.parseObject(s, Product.class);
        int parentId = product.getParentId();
        String materialNumber = product.getMaterialNumber();
        int id = product.getId();
        //调用service
        boolean b = productService.selectUpMN(parentId, materialNumber,id);
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
        boolean b = productService.selectIfProduct(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //查询当前分类下有没有物料名称相同的物料(更新)
    public void selectNameIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        Product product = JSON.parseObject(s, Product.class);
        //调用service
        boolean b = productService.selectNameIfExist(product);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }


    //查询当前分类下有没有物料名称相同的物料(添加)
    public void selectAddNameIfExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        Product product = JSON.parseObject(s, Product.class);
        //调用service
        boolean b = productService.selectAddNameIfExist(product);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }

    //查询所有的物料信息
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Product> products = productService.selectAll();
        //转化为JSON数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //数量页面使用
    //查询总数
    public void selectTotalCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        int i = productService.selectTotalCount();
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }





    //搜索功能
    public void searchProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String sortId = request.getParameter("sortId");
        String all = request.getParameter("all");//all可以是lingjian或者chengpin
        String sortVault = request.getParameter("sortVault");

        if (all == null){
            all="all";
        }
        else {
            if (sortVault!=null){
                if (sortVault.length()>0 && Integer.parseInt(sortVault)==1){
                    all="chengpin";
                }
                else if (sortVault.length()>0 && Integer.parseInt(sortVault)==0){
                    all="lingjian";
                }
            }


        }
        //调用service


        // 使用方式,转译，让数据库查询%
        str = SqlEscapeUtil.escapeLike(str);

        //查询数据是否带单位
        String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern2="(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);

        if (m.matches()){
            //编译
            Pattern pat = Pattern.compile(pattern1);
//        匹配
            Matcher macth = pat.matcher(str);
//        matcher类函数实现
        String string=  macth.replaceAll("");

            //编译
            Pattern pat2 = Pattern.compile(pattern2);
//        匹配
            Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
        String unit= macth2.replaceAll("");


            //去除单位的

                //带单位
                //unit就是取出的单位,查询带这个单位的attributeNameId
                //调用service


                //查询整个数据，带着单位
                List<Integer> integers = productService.searchProductUnit(str, Integer.parseInt(sortId));

                //查询末尾是这个的
                List<Integer> integers1 = attributeService.searchContentUnit1(string, Integer.parseInt(sortId),unit);

            //查询整个数据，带着单位
            List<Integer> integers2 = productService.searchFinProductUnit(str, Integer.parseInt(sortId));

            //查询末尾是这个的
            List<Integer> integers3 = attributeService.searchFinContentUnit1(string, Integer.parseInt(sortId),unit);

                //合并两个数组
                List<Integer> list = new ArrayList<>();

                list.addAll(integers1);

               list.addAll(integers);

            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());

            List<Integer> list2= new ArrayList<>();
            list2.addAll(integers2);
            list2.addAll(integers3);
            list2 = list2.stream().distinct().collect(Collectors.toList());

            List<Product> products = new ArrayList<>();
               if (list.size()>0 && !all.equals("chengpin")){
                   //调用service
                   List<Product> products1 = productService.selectProductByAllId(list);
//                   Map<Integer, List<Product>> map = products.stream().collect(Collectors.groupingBy(Product::getParentId));
//                   String s = JSON.toJSONString(map);
//                   System.out.println(s);
//                   Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

                   products.addAll(products1);

               }
               if (list2.size()>0 && !all.equals("lingjian")){
                   List<Product> products1 = productService.selectFinProductByAllId(list2);

                   products.addAll(products1);
               }

            //转化为JSON数据
            String s = JSON.toJSONString(products,SerializerFeature.WriteNullStringAsEmpty);

            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }
        else {

            //不带单位
            List<Integer> integers = productService.searchProduct(str, Integer.parseInt(sortId));

            List<Integer> integers1 = attributeService.searchContent(str, Integer.parseInt(sortId));

            //不带单位
            List<Integer> integers2 = productService.searchFinProduct(str, Integer.parseInt(sortId));
            List<Integer> integers3 = attributeService.searchFinContent(str, Integer.parseInt(sortId));

            //合并两个数组
            List<Integer> list = new ArrayList<>();
            list.addAll(integers);
            list.addAll(integers1);
            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());

            //合并两个数组
            List<Integer> list2 = new ArrayList<>();
            list2.addAll(integers2);
            list2.addAll(integers3);
            //去除重复项
            list2 = list2.stream().distinct().collect(Collectors.toList());
            List<Product> products=new ArrayList<>();
            if (list.size()>0 && !all.equals("chengpin")){
                //调用service
                List<Product> products1 = productService.selectProductByAllId(list);
//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果
                    products.addAll(products1);

            }
            if (list2.size()>0 && !all.equals("lingjian")){


                List<Product> products1 = productService.selectFinProductByAllId(list2);

//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果
                products.addAll(products1);

            }
            //转化为json数据
            String s = JSONArray.toJSONString(products,SerializerFeature.WriteNullStringAsEmpty);

            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }




    }


    //在搜索结果中搜索
    public void searchProductInProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");

        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        String str = request.getParameter("str");
//        String str = new String(request.getParameter("str").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 使用方式,转译，让数据库查询%
        str = SqlEscapeUtil.escapeLike(str);
        String priceUp = request.getParameter("priceUp");
        String numberUp = request.getParameter("numberUp");
//        System.out.println("price"+priceUp);
//        System.out.println("number"+numberUp);

        List<Product> products1 = JSONArray.parseArray(s1, Product.class);
        System.out.println(products1);

        //调用service


        //查询数据是否带单位

        String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern2="^(?i)(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);


        if (m.matches()){
            System.out.println("带单位");
            //编译
            Pattern pat = Pattern.compile(pattern1);
//        匹配
            Matcher macth = pat.matcher(str);
//        matcher类函数实现
            String string=  macth.replaceAll("");

            //编译
            Pattern pat2 = Pattern.compile(pattern2);
//        匹配
            Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
            String unit= macth2.replaceAll("");



            //去除单位的

            //带单位
            //unit就是取出的单位,查询带这个单位的attributeNameId
            //调用service


            //查询整个数据，带着单位
            List<Integer> integers = productService.selectInSelectP(products1,str);


            //查询末尾是这个的
            List<Integer> integers1 = attributeService.selectInSelectUnit(string, products1,unit);


            //合并两个数组
            List<Integer> list = new ArrayList<>(integers);

            list.addAll(integers1);

            list.addAll(integers);
            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());

            //查询整个数据，带着单位

            List<Integer> integers2 = productService.selectInSelectFP(products1,str);




            //查询末尾是这个的
            List<Integer> integers3 = attributeService.selectInFinSelectUnit(string, products1,unit);
            //合并两个数组
            List<Integer> list1 = new ArrayList<>(integers);
            //判断该仓库中是否有物料
            boolean b = finProductService.ifProduct();
            if (b){
                list1.addAll(integers2);
                list1.addAll(integers3);
                //去除重复项
                list1= list1.stream().distinct().collect(Collectors.toList());
            }


            if (list.size()>0||list1.size()>0){
                if (priceUp.equals("0") ||priceUp.equals("1")){
                    if (priceUp.equals("0")){
                        List<Product> products = new ArrayList<>();
                        //倒叙
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByBrandDesc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByBrandDesc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }
                    else {
                        //正序
                        List<Product> products = new ArrayList<>();
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByBrandAsc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByBrandAsc(list1);
                            products.addAll(products2);
                        }



                        //转化为JSON数据
                        String s = JSON.toJSONString(products,SerializerFeature.WriteNullStringAsEmpty);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }

                }
                else if (numberUp.equals("0") || numberUp.equals("1")) {

                    if (numberUp.equals("0")){
                        List<Product> products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByNumberDesc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByNumberDesc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }else {

                        List<Product> products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByNumberAsc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByNumberAsc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }

                }
                else {
                    List<Product> products = new ArrayList<>();
                    //调用service
                    if (list.size()>0){
                        List<Product> products2 = productService.selectProductByAllId(list);
                        products.addAll(products2);
                    }
                  if (list1.size()>0){
                      List<Product> products2 = productService.selectFinProductByAllId(list1);
                      products.addAll(products2);
                  }

                    //转化为JSON数据
                    String s = JSON.toJSONString(products,SerializerFeature.WriteNullStringAsEmpty);

                    //响应数据
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(s);
                }

            }


        }
        else {
            System.out.println("不带单位");
            //不带单位
            List<Integer> integers = productService.selectInSelectP(products1,str);
            List<Integer> integers1 = attributeService.selectInSelect(str, products1);
//            System.out.println(str);
            //合并两个数组
            List<Integer> list = new ArrayList<>(integers);
            list.addAll(integers);
            list.addAll(integers1);
            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());

            //合并两个数组
            List<Integer> list1 = new ArrayList<>(integers);
            //判断该仓库中是否有物料
            boolean b = finProductService.ifProduct();
            if (b){
                List<Integer> integers2 = productService.selectInSelectFP(products1,str);
                List<Integer> integers3 = attributeService.selectInFinSelect(str, products1);

                list1.addAll(integers2);
                list1.addAll(integers3);
            }


            //去除重复项
            list1 = list1.stream().distinct().collect(Collectors.toList());
            if (list.size()>0 ||list1.size()>0){
                if (priceUp.equals("0") ||priceUp.equals("1")){
                    if (priceUp.equals("0")){
                        //倒叙
                        List<Product>products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByBrandDesc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByBrandDesc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }
                    else {
                        //正序
                        List<Product>products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByBrandAsc(list);
                            products.addAll(products2);
                        }
                       if (list1.size()>0){
                           List<Product> products2 = productService.selectFinProductByBrandAsc(list1);
                           products.addAll(products2);
                       }

                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }

                }
                else if (numberUp.equals("0") || numberUp.equals("1")) {

                    if (numberUp.equals("0")){
                        List<Product>products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByNumberDesc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByNumberDesc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }else {
                        List<Product>products = new ArrayList<>();
                        //调用service
                        if (list.size()>0){
                            List<Product> products2 = productService.selectProductByNumberAsc(list);
                            products.addAll(products2);
                        }
                        if (list1.size()>0){
                            List<Product> products2 = productService.selectFinProductByNumberAsc(list1);
                            products.addAll(products2);
                        }


                        //转化为JSON数据
                        String s = JSON.toJSONString(products);

                        //响应数据
                        response.setContentType("text/json;charset=utf-8");
                        response.getWriter().write(s);
                    }

                }
                else {
                    List<Product>products = new ArrayList<>();
                    //调用service
                    if (list.size()>0){
                        List<Product> products2 = productService.selectProductByAllId(list);
                        products.addAll(products2);
                    }
                    if (list1.size()>0){
                        List<Product> products2 = productService.selectFinProductByAllId(list1);
                        products.addAll(products2);
                    }


                    //转化为JSON数据
                    String s = JSON.toJSONString(products);

                    //响应数据
                    response.setContentType("text/json;charset=utf-8");
                    response.getWriter().write(s);
                }


            }else {
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write("");
            }

        }




    }



//    //查询该分类下的所有物料,根据价格倒叙
//   public void selectAllInSortByBrandDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        //接收前端传来的数据
//       BufferedReader bufferedReader = request.getReader();
//       String s = bufferedReader.readLine();
//       //调用service
//       List<Product> products = productService.selectAllInSortByBrandDesc(Integer.parseInt(s));
//       //转化为JSON数据
//       String s1 = JSON.toJSONString(products);
//       //响应数据
//       response.setContentType("text/json;charset=utf-8");
//       response.getWriter().write(s1);
//
//
//   }
//    //查询该分类下的所有物料,根据价格正序
//  public void selectAllInSortByBrandAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//      //接收前端传来的数据
//      BufferedReader bufferedReader = request.getReader();
//      String s = bufferedReader.readLine();
//      //调用service
//      List<Product> products = productService.selectAllInSortByBrandAsc(Integer.parseInt(s));
//
//      //转化为JSON数据
//      String s1 = JSON.toJSONString(products);
//      //响应数据
//      response.setContentType("text/json;charset=utf-8");
//      response.getWriter().write(s1);
//
//  }
//
//    //查询该分类下的所有物料,根据库存数量倒叙
//  public void selectAllInSortByNumberDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//      //接收前端传来的数据
//      BufferedReader bufferedReader = request.getReader();
//      String s = bufferedReader.readLine();
//      //调用service
//      List<Product> products = productService.selectAllInSortByNumberDesc(Integer.parseInt(s));
//      //转化为JSON数据
//      String s1 = JSON.toJSONString(products);
//      //响应数据
//      response.setContentType("text/json;charset=utf-8");
//      response.getWriter().write(s1);
//
//
//  }
//    //查询该分类下的所有物料,根据库存数量正序
//   public void  selectAllInSortByNumberAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//       //接收前端传来的数据
//       BufferedReader bufferedReader = request.getReader();
//       String s = bufferedReader.readLine();
//       //调用service
//       List<Product> products = productService.selectAllInSortByNumberAsc(Integer.parseInt(s));
//       //转化为JSON数据
//       String s1 = JSON.toJSONString(products);
//       //响应数据
//       response.setContentType("text/json;charset=utf-8");
//       response.getWriter().write(s1);
//
//   }





    //筛选后的结果，按价格倒叙
    public void BrandDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Integer> list = JSONArray.parseArray(s, Integer.class);
        //调用service
        List<Product> products = productService.BrandDesc(list);
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);


    }

    //筛选后的结果，按价格正序
    public void BrandAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Integer> list = JSONArray.parseArray(s, Integer.class);


        //调用service
        List<Product> products = productService.BrandAsc(list);

        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下的所有物料,根据库存数量倒叙
    public void NumberDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Integer> list = JSONArray.parseArray(s, Integer.class);
        //调用service
        List<Product> products = productService.NumberDesc(list);
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询该分类下的所有物料,根据库存数量正序
    public void NumberAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Integer> list = JSONArray.parseArray(s, Integer.class);
        //调用service
        List<Product> products = productService.NumberAsc(list);
        //转化为JSON数据
        String s1 = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询product基本信息根据价格正序，搜索
   public void selectProductByBrandAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
       request.setCharacterEncoding("utf8");
       BufferedReader bufferedReader = request.getReader();
       String str = bufferedReader.readLine();
       String sortId = request.getParameter("sortId");
       //调用service


       //查询数据是否带单位
       String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern2="(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
       Pattern r = Pattern.compile(pattern);
       Matcher m = r.matcher(str);

       if (m.matches()){
           //编译
           Pattern pat = Pattern.compile(pattern1);
//        匹配
           Matcher macth = pat.matcher(str);
//        matcher类函数实现
           String string=  macth.replaceAll("");

           //编译
           Pattern pat2 = Pattern.compile(pattern2);
//        匹配
           Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
           String unit= macth2.replaceAll("");


           //去除单位的

           //带单位
           //unit就是取出的单位,查询带这个单位的attributeNameId
           //调用service


           //查询整个数据，带着单位
           List<Integer> integers = productService.searchProductUnit(str, Integer.parseInt(sortId));

           //查询末尾是这个的
           List<Integer> integers1 = attributeService.searchContentUnit1(string, Integer.parseInt(sortId),unit);

           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);

           list.addAll(integers1);



           list.addAll(integers);

           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByBrandAsc(list);
//                   Map<Integer, List<Product>> map = products.stream().collect(Collectors.groupingBy(Product::getParentId));
//                   String s = JSON.toJSONString(map);
//                   System.out.println(s);
//                   Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为JSON数据
               String s = JSON.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }


       }
       else {
           //不带单位
           List<Integer> integers = productService.searchProduct(str, Integer.parseInt(sortId));
           List<Integer> integers1 = attributeService.searchContent(str, Integer.parseInt(sortId));
           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);
           list.addAll(integers);
           list.addAll(integers1);
           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByBrandAsc(list);
//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为json数据
               String s = JSONArray.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }

       }


   }

    //查询product基本信息根据价格正序，倒叙
   public void selectProductByBrandDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
       request.setCharacterEncoding("utf8");
       BufferedReader bufferedReader = request.getReader();
       String str = bufferedReader.readLine();
       String sortId = request.getParameter("sortId");
       //调用service


       //查询数据是否带单位
       String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern2="(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
       Pattern r = Pattern.compile(pattern);
       Matcher m = r.matcher(str);

       if (m.matches()){
           //编译
           Pattern pat = Pattern.compile(pattern1);
//        匹配
           Matcher macth = pat.matcher(str);
//        matcher类函数实现
           String string=  macth.replaceAll("");

           //编译
           Pattern pat2 = Pattern.compile(pattern2);
//        匹配
           Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
           String unit= macth2.replaceAll("");


           //去除单位的

           //带单位
           //unit就是取出的单位,查询带这个单位的attributeNameId
           //调用service


           //查询整个数据，带着单位
           List<Integer> integers = productService.searchProductUnit(str, Integer.parseInt(sortId));

           //查询末尾是这个的
           List<Integer> integers1 = attributeService.searchContentUnit1(string, Integer.parseInt(sortId),unit);

           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);

           list.addAll(integers1);



           list.addAll(integers);

           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByBrandDesc(list);
//                   Map<Integer, List<Product>> map = products.stream().collect(Collectors.groupingBy(Product::getParentId));
//                   String s = JSON.toJSONString(map);
//                   System.out.println(s);
//                   Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为JSON数据
               String s = JSON.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }


       }
       else {
           //不带单位
           List<Integer> integers = productService.searchProduct(str, Integer.parseInt(sortId));
           List<Integer> integers1 = attributeService.searchContent(str, Integer.parseInt(sortId));
           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);
           list.addAll(integers);
           list.addAll(integers1);
           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByBrandDesc(list);
//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为json数据
               String s = JSONArray.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }

       }

   }


    //查询product基本信息根据库存数量正序，搜索
   public void selectProductByNumberAsc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       //接收前端传来的数据
       request.setCharacterEncoding("utf8");
       BufferedReader bufferedReader = request.getReader();
       String str = bufferedReader.readLine();
       String sortId = request.getParameter("sortId");
       //调用service


       //查询数据是否带单位
       String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
       String pattern2="(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
       Pattern r = Pattern.compile(pattern);
       Matcher m = r.matcher(str);

       if (m.matches()){
           //编译
           Pattern pat = Pattern.compile(pattern1);
//        匹配
           Matcher macth = pat.matcher(str);
//        matcher类函数实现
           String string=  macth.replaceAll("");

           //编译
           Pattern pat2 = Pattern.compile(pattern2);
//        匹配
           Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
           String unit= macth2.replaceAll("");


           //去除单位的

           //带单位
           //unit就是取出的单位,查询带这个单位的attributeNameId
           //调用service


           //查询整个数据，带着单位
           List<Integer> integers = productService.searchProductUnit(str, Integer.parseInt(sortId));

           //查询末尾是这个的
           List<Integer> integers1 = attributeService.searchContentUnit1(string, Integer.parseInt(sortId),unit);

           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);

           list.addAll(integers1);



           list.addAll(integers);

           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByNumberAsc(list);
//                   Map<Integer, List<Product>> map = products.stream().collect(Collectors.groupingBy(Product::getParentId));
//                   String s = JSON.toJSONString(map);
//                   System.out.println(s);
//                   Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为JSON数据
               String s = JSON.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }


       }
       else {
           //不带单位
           List<Integer> integers = productService.searchProduct(str, Integer.parseInt(sortId));
           List<Integer> integers1 = attributeService.searchContent(str, Integer.parseInt(sortId));
           //合并两个数组
           List<Integer> list = new ArrayList<>(integers);
           list.addAll(integers);
           list.addAll(integers1);
           //去除重复项
           list = list.stream().distinct().collect(Collectors.toList());
           if (list.size()>0){
               //调用service
               List<Product> products = productService.selectProductByNumberAsc(list);
//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

               //转化为json数据
               String s = JSONArray.toJSONString(products);

               //响应数据
               response.setContentType("text/json;charset=utf-8");
               response.getWriter().write(s);
           }

       }

   }

    //查询product基本信息根据库存数量正序，搜索
    public void selectProductByNumberDesc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String sortId = request.getParameter("sortId");
        //调用service


        //查询数据是否带单位
        String pattern = "(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern1="(?i)[VAW]|(ch)|(Khz)|(bit)|(KS/s)|(PPM)$";
        String pattern2="(?i)^(([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*))[fpnum%KMGT]?";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);

        if (m.matches()){
            //编译
            Pattern pat = Pattern.compile(pattern1);
//        匹配
            Matcher macth = pat.matcher(str);
//        matcher类函数实现
            String string=  macth.replaceAll("");

            //编译
            Pattern pat2 = Pattern.compile(pattern2);
//        匹配
            Matcher macth2 = pat2.matcher(str);
//        matcher类函数实现
            String unit= macth2.replaceAll("");


            //去除单位的

            //带单位
            //unit就是取出的单位,查询带这个单位的attributeNameId
            //调用service


            //查询整个数据，带着单位
            List<Integer> integers = productService.searchProductUnit(str, Integer.parseInt(sortId));

            //查询末尾是这个的
            List<Integer> integers1 = attributeService.searchContentUnit1(string, Integer.parseInt(sortId),unit);

            //合并两个数组
            List<Integer> list = new ArrayList<>(integers);

            list.addAll(integers1);



            list.addAll(integers);

            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());
            if (list.size()>0){
                //调用service
                List<Product> products = productService.selectProductByNumberDesc(list);
//                   Map<Integer, List<Product>> map = products.stream().collect(Collectors.groupingBy(Product::getParentId));
//                   String s = JSON.toJSONString(map);
//                   System.out.println(s);
//                   Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

                //转化为JSON数据
                String s = JSON.toJSONString(products);

                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);
            }


        }
        else {
            //不带单位
            List<Integer> integers = productService.searchProduct(str, Integer.parseInt(sortId));
            List<Integer> integers1 = attributeService.searchContent(str, Integer.parseInt(sortId));
            //合并两个数组
            List<Integer> list = new ArrayList<>(integers);
            list.addAll(integers);
            list.addAll(integers1);
            //去除重复项
            list = list.stream().distinct().collect(Collectors.toList());
            if (list.size()>0){
                //调用service
                List<Product> products = productService.selectProductByNumberDesc(list);
//                Map<Integer, List<Product>> groups = products.stream().collect(Collectors.groupingBy(Product::getParentId));        // 输出分组结果

                //转化为json数据
                String s = JSONArray.toJSONString(products);

                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);
            }

        }

    }

    //根据id查询产品数据
    public void selectProductByAllId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收parentId(N哪个分类下的物料)
        String parentId = request.getParameter("parentId");

        //转化为数组
        List<Integer> list = JSONArray.parseArray(s, Integer.class);

        List<Product> products = productService.selectProductByAllId(list);


        List<Product> apps=new ArrayList<>();
        List<Map<String, Object>> maps = sortService.selectAllDown(Integer.parseInt(parentId));
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


    //根据该分类下的所有物料信息获取物料后添加的属性，分页查询,带排序
    public void selectAllInSortContentLimitOrderBy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的该页的物料信息数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        List<Product> products = JSONArray.parseArray(s, Product.class);

        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");

        //调用service
        List<Product> products1 = productService.selectAllInSortLimitOrderBy(products, Integer.parseInt(ifs), Integer.parseInt(price), Integer.parseInt(priceDesc), Integer.parseInt(number), Integer.parseInt(numberDesc));
        //根据当前物料信息查询后添加的属性信息,然后将物料信息和属性值信息合并到一个数组当中

        ArrayList<List> ob = new ArrayList<>();
        ob.add(products1);
        if (products1.size()>0){
            List<AttributeContent> attributeContents1 = attributeService.selectByListProduct(products1);
            ob.add(attributeContents1);
            //转化为JSON数据
            String s1 = JSON.toJSONString(ob,SerializerFeature.WriteNullStringAsEmpty);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }



    }


    //根据本地储存在数据查询产品信息在前端显示
    public void selectProductByLocation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<Product> products = JSONArray.parseArray(s, Product.class);
//        System.out.println(products);
        List<Product> products1 = new ArrayList<>();
        //调用service
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Product product1 = productService.selectProductByLocation(product);
            products1.add(product1);
        }

        //转化为json数据
        String s1 = JSON.toJSONString(products1);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //根据物料id查询content 和mapping
    public void jz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String readLine = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> jz = productService.jz(Integer.parseInt(readLine));
        //转化为JSON数据
        String s = JSON.toJSONString(jz);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //根据id循环更新物料号
    public void updateMaterialNumberById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        List<Product> products = JSONArray.parseArray(s, Product.class);
        //调用service
        productService.updateMaterialNumberById(products);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询已经弃用的物料信息
    public void selectAbandonedProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String currentPage = request.getParameter("currentPage");
        int pageSize=20;

        //调用service
       PageBean<Product> products = productService.selectAbandonedProduct(Integer.parseInt(currentPage),pageSize);
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询物料信息（添加BOM表的时候查看）
    public void selectProductWhenAddBom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String id = request.getParameter("id");
        String vault = request.getParameter("vault");
        //调用service
        List<Map<String, Object>> maps = productService.selectProductWhenAddBom(Integer.parseInt(id), Integer.parseInt(vault));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //入库出库操作
    public void  vault (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s, ApplicationContent.class);
        //调用service
        productService.vault(applicationContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


//  显示页面使用  ====================================================================================================================

    //根据该分类下的所有物料信息获取物料后添加的属性，分页查询,带排序
    public void selectAllInFinSortContentLimitOrderBy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的该页的物料信息数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        List<Product> products = JSONArray.parseArray(s, Product.class);

        String ifs = request.getParameter("ifs");
        String price = request.getParameter("price");
        String priceDesc = request.getParameter("priceDesc");
        String number = request.getParameter("number");
        String numberDesc = request.getParameter("numberDesc");

        //调用service
        List<Product> products1 = productService.selectAllInFinSortLimitOrderBy(products, Integer.parseInt(ifs), Integer.parseInt(price), Integer.parseInt(priceDesc), Integer.parseInt(number), Integer.parseInt(numberDesc));
        //根据当前物料信息查询后添加的属性信息,然后将物料信息和属性值信息合并到一个数组当中

        ArrayList<List> ob = new ArrayList<>();
        ob.add(products1);

        List<AttributeContent> attributeContents1 = attributeService.selectByListFinProduct(products1);
        ob.add(attributeContents1);
        //转化为JSON数据
        String s1 = JSON.toJSONString(ob,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //模糊查询(搜索已弃用的物料信息，不分页)
    public void searchAbandoned(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();

        //调用service
        List<Product> products = productService.searchAbandoned(str);
        //转化为json数据
        String s = JSON.toJSONString(products);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询该分类最后插入的物料号，为了获取最后两位流水码
    public void selectMaterialNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端获取的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = productService.selectMaterialNumber(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }


    //根据物料号查询物料的信息
    public void selectByMaterialNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String materialNumber = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = productService.selectByMaterialNumber(materialNumber);
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


}
