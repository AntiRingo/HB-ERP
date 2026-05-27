package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hongbang.pojo.Balance;
import com.hongbang.pojo.FormCk;
import com.hongbang.pojo.FormRk;
import com.hongbang.pojo.Product;
import com.hongbang.service.BalanceService;
import com.hongbang.service.FormCkService;
import com.hongbang.service.FormRkService;
import com.hongbang.service.ProductService;
import com.hongbang.service.impl.BalanceServiceImpl;
import com.hongbang.service.impl.FormCkServiceImpl;
import com.hongbang.service.impl.FormRkServiceImpl;
import com.hongbang.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/balance/*")
public class BalanceServlet extends BaseServlet {
//获取service
    BalanceService balanceService = new BalanceServiceImpl();


    //查询一段时间内的数据
    public void selectAllByTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String s = bufferedReader.readLine();
        Map map= JSON.parseObject(s, Map.class);
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        //调用service
        List<Map<String,Object>> balances = balanceService.selectAllByTime(startTime.toString(), endTime.toString());
        //转化为json数据
        String s1 = JSON.toJSONString(balances);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s1);

    }

    //根据时间段，productId，vault查询剩余量
    public void selectNumber (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        //调用service
        Balance balance = balanceService.selectNumber(Integer.parseInt(productId) , Integer.parseInt(vault), startTime, endTime);
        //转化为json数据
        String s = JSON.toJSONString(balance);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }


    //查询上期结存
    public void  selectLast(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的数据
        String productId = request.getParameter("productId");
        String vault = request.getParameter("vault");
        String startTime = request.getParameter("startTime");
        //调用service
        Balance balance = balanceService.selectLast(Integer.parseInt(productId), Integer.parseInt(vault), startTime);
        //转化为json数据
        String s = JSON.toJSONString(balance);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);

    }



    //测试使用，生成balance（昨日的结存）

    public void newBalance (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        FormRkService formRkService = new FormRkServiceImpl();
        FormCkService formCkService = new FormCkServiceImpl();
        ProductService productService = new ProductServiceImpl();
        BalanceService balanceService = new BalanceServiceImpl();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        //获取昨天的时间
        LocalDateTime yesterdayStart = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
        String formattedStart = yesterdayStart.format(formatter);

//        formattedStart = "2025-06-10";
//        formattedTime = "2025-06-11";
        balanceService.delete(formattedStart);
        //查询今天0点截止到昨天0点，这段时间内的所有的入库信息(入库数量，入库物料id，仓库，申请单id)
        List<Map<String, Object>> maps = formRkService.selectTodayRk(formattedStart, formattedTime);
        //查询今天0点截止到昨天0点，这段时间内的所有的出库信息
        List<Map<String, Object>> maps1 = formCkService.selectTodayCk(formattedStart, formattedTime);


        //创建新的balance，结存,入库
        List<Balance> balanceList = new ArrayList<>();//入库
        //查询今天的入库信息
        System.out.println("maps"+maps);
        for (int i = 0; i < maps.size(); i++) {
            //可能会存在多个相同的物料信息，这些要一起计算


            Map<String, Object> map = maps.get(i);
            System.out.println("出库出库"+map);
            Object product_id = map.get("product_id");//物料id
            Object vault = map.get("vault");
            Object number = map.get("number");//入库数量
            Object app_form_id = map.get("app_form_id");//入库申请单id
            Object actual_price = map.get("actual_price");//实时价格

            List<Product> products = productService.selectById((Integer) product_id);
            Product product = products.get(0);
            String number1 = product.getNumber();


            //计算今日收入、今日支出
            //查询最新一条该申请单，该物料的总出库情况
            List<FormCk> formCkList = formCkService.selectNewCkByFormId((Integer) product_id,(Integer) vault, (Integer) app_form_id);
            System.out.println("ckqqq"+formCkList);
            if (formCkList.size()>0){
                //存在出库信息，获取第一条ck_total
                FormCk formCk = formCkList.get(0);
//                System.out.println("出库"+formCk);
                double ckTotal = formCk.getCkTotal();//获取已经出库的总数了
                BigDecimal ckT= new BigDecimal(ckTotal);
                //查询是否存在发票
                List<FormRk> haveFp = formRkService.selectFpByTime((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);
                System.out.println("发票"+haveFp);
                //设置balance
                Balance balance = new Balance();
                balance.setProductId((Integer) product_id);
                balance.setVault((Integer) vault);
                balance.setTodayInNumber(number.toString());//入库数量
                balance.setTime(formattedStart);//昨天的时间
                if (haveFp.size()>0){
                    //存在发票信息，获取发票信息
                    FormRk formRk = haveFp.get(0);
                    //发票价格
                    double actualPrice = formRk.getActualPrice();
                    System.out.println("发票价格"+actualPrice);
                    //获取出库数量
                    double ckTotal1 = formCk.getCkTotal();
                    System.out.println("出库数量"+ckTotal1);
                    //计算今日收入：入库数量*发票价
                    BigDecimal numberFp = new BigDecimal(number.toString());
                    BigDecimal fpPrice = new BigDecimal(actualPrice);
                    BigDecimal multiply = numberFp.multiply(fpPrice);//今日收入
                    balance.setTodayIn(number.toString());
                    balance.setTodayIn(String.valueOf(multiply));
                    //计算今日支出
                    balance.setTodayOutNumber(String.valueOf(ckTotal1));
                    BigDecimal ckNumber = new BigDecimal(ckTotal1);
                    BigDecimal multiply1 = ckNumber.multiply(fpPrice);
                    balance.setTodayOut(String.valueOf(multiply1));
                    balance.setTodayNumber(number1);
                    //获取剩余数量
                    BigDecimal number2 = new BigDecimal(number.toString());
                    BigDecimal subtract = number2.subtract(ckT);
                    //计算今日结存，发票价格乘以剩余数量
                    BigDecimal multiply2 = fpPrice.multiply(subtract);
                    balance.setToday(multiply2.toString());
                    System.out.println("这里是发票数据"+balance);

                }
                else {
                    //不存在发票信息
                    //计算今日收入，今日收入为：  入库数量*价格
                    BigDecimal Price = new BigDecimal( actual_price.toString());
                    BigDecimal newNumber = new BigDecimal(number.toString());
                    BigDecimal multiply = Price.multiply(newNumber);//该物料的今日收入
                    balance.setTodayIn(String.valueOf(multiply));//今日收入
                    System.out.println("收入"+Price+"价格数量"+newNumber);

                    //计算今日支出
                    BigDecimal newCkTotal = new BigDecimal(ckTotal);
                    balance.setTodayOutNumber(String.valueOf(ckTotal));
                    BigDecimal multiply1 = newCkTotal.multiply(Price);
                    balance.setTodayOut(String.valueOf(multiply1));
                    balance.setTodayNumber(number1);
                    System.out.println("支出"+Price+"价格数量"+newCkTotal);

                    //计算今日结存，当前没有发票信息，今日结存为数量*价格
                    BigDecimal number2 = new BigDecimal(number.toString());
                    BigDecimal subtract = number2.subtract(ckT);
                    //价格乘以数量
                    BigDecimal multiply2 = subtract.multiply(Price);
                    balance.setToday(multiply2.toString());

                }




                balanceList.add(balance);
            }
            else {
                //不存在出库信息，入库后还未出库(因为没有出库情况，所以按照最新价格计算，有发票和没发票都是这样，这样就不用判断是否有没有发票了)
                //设置balance
                Balance balance = new Balance();
                balance.setProductId((Integer) product_id);
                balance.setVault((Integer) vault);
                balance.setTodayInNumber(number.toString());//入库数量
                balance.setTime(formattedStart);//入库时间
                //查询价格
                FormRk formRk = formRkService.selectNew(formattedStart, formattedTime, (Integer) product_id, (Integer) vault, (Integer) app_form_id);
                double actualPrice = formRk.getActualPrice();
                //计算今日收入，今日收入为：  入库数量*价格
                BigDecimal Price = new BigDecimal(actualPrice);
                BigDecimal newNumber = new BigDecimal(number.toString());
                BigDecimal multiply = Price.multiply(newNumber);//该物料的今日收入
                balance.setTodayIn(String.valueOf(multiply));//今日收入
                balance.setTodayNumber(number1);
                balance.setToday(multiply.toString());
                balance.setTodayOut("0");
                balance.setTodayOutNumber("0");
                balanceList.add(balance);

            }



        }



        //出库信息
        //查询今天的出库信息，今天入库的哪些的出库就不用管了，上面已经操作了
        for (int i = 0; i < maps1.size(); i++) {
            Map<String, Object> map = maps1.get(i);
            Object product_id = map.get("product_id");
            Object vault = map.get("vault");
            Object app_form_id = map.get("app_form_id");
            Object ck_number = map.get("ck_number");
            //根据appFormId,productId,vault,时间，去查询最新的一条ck_total
            FormCk formCk = formCkService.selectCkTotalBetween((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);
            double ck_total = formCk.getCkTotal();
//            Object ck_total = map.get("ck_total");



            List<Product> products = productService.selectById((Integer) product_id);
            Product product = products.get(0);
            String number1 = product.getNumber();
            boolean cz = false;
            for (int j = 0; j < maps.size(); j++) {
                Map<String, Object> map1 = maps.get(j);
                Object product_id1 = map1.get("product_id");
                Object vault1 = map1.get("vault");
                Object app_form_id1 = map1.get("app_form_id");
                if (product_id.equals(product_id1) && vault.equals(vault1) && app_form_id.equals(app_form_id1)){
                    cz = true;
                    break;
                }
            }
            if (!cz){
                //创建新的balance
                Balance balance = new Balance();
                //设置balance
                balance.setTodayIn(String.valueOf(0));
                balance.setTodayInNumber(String.valueOf(0));
                balance.setProductId( (Integer) product_id);
                balance.setVault((Integer) vault );
                balance.setTime(formattedStart);



                //不是今天入库的产品出库,计算今日出库总数以及今日支出
                double v = formCkService.selectCkSumByForm((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);//今日支出总数量
                balance.setTodayOutNumber(String.valueOf(v));
//查询在今天以前，不是发票的价格
                FormRk formRk1 = formRkService.selectNewPriceNoFp((Integer) product_id, (Integer) vault, (Integer) app_form_id, formattedStart);
                double newPriceNoFp = formRk1.getActualPrice();//今天以前的最新的价格（）
                //查询出库的数量


                //查询今天是否有发票存在
                List<FormRk> formRkList = formRkService.selectFpByTime((Integer) app_form_id, (Integer) product_id, (Integer) vault, formattedStart, formattedTime);
                System.out.println("检查这里"+formRkList);
                if (formRkList.size()>0){
                    //存在发票，查询最新的发票
                    FormRk formRk = formRkList.get(0);
                    //查询发票价格
                    double actualPrice = formRk.getActualPrice();//发票价格
                    //计算今日收入（发票价格*数量）-（入库价格*数量）
                    System.out.println(app_form_id);
                    System.out.println(product_id);
                    System.out.println(vault);
                    Map<String, Object> map1 = formRkService.selectByAppIdAndId((Integer) app_form_id, (Integer) product_id, (Integer) vault);
                    System.out.println(map1);
                    Object app_number = map1.get("app_number");//入库数量
                    BigDecimal oldPrice = new BigDecimal(newPriceNoFp);
                    System.out.println("oldPrice"+oldPrice);
                    System.out.println("newPrice"+actualPrice);
                    BigDecimal newPrice = new BigDecimal(actualPrice);
                    BigDecimal subtract = newPrice.subtract(oldPrice);
                    BigDecimal appNumber1 = new BigDecimal(app_number.toString());
                    BigDecimal multiply = subtract.multiply(appNumber1);//今日收入（计算差价）
                    balance.setTodayIn(String.valueOf(multiply));
                    //计算今日支出
                    BigDecimal ckNumber = new BigDecimal(ck_number.toString());//今日出库数量
                    BigDecimal multiply1 = ckNumber.multiply(oldPrice);//今日支出
                    //计算冲销
                    BigDecimal subtract1 = newPrice.subtract(oldPrice);//计算差价
                    BigDecimal ckTotal = new BigDecimal(ck_total);//出库总数
                    BigDecimal multiply2 = subtract1.multiply(ckTotal);
                    BigDecimal add = multiply2.add(multiply1);//冲销
                    System.out.println("出去"+add);
                    balance.setTodayOut(String.valueOf(add));

                    //计算今日结存（发票价格*剩余数量）
                    BigDecimal appNumber = new BigDecimal(String.valueOf(app_number));
                    BigDecimal subtract2 = appNumber.subtract(ckTotal);
                    BigDecimal multiply3 = subtract2.multiply(newPrice);
                    System.out.println("结存阿"+multiply3);
                    balance.setToday(String.valueOf(multiply3));
                    balance.setTodayNumber(number1);

                }
                else {
                    //不存在发票
                    //计算本日支出
                    BigDecimal number = new BigDecimal(v);
                    BigDecimal price = new BigDecimal(newPriceNoFp);
                    BigDecimal multiply = number.multiply(price);
                    //查询该申请单还剩下多少未出库.计算今日结存
                    Map<String, Object> map1 = formRkService.selectByAppIdAndId((Integer) app_form_id, (Integer) product_id, (Integer) vault);
                    Object app_number = map1.get("app_number");
                    BigDecimal appNumber = new BigDecimal(app_number.toString());
                    BigDecimal ckTotal = new BigDecimal(ck_total);
                    BigDecimal subtract = appNumber.subtract(ckTotal);
                    BigDecimal multiply1 = subtract.multiply(price);
                    System.out.println("结存为"+multiply1);
                    balance.setToday(String.valueOf(multiply1));
                    System.out.println("出去去"+multiply);
                    balance.setTodayOut(String.valueOf(multiply));
                    balance.setTodayNumber(number1);
                }

                balanceList.add(balance);

            }
        }


        //发票信息（今天没有出入库信息，但是今天来的发票）
        List<FormRk> formRkList = formRkService.selectFpToday(formattedStart, formattedTime);
        System.out.println("今天来的发票"+formRkList);
        for (int i = 0; i < formRkList.size(); i++) {
            FormRk formRk = formRkList.get(i);
            int FpAppFormId = formRk.getAppFormId();//发票申请单id
            int FpProductId = formRk.getProductId();//发票中的物料id
            int FpVault = formRk.getVault();//发票物料仓库
            String FpRkTime = formRk.getRkTime();//发票时间


            //查询是否出现在出库入库中，都不在才是今天来的发票，但是没有出入库
            boolean exist = false;
            for (int j = 0; j < maps.size(); j++) {
                Map<String, Object> map = maps.get(j);
                Object product_id = map.get("product_id");//物料id
                Object vault = map.get("vault");
                Object app_form_id = map.get("app_form_id");//入库申请单id

                if (product_id.equals(FpProductId) && vault.equals(FpVault) && app_form_id.equals(FpAppFormId)){
                    exist = true;
                    //存在该申请单，退出
                    break;
                }

            }

            if (!exist){
                //不存在，接着在出库中查询
                for (int j = 0; j < maps1.size(); j++) {
                    Map<String, Object> map = maps1.get(j);
                    Object product_id = map.get("product_id");
                    Object vault = map.get("vault");
                    Object app_form_id = map.get("app_form_id");
                    if (product_id.equals(FpProductId) && vault.equals(FpVault) && app_form_id.equals(FpAppFormId)){
                        //存在该申请单，退出
                        exist = true;
                        break;
                    }
                }

            }

            //都不存在，计算结存，查询该申请单的最新出库数量以及在这个时间之前的最新价格
            if (!exist){
                //获取发票中的价格
                double actualPrice = formRk.getActualPrice();
                //查询该申请单中该物料信息有多少个
                Map<String, Object> map = formRkService.selectByAppIdAndId(FpAppFormId, FpProductId, FpVault);
                Object app_number = map.get("app_number");//申请数量
                System.out.println("申请数量"+app_number);
                //查询小于发票时间的最新的出库情况，根据appFormId,productId,vault
                System.out.println(FpProductId);
                System.out.println(FpAppFormId);
                System.out.println(FpVault);
                List<FormCk> formCkList = formCkService.selectNewCkByFormId(FpProductId, FpVault, FpAppFormId);
                if (formCkList.size()>0){
                    //存在出库情况
                    System.out.println("小于发票时间的最新出库情况"+formCkList);
                    FormCk formCk = formCkList.get(0);
                    double ckTotal = formCk.getCkTotal();//获取已经出库的总数了
                    //获取小于今天时间的上一次入库的价格
                    FormRk formRk1 = formRkService.selectNewPriceSort(FpProductId, FpVault, FpAppFormId, formattedStart);
                    System.out.println(formRk1);
                    double actualPrice1 = formRk1.getActualPrice();//上次价格
                    System.out.println("上次价格"+actualPrice1);
                    BigDecimal appNumber = new BigDecimal(app_number.toString());
                    BigDecimal fpPrice = new BigDecimal(actualPrice);//发票价格
                    System.out.println("发票价格"+fpPrice);
                    BigDecimal lastPrice = new BigDecimal(actualPrice1);//上次价格
                    BigDecimal ckTotal1 = new BigDecimal(ckTotal);
                    //计算入库差价
                    BigDecimal subtract = fpPrice.subtract(lastPrice);
                    BigDecimal multiply = subtract.multiply(appNumber);//入库差价
                    //计算出库差价
                    BigDecimal multiply1 = subtract.multiply(ckTotal1);//出库差价
                    //新建balance
                    Balance balance = new Balance();
                    balance.setProductId(FpProductId);
                    balance.setVault(FpVault);
                    balance.setTodayOut(multiply1.toString());
                    balance.setTodayIn(multiply.toString());
                    balance.setTodayInNumber("0");
                    balance.setTodayOutNumber("0");
                    balance.setTime(formattedStart);
                    List<Product> products = productService.selectById(FpProductId);
                    Product product = products.get(0);
                    String number1 = product.getNumber();
                    //计算该申请单上该物料还剩多少
                    BigDecimal subtract1 = appNumber.subtract(ckTotal1);//剩余量
                    BigDecimal multiply2 = fpPrice.multiply(subtract1);//结存
                    System.out.println("结存2"+multiply2);
                    balance.setToday(multiply2.toString());
                    balance.setTodayNumber(number1);
                    System.out.println("看二看"+balance);
                    balanceList.add(balance);

                }
                else {
                    //从未出过库
                    //查询最新价格以及申请数量
                    Map<String, Object> map1 = formRkService.selectByAppIdAndId(FpAppFormId, FpProductId, FpVault);
                    System.out.println("这个是什么"+map1);
                    String app_number1 = map1.get("app_number").toString();
                    FormRk formRk1 = formRkService.selectNewPriceSort(FpProductId, FpVault, FpAppFormId, formattedStart);
                    System.out.println(formRk1);
                    double actual_price = formRk1.getActualPrice();//上次价格
//                    String actual_price = map1.get("actual_price").toString();
                    System.out.println("未出库上次价格"+actual_price);
                    //计算入库插价
                    BigDecimal lastPrice = new BigDecimal(actual_price);
                    BigDecimal fpPrice = new BigDecimal(actualPrice);
                    System.out.println("未出库发票价格"+fpPrice);
                    BigDecimal appN=new BigDecimal(app_number1);
                    BigDecimal subtract = fpPrice.subtract(lastPrice);
                    BigDecimal multiply = subtract.multiply(appN);//入库结存
                    //新建balance
                    Balance balance = new Balance();
                    balance.setProductId(FpProductId);
                    balance.setVault(FpVault);
                    balance.setTodayOut("0");
                    balance.setTodayIn(multiply.toString());
                    balance.setTodayInNumber("0");
                    balance.setTodayOutNumber("0");
                    balance.setTime(formattedStart);
                    BigDecimal multiply1 = fpPrice.multiply(appN);
                    balance.setToday(multiply1.toString());
                    List<Product> products = productService.selectById(FpProductId);
                    Product product = products.get(0);
                    String number1 = product.getNumber();
                    balance.setTodayNumber(number1);
                    System.out.println("看一看"+balance);
                    balanceList.add(balance);
                }


            }
        }


        //添加balanceList
        System.out.println("初始数据"+balanceList);

// 根据 productId + vault 分组
// 1. 分组
        Map<String, List<Balance>> groupedData = balanceList.stream()
                .collect(Collectors.groupingBy(
                        balance -> balance.getProductId() + "_" + balance.getVault()
                ));

        // 2. 转换为二维数组
        List<List<Balance>> groupedLists = new ArrayList<>(groupedData.values());
        Balance[][] result = new Balance[groupedLists.size()][];
        for (int i = 0; i < groupedLists.size(); i++) {
            List<Balance> group = groupedLists.get(i);
            result[i] = group.toArray(new Balance[0]);
        }

        // 3. 打印结果
        System.out.println("二维数组结构：");

        List<Balance> newBalanceList = new ArrayList<>();//入库
        for (Balance[] group : result) {
            System.out.println(Arrays.toString(group));
                Balance balance = new Balance();
                String s = JSON.toJSONString(group);
                List<Balance> balances = JSONArray.parseArray(s, Balance.class);
              BigDecimal todayIn = new BigDecimal(0);
              BigDecimal todayOut = new BigDecimal(0);
              BigDecimal today = new BigDecimal(0);
              BigDecimal todayInNumber = new BigDecimal(0);
              BigDecimal todayOutNumber = new BigDecimal(0);
                for (int j = 0; j < balances.size(); j++) {

                    Balance balance1 = balances.get(j);
                    balance.setProductId(balance1.getProductId());
                    balance.setVault(balance1.getVault());
                    balance.setTime(balance1.getTime());
                    balance.setTodayNumber(balance1.getTodayNumber());
                    BigDecimal todayIn1 = new BigDecimal(balance1.getTodayIn());
                    BigDecimal todayOut1 = new BigDecimal(balance1.getTodayOut());
                    BigDecimal today1 = new BigDecimal(balance1.getToday());
                    BigDecimal todayInNumber1 = new BigDecimal(balance1.getTodayInNumber());
                    BigDecimal todayOutNumber1 = new BigDecimal(balance1.getTodayOutNumber());
                    todayIn = todayIn.add(todayIn1);
                    todayOut = todayOut.add(todayOut1);
                    today = today.add(today1);
                    todayInNumber = todayInNumber.add(todayInNumber1);
                    todayOutNumber = todayOutNumber.add(todayOutNumber1);




                }
                balance.setTodayIn(todayIn.toString());
                balance.setTodayOut(todayOut.toString());
                balance.setToday(today.toString());
                balance.setTodayInNumber(todayInNumber.toString());
                balance.setTodayOutNumber(todayOutNumber.toString());
                newBalanceList.add(balance);

        }

//        for (int i = 0; i < newBalanceList.size(); i++) {
//
//            Balance balance = newBalanceList.get(i);
//            System.out.println("balance"+balance);
//            int productId = balance.getProductId();
//            int vault = balance.getVault();
//
////查询该物料信息最新的form_ck的信息，查到最新的出库信息，然后查询从哪个入库申请单中出的库，然后查询比这个更晚入库的所有申请单，然后计算结存
//            List<FormCk> formCkList = formCkService.selectNewCkById(productId, vault);
//            System.out.println("注意这里"+formCkList);
//            BigDecimal jieCun = new BigDecimal(0);
//            if (formCkList.size()>0){
//                //存在出库信息
//                FormCk formCk = formCkList.get(0);
//
//                int appFormId = formCk.getAppFormId();
//
//                //查询这个申请单的入库时间，然后去查询所有的入库时间大于等于这个申请单并且物料id是product的
//                //获取大于这个时间的该物料的数据，计算所有申请单中该物料信息的结存(价格从入库里面去查询)
//                System.out.println(productId);
//                System.out.println(appFormId);
//                List<Map<String, Object>> maps2 = formRkService.selectFormRkAndAppNumberByTime(productId, vault, appFormId);
//                System.out.println("这里吗"+maps2);
//                //得到了入库数据，申请数量一类的,计算所有的结存
//                for (int j = 0; j < maps2.size(); j++) {
//                    //获取数据在各个申请单中的数量，然后去查询该申请单中该物料的最新价格，包含发票价
//                    Map<String, Object> map = maps2.get(j);
//                    //获取申请数量
//                    String app_number = map.get("app_number").toString();
//                    //获取申请单id
//                    String app_form_id = map.get("app_form_id").toString();
//                    //查询该申请单中该物料信息的最新价格(包含发票价)
//                    double price = formRkService.selectPrice(Integer.parseInt(app_form_id), productId, vault);
//                    BigDecimal appN=new BigDecimal(app_number);
//                    BigDecimal newPrice = new BigDecimal(price);
//                    //查询这个申请单中总出库数量（只需要在form_ck中查询最新的即可）
//                    List<FormCk> formCkList1 = formCkService.selectNewCkByFormId(productId,vault, Integer.parseInt(app_form_id));
//                    if (formCkList1.size()>0){
//                        FormCk formCk1 = formCkList1.get(0);
//                        System.out.println("再看这里"+formCk1);
//                        double ckTotal = formCk1.getCkTotal();
//                        //计算该申请单还剩多少
//
//                        BigDecimal ckT = new BigDecimal(ckTotal);
//                        BigDecimal subtract = appN.subtract(ckT);//剩余数量
//                        System.out.println("数量"+subtract);
//                        //计算结存
//
//                        System.out.println("价格"+newPrice);
//                        BigDecimal multiply = newPrice.multiply(subtract);
//                        System.out.println(multiply);
//                        jieCun = jieCun.add(multiply);
//                    }
//                    else {
//                        //存在出库的和为未出库的
//                        //计算结存
//                        BigDecimal multiply = appN.multiply(newPrice);
//                        jieCun = jieCun.add(multiply);
//                    }
//
//
//
//                }
//            }
//            else {
//                //不存在出库信息，那就去查所有的申请单以及金额计算结存
//                //根据物料id查询所有的入库信息，然后再查询价格
//                List<Map<String, Object>> maps2 = formRkService.selectByProductId(productId);
//                //获取没个申请单的id和物料id
//                for (int j = 0; j < maps2.size(); j++) {
//                    Map<String, Object> map = maps2.get(j);
//                   String app_form_id = map.get("app_form_id").toString();
//                    String app_number = map.get("app_number").toString();
//                    //根据app_form_id和productId查询该申请单中的该物料的最新价格
//                    double price = formRkService.selectPrice(Integer.parseInt(app_form_id), productId, vault);
//
//                    //计算结存
//                    BigDecimal appN=new BigDecimal(app_number);
//                    BigDecimal newPrice = new BigDecimal(price);
//                    BigDecimal multiply = appN.multiply(newPrice);
//                    jieCun = jieCun.add(multiply);
//                }
//
//
//            }
//            System.out.println("结存"+jieCun);
//            balance.setToday(jieCun.toString());
//
//        }

//        重写计算结存

        //重新计算结存
        for (int i = 0; i < newBalanceList.size(); i++) {
            Balance balance = newBalanceList.get(i);
            int productId = balance.getProductId();
            int vault = balance.getVault();
            System.out.println("eeee"+productId);
//新建结存为0
            BigDecimal jieCun = new BigDecimal(0);
            //查询最新数据
            List<Map<String, Object>> maps2 = balanceService.selectNewPriceAndNumber(productId, vault);
            System.out.println("rrrr"+maps2);
            //获取最新价格
            for (int j = 0; j < maps2.size(); j++) {
                Map<String, Object> map = maps2.get(j);

                //获取该入库申请单的总量以及已经从该申请单出库的数量
                String app_number = map.get("app_number").toString();
                Object ck_total = map.get("ck_total");
                //获取最新价格
                Object app_price = map.get("actual_price");
                BigDecimal appPrice = new BigDecimal(app_price.toString());
                BigDecimal appNumberRk = new BigDecimal(app_number);
                BigDecimal ckTotalRk;
                //这里ckTotal可能为null，如果未null就表示该入库申请单还未被出库
                if (ck_total==null){
                    ckTotalRk = new BigDecimal(0);
                }
                else {
                    ckTotalRk = new BigDecimal(ck_total.toString());
                }
                //计算该申请单还剩多少可以出库的数量
                BigDecimal canCkNumber = appNumberRk.subtract(ckTotalRk);
                //计算结存
                BigDecimal multiply = canCkNumber.multiply(appPrice);
                jieCun = jieCun.add(multiply);
            }
            balance.setToday(jieCun.toString());
        }

        System.out.println("哈哈哈"+newBalanceList);
        if (newBalanceList.size()>0){
            balanceService.addBalance(newBalanceList);
        }



        response.getWriter().write("success");


    }

}
