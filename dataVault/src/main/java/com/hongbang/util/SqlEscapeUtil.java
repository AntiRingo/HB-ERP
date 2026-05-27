package com.hongbang.util;


public class SqlEscapeUtil {

    public static String escapeLike(String input) {
        return input == null ? null : input
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
