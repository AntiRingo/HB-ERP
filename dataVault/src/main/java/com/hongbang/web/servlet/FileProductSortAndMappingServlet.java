package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.*;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;
import com.hongbang.util.FileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;
import sun.security.util.Length;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.beans.beancontext.BeanContext;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/fileProductSortAndMappingServlet/*")
public class FileProductSortAndMappingServlet extends HttpServlet {


    SortService sortService = new SortServiceImpl();
    AttributeService attributeService = new AttributeServiceImpl();
    ProductService productService = new ProductServiceImpl();
    AttributeValueService attributeValueService = new AttributeValueServiceImpl();
    MappingService mappingService = new MappingServiceImpl();
    AttributeFunctionService attributeFunctionService = new AttributeFunctionServiceImpl();
    CodeService codeService = new CodeServiceImpl();
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
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        //接收订单id
        String id = request.getParameter("id");

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
                                boolean publicAttributePass=true;
                                JSONArray publicAttribute=new JSONArray();//公共属性
                                boolean oneLevelSortPass=true;
                                JSONArray oneLevelSort=new JSONArray();//一级分类
                                JSONArray oneLevelAttribute=new JSONArray();//一级分类的属性信息
                                JSONArray oneLevelMapping= new JSONArray();//一级分类映射
                                List<JSONArray> oneLevelAll= new ArrayList<>();  //一级分类信息以及属性信息
                                JSONArray twoLevelSort=new JSONArray();//二级分类
                                JSONArray twoLevelAttribute=new JSONArray();//二级分类的属性信息
                                List<JSONArray> twoLevelAll= new ArrayList<>();  //二级分类信息以及属性信息
                                JSONArray twoLevelMapping= new JSONArray();
                                JSONArray threeLevelSort=new JSONArray();//三级级分类
                                JSONArray threeLevelAttribute=new JSONArray();//三级分类的属性信息
                                List<JSONArray> threeLevelAll= new ArrayList<>();  //三级分类信息以及属性信息
                                JSONArray threeLevelMapping= new JSONArray();
                                JSONArray fourLevelSort=new JSONArray();//四级级分类
                                JSONArray fourLevelAttribute=new JSONArray();//四级级分类的属性信息
                                List<JSONArray> fourLevelAll= new ArrayList<>();  //四级分类信息以及属性信息
                                JSONArray fourLevelMapping= new JSONArray();

                                try {
                                    //获取一个Excel文件  只支持.xls格式
                                    workbook = Workbook.getWorkbook(new File(filePath));
                                    //获取到一共有多少个表
                                    Sheet[] sheets = workbook.getSheets();
                                    //sheet.getCell(列，行);
                                    // sheets[i].getRows();行
//                                        sheets[i].getColumns();列

                                    for (int i = 0; i < sheets.length; i++) {
                                        System.out.println(i);
                                        //获取行列数
                                        int columns = sheets[i].getColumns();//列
                                        int rows = sheets[i].getRows();//行
                                        String sheetsName = sheets[i].getName();//获取表格的名称
                                        System.out.println(columns+"列数");
                                        System.out.println(rows+"行数");
                                        System.out.println(sheetsName+"表名");
                                        if (sheetsName.equals("公共属性表")){

                                            int bhColumns = 0;
                                            int sxColumns=0;
                                            int dwColumns=0;
                                            boolean bhExist=false;
                                            boolean sxExist=false;
                                            boolean dwExist=false;
                                            //获取每列的第一行数据
                                            for (int j = 0; j < columns; j++) {
                                                Cell cell = sheets[i].getCell(j, 0);
                                                String contents = cell.getContents().trim();
                                                if (contents.equals("属性编号")){
                                                    //记录列数
                                                    bhColumns=j;
                                                    bhExist=true;
                                                }
                                                else if (contents.equals("属性名称")){
                                                    //记录列数
                                                    sxColumns=j;
                                                    sxExist=true;

                                                }
                                                else if (contents.equals("属性单位")){
                                                    //记录列数
                                                    dwColumns=j;
                                                    dwExist=true;
                                                }
                                            }
                                            //判断两个数据是否都找到
                                            if (!bhExist || !sxExist || !dwExist){
                                                if (!bhExist){
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在表  "+sheetsName+"  中未找到名为：属性编号的列！");
                                                    FileUtil.delFile(filePath);
                                                    publicAttributePass=false;
                                                    break;

                                                }
                                                else if (!sxExist){
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在表  "+sheetsName+"  中未找到名为：属性名称的列!");
                                                    FileUtil.delFile(filePath);
                                                    publicAttributePass=false;
                                                    break;
                                                }
                                                else {
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在表  "+sheetsName+"  中未找到名为：属性单位的列!");
                                                    FileUtil.delFile(filePath);
                                                    publicAttributePass=false;
                                                    break;
                                                }

                                            }
                                            else {
                                                boolean sxbhExist=true;
                                                String[] repeat = new String[rows];
                                                //两个都存在，查询对应的属性编号以及名称是否存在
                                                for (int j = 1; j < rows; j++) {
                                                    Cell cell1 = sheets[i].getCell(bhColumns, j);//编号
                                                    Cell cell = sheets[i].getCell(sxColumns, j);//编号对应的属性
                                                    Cell cell2 = sheets[i].getCell(dwColumns, j);//编号对应的单位
                                                    int length = cell1.getContents().trim().length();
                                                    int length1 = cell.getContents().trim().length();

                                                    if (length>0&&length1>0){
                                                        repeat[j]=cell.getContents().trim();
                                                        //将数据添加到数组中
                                                        JSONObject object = new JSONObject();
                                                        object.put("id","");
                                                        object.put("parentId", 0);
                                                        object.put("name",cell.getContents().trim());
                                                        object.put("unit",cell2.getContents().trim());
                                                        object.put("length","");
                                                        object.put("publicApplication", 1);
                                                        publicAttribute.add(object);



                                                    }
                                                    else {
                                                        //公共属性编号与对应的属性不全

                                                        response.setContentType("text/json;charset=utf-8");
                                                        if (length>0){
                                                            //获取
                                                            String trim = cell1.getContents().trim();
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("未找到编号为：  "+trim+"  对应的属性名称信息");
                                                            FileUtil.delFile(filePath);
                                                            sxbhExist=false;

                                                            break;
                                                        }
                                                        else if (length1>0){
                                                            String trim = cell.getContents().trim();
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("未找到属性名称为：  "+trim+"  对应的编号信息");
                                                            FileUtil.delFile(filePath);
                                                            sxbhExist=false;

                                                            break;

                                                        }



                                                    }


                                                }
                                                if(!sxbhExist){
                                                    break;
                                                }

                                                for (int j = 0; j < repeat.length; j++) {

                                                    for (int k = 0; k < repeat.length; k++) {
                                                        if (j!=k && repeat[j]!=null && repeat[k]!=null){
                                                            if (repeat[j].equals(repeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("公共属性:  "+repeat[j]+"  在公共属性表中重复！");
                                                                FileUtil.delFile(filePath);
                                                                sxbhExist=false;

                                                                break;

                                                            }

                                                        }
                                                    }
                                                    if (!sxbhExist){

                                                        break;
                                                    }
                                                }

                                                if (!sxbhExist){
                                                    break;
                                                }


                                            }





                                        }
                                        else if (sheetsName.equals("一级属性表")){
                                            //获取分类码和分类名的信息
                                            int sortCodeColumns=0;
                                            int sortNameColumns=0;
                                            boolean sortCodeColumnsExist=false;
                                            boolean sortNameColumnsExist=false;
                                            for (int j = 0; j < columns; j++) {
                                                String trim = sheets[i].getCell(j, 0).getContents().trim();
                                                if (trim.equals("分类码")){
                                                    sortCodeColumns=j;
                                                    sortCodeColumnsExist=true;
                                                }
                                                else if (trim.equals("分类名")){
                                                    sortNameColumns=j;
                                                    sortNameColumnsExist=true;
                                                }
                                            }

                                            //判断两个列表是否存在
                                            if (!sortCodeColumnsExist || !sortNameColumnsExist){
                                                response.setContentType("text/json;charset=utf-8");
                                                if (!sortCodeColumnsExist){
                                                    //编码列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类码的列！");
                                                }
                                                else {
                                                    //分类名列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类名的列！");
                                                }
                                                FileUtil.delFile(filePath);
                                                oneLevelSortPass=false;

                                            }
                                            else {


                                                //都存在，获取数据
                                                String[] nameRepeat = new String[rows];
                                                String[] codeRepeat = new String[rows];


                                                for (int j = 1; j < rows; j++) {

                                                    String sortCode = sheets[i].getCell(sortCodeColumns, j).getContents().trim();
                                                    String sortName = sheets[i].getCell(sortNameColumns, j).getContents().trim();


                                                    if (sortCode.length()==0 || sortName.length()==0){
                                                        if (sortCode.length()==0 && sortName.length()>0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"中，  "+sortName+"  对应的分类码为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }else if (sortCode.length()>0 && sortName.length()==0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortCode+"  对应的分类名为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }
                                                    }
                                                    else if (sortCode.length()>0 && sortName.length()>0){


                                                        codeRepeat[j]=sortCode;
                                                        nameRepeat[j]=sortName;
                                                        //判断分类码的长度是否符合要求
                                                        List<Code> codes = codeService.selectCode(1);
                                                        int length = codes.get(0).getLength();
                                                        if (length !=sortCode.length()){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+sortCode+"  的长度与设置不符合！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass = false;
                                                            break;
                                                        }

                                                        //都存在保存数据
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parentId", "0");
                                                        object.put("name",sortName);
                                                        object.put("level",1);
                                                        object.put("code",sortCode);
                                                        object.put("vault",0);
                                                        oneLevelSort.add(object);




                                                        String[] attributeName = new String[columns];
                                                        //获取当前分类的属性信息

                                                        for (int l = 0; l < columns/2; l++) {
                                                            for (int k = 0; k < columns; k++) {
                                                                String trim = sheets[i].getCell(k, 0).getContents().trim();

                                                                if (trim.equals("属性"+l)){
                                                                    //属性列

                                                                    String oneLevelAttributeName = sheets[i].getCell(k, j).getContents().trim();
                                                                    String parentId=codeRepeat[j]=sortCode;

                                                                    if (oneLevelAttributeName.length()>0){
                                                                        JSONObject object1 = new JSONObject();
                                                                        attributeName[k]=oneLevelAttributeName;
                                                                        object1.put("id","");
                                                                        object1.put("parentId","");
                                                                        object1.put("name",oneLevelAttributeName);
                                                                        object1.put("length","");
                                                                        object1.put("publicApplication","0");
                                                                        object1.put("code", parentId);
                                                                        String trim1 = sheets[i].getCell(k + 1, 0).getContents().trim();
                                                                        if (trim1.equals("单位")){
                                                                            String oneLevelAttributeNameUnit = sheets[i].getCell(k+1, j).getContents().trim();
                                                                            object1.put("unit",oneLevelAttributeNameUnit);
                                                                        }
                                                                        else {
                                                                            object1.put("unit","");
                                                                        }
                                                                        oneLevelAttribute.add(object1);



                                                                    }
                                                                }








                                                            }
                                                        }


                                                        //查询当前分类的属性信息是否重复
                                                        for (int k = 0; k < attributeName.length; k++) {
                                                            for (int l = 0; l < attributeName.length; l++) {
                                                                if (k!=l && attributeName[k]!=null && attributeName[l]!=null){
                                                                    if (attributeName[k].equals(attributeName[l])){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+  "中，  "+sortName+"  的属性信息：  "+attributeName[k]+  "重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }

                                                            //查询一级属性是否与公共属性重复
                                                            for (int l = 0; l < publicAttribute.size(); l++) {
                                                                JSONObject o = (JSONObject) publicAttribute.get(l);
                                                                Object name = o.get("name");
                                                                for (int m = 0; m < attributeName.length; m++) {
                                                                    if (attributeName[m]!=null){
                                                                        if (attributeName.equals(name)){
                                                                            //有重复的
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息 ： "+attributeName[k]+"  与公共属性重复！");
                                                                            FileUtil.delFile(filePath);
                                                                            oneLevelSortPass=false;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if (!oneLevelSortPass){
                                                                    break;
                                                                }

                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }
                                                        }

                                                    }
                                                }
                                                //将分类和属性信息加入数组
                                                oneLevelAll.add(oneLevelSort);
                                                oneLevelAll.add(oneLevelAttribute);
                                                System.out.println(oneLevelAll);

                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //查询编码数据是有重复的
                                                for (int j = 0; j < codeRepeat.length; j++) {
                                                    for (int k = 0; k < codeRepeat.length; k++) {
                                                        if (j!=k && codeRepeat[j]!=null &&codeRepeat[k]!=null){
                                                            if (codeRepeat[j].equals(codeRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+codeRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }

                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }
                                                //查询分类名是否重复
                                                for (int j = 0; j < nameRepeat.length; j++) {
                                                    for (int k = 0; k < nameRepeat.length; k++) {
                                                        if (j!=k && nameRepeat[j]!=null &&nameRepeat[k]!=null){
                                                            if (nameRepeat[j].equals(nameRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类名：  "+nameRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }


                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }


                                            }
                                            if (!oneLevelSortPass){
                                                break;
                                            }


                                        }
                                        else if (sheetsName.equals("二级属性表")){
                                            //获取分类码和分类名的信息
                                            int sortCodeColumns=0;
                                            int sortNameColumns=0;
                                            boolean sortCodeColumnsExist=false;
                                            boolean sortNameColumnsExist=false;
                                            for (int j = 0; j < columns; j++) {
                                                String trim = sheets[i].getCell(j, 0).getContents().trim();
                                                if (trim.equals("分类码")){
                                                    sortCodeColumns=j;
                                                    sortCodeColumnsExist=true;
                                                }
                                                else if (trim.equals("分类名")){
                                                    sortNameColumns=j;
                                                    sortNameColumnsExist=true;
                                                }
                                            }

                                            //判断两个列表是否存在
                                            if (!sortCodeColumnsExist || !sortNameColumnsExist){
                                                response.setContentType("text/json;charset=utf-8");
                                                if (!sortCodeColumnsExist){
                                                    //编码列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中，未找到名为分类码的列！");
                                                }
                                                else {
                                                    //分类名列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中，未找到名为分类名的列！");
                                                }
                                                FileUtil.delFile(filePath);
                                                oneLevelSortPass=false;

                                            }
                                            else {

                                                //都存在，获取数据
                                                String[] nameRepeat = new String[rows];
                                                String[] codeRepeat = new String[rows];


                                                for (int j = 1; j < rows; j++) {
                                                    String sortCode = sheets[i].getCell(sortCodeColumns, j).getContents().trim();
                                                    String sortName = sheets[i].getCell(sortNameColumns, j).getContents().trim();

                                                    if (sortCode.length()==0 || sortName.length()==0){
                                                        if (sortCode.length()==0 && sortName.length()>0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  对应的分类码为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }else if (sortCode.length()>0 && sortName.length()==0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortCode+"  对应的分类名为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        codeRepeat[j]=sortCode;
                                                        nameRepeat[j]=sortName;
                                                        //判断分类码的长度是否符合要求
                                                        List<Code> codes = codeService.selectCode(1);
                                                        List<Code> codes1 = codeService.selectCode(2);
                                                        int length = codes.get(0).getLength();
                                                        int length1 = codes1.get(0).getLength();
                                                        int lengthSum=length1+length;
                                                        System.out.println(lengthSum);
                                                        System.out.println(sortCode.length());
                                                        if (lengthSum !=sortCode.length()){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+sortCode+"  的长度与设置不符合！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass = false;
                                                            break;
                                                        }

                                                        String code = sortCode.substring(1);//本级（二级）编码
                                                        String oneLevelCode  = sortCode.substring(0, 1);//上级（一级）编码


                                                        //都存在保存数据
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parentId", "");
                                                        object.put("name",sortName);
                                                        object.put("level",2);
                                                        object.put("code",code);
                                                        object.put("vault",0);
                                                        object.put("oneLevelCode",oneLevelCode);
                                                        twoLevelSort.add(object);



                                                        String[] attributeName = new String[columns];
                                                        //获取当前分类的属性信息

                                                        //获取当前分类的属性信息
                                                        for (int l = 0; l < columns/2; l++) {
                                                            for (int k = 0; k < columns; k++) {
                                                                String trim = sheets[i].getCell(k, 0).getContents().trim();

                                                                if (trim.equals("属性"+l)){
                                                                    //属性列

                                                                    String oneLevelAttributeName = sheets[i].getCell(k, j).getContents().trim();


                                                                    if (oneLevelAttributeName.length()>0){
                                                                        JSONObject object1 = new JSONObject();
                                                                        attributeName[k]=oneLevelAttributeName;
                                                                        object1.put("id","");
                                                                        object1.put("parentId","");
                                                                        object1.put("name",oneLevelAttributeName);

                                                                        object1.put("length","");
                                                                        object1.put("publicApplication","0");
                                                                        object1.put("oneLevelCode",oneLevelCode);
                                                                        object1.put("code",code);
                                                                        String trim1 = sheets[i].getCell(k + 1, 0).getContents().trim();
                                                                        if (trim1.equals("单位")){
                                                                            String oneLevelAttributeNameUnit = sheets[i].getCell(k+1, j).getContents().trim();
                                                                            object1.put("unit",oneLevelAttributeNameUnit);
                                                                        }
                                                                        else {
                                                                            object1.put("unit","");
                                                                        }
                                                                        twoLevelAttribute.add(object1);



                                                                    }
                                                                }







                                                            }
                                                        }

                                                        //查询当前分类的属性信息是否重复
                                                        for (int k = 0; k < attributeName.length; k++) {
                                                            for (int l = 0; l < attributeName.length; l++) {
                                                                if (k!=l && attributeName[k]!=null && attributeName[l]!=null){
                                                                    if (attributeName[k].equals(attributeName[l])){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息：  "+attributeName[k]+"  重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }
                                                        }

                                                        //查询二级属性是否与公共属性重复
                                                        for (int l = 0; l < publicAttribute.size(); l++) {
                                                            JSONObject o = (JSONObject) publicAttribute.get(l);
                                                            Object name = o.get("name");
                                                            for (int m = 0; m < attributeName.length; m++) {
                                                                if (attributeName[m]!=null){
                                                                    if (attributeName.equals(name)){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息  "+attributeName[m]+"  与公共属性重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }

                                                        }



                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //判断当前属性信息是否与一级属性重复
                                                for (int j = 0; j < twoLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) twoLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    for (int k = 0; k < oneLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) oneLevelAttribute.get(k);
                                                        Object code = o1.get("code");
                                                        if (oneLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+o.get("oneLevelCode")+o.get("code")+"  的属性信息：  "+name+"  与一级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }

                                                if (!oneLevelSortPass){
                                                    break;
                                                }

                                                //将分类和属性信息加入数组
                                                twoLevelAll.add(twoLevelSort);
                                                twoLevelAll.add(twoLevelAttribute);

                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //查询编码数据是有重复的
                                                for (int j = 0; j < codeRepeat.length; j++) {
                                                    for (int k = 0; k < codeRepeat.length; k++) {
                                                        if (j!=k && codeRepeat[j]!=null &&codeRepeat[k]!=null){
                                                            if (codeRepeat[j].equals(codeRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+codeRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }
                                                //查询分类名是否重复
                                                for (int j = 0; j < nameRepeat.length; j++) {
                                                    for (int k = 0; k < nameRepeat.length; k++) {
                                                        if (j!=k && nameRepeat[j]!=null &&nameRepeat[k]!=null){
                                                            if (nameRepeat[j].equals(nameRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类名  "+nameRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }


                                            }
                                            if (!oneLevelSortPass){
                                                break;
                                            }


                                        }
                                        else if (sheetsName.equals("三级属性表")){
                                            //获取分类码和分类名的信息
                                            int sortCodeColumns=0;
                                            int sortNameColumns=0;
                                            boolean sortCodeColumnsExist=false;
                                            boolean sortNameColumnsExist=false;
                                            for (int j = 0; j < columns; j++) {
                                                String trim = sheets[i].getCell(j, 0).getContents().trim();
                                                if (trim.equals("分类码")){
                                                    sortCodeColumns=j;
                                                    sortCodeColumnsExist=true;
                                                }
                                                else if (trim.equals("分类名")){
                                                    sortNameColumns=j;
                                                    sortNameColumnsExist=true;
                                                }
                                            }

                                            //判断两个列表是否存在
                                            if (!sortCodeColumnsExist || !sortNameColumnsExist){
                                                response.setContentType("text/json;charset=utf-8");
                                                if (!sortCodeColumnsExist){
                                                    //编码列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类码的列！");
                                                }
                                                else {
                                                    //分类名列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类名的列！");
                                                }
                                                FileUtil.delFile(filePath);
                                                oneLevelSortPass=false;

                                            }
                                            else {

                                                //都存在，获取数据
                                                String[] nameRepeat = new String[rows];
                                                String[] codeRepeat = new String[rows];


                                                for (int j = 1; j < rows; j++) {
                                                    String sortCode = sheets[i].getCell(sortCodeColumns, j).getContents().trim();
                                                    String sortName = sheets[i].getCell(sortNameColumns, j).getContents().trim();

                                                    if (sortCode.length()==0 || sortName.length()==0){
                                                        if (sortCode.length()==0 && sortName.length()>0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  对应的分类码为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }else if (sortCode.length()>0 && sortName.length()==0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortCode+"  对应的分类名为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        codeRepeat[j]=sortCode;
                                                        nameRepeat[j]=sortName;
                                                        //判断分类码的长度是否符合要求
                                                        List<Code> codes = codeService.selectCode(1);
                                                        List<Code> codes1 = codeService.selectCode(2);
                                                        List<Code> codes2 = codeService.selectCode(3);
                                                        int length = codes.get(0).getLength();
                                                        int length1 = codes1.get(0).getLength();
                                                        int length2 = codes2.get(0).getLength();
                                                        int lengthSum=length1+length+length2;
                                                        if (lengthSum !=sortCode.length()){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+sortCode+"  的长度与设置不符合！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass = false;
                                                            break;
                                                        }

                                                        String code = sortCode.substring(3);//本级（三级）编码
                                                        String twoLevelCode = sortCode.substring(1, 3);//二级编码
                                                        String oneLevelCode  = sortCode.substring(0, 1);//上级（一级）编码


                                                        //都存在保存数据
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parentId", "");
                                                        object.put("name",sortName);
                                                        object.put("level",3);
                                                        object.put("code",code);
                                                        object.put("vault",0);
                                                        object.put("oneLevelCode",oneLevelCode);
                                                        object.put("twoLevelCode",twoLevelCode);
                                                        threeLevelSort.add(object);



                                                        String[] attributeName = new String[columns];
                                                        //获取当前分类的属性信息
                                                        //获取当前分类的属性信息
                                                        for (int l = 0; l < columns/2; l++) {
                                                            for (int k = 2; k < columns; k++) {
                                                                String trim = sheets[i].getCell(k, 0).getContents().trim();

                                                                if (trim.equals("属性"+l)){
                                                                    //属性列

                                                                    String oneLevelAttributeName = sheets[i].getCell(k, j).getContents().trim();


                                                                    if (oneLevelAttributeName.length()>0){
                                                                        JSONObject object1 = new JSONObject();
                                                                        attributeName[k]=oneLevelAttributeName;
                                                                        object1.put("id","");
                                                                        object1.put("parentId","");
                                                                        object1.put("name",oneLevelAttributeName);

                                                                        object1.put("length","");
                                                                        object1.put("publicApplication","0");
                                                                        object1.put("oneLevelCode",oneLevelCode);
                                                                        object1.put("twoLevelCode",twoLevelCode);
                                                                        object1.put("code",code);
                                                                        String trim1 = sheets[i].getCell(k + 1, 0).getContents().trim();
                                                                        if (trim1.equals("单位")){
                                                                            String oneLevelAttributeNameUnit = sheets[i].getCell(k+1, j).getContents().trim();
                                                                            object1.put("unit",oneLevelAttributeNameUnit);
                                                                        }
                                                                        else {
                                                                            object1.put("unit","");
                                                                        }
                                                                        threeLevelAttribute.add(object1);





                                                                    }
                                                                }




                                                            }
                                                        }



                                                        //查询当前分类的属性信息是否重复
                                                        for (int k = 0; k < attributeName.length; k++) {
                                                            for (int l = 0; l < attributeName.length; l++) {
                                                                if (k!=l && attributeName[k]!=null && attributeName[l]!=null){
                                                                    if (attributeName[k].equals(attributeName[l])){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息：  "+attributeName[k]+"  重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }
                                                        }
                                                        //查询三级属性是否与公共属性重复
                                                        for (int l = 0; l < publicAttribute.size(); l++) {
                                                            JSONObject o = (JSONObject) publicAttribute.get(l);
                                                            Object name = o.get("name");
                                                            for (int m = 0; m < attributeName.length; m++) {
                                                                if (attributeName[m]!=null){
                                                                    if (attributeName.equals(name)){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息：  "+attributeName[m]+"  与公共属性重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }

                                                        }

                                                    }
                                                }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                //判断当前属性信息是否与一级属性重复
                                                for (int j = 0; j < threeLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) threeLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    Object twoLevelCode = o.get("twoLevelCode");
                                                    Object code1 = o.get("code");
                                                    for (int k = 0; k < oneLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) oneLevelAttribute.get(k);
                                                        Object code = o1.get("code");
                                                        if (oneLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+oneLevelCode+twoLevelCode+code1+"  的属性信息：  "+name+"  与一级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //判断当前属性信息是否与二级属性重复
                                                for (int j = 0; j < threeLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) threeLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    Object twoLevelCode = o.get("twoLevelCode");
                                                    Object code1 = o.get("code");
                                                    for (int k = 0; k < twoLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) twoLevelAttribute.get(k);
                                                        Object code = o1.get("code");
                                                        Object oneLevelCode1 = o1.get("oneLevelCode");
                                                        if (oneLevelCode.equals(oneLevelCode1) &&  twoLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+oneLevelCode+twoLevelCode+code1+"  的属性信息：  "+name+"  与二级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //将分类和属性信息加入数组
                                                threeLevelAll.add(threeLevelSort);
                                                threeLevelAll.add(threeLevelAttribute);

                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //查询编码数据是有重复的
                                                for (int j = 0; j < codeRepeat.length; j++) {
                                                    for (int k = 0; k < codeRepeat.length; k++) {
                                                        if (j!=k && codeRepeat[j]!=null &&codeRepeat[k]!=null){
                                                            if (codeRepeat[j].equals(codeRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+codeRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }
                                                //查询分类名是否重复
                                                for (int j = 0; j < nameRepeat.length; j++) {
                                                    for (int k = 0; k < nameRepeat.length; k++) {
                                                        if (j!=k && nameRepeat[j]!=null &&nameRepeat[k]!=null){
                                                            if (nameRepeat[j].equals(nameRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类名：  "+nameRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }


                                            }
                                            if (!oneLevelSortPass){
                                                break;
                                            }


                                        }
                                        else if (sheetsName.equals("四级属性表")){
                                            //获取分类码和分类名的信息
                                            int sortCodeColumns=0;
                                            int sortNameColumns=0;
                                            boolean sortCodeColumnsExist=false;
                                            boolean sortNameColumnsExist=false;
                                            for (int j = 0; j < columns; j++) {
                                                String trim = sheets[i].getCell(j, 0).getContents().trim();
                                                if (trim.equals("分类码")){
                                                    sortCodeColumns=j;
                                                    sortCodeColumnsExist=true;
                                                }
                                                else if (trim.equals("分类名")){
                                                    sortNameColumns=j;
                                                    sortNameColumnsExist=true;
                                                }
                                            }

                                            //判断两个列表是否存在
                                            if (!sortCodeColumnsExist || !sortNameColumnsExist){
                                                response.setContentType("text/json;charset=utf-8");
                                                if (!sortCodeColumnsExist){
                                                    //编码列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类码的列！");
                                                }
                                                else {
                                                    //分类名列不存在
                                                    response.getWriter().write("在  "+sheetsName+"  中未找到名为分类名的列！");
                                                }
                                                FileUtil.delFile(filePath);
                                                oneLevelSortPass=false;

                                            }
                                            else {

                                                //都存在，获取数据
                                                String[] nameRepeat = new String[rows];
                                                String[] codeRepeat = new String[rows];


                                                for (int j = 1; j < rows; j++) {
                                                    String sortCode = sheets[i].getCell(sortCodeColumns, j).getContents().trim();
                                                    String sortName = sheets[i].getCell(sortNameColumns, j).getContents().trim();

                                                    if (sortCode.length()==0 || sortName.length()==0){
                                                        if (sortCode.length()==0 && sortName.length()>0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  对应的分类码为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }else if (sortCode.length()>0 && sortName.length()==0){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，  "+sortCode+"  对应的分类名为空！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        codeRepeat[j]=sortCode;
                                                        nameRepeat[j]=sortName;
                                                        //判断分类码的长度是否符合要求
                                                        List<Code> codes = codeService.selectCode(1);
                                                        List<Code> codes1 = codeService.selectCode(2);
                                                        List<Code> codes2 = codeService.selectCode(3);
                                                        List<Code> codes3 = codeService.selectCode(4);
                                                        int length = codes.get(0).getLength();
                                                        int length1 = codes1.get(0).getLength();
                                                        int length2 = codes2.get(0).getLength();
                                                        int length3 = codes3.get(0).getLength();
                                                        int lengthSum=length1+length+length2+length3;
                                                        if (lengthSum !=sortCode.length()){
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+sortCode+"  的长度与设置不符合！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass = false;
                                                            break;
                                                        }

                                                        String code = sortCode.substring(5);//本级（四级）编码
                                                        String threeLevelCode = sortCode.substring(3, 5);//三级编码
                                                        String twoLevelCode = sortCode.substring(1, 3);//二级编码
                                                        String oneLevelCode  = sortCode.substring(0, 1);//上级（一级）编码


                                                        //都存在保存数据
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parentId", "");
                                                        object.put("name",sortName);
                                                        object.put("level",4);
                                                        object.put("code",code);
                                                        object.put("vault",0);
                                                        object.put("oneLevelCode",oneLevelCode);
                                                        object.put("twoLevelCode",twoLevelCode);
                                                        object.put("threeLevelCode",threeLevelCode);
                                                        fourLevelSort.add(object);



                                                        String[] attributeName = new String[columns];
                                                        //获取当前分类的属性信息
                                                        //获取当前分类的属性信息

                                                        for (int l = 0; l < columns/2; l++) {
                                                            for (int k = 2; k < columns; k++) {

                                                                String trim = sheets[i].getCell(k, 0).getContents().trim();

                                                                if (trim.equals("属性"+l)){
                                                                    //属性列

                                                                    String oneLevelAttributeName = sheets[i].getCell(k, j).getContents().trim();


                                                                    if (oneLevelAttributeName.length()>0){
                                                                        JSONObject object1 = new JSONObject();
                                                                        attributeName[k]=oneLevelAttributeName;
                                                                        object1.put("id","");
                                                                        object1.put("parentId","");
                                                                        object1.put("name",oneLevelAttributeName);

                                                                        object1.put("length","");
                                                                        object1.put("publicApplication","0");
                                                                        object1.put("oneLevelCode",oneLevelCode);
                                                                        object1.put("twoLevelCode",twoLevelCode);
                                                                        object1.put("threeLevelCode",threeLevelCode);
                                                                        object1.put("code",code);
                                                                        String trim1 = sheets[i].getCell(k + 1, 0).getContents().trim();
                                                                        if (trim1.equals("单位")){
                                                                            String oneLevelAttributeNameUnit = sheets[i].getCell(k+1, j).getContents().trim();
                                                                            object1.put("unit",oneLevelAttributeNameUnit);
                                                                        }
                                                                        else {
                                                                            object1.put("unit","");
                                                                        }
                                                                        fourLevelAttribute.add(object1);



                                                                    }
                                                                }






                                                            }
                                                        }


                                                        //查询当前分类的属性信息是否重复
                                                        for (int k = 0; k < attributeName.length; k++) {
                                                            for (int l = 0; l < attributeName.length; l++) {
                                                                if (k!=l && attributeName[k]!=null && attributeName[l]!=null){
                                                                    if (attributeName[k].equals(attributeName[l])){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息：  "+attributeName[k]+"  重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }
                                                        }

                                                        //查询四级属性是否与公共属性重复
                                                        for (int l = 0; l < publicAttribute.size(); l++) {
                                                            JSONObject o = (JSONObject) publicAttribute.get(l);
                                                            Object name = o.get("name");
                                                            for (int m = 0; m < attributeName.length; m++) {
                                                                if (attributeName[m]!=null){
                                                                    if (attributeName.equals(name)){
                                                                        //有重复的
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+sheetsName+"  中，  "+sortName+"  的属性信息：  "+attributeName[m]+"  与公共属性重复！");
                                                                        FileUtil.delFile(filePath);
                                                                        oneLevelSortPass=false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (!oneLevelSortPass){
                                                                break;
                                                            }

                                                        }

                                                    }

                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }

                                                //判断当前属性信息是否与一级属性重复
                                                for (int j = 0; j < fourLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) fourLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    Object twoLevelCode = o.get("twoLevelCode");
                                                    Object threeLevelCode = o.get("threeLevelCode");
                                                    Object code1 = o.get("code");
                                                    for (int k = 0; k < oneLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) oneLevelAttribute.get(k);
                                                        Object code = o1.get("code");
                                                        if (oneLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+oneLevelCode+twoLevelCode+threeLevelCode+code1+  "的属性信息：  "+name+"  与一级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //判断当前属性信息是否与二级属性重复
                                                for (int j = 0; j < fourLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) fourLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    Object twoLevelCode = o.get("twoLevelCode");
                                                    Object threeLevelCode = o.get("threeLevelCode");
                                                    Object code1 = o.get("code");
                                                    for (int k = 0; k < twoLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) twoLevelAttribute.get(k);
                                                        Object code = o1.get("code");//二级编码
                                                        Object oneLevelCode1 = o1.get("oneLevelCode");
                                                        if (oneLevelCode.equals(oneLevelCode1) && twoLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+oneLevelCode+twoLevelCode+threeLevelCode+code1+"  的属性信息：  "+name+"  与二级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //判断当前属性信息是否与三级属性重复
                                                for (int j = 0; j < fourLevelAttribute.size(); j++) {
                                                    JSONObject o = (JSONObject) fourLevelAttribute.get(j);
                                                    Object oneLevelCode = o.get("oneLevelCode");
                                                    Object twoLevelCode = o.get("twoLevelCode");
                                                    Object threeLevelCode = o.get("threeLevelCode");
                                                    for (int k = 0; k < threeLevelAttribute.size(); k++) {
                                                        JSONObject o1 = (JSONObject) threeLevelAttribute.get(k);
                                                        Object code = o1.get("code");
                                                        Object oneLevelCode1 = o1.get("oneLevelCode");
                                                        Object twoLevelCode1 = o1.get("twoLevelCode");
                                                        if (oneLevelCode.equals(oneLevelCode1) && twoLevelCode.equals(twoLevelCode1) && threeLevelCode.equals(code)){
                                                            //编码相同，获取属性信息，对比是否重复
                                                            Object name = o1.get("name");
                                                            Object name1 = o.get("name");
                                                            if (name.equals(name1)){
                                                                //有重复的
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，  "+oneLevelCode+twoLevelCode+o.get("threeLevelCode")+o.get("code")+"  的属性信息：  "+name+"  与三级分类重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!oneLevelSortPass){
                                                    break;
                                                }

                                                //将分类和属性信息加入数组
                                                fourLevelAll.add(fourLevelSort);
                                                fourLevelAll.add(fourLevelAttribute);

                                                if (!oneLevelSortPass){
                                                    break;
                                                }
                                                //查询编码数据是有重复的
                                                for (int j = 0; j < codeRepeat.length; j++) {
                                                    for (int k = 0; k < codeRepeat.length; k++) {
                                                        if (j!=k && codeRepeat[j]!=null &&codeRepeat[k]!=null){
                                                            if (codeRepeat[j].equals(codeRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中分类码  "+codeRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }
                                                //查询分类名是否重复
                                                for (int j = 0; j < nameRepeat.length; j++) {
                                                    for (int k = 0; k < nameRepeat.length; k++) {
                                                        if (j!=k && nameRepeat[j]!=null &&nameRepeat[k]!=null){
                                                            if (nameRepeat[j].equals(nameRepeat[k])){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类名：  "+nameRepeat[j]+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }
                                                }


                                            }
                                            if (!oneLevelSortPass){
                                                break;
                                            }
                                        }
                                        else if (sheetsName.equals("一级映射表")){
                                            boolean exist=false;
                                            //判断是否有映射存在

                                            for (int j = 1; j < rows; j++) {

                                                String trim = sheets[i].getCell(0, j).getContents().trim();
                                                if (trim.length()>0){
                                                    //有分类码，查询该分类码是否重复
                                                    for (int m = 1; m < rows; m++) {

                                                        if (m!=j){

                                                            String trim2 = sheets[i].getCell(0, m).getContents().trim();

                                                            if (trim.equals(trim2)){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+trim+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }

                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }





                                                    //分类码不重复，在分类表中查询该编码是否存在
                                                    for (int k = 0; k < sheets.length; k++) {

                                                        Sheet sheet = sheets[k];

                                                        String name = sheets[k].getName();

                                                        if (name.equals("一级属性表")){

                                                            int rows1 = sheet.getRows();
                                                            for (int l = 1; l < rows1; l++) {
                                                                exist=false;
                                                                String trim1 = sheet.getCell(0, l).getContents().trim();


                                                                if (trim1.length()>0 && trim.equals(trim1)){

                                                                    exist=true;
                                                                    break;

                                                                    //存在这个分类码，然后去查询映射的属性是否存在

                                                                }




                                                            }

                                                            if (!exist){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码  "+trim+"  在  "+name+"  中不存在！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }

                                                            int i1 = columns / 2;

                                                            int zwCount=0;//占位总数
                                                            String trim3="";
                                                            int end=0;
                                                            for (int o = 0; o <i1; o++) {
                                                                for (int z = 1; z < columns; z++) {
                                                                    String sx = sheets[i].getCell(z, 0).getContents().trim();//属性1,2,3...
                                                                    String zw = sheets[i].getCell(z - 1, 0).getContents().trim();
                                                                    if (sx.equals("属性"+z)){



                                                                        if (!zw.equals("占位")){
                                                                            exist=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表"+sheetsName+"未找到属性"+o+"的占位列！");
                                                                            FileUtil.delFile(filePath);
                                                                            oneLevelSortPass=false;
                                                                            break;
                                                                        }


                                                                        String  trim1= sheets[i].getCell(z-1, j).getContents().trim();//占位
                                                                        if (trim1.length()>0){
                                                                            end+=Integer.parseInt(trim1);
                                                                            zwCount+=Integer.parseInt(trim1);
                                                                        }
                                                                        String trim2 = sheets[i].getCell(z, j).getContents().trim();//映射属性
                                                                        trim3 = sheets[i].getCell(0, j).getContents().trim();//映射分类码
                                                                        JSONObject mappingObject = new JSONObject();
                                                                        if (trim1.length()>0 && trim2.length()>0 && trim3.length()>0){

                                                                            exist=false;


                                                                            for (int l = 0; l < oneLevelAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) oneLevelAttribute.get(l);
                                                                                Object code = object.get("code");//分类码
                                                                                if (code.equals(trim3)){
                                                                                    //分类码相同，对比属性是否存在
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //查询该属性的长度是不是没有
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }
                                                                                        exist=true;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!exist){
                                                                                //在分类属性中不存在，去查询公共属性中是否存在
                                                                                for (int l = 0; l < publicAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) publicAttribute.get(l);
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //查询该属性的长度是不是没有
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }
                                                                                        exist = true;
                                                                                    }
                                                                                }
                                                                            }


                                                                            if (!exist){
                                                                                //属性表中不存在
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  " + sheetsName + "  中，分类码为  " + trim3 + "  中的属性：  " + trim2 + "  在属性表中不存在！");
                                                                                FileUtil.delFile(filePath);
                                                                                oneLevelSortPass = false;
                                                                                break;
                                                                            }

                                                                            mappingObject.put("id","");
                                                                            mappingObject.put("attNameId", "");
                                                                            mappingObject.put("sortId","");
                                                                            mappingObject.put("beginLocation",end-(Integer.parseInt(trim1)-1));
                                                                            mappingObject.put("endLocation",end);
                                                                            mappingObject.put("length",trim1);
                                                                            mappingObject.put("attName", trim2);
                                                                            mappingObject.put("code",trim3);

                                                                            oneLevelMapping.add(mappingObject);







                                                                        }
                                                                    }
                                                                }









                                                            }

                                                            if (zwCount>9){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表"+sheetsName+"中映射"+trim3+"总占位超过9！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }


                                                        }
                                                        else {
                                                            exist=true;
                                                        }

                                                    }
                                                    if (!exist){
                                                        break;
                                                    }





                                                }
                                            }

                                            //判断映射中的属性是否重复

                                            for (int j = 0; j < oneLevelMapping.size(); j++) {
                                                JSONObject o = (JSONObject) oneLevelMapping.get(j);
                                                Object code = o.get("code");
                                                Object attName = o.get("attName");
                                                for (int k = 0; k < oneLevelMapping.size(); k++) {
                                                    if (j!=k){
                                                        JSONObject o1 = (JSONObject) oneLevelMapping.get(k);
                                                        Object code1 = o1.get("code");
                                                        Object attName1 = o1.get("attName");
                                                        if (code.equals(code1) && attName.equals(attName1)){
                                                            //判断属性是否相同

                                                            exist=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+sheetsName+"  中，映射分类码为：  "+code+"  的属性：  "+attName+"  重复！");
                                                            FileUtil.delFile(filePath);
                                                            oneLevelSortPass=false;
                                                            break;


                                                        }
                                                    }
                                                }
                                                if (!exist){
                                                    break;
                                                }
                                            }
//                                                if (!exist){
//                                                    break;
//                                                }
                                            if (exist){
                                                oneLevelAll.add(oneLevelMapping);
                                            }



                                        }
                                        else if (sheetsName.equals("二级映射表")){
                                            boolean exist=false;
                                            //判断是否有映射存在

                                            for (int j = 1; j < rows; j++) {

                                                String trim = sheets[i].getCell(0, j).getContents().trim();
                                                if (trim.length()>0){
                                                    //查询该分类码是否重复
                                                    for (int m = 1; m < rows; m++) {

                                                        if (m!=j){

                                                            String trim2 = sheets[i].getCell(0, m).getContents().trim();

                                                            if (trim.equals(trim2)){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表"+sheetsName+"中的分类码"+trim+"重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                        if (!exist){
                                                            break;
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }



                                                    //在分类表中查询该编码是否存在
                                                    for (int k = 0; k < sheets.length; k++) {

                                                        Sheet sheet = sheets[k];

                                                        String name = sheets[k].getName();

                                                        if (name.equals("二级属性表")){

                                                            int rows1 = sheet.getRows();
                                                            for (int l = 1; l < rows1; l++) {
                                                                exist=false;
                                                                String trim1 = sheet.getCell(0, l).getContents().trim();


                                                                if (trim1.length()>0 && trim.equals(trim1)){

                                                                    exist=true;
                                                                    break;
                                                                    //存在这个编码，然后去查询映射的属性是否存在

                                                                }
                                                            }

                                                            if (!exist){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+trim+"  在  "+name+"  中不存在！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }

                                                            int i1 = columns / 2;

                                                            int zwCount=0;//占位总数
                                                            String trim3="";
                                                            int end=0;
                                                            for (int o = 0; o <i1; o++) {

                                                                for (int z = 1; z < columns; z++) {
                                                                    String sx= sheets[i].getCell(z,0).getContents().trim();
                                                                    String zw = sheets[i].getCell(z - 1, 0).getContents().trim();
                                                                    if (sx.equals("属性"+o)){



                                                                        if (!zw.equals("占位")){
                                                                            exist=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表"+sheetsName+"未找到属性"+o+"的占位列！");
                                                                            FileUtil.delFile(filePath);
                                                                            oneLevelSortPass=false;
                                                                            break;
                                                                        }

                                                                        String trim1= sheets[i].getCell(z-1, j).getContents().trim();//占位
                                                                        if (trim1.length()>0){
                                                                            end+=Integer.parseInt(trim1);
                                                                            zwCount+=Integer.parseInt(trim1);
                                                                        }
                                                                        String trim2 = sheets[i].getCell(z, j).getContents().trim();//属性
                                                                        trim3 = sheets[i].getCell(0, j).getContents().trim();//分类码
                                                                        JSONObject mappingObject = new JSONObject();



                                                                        if (trim1.length()>0 && trim2.length()>0 && trim3.length()>0){

                                                                            exist=false;

                                                                            String oneSortCode = trim3.substring(0, 1);
                                                                            String twoSortCode = trim3.substring(1, 3);

                                                                            for (int l = 0; l < twoLevelAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) twoLevelAttribute.get(l);
                                                                                Object code = object.get("code");//二级分类编码
                                                                                Object oneLevelCode = object.get("oneLevelCode");//一级分类编码
                                                                                if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(code)){
                                                                                    //找到了该映射对应的分类属性，查询属性是否存在
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //查询该属性的长度是不是没有
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }

                                                                                        exist=true;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!exist){
                                                                                //二级属性没检测到，检测一级属性
                                                                                for (int l = 0; l < oneLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) oneLevelAttribute.get(l);
                                                                                    Object code = object.get("code");
                                                                                    if (code.equals(oneSortCode)){
                                                                                        //找到了对应的一级属性信息
                                                                                        Object name1 = object.get("name");
                                                                                        if (name1.equals(trim2)){
                                                                                            //查询该属性的长度是不是没有
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!exist){
                                                                                //一级属性没检测出来，检测公共属性
                                                                                for (int l = 0; l < publicAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) publicAttribute.get(l);
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //查询该属性的长度是不是没有
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }
                                                                                        exist=true;
                                                                                    }
                                                                                }
                                                                            }


                                                                            if (!exist){
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  " + sheetsName + "  中，分类码为  " + trim3 + "  中的属性：  " + trim2 + "在属性表中不存在！");
                                                                                FileUtil.delFile(filePath);
                                                                                oneLevelSortPass = false;
                                                                                break;
                                                                            }

                                                                            mappingObject.put("id","");
                                                                            mappingObject.put("attNameId", "");
                                                                            mappingObject.put("sortId","");
                                                                            mappingObject.put("beginLocation",end-(Integer.parseInt(trim1)-1));
                                                                            mappingObject.put("endLocation",end);
                                                                            mappingObject.put("length",trim1);
                                                                            mappingObject.put("attName", trim2);
                                                                            mappingObject.put("code",twoSortCode);
                                                                            mappingObject.put("oneLevelCode",oneSortCode);
                                                                            twoLevelMapping.add(mappingObject);






                                                                        }
                                                                    }
                                                                }
                                                                if (!exist){
                                                                    break;
                                                                }





                                                            }

                                                            if (zwCount>9){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表"+sheetsName+"中映射"+trim3+"总占位超过9！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }


                                                        }
                                                        else {
                                                            exist=true;
                                                        }

                                                    }
                                                    if (!exist){
                                                        break;
                                                    }
                                                    //存在编码，查询该编码下是否存在映射

                                                    //判断占位是否重叠
                                                }
                                            }



                                            //判断映射中的属性是否重复
                                            for (int j = 0; j < twoLevelMapping.size(); j++) {
                                                JSONObject o = (JSONObject) twoLevelMapping.get(j);
                                                Object code = o.get("code");
                                                Object attName = o.get("attName");
                                                Object oneLevelCode = o.get("oneLevelCode");
                                                for (int k = 0; k < twoLevelMapping.size(); k++) {
                                                    if (j!=k){
                                                        JSONObject o1 = (JSONObject) twoLevelMapping.get(k);
                                                        Object code1 = o1.get("code");
                                                        Object attName1 = o1.get("attName");
                                                        Object oneLevelCode1 = o1.get("oneLevelCode");
                                                        if (code.equals(code1) && oneLevelCode.equals(oneLevelCode1)){
                                                            //判断属性是否相同
                                                            if (attName.equals(attName1)){
                                                                exist=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，映射编码为：  "+code+"  的属性：  "+attName+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }

                                                        }
                                                    }
                                                }
                                                if (!exist){
                                                    break;
                                                }
                                            }
//                                                if (!exist){
//                                                    break;
//                                                }

                                            if (exist){
                                                twoLevelAll.add(twoLevelMapping);
                                            }

                                        }
                                        else if (sheetsName.equals("三级映射表")){
                                            boolean exist=false;
                                            //判断是否有映射存在

                                            for (int j = 1; j < rows; j++) {

                                                String trim = sheets[i].getCell(0, j).getContents().trim();
                                                if (trim.length()>0){

                                                    //查询该分类码是否重复
                                                    for (int m = 1; m < rows; m++) {

                                                        if (m!=j){

                                                            String trim2 = sheets[i].getCell(0, m).getContents().trim();

                                                            if (trim.equals(trim2)){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表"+sheetsName+"中的分类码"+trim+"重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                        if (!exist){
                                                            break;
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }


                                                    //在分类表中查询该编码是否存在
                                                    for (int k = 0; k < sheets.length; k++) {

                                                        Sheet sheet = sheets[k];

                                                        String name = sheets[k].getName();

                                                        if (name.equals("三级属性表")){

                                                            int rows1 = sheet.getRows();
                                                            for (int l = 1; l < rows1; l++) {
                                                                exist=false;
                                                                String trim1 = sheet.getCell(0, l).getContents().trim();


                                                                if (trim1.length()>0 && trim.equals(trim1)){

                                                                    exist=true;
                                                                    break;
                                                                    //存在这个编码，然后去查询映射的属性是否存在

                                                                }
                                                            }

                                                            if (!exist){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+trim+  "在  "+name+"  中不存在！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }

                                                            int i1 = columns  / 2;

                                                            int zwCount=0;//占位总数
                                                            String trim3="";
                                                            int end=0;
                                                            for (int o = 1; o <i1; o++) {
                                                                for (int z = 1; z < columns; z++) {
                                                                    String sx = sheets[i].getCell(z, 0).getContents().trim();
                                                                    String zw = sheets[i].getCell(z - 1, 0).getContents().trim();
                                                                    if (sx.equals("属性"+o)){

                                                                        if (!zw.equals("占位")){
                                                                            exist=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表"+sheetsName+"未找到属性"+o+"的占位列！");
                                                                            FileUtil.delFile(filePath);
                                                                            oneLevelSortPass=false;
                                                                            break;
                                                                        }

                                                                        String trim1= sheets[i].getCell(z-1, j).getContents().trim();//占位
                                                                        if (trim1.length()>0){
                                                                            end+=Integer.parseInt(trim1);
                                                                            zwCount+=Integer.parseInt(trim1);
                                                                        }
                                                                        String trim2 = sheets[i].getCell(z, j).getContents().trim();//属性
                                                                        trim3 = sheets[i].getCell(0, j).getContents().trim();//分类码
                                                                        JSONObject mappingObject = new JSONObject();

                                                                        String oneSortCode = trim3.substring(0, 1);
                                                                        String twoSortCode = trim3.substring(1, 3);
                                                                        String threeSortCode = trim3.substring(3, 5);


                                                                        if (trim1.length()>0 && trim2.length()>0 && trim3.length()>0){

                                                                            exist=false;


                                                                            for (int l = 0; l < threeLevelAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) threeLevelAttribute.get(l);
                                                                                Object threeLevelCode = object.get("code");
                                                                                Object twoLevelCode = object.get("twoLevelCode");
                                                                                Object oneLevelCode = object.get("oneLevelCode");

                                                                                if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(twoLevelCode) && threeSortCode.equals(threeLevelCode)){
                                                                                    //找到了该分类下的属性
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //检测长度是否是空的
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }
                                                                                        exist=true;
                                                                                    }
                                                                                }

                                                                            }


                                                                            //三级属性不存在，查询二级属性
                                                                            if (!exist){

                                                                                for (int l = 0; l < twoLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) twoLevelAttribute.get(l);
                                                                                    Object twoLevelCode = object.get("code");
                                                                                    Object oneLevelCode = object.get("oneLevelCode");
                                                                                    if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(twoLevelCode)){
                                                                                        //找到了该分类下的属性

                                                                                        Object name1 = object.get("name");
                                                                                        if (name1.equals(trim2)){
                                                                                            //检测长度是否是空的
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            //二级不存在查询一级属性
                                                                            if (!exist){
                                                                                for (int l = 0; l < oneLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) oneLevelAttribute.get(l);
                                                                                    Object oneLevelCode = object.get("code");
                                                                                    if (oneSortCode.equals(oneLevelCode)){
                                                                                        //找到了该分类下的属性
                                                                                        Object name1 = object.get("name");
                                                                                        if (name1.equals(trim2)){
                                                                                            //检测长度是否是空的
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }

                                                                                    }

                                                                                }
                                                                            }

                                                                            //一级属性也不存在，查询公共属性
                                                                            for (int l = 0; l < publicAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) publicAttribute.get(l);
                                                                                Object name1 = object.get("name");
                                                                                if (name1.equals(trim2)){
                                                                                    //检测长度是否是空的
                                                                                    Object length = object.get("length");
                                                                                    if (length.equals("")){
                                                                                        object.put("length",trim1);
                                                                                        object.toJSONString();
                                                                                    }
                                                                                    exist=true;
                                                                                }
                                                                            }



                                                                            if (!exist){
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表" + sheetsName + "中，分类码为：  " + trim3 + "中的属性" + trim2 + "在属性表中不存在！");
                                                                                FileUtil.delFile(filePath);
                                                                                oneLevelSortPass = false;
                                                                                break;
                                                                            }
                                                                            mappingObject.put("id","");
                                                                            mappingObject.put("attNameId", "");
                                                                            mappingObject.put("sortId","");
                                                                            mappingObject.put("beginLocation",end-(Integer.parseInt(trim1)-1));
                                                                            mappingObject.put("endLocation",end);
                                                                            mappingObject.put("length",trim1);
                                                                            mappingObject.put("attName", trim2);
                                                                            mappingObject.put("code",threeSortCode);
                                                                            mappingObject.put("twoLevelCode",twoSortCode);
                                                                            mappingObject.put("oneLevelCode",oneSortCode);

                                                                            threeLevelMapping.add(mappingObject);

                                                                        }
                                                                    }
                                                                }

                                                                if (!exist){
                                                                    break;
                                                                }







                                                            }

                                                            if (zwCount>9){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表"+sheetsName+"中映射"+trim3+"总占位超过9！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }


                                                        }
                                                        else {
                                                            exist=true;
                                                        }

                                                    }
                                                    if (!exist){
                                                        break;
                                                    }



                                                }
                                            }


                                            //判断映射中的属性是否重复
                                            for (int j = 0; j < threeLevelMapping.size(); j++) {
                                                JSONObject o = (JSONObject) threeLevelMapping.get(j);
                                                Object code = o.get("code");
                                                Object attName = o.get("attName");
                                                Object oneLevelCode = o.get("oneLevelCode");
                                                Object twoLevelCode = o.get("twoLevelCode");
                                                for (int k = 0; k < threeLevelMapping.size(); k++) {
                                                    if (j!=k){
                                                        JSONObject o1 = (JSONObject) threeLevelMapping.get(k);
                                                        Object code1 = o1.get("code");
                                                        Object attName1 = o1.get("attName");
                                                        Object oneLevelCode1= o1.get("oneLevelCode");
                                                        Object twoLevelCode1 = o1.get("twoLevelCode");
                                                        if (code.equals(code1) && oneLevelCode.equals(oneLevelCode1) && twoLevelCode.equals(twoLevelCode1)){
                                                            //判断属性是否相同
                                                            if (attName.equals(attName1)){
                                                                exist=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，映射编码为：  "+code+"  的属性：  "+attName+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }

                                                        }
                                                    }
                                                }
                                                if (!exist){
                                                    break;
                                                }
                                            }
//                                                if (!exist){
//                                                    break;
//                                                }

                                            if (exist){
                                                threeLevelAll.add(threeLevelMapping);
                                            }


                                        }
                                        else if (sheetsName.equals("四级映射表")){

                                            boolean exist=false;
                                            //判断是否有映射存在

                                            for (int j = 1; j < rows; j++) {

                                                String trim = sheets[i].getCell(0, j).getContents().trim();
                                                if (trim.length()>0){

                                                    //查询该分类码是否重复
                                                    for (int m = 1; m < rows; m++) {

                                                        if (m!=j){

                                                            String trim2 = sheets[i].getCell(0, m).getContents().trim();

                                                            if (trim.equals(trim2)){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+trim+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }
                                                        }
                                                        if (!exist){
                                                            break;
                                                        }
                                                    }
                                                    if (!oneLevelSortPass){
                                                        break;
                                                    }


                                                    //在分类表中查询该编码是否存在
                                                    for (int k = 0; k < sheets.length; k++) {

                                                        Sheet sheet = sheets[k];

                                                        String name = sheets[k].getName();

                                                        if (name.equals("四级属性表")){

                                                            int rows1 = sheet.getRows();
                                                            for (int l = 1; l < rows1; l++) {
                                                                exist=false;
                                                                String trim1 = sheet.getCell(0, l).getContents().trim();


                                                                if (trim1.length()>0 && trim.equals(trim1)){
                                                                    System.out.println("存在");

                                                                    exist=true;
                                                                    break;
                                                                    //存在这个编码，然后去查询映射的属性是否存在

                                                                }
                                                            }

                                                            if (!exist){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，分类码：  "+trim+"  在  "+name+"  中不存在！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }

                                                            int i1 = columns / 2;

                                                            int zwCount=0;//占位总数
                                                            String trim3="";
                                                            int end=0;
                                                            for (int o = 1; o <i1; o++) {
                                                                for (int z = 1; z < columns; z++) {
                                                                    String sx = sheets[i].getCell(z, 0).getContents().trim();
                                                                    String zw = sheets[i].getCell(z - 1, 0).getContents().trim();

                                                                    if (sx.equals("属性"+o)){

                                                                        if (!zw.equals("占位")){
                                                                            exist=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表"+sheetsName+"未找到属性"+o+"的占位列！");
                                                                            FileUtil.delFile(filePath);
                                                                            oneLevelSortPass=false;
                                                                            break;
                                                                        }

                                                                        String trim1= sheets[i].getCell(z-1, j).getContents().trim();//占位
                                                                        if (trim1.length()>0){
                                                                            end+=Integer.parseInt(trim1);
                                                                            zwCount+=Integer.parseInt(trim1);
                                                                        }

                                                                        String trim2 = sheets[i].getCell(z, j).getContents().trim();//属性

                                                                        trim3 = sheets[i].getCell(0, j).getContents().trim();//分类码
                                                                        JSONObject mappingObject = new JSONObject();

                                                                        String oneSortCode = trim3.substring(0, 1);
                                                                        String twoSortCode = trim3.substring(1, 3);
                                                                        String threeSortCode = trim3.substring(3, 5);
                                                                        String fourSortCode = trim3.substring(5);


                                                                        if (trim1.length()>0 && trim2.length()>0 && trim3.length()>0){


                                                                            exist=false;

                                                                            for (int l = 0; l < fourLevelAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) fourLevelAttribute.get(l);
                                                                                Object fourLevelCode = object.get("code");
                                                                                Object threeLevelCode = object.get("threeLevelCode");
                                                                                Object twoLevelCode = object.get("twoLevelCode");
                                                                                Object oneLevelCode = object.get("oneLevelCode");
                                                                                if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(twoLevelCode) && threeSortCode.equals(threeLevelCode) && fourLevelCode.equals(fourSortCode)){
                                                                                    //找到了该分类下的属性
                                                                                    Object name1 = object.get("name");
                                                                                    if (name1.equals(trim2)){
                                                                                        //检测长度是否是空的
                                                                                        Object length = object.get("length");
                                                                                        if (length.equals("")){
                                                                                            object.put("length",trim1);
                                                                                            object.toJSONString();
                                                                                        }
                                                                                        exist=true;
                                                                                    }
                                                                                }
                                                                            }
                                                                            System.out.println(threeLevelAttribute);
                                                                            System.out.println(trim2);
                                                                            //四级属性没有查询到，查询三级
                                                                            if (!exist){
                                                                                for (int l = 0; l < threeLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) threeLevelAttribute.get(l);
                                                                                    Object threeLevelCode = object.get("code");
                                                                                    Object twoLevelCode = object.get("twoLevelCode");
                                                                                    Object oneLevelCode = object.get("oneLevelCode");

                                                                                    if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(twoLevelCode) && threeSortCode.equals(threeLevelCode)){
                                                                                        //找到了该分类下的属性
                                                                                        Object name1 = object.get("name");

                                                                                        if (name1.equals(trim2)){
                                                                                            //检测长度是否是空的
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }
                                                                                    }

                                                                                }
                                                                            }



                                                                            //三级属性不存在，查询二级属性
                                                                            if (!exist){

                                                                                for (int l = 0; l < twoLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) twoLevelAttribute.get(l);
                                                                                    Object twoLevelCode = object.get("code");
                                                                                    Object oneLevelCode = object.get("oneLevelCode");
                                                                                    if (oneSortCode.equals(oneLevelCode) && twoSortCode.equals(twoLevelCode)){
                                                                                        //找到了该分类下的属性

                                                                                        Object name1 = object.get("name");
                                                                                        if (name1.equals(trim2)){
                                                                                            //检测长度是否是空的
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            //二级不存在查询一级属性
                                                                            if (!exist){
                                                                                for (int l = 0; l < oneLevelAttribute.size(); l++) {
                                                                                    JSONObject object = (JSONObject) oneLevelAttribute.get(l);
                                                                                    Object oneLevelCode = object.get("code");
                                                                                    if (oneSortCode.equals(oneLevelCode)){
                                                                                        //找到了该分类下的属性
                                                                                        Object name1 = object.get("name");
                                                                                        if (name1.equals(trim2)){
                                                                                            //检测长度是否是空的
                                                                                            Object length = object.get("length");
                                                                                            if (length.equals("")){
                                                                                                object.put("length",trim1);
                                                                                                object.toJSONString();
                                                                                            }
                                                                                            exist=true;
                                                                                        }

                                                                                    }

                                                                                }
                                                                            }

                                                                            //一级属性也不存在，查询公共属性
                                                                            for (int l = 0; l < publicAttribute.size(); l++) {
                                                                                JSONObject object = (JSONObject) publicAttribute.get(l);
                                                                                Object name1 = object.get("name");
                                                                                if (name1.equals(trim2)){
                                                                                    //检测长度是否是空的
                                                                                    Object length = object.get("length");
                                                                                    if (length.equals("")){
                                                                                        object.put("length",trim1);
                                                                                        object.toJSONString();
                                                                                    }
                                                                                    exist=true;
                                                                                }
                                                                            }

                                                                            if (!exist){
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  " + sheetsName + "中，分类码为：  " + trim3 + "  中的属性：  " + trim2 + "  在属性表中不存在！");
                                                                                FileUtil.delFile(filePath);
                                                                                oneLevelSortPass = false;
                                                                                break;
                                                                            }

                                                                            mappingObject.put("id","");
                                                                            mappingObject.put("attNameId", "");
                                                                            mappingObject.put("sortId","");
                                                                            mappingObject.put("beginLocation",end-(Integer.parseInt(trim1)-1));
                                                                            mappingObject.put("endLocation",end);
                                                                            mappingObject.put("length",trim1);
                                                                            mappingObject.put("attName", trim2);
                                                                            mappingObject.put("code",fourSortCode);
                                                                            mappingObject.put("threeLevelCode",threeSortCode);
                                                                            mappingObject.put("twoLevelCode",twoSortCode);
                                                                            mappingObject.put("oneLevelCode",oneSortCode);
                                                                            fourLevelMapping.add(mappingObject);







                                                                        }
                                                                    }

                                                                }
                                                                if (!exist){
                                                                    break;
                                                                }







                                                            }

                                                            if (zwCount>9){
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，映射"+trim3+"总占位超过9！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;

                                                            }


                                                        }
                                                        else {
                                                            exist=true;
                                                        }

                                                    }
                                                    if (!exist){
                                                        break;
                                                    }

                                                }
                                            }


                                            //判断映射中的属性是否重复
                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o = (JSONObject) fourLevelMapping.get(j);
                                                Object code = o.get("code");
                                                Object attName = o.get("attName");
                                                Object oneLevelCode = o.get("oneLevelCode");
                                                Object twoLevelCode = o.get("twoLevelCode");
                                                Object threeLevelCode = o.get("threeLevelCode");
                                                for (int k = 0; k < fourLevelMapping.size(); k++) {
                                                    if (j!=k){
                                                        JSONObject o1 = (JSONObject) fourLevelMapping.get(k);
                                                        Object code1 = o1.get("code");
                                                        Object attName1 = o1.get("attName");
                                                        Object oneLevelCode1 = o1.get("oneLevelCode");
                                                        Object twoLevelCode1 = o1.get("twoLevelCode");
                                                        Object threeLevelCode1 = o1.get("threeLevelCode");
                                                        if (code.equals(code1)){
                                                            //判断属性是否相同
                                                            if (attName.equals(attName1) && oneLevelCode.equals(oneLevelCode1) && twoLevelCode.equals(twoLevelCode1) && threeLevelCode.equals(threeLevelCode1)){
                                                                exist=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+sheetsName+"  中，映射编码为：  "+code+"  的属性  "+attName+"  重复！");
                                                                FileUtil.delFile(filePath);
                                                                oneLevelSortPass=false;
                                                                break;
                                                            }

                                                        }
                                                    }
                                                }
                                                if (!exist){
                                                    break;
                                                }
                                            }
//                                                if (!exist){
//                                                    break;
//                                                }

                                            if (exist){

                                                fourLevelAll.add(fourLevelMapping);
                                            }

                                        }



                                    }
                                    if(!oneLevelSortPass || !publicAttributePass){
                                        break;
                                    }


                                    //获取到了所有的信息

                                    //分类信息
                                    boolean check=true;
                                    if (oneLevelSortPass){

                                        JSONArray oneSort  = new JSONArray();
                                        JSONArray oneAttributeName = new JSONArray();
                                        JSONArray oneMapping = new JSONArray();

                                        for (int i = 0; i < oneLevelAll.size(); i++) {
                                            if (i==0){
                                                oneSort = oneLevelAll.get(i);
                                            }else if (i==1){
                                                oneAttributeName = oneLevelAll.get(i);
                                            }
                                            else if (i==2){
                                                oneMapping = oneLevelAll.get(i);
                                            }
                                        }
                                        System.out.println(1);
                                        JSONArray twoSort = new JSONArray();
                                        JSONArray twoAttributeName = new JSONArray();
                                        JSONArray twoMapping = new JSONArray();
                                        for (int i = 0; i < twoLevelAll.size(); i++) {
                                            if (i==0){
                                                twoSort = twoLevelAll.get(i);
                                            }else if (i==1){
                                                twoAttributeName = twoLevelAll.get(i);
                                            }
                                            else if (i==2){
                                                twoMapping = twoLevelAll.get(i);
                                            }
                                        }

                                        JSONArray threeSort = new JSONArray();
                                        JSONArray threeAttributeName = new JSONArray();
                                        JSONArray threeMapping = new JSONArray();

                                        for (int i = 0; i < threeLevelAll.size(); i++) {
                                            if (i==0){
                                                threeSort = threeLevelAll.get(i);
                                            }else if (i==1){
                                                threeAttributeName = threeLevelAll.get(i);
                                            }
                                            else if (i==2){
                                                threeMapping = threeLevelAll.get(i);
                                            }
                                        }
                                        System.out.println(2);

                                        JSONArray fourSort = new JSONArray();
                                        JSONArray fourAttributeName = new JSONArray();
                                        JSONArray fourMapping = new JSONArray();

                                        for (int i = 0; i < fourLevelAll.size(); i++) {
                                            if (i==0){
                                                fourSort = fourLevelAll.get(i);
                                            }else if (i==1){
                                                fourAttributeName = fourLevelAll.get(i);
                                            }
                                            else if (i==2){
                                                fourMapping = fourLevelAll.get(i);
                                            }
                                        }

                                        System.out.println(3);
                                        //添加数据:公共属性

                                        String ss = JSON.toJSONString(publicAttribute);


                                        List<AttributeName> attributeNames = JSONArray.parseArray(ss, AttributeName.class);
                                        System.out.println(attributeNames);
                                        for (int i = 0; i < attributeNames.size(); i++) {

                                            AttributeName attributeName = attributeNames.get(i);

                                            String name = attributeName.getName();
                                            //查询该公共属性是否存在
                                            boolean b = attributeService.selectNameExist(name);
                                            System.out.println(4);

                                            int id1;
                                            if (!b){
                                                //不存在才进行添加
                                                attributeService.addPublic(attributeName);

                                                //查询是否存在物料信息
                                                List<Product> products = productService.selectAll();
                                                if (products.size()>0){

                                                    //存在物料信息，给每个物料信息添加新的公共属性信息

                                                    attributeService.afterAddPublicIfProductExist(attributeName.getId(), products);
                                                }
                                                //获取公共属性的id
                                                id1 = attributeName.getId();


                                            }
                                            else {
                                                //存在,查询该公共属性的id
                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(name, 0);
                                                id1=attributeNames1.get(0).getId();

                                                //存在，更新单位
                                                attributeName.setId(id1);
                                                attributeService.updateUnit(attributeName);

                                                System.out.println(5);

                                            }
                                            //添加function信息
                                            JSONObject object = new JSONObject();
                                            object.put("id","");
                                            object.put("attNameId",id1);
                                            object.put("range",0);
                                            object.put("autoCode",0);
                                            String s1 = JSON.toJSONString(object);
                                            AttributeFunction attributeFunction = JSON.parseObject(s1, AttributeFunction.class);
                                            //查询function是否存在
                                            boolean b1 = attributeFunctionService.selectIfExist(id1);
                                            //不存在再进行添加
                                            if (!b1){
                                                //添加function
                                                attributeFunctionService.add(attributeFunction);
                                            }


                                            //查找该分类下的映射
                                            for (int j = 0; j < oneLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) oneLevelMapping.get(j);

                                                if (o1.get("attName").equals(name)){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }

                                            for (int j = 0; j < twoLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) twoLevelMapping.get(j);

                                                if (o1.get("attName").equals(name)){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }

                                            for (int j = 0; j < threeLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) threeLevelMapping.get(j);

                                                if (o1.get("attName").equals(name)){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }

                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) fourLevelMapping.get(j);

                                                if (o1.get("attName").equals(name) ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }


                                        }
                                        System.out.println("公共属性添加成功！");


                                        //添加数据：分类、属性、映射（一级分类）

                                        for (int i = 0; i < oneSort.size(); i++) {
                                            JSONObject o1 = (JSONObject) oneSort.get(i);
                                            String s = JSON.toJSONString(o1);
                                            Sort sort = JSON.parseObject(s, Sort.class);

                                            //查询分类名是否重复
                                            boolean b = sortService.selectNameAdd(sort.getName());
                                            //一级分类编码从零件库和产品库中查询，两个产品库不允许有相同的
                                            boolean b1 = sortService.ifCodeExistAddFromTwoTable(sort.getCode(), sort.getLevel(), sort.getParentId());


                                            //先查询编码是否重复
                                            int oneSortId = 0;
                                            boolean b2 = sortService.selectSortIfExist(sort);//查询分类是否存在
                                            if (b2){
                                                //分类存在，根据编码等级父级也相同查询该分类的id
                                                Sort sort1 = sortService.selectSortCode(sort);
                                                oneSortId= sort1.getId();
                                            }
                                            else {
                                                //分类不存在，查看名称是否重复
                                                if (b){
                                                    //名称相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在一级分类中，分类名："+sort.getName()+"在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (b1){
                                                    //编码相同
                                                     check=false;
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("在一级分类中分类名："+sort.getName()+"的分类码在数据库中已存在！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                }
                                                else if (!b && !b1){
                                                    //都不相同
                                                    //编码和名称都不重复然后添加
                                                    sortService.addSortExcel(sort);

                                                    //获取添加后的sortId
                                                    oneSortId = sort.getId();
                                                }
                                            }


                                            if (!check){
                                                break;
                                            }

                                            //寻找分类下面的分类信息
                                            for (int j = 0; j < twoSort.size(); j++) {
                                                JSONObject o = (JSONObject) twoSort.get(j);
                                                Object code = o.get("oneLevelCode");
                                                if (code.equals(o1.get("code"))){
                                                    o.put("parentId",oneSortId);
                                                    JSON.toJSONString(o);
                                                }
                                            }

                                            //寻找该分类下的属性信息
                                            for (int j = 0; j < oneAttributeName.size(); j++) {
                                                JSONObject o = (JSONObject) oneAttributeName.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode())){
                                                    //是该分类下的属性，给该分类下的属性中的parentId修改为刚才添加的分类id
                                                    o.put("parentId", oneSortId);
                                                    JSON.toJSONString(o);
                                                    //

                                                }
                                            }
                                            //寻找该分类下的映射信息
                                            //一级映射查询
                                            for (int j = 0; j < oneMapping.size(); j++) {
                                                JSONObject o = (JSONObject) oneMapping.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode())){
                                                    //是该分类下的映射信息，将映射中的sortId改为刚添加的分类的id
                                                    o.put("sortId", oneSortId);
                                                    JSON.toJSONString(o);



                                                }
                                            }






                                        }
                                        if (!check){
                                            break;
                                        }


                                        System.out.println("这里1");


                                        for (int i = 0; i < twoSort.size(); i++) {
                                            JSONObject o1 = (JSONObject) twoSort.get(i);
                                            String s = JSON.toJSONString(o1);
                                            Sort sort = JSON.parseObject(s, Sort.class);
                                            //查询分类名是否重复
                                            boolean b = sortService.selectNameAdd(sort.getName());
                                            boolean b1 = sortService.ifCodeExistAdd(sort.getCode(), sort.getLevel(), sort.getParentId());
                                            int twoSortId=0;
                                            boolean b2 = sortService.selectSortIfExist(sort);//查询分类是否存在
                                            if (b2){
                                                //分类存在，根据编码等级父级也相同查询该分类的id
                                                Sort sort1 = sortService.selectSortCode(sort);
                                               twoSortId= sort1.getId();
                                            }
                                            else {
                                                //分类不存在，查看名称是否重复
                                                if (b){
                                                    //名称相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在二级分类中，分类名："+sort.getName()+"在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (b1){
                                                    //编码相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在二级分类中分类名："+sort.getName()+"的分类码在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (!b && !b1){
                                                    //都不相同
                                                    //编码和名称都不重复然后添加
                                                    sortService.addSortExcel(sort);

                                                    //获取添加后的sortId
                                                    twoSortId = sort.getId();
                                                }
                                            }



                                            if (!check){
                                                break;
                                            }

                                            //寻找分类下面的分类信息
                                            for (int j = 0; j < threeSort.size(); j++) {
                                                JSONObject o = (JSONObject) threeSort.get(j);
                                                Object code = o.get("twoLevelCode");
                                                if (code.equals(o1.get("code")) && o.get("oneLevelCode").equals(o1.get("oneLevelCode"))){
                                                    o.put("parentId",twoSortId);
                                                    JSON.toJSONString(o);
                                                }
                                            }
                                            //寻找该分类下的属性信息
                                            for (int j = 0; j < twoAttributeName.size(); j++) {
                                                JSONObject o = (JSONObject) twoAttributeName.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode"))){
                                                    //是该分类下的属性，给该分类下的属性中的parentId修改为刚才添加的分类id
                                                    o.put("parentId", twoSortId);
                                                    JSON.toJSONString(o);
                                                    //

                                                }
                                            }
                                            //寻找该分类下的映射信息
                                            for (int j = 0; j < twoMapping.size(); j++) {
                                                JSONObject o = (JSONObject) twoMapping.get(j);
                                                Object code = o.get("code");//二级编码
                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode")) ){
                                                    //是该分类下的映射信息，将映射中的sortId改为刚添加的分类的id
                                                    o.put("sortId", twoSortId);
                                                    JSON.toJSONString(o);


                                                }
                                            }




                                        }
                                        if (!check){
                                            break;
                                        }
                                        System.out.println("这里2");

                                        for (int i = 0; i < threeSort.size(); i++) {
                                            JSONObject o1 = (JSONObject) threeSort.get(i);
                                            String s = JSON.toJSONString(o1);
                                            Sort sort = JSON.parseObject(s, Sort.class);
                                            //查询分类名是否重复
                                            boolean b = sortService.selectNameAdd(sort.getName());
                                            boolean b1 = sortService.ifCodeExistAdd(sort.getCode(), sort.getLevel(), sort.getParentId());
                                            int threeSortId=0;

                                            boolean b2 = sortService.selectSortIfExist(sort);//查询分类是否存在
                                            if (b2){
                                                //分类存在，根据编码等级父级也相同查询该分类的id
                                                Sort sort1 = sortService.selectSortCode(sort);
                                                threeSortId= sort1.getId();
                                            }
                                            else {
                                                //分类不存在，查看名称是否重复
                                                if (b){
                                                    //名称相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在三级分类中，分类名："+sort.getName()+"的在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (b1){
                                                    //编码相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在三级分类中分类名："+sort.getName()+"的分类码在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (!b && !b1){
                                                    //都不相同
                                                    //编码和名称都不重复然后添加
                                                    sortService.addSortExcel(sort);

                                                    //获取添加后的sortId
                                                    threeSortId = sort.getId();
                                                }
                                            }

                                            if (!check){
                                                break;
                                            }
                                            //寻找分类下面的分类信息
                                            for (int j = 0; j < fourSort.size(); j++) {
                                                JSONObject o = (JSONObject) fourSort.get(j);
                                                Object code = o.get("threeLevelCode");
                                                if (code.equals(o1.get("code")) && o1.get("oneLevelCode").equals(o.get("oneLevelCode")) && o1.get("twoLevelCode").equals(o.get("twoLevelCode")) ){
                                                    o.put("parentId",threeSortId);
                                                    JSON.toJSONString(o);
                                                }
                                            }
                                            //寻找该分类下的属性信息
                                            for (int j = 0; j < threeAttributeName.size(); j++) {
                                                JSONObject o = (JSONObject) threeAttributeName.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode")) && o.get("twoLevelCode").equals(o1.get("twoLevelCode")) ){
                                                    //是该分类下的属性，给该分类下的属性中的parentId修改为刚才添加的分类id
                                                    o.put("parentId", threeSortId);
                                                    JSON.toJSONString(o);
                                                    //

                                                }
                                            }
                                            //寻找该分类下的映射信息
                                            for (int j = 0; j < threeMapping.size(); j++) {
                                                JSONObject o = (JSONObject) threeMapping.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode"))  && o.get("twoLevelCode").equals(o1.get("twoLevelCode"))  ){
                                                    //是该分类下的映射信息，将映射中的sortId改为刚添加的分类的id
                                                    o.put("sortId", threeSortId);
                                                    JSON.toJSONString(o);


                                                }
                                            }

                                        }
                                        System.out.println("这里3");
                                        if(!check){
                                            break;
                                        }


                                        for (int i = 0; i < fourSort.size(); i++) {

                                            JSONObject o1 = (JSONObject) fourSort.get(i);
                                            String s = JSON.toJSONString(o1);
                                            Sort sort = JSON.parseObject(s, Sort.class);
                                            //查询分类名是否重复
                                            boolean b = sortService.selectNameAdd(sort.getName());
                                            boolean b1 = sortService.ifCodeExistAdd(sort.getCode(), sort.getLevel(), sort.getParentId());
                                            int fourSortId=0;
                                            boolean b2 = sortService.selectSortIfExist(sort);//查询分类是否存在
                                            if (b2){
                                                //分类存在，根据编码等级父级也相同查询该分类的id
                                                Sort sort1 = sortService.selectSortCode(sort);
                                                fourSortId= sort1.getId();
                                            }
                                            else {
                                                //分类不存在，查看名称是否重复
                                                if (b){
                                                    //名称相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在四级分类中,分类名："+sort.getName()+"在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (b1){
                                                    //编码相同
                                                    check=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在四级分类中分类名："+sort.getName()+"的分类码在数据库中已存在！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }
                                                else if (!b && !b1){
                                                    //都不相同
                                                    sortService.addSortExcel(sort);

                                                    //获取添加后的sortId
                                                    fourSortId = sort.getId();
                                                }
                                            }


                                            if (!check){
                                                break;
                                            }

                                            //寻找该分类下的属性信息
                                            for (int j = 0; j < fourAttributeName.size(); j++) {
                                                JSONObject o = (JSONObject) fourAttributeName.get(j);
                                                Object code = o.get("code");
                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode")) && o.get("twoLevelCode").equals(o1.get("twoLevelCode")) && o.get("threeLevelCode").equals(o1.get("threeLevelCode")) ){
                                                    //是该分类下的属性，给该分类下的属性中的parentId修改为刚才添加的分类id
                                                    o.put("parentId", fourSortId);
                                                    JSON.toJSONString(o);
                                                    //

                                                }
                                            }

                                            //寻找该分类下的映射信息

                                            for (int j = 0; j < fourMapping.size(); j++) {
                                                JSONObject o = (JSONObject) fourMapping.get(j);
                                                Object code = o.get("code");

                                                if (code.equals(sort.getCode()) && o.get("oneLevelCode").equals(o1.get("oneLevelCode"))  && o.get("twoLevelCode").equals(o1.get("twoLevelCode")) && o.get("threeLevelCode").equals(o1.get("threeLevelCode"))  ){
                                                    //是该分类下的映射信息，将映射中的sortId改为刚添加的分类的id
                                                    o.put("sortId", fourSortId);
                                                    JSON.toJSONString(o);


                                                }
                                            }


                                        }
                                        if (!check){
                                            break;
                                        }
                                        System.out.println("这里4");



                                        //分类信息添加完成，接下来添加属性信息
                                        JSONArray productValue= new JSONArray();
                                        for (int i = 0; i < oneAttributeName.size(); i++) {
                                            JSONObject o = (JSONObject) oneAttributeName.get(i);
                                            String s = JSON.toJSONString(o);
                                            AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
                                            //查询该属性信息是否存在
                                            boolean b = attributeService.selectAttNameIfExistExcel(attributeName);
                                            int id1;
                                            if (!b){
                                                //不存在该属性，检测该分类下是否有物料信息存在
                                                int parentId = attributeName.getParentId();
                                                List<Product> products = productService.selectAllInSort(parentId);
                                                if (products.size()==0){
                                                    //该分类下不存在物料信息，直接添加属性
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();




                                                }
                                                else {

                                                    //该分类下面有物料信息,添加完属性信息后，再给物料添加一个空的该属性的信息
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();
                                                    //查询该分类下的物料信息

                                                    for (int l = 0; l < products.size(); l++) {
                                                        JSONObject object1 = new JSONObject();
                                                        Product product1 = products.get(l);

                                                        String s4 = JSON.toJSONString(product1);
                                                        Product product = JSON.parseObject(s4, Product.class);
                                                        int id2 = product.getId();
                                                        object1.put("id", "");
                                                        object1.put("parentId", id1);
                                                        object1.put("productId", id2);
                                                        object1.put("content", "");
                                                        productValue.add(object1);
                                                    }
                                                }

                                            }
                                            else {
                                                //属性信息存在，查询属性信息的id
                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attributeName.getName(),attributeName.getParentId());
                                                id1 = attributeNames1.get(0).getId();

                                                //存在，更新单位
                                                attributeName.setId(id1);
                                                attributeService.updateUnit(attributeName);

                                            }

                                            //添加function信息
                                            JSONObject object = new JSONObject();
                                            object.put("id","");
                                            object.put("attNameId",id1);
                                            object.put("range",0);
                                            object.put("autoCode",0);
                                            String s1 = JSON.toJSONString(object);
                                            AttributeFunction attributeFunction = JSON.parseObject(s1, AttributeFunction.class);
                                            //添加function
                                            //查询是否已经存在function
                                            boolean b1 = attributeFunctionService.selectIfExist(id1);
                                            if (!b1){
                                                attributeFunctionService.add(attributeFunction);
                                            }


                                            //查找该分类下的映射
                                            for (int j = 0; j < oneLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) oneLevelMapping.get(j);
                                                Object code = o1.get("code");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < twoLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) twoLevelMapping.get(j);
                                                Object code = o1.get("oneLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < threeLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) threeLevelMapping.get(j);
                                                Object code = o1.get("oneLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) fourLevelMapping.get(j);
                                                Object code = o1.get("oneLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                        }

                                        for (int i = 0; i < twoAttributeName.size(); i++) {
                                            JSONObject o = (JSONObject) twoAttributeName.get(i);
                                            String s = JSON.toJSONString(o);
                                            AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
                                            //查询该属性信息是否存在
                                            boolean b = attributeService.selectAttNameIfExistExcel(attributeName);
                                            int id1;
                                            if (!b){
                                                //不存在该属性，检测该分类下是否有物料信息存在
                                                int parentId = attributeName.getParentId();
                                                List<Product> products = productService.selectAllInSort(parentId);
                                                if (products.size()==0){
                                                    //该分类下不存在物料信息，直接添加属性
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();




                                                }
                                                else {

                                                    //该分类下面有物料信息,添加完属性信息后，再给物料添加一个空的该属性的信息
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();
                                                    //查询该分类下的物料信息


                                                    for (int l = 0; l < products.size(); l++) {
                                                        JSONObject object1 = new JSONObject();
                                                        Product product1 = products.get(l);

                                                        String s4 = JSON.toJSONString(product1);
                                                        Product product = JSON.parseObject(s4, Product.class);
                                                        int id2 = product.getId();
                                                        object1.put("id", "");
                                                        object1.put("parentId", id1);
                                                        object1.put("productId", id2);
                                                        object1.put("content", "");
                                                        productValue.add(object1);
                                                    }
                                                }
                                            }
                                            else {
                                                //属性存在，获取该属性的id
                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attributeName.getName(), attributeName.getParentId());
                                                id1 = attributeNames1.get(0).getId();

                                            }

                                            //添加function信息
                                            JSONObject object = new JSONObject();
                                            object.put("id","");
                                            object.put("attNameId",id1);
                                            object.put("range",0);
                                            object.put("autoCode",0);
                                            String s1 = JSON.toJSONString(object);
                                            AttributeFunction attributeFunction = JSON.parseObject(s1, AttributeFunction.class);
                                            //添加function
                                            //查询是否已经存在function
                                            boolean b1 = attributeFunctionService.selectIfExist(id1);
                                            if (!b1){
                                                attributeFunctionService.add(attributeFunction);
                                            }

                                            //查找该分类下的映射
                                            for (int j = 0; j < twoLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) twoLevelMapping.get(j);
                                                Object code = o1.get("code");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode"))  ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < threeLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) threeLevelMapping.get(j);
                                                Object code = o1.get("twoLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode"))  ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) fourLevelMapping.get(j);
                                                Object code = o1.get("twoLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode"))  ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                        }

                                        for (int i = 0; i < threeAttributeName.size(); i++) {
                                            JSONObject o = (JSONObject) threeAttributeName.get(i);
                                            String s = JSON.toJSONString(o);
                                            AttributeName attributeName = JSON.parseObject(s, AttributeName.class);
                                            //查询该属性信息是否存在
                                            boolean b = attributeService.selectAttNameIfExistExcel(attributeName);
                                            int id1;
                                            if (!b){
                                                //不存在该属性，检测该分类下是否有物料信息存在
                                                int parentId = attributeName.getParentId();
                                                List<Product> products = productService.selectAllInSort(parentId);
                                                if (products.size()==0){
                                                    //该分类下不存在物料信息，直接添加属性
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();




                                                }
                                                else {

                                                    //该分类下面有物料信息,添加完属性信息后，再给物料添加一个空的该属性的信息
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();
                                                    //查询该分类下的物料信息

                                                    for (int l = 0; l < products.size(); l++) {
                                                        JSONObject object1 = new JSONObject();
                                                        Product product1 = products.get(l);

                                                        String s4 = JSON.toJSONString(product1);
                                                        Product product = JSON.parseObject(s4, Product.class);
                                                        int id2 = product.getId();
                                                        object1.put("id", "");
                                                        object1.put("parentId", id1);
                                                        object1.put("productId", id2);
                                                        object1.put("content", "");
                                                        productValue.add(object1);
                                                    }
                                                }
                                            }
                                            else {
                                                //该属性存在，查询该属性的id
                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attributeName.getName(), attributeName.getParentId());
                                                id1=attributeNames1.get(0).getId();
                                            }
                                            //添加function信息
                                            JSONObject object = new JSONObject();
                                            object.put("id","");
                                            object.put("attNameId",id1);
                                            object.put("range",0);
                                            object.put("autoCode",0);
                                            String s1 = JSON.toJSONString(object);
                                            AttributeFunction attributeFunction = JSON.parseObject(s1, AttributeFunction.class);
                                            //添加function
                                            //查询是否已经存在function
                                            boolean b1 = attributeFunctionService.selectIfExist(id1);
                                            if (!b1){
                                                attributeFunctionService.add(attributeFunction);
                                            }
                                            //查找该分类下的映射
                                            for (int j = 0; j < threeLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) threeLevelMapping.get(j);
                                                Object code = o1.get("code");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode")) && o1.get("twoLevelCode").equals(o.get("twoLevelCode"))  ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) fourLevelMapping.get(j);
                                                Object code = o1.get("threeLevelCode");
                                                if (o1.get("attName").equals(o.get("name")) &&code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode")) && o1.get("twoLevelCode").equals(o.get("twoLevelCode"))  ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);
                                                }
                                            }
                                        }

                                        for (int i = 0; i < fourAttributeName.size(); i++) {
                                            JSONObject o = (JSONObject) fourAttributeName.get(i);

                                            String s = JSON.toJSONString(o);

                                            AttributeName attributeName = JSON.parseObject(s, AttributeName.class);

                                            //查询该属性信息是否存在
                                            boolean b = attributeService.selectAttNameIfExistExcel(attributeName);
                                            int id1;
                                            if (!b){
                                                //不存在该属性，检测该分类下是否有物料信息存在
                                                int parentId = attributeName.getParentId();
                                                List<Product> products = productService.selectAllInSort(parentId);
                                                if (products.size()==0){
                                                    //该分类下不存在物料信息，直接添加属性
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();





                                                }
                                                else {

                                                    //该分类下面有物料信息,添加完属性信息后，再给物料添加一个空的该属性的信息
                                                    attributeService.addAttNameExcelAndId(attributeName);
                                                    //获取添加的属性信息id
                                                    id1 = attributeName.getId();



                                                    //查询该分类下的物料信息


                                                    for (int l = 0; l < products.size(); l++) {
                                                        JSONObject object1 = new JSONObject();
                                                        Product product1 = products.get(l);

                                                        String s4 = JSON.toJSONString(product1);
                                                        Product product = JSON.parseObject(s4, Product.class);
                                                        int id2 = product.getId();
                                                        object1.put("id", "");
                                                        object1.put("parentId", id1);
                                                        object1.put("productId", id2);
                                                        object1.put("content", "");
                                                        productValue.add(object1);
                                                    }
                                                }
                                            }
                                            else {
                                                //该属性存在。查询该属性的id
                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attributeName.getName(), attributeName.getParentId());
                                                id1=attributeNames1.get(0).getId();
                                            }
                                            //添加function信息
                                            JSONObject object = new JSONObject();
                                            object.put("id","");
                                            object.put("attNameId",id1);
                                            object.put("range",0);
                                            object.put("autoCode",0);
                                            String s1 = JSON.toJSONString(object);
                                            AttributeFunction attributeFunction = JSON.parseObject(s1, AttributeFunction.class);
                                            //添加function
                                            //查询是否已经存在function
                                            boolean b1 = attributeFunctionService.selectIfExist(id1);
                                            if (!b1){
                                                attributeFunctionService.add(attributeFunction);
                                            }
                                            //查找该分类下的映射
                                            for (int j = 0; j < fourLevelMapping.size(); j++) {
                                                JSONObject o1 = (JSONObject) fourLevelMapping.get(j);
                                                Object code = o1.get("code");
                                                if (o1.get("attName").equals(o.get("name")) && code.equals(o.get("code"))&&o1.get("oneLevelCode").equals(o.get("oneLevelCode")) && o1.get("twoLevelCode").equals(o.get("twoLevelCode")) && o1.get("threeLevelCode").equals(o.get("threeLevelCode"))   ){
                                                    o1.put("attNameId", id1);
                                                    attributeName.setLength(Integer.parseInt((String) o1.get("length")));
                                                    JSON.toJSONString(o1);

                                                }
                                            }
                                        }

                                        if (productValue.size()>0){
                                            //给已经存在物料信息的添加空的值
                                            String s4 = JSON.toJSONString(productValue);
                                            List<AttributeContent> attributeContents = JSONArray.parseArray(s4, AttributeContent.class);
                                            attributeService.addAttributeContentExcel(attributeContents);
                                        }
                                        System.out.println("属性信息添加完成！");


                                        //属性信息添加完成，添加映射信息


                                        JSONArray addMapping = new JSONArray();
                                        JSONArray updateMapping = new JSONArray();
                                        for (int i = 0; i < oneLevelMapping.size(); i++) {

                                            Object o = oneLevelMapping.get(i);
                                            String s = JSON.toJSONString(o);
                                            Mapping mapping = JSON.parseObject(s, Mapping.class);

                                            //判断当前分类下是否存在物料信息和映射，如果不存在物料信息和映射信息再进行添加
                                            int sortId = mapping.getSortId();
                                            List<Product> products = productService.selectAllInSort(sortId);
                                            boolean b = mappingService.selectMapIfExistExcel(mapping);

                                            if (products.size()==0 ){
                                                if (!b){
                                                    //不存在该映射，添加
                                                    addMapping.add(mapping);
                                                }
                                                else {
                                                    //存在映射，更新
                                                    updateMapping.add(mapping);
                                                }



                                            }


                                        }
                                        System.out.println("一级映射");


                                        for (int i = 0; i < twoLevelMapping.size(); i++) {
                                            Object o = twoLevelMapping.get(i);
                                            String s = JSON.toJSONString(o);
                                            Mapping mapping = JSON.parseObject(s, Mapping.class);

                                            //判断当前分类下是否存在物料信息和映射，如果不存在物料信息和映射信息再进行添加
                                            int sortId = mapping.getSortId();
                                            List<Product> products = productService.selectAllInSort(sortId);
                                            boolean b = mappingService.selectMapIfExistExcel(mapping);

                                            if (products.size()==0 ){
                                                if (!b){
                                                    //不存在该映射，添加
                                                    addMapping.add(mapping);
                                                }
                                                else {
                                                    //存在映射，更新
                                                    updateMapping.add(mapping);
                                                }


                                            }


                                        }
                                        System.out.println("二级映射");
                                        for (int i = 0; i < threeLevelMapping.size(); i++) {
                                            Object o = threeLevelMapping.get(i);
                                            String s = JSON.toJSONString(o);
                                            Mapping mapping = JSON.parseObject(s, Mapping.class);

                                            //判断当前分类下是否存在物料信息和映射，如果不存在物料信息和映射信息再进行添加
                                            int sortId = mapping.getSortId();
                                            List<Product> products = productService.selectAllInSort(sortId);
                                            boolean b = mappingService.selectMapIfExistExcel(mapping);

                                            if (products.size()==0 ){
                                                if (!b){
                                                    //不存在该映射，添加
                                                    addMapping.add(mapping);
                                                }
                                                else {
                                                    //存在映射，更新
                                                    System.out.println("更新"+mapping);
                                                    updateMapping.add(mapping);
                                                }


                                            }


                                        }
                                        System.out.println("三级映射");
                                        for (int i = 0; i < fourLevelMapping.size(); i++) {
                                            Object o = fourLevelMapping.get(i);
                                            String s = JSON.toJSONString(o);
                                            Mapping mapping = JSON.parseObject(s, Mapping.class);

                                            //判断当前分类下是否存在物料信息和映射，如果不存在物料信息和映射信息再进行添加
                                            int sortId = mapping.getSortId();
                                            List<Product> products = productService.selectAllInSort(sortId);
                                            boolean b = mappingService.selectMapIfExistExcel(mapping);


                                            if (products.size()==0 ){
                                                if (!b){
                                                    //不存在该映射，添加
                                                    addMapping.add(mapping);
                                                }
                                                else {
                                                    //存在映射，更新
                                                    updateMapping.add(mapping);
                                                    System.out.println("更新"+mapping);
                                                }


                                            }


                                        }
                                        System.out.println("四级映射");

                                        if (addMapping.size()>0){
                                            String s = JSON.toJSONString(addMapping);


                                            List<Mapping> mappings = JSONArray.parseArray(s, Mapping.class);
                                            System.out.println(mappings);
                                            //添加
                                            mappingService.addMapExcel(mappings);
                                            for (int i = 0; i < mappings.size(); i++) {
                                                Mapping mapping = mappings.get(i);
                                                int attNameId = mapping.getAttNameId();
                                                int length = mapping.getLength();
                                                //更新属性那边的长度
                                                int i1 = attributeService.selectIfLengthExist(attNameId);
                                                if (i1==0){
                                                    //长度为0，修改长度
                                                    attributeService.updateLength(length,attNameId);
                                                }
                                            }
                                            System.out.println("映射添加成功");
                                        }

                                        if (updateMapping.size()>0){
                                            List<Mapping> mappings = JSONArray.parseArray(JSON.toJSONString(updateMapping), Mapping.class);
                                            //先删除该分类下的映射，然后再重新添加这些映射
                                            mappingService.deleteMapExcelNextAdd(mappings);
                                            for (int i = 0; i < mappings.size(); i++) {
                                                Mapping mapping = mappings.get(i);
                                                int attNameId = mapping.getAttNameId();
                                                int length = mapping.getLength();
                                                //更新属性那边的长度
                                                int i1 = attributeService.selectIfLengthExist(attNameId);
                                                if (i1==0){
                                                    //长度为0，修改长度
                                                    attributeService.updateLength(length,attNameId);
                                                }
                                            }
                                            System.out.println(mappings);
                                            System.out.println("映射修改成功");
                                        }


                                    }



                                    if (!oneLevelSortPass){
                                        break;
                                    }
                                    response.setContentType("text/json;charset=utf-8");
                                    response.getWriter().write("一键导入成功！");
                                    FileUtil.delFile(filePath);



//




                                    workbook.close();

                                } catch (BiffException | IOException e) {
//                                    e.printStackTrace();
                                    FileUtil.delFile(filePath);
                                }

                            }

                            else{
                                FileUtil.delFile(filePath);
                                response.setContentType("text/json;charset=utf-8");
                                response.getWriter().write("请再次导入！");
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




