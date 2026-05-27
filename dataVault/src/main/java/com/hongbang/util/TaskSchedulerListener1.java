package com.hongbang.util;

import com.hongbang.service.BalanceService;
import com.hongbang.service.FormCkService;
import com.hongbang.service.FormRkService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.BalanceServiceImpl;
import com.hongbang.service.impl.FormCkServiceImpl;
import com.hongbang.service.impl.FormRkServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class TaskSchedulerListener1 implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai"); // 指定时区

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(3);
        scheduleDailyTask();
//        scheduleWeeklyTask();
//        scheduleMonthlyTask();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow(); // 关闭线程池
    }

    // ------------------------- 每日任务（0点触发） -------------------------
    private void scheduleDailyTask() {
        long initialDelay = getDelayToNextMidnight();
        scheduler.scheduleAtFixedRate(() -> {
            LocalDate yesterday = LocalDate.now(ZONE).minusDays(1);
            LocalDateTime yesterdayMidnight = yesterday.atStartOfDay();
            executeLogic("每日任务", yesterdayMidnight);
        }, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
    }

    // ------------------------- 每周任务（周一0点触发） -------------------------
//    private void scheduleWeeklyTask() {
//        long initialDelay = getDelayToNextMondayMidnight();
//        scheduler.scheduleAtFixedRate(() -> {
//            LocalDate lastMonday = LocalDate.now(ZONE)
//                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
//                    .minusWeeks(1);
//            LocalDateTime lastMondayMidnight = lastMonday.atStartOfDay();
//            executeLogic("每周任务", lastMondayMidnight);
//        }, initialDelay, 7 * 24 * 60 * 60, TimeUnit.SECONDS);
//    }

    // ------------------------- 每月任务（1号0点触发） -------------------------
//    private void scheduleMonthlyTask() {
//        long initialDelay = getDelayToNextMonthFirstDay();
//        scheduler.scheduleAtFixedRate(() -> {
//            LocalDate firstDayOfLastMonth = LocalDate.now(ZONE)
//                    .withDayOfMonth(1)
//                    .minusMonths(1);
//            LocalDateTime firstDayOfLastMonthMidnight = firstDayOfLastMonth.atStartOfDay();
//            executeLogic("每月任务", firstDayOfLastMonthMidnight);
//        }, initialDelay, getMonthlyInterval(), TimeUnit.SECONDS);
//    }

    // ------------------------- 工具方法 -------------------------
    FormRkService formRkService = new FormRkServiceImpl();
    FormCkService formCkService = new FormCkServiceImpl();
    ProductService productService = new ProductServiceImpl();
    BalanceService balanceService = new BalanceServiceImpl();
    private void executeLogic(String taskName, LocalDateTime time) {
        //注意；时间小于今日结存的上一条中最新的今日结存就是上期结存
        System.out.printf("[%s] 触发时间: %s\n", taskName, time);
        // 在此调用你的业务逻辑
//        if (taskName.equals("每日任务")){
//            //获取服务器时间
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String formattedTime = LocalDateTime.now().format(formatter);
//            //获取昨天的时间
//            LocalDateTime yesterdayStart = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
//            String formattedStart = yesterdayStart.format(formatter);
//
//            formattedStart = "2025-05-16";
//            formattedTime = "2025-05-17";
//            //查询今天0点截止到昨天0点，这段时间内的所有的入库信息(入库数量，入库物料id，仓库，申请单id)
//            List<Map<String, Object>> maps = formRkService.selectTodayRk(formattedStart, formattedTime);
//            //查询今天0点截止到昨天0点，这段时间内的所有的出库信息
//            List<Map<String, Object>> maps1 = formCkService.selectTodayCk(formattedStart, formattedTime);
//
//
//            //创建新的balance，结存,入库
//            List<Balance> balanceList = new ArrayList<>();//入库
//            //查询今天的入库信息
//
//            for (int i = 0; i < maps.size(); i++) {
//                //可能会存在多个相同的物料信息，这些要一起计算
//
//
//                Map<String, Object> map = maps.get(i);
//                Object product_id = map.get("product_id");//物料id
//                Object vault = map.get("vault");
//                Object number = map.get("number");//入库数量
//                Object app_form_id = map.get("app_form_id");//入库申请单id
//                Object actual_price = map.get("actual_price");//实时价格
//
//                List<Product> products = productService.selectById((Integer) product_id);
//                Product product = products.get(0);
//                String number1 = product.getNumber();
//
//
//                //计算今日收入、今日支出
//                //查询最新一条该申请单，该物料的总出库情况
//                List<FormCk> formCkList = formCkService.selectNewCkByFormId((Integer) product_id,(Integer) vault, (Integer) app_form_id);
//                if (formCkList.size()>0){
//                    //存在出库信息，获取第一条ck_total
//                    FormCk formCk = formCkList.get(0);
//                    double ckTotal = formCk.getCkTotal();//获取已经出库的总数了
//                    //查询是否存在发票
//                    List<FormRk> haveFp = formRkService.selectFpByTime((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);
//                    //设置balance
//                    Balance balance = new Balance();
//                    balance.setProductId((Integer) product_id);
//                    balance.setVault((Integer) vault);
//                    balance.setTodayInNumber((String) number);//入库数量
//                    balance.setTime((String) map.get("rk_time"));//入库时间
//                    if (haveFp.size()>0){
//                        //存在发票信息，获取发票信息
//                        FormRk formRk = haveFp.get(0);
//                        //发票价格
//                        double actualPrice = formRk.getActualPrice();
//                        //获取出库数量
//                        double ckTotal1 = formCk.getCkTotal();
//                        //计算今日收入：入库数量*发票价
//                        BigDecimal numberFp = new BigDecimal((String) number);
//                        BigDecimal fpPrice = new BigDecimal(actualPrice);
//                        BigDecimal multiply = numberFp.multiply(fpPrice);//今日收入
//                        balance.setTodayIn((String) number);
//                        balance.setTodayIn(String.valueOf(multiply));
//                        //计算今日支出
//                        balance.setTodayOutNumber(String.valueOf(ckTotal1));
//                        BigDecimal ckNumber = new BigDecimal(ckTotal1);
//                        BigDecimal multiply1 = ckNumber.multiply(fpPrice);
//                        balance.setTodayOut(String.valueOf(multiply1));
//                        balance.setTodayNumber(number1);
//
//
//                    }
//                    else {
//                        //不存在发票信息
//                        //计算今日收入，今日收入为：  入库数量*价格
//                        BigDecimal Price = new BigDecimal((char[]) actual_price);
//                        BigDecimal newNumber = new BigDecimal((String) number);
//                        BigDecimal multiply = Price.multiply(newNumber);//该物料的今日收入
//                        balance.setTodayIn(String.valueOf(multiply));//今日收入
//
//                        //计算今日支出
//                        BigDecimal newCkTotal = new BigDecimal(ckTotal);
//                        balance.setTodayOutNumber(String.valueOf(ckTotal));
//                        BigDecimal multiply1 = newCkTotal.multiply(Price);
//                        balance.setTodayOut(String.valueOf(multiply1));
//                        balance.setTodayNumber(number1);
//                    }
//
//
//
//
//                    balanceList.add(balance);
//                }
//                else {
//                    //不存在出库信息，入库后还未出库(因为没有出库情况，所以按照最新价格计算，有发票和没发票都是这样，这样就不用判断是否有没有发票了)
//                    //设置balance
//                    Balance balance = new Balance();
//                    balance.setProductId((Integer) product_id);
//                    balance.setVault((Integer) vault);
//                    balance.setTodayInNumber((String) number);//入库数量
//                    balance.setTime((String) map.get("rk_time"));//入库时间
//                    //查询价格
//                    FormRk formRk = formRkService.selectNew(formattedStart, formattedTime, (Integer) product_id, (Integer) vault, (Integer) app_form_id);
//                    double actualPrice = formRk.getActualPrice();
//                    //计算今日收入，今日收入为：  入库数量*价格
//                    BigDecimal Price = new BigDecimal(actualPrice);
//                    BigDecimal newNumber = new BigDecimal((String) number);
//                    BigDecimal multiply = Price.multiply(newNumber);//该物料的今日收入
//                    balance.setTodayIn(String.valueOf(multiply));//今日收入
//                    balance.setTodayNumber(number1);
//                    balanceList.add(balance);
//
//                }
//
//
//
//            }
//
//
//
//            //出库信息
//            //查询今天的出库信息，今天入库的哪些的出库就不用管了，上面已经操作了
//            for (int i = 0; i < maps1.size(); i++) {
//                Map<String, Object> map = maps1.get(i);
//                Object product_id = map.get("product_id");
//                Object vault = map.get("vault");
//                Object app_form_id = map.get("app_form_id");
//                Object ck_total = map.get("ck_total");
//                Object ck_number = map.get("ck_number");
//
//                List<Product> products = productService.selectById((Integer) product_id);
//                Product product = products.get(0);
//                String number1 = product.getNumber();
//                boolean cz = false;
//                for (int j = 0; j < maps.size(); j++) {
//                    Map<String, Object> map1 = maps.get(j);
//                    Object product_id1 = map1.get("product_id");
//                    Object vault1 = map1.get("vault");
//                    Object app_form_id1 = map1.get("app_form_id");
//                    if (product_id.equals(product_id1) && vault.equals(vault1) && app_form_id.equals(app_form_id1)){
//                        cz = true;
//                        break;
//                    }
//                }
//                if (!cz){
//                    //创建新的balance
//                    Balance balance = new Balance();
//                    //设置balance
//                    balance.setTodayIn(String.valueOf(0));
//                    balance.setTodayInNumber(String.valueOf(0));
//                    balance.setProductId( (Integer) product_id);
//                    balance.setVault((Integer) vault );
//                    balance.setTime(formattedTime);
//
//
//
//                    //不是今天入库的产品出库,计算今日出库总数以及今日支出
//                    double v = formCkService.selectCkSumByForm((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);//今日支出总数量
//                    balance.setTodayOutNumber(String.valueOf(v));
////查询在今天以前，不是发票的价格
//                    FormRk formRk1 = formRkService.selectNewPriceNoFp((Integer) product_id, (Integer) vault, (Integer) app_form_id, formattedTime);
//                    double newPriceNoFp = formRk1.getActualPrice();//不是发票的最新价格
//                    //查询出库的数量
//
//
//                    //查询今天是否有发票存在
//                    List<FormRk> formRkList = formRkService.selectFpByTime((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);
//                    if (formRkList.size()>0){
//                        //存在发票，查询最新的发票
//                        FormRk formRk = formRkList.get(0);
//                        //查询发票价格
//                        double actualPrice = formRk.getActualPrice();//发票价格
//                        //计算今日收入（发票价格*数量）-（入库价格*数量）
//                        Map<String, Object> map1 = formRkService.selectByAppIdAndId((Integer) app_form_id, (Integer) product_id, (Integer) vault);
//                        Object app_number = map1.get("app_number");//入库数量
//                        BigDecimal oldPrice = new BigDecimal(newPriceNoFp);
//                        BigDecimal newPrice = new BigDecimal(actualPrice);
//                        BigDecimal subtract = newPrice.subtract(oldPrice);
//                        BigDecimal multiply = subtract.multiply((BigDecimal) app_number);//今日收入（计算差价）
//                        balance.setTodayIn(String.valueOf(multiply));
//                        //计算今日支出
//                        BigDecimal ckNumber = new BigDecimal((char[]) ck_number);//今日出库数量
//                        BigDecimal multiply1 = ckNumber.multiply(oldPrice);//今日支出
//                        //计算冲销
//                        BigDecimal subtract1 = newPrice.subtract(oldPrice);//计算差价
//                        BigDecimal ckTotal = new BigDecimal((char[]) ck_total);//出库总数
//                        BigDecimal multiply2 = subtract1.multiply(ckTotal);
//                        BigDecimal add = multiply2.add(multiply1);//冲销
//                        balance.setTodayOut(String.valueOf(add));
//
//                        //计算今日结存（发票价格*剩余数量）
//                        BigDecimal appNumber = new BigDecimal(String.valueOf(app_number));
//                        BigDecimal subtract2 = appNumber.subtract(ckTotal);
//                        BigDecimal multiply3 = subtract2.multiply(newPrice);
//                        balance.setToday(String.valueOf(multiply3));
//                        balance.setTodayNumber(number1);
//
//                    }
//                    else {
//                        //不存在发票
//                        //计算本日支出
//                        BigDecimal number = new BigDecimal(v);
//                        BigDecimal price = new BigDecimal(newPriceNoFp);
//                        BigDecimal multiply = number.multiply(price);
//                        //查询该申请单还剩下多少未出库.计算今日结存
//                        Map<String, Object> map1 = formRkService.selectByAppIdAndId((Integer) app_form_id, (Integer) product_id, (Integer) vault);
//                        Object app_number = map1.get("app_number");
//                        BigDecimal appNumber = new BigDecimal((char[]) app_number);
//                        BigDecimal ckTotal = new BigDecimal((char[]) ck_total);
//                        BigDecimal subtract = appNumber.subtract(ckTotal);
//                        BigDecimal multiply1 = subtract.multiply(price);
//                        balance.setToday(String.valueOf(multiply1));
//
//                        balance.setTodayOut(String.valueOf(multiply));
//                        balance.setTodayNumber(number1);
//                    }
//
//                    balanceList.add(balance);
//
//                }
//            }
//
//
//            //发票信息（今天没有出入库信息，但是今天来的发票）
//            List<FormRk> formRkList = formRkService.selectFpToday(formattedStart, formattedTime);
//            for (int i = 0; i < formRkList.size(); i++) {
//                FormRk formRk = formRkList.get(i);
//                int FpAppFormId = formRk.getAppFormId();//发票申请单id
//                int FpProductId = formRk.getProductId();//发票中的物料id
//                int FpVault = formRk.getVault();//发票物料仓库
//                String FpRkTime = formRk.getRkTime();//发票时间
//
//                boolean exist = false;
//                for (int j = 0; j < maps.size(); j++) {
//                    Map<String, Object> map = maps.get(j);
//                    Object product_id = map.get("product_id");//物料id
//                    Object vault = map.get("vault");
//                    Object app_form_id = map.get("app_form_id");//入库申请单id
//
//                    if (product_id.equals(FpProductId) && vault.equals(FpVault) && app_form_id.equals(FpAppFormId)){
//                        exist = true;
//                        //存在该申请单，退出
//                        break;
//                    }
//
//                }
//
//                if (!exist){
//                    //不存在，接着在出库中查询
//                    Map<String, Object> map = maps1.get(i);
//                    Object product_id = map.get("product_id");
//                    Object vault = map.get("vault");
//                    Object app_form_id = map.get("app_form_id");
//                    if (product_id.equals(FpProductId) && vault.equals(FpVault) && app_form_id.equals(FpAppFormId)){
//                        exist = true;
//                        //存在该申请单，退出
//                        break;
//                    }
//                }
//
//                //都不存在，计算结存，查询该申请单的最新出库数量以及在这个时间之前的最新价格
//                if (!exist){
//                    //获取发票中的价格
//                    double actualPrice = formRk.getActualPrice();
//                    //查询该申请单中该物料信息有多少个
//                    Map<String, Object> map = formRkService.selectByAppIdAndId(FpAppFormId, FpProductId, FpVault);
//                    Object app_number = map.get("app_number");//申请数量
//                    //查询小于发票时间的最新的出库情况，根据appFormId,productId,vault
//                    List<FormCk> formCkList = formCkService.selectNewCkByFormId(FpProductId, FpVault, FpAppFormId);
//                    FormCk formCk = formCkList.get(0);
//                    double ckTotal = formCk.getCkTotal();//获取已经出库的总数了
//                   //获取小于发票时间的上一次入库的价格
//                    FormRk formRk1 = formRkService.selectNewPriceSort(FpProductId, FpVault, FpAppFormId, FpRkTime);
//                    double actualPrice1 = formRk1.getActualPrice();//上次价格
//                    BigDecimal appNumber = new BigDecimal(app_number.toString());
//                    BigDecimal fpPrice = new BigDecimal(actualPrice);//发票价格
//                    BigDecimal lastPrice = new BigDecimal(actualPrice1);//上次价格
//                    BigDecimal ckTotal1 = new BigDecimal(ckTotal);
//                    //计算入库差价
//                    BigDecimal subtract = fpPrice.subtract(lastPrice);
//                    BigDecimal multiply = subtract.multiply(appNumber);//入库差价
//                    //计算出库差价
//                    BigDecimal multiply1 = subtract.multiply(ckTotal1);//出库差价
//                    //新建balance
//                    Balance balance = new Balance();
//                    balance.setProductId(FpProductId);
//                    balance.setVault(FpVault);
//                    balance.setTodayOut(multiply1.toString());
//                    balance.setTodayIn(multiply.toString());
//                    balance.setTodayIn("0");
//                    balance.setTodayOut("0");
//                   //计算该申请单上该物料还剩多少
//                    BigDecimal subtract1 = appNumber.subtract(ckTotal1);//剩余量
//                    BigDecimal multiply2 = fpPrice.multiply(subtract1);//结存
//                    balance.setToday(multiply2.toString());
//                    balance.setTodayNumber(subtract1.toString());
//                    balanceList.add(balance);
//
//                }
//            }
//
//
//            //添加balanceList
//            balanceService.addBalance(balanceList);
//
//        }


    }

    // 计算到下一个0点的秒数
    private long getDelayToNextMidnight() {
        LocalDateTime now = LocalDateTime.now(ZONE);
        LocalDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, nextMidnight).getSeconds();
    }

//    // 计算到下一个周一0点的秒数
//    private long getDelayToNextMondayMidnight() {
//        LocalDateTime now = LocalDateTime.now(ZONE);
//        LocalDateTime nextMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
//                .toLocalDate().atStartOfDay();
//        return Duration.between(now, nextMonday).getSeconds();
//    }
//
//    // 计算到下个月1号0点的秒数
//    private long getDelayToNextMonthFirstDay() {
//        LocalDateTime now = LocalDateTime.now(ZONE);
//        LocalDateTime nextMonthFirstDay = LocalDate.now(ZONE)
//                .with(TemporalAdjusters.firstDayOfNextMonth())
//                .atStartOfDay();
//        return Duration.between(now, nextMonthFirstDay).getSeconds();
//    }
//
//    // 动态计算每月间隔（避免固定30天误差）
//    private long getMonthlyInterval() {
//        LocalDateTime nextMonthFirstDay = LocalDate.now(ZONE)
//                .with(TemporalAdjusters.firstDayOfNextMonth())
//                .atStartOfDay();
//        LocalDateTime nextNextMonthFirstDay = nextMonthFirstDay.toLocalDate()
//                .plusMonths(1)
//                .atStartOfDay();
//        return Duration.between(nextMonthFirstDay, nextNextMonthFirstDay).getSeconds();
//    }
}