package com.hongbang.util;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math; // 导入类



import static java.lang.Integer.parseInt;

public class OrderCode {

//    static String cs="";



    //Math.pow(2, 3) // pow方法调用，2的3次方
    public static String main(int length, int number,String cs) {

//顺序编码
        String [] list ={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
        //储存编码

            //length是编码的长度，number是第几个数

            if (length>0){

                //求shang
                int s = (int) (number/(Math.pow(list.length,length-1)));
//                System.out.println("商"+s);
                // console.log(s)
                //求余数
                int ys = (int) (number%(Math.pow(list.length,length-1) ));
                // console.log(ys)
//                System.out.println("余数"+ys);

                cs=cs+list[s];
//                System.out.println("编码"+cs);
                return  main(length-1,ys,cs);


            }else {


//                System.out.println("编码"+cs);
                return cs;

            }








    }

}
