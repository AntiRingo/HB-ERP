package com.hongbang.web.servlet;

import com.hongbang.pojo.PublicFile;
import com.hongbang.pojo.PublicNotice;
import com.hongbang.service.PublicNoticeService;
import com.hongbang.service.impl.PublicNoticeServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@WebServlet("/publicUploadServlet")
public class PublicUploadServlet extends HttpServlet {
     PublicNoticeService publicNoticeService = new PublicNoticeServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


//        设置编码方式
        request.setCharacterEncoding("utf-8");


//        设置输出
        PrintWriter outprint = response.getWriter();

//        设置文件目录
        String webroot = this.getServletContext().getRealPath("/upload");
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
        String[] type = new String[]{".jpg", ".png", ".jpeg", ".gif",".JPG", ".PNG", ".JPEG", ".GIF",".pdf",".PDF",".doc",".DOC",".xls",".XLS",".txt",".TXT",".docx",".DOCX",".xlsx",".XLSX"};

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
            //设置新的publicNotice
            PublicNotice publicNotice = new PublicNotice(0,null,null,null,null,null);
            //设置新的jsonarry
           List<PublicFile> publicFiles = new ArrayList<>();
//            遍历request file
            while (it.hasNext()) {
                FileItem fi = it.next();
//                判断该表单为普通表单类型


                if (fi.isFormField()) {
                    System.out.println("字段名：" + fi.getFieldName());//
                    System.out.println("字段值：" +  fi.getString("UTF-8"));//id的数值
                    String fieldName = fi.getFieldName();
                    String string = fi.getString("UTF-8");
                    if (fieldName.equals("title")){
                        publicNotice.setTitle(string);
                    }
                    else if (fieldName.equals("department")){
                        publicNotice.setDepartment(string);
                    }
                    else if (fieldName.equals("content")){
                        publicNotice.setContent(string);
                    }
                    else if (fieldName.equals("priority")){
                        publicNotice.setPriority(string);
                    }


                }
                else {
                    InputStream in = fi.getInputStream();
                    String name = fi.getName();//获得文件原名

//                    得到文件后缀名
                    int index = name.lastIndexOf(".");
                    String endWith = name.substring(index);


                    long size = fi.getSize();//文件大小

                    double sizeInKB = size / 1024.0;
                    String s = String.valueOf(sizeInKB);

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
                    smallUrll = "upload/" + newFimeName;


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
                    PublicFile publicFile = new PublicFile(0,0,name,s,endWith,smallUrll);

                    publicFiles.add(publicFile);


                }
            }

            //调用service
            publicNoticeService.addPublicNotice(publicNotice,publicFiles);

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

