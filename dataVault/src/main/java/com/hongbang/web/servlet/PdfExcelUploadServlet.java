package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.PdfExcel;
import com.hongbang.pojo.User;
import com.hongbang.service.PdfExcelService;
import com.hongbang.service.impl.PdfExcelServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@WebServlet("/pdfExcelUploadServlet")
public class PdfExcelUploadServlet extends HttpServlet {
     PdfExcelService pdfExcelService = new PdfExcelServiceImpl();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


//        设置编码方式
        request.setCharacterEncoding("utf-8");


//        设置输出
        PrintWriter outprint = response.getWriter();

        //接收前端传来的数据
        String bomTitleId = request.getParameter("bomTitleId");


//        设置文件目录
        String webroot = this.getServletContext().getRealPath("/pdf&excel");
        File temppath = new File(webroot + "fileuploadtemp");
        String dir = webroot;
        File path = new File(webroot );
        if (!temppath.exists()) {
            temppath.mkdirs();
        }
        if (!path.exists()) {
            path.mkdirs();
        }


//      设置文件类型(后期可新增文件类型)
        String[] type = new String[]{".pdf",".PDF",".xls",".XLS",".xlsx",".XLSX",".csv",".CSV"};

//      创建文件项工厂
        DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,
                temppath);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(1024 * 1024 * 20);
        String smallUrll = null;

        try {
            response.setCharacterEncoding("utf-8");
            List<FileItem> fileItems = upload.parseRequest(request);
            Iterator<FileItem> it = fileItems.iterator();
            //获取服务器的时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = LocalDateTime.now().format(formatter);


            //设置新的jsonarry
           List<PdfExcel> publicFiles = new ArrayList<>();
//            遍历request file
            while (it.hasNext()) {
                //设置新的publicNotice
                PdfExcel pdfExcel = new PdfExcel(0,0,null,null,null,0,null,1.06950000,0,0,1.5,0,false,false,0,0);

                FileItem fi = it.next();

                    //查询登录人
                    //查询现在登录的人的id
                    HttpSession session = request.getSession();
                    Object username = session.getAttribute("username");
                    String s = JSON.toJSONString(username);
                    User user = JSON.parseObject(s, User.class);
                    if(user!=null){
                        int id = user.getId();
                        pdfExcel.setUser(id);
                    }

                    pdfExcel.setBomTitleId(Integer.parseInt(bomTitleId));


                    System.out.println(formattedTime);
                    pdfExcel.setTime(formattedTime);
                    InputStream in = fi.getInputStream();
                    String name = fi.getName();//获得文件原名
                    System.out.println(name);
                    pdfExcel.setName(name);

//                    得到文件后缀名
                    int index = name.lastIndexOf(".");
                    String endWith = name.substring(index);


                    long size = fi.getSize();//文件大小

                    double sizeInKB = size / 1024.0;


//                    判断是否符合类型
                    boolean TypeExists = Arrays.asList(type).contains(endWith);
                    if (!TypeExists) {
                        outprint.print("\n" +
                                "alert(\"文件类型错误，只允许jpg,png,jpeg,gif\");\n"
                        );
                        return;
                    }

                    String newFimeName = System.currentTimeMillis() + endWith;//新文件名


                    // 调用service进行添加


//                    for (int i = 0; i < formData.length; i++) {
//                        System.out.println(formData[i]);
//                    }
//                    System.out.println(params);
//                    Product product = JSON.parseObject(params, Product.class);
//                    productService.update(product);
//                    创建上传文件
                    FileOutputStream out = new FileOutputStream(new File(
                            dir + "/" + newFimeName));
                    smallUrll = "pdf&excel/" + newFimeName;
                    pdfExcel.setUrl(smallUrll);
                    pdfExcel.setType(endWith);


                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);//写入大小
                    }
                    in.close();
                    out.close();
                    fi.delete();
//                    outprint.print("<script>\n" +
//                            "alert(\"上传成功\");window.location.href = document.referrer;\n" +
//                            "</script>");
                    //设置新的publicfile


                    System.out.println(pdfExcel);
                publicFiles.add(pdfExcel);


            }

            //调用service
            pdfExcelService.addPdfExcel(publicFiles);

            //3、响应成功标识
            response.getWriter().write("success");
        } catch (FileUploadException e) {
            response.getWriter().write(e.toString());
        }






    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

