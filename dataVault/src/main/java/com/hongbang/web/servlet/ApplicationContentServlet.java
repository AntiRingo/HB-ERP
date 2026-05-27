package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.*;
import com.hongbang.service.ApplicationContentService;
import com.hongbang.service.impl.ApplicationContentServiceImpl;
import com.hongbang.util.TokenManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/applicationContent/*")
public class ApplicationContentServlet extends BaseServlet {
   //获取service
    ApplicationContentService applicationContentService = new ApplicationContentServiceImpl();


    //循环添加所有数据
    public void addAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        ApplicationForm applicationForm = JSON.parseObject(s1, ApplicationForm.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
        //调用service


        applicationContentService.addAll(applicationForm, applicationContents,jsonArrays);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //生成添加新的订购单
    public void addAllDg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        ApplicationForm applicationForm = JSON.parseObject(s1, ApplicationForm.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
        //调用service


        applicationContentService.addAllDg(applicationForm, applicationContents);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //生成新的质检单
    public void addAllZj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        ApplicationForm applicationForm = JSON.parseObject(s1, ApplicationForm.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
//        //获取要修改的申请人的ID
//        Object o1 = jsonArrays.get(2).get(0);
//        System.out.println(o1);
//        JSONObject jsonObject = new JSONObject((Map<String, Object>) o1);  // 或者直接转换o1
//        String selectedOption = jsonObject.getString("selectedOption");
//
//
//        applicationForm.setUserId(Integer.parseInt(selectedOption));
        //调用service
        //查询正在登录的人的信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if(user!=null){
            String s3 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s3, User.class);

            applicationForm.setUserId(user1.getId());
            applicationContentService.addAllZj(applicationForm, applicationContents);

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");

        }
        else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("登录超时，请重新登录！");
        }





    }

    //循环添加所有拆分的数据
    public void addAllCf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        //申请单信息
        JSONArray jsonArray = jsonArrays.get(0);
        Object o = jsonArray.get(0);
        String s1 = JSON.toJSONString(o);
        ApplicationForm applicationForm = JSON.parseObject(s1, ApplicationForm.class);
        //申请单物料信息
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s2 = JSONArray.toJSONString(jsonArray1);
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
        //调用service


        applicationContentService.addAllCf(applicationForm, applicationContents,jsonArrays);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }





    //查看申请单数据
    public void selectAppContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service


        List<ApplicationContent> applicationContents = applicationContentService.selectByAppIdDz(Integer.parseInt(s));
//        System.out.println("dz"+applicationContents);


        List<Map<String, Object>> maps = new ArrayList<>();
        List<Map<String, Object>> map = new ArrayList<>();
        List<Map<String, Object>> DzLocation = new ArrayList<>();
        List<Map<String, Object>> CpLocation = new ArrayList<>();
        List[] All = new List[3];

        if (applicationContents.size()>0){
            int applicationId = applicationContents.get(0).getApplicationId();
            maps = applicationContentService.selectAppContentDz(applicationContents);
//            DzLocation = applicationContentService.selectLocationDz(applicationContents);
            DzLocation = applicationContentService.selectAttDz(applicationId);
        }

        List<ApplicationContent> applicationContent = applicationContentService.selectByAppIdCp(Integer.parseInt(s));
//        System.out.println("cp"+applicationContent);

        if (applicationContent.size()>0){
            int applicationId = applicationContent.get(0).getApplicationId();
            map = applicationContentService.selectAppContentCp(applicationContent);
//            CpLocation=applicationContentService.selectLocationCp(applicationContent);
            CpLocation=applicationContentService.selectAttCp(applicationId);
        }


        //将两个数据整合成一个

        map.addAll(maps);
        All[0]=map;//申请单中的物料信息
        All[1]=DzLocation;//电子位置
        All[2]=CpLocation;//产品位置



        //转化为json数据
        String s1 = JSON.toJSONString(All,SerializerFeature.WriteNullStringAsEmpty);
//        String s2 = JSON.toJSONString(map);
//        String concat = s1.concat(s2);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //查询申请单中的产品数据
    public void selectByAppIdCp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来额数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<ApplicationContent> applicationContents = applicationContentService.selectByAppIdCp(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(applicationContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }



    //先更新申请单信息，在更新仓库信息(出库)
    public void updateAllCk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        HttpSession session = request.getSession();
        String token = request.getParameter("token");

        // 验证令牌
        if (!TokenManager.validateToken(session, token)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效或重复的请求");
            return;
        }

        // 处理出库业务逻辑



        try {
            // 实际出库操作
            // 执行出库业务逻辑
            List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
            //申请单信息
            JSONArray applicationContent = jsonArrays.get(0);
            String s2 = JSON.toJSONString(applicationContent);
            List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);

            //电子仓库物料信息
            JSONArray product = jsonArrays.get(1);
            List<Product> products = null;
            //产品仓库物料信息
            JSONArray finProduct = jsonArrays.get(2);
            List<FinProduct> finProducts = null;

            if (product.size()>0){
                String s1 = JSON.toJSONString(product);
                products = JSONArray.parseArray(s1, Product.class);
            }
            if (finProduct.size()>0){
                String s1 = JSON.toJSONString(finProduct);
                finProducts = JSONArray.parseArray(s1, FinProduct.class);
            }
            //日志信息
            JSONArray jsonArray = jsonArrays.get(3);
            String s1 = JSON.toJSONString(jsonArray);
            List<Log> logs = JSONArray.parseArray(s1, Log.class);

            //更新申请单信息
            applicationContentService.updateAllCk(applicationContents, products, finProducts,logs);
            //返回成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "出库失败");
        }












    }


    //先更新申请单信息，在更新仓库信息(入库)
    public void updateAllRk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();



        HttpSession session = request.getSession();
        String token = request.getParameter("token");

        // 验证令牌
        if (!TokenManager.validateToken(session, token)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效或重复的请求");
            return;
        }

        // 处理出库业务逻辑


        try {
            // 实际出库操作

            List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
            //申请单信息
            JSONArray applicationContent = jsonArrays.get(0);
            String s2 = JSON.toJSONString(applicationContent);
            List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
            //电子仓库物料信息
            JSONArray product = jsonArrays.get(1);
            List<Product> products = null;
            //产品仓库物料信息
            JSONArray finProduct = jsonArrays.get(2);
            List<FinProduct> finProducts = null;

            if (product.size()>0){
                String s1 = JSON.toJSONString(product);
                products = JSONArray.parseArray(s1, Product.class);
            }
            if (finProduct.size()>0){
                String s1 = JSON.toJSONString(finProduct);
                finProducts = JSONArray.parseArray(s1, FinProduct.class);
            }

            //日志信息
            JSONArray jsonArray = jsonArrays.get(3);
            String s1 = JSON.toJSONString(jsonArray);
            List<Log> logs = JSONArray.parseArray(s1, Log.class);


            //更新申请单信息
            applicationContentService.updateAllRk(applicationContents, products, finProducts,logs);
            //返回成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "出库失败");
        }









    }


    //先更新申请单信息，在更新仓库信息(质检)
    public void updateAllZj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        HttpSession session = request.getSession();
        String token = request.getParameter("token");

        // 验证令牌
        if (!TokenManager.validateToken(session, token)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效或重复的请求");
            return;
        }

        // 处理出库业务逻辑



        try {
            // 实际出库操作
            // 执行出库业务逻辑
            List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
            //申请单信息
            JSONArray applicationContent = jsonArrays.get(0);
            String s2 = JSON.toJSONString(applicationContent);
            List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);

            //电子仓库物料信息
            JSONArray product = jsonArrays.get(1);
            List<Product> products = null;
            //产品仓库物料信息
            JSONArray finProduct = jsonArrays.get(2);
            List<FinProduct> finProducts = null;



            if (product.size()>0){
                String s1 = JSON.toJSONString(product);
                products = JSONArray.parseArray(s1, Product.class);
            }
            if (finProduct.size()>0){
                String s1 = JSON.toJSONString(finProduct);
                finProducts = JSONArray.parseArray(s1, FinProduct.class);
            }
            //日志信息
            JSONArray jsonArray = jsonArrays.get(3);
            String s1 = JSON.toJSONString(jsonArray);
            List<Log> logs = JSONArray.parseArray(s1, Log.class);

            //质检信息
            JSONArray passData = jsonArrays.get(4);
            String s3 = JSON.toJSONString(passData);
            List<Inspection> inspectionList = JSONArray.parseArray(s3, Inspection.class);


            //更新申请单信息
            applicationContentService.updateAllZj(applicationContents, products, finProducts,logs,inspectionList);
            //返回成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "出库失败");
        }












    }

    //更新质检的状态（只更新让步接收）
    public void updateZbjs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        List<Inspection> inspectionList = JSONArray.parseArray(s, Inspection.class);
        //调用service
        applicationContentService.updateZbjs(inspectionList);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



//    //更新订单信息（撤销后提交的然后删除之前的，最后添加现在的）
//    public void updateByProveAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        //接收前端传来的数据
//        BufferedReader bufferedReader = request.getReader();
//        String s = bufferedReader.readLine();
//        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
//        //获取要修改的订单信息
//        JSONArray jsonArray = jsonArrays.get(0);
//        Object o = jsonArray.get(0);
//        String s1 = JSON.toJSONString(o);
//        ApplicationForm applicationForm = JSON.parseObject(s1, ApplicationForm.class);
//        System.out.println(applicationForm);
//        int id = applicationForm.getId();
//        //获取要修改的申请物料信息
//        JSONArray jsonArray1 = jsonArrays.get(1);
//        String s2 = JSONArray.toJSONString(jsonArray1);
//        List<ApplicationContent> applicationContents = JSONArray.parseArray(s2, ApplicationContent.class);
//        //调用service
//        applicationContentService.updateByProveAll(applicationForm, id, applicationContents);
//        //响应成功标识
//        response.setContentType("text/json;charset=utf-8");
//        response.getWriter().write("success");
//    }



    //查询该申请单要制造什么
    public void selectWhatsThis(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的申请单ID
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectWhatsThis(Integer.parseInt(appId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }




//点击查看自己的申请单复原过程-------------------------------------------------------------
//1.查询lastlevel=0并且fin_product_id=0的产品
    public void  selectOneLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String applicationId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectOneLevel(Integer.parseInt(applicationId));
        System.out.println(maps);
        //转化为json数据
        String s = JSON.toJSONString(maps,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //2.查询lastlevel=0并且fin_product_id!=0的产品(这里查询的都是已经展开的，所以肯定是成品库中的)
    public void  selectOneLevelZk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String applicationId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectOneLevelZk(Integer.parseInt(applicationId));

        //转化为json数据
        String s = JSON.toJSONString(maps, SerializerFeature.WriteMapNullValue,  // 关键：允许输出null字段
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询展开里面的内容
    public void selectProductByIdZkContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        String applicationId = request.getParameter("applicationId");
        String lastLevel = request.getParameter("lastLevel");
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectProductByIdZkContent(Integer.parseInt(finProductId), Integer.parseInt(applicationId), Integer.parseInt(lastLevel));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询可以展开的产品
    public void selectCanZk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        String applicationId = request.getParameter("applicationId");
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectCanZk(Integer.parseInt(finProductId), Integer.parseInt(applicationId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }






    //查询该申请单中是否存在已经有出库信息的
    public void  selectIfActualNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String applicationId = bufferedReader.readLine();
        //调用service
        boolean b = applicationContentService.selectIfActualNumber(Integer.parseInt(applicationId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }

    //根据申请ID 更新app_price
    public void  updateAppPrice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //转化为类
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s, ApplicationContent.class);
        //调用service
        applicationContentService.updateAppPrice(applicationContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据申请单ID   只更新申请数量
    public void  updateAppNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //转化为类
        List<ApplicationContent> applicationContents = JSONArray.parseArray(s, ApplicationContent.class);
        System.out.println(applicationContents);
        //调用service
        applicationContentService.updateAppNumber(applicationContents);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }




    //查询采购单生成的所有采购入库单的总和
    public void selectCgAndRkSum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectCgAndRkSum(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询所有的申请单数据（两个仓库的）
    public void selectByAppIdAllVault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectByAppIdAllVault(Integer.parseInt(appId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //相应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询质检单的质检信息
    public void selectZjAndAppContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationContentService.selectZjAndAppContent(Integer.parseInt(appId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }
    public void selectIfComplete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        boolean b = applicationContentService.selectIfComplete(Integer.parseInt(appId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));

    }



    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // ======================== 1. 查询主表+附表信息（你要的格式） ========================
    // 分页查询 主表+附表+物料联查
    public void selectAppAndDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader bufferedReader = request.getReader();
        String param = bufferedReader.readLine();
        Map paramMap = JSON.parseObject(param, Map.class);

        Object currentPage = paramMap.get("currentPage");
        Object pageSize = paramMap.get("pageSize");

        PageBean<Map<String, Object>> pageBean = applicationContentService.selectAppAndDetailPage(currentPage, pageSize);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(pageBean));
    }
    // ======================== 2. 新增分批到货/退货/让步 ========================
    public void addReceiveBatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String json = reader.readLine();
        Map<String, Object> param = JSON.parseObject(json);

        Integer contentId = Integer.valueOf(param.get("contentId").toString());
        String batchCode = (String) param.get("batchCode");
        BigDecimal receiveNum = new BigDecimal(param.get("receiveNum").toString());
        Integer receiveType = Integer.valueOf(param.get("receiveType").toString());
        String vault = (String) param.get("vault");
        String operateUser = (String) param.get("operateUser");
        String batchRemark = (String) param.get("batchRemark");

        ApplicationReceiveBatch batch = new ApplicationReceiveBatch();
        batch.setContentId(contentId);
        batch.setBatchCode(batchCode);
        batch.setReceiveNum(receiveNum);
        batch.setReceiveType(receiveType);
        batch.setVault(vault);
        batch.setOperateUser(operateUser);
        batch.setOperateTime(sdf.format(new Date()));
        batch.setBatchRemark(batchRemark);

        applicationContentService.addReceiveBatch(batch);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    // ======================== 3. 查询批次记录列表 ========================
    public void getBatchList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader bufferedReader = request.getReader();
        String contentId = bufferedReader.readLine();

        List<ApplicationReceiveBatch> list = applicationContentService.getBatchList(Integer.parseInt(contentId));
        String json = JSON.toJSONString(list);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }

    // ======================== 4. 获取实际到货总数量 ========================
    public void getActualTotalNum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader bufferedReader = request.getReader();
        String contentId = bufferedReader.readLine();

        BigDecimal totalNum = applicationContentService.countTotalReceiveNum(Integer.parseInt(contentId));
        String json = JSON.toJSONString(totalNum);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }



    // 自动保存
    public void autoSaveData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        String json = reader.readLine();
        Map<String, Object> saveMap = JSON.parseObject(json, Map.class);

        applicationContentService.autoSaveData(saveMap);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    // 复制新增行（到货/退货）
    public void copyRowData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader bufferedReader = request.getReader();
        String jsonStr = bufferedReader.readLine();

        // 直接用 JSONObject 取值，不会类型异常！
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        // ✅ 用 getInteger 万能安全获取
        Integer oldId = jsonObject.getInteger("id");
        Integer newNum = jsonObject.getInteger("app_number");

        Integer newId = applicationContentService.copyRowData(oldId, newNum);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(newId.toString());
    }

    // 修改申请数量
    public void updateApplicationNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader bufferedReader = request.getReader();
        String jsonStr = bufferedReader.readLine();

        // 直接用 JSONObject 取值
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        // ✅ 万能安全获取
        Integer id = jsonObject.getInteger("id");
        Integer appNumber = jsonObject.getInteger("remainNum");
        Integer addNum = jsonObject.getInteger("addNum");
        String arrive_date = jsonObject.getString("arrive_date");


        applicationContentService.editApplicationNum(id, appNumber,addNum,arrive_date);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

}
