package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.FinProduct;
import com.hongbang.pojo.Product;
import com.hongbang.service.FinBomService;
import com.hongbang.service.FinProductService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.FinBomServiceImpl;
import com.hongbang.service.impl.FinProductServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;
import com.hongbang.util.FileUtil;
import com.hongbang.util.RegexMatches;
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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/fileAddBom")
public class FileAddBomServlet extends HttpServlet {


    FinProductService finProductService = new FinProductServiceImpl();
    ProductService productService = new ProductServiceImpl();
    FinBomService finBomService = new FinBomServiceImpl();


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
                                    boolean pass=true;

                                    try {
                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));
                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        int columns = sheets[0].getColumns();//列数
                                        int rows = sheets[0].getRows();//行数
                                        //判断物料号是否有重复的
                                        ArrayList<String> array = new ArrayList<>();



                                        //查询excel表中的BOM表物料号和产品名称
                                        String wlNumber = "";//excel中的物料号
                                        String wlName = "";//excel中的物料名
                                        for (int i = 1; i < rows; i++) {
                                            Cell cell1 = sheets[0].getCell(0, i);
                                            String trim = cell1.getContents().trim();
                                            if (trim.length()>0){
                                                wlNumber=trim;
                                                break;
                                            }

                                        }
                                        for (int i = 1; i < rows; i++) {
                                            Cell cell1 = sheets[0].getCell(1, i);
                                            String trim = cell1.getContents().trim();
                                            if (trim.length()>0){
                                                wlName=trim;
                                                break;
                                            }

                                        }


                                        if (wlName.length()>0 && wlNumber.length()>0){
                                            //两个都不为空，根据前端传来的物料id查询物料号一级名称信息进行比较是否相同
                                            List<FinProduct> finProducts = finProductService.selectById(Integer.parseInt(finId));
                                            FinProduct finProduct = finProducts.get(0);
                                            String finMaterialNumber = finProduct.getFinMaterialNumber();
                                            String finProductName = finProduct.getFinProductName();
                                            if (finMaterialNumber.equals(wlNumber) && finProductName.equals(wlName)){


                                                for (int i = 1; i < rows; i++) {
                                                    Cell cell = sheets[0].getCell(2, i);
                                                    String trim = cell.getContents().trim();
                                                    if (trim.length()>0){
                                                        array.add(trim);
                                                    }
                                                }


                                                for (int i = 0; i < array.size(); i++) {

                                                    String s1 = array.get(i);
                                                    for (int j = 0; j < array.size(); j++) {
                                                        if (i!=j){
                                                            String s2 = array.get(j);


                                                            if (s2.length()>0 && s1.length()>0 && s1.equals(s2)){
                                                                pass=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在excel表中，物料号："+s1+"重复，请合并！");
                                                                FileUtil.delFile(filePath);
                                                                break;
                                                            }
                                                        }


                                                    }
                                                    if (!pass){
                                                        break;
                                                    }
                                                }

                                                if (!pass){
                                                    break;
                                                }

                                                //都相同表示要添加的BOM表是该物料下的
                                                //获取要加入BOM表的数据
                                                for (int i = 1; i < rows; i++) {
                                                    Cell cell1 = sheets[0].getCell(2, i);
                                                    String wlNumberBom = cell1.getContents().trim();//要添加的BOM表物料号内容
                                                    Cell cell2 = sheets[0].getCell(3, i);
                                                    String wlNameBom = cell2.getContents().trim();//要添加的BOM表物料名称内容
                                                    Cell cell3 = sheets[0].getCell(4, i);
                                                    String wlCountBom = cell3.getContents().trim();//要添加的BOM表物料所需数量
                                                    //判断三个是否为空
                                                    if (wlNumberBom.length()>0 && wlNameBom.length()>0 && wlCountBom.length()>0){
                                                        //三个都不为空才进行查询判断,先查询数量是否符合要求，然后在零件库中查询，如果没查到再查询产品库

                                                        boolean positiveInteger = RegexMatches.isPositiveInteger(wlCountBom);
                                                        if (positiveInteger){
                                                            List<Product> products = productService.selectProductByMaterialNumber(wlNumberBom);
                                                            if (products.size()>0){
                                                                //查到了,对比名称是否相同
                                                                Product product = products.get(0);
                                                                String name = product.getName();
                                                                if (name.equals(wlNameBom)){
                                                                    //相同，将数据放入数组
                                                                    JSONObject object = new JSONObject();
                                                                    object.put("id","");
                                                                    object.put("bomTitleId", bomTitleId1);
                                                                    object.put("status",1);
                                                                    object.put("productId",product.getId());
                                                                    object.put("vault",0);
                                                                    object.put("number",wlCountBom);
                                                                    bomContent.add(object);
                                                                }
                                                                else{
                                                                    //不相同，报警
                                                                    pass = false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("物料号："+wlNumberBom+"对应的物料名："+wlNameBom+"与在数据库中查询到的不一致！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;
                                                                }
                                                            }
                                                            else {
                                                                //没查到，去产品库查
                                                                List<FinProduct> finProducts1 = finProductService.selectProductByMaterialNumber(wlNumberBom);


                                                                if (finProducts1.size()>0){
                                                                    //产品库查到了
                                                                    FinProduct finProduct1 = finProducts1.get(0);
                                                                    int id = finProduct1.getId();
                                                                    String finProductName1 = finProduct1.getFinProductName();
                                                                    String finMaterialNumber1 = finProduct1.getFinMaterialNumber();
                                                                    //判断这个物料是否和要添加BOM表的物料重复，查询该物料是否有BOM表，如果有查询该BOM表中是否包含当前物料
                                                                    if (finMaterialNumber1.equals(finMaterialNumber)){
                                                                        //物料号相同，要添加的BOM表中存在自己
                                                                        pass=false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("不能在BOM表中添加自己！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }
                                                                    else {
                                                                        //物料号不相同，查看该物料的BOM表中是否包含要添加BOM表的物料
                                                                        List<FinBom> finBoms = finBomService.selectBomVault(id);
                                                                        if (finBoms.size()>0){
                                                                            //BOM表中有内容，查询这些信息是否和要插入BOM表的信息重复
                                                                            for (int j = 0; j < finBoms.size(); j++) {
                                                                                FinBom finBom = finBoms.get(j);
                                                                                int productId = finBom.getProductId();
                                                                                if (productId == Integer.parseInt(finId)){
                                                                                    //id相同,证明该产品的BOM表包含当前产品！
                                                                                    pass=false;
                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                    response.getWriter().write(""+finMaterialNumber1+"的BOM表包含当前要添加的产品信息！");
                                                                                    FileUtil.delFile(filePath);
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }

                                                                    }
                                                                    if (!pass){
                                                                        break;
                                                                    }

                                                                    if (finProductName1.equals(wlNameBom)){
                                                                        //相同，将数据放入数组
                                                                        JSONObject object = new JSONObject();
                                                                        object.put("id","");
                                                                        object.put("bomTitleId", bomTitleId1);
                                                                        object.put("status",1);
                                                                        object.put("productId",finProduct1.getId());
                                                                        object.put("vault",1);
                                                                        object.put("number",wlCountBom);
                                                                        bomContent.add(object);
                                                                    }
                                                                    else{
                                                                        //不相同，报警
                                                                        pass = false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("物料号："+wlNumberBom+"对应的物料名："+wlNameBom+"与在数据库中查询到的不一致！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }
                                                                }
                                                                else {
                                                                    //产品库也没查到
                                                                    pass = false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("物料号："+wlNumberBom+"在数据库中未找到！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;

                                                                }
                                                            }
                                                        }
                                                        else {
                                                            //数量不符合要求
                                                            pass = false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("物料号："+wlNumberBom+"在的数量不符合要求！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }



                                                    }
                                                    else {
                                                        if (wlNumberBom.length() != 0 || wlNameBom.length() != 0 || wlCountBom.length() != 0) {
                                                            int x = i + 1;
                                                            pass = false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("excel中第" + x + "行中，所需信息存在空值！！");
                                                            FileUtil.delFile(filePath);
                                                        }


                                                    }


                                                }
                                                if (!pass){
                                                    break;
                                                }
                                                //获取到了所有的数据，进行BOM表内容的添加
                                                String s = bomContent.toJSONString();
                                                List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);
                                                if (finBoms.size()>0){
                                                    finBomService.add(finBoms, Integer.parseInt(bomTitleId1));
                                                    //成功导入
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("导入成功！");
                                                    FileUtil.delFile(filePath);
                                                }
                                                else {
                                                    pass=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel表中不存在可插入的数据！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }



                                            }
                                            else {
                                                if (!finMaterialNumber.equals(wlNumber)){
                                                    //物料号不相同
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel中的物料号与要添加BOM表的产品物料号不相同！");
                                                    FileUtil.delFile(filePath);
                                                    break;

                                                }else {
                                                    //产品名称不相同
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel中的产品 名称与要添加BOM表的产品名称不相同！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                            }

                                        }
                                        else{
                                            if (wlNumber.length()==0){
                                                response.setContentType("text/json;charset=utf-8");
                                                response.getWriter().write("在excel表，列：物料号中未找到物料号相关数据！");
                                                FileUtil.delFile(filePath);
                                                break;
                                            }
                                            else {
                                                response.setContentType("text/json;charset=utf-8");
                                                response.getWriter().write("在excel表，列：物料名称中未找到物料号相关数据！");
                                                FileUtil.delFile(filePath);
                                                break;
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
                                    boolean pass=true;
                                    try {
                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));
                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        int columns = sheets[0].getColumns();//列数
                                        int rows = sheets[0].getRows();//行数
                                        //判断物料号是否有重复的
                                        ArrayList<String> array = new ArrayList<>();



                                        //查询excel表中的BOM表物料号和产品名称
                                        String wlNumber = "";//excel中的物料号
                                        String wlName = "";//excel中的物料名
                                        for (int i = 1; i < rows; i++) {
                                            Cell cell1 = sheets[0].getCell(0, i);
                                            String trim = cell1.getContents().trim();
                                            if (trim.length()>0){
                                                wlNumber=trim;
                                                break;
                                            }

                                        }
                                        for (int i = 1; i < rows; i++) {
                                            Cell cell1 = sheets[0].getCell(1, i);
                                            String trim = cell1.getContents().trim();
                                            if (trim.length()>0){
                                                wlName=trim;
                                                break;
                                            }

                                        }


                                        if (wlName.length()>0 && wlNumber.length()>0){
                                            //两个都不为空，根据前端传来的物料id查询物料号一级名称信息进行比较是否相同
                                            List<FinProduct> finProducts = finProductService.selectById(Integer.parseInt(finId));
                                            FinProduct finProduct = finProducts.get(0);
                                            String finMaterialNumber = finProduct.getFinMaterialNumber();
                                            String finProductName = finProduct.getFinProductName();
                                            if (finMaterialNumber.equals(wlNumber) && finProductName.equals(wlName)){


                                                for (int i = 1; i < rows; i++) {
                                                    Cell cell = sheets[0].getCell(2, i);
                                                    String trim = cell.getContents().trim();
                                                    if (trim.length()>0){
                                                        array.add(trim);
                                                    }
                                                }


                                                for (int i = 0; i < array.size(); i++) {

                                                    String s1 = array.get(i);
                                                    for (int j = 0; j < array.size(); j++) {
                                                        if (i!=j){
                                                            String s2 = array.get(j);


                                                            if (s2.length()>0 && s1.length()>0 && s1.equals(s2)){
                                                                pass=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在excel表中，物料号："+s1+"重复，请合并！");
                                                                FileUtil.delFile(filePath);
                                                                break;
                                                            }
                                                        }


                                                    }
                                                    if (!pass){
                                                        break;
                                                    }
                                                }

                                                if (!pass){
                                                    break;
                                                }

                                                //都相同表示要添加的BOM表是该物料下的
                                                //获取要加入BOM表的数据
                                                for (int i = 1; i < rows; i++) {
                                                    Cell cell1 = sheets[0].getCell(2, i);
                                                    String wlNumberBom = cell1.getContents().trim();//要添加的BOM表物料号内容
                                                    Cell cell2 = sheets[0].getCell(3, i);
                                                    String wlNameBom = cell2.getContents().trim();//要添加的BOM表物料名称内容
                                                    Cell cell3 = sheets[0].getCell(4, i);
                                                    String wlCountBom = cell3.getContents().trim();//要添加的BOM表物料所需数量
                                                    //判断三个是否为空
                                                    if (wlNumberBom.length()>0 && wlNameBom.length()>0 && wlCountBom.length()>0){
                                                        //三个都不为空才进行查询判断,先查询数量是否符合要求，然后在零件库中查询，如果没查到再查询产品库

                                                        boolean positiveInteger = RegexMatches.isPositiveInteger(wlCountBom);
                                                        if (positiveInteger){
                                                            List<Product> products = productService.selectProductByMaterialNumber(wlNumberBom);
                                                            if (products.size()>0){
                                                                //查到了,对比名称是否相同
                                                                Product product = products.get(0);
                                                                String name = product.getName();
                                                                if (name.equals(wlNameBom)){
                                                                    //相同，将数据放入数组
                                                                    JSONObject object = new JSONObject();
                                                                    object.put("id","");
                                                                    object.put("bomTitleId", bomTitleId1);
                                                                    object.put("status",1);
                                                                    object.put("productId",product.getId());
                                                                    object.put("vault",0);
                                                                    object.put("number",wlCountBom);
                                                                    bomContent.add(object);
                                                                }
                                                                else{
                                                                    //不相同，报警
                                                                    pass = false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("物料号："+wlNumberBom+"对应的物料名："+wlNameBom+"与在数据库中查询到的不一致！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;
                                                                }
                                                            }
                                                            else {
                                                                //没查到，去产品库查
                                                                List<FinProduct> finProducts1 = finProductService.selectProductByMaterialNumber(wlNumberBom);


                                                                if (finProducts1.size()>0){
                                                                    //产品库查到了
                                                                    FinProduct finProduct1 = finProducts1.get(0);
                                                                    int id = finProduct1.getId();
                                                                    String finProductName1 = finProduct1.getFinProductName();
                                                                    String finMaterialNumber1 = finProduct1.getFinMaterialNumber();
                                                                    //判断这个物料是否和要添加BOM表的物料重复，查询该物料是否有BOM表，如果有查询该BOM表中是否包含当前物料
                                                                    if (finMaterialNumber1.equals(finMaterialNumber)){
                                                                        //物料号相同，要添加的BOM表中存在自己
                                                                        pass=false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("不能在BOM表中添加自己！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }
                                                                    else {
                                                                        //物料号不相同，查看该物料的BOM表中是否包含要添加BOM表的物料
                                                                        List<FinBom> finBoms = finBomService.selectBomVault(id);
                                                                        if (finBoms.size()>0){
                                                                            //BOM表中有内容，查询这些信息是否和要插入BOM表的信息重复
                                                                            for (int j = 0; j < finBoms.size(); j++) {
                                                                                FinBom finBom = finBoms.get(j);
                                                                                int productId = finBom.getProductId();
                                                                                if (productId == Integer.parseInt(finId)){
                                                                                    //id相同,证明该产品的BOM表包含当前产品！
                                                                                    pass=false;
                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                    response.getWriter().write(""+finMaterialNumber1+"的BOM表包含当前要添加的产品信息！");
                                                                                    FileUtil.delFile(filePath);
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }

                                                                    }
                                                                    if (!pass){
                                                                        break;
                                                                    }

                                                                    if (finProductName1.equals(wlNameBom)){
                                                                        //相同，将数据放入数组
                                                                        JSONObject object = new JSONObject();
                                                                        object.put("id","");
                                                                        object.put("bomTitleId", bomTitleId1);
                                                                        object.put("status",1);
                                                                        object.put("productId",finProduct1.getId());
                                                                        object.put("vault",1);
                                                                        object.put("number",wlCountBom);
                                                                        bomContent.add(object);
                                                                    }
                                                                    else{
                                                                        //不相同，报警
                                                                        pass = false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("物料号："+wlNumberBom+"对应的物料名："+wlNameBom+"与在数据库中查询到的不一致！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }
                                                                }
                                                                else {
                                                                    //产品库也没查到
                                                                    pass = false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("物料号："+wlNumberBom+"在数据库中未找到！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;

                                                                }
                                                            }
                                                        }
                                                        else {
                                                            //数量不符合要求
                                                            pass = false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("物料号："+wlNumberBom+"在的数量不符合要求！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }



                                                    }
                                                    else {
                                                        if (wlNumberBom.length() != 0 || wlNameBom.length() != 0 || wlCountBom.length() != 0) {
                                                            int x = i + 1;
                                                            pass = false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("excel中第" + x + "行中，所需信息存在空值！！");
                                                            FileUtil.delFile(filePath);
                                                        }


                                                    }


                                                }
                                                if (!pass){
                                                    break;
                                                }
                                                //获取到了所有的数据，进行BOM表内容的添加
                                                String s = bomContent.toJSONString();
                                                List<FinBom> finBoms = JSONArray.parseArray(s, FinBom.class);
                                                if (finBoms.size()>0){
                                                    finBomService.add(finBoms, Integer.parseInt(bomTitleId1));
                                                    //成功导入
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("导入成功！");
                                                    FileUtil.delFile(filePath);
                                                }
                                                else {
                                                    pass=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel表中不存在可插入的数据！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }



                                            }
                                            else {
                                                if (!finMaterialNumber.equals(wlNumber)){
                                                    //物料号不相同
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel中的物料号与要添加BOM表的产品物料号不相同！");
                                                    FileUtil.delFile(filePath);
                                                    break;

                                                }else {
                                                    //产品名称不相同
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("excel中的产品 名称与要添加BOM表的产品名称不相同！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                            }

                                        }
                                        else{
                                            if (wlNumber.length()==0){
                                                response.setContentType("text/json;charset=utf-8");
                                                response.getWriter().write("在excel表，列：物料号中未找到物料号相关数据！");
                                                FileUtil.delFile(filePath);
                                                break;
                                            }
                                            else {
                                                response.setContentType("text/json;charset=utf-8");
                                                response.getWriter().write("在excel表，列：物料名称中未找到物料号相关数据！");
                                                FileUtil.delFile(filePath);
                                                break;
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


