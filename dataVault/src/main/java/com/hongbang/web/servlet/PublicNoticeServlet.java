package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.Announcement;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.PublicFile;
import com.hongbang.pojo.PublicNotice;
import com.hongbang.service.PublicFileService;
import com.hongbang.service.PublicNoticeService;
import com.hongbang.service.impl.PublicFileServiceImpl;
import com.hongbang.service.impl.PublicNoticeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/publicNotice/*")
public class PublicNoticeServlet extends BaseServlet {
    PublicNoticeService publicNoticeService = new PublicNoticeServiceImpl();
    PublicFileService publicFileService = new PublicFileServiceImpl();


    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的页码
//        String currentPage = request.getParameter("currentPage");
//        int pageSize = 8;
        PageBean<PublicNotice> pageBean = publicNoticeService.selectAll();
//设置数据
        List<Announcement> announcements = new ArrayList<>();

        for (int i = 0; i < pageBean.getRows().size(); i++) {
            PublicNotice publicNotice = pageBean.getRows().get(i);
            //查询文件信息
            int id = publicNotice.getId();
            List<PublicFile> publicFiles = publicFileService.selectFile(id);
            Announcement announcement = new Announcement(publicNotice,publicFiles);

            announcements.add(announcement);

        }

        PageBean<Announcement> pageBean1 = new PageBean<>();
        pageBean1.setRows(announcements);
        pageBean1.setTotalCount(pageBean.getTotalCount());
        //转化为json数据
        String s = JSON.toJSONString(pageBean1);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }
}
