
package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.Product;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.ProductServiceImpl;
import com.hongbang.util.FileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@WebServlet("/fileLinShi")
public class FileLinShiServlet extends HttpServlet {





    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 2000; // 2gb
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 2000; // 2gb

    /**
     * 上传数据及保存文件
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProductService productService = new ProductServiceImpl();

        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

//      设置文件类型(后期可新增文件类型)
        String[] type = new String[]{".xls"};
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        int index = fileName.lastIndexOf(".");
                        String endWith = fileName.substring(index);
                        //判断是否符合类型
                        boolean TypeExists = Arrays.asList(type).contains(endWith);
                        if (!TypeExists){
                            //3.响应数据
                            response.setContentType("text/json;charset=utf-8");
                            response.getWriter().write("type");
                        }else {
                            String filePath = uploadPath + File.separator + fileName;
                            File storeFile = new File(filePath);
//                        System.out.println(filePath);

                            //判断文件名是否重复
                            if (!storeFile.exists()){
                                // 在控制台输出文件的上传路径
                                System.out.println(filePath);
                                // 保存文件到硬盘
                                item.write(storeFile);
//                        fileName = URLEncoder.encode(fileName, "utf-8");
                                Workbook workbook;
                                JSONArray product = new JSONArray();

                                try {
                                    //获取一个Excel文件  只支持.xls格式
                                    workbook = Workbook.getWorkbook(new File(filePath));
                                    //获取到一共有多少个表
                                    Sheet[] sheets = workbook.getSheets();
                                  
                                    int rows = sheets[sheets.length-1].getRows();//行数

                                    // 3. 获取可写副本（jxl修改必须通过WritableWorkbook）
//                                    WritableWorkbook writableWorkbook = Workbook.createWorkbook(
//                                            new File("modified.xls"), workbook); // 保存到服务器本地


                                    //获取写着数量的列
                                    boolean a=true;
                                    for (int i = 2; i < rows; i++) {


                                        Cell cell = sheets[sheets.length-1].getCell(11, i);
                                        String trim = cell.getContents().trim();
//                                        System.out.println(trim);
                                        if (trim.length()>0 ){
                                            //获取物料名称
                                            Cell cell1 = sheets[sheets.length-1].getCell(1, i);
                                            String trim1 = cell1.getContents().trim();
//                                            System.out.println(trim1);
                                            //查询物料号是否存在
                                            if (trim1.length()>0){
                                                List<Product> products = productService.selectProductByName(trim1);

                                                if (products.size() == 1 ){
                                                    if (Integer.parseInt(trim)>0){
//                                                    System.out.println("一个");
                                                        //该物料名称只对应一个物料
                                                        JSONObject object = new JSONObject();
                                                        object.put("number", trim);
                                                        object.put("name",trim1);
                                                        product.add(object);
                                                    }
//

                                                }
                                                else if (products.size()==0){
                                                    System.out.println(trim1);
                                                    System.out.println("未找到");
                                                    a=false;



                                                }

                                                else {
                                                    System.out.println(trim1);
                                                    System.out.println("找到多个");
                                                    a=false;

                                                }
                                            }

                                        }
                                    }

                                    if (a){
                                        //根据物料号更新产品的数量
                                        String s = product.toJSONString();
                                        List<Product> products = JSONArray.parseArray(s, Product.class);
                                        System.out.println(products);
                                        //调用service
                                        productService.updateNumberByName(products);

                                        //响应成功标识
                                        response.setContentType("text/json;charset=utf-8");
                                        response.getWriter().write("success");


                                        workbook.close();
                                        FileUtil.delFile(filePath);
                                    }


                                }
                                catch (BiffException | IOException e) {
//                                    e.printStackTrace();
                                    FileUtil.delFile(filePath);
                                }

                            }


                            //判断文件名是否重复
//                            else{
//                                FileUtil.delFile(filePath);
//                                // 在控制台输出文件的上传路径
//                                System.out.println(filePath);
//                                // 保存文件到硬盘
//                                item.write(storeFile);
////                        fileName = URLEncoder.encode(fileName, "utf-8");
//                                Workbook workbook;
//
//
//                                JSONArray product = new JSONArray();
//
//                                try {
//                                    //获取一个Excel文件  只支持.xls格式
//                                    workbook = Workbook.getWorkbook(new File(filePath));
//                                    //获取到一共有多少个表
//                                    Sheet[] sheets = workbook.getSheets();
//
//                                    int rows = sheets[sheets.length-1].getRows();//行数
//
//                                    // 3. 获取可写副本（jxl修改必须通过WritableWorkbook）
////                                    WritableWorkbook writableWorkbook = Workbook.createWorkbook(
////                                            new File("modified.xls"), workbook); // 保存到服务器本地
//
//
//                                    //获取写着数量的列
//                                    boolean a=true;
//                                    for (int i = 2; i < rows; i++) {
//
//                                        Cell cell = sheets[sheets.length-1].getCell(11, i);
//                                        String trim = cell.getContents().trim();
////                                        System.out.println(trim);
//                                        if (trim.length()>0 ){
//                                            //获取物料名称
//                                            Cell cell1 = sheets[sheets.length-1].getCell(1, i);
//                                            String trim1 = cell1.getContents().trim();
////                                            System.out.println(trim1);
//                                            //查询物料号是否存在
//                                            if (trim1.length()>0){
//                                                List<Product> products = productService.selectProductByName(trim1);
//
//                                                if (products.size() == 1 ){
//                                                    if (Integer.parseInt(trim)>0){
////                                                    System.out.println("一个");
//                                                        //该物料名称只对应一个物料
//                                                        JSONObject object = new JSONObject();
//                                                        object.put("number", trim);
//                                                        object.put("name",trim1);
//                                                        product.add(object);
//                                                    }
////
//
//                                                }
//                                                else if (products.size()==0){
//                                                    System.out.println(trim1);
//                                                    System.out.println("未找到");
//                                                    a=false;
//
//
//
//                                                }
//
//                                                else {
//                                                    System.out.println(trim1);
//                                                    System.out.println("找到多个");
//                                                    a=false;
//
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                    if (a){
//                                        //根据物料号更新产品的数量
//                                        String s = product.toJSONString();
//                                        List<Product> products = JSONArray.parseArray(s, Product.class);
//                                        System.out.println(products);
//                                        //调用service
//                                        productService.updateNumberByName(products);
//                                        //响应成功标识
//                                        response.setContentType("text/json;charset=utf-8");
//                                        response.getWriter().write("success");
//
//
//                                        workbook.close();
//                                        FileUtil.delFile(filePath);
//                                    }
//
//
//                                }
//                                catch (BiffException | IOException e) {
////                                    e.printStackTrace();
//                                    FileUtil.delFile(filePath);
//                                }
//
//
//                            }
                        }




                    }
                }
            }
        } catch (Exception ex) {
//            response.setContentType("text/json;charset=utf-8");
//            response.getWriter().write("false");
//            ex.printStackTrace();

        }

    }
}