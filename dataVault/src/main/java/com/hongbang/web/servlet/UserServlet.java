package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Function;
import com.hongbang.pojo.FunctionTwo;
import com.hongbang.pojo.Module;
import com.hongbang.pojo.User;
import com.hongbang.pojo.UserSession;
import com.hongbang.service.UserService;
import com.hongbang.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    public static final Map<String, UserSession> sessionMap = new ConcurrentHashMap<>(); // 线程安全的全局Map
    //获取service
    UserService userService = new UserServiceImpl();

    //添加用户信息
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //json序列化
        User user = JSON.parseObject(s, User.class);
        //调用service
        userService.add(user);
        int id = user.getId();
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+id+"");

    }

    //修改用户权限
//    public void updatePurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//        //接收前端传来的信息
//        BufferedReader bufferedReader = request.getReader();
//        String s = bufferedReader.readLine();
//        //json序列化
//        User user = JSON.parseObject(s, User.class);
//        //调用service
//        userService.updatePurview(user);
//        //响应成功标识
//        response.setContentType("text/json;charset=utf-8");
//        response.getWriter().write("success");
//    }


    //查询所有用户信息
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<User> users = userService.selectAll();
        //转化为json数据
        String s = JSON.toJSONString(users);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //删除用户信息
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        userService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //根据id查询用户信息
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        User users = userService.selectById(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(users);
        User user = JSON.parseObject(s, User.class);
        user.setPassWord("");
        String s1 = JSON.toJSONString(user);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }
//
//    //登录验证
//    public void loginVerification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
//
//        //处理请求乱码问题
//
//        request.setCharacterEncoding("utf-8");
//        response.setContentType("text/html;charset=utf-8");
//        //1、接收前端传来的数据
//
//
//
//        BufferedReader bufferedReader = request.getReader();
//
//        String username = bufferedReader.readLine();
//
//        String password = request.getParameter("password");
//        //接收验证码
//        String checkCode = request.getParameter("checkCode");
//
//
//
//        //从session中获取程序生成的验证码
//
//        HttpSession session = request.getSession();
//        String checkCodeGen = (String)session.getAttribute("checkCodeGen");
//
//
//
////
//
//        User user = userService.login(username, password);
//
//
//
//
//        //3、判断
//        if(user!= null && checkCodeGen.equalsIgnoreCase(checkCode)){
//            //登陆成功，跳转页面
//
//
//            //session.getServletContext()得到时application对象
//            ServletContext application=session.getServletContext();
//
//            Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
//            if(loginMap==null){
//                loginMap = new HashMap<>();
//            }
//
//
//            for(String key:loginMap.keySet()) {
//                if (user.getUserName().equals(key)) {
//                    if(session.getId().equals(loginMap.get(key))) {
//                        System.out.println(username+"在同一地点多次登录！");
//                    }else{
//
//                        System.out.println(username+"异地登录");
//
//                        HttpSession session1 = MySessionContext.getSession(loginMap.get(key));
//                       if (session1!=null){
////                           session1.removeAttribute("username");
//                           session1.invalidate();
//                       }
//
//
//
//
//                    }
//                }
//            }
//            loginMap.put(user.getUserName(),session.getId());
//            application.setAttribute("loginMap", loginMap);
//            session.setAttribute("username",user);
//
//
//
//
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("success");
//
//
//
//
//
//        }
//        else if(user != null && !checkCodeGen.equalsIgnoreCase(checkCode)){
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("codefalse");
//
//        }
//        else if (user==null){
//            // 登录失败,
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("false");
//
//        }
//
//
//
//
//
//
//
//    }

    /**
     * 安全地使会话失效
     * @param session 需要失效的会话对象
     */
    private void invalidateSessionSafely(HttpSession session) {
        if (session != null) {
            try {
                // 避免对已失效会话进行操作
                session.invalidate();
            } catch (IllegalStateException e) {
                // 会话已失效，无需处理
            }
        }
    }
    //获取IP地址
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0]; // 多层代理取第一个IP
    }
    //登录验证
    public void loginVerification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //处理请求乱码问题

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //1、接收前端传来的数据



        BufferedReader bufferedReader = request.getReader();

        String username = bufferedReader.readLine();

        String password = request.getParameter("password");
        //接收验证码
        String checkCode = request.getParameter("checkCode");



        //从session中获取程序生成的验证码

        HttpSession session = request.getSession();
        String checkCodeGen = (String)session.getAttribute("checkCodeGen");



        User user = userService.login(username, password);


        //3、判断
//        if(checkCodeGen!=null){
//
//
//
//
//
//        }
//        else {
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("codenull");
//        }

//        && checkCodeGen.equalsIgnoreCase(checkCode)
        if (user != null) {
            // 登录成功
            String sessionId = session.getId();
            String ip = getClientIp(request);

            // 同步块保证线程安全
            synchronized(sessionMap) {
                if (sessionMap.containsKey(username)) {
                    UserSession oldSession = sessionMap.get(username);

                    // 仅当IP不同时才触发异地登录
                    if (!oldSession.getIp().equals(ip)) {
                        System.out.println(username + "异地登录，登录ip：" + ip);

                        // 使用安全方式使旧会话失效
                        invalidateSessionSafely(oldSession.getHttpSession());
                    }
                    // 清理旧会话引用
                    sessionMap.remove(username);
                }

                // 保存当前会话
                session.setAttribute("username", user);
                sessionMap.put(username, new UserSession(sessionId, ip,
                        new java.sql.Date(System.currentTimeMillis()), session));
            }

            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }

        else {
            // 登录失败,
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");

        }
//        else {
//            System.out.println("这是什么情况");
//        }

//        else if(user != null && !checkCodeGen.equalsIgnoreCase(checkCode)){
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("codefalse");
//
//        }




    }


    //查询用户名
    public void selectName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //处理请求乱码问题

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        if (username!=null){
            String s = JSON.toJSONString(username);

            User user = JSON.parseObject(s, User.class);
            user.setAge(888);
            user.setPassWord("");
            user.setSex("");
            String s1 = JSON.toJSONString(user);
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(s1);
        }



    }


    //最高权限人员查询用户信息除了自己的
    public void userNoMe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //从session中获取登录用户的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();
        int level = user1.getLevel();

        //调用service
        List<Map<String, Object>> maps = userService.userNoMe(id,level);
        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> stringObjectMap = maps.get(i);
            for (int j = 0; j < stringObjectMap.size(); j++) {
                //将密码设置为空返回数据给前端
               stringObjectMap.put("passWord", "");

            }

        }
       //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //新增管理员时检测登录名是否重复
    public void selectUserExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的用户名
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String username = bufferedReader.readLine();
        //调用service
        boolean b = userService.selectUserExist(username);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }


    //超级管理员更改用户信息的时候验证用户名是否重复，自身除外
    public void selectUserExistUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的用户名
        request.setCharacterEncoding("utf8");
        BufferedReader bufferedReader = request.getReader();
        String userName = bufferedReader.readLine();
        String id = request.getParameter("id");
        //调用service
        boolean b = userService.selectUserExistUpdate(Integer.parseInt(id),userName);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //管理员修改用户信息
    public void adminUpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        User user = JSON.parseObject(s, User.class);
        //调用service
        userService.adminUpdateUser(user);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //用户查询自己的信息：姓名、年龄、性别、部门
    public void userSelectPersonal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        int id = user.getId();

        //调用service
        List<Map<String,Object>> users = userService.userSelectPersonal(id);

        //转化为JSON数据
        String s1 = JSON.toJSONString(users);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //用户更新个人基本信息：姓名、年龄、性别
    public void userUpdatePersonal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        User user = JSON.parseObject(s, User.class);
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s1 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s1, User.class);
        int id = user1.getId();
        user.setId(id);

        //调用service
        userService.userUpdatePersonal(user);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //检测输入的用户名密码与此时登录的用户名密码是否一致
    public void isRight(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的用户名与密码
        request.setCharacterEncoding("utf8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //获取登录的用户名与密码
        HttpSession session = request.getSession();
        Object username1 = session.getAttribute("username");
        String s1 = JSON.toJSONString(username1);
        User user1 = JSON.parseObject(s1, User.class);

        String userName = user1.getUserName();
        String passWord = user1.getPassWord();


        if (username.equals(userName)&& password.equals(passWord)){
            //响应成功标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("success");
        }else {
            //响应失败标识
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("false");
        }

    }


    //根据id修改密码
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的密码
        BufferedReader bufferedReader = request.getReader();
        String passWord = bufferedReader.readLine();
        //获取登录账号的id
        HttpSession session = request.getSession();
        Object username1 = session.getAttribute("username");
        String s1 = JSON.toJSONString(username1);
        User user1 = JSON.parseObject(s1, User.class);
        int id = user1.getId();
        //调用service
        userService.updatePassword(passWord, id);
        //删除登录的session
        session.removeAttribute("username");
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }
    //根据ID修改二级密码
    public void updateSecondaryPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的密码
        BufferedReader bufferedReader = request.getReader();
        String secondaryPassword = bufferedReader.readLine();
        //获取登录账号的ID
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        int id = user.getId();
        //调用service
        userService.updateSecondaryPassword(secondaryPassword,id);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }



    //部门管理人查询自己部门的信息
   public void managerSelectUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
       BufferedReader bufferedReader = request.getReader();
       String id = bufferedReader.readLine();
       String department = request.getParameter("department");

       //调用service
       List<Map<String,Object>> users = userService.managerSelectUser(Integer.parseInt(department), Integer.parseInt(id));
       //转化为json数据
       String s = JSON.toJSONString(users);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);

   }

    //部门管理人查询自己部门的信息(不用传参数)
    public void managerSelectUserNoString(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        //获取登录账号的id
        HttpSession session = request.getSession();
        Object username1 = session.getAttribute("username");
        String s1 = JSON.toJSONString(username1);
        User user1 = JSON.parseObject(s1, User.class);
        int id = user1.getId();
        int department = user1.getDepartment();

        //调用service
        List<Map<String,Object>> users = userService.managerSelectUser(department, id);

        for (int i = 0; i < users.size(); i++) {
            users.get(i).remove("passWord");
        }
        //转化为json数据
        String s = JSON.toJSONString(users);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //根据userid查询该用户所拥有的模块
    public void selectModuleByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的用户ID
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        List<Module> modules = userService.selectModuleByUserId(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(modules);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据userID查询该用户所拥有的功能
    public void  selectUserFunctionByUserId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的用户ID
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();

        //调用service
        List<Function> functions = userService.selectUserFunctionByUserId(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(functions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //根据用户ID查询用户的级别
    public void selectLevelById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的ID
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        int i = userService.selectLevelById(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+i+"");
    }

    //查询所有的功能。一级
    public void  selectAllModule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Module> modules = userService.selectAllModule();
        //转化为json数据
        String s = JSON.toJSONString(modules);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询所有的功能。三级
    public void  selectAllFunctionThree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //调用service
        List<FunctionTwo> functionTwos = userService.selectAllFunctionThree();
        //转化为json数据
        String s1 = JSON.toJSONString(functionTwos);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（一级），根据部门ID查询
    public void  selectBZModule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Module> modules = userService.selectBZModule(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(modules);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //部长查询所有的功能，包括自己部门的所有功能和通用功能（二级），根据部门ID查询
    public void  selectBZFunction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
      //获取前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<Function> functions = userService.selectBZFunction(Integer.parseInt(s));
        //转化为json数据
        String s1 = JSON.toJSONString(functions);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //部长查询所有的功能，包括自己部门的所有功能和通用功能（三级），根据部门ID查询
    public void selectBZFunctionTwo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        List<FunctionTwo> functionTwos = userService.selectBZFunctionTwo(Integer.parseInt(s));
        //转化为JSON数据
        String s1 = JSON.toJSONString(functionTwos);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }


    //查询该部门下是否有人员存在
    public void selectIfUserDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //调用service
        boolean b = userService.selectIfUserDepartment(Integer.parseInt(s));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(b));
    }

    //删除session
    public void deleteSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("username");
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //验证用户是否登录
    public void ifLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1、判断session中是否有user
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");



        //2、判断user是否为null,如果不为null说明用户登录过了
        response.setContentType("text/json;charset=utf-8");
        if (user!=null){
            String s = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s, User.class);
            String userName = user1.getUserName();
            response.getWriter().write(""+userName+"");

        }else {
            response.getWriter().write("false");
        }
    }

    //查询当前登录人的id（在将申请数据储存在本地中使用）
    public void selectLoginId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //处理请求乱码问题

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        if (username!=null){
            String s = JSON.toJSONString(username);
            User user = JSON.parseObject(s, User.class);
            int id = user.getId();

            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(""+id+"");

        }else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write("fail");
        }

    }



    //根据用户ID查询保留时间
    public void selectExitTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String userId = bufferedReader.readLine();
        //调用service
        Map<String, Object> map = userService.selectExitTime(Integer.parseInt(userId));
        //转化为json数据
        String s = JSON.toJSONString(map);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询该部门下是否已经存在部长级别的账号了
    public void selectLevelTwoExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String department = bufferedReader.readLine();
        //调用service
        boolean b = userService.selectLevelTwoExist(Integer.parseInt(department));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }


    //查询该部门下是否已经存在部长级别的账号了
    public void updateLevelTwoExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String department = bufferedReader.readLine();

        String id = request.getParameter("id");
        //调用service
        boolean b = userService.updateLevelTwoExist(Integer.parseInt(department), Integer.parseInt(id));

        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }


    //根据用户id查询用户名，名称，部门
    public void selectDepartById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = userService.selectDepartById(Integer.parseInt(id));
        //转化为json数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //重置密码为默认状态
    public void resetPassWord(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        userService.resetPassWord(Integer.parseInt(id));
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }


    //根据用户ID以及用户输入的二级密码来判断二级密码是否正确
    public void secondaryPasswordExamine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String secondary = bufferedReader.readLine();
        //获取登录的用户ID
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();
        //调用service
        boolean b = userService.secondaryPasswordExamine(id, secondary);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");
    }

    //测试30秒是否有用
    public void executeLongQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        userService.executeLongQuery();
    }


    //根据多个用户ID查询
    public void selectDepartByIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        List<ApplicationForm> applicationForms = JSONArray.parseArray(s, ApplicationForm.class);
        //调用service
        List<Map<String, Object>> maps = userService.selectDepartByIds(applicationForms);
        //转化为json数据
        String s1 = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    public void asasa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        HttpSession session = request.getSession();
        Object token = session.getAttribute("TOKEN");
        System.out.println(session.getAttributeNames().toString());
        System.out.println(token.toString());
    }


    //查询已经登录的是否是管理员账号
    public void ifAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s = JSON.toJSONString(username);
        User user = JSON.parseObject(s, User.class);
        if(user!=null){
            int id = user.getId();

            //调用service
            List<Map<String,Object>> users = userService.userSelectPersonal(id);
            String level = users.get(0).get("level").toString();
            System.out.println(level);
            if (Integer.parseInt(level)<2){
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(""+true+"");
            }
            else {
                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(""+false+"");
            }
        }



    }


    // 查询用户信息（根据您的表结构）
    public void  selectUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map<String, Object> map = userService.selectUserInfo(Integer.parseInt(s));
        String s1 = JSON.toJSONString(map);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);
    }

    //查询所以的用户信息
    public void selectAllUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //调用service
        List<Map<String, Object>> users = userService.selectAllUser();
        //转化为json数据
        String s = JSON.toJSONString(users);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

}


