package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.AttributeFunction;
import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.AttributeValue;
import com.hongbang.pojo.Sort;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;
import com.hongbang.util.Compare;
import com.hongbang.util.FileUtil;
import com.hongbang.util.RegexMatches;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet("/fileAttributeCodeServlet/*")
public class FileAttributeCodeServlet extends HttpServlet {

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


                                try {
                                    //获取一个Excel文件  只支持.xls格式
                                    workbook = Workbook.getWorkbook(new File(filePath));
                                    //获取到一共有多少个表
                                    Sheet[] sheets = workbook.getSheets();
                                    boolean pass=true;
                                    //sheet.getCell(列，行);
                                    // sheets[i].getRows();行
//                                        sheets[i].getColumns();列
                                    JSONArray attributeFunctionAdd = new JSONArray();
                                    JSONArray attributeValueAdd=new JSONArray();
                                    JSONArray attributeFunctionUpdate = new JSONArray();
                                    JSONArray attributeValueUpdate=new JSONArray();


                                    for (int i = 0; i < sheets.length; i++) {
                                        String name = sheets[i].getName().trim();//每个表格的名字
                                        int rows = sheets[i].getRows();//该表格的行数
                                        int columns = sheets[i].getColumns();//该表格的列数
                                        //获取表中的分类码名，然后查询分类码名和表格名是否一致，然后根据分类码查询该分类是否存在

                                        for (int j = 0; j < columns; j++) {
                                            String trim = sheets[i].getCell(j, 0).getContents().trim();
                                            if (trim.equals("分类码")){

                                                //当前列是分类码列查询该列中是否存在分类码
                                                String trim1 = sheets[i].getCell(j, 1).getContents().trim();//找到分类码
                                                if (trim1.equals(name) && trim1.length()>0){
                                                    System.out.println("分类码一致");
                                                }
                                                else {
                                                    System.out.println(trim1);
                                                    System.out.println(name);
                                                    //分类码不相同，提示不相同
                                                    pass=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在表  "+name+"  中的分类码与表名不一致！");
                                                    FileUtil.delFile(filePath);
                                                }
                                                break;

                                            }

                                        }
                                        if (!pass){
                                            break;
                                        }

                                        if (name.equals("公共属性编码")){

                                            //分类码对应的分类在数据库中存在，查询属性是否存在
                                            int i2 = columns / 4;//对应着有几个属性
                                            for (int k = 1; k < i2; k++) {
                                                for (int z = 0; z < columns; z++) {
                                                    String sx = sheets[i].getCell(z, 0).getContents().trim();
                                                    if (sx.equals("属性"+k)){
                                                        String cd = sheets[i].getCell(z  + 1, 0).getContents().trim();//长度
                                                        String zlb = sheets[i].getCell(z  + 2, 0).getContents().trim();//值列表
                                                        String zbm = sheets[i].getCell(z  + 3, 0).getContents().trim();//值编码
                                                        System.out.println(cd);
                                                        if (!cd.equals("长度")){

                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的长度列！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }

                                                        if (!zlb.contains("值列表")){
                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的值列表列！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }

                                                        if (!zbm.contains("值编码")){
                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的值编码列！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }
                                                        if (!pass){
                                                            break;
                                                        }

                                                        //找到了属性列
                                                        JSONObject function = new JSONObject();//记录这属性的方法

                                                        String excelAttributeName = sheets[i].getCell(z , 1).getContents().trim();//对应着表格中的属性名
                                                        String excelAttributeLength = sheets[i].getCell(z  + 1, 1).getContents().trim();//对应着表格中属性的占位即长度
                                                        boolean excelAttributeRange = sheets[i].getCell(z  + 2, 0).getContents().trim().contains("$");;//对应着表格中属性的类型，是值还是范围 $为范围，值不加任何信息
                                                        boolean excelAttributeAutoCode1 = sheets[i].getCell(z  + 3, 0).getContents().trim().contains("&");//对应着该属性的编码是否开启了值为编码的功能
                                                        boolean excelAttributeAutoCode2 = sheets[i].getCell(z  + 3, 0).getContents().trim().contains("#");//对应着该属性的编码是否开启了顺序编码功能
                                                        //根据该属性所在的分类id以及属性名称查询该属性书否存在
                                                        if (excelAttributeName.length()>0){

                                                            List<AttributeName> attributeNames = attributeService.selectNameIdExcel(excelAttributeName, 0);
                                                            int havaAttributeName=0;//属性值的数量/是否存在
                                                            int haveAttributeCode=0;//属性值对应的编码数量/是否存在

                                                            if (attributeNames.size()>0 && excelAttributeLength.length()>0){
                                                                //该属性存在，查询长度
                                                                int length = attributeNames.get(0).getLength();
                                                                System.out.println("属性名长度"+length);
                                                                System.out.println("excel长度"+excelAttributeName);
                                                                boolean exist=false;

                                                                if (length == Integer.parseInt(excelAttributeLength)){
                                                                    //跟属性名中的长度相同
                                                                    exist=true;

                                                                }

                                                                else {
                                                                    System.out.println("不相同");
                                                                    //跟属性名中的长度不同，查询跟映射中的长度是否相同
                                                                    if (length==0){
                                                                        //属性名中的长度为0
                                                                        int id1 = attributeNames.get(0).getId();
                                                                        List<Map<String, Object>> maps = mappingService.selectLength(id1);


                                                                        if (maps.size()>0){
                                                                            //存在映射中
                                                                            Map<String, Object> map = maps.get(0);
                                                                            System.out.println(map);
                                                                            Object length1 = map.get("length");

                                                                            if (length1.equals(Integer.parseInt(excelAttributeLength))){
                                                                                //如果相同
                                                                                exist=true;
                                                                            }
                                                                            else {
                                                                                //长度不相同
                                                                                exist=false;
                                                                                pass=false;
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  长度和数据库中的长度不一致！");
                                                                                FileUtil.delFile(filePath);
                                                                                break;
                                                                            }
                                                                        }
                                                                        else {
                                                                            //映射中也没查询到，按照excel表上为准,更新attributeName上面的长度
                                                                            System.out.println("映射中没有，属性名中也没有");

                                                                            attributeService.updateLength(Integer.parseInt(excelAttributeLength),id1);
                                                                            exist=true;

                                                                        }
                                                                    }
                                                                    else {
                                                                        //长度不相同
                                                                        exist=false;
                                                                        pass=false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+name+"  中,属性："+excelAttributeName+"长度和数据库中的长度不一致！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }


                                                                }

                                                                if (!exist|| !pass){
                                                                    break;
                                                                }
                                                                System.out.println("1");
                                                                //长度相同，在数据库中查找该属性是值还是范围
                                                                int id1 = attributeNames.get(0).getId();//该属性的id
                                                                function.put("attNameId",id1);
                                                                //通过属性id去查询该属性在数据库中记录的function，另外一张表导入的时候会添加一个默认的，所有一定会存在一个
                                                                List<AttributeFunction> attributeFunctions = attributeFunctionService.selectByAttNameId(id1);
                                                                System.out.println(attributeFunctions);
                                                                if (attributeFunctions.size()>0){
                                                                    //存在function，说明之前添加过，获取范围还是值，如果是范围的进行判断

                                                                    //一致，是范围的或者是值的
                                                                    System.out.println(excelAttributeRange);
                                                                    System.out.println(attributeFunctions.get(0).getRange()==1);
                                                                    if (excelAttributeRange){
                                                                        //范围的，判断属性信息是否重复，以及符合要求
                                                                        function.put("range",1);
                                                                        function.put("autoCode", 0);



                                                                    }
                                                                    else {
                                                                        //是值
                                                                        function.put("range",0);
                                                                        //判断方法
                                                                        if (excelAttributeAutoCode1){
                                                                            function.put("autoCode",1);

                                                                        }
                                                                        else if (excelAttributeAutoCode2){
                                                                            function.put("autoCode",2);
                                                                        }
                                                                        else if (!excelAttributeAutoCode1 && !excelAttributeAutoCode2)  {
                                                                            function.put("autoCode",0);
                                                                        }
                                                                    }


                                                                    //获取attributeFunction
                                                                }



                                                                attributeFunctionAdd.add(function);


                                                                System.out.println("2");
                                                                //判断属性信息是否重复
                                                                for (int l = 1; l < rows; l++) {
                                                                    String trim2 = sheets[i].getCell(z  + 2, l).getContents().trim();//该属性的属性值
                                                                    String trim3 = sheets[i].getCell(z  + 3, l).getContents().trim();//该属性的属性值对应的编码
                                                                    if (trim2.length()>0){
                                                                        havaAttributeName++;
                                                                    }
                                                                    if (trim3.length()>0){
                                                                        haveAttributeCode++;
                                                                    }
                                                                    if (havaAttributeName>0 && haveAttributeCode>0){
                                                                        break;
                                                                    }


                                                                }

                                                                System.out.println("4");
                                                                if (havaAttributeName>0 && haveAttributeCode>0){
                                                                    //只有两种情况，要么就是，值和编码都存在，要么就是都不存在，如果是值或者编码只有一个存在，那就报错
                                                                    System.out.println("5");

                                                                    //都存在，查询属性值对应的编码或者编码对应的属性值为空
                                                                    for (int l = 1; l < rows; l++) {
                                                                        String trim2 = sheets[i].getCell(z  + 2, l).getContents().trim();//该属性的属性值
                                                                        String trim3 = sheets[i].getCell(z  + 3, l).getContents().trim();//该属性的属性值对应的编码
                                                                System.out.println("属性值："+trim2+"编码"+trim3);


                                                                        if (trim2.length()>0 && trim3.length()==0){
                                                                            System.out.println("6");
                                                                            pass=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的值:  "+trim2+"  对应的编码为空！");
                                                                            FileUtil.delFile(filePath);

                                                                            break;
                                                                        }

                                                                        if (trim3.length()>0 && trim2.length()==0 ){
                                                                            System.out.println("7");
                                                                            pass=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+name+"  中,属性："+excelAttributeName+"的编码:  "+trim3+"  对应的值为空！");
                                                                            FileUtil.delFile(filePath);

                                                                            break;
                                                                        }




                                                                        //验证属性值或者编码值是否重复，以及值是否符合范围的要求，编码长度是否符合要求
                                                                        for (int m = 1; m < rows; m++) {
                                                                            String trim4 = sheets[i].getCell(z  + 2, m).getContents().trim();//该属性的属性值
                                                                            String trim5 = sheets[i].getCell(z  + 3, m).getContents().trim();//该属性的属性值对应的编码
                                                                            //范围的
                                                                            if (trim2.length()>0 && trim3.length()>0 ){
                                                                                if (l!=m){

                                                                                    if (trim3.length()==attributeNames.get(0).getLength() || trim3.length()== Integer.parseInt(excelAttributeLength)){
                                                                                        //长度符合
                                                                                        if (trim2.equals(trim4) || trim3.equals(trim5)){
                                                                                            if (trim2.equals(trim4)){
                                                                                                pass=false;
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("在  表"+name+"  中,属性：  "+excelAttributeName+"  的值:  "+trim2+"  在表中重复！");
                                                                                                FileUtil.delFile(filePath);
                                                                                                FileUtil.delFile(filePath);
                                                                                                break;
                                                                                            }
                                                                                            else {
                                                                                                pass=false;
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("在  表"+name+"  中,属性：  "+excelAttributeName+"  的编码:  "+trim3+"  在表中重复！");
                                                                                                FileUtil.delFile(filePath);
                                                                                                FileUtil.delFile(filePath);
                                                                                                break;
                                                                                            }
                                                                                        }

                                                                                        if (!pass){
                                                                                            break;
                                                                                        }

                                                                                        //如果是范围的就多加几条判断条件
                                                                                        if (excelAttributeRange){
                                                                                            //判断是否符合范围的要求
                                                                                            boolean main = RegexMatches.main(trim2);
                                                                                            if (main){
                                                                                                //符合范围条件，判断范围是否重叠
                                                                                                if (l!=m && trim2.length()>0 && trim4.length()>0 ){
                                                                                                    boolean b1 = Compare.compare3(trim2, trim4);
                                                                                                    if (b1){
                                                                                                        pass=false;
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的范围"+trim2+"和"+trim4+"重叠！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }
                                                                                                }

                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                //不符合范围条件

                                                                                                System.out.println(trim2);
                                                                                                pass=false;
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的值:  "+trim2+"  不符合范围的格式！");
                                                                                                FileUtil.delFile(filePath);
                                                                                                FileUtil.delFile(filePath);
                                                                                                break;

                                                                                            }
                                                                                        }


                                                                                        if (!pass){
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    else {
                                                                                        //长度不符合
                                                                                        pass=false;
                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                        response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的编码:  "+trim3+"  不符合长度规则！");
                                                                                        FileUtil.delFile(filePath);

                                                                                        break;
                                                                                    }





                                                                                }
                                                                            }



                                                                        }
                                                                        if (!pass){
                                                                            break;
                                                                        }
                                                                        JSONObject object = new JSONObject();
                                                                        object.put("id","");
                                                                        object.put("attNameId",id1);
                                                                        object.put("code",trim3);
                                                                        object.put("attValue",trim2);
                                                                        //查询该属性信息是否存在
                                                                        String s = JSON.toJSONString(object);
                                                                        AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
                                                                        boolean b = attributeValueService.selectValueExistAdd(attributeValue);
                                                                        boolean b1 = attributeValueService.selectCodeExistAdd(attributeValue);


                                                                        if (!b && !b1 && trim3.length()>0 && trim2.length()>0){
                                                                            if (excelAttributeRange){
                                                                                //如果是范围的，查询是否跟数据库中的范围重叠
                                                                                //查询该属性下的属性编码信息
                                                                                List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(attributeValue.getAttNameId());
                                                                                if (attributeValues.size()>0){
                                                                                    for (int j = 0; j < attributeValues.size(); j++) {
                                                                                        AttributeValue attributeValue1 = attributeValues.get(j);
                                                                                        String attValue = attributeValue1.getAttValue();//从数据库获取的数据
                                                                                        boolean b2 = Compare.compare3(attValue, trim2);
                                                                                        System.out.println(attValue +"和"+ trim2+"结果为："+b2);
                                                                                        if (b2){
                                                                                            //重叠
                                                                                            pass=false;
                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的属性值:  "+attributeValue.getAttValue()+"  在数据库与数据库中的属性值：  "+attValue+"   范围重叠！");
                                                                                            FileUtil.delFile(filePath);

                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    if (!pass){
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    //两个都不重复,范围的，当前属性没有编码信息
                                                                                    attributeValueAdd.add(object);
                                                                                }
                                                                                if (!pass){
                                                                                    break;
                                                                                }
                                                                            }
                                                                            else {
                                                                                //不是范围的
                                                                                //两个都不重复
                                                                                attributeValueAdd.add(object);
                                                                            }
                                                                            if (!pass){
                                                                                break;
                                                                            }

                                                                        }
                                                                        else if (b && !b1 && trim3.length()>0 && trim2.length()>0){
                                                                            //属性值与数据库中的重复,但是编码不重复
                                                                            pass=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的属性值:  "+attributeValue.getAttValue()+"  在数据库中已存在！");
                                                                            FileUtil.delFile(filePath);

                                                                            break;
                                                                        }else if (!b && b1 && trim3.length()>0 && trim2.length()>0){
                                                                            //属性值与数据库中不重复，但是编码重复
                                                                            pass=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的编码:  "+attributeValue.getCode()+"  在数据库中已存在！");
                                                                            FileUtil.delFile(filePath);

                                                                            break;
                                                                        }



                                                                    }
                                                                    if (!pass){
                                                                        break;
                                                                    }



                                                                }
                                                                System.out.println("10");
                                                                System.out.println("这里啊");
                                                            }
                                                            else {
                                                                if (attributeNames.size()==0){
                                                                    //该属性不存在
                                                                    pass=false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  在数据库中不存在！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;
                                                                }
                                                                else if (excelAttributeLength.length()==0){
                                                                    //占位未填写
                                                                    pass=false;
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  占位未填写！");
                                                                    FileUtil.delFile(filePath);
                                                                    break;
                                                                }

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



                                            }

                                        }
                                        else {
                                            //分类码相同，查询该分类吗是否存在
                                            int i1 = (name.length()-1)/2;
                                            //获取分类码的首位：一级分类码
                                            String substring = name.substring(0, 1);
                                            //查询一级分类是否存在
                                            List<Sort> sorts = sortService.selectByCode(1,substring,0);
                                            if (sorts.size()>0){
                                                //存在该分类，获取分类id
                                                int oneSortId = sorts.get(0).getId();//编码中一级分类编码对应的分类id
                                                int twoSortId = 0;//编码中二级分类编码对应的分类id
                                                int threeSortId = 0;//编码中三级分类编码对应的分类id
                                                int fourSortId = 0;//编码中四级分类编码对应的分类id
                                                System.out.println(name);
                                                for (int k = 0; k < i1; k++) {
                                                    if (k==0){
                                                        //获取二级分类的编码
                                                        String substring1 = name.substring(1, 3);
                                                        //根据编码查询分类id
                                                        List<Sort> sorts1 = sortService.selectByCode(2, substring1, oneSortId);
                                                        if (sorts1.size()>0){

                                                            twoSortId  = sorts1.get(0).getId();
                                                        }
                                                        else {
                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中,分类码:  "+substring1+"  对应的分类在数据库中不存在！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }
                                                    }
                                                    else if (k==1){
                                                        //获取三级分类编码
                                                        String substring1 = name.substring(3, 5);
                                                        //根据编码查询分类id
                                                        List<Sort> sorts1 = sortService.selectByCode(3, substring1, twoSortId);

                                                        if (sorts1.size()>0){

                                                            threeSortId  = sorts1.get(0).getId();
                                                        }
                                                        else {
                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中,分类码:  "+substring1+"  对应的分类在数据库中不存在！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }

                                                    }
                                                    else if (k==2){
                                                        //获取四级分类编码
                                                        String substring1 = name.substring(5);
                                                        //根据编码查询分类id
                                                        List<Sort> sorts1 = sortService.selectByCode(4, substring1, threeSortId);
                                                        if (sorts1.size()>0){

                                                            fourSortId  = sorts1.get(0).getId();
                                                        }
                                                        else {
                                                            pass=false;
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("在表  "+name+"  中,分类码:  "+substring1+"  对应的分类在数据库中不存在！");
                                                            FileUtil.delFile(filePath);
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (!pass){
                                                    break;
                                                }
                                                System.out.println("分类码："+oneSortId);
                                                System.out.println("分类码："+twoSortId);
                                                System.out.println("分类码："+threeSortId);
                                                System.out.println("分类码："+fourSortId);


                                                //分类码对应的分类在数据库中存在，查询属性是否存在
                                                int i2 = columns  / 4;//对应着有几个属性
                                                for (int k = 1; k <= i2; k++) {
                                                    for (int z = 0; z <columns ; z++) {
                                                        String sx = sheets[i].getCell(z, 0).getContents().trim();

                                                        if (sx.equals("属性"+k)){
                                                            String cd = sheets[i].getCell(z  + 1, 0).getContents().trim();//长度
                                                            String zlb = sheets[i].getCell(z  + 2, 0).getContents().trim();//值列表
                                                            String zbm = sheets[i].getCell(z  + 3, 0).getContents().trim();//值编码
                                                            if (!cd.equals("长度")){
                                                                pass=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的长度列！");
                                                                FileUtil.delFile(filePath);
                                                                break;
                                                            }

                                                            if (!zlb.contains("值列表")){
                                                                pass=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的值列表列！");
                                                                FileUtil.delFile(filePath);
                                                                break;
                                                            }

                                                            if (!zbm.contains("值编码")){
                                                                pass=false;
                                                                response.setContentType("text/json;charset=utf-8");
                                                                response.getWriter().write("在表  "+name+"  中未找到列： 属性"+k+" 对应的值编码列！");
                                                                FileUtil.delFile(filePath);
                                                                break;
                                                            }

                                                            JSONObject function = new JSONObject();//记录这属性的方法

                                                            String excelAttributeName = sheets[i].getCell(z , 1).getContents().trim();//对应着表格中的属性名
                                                            String excelAttributeLength = sheets[i].getCell(z+1, 1).getContents().trim();//对应着表格中属性的占位即长度
                                                            boolean excelAttributeRange = sheets[i].getCell( z + 2, 0).getContents().trim().contains("$");;//对应着表格中属性的类型，是值还是范围 ￥为范围，值不加任何信息
                                                            boolean excelAttributeAutoCode1 = sheets[i].getCell(z  + 3, 0).getContents().trim().contains("&");//对应着该属性的编码是否开启了值为编码的功能
                                                            boolean excelAttributeAutoCode2 = sheets[i].getCell(z  +3, 0).getContents().trim().contains("#");//对应着该属性的编码是否开启了顺序编码功能
                                                            //根据该属性所在的分类id以及属性名称查询该属性书否存在
//                                                            System.out.println(excelAttributeName);
//                                                            System.out.println(i2);
                                                            if (excelAttributeName.length()>0){

                                                                int sortId = 0;
                                                                if (name.length()==1){
                                                                    sortId = oneSortId;
                                                                }
                                                                else if (name.length()==3){
                                                                    sortId = twoSortId;
                                                                }
                                                                else if (name.length()==5){
                                                                    sortId = threeSortId;
                                                                }
                                                                else if (name.length()==7){
                                                                    sortId = fourSortId;
                                                                }

                                                                List<AttributeName> attributeNames = attributeService.selectNameIdExcel(excelAttributeName, sortId);
                                                                int havaAttributeName=0;//属性值的数量/是否存在
                                                                int haveAttributeCode=0;//属性值对应的编码数量/是否存在

                                                                if (attributeNames.size()>0 && excelAttributeLength.length()>0){
                                                                    //该属性存在，查询长度
                                                                    int length = attributeNames.get(0).getLength();
                                                                    System.out.println("属性名长度"+length);
                                                                    System.out.println("excel长度"+excelAttributeLength);
                                                                    boolean exist=false;

                                                                    if (length == Integer.parseInt(excelAttributeLength)){
                                                                        exist=true;

                                                                    }

                                                                    else {
                                                                        //查询映射中的长度是否相同
                                                                        if (length==0){
                                                                            int id1 = attributeNames.get(0).getId();

                                                                            List<Map<String, Object>> maps = mappingService.selectLength(id1);


                                                                            if (maps.size()>0){
                                                                                Map<String, Object> map = maps.get(0);

                                                                                Object length1 = map.get("length");

                                                                                if (length1.equals(Integer.parseInt(excelAttributeLength))){
                                                                                    //如果相同
                                                                                    exist=true;
                                                                                }
                                                                                else {
                                                                                    //长度不相同
                                                                                    exist=false;
                                                                                    pass=false;
                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  长度和数据库中的长度不一致！");
                                                                                    FileUtil.delFile(filePath);
                                                                                    break;
                                                                                }
                                                                            }
                                                                            else {
                                                                                System.out.println("都没有");
                                                                                //映射中也没有
                                                                                attributeService.updateLength(Integer.parseInt(excelAttributeLength),id1);
                                                                                exist=true;
                                                                            }
                                                                        }
                                                                        else {
                                                                            //长度不相同
                                                                            exist=false;
                                                                            pass=false;
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  长度和数据库中的长度不一致！");
                                                                            FileUtil.delFile(filePath);
                                                                            break;
                                                                        }


                                                                    }

                                                                    if (!exist|| !pass){
                                                                        break;
                                                                    }
                                                                    System.out.println("1");
                                                                    //长度相同，在数据库中查找该属性是值还是范围
                                                                    int id1 = attributeNames.get(0).getId();//该属性的id
                                                                    function.put("attNameId",id1);
                                                                    //通过属性id去查询该属性在数据库中记录的function
                                                                    List<AttributeFunction> attributeFunctions = attributeFunctionService.selectByAttNameId(id1);
                                                                    System.out.println(attributeFunctions);
                                                                    if (attributeFunctions.size()>0){
                                                                        //存在function，说明之前添加过，获取范围还是值，如果是范围的进行判断

                                                                        //是范围的或者是值的
                                                                        System.out.println(excelAttributeRange);
                                                                        System.out.println(attributeFunctions.get(0).getRange()==1);
                                                                        if (excelAttributeRange){
                                                                            //范围的，判断属性信息是否重复，以及符合要求
                                                                            function.put("range",1);
                                                                            function.put("autoCode", 0);



                                                                        }
                                                                        else {
                                                                            //是值
                                                                            function.put("range",0);
                                                                            //判断方法
                                                                            if (excelAttributeAutoCode1){
                                                                                function.put("autoCode",1);

                                                                            }
                                                                            else if (excelAttributeAutoCode2){
                                                                                function.put("autoCode",2);
                                                                            }
                                                                            else if (!excelAttributeAutoCode1 && !excelAttributeAutoCode2) {
                                                                                function.put("autoCode",0);
                                                                            }
                                                                        }


                                                                        //获取attributeFunction
                                                                    }


                                                                    else {
                                                                        //不存在，要重新添加
                                                                        if (excelAttributeRange){
                                                                            //范围的，判断属性信息是否重复，以及符合要求
                                                                            function.put("range",1);
                                                                            function.put("autoCode", 0);



                                                                        }
                                                                        else {
                                                                            //是值
                                                                            function.put("range",0);
                                                                            //判断方法
                                                                            if (excelAttributeAutoCode1){
                                                                                function.put("autoCode",1);

                                                                            }
                                                                            else if (excelAttributeAutoCode2){
                                                                                function.put("autoCode",2);
                                                                            }
                                                                            else if (!excelAttributeAutoCode1 && !excelAttributeAutoCode2)  {
                                                                                function.put("autoCode",0);
                                                                            }

                                                                        }


                                                                    }
                                                                    attributeFunctionAdd.add(function);


                                                                    System.out.println("2");
                                                                    //判断属性信息是否重复
                                                                    for (int l = 1; l < rows; l++) {
                                                                        String trim2 = sheets[i].getCell(z  + 2, l).getContents().trim();//该属性的属性值
                                                                        String trim3 = sheets[i].getCell(z  + 3, l).getContents().trim();//该属性的属性值对应的编码
                                                                        if (trim2.length()>0){
                                                                            havaAttributeName++;
                                                                        }
                                                                        if (trim3.length()>0){
                                                                            haveAttributeCode++;
                                                                        }
                                                                        if (havaAttributeName>0 && haveAttributeCode>0){
                                                                            break;
                                                                        }


                                                                    }

                                                                    System.out.println("4");
                                                                    if (havaAttributeName>0 && haveAttributeCode>0){
                                                                        //只有两种情况，要么就是，值和编码都存在，要么就是都不存在，如果是值或者编码只有一个存在，那就报错
                                                                        System.out.println("5");

                                                                        //都存在，查询属性值对应的编码或者编码对应的属性值为空
                                                                        for (int l = 1; l < rows; l++) {
                                                                            String trim2 = sheets[i].getCell(z  + 2, l).getContents().trim();//该属性的属性值
                                                                            String trim3 = sheets[i].getCell(z  + 3, l).getContents().trim();//该属性的属性值对应的编码
                                                                    System.out.println("属性值："+trim2+"编码"+trim3);


                                                                            if (trim2.length()>0 && trim3.length()==0){
                                                                                System.out.println("6");
                                                                                pass=false;
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的值:  "+trim2+"  对应的编码为空！");
                                                                                FileUtil.delFile(filePath);

                                                                                break;
                                                                            }

                                                                            if (trim3.length()>0 && trim2.length()==0 ){
                                                                                System.out.println("7");
                                                                                pass=false;
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"的  编码:  "+trim3+"  对应的值为空！");
                                                                                FileUtil.delFile(filePath);

                                                                                break;
                                                                            }



                                                                            //验证属性值或者编码值是否重复，以及值是否符合范围的要求，编码长度是否符合要求
                                                                            for (int m = 1; m < rows; m++) {
                                                                                String trim4 = sheets[i].getCell(z  + 2, m).getContents().trim();//该属性的属性值
                                                                                String trim5 = sheets[i].getCell(z  + 3, m).getContents().trim();//该属性的属性值对应的编码
                                                                                //范围的
                                                                                if (trim2.length()>0 && trim3.length()>0 ){
                                                                                    if (l!=m){

                                                                                        if (trim3.length()==attributeNames.get(0).getLength() || trim3.length()== Integer.parseInt(excelAttributeLength)){
                                                                                            //长度符合
                                                                                            if (trim2.equals(trim4) || trim3.equals(trim5)){
                                                                                                if (trim2.equals(trim4)){
                                                                                                    pass=false;
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"的  值:  "+trim2+"  在表中重复！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                                else {
                                                                                                    pass=false;
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的编码:  "+trim3+"  在表中重复！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                            }

                                                                                            if (!pass){
                                                                                                break;
                                                                                            }

                                                                                            //如果是范围的就多加几条判断条件
                                                                                            if (excelAttributeRange){
                                                                                                //判断是否符合范围的要求
                                                                                                boolean main = RegexMatches.main(trim2);
                                                                                                if (main){
                                                                                                    //符合范围条件，判断范围是否重复
                                                                                                    if (l!=m && trim2.length()>0 && trim4.length()>0 ){
                                                                                                        boolean b1 = Compare.compare3(trim2, trim4);
                                                                                                        if (b1){
                                                                                                            pass=false;
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的范围  "+trim2+"和"+trim4+"  重叠！");
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            break;
                                                                                                        }
                                                                                                    }

                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                    //不符合范围条件

                                                                                                    System.out.println(trim2);
                                                                                                    pass=false;
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的值:  "+trim2+"  不符合范围的格式！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;

                                                                                                }
                                                                                            }


                                                                                            if (!pass){
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        else {
                                                                                            //长度不符合
                                                                                            pass=false;
                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                            response.getWriter().write("在表  "+name+"  中,属性："+excelAttributeName+"的编码"+trim3+"不符合长度规则！");
                                                                                            FileUtil.delFile(filePath);
                                                                                            FileUtil.delFile(filePath);
                                                                                            break;
                                                                                        }





                                                                                    }
                                                                                }



                                                                            }
                                                                            if (!pass){
                                                                                break;
                                                                            }
                                                                            JSONObject object = new JSONObject();
                                                                            object.put("id","");
                                                                            object.put("attNameId",id1);
                                                                            object.put("code",trim3);
                                                                            object.put("attValue",trim2);
                                                                            //查询该属性信息是否存在
                                                                            String s = JSON.toJSONString(object);
                                                                            AttributeValue attributeValue = JSON.parseObject(s, AttributeValue.class);
                                                                            boolean b = attributeValueService.selectValueExistAdd(attributeValue);
                                                                            boolean b1 = attributeValueService.selectCodeExistAdd(attributeValue);
                                                                            if (!b && !b1 && trim3.length()>0 && trim2.length()>0){
                                                                                if (excelAttributeRange){
                                                                                    //如果是范围的，查询是否跟数据库中的范围重叠
                                                                                    //查询该属性下的属性编码信息
                                                                                    List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(attributeValue.getAttNameId());
                                                                                    if (attributeValues.size()>0){
                                                                                        for (int j = 0; j < attributeValues.size(); j++) {
                                                                                            AttributeValue attributeValue1 = attributeValues.get(j);
                                                                                            String attValue = attributeValue1.getAttValue();//从数据库获取的数据
                                                                                            boolean b2 = Compare.compare3(attValue, trim2);
                                                                                            System.out.println(attValue +"和"+ trim2+"结果为："+b2);
                                                                                            if (b2){
                                                                                                //重叠
                                                                                                pass=false;
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的属性值:  "+attributeValue.getAttValue()+"  在数据库与数据库中的属性值：  "+attValue+"   范围重叠！");
                                                                                                FileUtil.delFile(filePath);

                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if (!pass){
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                    else {
                                                                                        //两个都不重复,范围的，当前属性没有编码信息
                                                                                        attributeValueAdd.add(object);
                                                                                    }
                                                                                    if (!pass){
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    //是值，两个都不重复
                                                                                    attributeValueAdd.add(object);
                                                                                }
                                                                                if (!pass){
                                                                                    break;
                                                                                }

                                                                            }
                                                                            else if (b && !b1 && trim3.length()>0 && trim2.length()>0){
                                                                                //属性值与数据库中的重复,但是编码不重复
                                                                                pass=false;
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的属性值:  "+attributeValue.getAttValue()+"  在数据库中已存在！");
                                                                                FileUtil.delFile(filePath);

                                                                                break;
                                                                            }
                                                                            else if (!b && b1 && trim3.length()>0 && trim2.length()>0){
                                                                                //属性值与数据库中不重复，但是编码重复
                                                                                pass=false;
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  的编码:  "+attributeValue.getCode()+"  在数据库中已存在！");
                                                                                FileUtil.delFile(filePath);

                                                                                break;
                                                                            }



                                                                        }
                                                                        if (!pass){
                                                                            break;
                                                                        }



                                                                    }
                                                                    System.out.println("10");
                                                                    System.out.println("这里啊");
                                                                    if (!pass){
                                                                        break;
                                                                    }
                                                                }
                                                                else {
                                                                    if (attributeNames.size()==0){
                                                                        //该属性不存在
                                                                        pass=false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  在数据库中不存在！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }
                                                                    else if (excelAttributeLength.length()==0){
                                                                        //占位未填写
                                                                        pass=false;
                                                                        response.setContentType("text/json;charset=utf-8");
                                                                        response.getWriter().write("在表  "+name+"  中,属性：  "+excelAttributeName+"  占位未填写！");
                                                                        FileUtil.delFile(filePath);
                                                                        break;
                                                                    }

                                                                }
                                                            }

                                                        }
                                                    }


                                                }


                                            }
                                            else {
                                                //不存在分类提示分类码对应的分类在数据库中不存在
                                                pass=false;
                                                response.setContentType("text/json;charset=utf-8");
                                                response.getWriter().write("在表  "+name+"  中,分类码:  "+substring+"  对应的分类在数据库中不存在！");
                                                FileUtil.delFile(filePath);
                                                break;
                                            }
                                            if (!pass){
                                                break;
                                            }
                                        }
                                        if (!pass){
                                            break;
                                        }
                                    }


                                    if (!pass){
                                        break;
                                    }


                                    //循环添加属性信息和function
                                    String s = JSON.toJSONString(attributeFunctionAdd);
                                    List<AttributeFunction> attributeFunctions = JSONArray.parseArray(s, AttributeFunction.class);


                                    System.out.println(attributeFunctions);
                                    System.out.println(attributeFunctionAdd);

                                    if (attributeFunctions.size()>0){
                                        for (int i = 0; i < attributeFunctions.size(); i++) {
                                            AttributeFunction attributeFunction = attributeFunctions.get(i);
                                            int attNameId = attributeFunction.getAttNameId();
                                            //根据属性id查询该属性下是否存在编码信息
                                            boolean b = attributeValueService.selectIfCode(attNameId);

                                            if (b){
                                                //如果存在编码，查询该function是否与excel里的一致，如果不一致报警

                                                //查询数据库中的function
                                                List<AttributeFunction> attributeFunctions1 = attributeFunctionService.selectByAttNameId(attNameId);
                                                AttributeFunction attributeFunction1 = attributeFunctions1.get(0);
                                                int range = attributeFunction1.getRange();

                                                //获取excel表中的表示值或者范围的数据
                                                int range1 = attributeFunction.getRange();

                                                if (range!=range1){
                                                    //查询该属性信息
                                                    List<AttributeName> attributeNames = attributeService.selectById(attNameId);
                                                    String name = attributeNames.get(0).getName();
                                                    //查询该属性所在的分类
                                                    int parentId = attributeNames.get(0).getParentId();
                                                    List<Sort> sorts = sortService.selectSortById(parentId);
                                                    String name1 = sorts.get(0).getName();
                                                    //提示当前分类下有物料信息，但是数据库中的值类型不同
                                                    pass=false;
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("在分类：  "+name1+"  下的属性：  "+name+"  存在编码信息，类型不可修改！");
                                                    FileUtil.delFile(filePath);
                                                    break;
                                                }


                                            }

                                        }


                                    }
                                    if (!pass){
                                        break;
                                    }

                                    //添加function
                                    if (attributeFunctions.size()>0){
                                        for (int i = 0; i < attributeFunctions.size(); i++) {
                                            AttributeFunction attributeFunction = attributeFunctions.get(i);

                                            attributeFunctionService.update(attributeFunction);

                                        }

                                        System.out.println("function添加成功");
                                    }

                                    //function添加成功再添加属性值和编码
                                    String s1 = JSON.toJSONString(attributeValueAdd);
                                    List<AttributeValue> attributeValues = JSONArray.parseArray(s1, AttributeValue.class);

                                    System.out.println(attributeValueAdd);
                                    System.out.println(attributeValues);
                                    //修改function还要查询是否有物料信息
                                    if (attributeValues.size()>0){
                                        attributeValueService.addCodeAuto(attributeValues);
                                        System.out.println("属性值和编码添加成功");
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



                            else {
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
