package com.hongbang.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateTimeUtils {

    // 获取当前时间（格式：yyyy-MM-dd HH:mm:ss）
    public static String getNow() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取昨天第一秒（23:59:59）
    public static String getYesterdayStar() {
        return LocalDateTime.now()
                .minusDays(1)
                .with(LocalTime.MIN) // 23:59:59.999999999
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取上周一00:00:00
    public static String getLastMondayStart() {
        return LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1)
                .with(LocalTime.MIN) // 00:00:00
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取上月1号00:00:00
    public static String getLastMonthFirstDay() {
        return LocalDateTime.now()
                .minusMonths(1)
                .withDayOfMonth(1)
                .with(LocalTime.MIN)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
