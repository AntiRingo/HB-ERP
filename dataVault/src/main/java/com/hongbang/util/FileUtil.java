package com.hongbang.util;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：文件处理
 * from：fhadmin.cn
 */
public class FileUtil {


    /**获取文件大小 返回 KB 保留3位小数  没有文件时返回0
     * @param filepath 文件完整路径，包括文件名
     * @return
     */
    public static Double getFilesize(String filepath){
        File backupath = new File(filepath);
        System.out.println(Double.valueOf(backupath.length())/1000.000);
        return Double.valueOf(backupath.length())/1000.000;
    }

    /**
     * 创建目录
     * @paramdestDirName目标目录名
     * @return
     */
    public static Boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if(!dir.getParentFile().exists()){				//判断有没有父路径，就是判断文件整个路径是否存在
            return dir.getParentFile().mkdirs();		//不存在就全部创建
        }
        return false;
    }

    /**
     * 删除文件
     * @param filePathAndName
     *            String 文件路径及名称 如c:/fqf.txt
     * @paramfileContent
     *            String
     * @return boolean
     */
    public static void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();

            File myDelFile = new File(filePath);
            myDelFile.delete();
            System.out.println("ture");
        } catch (Exception e) {
            System.out.println("f");
            e.printStackTrace();
        }
    }

    /**
     * 读取到字节数组0
     * @param filePath //路径
     * @throws IOException
     */
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 读取到字节数组1
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filePath) throws IOException {

        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 读取到字节数组2
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException(filePath);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @paramfilename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filePath) throws IOException {

        FileChannel fc = null;
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filePath, "r");
            fc = rf.getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            //System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rf.close();
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    //遍历文件夹    返回文件名
   public static String listFiles(String filePathAndName){
        String filePath= filePathAndName;
        filePath= filePath.toString();
       File myFile = new File(filePath);
       File[] files = myFile.listFiles();
       String s="";
       for (int i=0; i< files.length;i++){
           if (files[i].isFile()){
               s+=files[i].getName()+",";

           }
//           else if(files[i].isDirectory()) {
//
//
//             String filePath1= String.valueOf(files[i]);
//             filePath1=filePath1.toString();
//               java.io.File myFile1 = new java.io.File(filePath1);
//               File[] files1 = myFile1.listFiles();
//               for (int j = 0; j < files1.length; j++) {
//                   if (files1[j].isFile()){
//                       s+=files1[j].getName()+",";
//
//
//                   }
//               }
//
//
//
//
//           }
       }

       return s;
   }



   //获取文件最后修改时间


   public static String time(String filePathAndName){
       String filePath= filePathAndName;
       filePath= filePath.toString();
       File myFile = new File(filePath);
       File[] files = myFile.listFiles();
       String s="";
       for (int i=0; i< files.length;i++){
           if (files[i].isFile()){




               SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

               s+=sdf.format(files[i].lastModified())+",";


           }
//           else if(files[i].isDirectory()) {
//
//
//               String filePath1= String.valueOf(files[i]);
//               filePath1=filePath1.toString();
//               java.io.File myFile1 = new java.io.File(filePath1);
//               File[] files1 = myFile1.listFiles();
//               for (int j = 0; j < files1.length; j++) {
//                   if (files1[j].isFile()){
//                       s+=new Date(files[i].lastModified())+",";
//
//                   }
//               }
//
//
//
//
//           }
       }

       return s;
   }

   //获取文件大小

    public static String size(String filePathAndName){
        String filePath= filePathAndName;
        filePath= filePath.toString();
        File myFile = new File(filePath);
        File[] files = myFile.listFiles();
        String s="";
        for (int i=0; i< files.length;i++){
            if (files[i].isFile()){
                s+= files[i].length()+",";


            }
//            else if(files[i].isDirectory()) {
//
//
//                String filePath1= String.valueOf(files[i]);
//                filePath1=filePath1.toString();
//                java.io.File myFile1 = new java.io.File(filePath1);
//                File[] files1 = myFile1.listFiles();
//                for (int j = 0; j < files1.length; j++) {
//                    if (files1[j].isFile()){
//                        s+= files[i].length()+"字节"+",";
//
//                    }
//                }
//
//
//
//
//            }
        }

        return s;
    }



    //获取文件的后缀名

    public static String substring(String filePathAndName){
        String filePath= filePathAndName;
        filePath= filePath.toString();
        File myFile = new File(filePath);
        File[] files = myFile.listFiles();
        String s="";
        for (int i=0; i< files.length;i++){
            if (files[i].isFile()){
                String fileName = files[i].getName();

                s+=fileName.substring(fileName.lastIndexOf(".")+1)+"文件"+",";

            }else if(files[i].isDirectory()) {


                String filePath1= String.valueOf(files[i]);
                filePath1=filePath1.toString();
                File myFile1 = new File(filePath1);
                File[] files1 = myFile1.listFiles();
                for (int j = 0; j < files1.length; j++) {
                    if (files1[j].isFile()){
                        String fileName = files[i].getName();

                        s+=fileName.substring(fileName.lastIndexOf(".")+1)+"文件"+",";



                    }
                }




            }
        }

        return s;
    }

    public static boolean copyFile(String file1, String file2) {
        File in = new File(file1);
        File out = new File(file2);
        //要拷贝的文件
        if (!in.exists()) {
            System.out.println(in.getAbsolutePath() + "要拷贝的文件路径错误！！！");
            return false;
        }
        //目标文件夹,如果不存在则创建
        if (!out.exists()) {
            out.mkdirs();
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //创建读取 要拷贝的文件
            fis = new FileInputStream(in);
            String name = in.getName();
            //                    得到文件后缀名
            int index = name.lastIndexOf(".");
            String endWith = name.substring(index);
            //新文件名
            String newFileName = System.currentTimeMillis() + endWith;
            //创建 要复制到的文件
            fos = new FileOutputStream(new File(file2 + "\\" + newFileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int c;
        //创建字节数组
        byte[] b = new byte[1024 * 5];
        try {
            //写入文件
            while ((c = fis.read(b)) != -1) {
                fos.write(b, 0, c);
            }
            fis.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }




    //删除文件夹以及文件夹下的文件
    public static Boolean deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                f.delete();
                //打印文件名
                System.out.println("文件名：" + f.getName());
            }
        }
        //文件夹删除
        file.delete();
        System.out.println("目录名：" + file.getName());
        return true;
    }







//
//        /**
//         * 计算物料字符串的总数量（支持带*数量的物料项）
//         * @param input 物料字符串（如："U24 * 22 U25 U1 U11 U2 U7 U3"）
//         * @return 总数量
//         */


    public static int calculateTotalQuantity(String input) {
        // 修正后的正则表达式：
        // 1. 范围项：([A-Za-z]+)(\\d+)-\\1(\\d+) 匹配 a01-a100 格式（字母+数字-相同字母+数字）
        // 2. 普通项：([^*\\s-]+)(?:\\s*\\*\\s*(\\d+))? 匹配 U24 或 U24 * 22 格式（不含*、-、空格）
        Pattern pattern = Pattern.compile("(?:([A-Za-z]+)(\\d+)-\\1(\\d+))|([^*\\s-]+)(?:\\s*\\*\\s*(\\d+))?");
        Matcher matcher = pattern.matcher(input.trim()); // 去除首尾空格

        int total = 0;

        while (matcher.find()) {
            int quantity = 0;
            // 检查是否匹配范围项（分组1-3有值）
            if (matcher.group(1) != null) {
                String startStr = matcher.group(2);
                String endStr = matcher.group(3);
                try {
                    int start = Integer.parseInt(startStr);
                    int end = Integer.parseInt(endStr);
                    quantity = end - start + 1;
                    // 处理起始大于结束的情况，视为0
                    if (quantity < 0) {
                        quantity = 0;
                    }
                } catch (NumberFormatException e) {
                    // 数字格式错误，数量视为0
                    quantity = 0;
                }
            }
            // 检查是否匹配普通项（分组4-5有值）
            else if (matcher.group(4) != null) {
                String quantityStr = matcher.group(5);
                if (quantityStr != null) {
                    try {
                        quantity = Integer.parseInt(quantityStr);
                    } catch (NumberFormatException e) {
                        // *后非数字，数量视为1
                        quantity = 1;
                    }
                } else {
                    // 无*，数量视为1
                    quantity = 1;
                }
            }
            total += quantity;
        }

        return total;
    }

}