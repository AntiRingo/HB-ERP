package com.hongbang.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;

//做运算的方法
//        add(BigDecimal)
//
//        BigDecimal对象中的值相加，然后返回这个对象。
//
//        subtract(BigDecimal)
//
//        BigDecimal对象中的值相减，然后返回这个对象。
//
//        multiply(BigDecimal)
//
//        BigDecimal对象中的值相乘，然后返回这个对象。
//
//        divide(BigDecimal)
//
//        BigDecimal对象中的值相除，然后返回这个对象。


//做比较的方法
//   BigDecimal a = new BigDecimal(10);
//           BigDecimal b = new BigDecimal(5);
//
//           if (a.compareTo(b) == 0)
//           System.out.println("a = b");
//
//           if (a.compareTo(b) == -1)
//           System.out.println("a < b");
//
//           if (a.compareTo(b) == 1)
//           System.out.println("a > b");
//
//           if (a.compareTo(b) != 0)
//           System.out.println("a != b");
//
//           if (a.compareTo(b) != -1)
//           System.out.println("a >= b");
//
//           if (a.compareTo(b) != 1)
//           System.out.println("a <= b");

//单位换算
public class Compare {
    //power单位换算
    public static BigDecimal power(String str) {
        String regex = "[fpnum%KMGT]";
        String regex1 = "([-]?[0-9]+[.][0-9]+)|([-]?[0-9]*)";

        String replacement = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(str);
        String number = matcher.replaceAll(replacement);//数字
        String power = matcher1.replaceAll(replacement);//power符号
        int length = power.length();
        if (length>0){
            String num = "";



            switch (power) {
                case "f":
                    num = "0.000000000000001";
                    break;
                case "p":
                    num = "0.000000000001";
                    break;
                case "n":
                    num = "0.000000001";
                    break;
                case "u":
                    num = "0.000001";
                    break;
                case "m":
                    num = "0.001";
                    break;
                case "%":
                    num = "0.01";
                    break;
                case "K":
                    num = "1000";
                    break;
                case "M":
                    num = "1000000";
                    break;
                case "G":
                    num = "1000000000";
                    break;
                case "T":
                    num = "1000000000000";
                    break;
            }

            BigDecimal a= new BigDecimal(num);
            BigDecimal b= new BigDecimal(number);
            //返回两个数相乘
            return a.multiply(b);

        }else {
            return new BigDecimal(number);
        }



    }

    //在符合pattern后，判断范围规定是否符合规定，例如<100~80]就不符合规定
    public static boolean compare(String str){
        if (str.length()>0){
            String pattern = "^\\[.*\\]$";
            String patterns = "^\\<.*\\]$";
            String pattern1 = "^\\<.*\\>$";
            String patterns1 = "^\\[.*\\>$";
            Pattern r = Pattern.compile(pattern);
            Pattern rs = Pattern.compile(patterns);
            Pattern r1 = Pattern.compile(pattern1);
            Pattern rs1 = Pattern.compile(patterns1);

            Matcher m = r.matcher(str);
            Matcher ms = rs.matcher(str);
            Matcher m1 = r1.matcher(str);
            Matcher ms1 = rs1.matcher(str);
            int i = 0;
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = str.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] split = replace1.split("~");
                BigDecimal power = power(split[0]);
                BigDecimal power1 = power(split[1]);
                //比较两个数的大小
                i = power.compareTo(power1);



            }else if (ms.matches()){
                String replace = str.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] split = replace1.split("~");
                BigDecimal power = power(split[0]);
                BigDecimal power1 = power(split[1]);
                //比较两个数的大小
                 i = power.compareTo(power1);

            }else if (m1.matches()){
                String replace = str.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] split = replace1.split("~");
                BigDecimal power = power(split[0]);
                BigDecimal power1 = power(split[1]);
                //比较两个数的大小
                 i = power.compareTo(power1);

            }else if (ms1.matches()){
                String replace = str.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] split = replace1.split("~");
                BigDecimal power = power(split[0]);
                BigDecimal power1 = power(split[1]);
                //比较两个数的大小
                i = power.compareTo(power1);

            }

            if (i < 0){
                return true;
            }else {
                return false;
            }
        }
        else {
            return false;
        }



    }

    //判断compare后取出数据库中的数据判断是否有重复的，例如，数据库中有<0~10],用户输入0~5，或者8~11等等
    public static boolean compare3(String getValue,String inputValue){
        //getValue是从数据库中取得的数据;inputValue是用户输入的数据
        //1.先解析数据
        String pattern = "^\\[.*\\]$";
        String patterns = "^\\<.*\\]$";
        String pattern1 = "^\\<.*\\>$";
        String patterns1 = "^\\[.*\\>$";

        Pattern r = Pattern.compile(pattern);
        Pattern rs = Pattern.compile(patterns);
        Pattern r1 = Pattern.compile(pattern1);
        Pattern rs1 = Pattern.compile(patterns1);
//数据库中的
        Matcher im = r.matcher(getValue);
        Matcher ims = rs.matcher(getValue);
        Matcher im1 = r1.matcher(getValue);
        Matcher ims1 = rs1.matcher(getValue);

        //excel表中的
        Matcher m = r.matcher(inputValue);
        Matcher ms = rs.matcher(inputValue);
        Matcher m1 = r1.matcher(inputValue);
        Matcher ms1 = rs1.matcher(inputValue);
        boolean b = false;

        if (im.matches()){
            //去除两边的符号，并且根据~将字符串分割，获取数字
            //解析数据库中的数据
            String replace2 = getValue.replace("[", "");
            String replace3 = replace2.replace("]", "");
            String[] getValueStrings = replace3.split("~");
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2 != -1 && i3 != 1)||(i !=1 && i3 != -1)){

                    b=true;
//                    System.out.println(b);
                }else {

                    b=false;
//                    System.out.println(b);
                }




            }else if (ms.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));


                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){

                    if (i1==0){
                       b=false;
                    }else {
                        b= true;
                    }

                }else {
                    b= false;
                }

            }else if (m1.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i1==0 || i2 == 0){
                        b= false;
                    }else {
                       b= true;
                    }

                }else {
                    b= false;
                }

            }else if (ms1.matches()){
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0){
                        b= false;
                    }else {
                       b= true;
                    }

                }else {
                   b= false;
                }

            }
           


        }

        else if (ims.matches()){
            String replace2 = getValue.replace("<", "");
            String replace3 = replace2.replace("]", "");
            String[] getValueStrings = replace3.split("~");
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");
                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0){
                        b= false;
                    }else {
                        b=true;
                    }
                }else {
                    b= false;
                }



            }
            else if (ms.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){

                    if (i1==0 || i2==0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                    b=false;
                }

            }
            else if (m1.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i1==0 || i2 == 0){
                       b=false;
                    }else {
                       b=true;
                    }

                }else {
                    b=false;
                }

            }
            else if (ms1.matches()){
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                    b=false;
                }

            }

        }

        else if (im1.matches()){
            String replace2 = getValue.replace("<", "");
            String replace3 = replace2.replace("]", "");
            String[] getValueStrings = replace3.split("~");
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0 || i1==0){
                       b=false;
                    }else {
                        b=true;
                    }
                }else {
                   b=false;
                }



            }
            else if (ms.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){

                    if (i1==0 || i2==0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                   b=false;
                }

            }
            else if (m1.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i1==0 || i2 == 0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                   b=false;
                }

            }
            else if (ms1.matches()){
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0 || i1==0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                    b=false;
                }

            }




        }

        else if (ims1.matches()){
            String replace2 = getValue.replace("<", "");
            String replace3 = replace2.replace("]", "");
            String[] getValueStrings = replace3.split("~");
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i1==0){
                        b=false;
                    }else {
                       b=true;
                    }
                }else {
                    b=false;
                }



            }
            else if (ms.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){

                    if (i1==0){
                       b=false;
                    }else {
                        b=true;
                    }

                }else {
                   b=false;
                }

            }
            else if (m1.matches()){
                String replace = inputValue.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1

                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i1==0 || i2 == 0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                    b=false;
                }

            }else if (ms1.matches()){
                String replace = inputValue.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] inputValueStrings = replace1.split("~");

                BigDecimal c = new BigDecimal(String.valueOf(power(inputValueStrings[0])));
                BigDecimal d = new BigDecimal(String.valueOf(power(inputValueStrings[1])));
                BigDecimal e = new BigDecimal(String.valueOf(power(getValueStrings[0])));
                BigDecimal f = new BigDecimal(String.valueOf(power(getValueStrings[1])));

                int i = c.compareTo(e);//!=-1
                int i1 = c.compareTo(f);//i!=1
                int i2 = d.compareTo(e);//!=-1
                int i3 = d.compareTo(f);//i!=1


                if ((i != -1 && i1!=1)||(i2!=-1 && i3!=1)||(i!=1&& i3!=-1)){
                    if (i2==0 || i1==0){
                        b=false;
                    }else {
                        b=true;
                    }

                }else {
                    b=false;
                }

            }



        }

        return b;

    }



    //判断价格是否符合这个范围
    public static boolean compare4(String str, String number){


        if (str.length()>0){
            String pattern = "^\\[.*\\]$";
            String patterns = "^\\<.*\\]$";
            String pattern1 = "^\\<.*\\>$";
            String patterns1 = "^\\[.*\\>$";
            Pattern r = Pattern.compile(pattern);
            Pattern rs = Pattern.compile(patterns);
            Pattern r1 = Pattern.compile(pattern1);
            Pattern rs1 = Pattern.compile(patterns1);

            Matcher m = r.matcher(str);
            Matcher ms = rs.matcher(str);
            Matcher m1 = r1.matcher(str);
            Matcher ms1 = rs1.matcher(str);
            int i = 0;
            if (m.matches()){
                //去除两边的符号，并且根据~将字符串分割，获取数字
                String replace = str.replace("[", "");
                String replace1 = replace.replace("]", "");
                String[] split = replace1.split("~");
                if ((split[0].equals("∞") || split[1].equals("∞"))){
                    if (split[0].equals("∞") && !split[1].equals("∞")){
                        //负无穷~split[1]，检查数字是否小于等于split[1]
                        BigDecimal power1 = power(split[1]);
                        if (Double.parseDouble(number)<= Double.parseDouble(String.valueOf(power1))){
                            i=1;
                        }
                    }
                    else if (!split[0].equals("∞")){
                        //split[0]~正无穷,检查数字是否大于等于split[0]
                        BigDecimal power = power(split[0]);
                        if (Double.parseDouble(number)>= Double.parseDouble(String.valueOf(power))){
                            i=1;
                        }
                    }
                    else{
                        i=1;
                    }
                }
                else {
                    BigDecimal power = power(split[0]);
                    BigDecimal power1 = power(split[1]);
                    //比较两个数的大小
                    if ((Double.parseDouble(number)>= Double.parseDouble(String.valueOf(power))) && ((Double.parseDouble(number)<= Double.parseDouble(String.valueOf(power1))))){
                        i=1;
                    }
                }





            }else if (ms.matches()){
                String replace = str.replace("<", "");
                String replace1 = replace.replace("]", "");
                String[] split = replace1.split("~");
                if ((split[0].equals("∞") || split[1].equals("∞"))){
                    if (split[0].equals("∞") && !split[1].equals("∞")){
                        //负无穷~split[1]，检查数字是否小于等于split[1]
                        BigDecimal power1 = power(split[1]);
                        if (Double.parseDouble(number)<= Double.parseDouble(String.valueOf(power1))){
                            i=1;
                        }
                    }
                    else if (!split[0].equals("∞")){
                        //split[0]~正无穷,检查数字是否大于split[0]
                        BigDecimal power = power(split[0]);
                        if (Double.parseDouble(number)> Double.parseDouble(String.valueOf(power))){
                            i=1;
                        }
                    }
                    else{
                        i=1;
                    }
                }
                else{
                    BigDecimal power = power(split[0]);
                    BigDecimal power1 = power(split[1]);
                    //比较两个数的大小
                    if ((Double.parseDouble(number)> Double.parseDouble(String.valueOf(power))) && ((Double.parseDouble(number)<= Double.parseDouble(String.valueOf(power1))))){
                        i=1;
                    }

                }


            }else if (m1.matches()){
                String replace = str.replace("<", "");
                String replace1 = replace.replace(">", "");
                String[] split = replace1.split("~");
                if ((split[0].equals("∞") || split[1].equals("∞"))){
                    if (split[0].equals("∞") && !split[1].equals("∞")){
                        //负无穷~split[1]，检查数字是否小于split[1]
                        BigDecimal power1 = power(split[1]);
                        if (Double.parseDouble(number)< Double.parseDouble(String.valueOf(power1))){
                            i=1;
                        }
                    }
                    else if (!split[0].equals("∞")){
                        //split[0]~正无穷,检查数字是否大于split[0]
                        BigDecimal power = power(split[0]);

                        if (Double.parseDouble(number)> Double.parseDouble(String.valueOf(power))){
                            i=1;
                        }
                    }
                    else{
                        i=1;
                    }
                }
                else{
                    BigDecimal power = power(split[0]);
                    BigDecimal power1 = power(split[1]);
                    //比较两个数的大小
                    if ((Double.parseDouble(number)> Double.parseDouble(String.valueOf(power))) && ((Double.parseDouble(number)< Double.parseDouble(String.valueOf(power1))))){
                        i=1;
                    }
                }



            }else if (ms1.matches()){
                String replace = str.replace("[", "");
                String replace1 = replace.replace(">", "");
                String[] split = replace1.split("~");
                if ((split[0].equals("∞") || split[1].equals("∞"))){
                    if (split[0].equals("∞") && !split[1].equals("∞")){
                        //负无穷~split[1]，检查数字是否小于split[1]
                        BigDecimal power1 = power(split[1]);
                        if (Double.parseDouble(number)< Double.parseDouble(String.valueOf(power1))){
                            i=1;
                        }
                    }
                    else if (!split[0].equals("∞")){
                        //split[0]~正无穷,检查数字是否大于等于split[0]
                        BigDecimal power = power(split[0]);
                        if (Double.parseDouble(number)>= Double.parseDouble(String.valueOf(power))){
                            i=1;
                        }
                    }
                    else{
                        i=1;
                    }
                }
                else{
                    BigDecimal power = power(split[0]);
                    BigDecimal power1 = power(split[1]);
                    //比较两个数的大小
                    if ((Double.parseDouble(number)>= Double.parseDouble(String.valueOf(power))) && ((Double.parseDouble(number)< Double.parseDouble(String.valueOf(power1))))){
                        i=1;
                    }

                }


            }

            //符合条件
            return i > 0;
        }
        else {
            return false;
        }



    }



}




