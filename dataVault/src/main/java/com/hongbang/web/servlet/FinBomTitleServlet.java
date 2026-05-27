package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.*;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/finBomTitle/*")
public class FinBomTitleServlet extends BaseServlet {
    FinBomTitleService finBomTitleService = new FinBomTitleServiceImpl();
    DepartmentService departmentService = new DepartmentServiceImpl();
    UserService userService = new UserServiceImpl();
    BomPurviewService bomPurviewService = new BomPurviewServiceImpl();
    AuthorUserService authorUserService = new AuthorUserServiceImpl();


    //根据productId查询BOM表标题信息
    public void selectBomTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId = bufferedReader.readLine();
        //调用service
        List<FinBomTitle> finBomTitles = finBomTitleService.selectBomTitle(Integer.parseInt(finProductId));
        //转化为JSON数据
        String s = JSON.toJSONString(finBomTitles);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //插入BOM表标题信息
    public void addBomTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON数据序列化
        FinBomTitle finBomTitle = JSON.parseObject(s, FinBomTitle.class);
        //调用service
        //查询现在登录的人的id
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String a = JSON.toJSONString(username);
        User user = JSON.parseObject(a, User.class);
        int id = user.getId();
        finBomTitleService.addBomTitle(finBomTitle,id);

        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //查询物料号标题和BOM表信息
    public void selectBomTitleAndProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String finProductId= bufferedReader.readLine();
        //调用service
        List<Map<String, Object>> maps = finBomTitleService.selectBomTitleAndProduct(Integer.parseInt(finProductId));
        //转化为JSON数据
        String s = JSON.toJSONString(maps);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //删除BOM表标题,一级BOM表
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        finBomTitleService.delete(Integer.parseInt(id));
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //修改BOM表列表信息
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        //JSON序列化
        FinBomTitle finBomTitle = JSON.parseObject(s, FinBomTitle.class);
        //调用service
        finBomTitleService.update(finBomTitle);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");

    }

    //获取数据进行回显
    public void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String id = bufferedReader.readLine();
        //调用service
        FinBomTitle finBomTitle = finBomTitleService.selectById(Integer.parseInt(id));
        //转化为JSON数据
        String s = JSON.toJSONString(finBomTitle);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    //搜索功能
   public void  search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
       request.setCharacterEncoding("utf-8");
       BufferedReader bufferedReader = request.getReader();
       String str = bufferedReader.readLine();
       String currentPage = request.getParameter("currentPage");
       int pageSize = 20;
       //调用service
       PageBean<FinBomTitle> search = finBomTitleService.search(Integer.parseInt(currentPage), pageSize, str);
       //转化为json数据
       String s = JSON.toJSONString(search);
       //响应数据
       response.setContentType("text/json;charset=utf-8");
       response.getWriter().write(s);
   }

    //查询所有的BOM表标题
    public void selectAllBomTitle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String currentPage = request.getParameter("currentPage");
        int pageSize = 20;
        //调用service
        PageBean<FinBomTitle> pageBean = finBomTitleService.selectAllBomTitle(Integer.parseInt(currentPage), pageSize);
        //转化为数据
        String s = JSON.toJSONString(pageBean);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //根据物料号或者物料名称查询
    public void  selectAllByProductName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        request.setCharacterEncoding("utf-8");
        BufferedReader bufferedReader = request.getReader();
        String str = bufferedReader.readLine();
        String currentPage = request.getParameter("currentPage");
        int pageSize = 20;
        //调用service
        PageBean<FinBomTitle> search = finBomTitleService.selectAllByProductName(Integer.parseInt(currentPage), pageSize, str);
        //转化为json数据
        String s = JSON.toJSONString(search);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }

    //查询每个BOM表的权限
    public void bomPurview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的BOM表id
        BufferedReader bufferedReader = request.getReader();
        String bomTitleId = bufferedReader.readLine();

        //查询作者
        FinBomTitle finBomTitle = finBomTitleService.selectById(Integer.parseInt(bomTitleId));
        Integer author = finBomTitle.getAuthor();//作者
        //查询作者可以控制的人分为几个部门
        List<Department> departments = authorUserService.selectAllDepart(author);
        //查询BOM表可以查看的部门以及用户


        List<BomUserPurview> bomUserPurviews = new ArrayList<>(departments.size());

        for (int i = 0; i < departments.size(); i++) {
            Department department = departments.get(i);



            //查询该部门下的所有用户信息
            List<User> users = userService.selectDepartmentUser(department.getId());

            if (users.size()>0){
                //查询作者拥有的管理的所有人
                List<AuthorUser> authorUserList = authorUserService.selectNowAuthor(author);

                List<Members> members = new ArrayList<>(users.size());
                for (int j = 0; j < users.size(); j++) {
                    int id = users.get(j).getId();
                    for (int k = 0; k < authorUserList.size(); k++) {
                        int userId = authorUserList.get(k).getUserId();

                        if (id==userId){
                            Members members1 = new Members(users.get(j).getId(),users.get(j).getName(),false,false,0);
                            members.add(members1);
                        }

                    }

                }
                //查询该BOM表下的权限
                List<BomPurview> list = bomPurviewService.selectUserPurview(Integer.parseInt(bomTitleId));
                if (list.size()>0){
                    for (int j = 0; j < list.size(); j++) {
                        Integer integer = list.get(j).getUserPurview();
                        int canEdit = list.get(j).getCanEdit();
                        for (int k = 0; k < members.size(); k++) {
                            Members members1 = members.get(k);
                            int id = members1.getId();
                            if (id==integer){
                                members1.setSelected(true);
                                if (canEdit==1){
                                    members1.setCanEdit(true);
                                }

                            }
                        }
                    }
                }
                BomUserPurview bomUserPurview =  new BomUserPurview(department.getId(),department.getDepartmentName(),members);
                bomUserPurviews.add(bomUserPurview);
            }






        }



        //转化为json数据
        String s = JSON.toJSONString(bomUserPurviews);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }

    //查询是否是作者
    public void selectIsAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
         BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        String s2 = JSON.toJSONString(username);
        User user1 = JSON.parseObject(s2, User.class);
        int id = user1.getId();

        //调用service
        boolean b = finBomTitleService.selectIsAuthor(id,Integer.parseInt(s));
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(""+b+"");

    }
}
