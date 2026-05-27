package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.hongbang.pojo.*;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;
import com.hongbang.util.Compare;
import com.hongbang.util.FileUtil;

import com.hongbang.util.OrderCode;
import com.hongbang.util.RegexMatches;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import jxl.Cell;
import jxl.Sheet;



import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/fileImport")
public class FileImportServlet extends HttpServlet {

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
                                    Sheet sheet;
                                    Cell cell ;
                                    JSONArray jsons = new JSONArray();
                                    JSONArray addjsons = new JSONArray();
                                    JSONArray addjsonslast = new JSONArray();
                                    JSONArray updatejsons = new JSONArray();
                                    JSONArray alljsons = new JSONArray();//记录所有的分类，判断是否有重复的
                                    JSONArray common = new JSONArray();//普通属性，添加function时使用

                                    try {
                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));
                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        for(int i = 0; i < sheets.length; i++) {
                                            //第一个表格  //sheet.getCell(列，行);
//                                            String name5 = sheets[i].getName();获取表格的名称
                                            boolean two = false;
                                            boolean three = false;
                                            boolean bgThree=false;
                                            if (i==0){
                                                //获取行数
                                                int rows = sheets[i].getRows();
                                                System.out.println("行数:"+rows);
                                                //获取列数
                                                int columns = sheets[i].getColumns();
                                                System.out.println("列数:"+columns);

                                                Cell[] levelOne = new Cell[rows];
                                                Cell[] levelOneCode = new Cell[rows];
                                                Cell[] levelTwo = new Cell[rows];
                                                Cell[] levelTwoCode = new Cell[rows];
                                                Cell[] levelThree = new Cell[rows];
                                                Cell[] levelThreeCode = new Cell[rows];
                                                Cell[] levelFour = new Cell[rows];
                                                Cell[] levelFourCode = new Cell[rows];
                                                int idOne = 0;
                                                int idTwo = 0;
                                                int idThree = 0;


                                                //查询一级分类的内容
                                                int one = 1;
                                                int oneHave=0;
                                                for (int z = 1; z < rows; z++) {
                                                    levelOne[0] = sheets[i].getCell(1, z);
                                                    levelOneCode[0] = sheets[i].getCell(2, z);
                                                    String contents = levelOne[0].getContents().trim();
                                                    String code=levelOneCode[0].getContents().trim();
                                                    if ((contents!=null && !contents.equals(""))&& (code!=null&& !code.equals(""))){
                                                        oneHave++;
                                                        if (oneHave>1){
                                                            break;
                                                        }
                                                        //每一行创建一个JSONObject对象
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parent_id",0);
                                                        object.put("name",contents);
                                                        object.put("level",1);
                                                        object.put("code",code);


                                                        //加入json队列
                                                        jsons.add(object);
                                                        alljsons.add(object);

                                                        //查询二级分类内容
                                                        int twos=1;
                                                        int twosHave=0;
                                                        for (int y = 1; y < rows; y++) {
                                                            levelTwo[0] = sheets[i].getCell(3, y);
                                                            levelTwoCode[0] = sheets[i].getCell(4, y);
                                                            String name=levelTwo[0].getContents().trim();
                                                            String code2 = levelTwoCode[0].getContents().trim();
                                                            JSONArray twojsons = new JSONArray();
                                                            if ((name!=null&&!name.equals(""))&&(code2!=null&&!code2.equals(""))){
                                                                twosHave++;
                                                                if (twosHave>1){
                                                                    break;
                                                                }

                                                                //每一行创建一个JSONObject对象
                                                                JSONObject object2 = new JSONObject();
                                                                object2.put("id", "");
                                                                object2.put("name",name);
                                                                object2.put("level",2);
                                                                object2.put("code",code2);


                                                                //加入json队列
                                                                alljsons.add(object2);


                                                                //查询三级分类的内容
                                                                //获取到二级分类添加的id，下面执行三级分类
                                                                int threes = 1;
                                                                int threesHave=0;
                                                                for (int x = 1; x < rows; x++) {
                                                                    levelThree[0] = sheets[i].getCell(5, x);
                                                                    levelThreeCode[0] = sheets[i].getCell(6, x);
                                                                    String name3=levelThree[0].getContents().trim();
                                                                    String code3 = levelThreeCode[0].getContents().trim();
                                                                    JSONArray threejsons = new JSONArray();
                                                                    if ((name3!=null&&!name3.equals(""))&&(code3!=null&& !code3.equals(""))){
                                                                        threesHave++;
                                                                        if (threesHave>1){
                                                                            break;
                                                                        }

                                                                        //每一行创建一个JSONObject对象
                                                                        JSONObject object3 = new JSONObject();
                                                                        object3.put("id", "");
                                                                        object3.put("name",name3);
                                                                        object3.put("level",3);
                                                                        object3.put("code",code3);

                                                                        //加入json队列
                                                                        alljsons.add(object3);

                                                                        //查询四级分类内容信息
                                                                        JSONArray fourjsonss = new JSONArray();
                                                                        //获取到了添加的三级分类的id，下面进行四级分类的添加操作
                                                                        for (int k = 1; k < rows; k++) {
                                                                            JSONObject objects4 = new JSONObject();
                                                                            levelFour[k] = sheets[i].getCell(7, k);
                                                                            levelFourCode[k] = sheets[i].getCell(8, k);
                                                                            String name4 = levelFour[k].getContents().trim();
                                                                            String code4=levelFourCode[k].getContents().trim();
                                                                            if (name4!=null&& !name4.equals("") && code4!=null&& !code4.equals("")){
                                                                                    //每一行创建一个JSONObject对象
                                                                                    objects4.put("id", "");
                                                                                    objects4.put("name",name4);
                                                                                    objects4.put("level",4);
                                                                                    objects4.put("code",code4);
                                                                                    fourjsonss.add(objects4);
                                                                                    //加入json队列
                                                                                    alljsons.add(objects4);
                                                                            }

                                                                        }
                                                                        if (fourjsonss.size()>0){
                                                                            //检测数据是否重复
                                                                            boolean eq=true;
                                                                            String as = JSON.toJSONString(alljsons);
                                                                            List<Sort> sorts = JSONArray.parseArray(as, Sort.class);
                                                                            //查看所有的分类名是否重复
                                                                            for (int j = 0; j < sorts.size(); j++) {
                                                                                Sort sort = sorts.get(j);
                                                                                String name1 = sort.getName();
                                                                                for (int k = 0; k < sorts.size(); k++) {
                                                                                    if (k!=j){
                                                                                        Sort sort1 = sorts.get(k);
                                                                                        String name2 = sort1.getName();
                                                                                        if (name2.equals(name1)){
                                                                                            eq=false;

                                                                                        }
                                                                                    }
                                                                                    if (!eq){
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                if (!eq){
                                                                                    break;
                                                                                }
                                                                            }
                                                                            if (!eq){
                                                                                //存在重复信息
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("分类中存在重复信息!");
                                                                                FileUtil.delFile(filePath);

                                                                            }
                                                                            else {
                                                                                //不存在重复信息，查询编码是否相同(这里只检测的四级的)
                                                                                String s7 = JSON.toJSONString(fourjsonss);
                                                                                List<Sort> sorts1 = JSONArray.parseArray(s7, Sort.class);
                                                                                for (int j = 0; j <sorts1.size(); j++) {
                                                                                    Sort sort = sorts1.get(j);
                                                                                    String code1 = sort.getCode();
                                                                                    for (int k = 0; k < sorts1.size(); k++) {

                                                                                        if (j!=k){
                                                                                            Sort sort1 = sorts1.get(k);
                                                                                            String code4 = sort1.getCode();
                                                                                            if (code1.equals(code4)&&!code1.equals("") && code1!=null){
                                                                                                eq=false;
                                                                                            }
                                                                                        }
                                                                                        if (!eq){
                                                                                            break;
                                                                                        }
                                                                                    }


                                                                                }

                                                                                if (!eq){
                                                                                    //存在重复信息
                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                    response.getWriter().write("分类中编码存在重复信息!");
                                                                                    FileUtil.delFile(filePath);

                                                                                }
                                                                                else {
                                                                                    boolean nameRepeat=true;
                                                                                    boolean codeRepeat=true;
                                                                                    //得到所有的一级分类，检测该分类是否存在，执行添加操作
                                                                                    for (int j = 0; j < jsons.size(); j++) {
                                                                                        Object o = jsons.get(j);
                                                                                        String s = JSON.toJSONString(o);
                                                                                        Sort sort = JSON.parseObject(s, Sort.class);
                                                                                        //查询分类是否存在,调用service
                                                                                        boolean b = sortService.selectSortIfExist(sort);
                                                                                        if (b){
                                                                                            //如果b为真，证明存在，先执行查询操作，查询该分类的id
                                                                                            String name1 = sort.getName();
                                                                                            idOne = sortService.selectIdExcel(name1);




                                                                                        }else {
                                                                                            //b为假，不存在，查询分类名称或者编码是否有被使用，都未被使用才能添加
                                                                                            //查询名称是否重复
                                                                                            boolean b1 = sortService.selectSortNameIfExist(sort);
                                                                                            if (!b1){
                                                                                                //不重复，查询是否编码重复
                                                                                                boolean b2 = sortService.selectSortCodeIfExist(sort);
                                                                                                if (!b2){
                                                                                                    //不重复
                                                                                                    sortService.addSortExcel(sort);
                                                                                                    idOne= sort.getId();
                                                                                                }else {
                                                                                                    //编码重复
                                                                                                    codeRepeat = false;


                                                                                                }
                                                                                                if (!codeRepeat){
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("一级分类："+sort.getName()+"中的编码值："+sort.getCode()+"已被使用！");
                                                                                                    System.out.println("一级分类："+sort.getName()+"中的编码值："+sort.getCode()+"已被使用！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                            }else {
                                                                                                //分类名重复
                                                                                                nameRepeat=false;

                                                                                            }
                                                                                            if (!nameRepeat){
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("一级分类："+sort.getName()+"中的分类名："+sort.getName()+"已被使用！");
                                                                                                System.out.println("一级分类："+sort.getName()+"中的分类名："+sort.getName()+"已被使用！");
                                                                                                FileUtil.delFile(filePath);
                                                                                                break;
                                                                                            }

                                                                                        }
                                                                                        if (!codeRepeat){
                                                                                            break;
                                                                                        }
                                                                                        if (!nameRepeat){
                                                                                            break;
                                                                                        }
                                                                                        if (nameRepeat && codeRepeat){
                                                                                            //获取到了刚添加的一级分类的id下面进行二级分类的操作
                                                                                            object2.put("parent_id",idOne);

                                                                                            //加入json队列
                                                                                            twojsons.add(object2);
                                                                                            //查询是否重复
                                                                                            Object o2 = twojsons.get(0);
                                                                                            String ts = JSON.toJSONString(o2);
                                                                                            Sort sort2 = JSON.parseObject(ts, Sort.class);

                                                                                            //查询分类是否存在,调用service
                                                                                            boolean b2 = sortService.selectSortIfExist(sort2);
                                                                                            if (!b2){

                                                                                                //查询分类名是否重复
                                                                                                boolean br = sortService.selectSortNameIfExist(sort2);
                                                                                                if (!br){
                                                                                                    //不重复,查看编码是否重复
                                                                                                    boolean bt = sortService.selectSortCodeIfExist(sort2);
                                                                                                    if (!bt){
                                                                                                        //不重复
                                                                                                        //不存在，执行添加操作
                                                                                                        sortService.addSortExcel(sort2);
                                                                                                        idTwo = sort2.getId();
                                                                                                    }else {
                                                                                                        //重复
                                                                                                        codeRepeat = false;
                                                                                                    }
                                                                                                    if (!codeRepeat){
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("二级分类："+sort2.getName()+"中的编码值："+sort2.getCode()+"已被使用！");
                                                                                                        System.out.println("二级分类："+sort2.getName()+"中的编码值："+sort2.getCode()+"已被使用！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }
                                                                                                }else {
                                                                                                    //重复
                                                                                                    nameRepeat = false;
                                                                                                }
                                                                                                if (!nameRepeat){
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("二级分类："+sort2.getName()+"中的分类名："+sort2.getName()+"已被使用！");
                                                                                                    System.out.println("二级分类："+sort2.getName()+"中的分类名："+sort2.getName()+"已被使用！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                            }else {
                                                                                                //存在，查询id
                                                                                                String name1 = sort2.getName();
                                                                                                idTwo = sortService.selectIdExcel(name1);

                                                                                            }
                                                                                            if (!codeRepeat){
                                                                                                break;
                                                                                            }
                                                                                            if (!nameRepeat){
                                                                                                break;
                                                                                            }
                                                                                            if (nameRepeat && codeRepeat){
                                                                                                //每一行创建一个JSONObject对象

                                                                                                object3.put("parent_id",idTwo);

                                                                                                threejsons.add(object3);
                                                                                                //查询是否重复
                                                                                                Object o3 = threejsons.get(0);
                                                                                                String s3 = JSON.toJSONString(o3);
                                                                                                Sort sort3 = JSON.parseObject(s3, Sort.class);

                                                                                                boolean b3 = sortService.selectSortIfExist(sort3);
                                                                                                if (!b3){

                                                                                                    //查询分类名是否重复
                                                                                                    boolean by = sortService.selectSortNameIfExist(sort3);
                                                                                                    if (!by){
                                                                                                        //不重复，查询编码是否重复
                                                                                                        boolean bu = sortService.selectSortCodeIfExist(sort3);
                                                                                                        if (!bu){
                                                                                                            //不重复
                                                                                                            //不存在，执行添加操作
                                                                                                            sortService.addSortExcel(sort3);
                                                                                                            idThree= sort3.getId();
                                                                                                        }else {
                                                                                                            //编码重复
                                                                                                            codeRepeat = false;
                                                                                                        }
                                                                                                        if (!codeRepeat){
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("二级分类："+sort3.getName()+"中的编码值："+sort3.getCode()+"已被使用！");
                                                                                                            System.out.println("三级分类："+sort3.getName()+"中的编码值："+sort3.getCode()+"已被使用！");
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            break;
                                                                                                        }
                                                                                                    }else {
                                                                                                        //重复
                                                                                                        nameRepeat = false;
                                                                                                    }
                                                                                                    if (!nameRepeat){
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("三级分类："+sort3.getName()+"中的分类名："+sort3.getName()+"已被使用！");
                                                                                                        System.out.println("三级分类："+sort3.getName()+"中的分类名："+sort3.getName()+"已被使用！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }
                                                                                                }else {
                                                                                                    //存在,查询信息
                                                                                                    String name1 = sort3.getName();
                                                                                                    idThree = sortService.selectIdExcel(name1);

                                                                                                }

                                                                                                if (!codeRepeat){
                                                                                                    break;
                                                                                                }
                                                                                                if (!nameRepeat){
                                                                                                    break;
                                                                                                }
                                                                                                if (nameRepeat && codeRepeat){
                                                                                                    JSONArray fourjsons = new JSONArray();
                                                                                                    //获取到了添加的三级分类的id，下面进行四级分类的添加操作
                                                                                                    for (int k = 1; k < rows; k++) {
                                                                                                        levelFour[k] = sheets[i].getCell(7, k);
                                                                                                        levelFourCode[k] = sheets[i].getCell(8, k);
                                                                                                        String name4 = levelFour[k].getContents().trim();
                                                                                                        String code4=levelFourCode[k].getContents().trim();

                                                                                                        if (name4!=null&& !name4.equals("")){
                                                                                                            if (code4!=null&&!code4.equals("")){
                                                                                                                //每一行创建一个JSONObject对象
                                                                                                                JSONObject object4 = new JSONObject();
                                                                                                                object4.put("id", "");
                                                                                                                object4.put("parent_id",idThree);
                                                                                                                object4.put("name",name4);
                                                                                                                object4.put("level",4);
                                                                                                                object4.put("code",code4);
                                                                                                                fourjsons.add(object4);
                                                                                                            }
                                                                                                        }

                                                                                                    }

                                                                                                    //不存在重复元素
                                                                                                    for (int k = 0; k <fourjsons.size() ; k++) {
                                                                                                        Object o4 = fourjsons.get(k);
                                                                                                        String s4 = JSON.toJSONString(o4);
                                                                                                        Sort sort4 = JSON.parseObject(s4, Sort.class);

                                                                                                        //查询是否重复
                                                                                                        boolean b4 = sortService.selectSortIfExist(sort4);
                                                                                                        if (!b4){
                                                                                                            //不重复
                                                                                                            addjsons.add(o4);
                                                                                                        }else {
                                                                                                            //重复
                                                                                                            updatejsons.add(o4);
                                                                                                        }
                                                                                                    }
                                                                                                    //得到了所有要添加的以及要更新的四级分类的数据
                                                                                                    if (addjsons.size()>0){
                                                                                                        //如果大于0就执行添加操作
                                                                                                        for (int k = 0; k < addjsons.size(); k++) {
                                                                                                            Object o1 = addjsons.get(k);
                                                                                                            String s1 = JSON.toJSONString(o1);
                                                                                                            Sort sort4 = JSON.parseObject(s1, Sort.class);
                                                                                                            //查询分类名是否重复
                                                                                                            boolean b1 = sortService.selectSortNameIfExist(sort4);
                                                                                                            if (!b1){
                                                                                                                //不重复，检测编码
                                                                                                                boolean b4 = sortService.selectSortCodeIfExist(sort4);
                                                                                                                if (!b4){
                                                                                                                    //不重复
                                                                                                                    addjsonslast.add(sort4);

                                                                                                                }else {
                                                                                                                    codeRepeat = false;
                                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                                    response.getWriter().write("四级分类："+sort4.getName()+"中的编码值："+sort4.getCode()+"已被使用！");
                                                                                                                    System.out.println("四级分类："+sort4.getName()+"中的编码值："+sort4.getCode()+"已被使用！");
                                                                                                                    FileUtil.delFile(filePath);
                                                                                                                    break;
                                                                                                                }
                                                                                                            }else {
                                                                                                                nameRepeat = false;
                                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                                response.getWriter().write("四级分类级分类："+sort4.getName()+"中的分类名："+sort4.getName()+"已被使用！");
                                                                                                                System.out.println("四级分类："+sort4.getName()+"中的分类名："+sort4.getName()+"已被使用！");
                                                                                                                FileUtil.delFile(filePath);
                                                                                                                break;
                                                                                                            }
                                                                                                        }
                                                                                                        if (nameRepeat && codeRepeat){
                                                                                                            //名称和编码都不重复，测试等级是否有四个
                                                                                                            int i1 = codeService.selectCount();
                                                                                                            if (i1<4){
                                                                                                                two=false;
                                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                                response.getWriter().write("系统设定的分类等级数少于4，与文档不符！");
                                                                                                                System.out.println("系统设定的分类等级数少于4，与文档不符！");
                                                                                                            }else {
                                                                                                                String ss = addjsonslast.toJSONString();
                                                                                                                List<Sort> addsorts = JSONArray.parseArray(ss, Sort.class);
                                                                                                                sortService.addSortFourExcel(addsorts);
                                                                                                                System.out.println("添加分类成功");
                                                                                                                two=true;
                                                                                                            }

                                                                                                        }




                                                                                                    }else {
                                                                                                        if (updatejsons.size()>0){
                                                                                                            two=true;
                                                                                                        }

                                                                                                    }



                                                                                                }
                                                                                            }


                                                                                        }




                                                                                        if (two && nameRepeat && codeRepeat){
                                                                                            //如果two是true,获取属性表的信息
                                                                                            //获取行数,这里面使用分类表中的行数，两张表行数不同会导致，代码加载失败
                                                                                            System.out.println("下面是属性表的信息");
                                                                                            int rows0 = sheets[0].getRows();
                                                                                            int rows1=sheets[1].getRows();
//                                                                            if (rows0>rows1){
//                                                                                rows=rows1;
//                                                                            }else if (rows0<rows1){
//                                                                                rows=rows0;
//                                                                            }else {
//                                                                                rows=rows0;
//                                                                            }
                                                                                            System.out.println("行数:"+rows0);
                                                                                            //获取列数
                                                                                            columns = sheets[1].getColumns();
                                                                                            System.out.println("列数:"+columns);
                                                                                            JSONArray array = new JSONArray();
                                                                                            //根据分类id去分类表中查询该分类名称，然后去数据库中查询id作为属性的parentid
                                                                                            int mnb=0;
                                                                                            String con="";
                                                                                            for (int k = 1; k < rows1; k++) {
                                                                                                Cell cell2 = sheets[1].getCell(0, k);
                                                                                                String contents2 = cell2.getContents().trim();

                                                                                                if (contents2!=null && !contents2.equals("")){
                                                                                                    System.out.println("属性表"+contents2);
                                                                                                    for (int l = 1; l < rows0; l++) {


                                                                                                        //获取分类表中的分类码
                                                                                                        Cell cell1 = sheets[0].getCell(0, l);
                                                                                                        String contents1 = cell1.getContents().trim();

                                                                                                        if (contents1!=null && !contents1.equals("") && contents1.equals(contents2)){
                                                                                                            mnb=0;
                                                                                                            mnb++;
                                                                                                        }
                                                                                                    }
                                                                                                    if (mnb==0){
                                                                                                        con=contents2;
                                                                                                        bgThree = true;
                                                                                                        break;
                                                                                                    }


                                                                                                }


                                                                                                if (bgThree){
                                                                                                    break;
                                                                                                }


                                                                                            }

                                                                                            if (!bgThree){
                                                                                                for (int k = 1; k < rows; k++) {

                                                                                                    //获取属性表中的分类码
                                                                                                    Cell cell1 = sheets[0].getCell(0, k);
                                                                                                    String contents1 = cell1.getContents().trim();
                                                                                                    if (contents1!=null && !contents1.equals("")){
                                                                                                        //获取分类表中的分类码
                                                                                                        for (int bb = 1; bb < rows1; bb++) {

                                                                                                            Cell cell2 = sheets[1].getCell(0, bb);
                                                                                                            String contents2 = cell2.getContents().trim();


                                                                                                            if (contents2!=null && !contents2.equals("") && contents1.equals(contents2)){

                                                                                                                //两者都不为空，并且相同，然后获取分类表中该分类的名称和编码
                                                                                                                Cell cell3 = sheets[0].getCell(7, k);
                                                                                                                String names = cell3.getContents().trim();

                                                                                                                //根据names查询id作为属性名的parentId
                                                                                                                int parentId = sortService.selectIdExcel(names);
                                                                                                                //然后获取该分类的属性信息

                                                                                                                int num=(columns-1)/2;
                                                                                                                for (int l = 0; l <num; l++) {
                                                                                                                    Cell cell4 = sheets[1].getCell(2*l+1, bb);
                                                                                                                    Cell cell5 = sheets[1].getCell(2*l+2, bb);
                                                                                                                    String attributeName = cell4.getContents().trim();
                                                                                                                    String attributeUnit = cell5.getContents().trim();
                                                                                                                    //单位可以为空，属性名不能不为空
                                                                                                                    if (attributeName!=null && !attributeName.equals("")){

                                                                                                                        JSONObject object1 = new JSONObject();
                                                                                                                        object1.put("id", "");
                                                                                                                        object1.put("parentId", parentId);
                                                                                                                        object1.put("name",attributeName);
                                                                                                                        object1.put("unit", attributeUnit);

                                                                                                                        int length=0;
                                                                                                                        //获取长度
                                                                                                                        //先获取当前的分类码
                                                                                                                        Cell cell6 = sheets[1].getCell(0, bb);
                                                                                                                        String trim = cell6.getContents().trim();

                                                                                                                        for (int m = 2; m < sheets.length; m++) {
                                                                                                                            //获取每张编码表中的分类码
                                                                                                                            Cell cell7 = sheets[m].getCell(0, 1);
                                                                                                                            String trim1 = cell7.getContents().trim();
                                                                                                                            boolean mm=false;
                                                                                                                            if (trim1.equals(trim)){
                                                                                                                                //如果分类编码相同，那就是储存这张表的信息,然后获取长度
                                                                                                                                int columns1 = sheets[m].getColumns();
                                                                                                                                int num1 = (columns1-1)/4;
                                                                                                                                boolean kk = false;
                                                                                                                                for (int n = 0; n < num1; n++) {
                                                                                                                                    Cell cell8 = sheets[m].getCell(4 * n + 2, 1);
                                                                                                                                    String trim2 = cell8.getContents().trim();
                                                                                                                                    if (trim2!=null && !trim2.equals("")){
                                                                                                                                        if (trim2.equals(attributeName)){
                                                                                                                                            //如果想听证明是同一个属性，然后获取长度
                                                                                                                                            Cell cell9 = sheets[m].getCell(4 * n + 1, 1);
                                                                                                                                            String trim3 = cell9.getContents().trim();
                                                                                                                                            length = Integer.parseInt(trim3);
                                                                                                                                            kk=true;
                                                                                                                                        }
                                                                                                                                    }

                                                                                                                                }
                                                                                                                                if (!kk){
                                                                                                                                    //说明后面没有这个属性，这个是普通属性，要设置function
//                                                                                                                System.out.println("普通属性"+attributeName);
                                                                                                                                    object1.put("length",length);
                                                                                                                                    common.add(object1);
                                                                                                                                }
                                                                                                                                mm=true;
                                                                                                                            }

                                                                                                                            if (mm){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                        }
                                                                                                                        object1.put("length",length);
                                                                                                                        array.add(object1);

                                                                                                                    }



                                                                                                                }


                                                                                                            }




                                                                                                        }



                                                                                                    }



                                                                                                }


                                                                                                String s1 = JSON.toJSONString(array);
                                                                                                List<AttributeName> attributeNames = JSONArray.parseArray(s1, AttributeName.class);
                                                                                                JSONArray attributeNameAdd = new JSONArray();
                                                                                                JSONArray attributeNameUpdate= new JSONArray();
                                                                                                //查询是否存在，不存在再执行添加操作

                                                                                                for (int k = 0; k < attributeNames.size(); k++) {
                                                                                                    Object o1 = attributeNames.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                    boolean b1 = attributeService.selectAttNameIfExistExcel(attributeName);
                                                                                                    if (!b1){
                                                                                                        attributeNameAdd.add(o1);
                                                                                                    }else {
                                                                                                        attributeNameUpdate.add(o1);
                                                                                                    }

                                                                                                }
//                                                        System.out.println("所有的"+attributeNameAdd);
                                                                                                JSONArray havaProduct = new JSONArray();//有物料的
                                                                                                JSONArray noneProduct = new JSONArray();//没有物料的
                                                                                                if (attributeNameAdd.size()>0){
                                                                                                    //大于0进行添加,查询该分类下面是否有物料存在
                                                                                                    for (int k = 0; k < attributeNameAdd.size(); k++) {
                                                                                                        Object o1 = attributeNameAdd.get(k);
                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                        AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                        int parentId = attributeName.getParentId();

                                                                                                        List<Product> products = productService.selectProductExistExcel(parentId);

                                                                                                        if (products.size()>0){
                                                                                                            havaProduct.add(o1);
                                                                                                        }else {
                                                                                                            noneProduct.add(o1);
                                                                                                        }

                                                                                                    }


                                                                                                    //先添加没有物料信息的
                                                                                                    if (noneProduct.size()>0){
                                                                                                        String s2 = JSON.toJSONString(noneProduct);
                                                                                                        List<AttributeName> attributeNames1 = JSONArray.parseArray(s2, AttributeName.class);
                                                                                                        attributeService.addAttNameExcel(attributeNames1);
                                                                                                    }
                                                                                                    System.out.println("没有物料的属性信息添加成功！");
                                                                                                    //再添加存在物料信息的，存在物料的添加成功后获取新添加属性名id，以及物料id
                                                                                                    if (havaProduct.size()>0){
                                                                                                        JSONArray productValue= new JSONArray();
                                                                                                        for (int k = 0; k < havaProduct.size(); k++) {
                                                                                                            Object o1 = havaProduct.get(k);
                                                                                                            String s2 = JSON.toJSONString(o1);
                                                                                                            AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                            attributeService.addAttNameExcelAndId(attributeName);
                                                                                                            int id1 = attributeName.getId();//得到了添加信息后的属性名id
                                                                                                            int parentId = attributeName.getParentId();

                                                                                                            //查询该分类下的物料信息
                                                                                                            List<Product> products = productService.selectProductExistExcel(parentId);

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
                                                                                                        //添加空的值
                                                                                                        String s4 = JSON.toJSONString(productValue);
                                                                                                        List<AttributeContent> attributeContents = JSONArray.parseArray(s4, AttributeContent.class);
                                                                                                        attributeService.addAttributeContentExcel(attributeContents);

                                                                                                    }


                                                                                                    three=true;

                                                                                                }else {
                                                                                                    if (attributeNameUpdate.size()>0){
                                                                                                        three=true;
                                                                                                    }

                                                                                                }

                                                                                                System.out.println("添加成功！");
                                                                                            }
                                                                                            else {
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("分类表中未找到属性表的分类码："+con+"");
                                                                                                System.out.println("分类表中未找到属性表的分类码："+con+"");
                                                                                                FileUtil.delFile(filePath);
                                                                                            }



                                                                                            if (three){
                                                                                                JSONArray array2 = new JSONArray();//编码表
                                                                                                JSONArray array3 = new JSONArray();//function表

                                                                                                //先给普通属性添加function
                                                                                                for (int k = 0; k < common.size(); k++) {
                                                                                                    Object o1 = common.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                    String name1 = attributeName.getName();
                                                                                                    int parentId = attributeName.getParentId();
                                                                                                    //查询名称id
                                                                                                    List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(name1, parentId);
                                                                                                    AttributeName attributeName1 = attributeNames1.get(0);
                                                                                                    int id1 = attributeName1.getId();
                                                                                                    JSONObject object1 = new JSONObject();
                                                                                                    object1.put("id","");
                                                                                                    object1.put("att_name_id",id1);
                                                                                                    object1.put("range",0);
                                                                                                    object1.put("auto_code",0);

                                                                                                    array3.add(object1);
                                                                                                }


                                                                                                //进行属性值信息的添加
                                                                                                JSONArray attCode= new JSONArray();
                                                                                                JSONArray upCode= new JSONArray();
                                                                                                JSONArray addMapping= new JSONArray();
                                                                                                JSONArray upMapping= new JSONArray();
                                                                                                JSONArray addFunction= new JSONArray();
                                                                                                JSONArray upFunction= new JSONArray();
                                                                                                boolean map=true;
                                                                                                boolean repeat=true;
                                                                                                boolean nn=true;
                                                                                                boolean nn1 = true;
                                                                                                boolean mm = true;
                                                                                                boolean bb=true;
                                                                                                boolean cc=true;
                                                                                                boolean dd = true;
                                                                                                boolean ee = true;
                                                                                                boolean rr = true;
                                                                                                boolean co = true;
                                                                                                boolean co3 = true;
                                                                                                for (int k = 2; k < sheets.length; k++) {
                                                                                                    //前两个表是分类表和属性表,后面的是编码表
                                                                                                    System.out.println("表："+sheets[k].getName());
                                                                                                    int rows2 = sheets[k].getRows();
                                                                                                    int columns1 = sheets[k].getColumns();
                                                                                                    int i1 = (columns1 - 1) / 4;


                                                                                                    //获取属性值和编码信息
                                                                                                    //占位
                                                                                                    int sum=0;
                                                                                                    for (int l = 0; l < i1; l++) {

                                                                                                        //获取占位
                                                                                                        Cell cell1 = sheets[k].getCell(l * 4 + 1, 1);
                                                                                                        String length = cell1.getContents().trim();

                                                                                                        if (length!=null && !length.equals("")){
                                                                                                            sum+=Integer.parseInt(length);
                                                                                                        }


                                                                                                        //获取属性名
                                                                                                        Cell cell2 = sheets[k].getCell(l * 4 + 2, 1);
                                                                                                        String attName = cell2.getContents().trim();


                                                                                                        //查询该属性的属性id
                                                                                                        //先获取分类码
                                                                                                        Cell cell4 = sheets[k].getCell(0, 1);
                                                                                                        String contents2 = cell4.getContents().trim();

                                                                                                        //去分类表中查询该分类码对应的分类名,获取分类表中的所有分类编码
                                                                                                        int rows3 = sheets[0].getRows();
                                                                                                        int ss=0;
                                                                                                        boolean sw = true;

                                                                                                        for (int m = 1; m <rows3 ; m++) {

                                                                                                            Cell cell3 = sheets[0].getCell(0, m);
                                                                                                            String contents1 = cell3.getContents().trim();
                                                                                                            if (contents1!=null && !contents1.equals("")){
                                                                                                                if (contents2.equals(contents1)){
                                                                                                                    ss=m;
                                                                                                                    sw=false;
                                                                                                                }

                                                                                                            }
                                                                                                            if (!sw){
                                                                                                                break;
                                                                                                            }
                                                                                                        }

                                                                                                        if (!sw){
                                                                                                            //然后去查询对应的分类名
                                                                                                            Cell cell5 = sheets[0].getCell(7, ss);
                                                                                                            String contents3 = cell5.getContents().trim();
                                                                                                            //得到了分类名，然后去查询分类id
                                                                                                            int i2 = sortService.selectIdExcel(contents3);


                                                                                                            //根据id和属性名查询属性名id
                                                                                                            if (attName!=null && !attName.equals("")){

                                                                                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attName, i2);
                                                                                                                if (attributeNames1.size()>0){
                                                                                                                    AttributeName attributeName = attributeNames1.get(0);

                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    int id1 = attributeName.getId();

                                                                                                                    //获取属性值
                                                                                                                    JSONArray array1  = new JSONArray();//编码表
                                                                                                                    JSONObject object4 = new JSONObject();//映射
                                                                                                                    JSONObject object5 = new JSONObject();//function
                                                                                                                    for (int m = 1; m < rows2; m++) {

                                                                                                                        JSONObject object1 = new JSONObject();//编码
                                                                                                                        //获取属性值
                                                                                                                        Cell cell3 = sheets[k].getCell(l * 4 + 3, m);
                                                                                                                        String contents1 = cell3.getContents().trim();
//                                                                                                            System.out.println("值"+contents1);
                                                                                                                        //获取编码
                                                                                                                        Cell cell6 = sheets[k].getCell(l * 4 + 4, m);
                                                                                                                        String contents4 = cell6.getContents().trim();

//                                                                                                            System.out.println("编码"+contents4);
                                                                                                                        //获取长度
                                                                                                                        Cell cell7 = sheets[k].getCell(l * 4 + 1, 1);
                                                                                                                        String length1 = cell7.getContents().trim();
                                                                                                                        int length2 = contents4.length();

                                                                                                                        //查询是否是开启顺序编码功能
                                                                                                                        Cell cell8 = sheets[k].getCell(l * 4 + 4, 0);


                                                                                                                        if ((contents1!=null && !contents1.equals(""))&&(contents4!=null && !contents4.equals(""))){

                                                                                                                            if (length2>0 && Integer.parseInt(length1) == length2){
                                                                                                                                object1.put("id", "");
                                                                                                                                object1.put("att_name_id", id1);
                                                                                                                                object1.put("code",contents4);
                                                                                                                                object1.put("att_value",contents1);
                                                                                                                                array1.add(object1);
                                                                                                                            }else {
                                                                                                                                //编码长度不匹配
                                                                                                                                cc=false;
                                                                                                                            }



                                                                                                                        }
                                                                                                                        else {
                                                                                                                            //编码值为空
                                                                                                                            if ((contents4==null || contents4.equals("")&&(contents1!=null&&!contents1.equals("")))){
                                                                                                                                //查询是否开启了顺序编码功能
                                                                                                                                String trim = cell8.getContents().trim();
                                                                                                                                boolean contains1 = trim.contains("#");
                                                                                                                                if (contains1){
                                                                                                                                    //获取编码
                                                                                                                                    String main = OrderCode.main(Integer.parseInt(length1), m,"");

                                                                                                                                    object1.put("id", "");
                                                                                                                                    object1.put("att_name_id", id1);
                                                                                                                                    object1.put("code",main);
                                                                                                                                    object1.put("att_value",contents1);
                                                                                                                                    array1.add(object1);
                                                                                                                                }else {
                                                                                                                                    dd=false;
                                                                                                                                }

                                                                                                                            }
                                                                                                                            //属性值为空
                                                                                                                            if ((contents1==null || contents1.equals(""))&&(contents4!=null&&!contents4.equals(""))){
                                                                                                                                bb=false;
                                                                                                                            }



                                                                                                                        }



                                                                                                                        if (!dd){

                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码为空！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码为空！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                        if (!bb){
                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"编码:"+contents4+"对应的属性值为空！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"编码:"+contents4+"对应的属性值为空！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                        if (!cc){
                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码："+contents4+"长度不符合！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码："+contents4+"长度不符合！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                    }
                                                                                                                    if (!bb){
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    if (!cc){
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    if (!dd){
                                                                                                                        break;
                                                                                                                    }


                                                                                                                    //映射
                                                                                                                    object4.put("id", "");
                                                                                                                    object4.put("att_name_id", id1);
                                                                                                                    object4.put("sort_id",parentId);
                                                                                                                    if (l==0){
                                                                                                                        object4.put("begin_location",1);
                                                                                                                        object4.put("end_location",length);
                                                                                                                        object4.put("length",length);
                                                                                                                    }else {
                                                                                                                        //获取长度
                                                                                                                        int s5= Integer.parseInt(length);
                                                                                                                        int s6=0;
                                                                                                                        Cell cell9= sheets[k].getCell(l * 4 + 1, 1);
                                                                                                                        String trim1 = cell9.getContents().trim();

                                                                                                                        if (trim1!=null && !trim1.equals("")){
                                                                                                                            for (int n = 0; n < l; n++) {
                                                                                                                                Cell cell8= sheets[k].getCell(n * 4 + 1, 1);
                                                                                                                                String trim = cell8.getContents().trim();
                                                                                                                                if (trim!=null && !trim.equals("")){
                                                                                                                                    s5+=Integer.parseInt(trim) ;
                                                                                                                                }


                                                                                                                            }
                                                                                                                            s6=s5-(Integer.parseInt(trim1)-1);
                                                                                                                            object4.put("end_location",s5);
                                                                                                                            object4.put("begin_location",s6);
                                                                                                                            object4.put("length",length);
                                                                                                                        }

                                                                                                                    }


                                                                                                                    array2.add(object4);

                                                                                                                    //function
                                                                                                                    object5.put("id","");
                                                                                                                    object5.put("att_name_id",id1);
                                                                                                                    //判断是否是范围的

                                                                                                                    Cell cell3 = sheets[k].getCell(l * 4 + 3, 0);
                                                                                                                    Cell cell6 = sheets[k].getCell(l * 4 + 4, 0);
                                                                                                                    String trim1 = cell6.getContents().trim();
                                                                                                                    String trim = cell3.getContents().trim();

                                                                                                                    if (trim!=null && !trim.equals("")){
                                                                                                                        boolean sss = trim.contains("$");
                                                                                                                        if (sss){
                                                                                                                            //包含
                                                                                                                            int rows4 = sheets[k].getRows();
                                                                                                                            for (int m = 1; m <rows4 ; m++) {
                                                                                                                                Cell cell7 = sheets[k].getCell(l * 4 + 3, m);
                                                                                                                                Cell cell8 = sheets[k].getCell(l * 4 + 4, m);
                                                                                                                                String trim2 = cell7.getContents().trim();
                                                                                                                                String trim3 = cell8.getContents().trim();

                                                                                                                                if ((trim2!=null&&!trim2.equals(""))&&(trim3!=null&& !trim3.equals(""))){
                                                                                                                                    //判断值是否符合范围的格式

                                                                                                                                    boolean main = RegexMatches.main(trim2);
                                                                                                                                    if (!main){
                                                                                                                                        //不符合规则
                                                                                                                                        rr=false;
                                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                                        response.getWriter().write("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                        System.out.println("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                                    }else {
                                                                                                                                        //判断是否格式正确：<10~5]
                                                                                                                                        co = Compare.compare(trim2);
                                                                                                                                        if (!co){
                                                                                                                                            co = false;
                                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                                            response.getWriter().write("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"数据格式错误！");
                                                                                                                                            System.out.println("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }

                                                                                                                                if (!rr){
                                                                                                                                    break;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (rr && co){
                                                                                                                                object5.put("range",1);
                                                                                                                            }

                                                                                                                        }else {
                                                                                                                            //不包含
                                                                                                                            object5.put("range",0);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    //判断是否开启值最为编码或者顺序编码的功能
                                                                                                                    if (trim1!=null && !trim1.equals("")){
                                                                                                                        boolean contains = trim1.contains("&");
                                                                                                                        boolean contains1 = trim1.contains("#");
                                                                                                                        if (contains){
                                                                                                                            object5.put("auto_code", 1);
                                                                                                                        }else if (contains1){
                                                                                                                            object5.put("auto_code", 2);

                                                                                                                        }
                                                                                                                        else {
                                                                                                                            object5.put("auto_code", 0);
                                                                                                                        }
                                                                                                                    }

                                                                                                                    if (rr && co){
                                                                                                                        array3.add(object5);
                                                                                                                    }else {
                                                                                                                        break;
                                                                                                                    }



                                                                                                                    //得到了所有的数据，查询是否重复
//                                                                                                                    System.out.println("开始");

                                                                                                                    JSONArray array4 = new JSONArray();//临时的范围属性内容
                                                                                                                    String aa="";
                                                                                                                    String aa1="";
                                                                                                                    String s1 = array1.toJSONString();
                                                                                                                    List<AttributeValue> attributeValues1 = JSONArray.parseArray(s1, AttributeValue.class);
                                                                                                                    for (int m = 0; m < array1.size(); m++) {
                                                                                                                        aa="";
                                                                                                                         aa1="";
                                                                                                                        Object o1 = array1.get(m);
                                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                                        AttributeValue attributeValue = JSON.parseObject(s2, AttributeValue.class);
                                                                                                                        String attValue = attributeValue.getAttValue().replaceAll(" +","");;
                                                                                                                        String code4 = attributeValue.getCode().replaceAll(" +","");;
                                                                                                                        int attNameId1 = attributeValue.getAttNameId();


                                                                                                                        for (int n = 0; n < array1.size(); n++) {
                                                                                                                            //比较属性值是否重复

                                                                                                                            if (m!=n){
                                                                                                                                Object o4 = array1.get(n);
                                                                                                                                String s4 = JSON.toJSONString(o4);
                                                                                                                                AttributeValue attributeValue1 = JSON.parseObject(s4, AttributeValue.class);
                                                                                                                                String attValue1 = attributeValue1.getAttValue().replaceAll(" +","");;
                                                                                                                                if (attValue1.equals(attValue)){
                                                                                                                                    nn=false;
                                                                                                                                    aa  =  attValue;

                                                                                                                                }
                                                                                                                                String code1 = attributeValue1.getCode().replaceAll(" +","");;
                                                                                                                                if (code1.equals(code4)&&!code1.equals("") && code1!=null){
                                                                                                                                    nn1=false;
                                                                                                                                    aa1  =  code4;

                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (!nn|| !nn1){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                        }

//                                                                                                                        for (int n = 0; n < array1.size(); n++) {
//                                                                                                                            //编码值是否重复
//
//                                                                                                                            if (m!=n){
//                                                                                                                                Object o4 = array1.get(n);
//                                                                                                                                String s4 = JSON.toJSONString(o4);
//                                                                                                                                AttributeValue attributeValue1 = JSON.parseObject(s4, AttributeValue.class);
//                                                                                                                                String code1 = attributeValue1.getCode();
//                                                                                                                                if (code1.equals(code4)&&!code1.equals("") && code1!=null){
//                                                                                                                                    nn1=false;
//                                                                                                                                    aa1  =  code4;
//
//                                                                                                                                }
//                                                                                                                            }
//                                                                                                                            if (!nn1){
//                                                                                                                                break;
//                                                                                                                            }
//
//                                                                                                                        }
                                                                                                                        if (nn && nn1){
                                                                                                                            for (int n = 0; n < array3.size(); n++) {
                                                                                                                                Object o2 = array3.get(n);
                                                                                                                                String s3 = JSON.toJSONString(o2);
                                                                                                                                AttributeFunction attributeFunction = JSON.parseObject(s3, AttributeFunction.class);
                                                                                                                                int attNameId = attributeFunction.getAttNameId();
                                                                                                                                int range = attributeFunction.getRange();


                                                                                                                                if (attNameId==attNameId1 && range==1){
                                                                                                                                    //是范围的，获取当前attNameid的所有内容然后跟excel文档中的数据比较
                                                                                                                                    array4.add(o1);
                                                                                                                                }


                                                                                                                            }


                                                                                                                        }



                                                                                                                        if (!nn || !nn1){
                                                                                                                            break;
                                                                                                                        }

                                                                                                                    }
                                                                                                                    if (array4.size()>0){
                                                                                                                        //查看是否重叠
                                                                                                                        for (int n = 0; n < array4.size(); n++) {
                                                                                                                            Object o2 = array4.get(n);
                                                                                                                            String s3 = JSON.toJSONString(o2);
                                                                                                                            AttributeValue attributeValue1 = JSON.parseObject(s3, AttributeValue.class);
                                                                                                                            String attValue1 = attributeValue1.getAttValue();
                                                                                                                            int attNameId = attributeValue1.getAttNameId();

                                                                                                                            //跟文档中的数据比较
                                                                                                                            for (int p = 0; p < array4.size(); p++) {
                                                                                                                                if (n!=p){

                                                                                                                                    Object o3 = array4.get(p);
                                                                                                                                    String s4 = JSON.toJSONString(o3);
                                                                                                                                    AttributeValue attributeValue2 = JSON.parseObject(s4, AttributeValue.class);
                                                                                                                                    String attValue2 = attributeValue2.getAttValue();
                                                                                                                                    boolean b1 = Compare.compare3(attValue1, attValue2);

                                                                                                                                    if (b1){
                                                                                                                                        co3=false;
                                                                                                                                        aa=attValue1+attValue2;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if (!co3){
                                                                                                                                    break;
                                                                                                                                }

                                                                                                                            }

                                                                                                                            if (!co3){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                            //跟数据库中的数据比较
                                                                                                                            //根据attNameid获取属性值
                                                                                                                            if (co3){
                                                                                                                                //要先查询数据库中是否存在
                                                                                                                                boolean b2 = attributeValueService.selectValueExistExcel(attributeValue1);
                                                                                                                                if (!b2){
                                                                                                                                    List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(attNameId);
                                                                                                                                    for (int p = 0; p < attributeValues.size(); p++) {
                                                                                                                                        AttributeValue attributeValue2 = attributeValues.get(p);
                                                                                                                                        String attValue2 = attributeValue2.getAttValue();
                                                                                                                                        boolean b1 = Compare.compare3(attValue2, attValue1);
                                                                                                                                        if (b1){
                                                                                                                                            co3=false;
                                                                                                                                            aa=attValue2+attValue1;
                                                                                                                                            break;
                                                                                                                                        }

                                                                                                                                    }
                                                                                                                                }

                                                                                                                            }

                                                                                                                        }
                                                                                                                    }
                                                                                                                    //判断是否重复
                                                                                                                    if (!nn){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"属性值"+aa+"重复！");
                                                                                                                        System.out.println(name1+"中的"+attName+"属性值"+aa+"重复！");
                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;

                                                                                                                    }else if (!co3){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"两个属性值"+aa+"范围重叠！");
                                                                                                                        System.out.println(name1+"中的"+attName+"两个属性值"+aa+"范围重叠！");
                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;
                                                                                                                    }else if (!nn1){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"编码值"+aa1+"重复！");
                                                                                                                        System.out.println("这里吗");
                                                                                                                        System.out.println(name1+"中的"+attName+"编码值"+aa1+"重复！");

                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;
                                                                                                                    }

                                                                                                                    else {


                                                                                                                        //查询数据库中是否有这个了
                                                                                                                        for (int m = 0; m < array1.size(); m++) {
                                                                                                                            Object o1 = array1.get(m);
                                                                                                                            String s2 = JSON.toJSONString(o1);
                                                                                                                            AttributeValue attributeValue = JSON.parseObject(s2, AttributeValue.class);
                                                                                                                            boolean b1 = attributeValueService.selectValueExistExcel(attributeValue);
                                                                                                                            if (b1){
                                                                                                                                //数据库中已存在
                                                                                                                                upCode.add(o1);
                                                                                                                            }else {
                                                                                                                                //数据库中不存在
                                                                                                                                attCode.add(o1);
                                                                                                                            }
                                                                                                                        }


                                                                                                                    }



                                                                                                                }else {
                                                                                                                    //没有找到属性名
                                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                                    response.getWriter().write("表："+sheets[k].getName()+"未找到属性"+attName+"");
                                                                                                                    System.out.println("表："+sheets[k].getName()+"未找到属性"+attName+"");
                                                                                                                    FileUtil.delFile(filePath);
                                                                                                                    mm = false;
                                                                                                                }


                                                                                                            }
                                                                                                        }
                                                                                                        else {
                                                                                                            System.out.println("没有找到分类码："+contents2);
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("没有找到分类码："+contents2);
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            mm=false;
                                                                                                        }

                                                                                                        if (!mm){
                                                                                                            break;
                                                                                                        }

                                                                                                    }

                                                                                                    if (!nn){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!nn1){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!co3){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!mm){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!bb){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!cc){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!dd){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!rr){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (sum>9){
                                                                                                        ee = false;
                                                                                                        System.out.println("表："+sheets[k].getName()+"的总占位超过了9！");
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("表："+sheets[k].getName()+"的总占位超过了9！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }


                                                                                                }


                                                                                                //得到了所有的映射
                                                                                                for (int k = 0; k < array2.size(); k++) {
                                                                                                    Object o1 = array2.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    Mapping mapping = JSON.parseObject(s2, Mapping.class);

                                                                                                    boolean b1 = mappingService.selectMapIfExistExcel(mapping);
                                                                                                    int sortId = mapping.getSortId();

                                                                                                    //查询映射所在的分类下是否存在物料
                                                                                                    List<Product> products = productService.selectProductExistExcel(sortId);
                                                                                                    if (b1){
                                                                                                        //存在映射
                                                                                                        if (products.size()>0){
                                                                                                            Product product = products.get(0);
                                                                                                            String name1 = product.getName();
                                                                                                            System.out.println(name1+"中存在物料，不能进行映射的修改！");
                                                                                                            map = false;
                                                                                                        }else {
                                                                                                            //重复，添加到更新中
                                                                                                            upMapping.add(o1);
                                                                                                        }

                                                                                                    }else {
                                                                                                        //不存在映射，添加到新增中
                                                                                                        addMapping.add(o1);
                                                                                                    }
                                                                                                    if (!map){
                                                                                                        break;
                                                                                                    }
                                                                                                }
//                                                                                                System.out.println("结束");


                                                                                                if (repeat&&nn&&mm&&bb&&cc&&dd&&ee&&rr&&co3){
                                                                                                    String scv="";
                                                                                                    boolean cv = true;
                                                                                                    //执行更新和，添加操作
                                                                                                    if (addMapping.size()>0){
                                                                                                        String s2 = JSON.toJSONString(addMapping);
                                                                                                        List<Mapping> mappings = JSONArray.parseArray(s2, Mapping.class);
                                                                                                        //调用service
                                                                                                        mappingService.addMapExcel(mappings);
                                                                                                        System.out.println("映射添加成功");
                                                                                                    }

                                                                                                    if (upMapping.size()>0){
                                                                                                        //执行更新操作
                                                                                                        String s2 = JSON.toJSONString(upMapping);
                                                                                                        List<Mapping> mappings = JSONArray.parseArray(s2, Mapping.class);
                                                                                                        mappingService.updateMapExcel(mappings);
                                                                                                        System.out.println("映射更新成功");
                                                                                                    }




                                                                                                    //得到了所有的function,查询是否重复以及是否可以添加
                                                                                                    for (int k = 0; k < array3.size(); k++) {
                                                                                                        Object o1 = array3.get(k);
                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                        AttributeFunction attributeFunction = JSON.parseObject(s2, AttributeFunction.class);
                                                                                                        //查询function是否存在
                                                                                                        boolean b1 = attributeFunctionService.selectExistExcel(attributeFunction);
                                                                                                        if (!b1){
                                                                                                            addFunction.add(o1);
                                                                                                        }
                                                                                                    }

                                                                                                    if (addFunction.size()>0){
                                                                                                        String s2 = JSON.toJSONString(addFunction);
                                                                                                        List<AttributeFunction> attributeFunctions = JSONArray.parseArray(s2, AttributeFunction.class);
                                                                                                        //执行添加操作
                                                                                                        attributeFunctionService.addExcel(attributeFunctions);

                                                                                                        System.out.println("添加function成功");
                                                                                                    }




                                                                                                    if (attCode.size()>0){

                                                                                                        for (int m = 0; m < attCode.size(); m++) {
                                                                                                            Object o1 = attCode.get(m);
                                                                                                            String s21= JSON.toJSONString(o1);
                                                                                                            AttributeValue attributeValue = JSON.parseObject(s21, AttributeValue.class);
                                                                                                            boolean b1 = attributeValueService.selectValueCodeExistExcel(attributeValue);
                                                                                                            boolean b2 = attributeValueService.selectValueCodesExistExcel(attributeValue);
                                                                                                            if (b1||b2){
                                                                                                                //数据库中已存在
                                                                                                                if (b1){
                                                                                                                    //查询属性名
                                                                                                                    int attNameId = attributeValue.getAttNameId();
                                                                                                                    List<AttributeName> attributeNames = attributeService.selectById(attNameId);
                                                                                                                    AttributeName attributeName = attributeNames.get(0);
                                                                                                                    String name1 = attributeName.getName();
                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    //查询分类名
                                                                                                                    List<Sort> sorts2 = sortService.selectSortById(parentId);
                                                                                                                    Sort sort1 = sorts2.get(0);
                                                                                                                    String name2 = sort1.getName();

                                                                                                                    cv = false;
                                                                                                                    String attValue = attributeValue.getAttValue();
                                                                                                                    String code1 = attributeValue.getCode();
                                                                                                                    scv="四级分类："+name2+"中属性："+name1+"中属性值为"+attValue+"编码值为"+code1+"的属性值数据库中已存在！";
                                                                                                                    break;
                                                                                                                }else if (b2){
                                                                                                                    //查询属性名
                                                                                                                    int attNameId = attributeValue.getAttNameId();
                                                                                                                    List<AttributeName> attributeNames = attributeService.selectById(attNameId);
                                                                                                                    AttributeName attributeName = attributeNames.get(0);
                                                                                                                    String name1 = attributeName.getName();
                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    //查询分类名
                                                                                                                    List<Sort> sorts2 = sortService.selectSortById(parentId);
                                                                                                                    Sort sort1 = sorts2.get(0);
                                                                                                                    String name2 = sort1.getName();
                                                                                                                    cv = false;
                                                                                                                    String attValue = attributeValue.getAttValue();
                                                                                                                    String code1 = attributeValue.getCode();
                                                                                                                    scv="四级分类："+name2+"中属性："+name1+"中属性值为"+attValue+"编码值为"+code1+"的编码值数据库中已存在！";
                                                                                                                    break;
                                                                                                                }

                                                                                                            }
                                                                                                        }
                                                                                                        if (cv){
                                                                                                            //数据库中不存在
                                                                                                            // 执行添加操作
                                                                                                            String s2 = JSON.toJSONString(attCode);


                                                                                                            List<AttributeValue> attributeValues = JSONArray.parseArray(s2, AttributeValue.class);
                                                                                                            //先查询要添加的是否跟数据库中的重复，属性值以及编码

                                                                                                            attributeValueService.addCodeExcel(attributeValues);
                                                                                                            //响应数据
//                                                                    response.setContentType("text/json;charset=utf-8");
//                                                                    response.getWriter().write(s2);
                                                                                                            System.out.println("添加编码成功！");
                                                                                                        }

                                                                                                    }else {
                                                                                                        if (upCode.size()>0){
                                                                                                            System.out.println("更新编码成功！");
                                                                                                        }
                                                                                                    }
                                                                                                        if (cv){
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("一键导入成功！");
                                                                                                            FileUtil.delFile(filePath);
                                                                                                        }else {
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write(""+scv+"");
                                                                                                            System.out.println(""+scv+"");
                                                                                                            FileUtil.delFile(filePath);
                                                                                                        }

                                                                                                }


                                                                                            }

                                                                                        }



                                                                                    }


                                                                                }


                                                                            }
                                                                        }else {
                                                                            //四级分类的分类名或者编码为空
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("四级分类的分类名或编码为空!");
                                                                            System.out.println("四级分类的分类名或编码为空!");
                                                                            FileUtil.delFile(filePath);
                                                                        }


                                                                    }else {
                                                                        threes++;
                                                                        if (threes==rows){
                                                                            //三级分类的分类名或者编码为空
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("三级分类的分类名或编码为空!");
                                                                            System.out.println("三级分类的分类名或编码为空!");
                                                                            FileUtil.delFile(filePath);
                                                                        }

                                                                    }

                                                                }
                                                                if (threesHave>1){
                                                                    //三级分类中存在多个值
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("属性表中存在多个三级分类！");
                                                                    System.out.println("属性表中存在多个三级分类!");
                                                                    FileUtil.delFile(filePath);
                                                                }
                                                            }else {
                                                                twos++;
                                                                if (twos==rows){
                                                                    //二级分类的分类名或者编码为空
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("二级分类的分类名或编码为空!");
                                                                    System.out.println("二级分类的分类名或编码为空!");
                                                                    FileUtil.delFile(filePath);
                                                                }

                                                            }
                                                        }
                                                        if (twosHave>1){
                                                            //二级分类中存在多个值
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("属性表中存在多个二级分类！");
                                                            System.out.println("属性表中存在多个二级分类!");
                                                            FileUtil.delFile(filePath);
                                                        }




                                                    }
                                                    else {
                                                        one++;
                                                        if (one==rows){
                                                            //一级分类的编码或者分类名为空
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("一级分类的分类名或编码为空!");
                                                            System.out.println("一级分类的分类名或编码为空!");
                                                            FileUtil.delFile(filePath);
                                                        }

                                                    }
                                                }
                                                if (oneHave>1){
                                                    //一级分类中存在多个值
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("属性表中存在多个一级分类！");
                                                    System.out.println("属性表中存在多个一级分类!");
                                                    FileUtil.delFile(filePath);
                                                }




                                            }


                                        }




                                        workbook.close();

                                    } catch (BiffException | IOException e) {
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
                                    Sheet sheet;
                                    Cell cell ;
                                    JSONArray jsons = new JSONArray();
                                    JSONArray addjsons = new JSONArray();
                                    JSONArray addjsonslast = new JSONArray();
                                    JSONArray updatejsons = new JSONArray();
                                    JSONArray alljsons = new JSONArray();//记录所有的分类，判断是否有重复的
                                    JSONArray common = new JSONArray();//普通属性，添加function时使用

                                    try {
                                        //获取一个Excel文件  只支持.xls格式
                                        workbook = Workbook.getWorkbook(new File(filePath));
                                        //获取到一共有多少个表
                                        Sheet[] sheets = workbook.getSheets();
                                        for(int i = 0; i < sheets.length; i++) {
                                            //第一个表格  //sheet.getCell(列，行);
//                                            String name5 = sheets[i].getName();获取表格的名称
                                            boolean two = false;
                                            boolean three = false;
                                            boolean bgThree=false;
                                            if (i==0){
                                                //获取行数
                                                int rows = sheets[i].getRows();
                                                System.out.println("行数:"+rows);
                                                //获取列数
                                                int columns = sheets[i].getColumns();
                                                System.out.println("列数:"+columns);

                                                Cell[] levelOne = new Cell[rows];
                                                Cell[] levelOneCode = new Cell[rows];
                                                Cell[] levelTwo = new Cell[rows];
                                                Cell[] levelTwoCode = new Cell[rows];
                                                Cell[] levelThree = new Cell[rows];
                                                Cell[] levelThreeCode = new Cell[rows];
                                                Cell[] levelFour = new Cell[rows];
                                                Cell[] levelFourCode = new Cell[rows];
                                                int idOne = 0;
                                                int idTwo = 0;
                                                int idThree = 0;


                                                //查询一级分类的内容
                                                int one = 1;
                                                int oneHave=0;
                                                for (int z = 1; z < rows; z++) {
                                                    levelOne[0] = sheets[i].getCell(1, z);
                                                    levelOneCode[0] = sheets[i].getCell(2, z);
                                                    String contents = levelOne[0].getContents().trim();
                                                    String code=levelOneCode[0].getContents().trim();
                                                    if ((contents!=null && !contents.equals(""))&& (code!=null&& !code.equals(""))){
                                                        oneHave++;
                                                        if (oneHave>1){
                                                            break;
                                                        }
                                                        //每一行创建一个JSONObject对象
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", "");
                                                        object.put("parent_id",0);
                                                        object.put("name",contents);
                                                        object.put("level",1);
                                                        object.put("code",code);


                                                        //加入json队列
                                                        jsons.add(object);
                                                        alljsons.add(object);

                                                        //查询二级分类内容
                                                        int twos=1;
                                                        int twosHave=0;
                                                        for (int y = 1; y < rows; y++) {
                                                            levelTwo[0] = sheets[i].getCell(3, y);
                                                            levelTwoCode[0] = sheets[i].getCell(4, y);
                                                            String name=levelTwo[0].getContents().trim();
                                                            String code2 = levelTwoCode[0].getContents().trim();
                                                            JSONArray twojsons = new JSONArray();
                                                            if ((name!=null&&!name.equals(""))&&(code2!=null&&!code2.equals(""))){
                                                                twosHave++;
                                                                if (twosHave>1){
                                                                    break;
                                                                }

                                                                //每一行创建一个JSONObject对象
                                                                JSONObject object2 = new JSONObject();
                                                                object2.put("id", "");
                                                                object2.put("name",name);
                                                                object2.put("level",2);
                                                                object2.put("code",code2);


                                                                //加入json队列
                                                                alljsons.add(object2);


                                                                //查询三级分类的内容
                                                                //获取到二级分类添加的id，下面执行三级分类
                                                                int threes = 1;
                                                                int threesHave=0;
                                                                for (int x = 1; x < rows; x++) {
                                                                    levelThree[0] = sheets[i].getCell(5, x);
                                                                    levelThreeCode[0] = sheets[i].getCell(6, x);
                                                                    String name3=levelThree[0].getContents().trim();
                                                                    String code3 = levelThreeCode[0].getContents().trim();
                                                                    JSONArray threejsons = new JSONArray();
                                                                    if ((name3!=null&&!name3.equals(""))&&(code3!=null&& !code3.equals(""))){
                                                                        threesHave++;
                                                                        if (threesHave>1){
                                                                            break;
                                                                        }

                                                                        //每一行创建一个JSONObject对象
                                                                        JSONObject object3 = new JSONObject();
                                                                        object3.put("id", "");
                                                                        object3.put("name",name3);
                                                                        object3.put("level",3);
                                                                        object3.put("code",code3);

                                                                        //加入json队列
                                                                        alljsons.add(object3);

                                                                        //查询四级分类内容信息
                                                                        JSONArray fourjsonss = new JSONArray();
                                                                        //获取到了添加的三级分类的id，下面进行四级分类的添加操作
                                                                        for (int k = 1; k < rows; k++) {
                                                                            JSONObject objects4 = new JSONObject();
                                                                            levelFour[k] = sheets[i].getCell(7, k);
                                                                            levelFourCode[k] = sheets[i].getCell(8, k);
                                                                            String name4 = levelFour[k].getContents().trim();
                                                                            String code4=levelFourCode[k].getContents().trim();
                                                                            if (name4!=null&& !name4.equals("") && code4!=null&& !code4.equals("")){
                                                                                //每一行创建一个JSONObject对象
                                                                                objects4.put("id", "");
                                                                                objects4.put("name",name4);
                                                                                objects4.put("level",4);
                                                                                objects4.put("code",code4);
                                                                                fourjsonss.add(objects4);
                                                                                //加入json队列
                                                                                alljsons.add(objects4);
                                                                            }

                                                                        }
                                                                        if (fourjsonss.size()>0){
                                                                            //检测数据是否重复
                                                                            boolean eq=true;
                                                                            String as = JSON.toJSONString(alljsons);
                                                                            List<Sort> sorts = JSONArray.parseArray(as, Sort.class);
                                                                            //查看所有的分类名是否重复
                                                                            for (int j = 0; j < sorts.size(); j++) {
                                                                                Sort sort = sorts.get(j);
                                                                                String name1 = sort.getName();
                                                                                for (int k = 0; k < sorts.size(); k++) {
                                                                                    if (k!=j){
                                                                                        Sort sort1 = sorts.get(k);
                                                                                        String name2 = sort1.getName();
                                                                                        if (name2.equals(name1)){
                                                                                            eq=false;

                                                                                        }
                                                                                    }
                                                                                    if (!eq){
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                if (!eq){
                                                                                    break;
                                                                                }
                                                                            }
                                                                            if (!eq){
                                                                                //存在重复信息
                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                response.getWriter().write("分类中存在重复信息!");
                                                                                FileUtil.delFile(filePath);

                                                                            }
                                                                            else {
                                                                                //不存在重复信息，查询编码是否相同(这里只检测的四级的)
                                                                                String s7 = JSON.toJSONString(fourjsonss);
                                                                                List<Sort> sorts1 = JSONArray.parseArray(s7, Sort.class);
                                                                                for (int j = 0; j <sorts1.size(); j++) {
                                                                                    Sort sort = sorts1.get(j);
                                                                                    String code1 = sort.getCode();
                                                                                    for (int k = 0; k < sorts1.size(); k++) {

                                                                                        if (j!=k){
                                                                                            Sort sort1 = sorts1.get(k);
                                                                                            String code4 = sort1.getCode();
                                                                                            if (code1.equals(code4)&&!code1.equals("") && code1!=null){
                                                                                                eq=false;
                                                                                            }
                                                                                        }
                                                                                        if (!eq){
                                                                                            break;
                                                                                        }
                                                                                    }


                                                                                }

                                                                                if (!eq){
                                                                                    //存在重复信息
                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                    response.getWriter().write("分类中编码存在重复信息!");
                                                                                    FileUtil.delFile(filePath);

                                                                                }
                                                                                else {
                                                                                    boolean nameRepeat=true;
                                                                                    boolean codeRepeat=true;
                                                                                    //得到所有的一级分类，检测该分类是否存在，执行添加操作
                                                                                    for (int j = 0; j < jsons.size(); j++) {
                                                                                        Object o = jsons.get(j);
                                                                                        String s = JSON.toJSONString(o);
                                                                                        Sort sort = JSON.parseObject(s, Sort.class);
                                                                                        //查询分类是否存在,调用service
                                                                                        boolean b = sortService.selectSortIfExist(sort);
                                                                                        if (b){
                                                                                            //如果b为真，证明存在，先执行查询操作，查询该分类的id
                                                                                            String name1 = sort.getName();
                                                                                            idOne = sortService.selectIdExcel(name1);




                                                                                        }else {
                                                                                            //b为假，不存在，查询分类名称或者编码是否有被使用，都未被使用才能添加
                                                                                            //查询名称是否重复
                                                                                            boolean b1 = sortService.selectSortNameIfExist(sort);
                                                                                            if (!b1){
                                                                                                //不重复，查询是否编码重复
                                                                                                boolean b2 = sortService.selectSortCodeIfExist(sort);
                                                                                                if (!b2){
                                                                                                    //不重复
                                                                                                    sortService.addSortExcel(sort);
                                                                                                    idOne= sort.getId();
                                                                                                }else {
                                                                                                    //编码重复
                                                                                                    codeRepeat = false;


                                                                                                }
                                                                                                if (!codeRepeat){
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("一级分类："+sort.getName()+"中的编码值："+sort.getCode()+"已被使用！");
                                                                                                    System.out.println("一级分类："+sort.getName()+"中的编码值："+sort.getCode()+"已被使用！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                            }else {
                                                                                                //分类名重复
                                                                                                nameRepeat=false;

                                                                                            }
                                                                                            if (!nameRepeat){
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("一级分类："+sort.getName()+"中的分类名："+sort.getName()+"已被使用！");
                                                                                                System.out.println("一级分类："+sort.getName()+"中的分类名："+sort.getName()+"已被使用！");
                                                                                                FileUtil.delFile(filePath);
                                                                                                break;
                                                                                            }

                                                                                        }
                                                                                        if (!codeRepeat){
                                                                                            break;
                                                                                        }
                                                                                        if (!nameRepeat){
                                                                                            break;
                                                                                        }
                                                                                        if (nameRepeat && codeRepeat){
                                                                                            //获取到了刚添加的一级分类的id下面进行二级分类的操作
                                                                                            object2.put("parent_id",idOne);

                                                                                            //加入json队列
                                                                                            twojsons.add(object2);
                                                                                            //查询是否重复
                                                                                            Object o2 = twojsons.get(0);
                                                                                            String ts = JSON.toJSONString(o2);
                                                                                            Sort sort2 = JSON.parseObject(ts, Sort.class);

                                                                                            //查询分类是否存在,调用service
                                                                                            boolean b2 = sortService.selectSortIfExist(sort2);
                                                                                            if (!b2){

                                                                                                //查询分类名是否重复
                                                                                                boolean br = sortService.selectSortNameIfExist(sort2);
                                                                                                if (!br){
                                                                                                    //不重复,查看编码是否重复
                                                                                                    boolean bt = sortService.selectSortCodeIfExist(sort2);
                                                                                                    if (!bt){
                                                                                                        //不重复
                                                                                                        //不存在，执行添加操作
                                                                                                        sortService.addSortExcel(sort2);
                                                                                                        idTwo = sort2.getId();
                                                                                                    }else {
                                                                                                        //重复
                                                                                                        codeRepeat = false;
                                                                                                    }
                                                                                                    if (!codeRepeat){
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("二级分类："+sort2.getName()+"中的编码值："+sort2.getCode()+"已被使用！");
                                                                                                        System.out.println("二级分类："+sort2.getName()+"中的编码值："+sort2.getCode()+"已被使用！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }
                                                                                                }else {
                                                                                                    //重复
                                                                                                    nameRepeat = false;
                                                                                                }
                                                                                                if (!nameRepeat){
                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                    response.getWriter().write("二级分类："+sort2.getName()+"中的分类名："+sort2.getName()+"已被使用！");
                                                                                                    System.out.println("二级分类："+sort2.getName()+"中的分类名："+sort2.getName()+"已被使用！");
                                                                                                    FileUtil.delFile(filePath);
                                                                                                    break;
                                                                                                }
                                                                                            }else {
                                                                                                //存在，查询id
                                                                                                String name1 = sort2.getName();
                                                                                                idTwo = sortService.selectIdExcel(name1);

                                                                                            }
                                                                                            if (!codeRepeat){
                                                                                                break;
                                                                                            }
                                                                                            if (!nameRepeat){
                                                                                                break;
                                                                                            }
                                                                                            if (nameRepeat && codeRepeat){
                                                                                                //每一行创建一个JSONObject对象

                                                                                                object3.put("parent_id",idTwo);

                                                                                                threejsons.add(object3);
                                                                                                //查询是否重复
                                                                                                Object o3 = threejsons.get(0);
                                                                                                String s3 = JSON.toJSONString(o3);
                                                                                                Sort sort3 = JSON.parseObject(s3, Sort.class);

                                                                                                boolean b3 = sortService.selectSortIfExist(sort3);
                                                                                                if (!b3){

                                                                                                    //查询分类名是否重复
                                                                                                    boolean by = sortService.selectSortNameIfExist(sort3);
                                                                                                    if (!by){
                                                                                                        //不重复，查询编码是否重复
                                                                                                        boolean bu = sortService.selectSortCodeIfExist(sort3);
                                                                                                        if (!bu){
                                                                                                            //不重复
                                                                                                            //不存在，执行添加操作
                                                                                                            sortService.addSortExcel(sort3);
                                                                                                            idThree= sort3.getId();
                                                                                                        }else {
                                                                                                            //编码重复
                                                                                                            codeRepeat = false;
                                                                                                        }
                                                                                                        if (!codeRepeat){
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("二级分类："+sort3.getName()+"中的编码值："+sort3.getCode()+"已被使用！");
                                                                                                            System.out.println("三级分类："+sort3.getName()+"中的编码值："+sort3.getCode()+"已被使用！");
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            break;
                                                                                                        }
                                                                                                    }else {
                                                                                                        //重复
                                                                                                        nameRepeat = false;
                                                                                                    }
                                                                                                    if (!nameRepeat){
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("三级分类："+sort3.getName()+"中的分类名："+sort3.getName()+"已被使用！");
                                                                                                        System.out.println("三级分类："+sort3.getName()+"中的分类名："+sort3.getName()+"已被使用！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }
                                                                                                }else {
                                                                                                    //存在,查询信息
                                                                                                    String name1 = sort3.getName();
                                                                                                    idThree = sortService.selectIdExcel(name1);

                                                                                                }

                                                                                                if (!codeRepeat){
                                                                                                    break;
                                                                                                }
                                                                                                if (!nameRepeat){
                                                                                                    break;
                                                                                                }
                                                                                                if (nameRepeat && codeRepeat){
                                                                                                    JSONArray fourjsons = new JSONArray();
                                                                                                    //获取到了添加的三级分类的id，下面进行四级分类的添加操作
                                                                                                    for (int k = 1; k < rows; k++) {
                                                                                                        levelFour[k] = sheets[i].getCell(7, k);
                                                                                                        levelFourCode[k] = sheets[i].getCell(8, k);
                                                                                                        String name4 = levelFour[k].getContents().trim();
                                                                                                        String code4=levelFourCode[k].getContents().trim();

                                                                                                        if (name4!=null&& !name4.equals("")){
                                                                                                            if (code4!=null&&!code4.equals("")){
                                                                                                                //每一行创建一个JSONObject对象
                                                                                                                JSONObject object4 = new JSONObject();
                                                                                                                object4.put("id", "");
                                                                                                                object4.put("parent_id",idThree);
                                                                                                                object4.put("name",name4);
                                                                                                                object4.put("level",4);
                                                                                                                object4.put("code",code4);
                                                                                                                fourjsons.add(object4);
                                                                                                            }
                                                                                                        }

                                                                                                    }

                                                                                                    //不存在重复元素
                                                                                                    for (int k = 0; k <fourjsons.size() ; k++) {
                                                                                                        Object o4 = fourjsons.get(k);
                                                                                                        String s4 = JSON.toJSONString(o4);
                                                                                                        Sort sort4 = JSON.parseObject(s4, Sort.class);

                                                                                                        //查询是否重复
                                                                                                        boolean b4 = sortService.selectSortIfExist(sort4);
                                                                                                        if (!b4){
                                                                                                            //不重复
                                                                                                            addjsons.add(o4);
                                                                                                        }else {
                                                                                                            //重复
                                                                                                            updatejsons.add(o4);
                                                                                                        }
                                                                                                    }
                                                                                                    //得到了所有要添加的以及要更新的四级分类的数据
                                                                                                    if (addjsons.size()>0){
                                                                                                        //如果大于0就执行添加操作
                                                                                                        for (int k = 0; k < addjsons.size(); k++) {
                                                                                                            Object o1 = addjsons.get(k);
                                                                                                            String s1 = JSON.toJSONString(o1);
                                                                                                            Sort sort4 = JSON.parseObject(s1, Sort.class);
                                                                                                            //查询分类名是否重复
                                                                                                            boolean b1 = sortService.selectSortNameIfExist(sort4);
                                                                                                            if (!b1){
                                                                                                                //不重复，检测编码
                                                                                                                boolean b4 = sortService.selectSortCodeIfExist(sort4);
                                                                                                                if (!b4){
                                                                                                                    //不重复
                                                                                                                    addjsonslast.add(sort4);

                                                                                                                }else {
                                                                                                                    codeRepeat = false;
                                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                                    response.getWriter().write("四级分类："+sort4.getName()+"中的编码值："+sort4.getCode()+"已被使用！");
                                                                                                                    System.out.println("四级分类："+sort4.getName()+"中的编码值："+sort4.getCode()+"已被使用！");
                                                                                                                    FileUtil.delFile(filePath);
                                                                                                                    break;
                                                                                                                }
                                                                                                            }else {
                                                                                                                nameRepeat = false;
                                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                                response.getWriter().write("四级分类级分类："+sort4.getName()+"中的分类名："+sort4.getName()+"已被使用！");
                                                                                                                System.out.println("四级分类："+sort4.getName()+"中的分类名："+sort4.getName()+"已被使用！");
                                                                                                                FileUtil.delFile(filePath);
                                                                                                                break;
                                                                                                            }
                                                                                                        }
                                                                                                        if (nameRepeat && codeRepeat){
                                                                                                            //名称和编码都不重复，测试等级是否有四个
                                                                                                            int i1 = codeService.selectCount();
                                                                                                            if (i1<4){
                                                                                                                two=false;
                                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                                response.getWriter().write("系统设定的分类等级数少于4，与文档不符！");
                                                                                                                System.out.println("系统设定的分类等级数少于4，与文档不符！");
                                                                                                            }else {
                                                                                                                String ss = addjsonslast.toJSONString();
                                                                                                                List<Sort> addsorts = JSONArray.parseArray(ss, Sort.class);
                                                                                                                sortService.addSortFourExcel(addsorts);
                                                                                                                System.out.println("添加分类成功");
                                                                                                                two=true;
                                                                                                            }

                                                                                                        }




                                                                                                    }else {
                                                                                                        if (updatejsons.size()>0){
                                                                                                            two=true;
                                                                                                        }

                                                                                                    }



                                                                                                }
                                                                                            }


                                                                                        }




                                                                                        if (two && nameRepeat && codeRepeat){
                                                                                            //如果two是true,获取属性表的信息
                                                                                            //获取行数,这里面使用分类表中的行数，两张表行数不同会导致，代码加载失败
                                                                                            System.out.println("下面是属性表的信息");
                                                                                            int rows0 = sheets[0].getRows();
                                                                                            int rows1=sheets[1].getRows();
//                                                                            if (rows0>rows1){
//                                                                                rows=rows1;
//                                                                            }else if (rows0<rows1){
//                                                                                rows=rows0;
//                                                                            }else {
//                                                                                rows=rows0;
//                                                                            }
                                                                                            System.out.println("行数:"+rows0);
                                                                                            //获取列数
                                                                                            columns = sheets[1].getColumns();
                                                                                            System.out.println("列数:"+columns);
                                                                                            JSONArray array = new JSONArray();
                                                                                            //根据分类id去分类表中查询该分类名称，然后去数据库中查询id作为属性的parentid
                                                                                            int mnb=0;
                                                                                            String con="";
                                                                                            for (int k = 1; k < rows1; k++) {
                                                                                                Cell cell2 = sheets[1].getCell(0, k);
                                                                                                String contents2 = cell2.getContents().trim();

                                                                                                if (contents2!=null && !contents2.equals("")){
                                                                                                    System.out.println("属性表"+contents2);
                                                                                                    for (int l = 1; l < rows0; l++) {


                                                                                                        //获取分类表中的分类码
                                                                                                        Cell cell1 = sheets[0].getCell(0, l);
                                                                                                        String contents1 = cell1.getContents().trim();

                                                                                                        if (contents1!=null && !contents1.equals("") && contents1.equals(contents2)){
                                                                                                            mnb=0;
                                                                                                            mnb++;
                                                                                                        }
                                                                                                    }
                                                                                                    if (mnb==0){
                                                                                                        con=contents2;
                                                                                                        bgThree = true;
                                                                                                        break;
                                                                                                    }


                                                                                                }


                                                                                                if (bgThree){
                                                                                                    break;
                                                                                                }


                                                                                            }

                                                                                            if (!bgThree){
                                                                                                for (int k = 1; k < rows; k++) {

                                                                                                    //获取属性表中的分类码
                                                                                                    Cell cell1 = sheets[0].getCell(0, k);
                                                                                                    String contents1 = cell1.getContents().trim();
                                                                                                    if (contents1!=null && !contents1.equals("")){
                                                                                                        //获取分类表中的分类码
                                                                                                        for (int bb = 1; bb < rows1; bb++) {

                                                                                                            Cell cell2 = sheets[1].getCell(0, bb);
                                                                                                            String contents2 = cell2.getContents().trim();


                                                                                                            if (contents2!=null && !contents2.equals("") && contents1.equals(contents2)){

                                                                                                                //两者都不为空，并且相同，然后获取分类表中该分类的名称和编码
                                                                                                                Cell cell3 = sheets[0].getCell(7, k);
                                                                                                                String names = cell3.getContents().trim();

                                                                                                                //根据names查询id作为属性名的parentId
                                                                                                                int parentId = sortService.selectIdExcel(names);
                                                                                                                //然后获取该分类的属性信息

                                                                                                                int num=(columns-1)/2;
                                                                                                                for (int l = 0; l <num; l++) {
                                                                                                                    Cell cell4 = sheets[1].getCell(2*l+1, bb);
                                                                                                                    Cell cell5 = sheets[1].getCell(2*l+2, bb);
                                                                                                                    String attributeName = cell4.getContents().trim();
                                                                                                                    String attributeUnit = cell5.getContents().trim();
                                                                                                                    //单位可以为空，属性名不能不为空
                                                                                                                    if (attributeName!=null && !attributeName.equals("")){

                                                                                                                        JSONObject object1 = new JSONObject();
                                                                                                                        object1.put("id", "");
                                                                                                                        object1.put("parentId", parentId);
                                                                                                                        object1.put("name",attributeName);
                                                                                                                        object1.put("unit", attributeUnit);

                                                                                                                        int length=0;
                                                                                                                        //获取长度
                                                                                                                        //先获取当前的分类码
                                                                                                                        Cell cell6 = sheets[1].getCell(0, bb);
                                                                                                                        String trim = cell6.getContents().trim();

                                                                                                                        for (int m = 2; m < sheets.length; m++) {
                                                                                                                            //获取每张编码表中的分类码
                                                                                                                            Cell cell7 = sheets[m].getCell(0, 1);
                                                                                                                            String trim1 = cell7.getContents().trim();
                                                                                                                            boolean mm=false;
                                                                                                                            if (trim1.equals(trim)){
                                                                                                                                //如果分类编码相同，那就是储存这张表的信息,然后获取长度
                                                                                                                                int columns1 = sheets[m].getColumns();
                                                                                                                                int num1 = (columns1-1)/4;
                                                                                                                                boolean kk = false;
                                                                                                                                for (int n = 0; n < num1; n++) {
                                                                                                                                    Cell cell8 = sheets[m].getCell(4 * n + 2, 1);
                                                                                                                                    String trim2 = cell8.getContents().trim();
                                                                                                                                    if (trim2!=null && !trim2.equals("")){
                                                                                                                                        if (trim2.equals(attributeName)){
                                                                                                                                            //如果想听证明是同一个属性，然后获取长度
                                                                                                                                            Cell cell9 = sheets[m].getCell(4 * n + 1, 1);
                                                                                                                                            String trim3 = cell9.getContents().trim();
                                                                                                                                            length = Integer.parseInt(trim3);
                                                                                                                                            kk=true;
                                                                                                                                        }
                                                                                                                                    }

                                                                                                                                }
                                                                                                                                if (!kk){
                                                                                                                                    //说明后面没有这个属性，这个是普通属性，要设置function
//                                                                                                                System.out.println("普通属性"+attributeName);
                                                                                                                                    object1.put("length",length);
                                                                                                                                    common.add(object1);
                                                                                                                                }
                                                                                                                                mm=true;
                                                                                                                            }

                                                                                                                            if (mm){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                        }
                                                                                                                        object1.put("length",length);
                                                                                                                        array.add(object1);

                                                                                                                    }



                                                                                                                }


                                                                                                            }




                                                                                                        }



                                                                                                    }



                                                                                                }


                                                                                                String s1 = JSON.toJSONString(array);
                                                                                                List<AttributeName> attributeNames = JSONArray.parseArray(s1, AttributeName.class);
                                                                                                JSONArray attributeNameAdd = new JSONArray();
                                                                                                JSONArray attributeNameUpdate= new JSONArray();
                                                                                                //查询是否存在，不存在再执行添加操作

                                                                                                for (int k = 0; k < attributeNames.size(); k++) {
                                                                                                    Object o1 = attributeNames.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                    boolean b1 = attributeService.selectAttNameIfExistExcel(attributeName);
                                                                                                    if (!b1){
                                                                                                        attributeNameAdd.add(o1);
                                                                                                    }else {
                                                                                                        attributeNameUpdate.add(o1);
                                                                                                    }

                                                                                                }
//                                                        System.out.println("所有的"+attributeNameAdd);
                                                                                                JSONArray havaProduct = new JSONArray();//有物料的
                                                                                                JSONArray noneProduct = new JSONArray();//没有物料的
                                                                                                if (attributeNameAdd.size()>0){
                                                                                                    //大于0进行添加,查询该分类下面是否有物料存在
                                                                                                    for (int k = 0; k < attributeNameAdd.size(); k++) {
                                                                                                        Object o1 = attributeNameAdd.get(k);
                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                        AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                        int parentId = attributeName.getParentId();

                                                                                                        List<Product> products = productService.selectProductExistExcel(parentId);

                                                                                                        if (products.size()>0){
                                                                                                            havaProduct.add(o1);
                                                                                                        }else {
                                                                                                            noneProduct.add(o1);
                                                                                                        }

                                                                                                    }


                                                                                                    //先添加没有物料信息的
                                                                                                    if (noneProduct.size()>0){
                                                                                                        String s2 = JSON.toJSONString(noneProduct);
                                                                                                        List<AttributeName> attributeNames1 = JSONArray.parseArray(s2, AttributeName.class);
                                                                                                        attributeService.addAttNameExcel(attributeNames1);
                                                                                                    }
                                                                                                    System.out.println("没有物料的属性信息添加成功！");
                                                                                                    //再添加存在物料信息的，存在物料的添加成功后获取新添加属性名id，以及物料id
                                                                                                    if (havaProduct.size()>0){
                                                                                                        JSONArray productValue= new JSONArray();
                                                                                                        for (int k = 0; k < havaProduct.size(); k++) {
                                                                                                            Object o1 = havaProduct.get(k);
                                                                                                            String s2 = JSON.toJSONString(o1);
                                                                                                            AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                            attributeService.addAttNameExcelAndId(attributeName);
                                                                                                            int id1 = attributeName.getId();//得到了添加信息后的属性名id
                                                                                                            int parentId = attributeName.getParentId();

                                                                                                            //查询该分类下的物料信息
                                                                                                            List<Product> products = productService.selectProductExistExcel(parentId);

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
                                                                                                        //添加空的值
                                                                                                        String s4 = JSON.toJSONString(productValue);
                                                                                                        List<AttributeContent> attributeContents = JSONArray.parseArray(s4, AttributeContent.class);
                                                                                                        attributeService.addAttributeContentExcel(attributeContents);

                                                                                                    }


                                                                                                    three=true;

                                                                                                }else {
                                                                                                    if (attributeNameUpdate.size()>0){
                                                                                                        three=true;
                                                                                                    }

                                                                                                }

                                                                                                System.out.println("添加成功！");
                                                                                            }
                                                                                            else {
                                                                                                response.setContentType("text/json;charset=utf-8");
                                                                                                response.getWriter().write("分类表中未找到属性表的分类码："+con+"");
                                                                                                System.out.println("分类表中未找到属性表的分类码："+con+"");
                                                                                                FileUtil.delFile(filePath);
                                                                                            }



                                                                                            if (three){
                                                                                                JSONArray array2 = new JSONArray();//编码表
                                                                                                JSONArray array3 = new JSONArray();//function表

                                                                                                //先给普通属性添加function
                                                                                                for (int k = 0; k < common.size(); k++) {
                                                                                                    Object o1 = common.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    AttributeName attributeName = JSON.parseObject(s2, AttributeName.class);
                                                                                                    String name1 = attributeName.getName();
                                                                                                    int parentId = attributeName.getParentId();
                                                                                                    //查询名称id
                                                                                                    List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(name1, parentId);
                                                                                                    AttributeName attributeName1 = attributeNames1.get(0);
                                                                                                    int id1 = attributeName1.getId();
                                                                                                    JSONObject object1 = new JSONObject();
                                                                                                    object1.put("id","");
                                                                                                    object1.put("att_name_id",id1);
                                                                                                    object1.put("range",0);
                                                                                                    object1.put("auto_code",0);

                                                                                                    array3.add(object1);
                                                                                                }


                                                                                                //进行属性值信息的添加
                                                                                                JSONArray attCode= new JSONArray();
                                                                                                JSONArray upCode= new JSONArray();
                                                                                                JSONArray addMapping= new JSONArray();
                                                                                                JSONArray upMapping= new JSONArray();
                                                                                                JSONArray addFunction= new JSONArray();
                                                                                                JSONArray upFunction= new JSONArray();
                                                                                                boolean map=true;
                                                                                                boolean repeat=true;
                                                                                                boolean nn=true;
                                                                                                boolean nn1 = true;
                                                                                                boolean mm = true;
                                                                                                boolean bb=true;
                                                                                                boolean cc=true;
                                                                                                boolean dd = true;
                                                                                                boolean ee = true;
                                                                                                boolean rr = true;
                                                                                                boolean co = true;
                                                                                                boolean co3 = true;
                                                                                                for (int k = 2; k < sheets.length; k++) {
                                                                                                    //前两个表是分类表和属性表,后面的是编码表
                                                                                                    System.out.println("表："+sheets[k].getName());
                                                                                                    int rows2 = sheets[k].getRows();
                                                                                                    int columns1 = sheets[k].getColumns();
                                                                                                    int i1 = (columns1 - 1) / 4;


                                                                                                    //获取属性值和编码信息
                                                                                                    //占位
                                                                                                    int sum=0;
                                                                                                    for (int l = 0; l < i1; l++) {

                                                                                                        //获取占位
                                                                                                        Cell cell1 = sheets[k].getCell(l * 4 + 1, 1);
                                                                                                        String length = cell1.getContents().trim();

                                                                                                        if (length!=null && !length.equals("")){
                                                                                                            sum+=Integer.parseInt(length);
                                                                                                        }


                                                                                                        //获取属性名
                                                                                                        Cell cell2 = sheets[k].getCell(l * 4 + 2, 1);
                                                                                                        String attName = cell2.getContents().trim();


                                                                                                        //查询该属性的属性id
                                                                                                        //先获取分类码
                                                                                                        Cell cell4 = sheets[k].getCell(0, 1);
                                                                                                        String contents2 = cell4.getContents().trim();

                                                                                                        //去分类表中查询该分类码对应的分类名,获取分类表中的所有分类编码
                                                                                                        int rows3 = sheets[0].getRows();
                                                                                                        int ss=0;
                                                                                                        boolean sw = true;

                                                                                                        for (int m = 1; m <rows3 ; m++) {

                                                                                                            Cell cell3 = sheets[0].getCell(0, m);
                                                                                                            String contents1 = cell3.getContents().trim();
                                                                                                            if (contents1!=null && !contents1.equals("")){
                                                                                                                if (contents2.equals(contents1)){
                                                                                                                    ss=m;
                                                                                                                    sw=false;
                                                                                                                }

                                                                                                            }
                                                                                                            if (!sw){
                                                                                                                break;
                                                                                                            }
                                                                                                        }

                                                                                                        if (!sw){
                                                                                                            //然后去查询对应的分类名
                                                                                                            Cell cell5 = sheets[0].getCell(7, ss);
                                                                                                            String contents3 = cell5.getContents().trim();
                                                                                                            //得到了分类名，然后去查询分类id
                                                                                                            int i2 = sortService.selectIdExcel(contents3);


                                                                                                            //根据id和属性名查询属性名id
                                                                                                            if (attName!=null && !attName.equals("")){

                                                                                                                List<AttributeName> attributeNames1 = attributeService.selectNameIdExcel(attName, i2);
                                                                                                                if (attributeNames1.size()>0){
                                                                                                                    AttributeName attributeName = attributeNames1.get(0);

                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    int id1 = attributeName.getId();

                                                                                                                    //获取属性值
                                                                                                                    JSONArray array1  = new JSONArray();//编码表
                                                                                                                    JSONObject object4 = new JSONObject();//映射
                                                                                                                    JSONObject object5 = new JSONObject();//function
                                                                                                                    for (int m = 1; m < rows2; m++) {

                                                                                                                        JSONObject object1 = new JSONObject();//编码
                                                                                                                        //获取属性值
                                                                                                                        Cell cell3 = sheets[k].getCell(l * 4 + 3, m);
                                                                                                                        String contents1 = cell3.getContents().trim();
//                                                                                                            System.out.println("值"+contents1);
                                                                                                                        //获取编码
                                                                                                                        Cell cell6 = sheets[k].getCell(l * 4 + 4, m);
                                                                                                                        String contents4 = cell6.getContents().trim();

//                                                                                                            System.out.println("编码"+contents4);
                                                                                                                        //获取长度
                                                                                                                        Cell cell7 = sheets[k].getCell(l * 4 + 1, 1);
                                                                                                                        String length1 = cell7.getContents().trim();
                                                                                                                        int length2 = contents4.length();

                                                                                                                        //查询是否是开启顺序编码功能
                                                                                                                        Cell cell8 = sheets[k].getCell(l * 4 + 4, 0);


                                                                                                                        if ((contents1!=null && !contents1.equals(""))&&(contents4!=null && !contents4.equals(""))){

                                                                                                                            if (length2>0 && Integer.parseInt(length1) == length2){
                                                                                                                                object1.put("id", "");
                                                                                                                                object1.put("att_name_id", id1);
                                                                                                                                object1.put("code",contents4);
                                                                                                                                object1.put("att_value",contents1);
                                                                                                                                array1.add(object1);
                                                                                                                            }else {
                                                                                                                                //编码长度不匹配
                                                                                                                                cc=false;
                                                                                                                            }



                                                                                                                        }else {
                                                                                                                            //编码值为空
                                                                                                                            if ((contents4==null || contents4.equals("")&&(contents1!=null&&!contents1.equals("")))){
                                                                                                                                //查询是否开启了顺序编码功能
                                                                                                                                String trim = cell8.getContents().trim();
                                                                                                                                boolean contains1 = trim.contains("#");
                                                                                                                                if (contains1){
                                                                                                                                    //获取编码
                                                                                                                                    String main = OrderCode.main(Integer.parseInt(length1), m,"");

                                                                                                                                    object1.put("id", "");
                                                                                                                                    object1.put("att_name_id", id1);
                                                                                                                                    object1.put("code",main);
                                                                                                                                    object1.put("att_value",contents1);
                                                                                                                                    array1.add(object1);
                                                                                                                                }else {
                                                                                                                                    dd=false;
                                                                                                                                }

                                                                                                                            }
                                                                                                                            //属性值为空
                                                                                                                            if ((contents1==null || contents1.equals(""))&&(contents4!=null&&!contents4.equals(""))){
                                                                                                                                bb=false;
                                                                                                                            }



                                                                                                                        }



                                                                                                                        if (!dd){

                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码为空！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码为空！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                        if (!bb){
                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"编码:"+contents4+"对应的属性值为空！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"编码:"+contents4+"对应的属性值为空！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                        if (!cc){
                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                            response.getWriter().write("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码："+contents4+"长度不符合！");
                                                                                                                            System.out.println("表："+sheets[k].getName()+"中的"+attName+"属性值:"+contents1+"对应的编码："+contents4+"长度不符合！");
                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                            break;
                                                                                                                        }

                                                                                                                    }
                                                                                                                    if (!bb){
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    if (!cc){
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    if (!dd){
                                                                                                                        break;
                                                                                                                    }


                                                                                                                    //映射
                                                                                                                    object4.put("id", "");
                                                                                                                    object4.put("att_name_id", id1);
                                                                                                                    object4.put("sort_id",parentId);
                                                                                                                    if (l==0){
                                                                                                                        object4.put("begin_location",1);
                                                                                                                        object4.put("end_location",length);
                                                                                                                        object4.put("length",length);
                                                                                                                    }else {
                                                                                                                        //获取长度
                                                                                                                        int s5= Integer.parseInt(length);
                                                                                                                        int s6=0;
                                                                                                                        Cell cell9= sheets[k].getCell(l * 4 + 1, 1);
                                                                                                                        String trim1 = cell9.getContents().trim();

                                                                                                                        if (trim1!=null && !trim1.equals("")){
                                                                                                                            for (int n = 0; n < l; n++) {
                                                                                                                                Cell cell8= sheets[k].getCell(n * 4 + 1, 1);
                                                                                                                                String trim = cell8.getContents().trim();
                                                                                                                                if (trim!=null && !trim.equals("")){
                                                                                                                                    s5+=Integer.parseInt(trim) ;
                                                                                                                                }


                                                                                                                            }
                                                                                                                            s6=s5-(Integer.parseInt(trim1)-1);
                                                                                                                            object4.put("end_location",s5);
                                                                                                                            object4.put("begin_location",s6);
                                                                                                                            object4.put("length",length);
                                                                                                                        }

                                                                                                                    }


                                                                                                                    array2.add(object4);

                                                                                                                    //function
                                                                                                                    object5.put("id","");
                                                                                                                    object5.put("att_name_id",id1);
                                                                                                                    //判断是否是范围的

                                                                                                                    Cell cell3 = sheets[k].getCell(l * 4 + 3, 0);
                                                                                                                    Cell cell6 = sheets[k].getCell(l * 4 + 4, 0);
                                                                                                                    String trim1 = cell6.getContents().trim();
                                                                                                                    String trim = cell3.getContents().trim();

                                                                                                                    if (trim!=null && !trim.equals("")){
                                                                                                                        boolean sss = trim.contains("$");
                                                                                                                        if (sss){
                                                                                                                            //包含
                                                                                                                            int rows4 = sheets[k].getRows();
                                                                                                                            for (int m = 1; m <rows4 ; m++) {
                                                                                                                                Cell cell7 = sheets[k].getCell(l * 4 + 3, m);
                                                                                                                                Cell cell8 = sheets[k].getCell(l * 4 + 4, m);
                                                                                                                                String trim2 = cell7.getContents().trim();
                                                                                                                                String trim3 = cell8.getContents().trim();

                                                                                                                                if ((trim2!=null&&!trim2.equals(""))&&(trim3!=null&& !trim3.equals(""))){
                                                                                                                                    //判断值是否符合范围的格式

                                                                                                                                    boolean main = RegexMatches.main(trim2);
                                                                                                                                    if (!main){
                                                                                                                                        //不符合规则
                                                                                                                                        rr=false;
                                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                                        response.getWriter().write("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                        System.out.println("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                                    }else {
                                                                                                                                        //判断是否格式正确：<10~5]
                                                                                                                                        co = Compare.compare(trim2);
                                                                                                                                        if (!co){
                                                                                                                                            co = false;
                                                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                                                            response.getWriter().write("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"数据格式错误！");
                                                                                                                                            System.out.println("表"+sheets[k].getName()+"中的"+attName+"属性值"+trim2+"不符合范围的格式！");
                                                                                                                                            FileUtil.delFile(filePath);
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }

                                                                                                                                if (!rr){
                                                                                                                                    break;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (rr && co){
                                                                                                                                object5.put("range",1);
                                                                                                                            }

                                                                                                                        }else {
                                                                                                                            //不包含
                                                                                                                            object5.put("range",0);
                                                                                                                        }
                                                                                                                    }
                                                                                                                    //判断是否开启值为编码或者顺序编码的功能
                                                                                                                    if (trim1!=null && !trim1.equals("")){
                                                                                                                        boolean contains = trim1.contains("&");
                                                                                                                        boolean contains1 = trim1.contains("#");
                                                                                                                        if (contains){
                                                                                                                            object5.put("auto_code", 1);
                                                                                                                        }else if (contains1){
                                                                                                                            object5.put("auto_code", 2);

                                                                                                                        }
                                                                                                                        else {
                                                                                                                            object5.put("auto_code", 0);
                                                                                                                        }
                                                                                                                    }

                                                                                                                    if (rr && co){
                                                                                                                        array3.add(object5);
                                                                                                                    }else {
                                                                                                                        break;
                                                                                                                    }



                                                                                                                    //得到了所有的数据，查询是否重复
//                                                                                                                    System.out.println("开始");

                                                                                                                    JSONArray array4 = new JSONArray();//临时的范围属性内容
                                                                                                                    String aa="";
                                                                                                                    String aa1="";
                                                                                                                    String s1 = array1.toJSONString();
                                                                                                                    List<AttributeValue> attributeValues1 = JSONArray.parseArray(s1, AttributeValue.class);
                                                                                                                    for (int m = 0; m < array1.size(); m++) {
                                                                                                                        aa="";
                                                                                                                        aa1="";
                                                                                                                        Object o1 = array1.get(m);
                                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                                        AttributeValue attributeValue = JSON.parseObject(s2, AttributeValue.class);
                                                                                                                        String attValue = attributeValue.getAttValue().replaceAll(" +", "");
                                                                                                                        String code4 = attributeValue.getCode().replaceAll(" +", "");
                                                                                                                        int attNameId1 = attributeValue.getAttNameId();


                                                                                                                        for (int n = 0; n < array1.size(); n++) {
                                                                                                                            //比较属性值是否重复

                                                                                                                            if (m!=n){
                                                                                                                                Object o4 = array1.get(n);
                                                                                                                                String s4 = JSON.toJSONString(o4);
                                                                                                                                AttributeValue attributeValue1 = JSON.parseObject(s4, AttributeValue.class);
                                                                                                                                String attValue1 = attributeValue1.getAttValue().replaceAll(" +", "");
                                                                                                                                if (attValue1.equals(attValue)){

                                                                                                                                    nn=false;
                                                                                                                                    aa  =  attValue;

                                                                                                                                }
                                                                                                                                String code1 = attributeValue1.getCode().replaceAll(" +", "");
                                                                                                                                if (code1.equals(code4)&&!code1.equals("") && code1!=null){
                                                                                                                                    nn1=false;
                                                                                                                                    aa1  =  code4;

                                                                                                                                }
                                                                                                                            }
                                                                                                                            if (!nn|| !nn1){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                        }

//                                                                                                                        for (int n = 0; n < array1.size(); n++) {
//                                                                                                                            //编码值是否重复
//
//                                                                                                                            if (m!=n){
//                                                                                                                                Object o4 = array1.get(n);
//                                                                                                                                String s4 = JSON.toJSONString(o4);
//                                                                                                                                AttributeValue attributeValue1 = JSON.parseObject(s4, AttributeValue.class);
//                                                                                                                                String code1 = attributeValue1.getCode();
//                                                                                                                                if (code1.equals(code4)&&!code1.equals("") && code1!=null){
//                                                                                                                                    nn1=false;
//                                                                                                                                    aa1  =  code4;
//
//                                                                                                                                }
//                                                                                                                            }
//                                                                                                                            if (!nn1){
//                                                                                                                                break;
//                                                                                                                            }
//
//                                                                                                                        }
                                                                                                                        if (nn && nn1){
                                                                                                                            for (int n = 0; n < array3.size(); n++) {
                                                                                                                                Object o2 = array3.get(n);
                                                                                                                                String s3 = JSON.toJSONString(o2);
                                                                                                                                AttributeFunction attributeFunction = JSON.parseObject(s3, AttributeFunction.class);
                                                                                                                                int attNameId = attributeFunction.getAttNameId();
                                                                                                                                int range = attributeFunction.getRange();


                                                                                                                                if (attNameId==attNameId1 && range==1){
                                                                                                                                    //是范围的，获取当前attNameid的所有内容然后跟excel文档中的数据比较
                                                                                                                                    array4.add(o1);
                                                                                                                                }


                                                                                                                            }


                                                                                                                        }



                                                                                                                        if (!nn || !nn1){
                                                                                                                            break;
                                                                                                                        }

                                                                                                                    }
                                                                                                                    if (array4.size()>0){
                                                                                                                        //查看是否重叠
                                                                                                                        for (int n = 0; n < array4.size(); n++) {
                                                                                                                            Object o2 = array4.get(n);
                                                                                                                            String s3 = JSON.toJSONString(o2);
                                                                                                                            AttributeValue attributeValue1 = JSON.parseObject(s3, AttributeValue.class);
                                                                                                                            String attValue1 = attributeValue1.getAttValue();
                                                                                                                            int attNameId = attributeValue1.getAttNameId();

                                                                                                                            //跟文档中的数据比较
                                                                                                                            for (int p = 0; p < array4.size(); p++) {
                                                                                                                                if (n!=p){

                                                                                                                                    Object o3 = array4.get(p);
                                                                                                                                    String s4 = JSON.toJSONString(o3);
                                                                                                                                    AttributeValue attributeValue2 = JSON.parseObject(s4, AttributeValue.class);
                                                                                                                                    String attValue2 = attributeValue2.getAttValue();
                                                                                                                                    boolean b1 = Compare.compare3(attValue1, attValue2);

                                                                                                                                    if (b1){
                                                                                                                                        co3=false;
                                                                                                                                        aa=attValue1+attValue2;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                if (!co3){
                                                                                                                                    break;
                                                                                                                                }

                                                                                                                            }

                                                                                                                            if (!co3){
                                                                                                                                break;
                                                                                                                            }

                                                                                                                            //跟数据库中的数据比较
                                                                                                                            //根据attNameid获取属性值
                                                                                                                            if (co3){
                                                                                                                                //要先查询数据库中是否存在
                                                                                                                                boolean b2 = attributeValueService.selectValueExistExcel(attributeValue1);
                                                                                                                                if (!b2){
                                                                                                                                    List<AttributeValue> attributeValues = attributeValueService.selectByAttNameId(attNameId);
                                                                                                                                    for (int p = 0; p < attributeValues.size(); p++) {
                                                                                                                                        AttributeValue attributeValue2 = attributeValues.get(p);
                                                                                                                                        String attValue2 = attributeValue2.getAttValue();
                                                                                                                                        boolean b1 = Compare.compare3(attValue2, attValue1);
                                                                                                                                        if (b1){
                                                                                                                                            co3=false;
                                                                                                                                            aa=attValue2+attValue1;
                                                                                                                                            break;
                                                                                                                                        }

                                                                                                                                    }
                                                                                                                                }

                                                                                                                            }

                                                                                                                        }
                                                                                                                    }
                                                                                                                    //判断是否重复
                                                                                                                    if (!nn){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"属性值"+aa+"重复！");
                                                                                                                        System.out.println(name1+"中的"+attName+"属性值"+aa+"重复！");
                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;

                                                                                                                    }else if (!co3){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"两个属性值"+aa+"范围重叠！");
                                                                                                                        System.out.println(name1+"中的"+attName+"两个属性值"+aa+"范围重叠！");
                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;
                                                                                                                    }else if (!nn1){
                                                                                                                        String name1 = sheets[k].getName();
                                                                                                                        repeat = false;
                                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                                        response.getWriter().write(name1+"中的"+attName+"编码值"+aa1+"重复！");
                                                                                                                        System.out.println("这里吗");
                                                                                                                        System.out.println(name1+"中的"+attName+"编码值"+aa1+"重复！");

                                                                                                                        FileUtil.delFile(filePath);
                                                                                                                        break;
                                                                                                                    }

                                                                                                                    else {


                                                                                                                        //查询数据库中是否有这个了
                                                                                                                        for (int m = 0; m < array1.size(); m++) {
                                                                                                                            Object o1 = array1.get(m);
                                                                                                                            String s2 = JSON.toJSONString(o1);
                                                                                                                            AttributeValue attributeValue = JSON.parseObject(s2, AttributeValue.class);
                                                                                                                            boolean b1 = attributeValueService.selectValueExistExcel(attributeValue);
                                                                                                                            if (b1){
                                                                                                                                //数据库中已存在
                                                                                                                                upCode.add(o1);
                                                                                                                            }else {
                                                                                                                                //数据库中不存在
                                                                                                                                attCode.add(o1);
                                                                                                                            }
                                                                                                                        }


                                                                                                                    }



                                                                                                                }else {
                                                                                                                    //没有找到属性名
                                                                                                                    response.setContentType("text/json;charset=utf-8");
                                                                                                                    response.getWriter().write("表："+sheets[k].getName()+"未找到属性"+attName+"");
                                                                                                                    System.out.println("表："+sheets[k].getName()+"未找到属性"+attName+"");
                                                                                                                    FileUtil.delFile(filePath);
                                                                                                                    mm = false;
                                                                                                                }


                                                                                                            }
                                                                                                        }
                                                                                                        else {
                                                                                                            System.out.println("没有找到分类码："+contents2);
                                                                                                            response.setContentType("text/json;charset=utf-8");
                                                                                                            response.getWriter().write("没有找到分类码："+contents2);
                                                                                                            FileUtil.delFile(filePath);
                                                                                                            mm=false;
                                                                                                        }

                                                                                                        if (!mm){
                                                                                                            break;
                                                                                                        }

                                                                                                    }

                                                                                                    if (!nn){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!nn1){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!co3){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!mm){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!bb){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!cc){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!dd){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (!rr){
                                                                                                        break;
                                                                                                    }
                                                                                                    if (sum>9){
                                                                                                        ee = false;
                                                                                                        System.out.println("表："+sheets[k].getName()+"的总占位超过了9！");
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("表："+sheets[k].getName()+"的总占位超过了9！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                        break;
                                                                                                    }


                                                                                                }


                                                                                                //得到了所有的映射
                                                                                                for (int k = 0; k < array2.size(); k++) {
                                                                                                    Object o1 = array2.get(k);
                                                                                                    String s2 = JSON.toJSONString(o1);
                                                                                                    Mapping mapping = JSON.parseObject(s2, Mapping.class);

                                                                                                    boolean b1 = mappingService.selectMapIfExistExcel(mapping);
                                                                                                    int sortId = mapping.getSortId();

                                                                                                    //查询映射所在的分类下是否存在物料
                                                                                                    List<Product> products = productService.selectProductExistExcel(sortId);
                                                                                                    if (b1){
                                                                                                        //存在映射
                                                                                                        if (products.size()>0){
                                                                                                            Product product = products.get(0);
                                                                                                            String name1 = product.getName();
                                                                                                            System.out.println(name1+"中存在物料，不能进行映射的修改！");
                                                                                                            map = false;
                                                                                                        }else {
                                                                                                            //重复，添加到更新中
                                                                                                            upMapping.add(o1);
                                                                                                        }

                                                                                                    }else {
                                                                                                        //不存在映射，添加到新增中
                                                                                                        addMapping.add(o1);
                                                                                                    }
                                                                                                    if (!map){
                                                                                                        break;
                                                                                                    }
                                                                                                }
//                                                                                                System.out.println("结束");


                                                                                                if (repeat&&nn&&mm&&bb&&cc&&dd&&ee&&rr&&co3){
                                                                                                    String scv="";
                                                                                                    boolean cv = true;
                                                                                                    //执行更新和，添加操作
                                                                                                    if (addMapping.size()>0){
                                                                                                        String s2 = JSON.toJSONString(addMapping);
                                                                                                        List<Mapping> mappings = JSONArray.parseArray(s2, Mapping.class);
                                                                                                        //调用service
                                                                                                        mappingService.addMapExcel(mappings);
                                                                                                        System.out.println("映射添加成功");
                                                                                                    }

                                                                                                    if (upMapping.size()>0){
                                                                                                        //执行更新操作
                                                                                                        String s2 = JSON.toJSONString(upMapping);
                                                                                                        List<Mapping> mappings = JSONArray.parseArray(s2, Mapping.class);
                                                                                                        mappingService.updateMapExcel(mappings);
                                                                                                        System.out.println("映射更新成功");
                                                                                                    }




                                                                                                    //得到了所有的function,查询是否重复以及是否可以添加
                                                                                                    for (int k = 0; k < array3.size(); k++) {
                                                                                                        Object o1 = array3.get(k);
                                                                                                        String s2 = JSON.toJSONString(o1);
                                                                                                        AttributeFunction attributeFunction = JSON.parseObject(s2, AttributeFunction.class);
                                                                                                        //查询function是否存在
                                                                                                        boolean b1 = attributeFunctionService.selectExistExcel(attributeFunction);
                                                                                                        if (!b1){
                                                                                                            addFunction.add(o1);
                                                                                                        }
                                                                                                    }

                                                                                                    if (addFunction.size()>0){
                                                                                                        String s2 = JSON.toJSONString(addFunction);
                                                                                                        List<AttributeFunction> attributeFunctions = JSONArray.parseArray(s2, AttributeFunction.class);
                                                                                                        //执行添加操作
                                                                                                        attributeFunctionService.addExcel(attributeFunctions);

                                                                                                        System.out.println("添加function成功");
                                                                                                    }




                                                                                                    if (attCode.size()>0){

                                                                                                        for (int m = 0; m < attCode.size(); m++) {
                                                                                                            Object o1 = attCode.get(m);
                                                                                                            String s21= JSON.toJSONString(o1);
                                                                                                            AttributeValue attributeValue = JSON.parseObject(s21, AttributeValue.class);
                                                                                                            boolean b1 = attributeValueService.selectValueCodeExistExcel(attributeValue);
                                                                                                            boolean b2 = attributeValueService.selectValueCodesExistExcel(attributeValue);
                                                                                                            if (b1||b2){
                                                                                                                //数据库中已存在
                                                                                                                if (b1){
                                                                                                                    //查询属性名
                                                                                                                    int attNameId = attributeValue.getAttNameId();
                                                                                                                    List<AttributeName> attributeNames = attributeService.selectById(attNameId);
                                                                                                                    AttributeName attributeName = attributeNames.get(0);
                                                                                                                    String name1 = attributeName.getName();
                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    //查询分类名
                                                                                                                    List<Sort> sorts2 = sortService.selectSortById(parentId);
                                                                                                                    Sort sort1 = sorts2.get(0);
                                                                                                                    String name2 = sort1.getName();

                                                                                                                    cv = false;
                                                                                                                    String attValue = attributeValue.getAttValue();
                                                                                                                    String code1 = attributeValue.getCode();
                                                                                                                    scv="四级分类："+name2+"中属性："+name1+"中属性值为"+attValue+"编码值为"+code1+"的属性值数据库中已存在！";
                                                                                                                    break;
                                                                                                                }else if (b2){
                                                                                                                    //查询属性名
                                                                                                                    int attNameId = attributeValue.getAttNameId();
                                                                                                                    List<AttributeName> attributeNames = attributeService.selectById(attNameId);
                                                                                                                    AttributeName attributeName = attributeNames.get(0);
                                                                                                                    String name1 = attributeName.getName();
                                                                                                                    int parentId = attributeName.getParentId();
                                                                                                                    //查询分类名
                                                                                                                    List<Sort> sorts2 = sortService.selectSortById(parentId);
                                                                                                                    Sort sort1 = sorts2.get(0);
                                                                                                                    String name2 = sort1.getName();
                                                                                                                    cv = false;
                                                                                                                    String attValue = attributeValue.getAttValue();
                                                                                                                    String code1 = attributeValue.getCode();
                                                                                                                    scv="四级分类："+name2+"中属性："+name1+"中属性值为"+attValue+"编码值为"+code1+"的编码值数据库中已存在！";
                                                                                                                    break;
                                                                                                                }

                                                                                                            }
                                                                                                        }
                                                                                                        if (cv){
                                                                                                            //数据库中不存在
                                                                                                            // 执行添加操作
                                                                                                            String s2 = JSON.toJSONString(attCode);


                                                                                                            List<AttributeValue> attributeValues = JSONArray.parseArray(s2, AttributeValue.class);
                                                                                                            //先查询要添加的是否跟数据库中的重复，属性值以及编码

                                                                                                            attributeValueService.addCodeExcel(attributeValues);
                                                                                                            //响应数据
//                                                                    response.setContentType("text/json;charset=utf-8");
//                                                                    response.getWriter().write(s2);
                                                                                                            System.out.println("添加编码成功！");
                                                                                                        }

                                                                                                    }else {
                                                                                                        if (upCode.size()>0){
                                                                                                            System.out.println("更新编码成功！");
                                                                                                        }
                                                                                                    }
                                                                                                    if (cv){
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write("一键导入成功！");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                    }else {
                                                                                                        response.setContentType("text/json;charset=utf-8");
                                                                                                        response.getWriter().write(""+scv+"");
                                                                                                        System.out.println(""+scv+"");
                                                                                                        FileUtil.delFile(filePath);
                                                                                                    }

                                                                                                }


                                                                                            }

                                                                                        }



                                                                                    }


                                                                                }


                                                                            }
                                                                        }else {
                                                                            //四级分类的分类名或者编码为空
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("四级分类的分类名或编码为空!");
                                                                            System.out.println("四级分类的分类名或编码为空!");
                                                                            FileUtil.delFile(filePath);
                                                                        }


                                                                    }else {
                                                                        threes++;
                                                                        if (threes==rows){
                                                                            //三级分类的分类名或者编码为空
                                                                            response.setContentType("text/json;charset=utf-8");
                                                                            response.getWriter().write("三级分类的分类名或编码为空!");
                                                                            System.out.println("三级分类的分类名或编码为空!");
                                                                            FileUtil.delFile(filePath);
                                                                        }

                                                                    }

                                                                }
                                                                if (threesHave>1){
                                                                    //三级分类中存在多个值
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("属性表中存在多个三级分类！");
                                                                    System.out.println("属性表中存在多个三级分类!");
                                                                    FileUtil.delFile(filePath);
                                                                }
                                                            }else {
                                                                twos++;
                                                                if (twos==rows){
                                                                    //二级分类的分类名或者编码为空
                                                                    response.setContentType("text/json;charset=utf-8");
                                                                    response.getWriter().write("二级分类的分类名或编码为空!");
                                                                    System.out.println("二级分类的分类名或编码为空!");
                                                                    FileUtil.delFile(filePath);
                                                                }

                                                            }
                                                        }
                                                        if (twosHave>1){
                                                            //二级分类中存在多个值
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("属性表中存在多个二级分类！");
                                                            System.out.println("属性表中存在多个二级分类!");
                                                            FileUtil.delFile(filePath);
                                                        }




                                                    }
                                                    else {
                                                        one++;
                                                        if (one==rows){
                                                            //一级分类的编码或者分类名为空
                                                            response.setContentType("text/json;charset=utf-8");
                                                            response.getWriter().write("一级分类的分类名或编码为空!");
                                                            System.out.println("一级分类的分类名或编码为空!");
                                                            FileUtil.delFile(filePath);
                                                        }

                                                    }
                                                }
                                                if (oneHave>1){
                                                    //一级分类中存在多个值
                                                    response.setContentType("text/json;charset=utf-8");
                                                    response.getWriter().write("属性表中存在多个一级分类！");
                                                    System.out.println("属性表中存在多个一级分类!");
                                                    FileUtil.delFile(filePath);
                                                }




                                            }


                                        }




                                        workbook.close();

                                    } catch (BiffException | IOException e) {
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


