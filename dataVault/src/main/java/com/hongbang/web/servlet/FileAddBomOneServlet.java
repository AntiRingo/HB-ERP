package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.FinProduct;
import com.hongbang.pojo.Product;
import com.hongbang.pojo.User;
import com.hongbang.service.FinBomService;
import com.hongbang.service.FinBomTitleService;
import com.hongbang.service.FinProductService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.FinBomServiceImpl;
import com.hongbang.service.impl.FinBomTitleServiceImpl;
import com.hongbang.service.impl.FinProductServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;
import com.hongbang.util.FileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/fileAddBomOne")
public class FileAddBomOneServlet extends HttpServlet {


    FinProductService finProductService = new FinProductServiceImpl();
    ProductService productService = new ProductServiceImpl();
    FinBomService finBomService = new FinBomServiceImpl();
    FinBomTitleService finBomTitleService = new FinBomTitleServiceImpl();


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
            String finId = request.getParameter("finId");//得到物料id。判断上传的是否是当前物料的BOM表
            String bomTitleId1 = request.getParameter("bomTitleId");


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
                                    JSONArray bomContent = new JSONArray();


                                    try {

                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));

                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        int columns = sheets[0].getColumns();//列数
                                        int rows = sheets[0].getRows();//行数
                                        System.out.println("行数"+columns);
                                        System.out.println("列数"+rows);

                                        int wlhRows = 0;
                                        int wlhColumns = 0;
                                        int bhColumns = 0;
                                        boolean a = true;
                                        boolean c = true;
                                        //查询所有的物料号
                                        for (int i = 0; i < columns; i++) {
                                                //找到存放物料号的那一列
                                                for (int j = 0; j < rows; j++) {
                                                    //寻找物料号的那一列
                                                    Cell cell = sheets[0].getCell(i, j);
                                                    String trim1 = cell.getContents().trim();
                                                    System.out.println(trim1);
                                                    if (trim1.length()>0 && trim1.equals("物料号")){
                                                        System.out.println(wlhRows);
                                                        System.out.println(wlhColumns);
                                                        //找到了哪一列是物料号，去寻找对应的数量信息,以及标号信息
                                                        wlhRows = j+1;
                                                        wlhColumns = i;
                                                        a=false;


                                                    }
                                                    else if (trim1.length()>0 && trim1.equals("标号")){
                                                        bhColumns = i;
                                                        c = false;
                                                    }
                                                }

                                               if (!a &&!c){
                                                   break;
                                               }


                                        }
                                        if (!a && !c){

                                            System.out.println("这里了吗");
//                                            得到了物料号所在的行列，去找对应的行列和标号
                                            System.out.println("第"+wlhRows+"行");
                                            System.out.println("第"+wlhColumns+"列");
                                            boolean b = true;
                                            for (int i = wlhRows; i < rows; i++) {
                                                Cell wlh = sheets[0].getCell(wlhColumns, i);//物料号
                                                Cell bh = sheets[0].getCell(bhColumns, i);//标号
                                                System.out.println(wlh.getContents());
                                                System.out.println(bh.getContents());
                                                Cell zj = sheets[0].getCell(wlhColumns + 3, i);//注解
                                                System.out.println(zj.getContents());

                                                //判断是否为空（物料号，所需数量，标号都不能为空）
                                                if ((wlh.getContents().length()!=0 && bh.getContents().length()==0) || (wlh.getContents().length()==0 && bh.getContents().length()!=0) ){

                                                    //三个其中一个为空的时候就报错
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在excel表中，物料号、标号都不可以为空，请确认后再试！");
                                                    FileUtil.delFile(filePath);
                                                    b = false;
                                                    break;
                                                }
                                                else if (wlh.getContents().length()!=0 && bh.getContents().length()!=0){

                                                    //两个都不为空
                                                    //1.查询根据物料号是否能查出信息
                                                    List<Product> products = productService.selectProductByMaterialNumber(wlh.getContents());
                                                    List<FinProduct> finProducts = finProductService.selectProductByMaterialNumber(wlh.getContents());

                                                    if (products.size()>0 || finProducts.size()>0){
                                                        //物料号存在
                                                        JSONObject object = new JSONObject();
                                                        object.put("id","");
                                                        object.put("bomTitleId", bomTitleId1);
                                                        object.put("status",1);

                                                        if (products.size()>0){

                                                            object.put("productId",products.get(0).getId());
                                                            object.put("vault",0);
                                                        }
                                                        else {
                                                            object.put("productId",finProducts.get(0).getId());
                                                            object.put("vault",1);
                                                        }


//                                                        int count = bh.getContents().trim().split("\\s+").length;
                                                        int count = FileUtil.calculateTotalQuantity(String.valueOf(bh.getContents()));
                                                        object.put("number",count);
                                                        object.put("partNumber", bh.getContents());
                                                        object.put("notes", zj.getContents());
                                                        bomContent.add(object);
                                                    }
                                                    else {
                                                        //不存在
                                                        response.setContentType("text/json;charset=utf-8");
                                                        response.getWriter().write("在数据库中，物料号"+wlh.getContents()+"对应的物料信息并未找到，请确认后再试！");
                                                        FileUtil.delFile(filePath);
                                                        b = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (b){

                                                //导入数据
                                                //获取到了所有的数据，进行BOM表内容的添加
                                                String s = bomContent.toJSONString();
                                                List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);
                                                System.out.println(finBoms);
                                                if (finBoms.size()>0){

                                                    finBomService.add(finBoms, Integer.parseInt(bomTitleId1));
                                                    boolean b1 = finBomTitleService.selectIfAuthor(Integer.parseInt(bomTitleId1));
                                                    if (!b1){
                                                        //查询当前登录的用户信息
                                                        HttpSession session = request.getSession();
                                                        Object username = session.getAttribute("username");
                                                        String s2 = JSON.toJSONString(username);
                                                        User user1 = JSON.parseObject(s2, User.class);
                                                        int id = user1.getId();
                                                        finBomTitleService.updateAuthor(id, Integer.parseInt(bomTitleId1));
                                                    }

                                                    System.out.println("成功了，啊");
                                                    //成功导入
                                                    //查询是否该BOM表已经有作者了，如果没有就更新作者

                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("导入成功！");
                                                    FileUtil.delFile(filePath);
                                                }
                                                else {
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel表中不存在可插入的数据！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }

                                            }

                                        }





                                        workbook.close();

                                    }
                                    catch (BiffException | IOException e) {
//                                    e.printStackTrace();
                                        FileUtil.delFile(filePath);
                                    }

                                }


                                //判断文件名是否重复
                                else{
                                    FileUtil.delFile(filePath);
                                    // 在控制台输出文件的上传路径
                                    System.out.println(filePath);
                                    // 保存文件到硬盘
                                    item.write(storeFile);
//                        fileName = URLEncoder.encode(fileName, "utf-8");
                                    Workbook workbook;


                                    JSONArray bomContent = new JSONArray();

                                    try {
                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));
                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        int columns = sheets[0].getColumns();//列数
                                        int rows = sheets[0].getRows();//行数
                                        System.out.println("行数"+columns);
                                        System.out.println("列数"+rows);

                                        int wlhRows = 0;
                                        int wlhColumns = 0;
                                        int bhColumns = 0;
                                        boolean a = true;
                                        boolean c = true;
                                        //查询所有的物料号
                                        for (int i = 0; i < columns; i++) {
                                            //找到存放物料号的那一列
                                            for (int j = 0; j < rows; j++) {
                                                //寻找物料号的那一列
                                                Cell cell = sheets[0].getCell(i, j);
                                                String trim1 = cell.getContents().trim();
                                                System.out.println(trim1);
                                                if (trim1.length()>0 && trim1.equals("物料号")){
                                                    System.out.println(wlhRows);
                                                    System.out.println(wlhColumns);
                                                    //找到了哪一列是物料号，去寻找对应的数量信息,以及标号信息
                                                    wlhRows = j+1;
                                                    wlhColumns = i;
                                                    a=false;


                                                }
                                                else if (trim1.length()>0 && trim1.equals("标号")){
                                                    bhColumns = i;
                                                    c = false;
                                                }
                                            }

                                            if (!a &&!c){
                                                break;
                                            }


                                        }
                                        if (!a && !c){
                                            //得到了物料号所在的行列，去找对应的行列和标号
//                                            System.out.println("第"+wlhRows+"行");
//                                            System.out.println("第"+wlhColumns+"列");
                                            boolean b = true;
                                            for (int i = wlhRows; i < rows; i++) {
                                                Cell wlh = sheets[0].getCell(wlhColumns, i);//物料号
                                                Cell bh = sheets[0].getCell(bhColumns, i);//标号
                                                Cell zj = sheets[0].getCell(wlhColumns + 3, i);//注解

                                                //判断是否为空（物料号，所需数量，标号都不能为空）
                                                if ((wlh.getContents().length()!=0 && bh.getContents().length()==0) || (wlh.getContents().length()==0 && bh.getContents().length()!=0) ){

                                                    //三个其中一个为空的时候就报错
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在excel表中，物料号、标号都不可以为空，请确认后再试！");
                                                    FileUtil.delFile(filePath);
                                                    b = false;
                                                    break;
                                                }
                                                else if (wlh.getContents().length()!=0 && bh.getContents().length()!=0){

                                                    //两个都不为空
                                                    //1.查询根据物料号是否能查出信息
                                                    List<Product> products = productService.selectProductByMaterialNumber(wlh.getContents());
                                                    List<FinProduct> finProducts = finProductService.selectProductByMaterialNumber(wlh.getContents());

                                                    if (products.size()>0 || finProducts.size()>0){
                                                        //物料号存在
                                                        JSONObject object = new JSONObject();
                                                        object.put("id","");
                                                        object.put("bomTitleId", bomTitleId1);
                                                        object.put("status",1);

                                                        if (products.size()>0){

                                                            object.put("productId",products.get(0).getId());
                                                            object.put("vault",0);
                                                        }
                                                        else {
                                                            object.put("productId",finProducts.get(0).getId());
                                                            object.put("vault",1);
                                                        }


//                                                        int count = bh.getContents().trim().split("\\s+").length;
                                                        int count = FileUtil.calculateTotalQuantity(String.valueOf(bh.getContents()));

                                                        object.put("number",count);
                                                        object.put("partNumber", bh.getContents());
                                                        object.put("notes", zj.getContents());
                                                        bomContent.add(object);
                                                    }
                                                    else {
                                                        //不存在
                                                        response.setContentType("text/json;charset=utf-8");
                                                        response.getWriter().write("在数据库中，物料号"+wlh.getContents()+"对应的物料信息并未找到，请确认后再试！");
                                                        FileUtil.delFile(filePath);
                                                        b = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (b){

                                                //导入数据
                                                //获取到了所有的数据，进行BOM表内容的添加
                                                String s = bomContent.toJSONString();
                                                List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);
                                                if (finBoms.size()>0){
                                                    finBomService.add(finBoms, Integer.parseInt(bomTitleId1));
                                                    boolean b1 = finBomTitleService.selectIfAuthor(Integer.parseInt(bomTitleId1));
                                                    if (!b1){
                                                        //查询当前登录的用户信息
                                                        HttpSession session = request.getSession();
                                                        Object username = session.getAttribute("username");
                                                        String s2 = JSON.toJSONString(username);
                                                        User user1 = JSON.parseObject(s2, User.class);
                                                        int id = user1.getId();
                                                        finBomTitleService.updateAuthor(id, Integer.parseInt(bomTitleId1));
                                                    }
                                                    //成功导入
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("导入成功！");
                                                    FileUtil.delFile(filePath);
                                                }
                                                else {
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel表中不存在可插入的数据！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }

                                            }

                                        }





                                        workbook.close();

                                    }
                                    catch (BiffException | IOException e) {
//                                    e.printStackTrace();
                                        FileUtil.delFile(filePath);
                                    }



                                }
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


