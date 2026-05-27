package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.User;
import com.hongbang.service.ApplicationFormService;
import com.hongbang.service.TakeOrderService;
import com.hongbang.service.UserFunctionService;
import com.hongbang.service.UserService;
import com.hongbang.service.impl.ApplicationFormServiceImpl;
import com.hongbang.service.impl.TakeOrderServiceImpl;
import com.hongbang.service.impl.UserFunctionServiceImpl;
import com.hongbang.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/applicationForm/*")
public class ApplicationFormServlet extends BaseServlet {
   //获取service
    ApplicationFormService applicationFormService = new ApplicationFormServiceImpl();
    UserService userService = new UserServiceImpl();
    TakeOrderService takeOrderService = new TakeOrderServiceImpl();
    UserFunctionService userFunctionService = new UserFunctionServiceImpl();


//    //新增申请单
//    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    //接收前端传来的数据
//        BufferedReader bufferedReader = request.getReader();
//        String s = bufferedReader.readLine();
//        //json数据序列化
//        ApplicationForm applicationForm = JSON.parseObject(s, ApplicationForm.class);
//        //调用service
//
//        applicationFormService.add(applicationForm);
//        //获取新添加的申请单id作为添加applicationContent的applicationId
//        int id = applicationForm.getId();
//
//        response.getWriter().write(""+id+"");
//
//    }


    //删除申请表
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的id
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        applicationFormService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询申请单信息（包含申请人信息）
    public void selectAllApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String sort = bufferedReader.readLine();


        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        if (user != null){
            User user1 = userService.selectById(Integer.parseInt(user));
            int level = user1.getLevel();
            //判斷是否是員工級別数据
            boolean a;
            if (level<=2){
                //部长以上的级别
                a = false;
            }else {
                a=true;
            }

            //查询出库入库权限
            boolean rk = userFunctionService.selectRkQx(Integer.parseInt(user));
            boolean ck = userFunctionService.selectCkQx(Integer.parseInt(user));
            boolean cg = userFunctionService.selectCgQx(Integer.parseInt(user));
            boolean zj = userFunctionService.selectZjQx(Integer.parseInt(user));
            boolean dg = userFunctionService.selectDgQx(Integer.parseInt(user));


            //调用service
            PageBean<Map<String, Object>> pageBean = applicationFormService.selectAllApplication(Integer.parseInt(sort), Integer.parseInt(currentPage), pageSize, Integer.parseInt(user),a,ck,rk,cg,zj,dg);

            //转化为json数据
            String s = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }
        else {

        }

    }

    //查询订购申请单信息（包含申请人信息）
    public void selectDgApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String sort = bufferedReader.readLine();


        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        if (user != null){
            User user1 = userService.selectById(Integer.parseInt(user));
            int level = user1.getLevel();
            //判斷是否是員工級別数据
            boolean a;
            if (level<=2){
                //部长以上的级别
                a = false;
            }else {
                a=true;
            }

            //查询出库入库权限
            boolean rk = false;
            boolean ck = false;
            boolean cg = false;
            boolean zj = false;
            boolean dg = userFunctionService.selectDgQx(Integer.parseInt(user));


            //调用service
            PageBean<Map<String, Object>> pageBean = applicationFormService.selectAllApplication(Integer.parseInt(sort), Integer.parseInt(currentPage), pageSize, Integer.parseInt(user),a,ck,rk,cg,zj,dg);

            //转化为json数据
            String s = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }
        else {

        }

    }

    //查询质检申请单信息（包含申请人信息）
    public void selectZjApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String sort = bufferedReader.readLine();


        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        if (user != null){
            User user1 = userService.selectById(Integer.parseInt(user));
            int level = user1.getLevel();
            //判斷是否是員工級別数据
            boolean a;
            if (level<=2){
                //部长以上的级别
                a = false;
            }else {
                a=true;
            }

            //查询出库入库权限
            boolean rk = false;
            boolean ck = false;
            boolean cg = false;
            boolean zj = userFunctionService.selectZjQx(Integer.parseInt(user));
            boolean dg = false;


            //调用service
            PageBean<Map<String, Object>> pageBean = applicationFormService.selectAllApplication(Integer.parseInt(sort), Integer.parseInt(currentPage), pageSize, Integer.parseInt(user),a,ck,rk,cg,zj,dg);

            //转化为json数据
            String s = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s);
        }
        else {

        }

    }


    //根据用户ID查询该用户的申请单
    public void  selectByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);
        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //调用service
        PageBean pageBean = applicationFormService.selectByUserId(map, Integer.parseInt(currentPage), pageSize);

        //转化为json数据
        String s1 = JSON.toJSONString(pageBean,SerializerFeature.WriteNullStringAsEmpty);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //更改申请单为撤销状态
    public void updateRevoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        applicationFormService.updateRevoke(Integer.parseInt(s));


        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查看是否有新增的申请信息(入库申请)
    public void  selectIfNewApplicationRk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //调用service
        List<Integer> list = applicationFormService.selectIfNewApplicationRk();
        //转化为JSON数据
        String s = JSON.toJSONString(list);

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查看是否有新增的申请信息(出库申请)

   public void  selectIfNewApplicationCk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
       List<Integer> list = applicationFormService.selectIfNewApplicationCk();
       //转化为JSON数据
       String s = JSON.toJSONString(list);

       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }

    //点击查看后将申请单从新增状态改为取货中状态
    public void updateStatusTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        applicationFormService.updateStatusTwo(Integer.parseInt(s));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //申请单筛选
    public void screen (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        User user1 = userService.selectById(Integer.parseInt(user));
        int level = user1.getLevel();
        //判斷是否是員工級別数据
        boolean a;
//        if (level==4){
//            a = true;
//        }else {
//            a=false;
//        }
        if (level<=2){
            //部长以上的级别
            a = false;
        }else {
            a=true;
        }

        //调用service
        PageBean screen = applicationFormService.screen(map, Integer.parseInt(currentPage), pageSize, Integer.parseInt(user),a);
        //转化为JSON数据
        String s1 = JSON.toJSONString(screen);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据申请类型获取筛选人
    public void screenUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        User user1 = userService.selectById(Integer.parseInt(user));
        int level = user1.getLevel();
        //判斷是否是員工級別数据
        boolean a;
        if (level==4){
            a = true;
        }else {
            a=false;
        }
        //调用service
        List<Map<String, Object>> maps = applicationFormService.screenUserId(Integer.parseInt(s), Integer.parseInt(user),a);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //模糊查询
    public void searchOrderNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String sort = request.getParameter("sort");
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        User user1 = userService.selectById(Integer.parseInt(user));
        int level = user1.getLevel();
        //判斷是否是員工級別数据
        boolean a;
        if (level==4){
            a = true;
        }else {
            a=false;
        }
        //调用service
        PageBean<Map<String, Object>> pageBean = applicationFormService.searchOrderNumber(str, Integer.parseInt(sort), Integer.parseInt(currentPage), pageSize, Integer.parseInt(user),a);
        //转化为json数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //将申请单状态修改为已出库
    public void updateStatusFour(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        applicationFormService.updateStatusFour(Integer.parseInt(id));
        //相应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }
    //修改申请单状态
    public void updateStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        String status = request.getParameter("status");
        //调用service
        applicationFormService.updateStatus(Integer.parseInt(status),Integer.parseInt(id));
        //相应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询是否存在已出库状态的申请单
    public void selectPickUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userId = bufferedReader.readLine();
        //调用service
        boolean b = applicationFormService.selectPickUp(Integer.parseInt(userId));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }


    //查询是否存在已出库状态的申请单
    public void selectPickUpAppId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userId = bufferedReader.readLine();
        //调用service
        List<Integer> list = applicationFormService.selectPickUpAppId(Integer.parseInt(userId));
        //转化为JSON数据
        String s = JSON.toJSONString(list);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }
    //根据id查询申请单信息
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<ApplicationForm> applicationForms = applicationFormService.selectById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(applicationForms);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据用户id查询该用户的申请单（已完成的和已撤销的除外）
    public void selectAppFormNotCompleteAndDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userId = bufferedReader.readLine();
        //调用service
        List<ApplicationForm> applicationForms = applicationFormService.selectAppFormNotCompleteAndDelete(Integer.parseInt(userId));
        //转化为JSON数据
        String s = JSON.toJSONString(applicationForms);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //仓库查看已经审核通过的申请
    public  void depotExamine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<ApplicationForm> applicationForms = applicationFormService.depotExamine();
        //转化为JSON数据
        String s = JSON.toJSONString(applicationForms);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }



   //审核：部长或者总经理，要根据等级来判断
    public void examine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String minister = bufferedReader.readLine();
        //接收页码
        String currentPage = request.getParameter("currentPage");
        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        int pageSize = 15;




        //2、判断user是否为null,如果不为null说明用户登录过了
        response.setContentType("text/json;charset=utf-8");
        if (user!=null){
            String s = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s, User.class);
            int level;
            String agent = request.getParameter("agent");

            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }

            if (level==2){
                //总经理级别


            }
            else if (level==3){

                //部长级别
                int department = user1.getDepartment();
                //调用service
                PageBean<ApplicationForm> applicationForms = applicationFormService.ministerExamine(department, Integer.parseInt(minister), Integer.parseInt(currentPage),pageSize);
                //转化为JSON数据
                String s1 = JSON.toJSONString(applicationForms);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s1);

            }

        }else {
            response.getWriter().write("false");
        }

    }

    //部长,总经理筛选
    public void ministerScreen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;

        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int level;
            String agent = request.getParameter("agent");
            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }
            int department = user1.getDepartment();
            System.out.println(level);
           if(level==3 || level==2){
                //部长级别或总经理级别
                //调用service
                PageBean pageBean = applicationFormService.ministerScreen(map, Integer.parseInt(currentPage), pageSize,department);
                //转化为JSON数据
                String s2 = JSON.toJSONString(pageBean, SerializerFeature.WriteNullStringAsEmpty);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s2);
            }
        }
        else {
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }

    }

    //查询是否有新的申请单
    public void selectIfExamine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String sort = bufferedReader.readLine();
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int level;
            String agent = request.getParameter("agent");
            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }
            int department = user1.getDepartment();

            if (level==3 || level==2){
                //部长级别,调用service
                List<Integer> list = applicationFormService.selectIfExamine(Integer.parseInt(sort), department);
                //转化为json数据
                String s = JSON.toJSONString(list);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);


            }
        }
    }

    //已经申请的人员（筛选使用）
    public void screenUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int level;
            String agent = request.getParameter("agent");
            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }
            int department = user1.getDepartment();

           if (level==3 || level==2){
                //部长级别
             //调用service
                List maps = applicationFormService.ministerScreenUser(map,department);
                //转化为json数据
                String s3 = JSON.toJSONString(maps);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s3);


            }
        }
    }

    //部长显示申请部门(筛选使用)
    public void screenDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map = JSON.parseObject(s, Map.class);

        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int level;
            String agent = request.getParameter("agent");
            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }
            int department = user1.getDepartment();

            if (level==3 || level==2){
                //部长级别
                //调用service
                List maps = applicationFormService.ministerScreenDepartment(map,department);
                //转化为json数据
                String s3 = JSON.toJSONString(maps);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s3);


            }
        }
    }



    //根据申请单id查询是否总价钱
    public void selectPriceAndCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn数据序列化
        List<ApplicationForm> applicationForms = JSONArray.parseArray(s, ApplicationForm.class);
        //调用service
        List<Map<String, Object>> maps = applicationFormService.selectPriceAndCount(applicationForms);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据id查询申请单状态
    public void selectComplete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationFormService.selectComplete(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //查询审核流程
    public void selectLc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appFormId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = applicationFormService.selectLc(Integer.parseInt(appFormId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //查询申请单的发票是否已经全部上传并审核完成
    public void selectInvoiceSign(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        boolean b = applicationFormService.selectInvoiceSign(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }


    //根据申请单id查询总价（只包含零件，成品价格在前端查询）
    public void selectPriceAndCountLj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //josn数据序列化
        List<ApplicationForm> applicationForms = JSONArray.parseArray(s, ApplicationForm.class);
        //调用service
        List<Map<String, Object>> maps = applicationFormService.selectPriceAndCountLj(applicationForms);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //接单使用（查询所有出库，入库的并且是待查看状态的，并且是未接单状态的申请单）
    public void selectTakeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //查询用户是几级账号
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user!=null){
            String s1 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s1, User.class);
            int level;
        level = user1.getLevel();


        //查询出入库权限
            //查询出库入库权限
            boolean rk = userFunctionService.selectRkQx(user1.getId());
            boolean ck = userFunctionService.selectCkQx(user1.getId());
            //查询采购权限
            boolean cg = userFunctionService.selectCgQx(user1.getId());
            //查询质检权限
            boolean zj = userFunctionService.selectZjQx(user1.getId());
            //查询订购权限
            boolean dg = userFunctionService.selectDgQx(user1.getId());


            if (level==4){

                //调用service
                List<ApplicationForm> applicationForms = applicationFormService.selectTakeOrder(ck,rk,cg,zj,dg);
                //转化为json数据
                String s = JSON.toJSONString(applicationForms);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);
            }
            else {
                //接收前端传来的数据
                String currentPage = request.getParameter("currentPage");
                int pageSize = 15;
                PageBean<Map<String, Object>> pageBean = takeOrderService.selectAllTakeOrder(Integer.parseInt(currentPage), pageSize,ck,rk,cg,zj,dg);
                //转化为json数据
                String s = JSON.toJSONString(pageBean);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);
            }
        }


    }


    public void selectDgByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的页码
        BufferedReader bufferedReader = request.getReader();
        String currentPage = bufferedReader.readLine();
        //接受每页多少条
        String size = request.getParameter("size");
        //查询正在登录人的ID
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        if(user!=null){
            int id = user.getId();
            //调用service
            PageBean<ApplicationForm> pageBean = applicationFormService.selectDgByUserId(id, Integer.parseInt(currentPage), Integer.parseInt(size));
            //转化为json数据
            String s1 = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        else {
            //返回登录失效
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }
    }
    //查询自己申请的质检单
    public void selectMyZj(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的页码
        BufferedReader bufferedReader = request.getReader();
        String currentPage = bufferedReader.readLine();
        //接受每页多少条
        String size = request.getParameter("size");
        //查询正在登录人的ID
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        if(user!=null){
            int id = user.getId();
            //调用service
            PageBean<ApplicationForm> pageBean = applicationFormService.selectMyZj(id, Integer.parseInt(currentPage), Integer.parseInt(size));
            //转化为json数据
            String s1 = JSON.toJSONString(pageBean);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        else {
            //返回登录失效
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }
    }

    //申购单（即采购单）不需要进行接单，该类型的申请单由采购部部长进行拆分
    public void selectAllCgApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        Map map = JSON.parseObject(s1, Map.class);


        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        //接收用戶ID，查詢用戶ID是否大于4
        String user = request.getParameter("user");
        if (user != null){
            User user1 = userService.selectById(Integer.parseInt(user));
            int level = user1.getLevel();
            if (level<=3){
                //查询出库入库权限
                boolean rk = userFunctionService.selectRkQx(Integer.parseInt(user));
                boolean ck = userFunctionService.selectCkQx(Integer.parseInt(user));
                boolean cg = userFunctionService.selectCgQx(Integer.parseInt(user));
                boolean zj = userFunctionService.selectZjQx(Integer.parseInt(user));


                //调用service
                PageBean pageBean = applicationFormService.selectAllCgApplication(Integer.parseInt(currentPage), pageSize, ck,rk,cg,zj,map);

                //转化为json数据
                String s = JSON.toJSONString(pageBean);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);
            }


        }
        else {

        }

    }


    //申购单（即采购单）不需要进行接单，该类型的申请单由采购部部长进行拆分
    public void selectAllDgApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s1 = bufferedReader.readLine();
        Map map = JSON.parseObject(s1, Map.class);


        //接收页码
        String currentPage = request.getParameter("currentPage");
        int pageSize=15;
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");
        if (user != null){

            String s2 = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s2, User.class);

            int level = user1.getLevel();
            int id = user1.getId();

                //查询出库入库权限
                boolean rk = userFunctionService.selectRkQx(id);
                boolean ck = userFunctionService.selectCkQx(id);
                boolean cg = userFunctionService.selectCgQx(id);
                boolean zj = userFunctionService.selectZjQx(id);


                //调用service
                PageBean pageBean = applicationFormService.selectAllDgApplication(Integer.parseInt(currentPage), pageSize, ck,rk,cg,zj,map,id);

                //转化为json数据
                String s = JSON.toJSONString(pageBean);
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(s);



        }
        else {

        }

    }



    // 获取申请单状态
    public void getStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int applicationId = Integer.parseInt(params.get("application_id").toString());

            //调用service获取详细信息
            Map<String, Object> result = applicationFormService.getApplicationStatusDetail(applicationId);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取状态失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 获取简单的状态码
    public void getSimpleStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int applicationId = Integer.parseInt(params.get("application_id").toString());

            //调用service
            int status = applicationFormService.getApplicationStatus(applicationId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", status);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取状态失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 更新申请单状态
    public void updateStatusNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();
            // 从URL参数获取状态
            String statusParam = request.getParameter("status");
            int newStatus = Integer.parseInt(statusParam);



            //解析JSON数据
            int applicationId = Integer.parseInt(jsonStr);


            //调用service
            boolean success = applicationFormService.updateStatusNew(applicationId, newStatus);

            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("status", "success");
                result.put("message", "状态更新成功");
            } else {
                result.put("status", "error");
                result.put("message", "状态更新失败");
            }

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "更新状态失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }
}
