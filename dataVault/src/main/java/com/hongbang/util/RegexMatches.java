package com.hongbang.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexMatches {

    public static boolean main(String str) {

        String pattern = "^(<|\\[)[-]?[0-9]+[.]?[0-9]*[fpnum%KMGT]?[~][-]?[0-9]+[.]?[0-9]*[fpnum%KMGT]?(>|])$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

//    //带单位的查询
//    public static String unit(String str) {
//        String pattern = "^(([-]?[0-9]+[.][0-9]+)|([0-9]*))[fpnum%KMGT]?[VAW]?(ch)?(Khz)?(bit)?(KS/s)?(PPM)?$";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(str);
//
//        if (m.find()) {
//            //            group(1)只展示被匹配到的内容
//
//            return m.group(1);
//        } else {
//
//            return null;
//        }
//
//    }

    public static boolean isPositiveInteger(String str) {
        // 正则表达式匹配非负整数，包括0
        String regex = "^\\d+$";
        if (str.matches(regex)) {
            // 使用 Integer.parseInt() 尝试转换字符串，如果成功且结果是非负整数，则说明是正整数
            try {
                int num = Integer.parseInt(str);
                return num > 0;
            } catch (NumberFormatException e) {
                // NumberFormatException 捕获到时，说明不是正整数
                return false;
            }
        } else {
            return false;
        }
    }

}