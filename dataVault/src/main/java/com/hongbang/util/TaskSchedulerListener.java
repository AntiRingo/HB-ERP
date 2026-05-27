package com.hongbang.util;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class TaskSchedulerListener implements ServletContextListener {
    private Timer dailyTimer;
    private Timer weeklyTimer;
    private Timer monthlyTimer;
    private Timer yearlyTimer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 初始化定时器
        dailyTimer = new Timer("DailyTaskTimer", true);
        weeklyTimer = new Timer("WeeklyTaskTimer", true);
        monthlyTimer = new Timer("MonthlyTaskTimer", true);
        yearlyTimer = new Timer("YearlyTaskTimer", true);

        // 启动任务
        scheduleDailyTask();
        scheduleWeeklyTask();
        scheduleMonthlyTask();
        scheduleYearlyTask();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 销毁定时器
        dailyTimer.cancel();
        weeklyTimer.cancel();
        monthlyTimer.cancel();
        yearlyTimer.cancel();
    }

    //--- 核心调度方法 ---//

    private void scheduleDailyTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); // 明天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 每天执行一次（24小时间隔）
        dailyTimer.scheduleAtFixedRate(new DailyTask(), calendar.getTime(), 24 * 60 * 60 * 1000);
    }

    private void scheduleWeeklyTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 下周一
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 每周执行一次（7天间隔）
        weeklyTimer.scheduleAtFixedRate(new WeeklyTask(), calendar.getTime(), 7 * 24 * 60 * 60 * 1000);
    }

    private void scheduleMonthlyTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1); // 下个月1号
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 每月执行一次（按实际月份天数间隔）
        monthlyTimer.scheduleAtFixedRate(new MonthlyTask(), calendar.getTime(), getNextMonthInterval());
    }

    private void scheduleYearlyTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1); // 明年1月1日
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // 每年执行一次（365天间隔）
        yearlyTimer.scheduleAtFixedRate(new YearlyTask(), calendar.getTime(), 365L * 24 * 60 * 60 * 1000);
    }

    // 计算下个月的间隔天数
    private long getNextMonthInterval() {
        Calendar calendar = Calendar.getInstance();
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay * 24 * 60 * 60 * 1000L;
    }


    public static class DailyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("[Daily] 任务执行: " + new Date());
            // 业务代码
            String yesterdayStar = DateTimeUtils.getYesterdayStar();
        }
    }

    public static class WeeklyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("[Weekly] 任务执行: " + new Date());
            // 业务代码
            String lastMondayStart = DateTimeUtils.getLastMondayStart();
            String now = DateTimeUtils.getNow();
            //查询这段时间内所有的该物料的最新的出货信息


            //查询这段时间内所有的最新的发票信息
        }
    }

    public static class MonthlyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("[Monthly] 任务执行: " + new Date());
            // 业务代码
            String lastMondayStart = DateTimeUtils.getLastMondayStart();
        }
    }

    public static class YearlyTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("[Yearly] 任务执行: " + new Date());
            // 业务代码

        }
    }
}