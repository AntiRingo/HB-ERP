package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.User;
import com.hongbang.pojo.UserFunction;
import com.hongbang.pojo.UserFunctionTwo;
import com.hongbang.service.UserFunctionService;
import com.hongbang.service.impl.UserFunctionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/userFunction/*")
public class UserFunctionServlet extends BaseServlet {
   //调用service
    UserFunctionService userFunctionService = new UserFunctionServiceImpl();

    //循环添加用户的功能信息
    public void addAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        System.out.println(s);
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);

        JSONArray jsonArray = jsonArrays.get(0);
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s1 = JSON.toJSONString(jsonArray);
        List<UserFunction> userFunctions = JSONArray.parseArray(s1, UserFunction.class);
        String s2 = JSON.toJSONString(jsonArray1);
        List<UserFunctionTwo> userFunctionTwos = JSONArray.parseArray(s2, UserFunctionTwo.class);

        //调用service
        if (userFunctions.size()>0 && userFunctionTwos.size()>0){
            userFunctionService.addAll23(userFunctions, userFunctionTwos);
        }

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //查询用户的权限信息
    public void selectByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<UserFunction> userFunctions = userFunctionService.selectByUserId(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(userFunctions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //循环更新用户的功能信息
    public  void updateAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<JSONArray> jsonArrays = JSONArray.parseArray(s, JSONArray.class);
        JSONArray jsonArray = jsonArrays.get(0);
        JSONArray jsonArray1 = jsonArrays.get(1);
        String s1 = JSON.toJSONString(jsonArray);
        List<UserFunction> userFunctions = JSONArray.parseArray(s1, UserFunction.class);
        String s2 = JSON.toJSONString(jsonArray1);
        List<UserFunctionTwo> userFunctionTwos = JSONArray.parseArray(s2, UserFunctionTwo.class);
        //查询这个用户是什么级别
        String level = request.getParameter("level");
//        System.out.println(level);
//        System.out.println(userFunctions);
//        System.out.println(userFunctionTwos);
        if (Integer.parseInt(level)>=2){

             //查询用户id,然后删除原来的，最后添加最新的

            if (userFunctions.size()>0 && userFunctionTwos.size()>0){
                int userId = userFunctions.get(0).getUserId();
                userFunctionService.deleteUserFunction(userId);
                userFunctionService.deleteUserFunctionTwo(userId);
                userFunctionService.addAll23(userFunctions,userFunctionTwos);
            }

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");

        }
        else {
            //调用service
            if (userFunctions.size()>0 && userFunctionTwos.size()>0){
                userFunctionService.updateAll23(userFunctions,userFunctionTwos);
            }

            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }



    }


    //查询用户是否拥有物料分类管理的权限（添加物料的部分有用到这一功能）
    public void selectFunction2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = userFunctionService.selectFunction2(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //查询详细权限user_function_two
    public void selectUserFunctionTwoByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userId = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = userFunctionService.selectUserFunctionTwoByUserId(Integer.parseInt(userId));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询详细权限user_function_two（前端不传数据版本）
    public void selectUserFunctionTwoNoUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        if(user!=null) {
            int id = user.getId();
            List<Map<String, Object>> maps = userFunctionService.selectUserFunctionTwoByUserId(id);
            //转化为json数据
            String s1 = JSON.toJSONString(maps);
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }
        //调用service

    }

    //查询用户是否具有审核物料信息的权限
    public void selectShPQx(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        if(user!=null) {

            //调用service
            boolean b = userFunctionService.selectShPQx(user.getId());

            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+b+"");
        }
        else {
            //响应数据
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("登录超时，请重新登录！");
        }


    }

}
