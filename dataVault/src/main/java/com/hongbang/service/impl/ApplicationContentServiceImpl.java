package com.hongbang.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.mapper.*;
import com.hongbang.pojo.*;
import com.hongbang.service.ApplicationContentService;
import com.hongbang.service.InspectionService;
import com.hongbang.service.RelationshipService;
import com.hongbang.util.Compare;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ApplicationContentServiceImpl implements ApplicationContentService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //获取service
    RelationshipService relationshipService = new RelationshipServiceImpl();
    InspectionService inspectionService = new InspectionServiceImpl();
    //批量添加所有数据
    @Override
    public void addAll(ApplicationForm applicationForm,List<ApplicationContent>applicationContents,List<JSONArray>jsonArrays){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        UserMapper mapper5 = sqlSession.getMapper(UserMapper.class);
        ProductMapper mapper7 = sqlSession.getMapper(ProductMapper.class);
        InvoiceMapper mapper8 = sqlSession.getMapper(InvoiceMapper.class);
        TakeOrderMapper mapper9 = sqlSession.getMapper(TakeOrderMapper.class);


        //查询申请人的部门
        User user = mapper5.selectById(applicationForm.getUserId());
        int department = user.getDepartment();
        int level = user.getLevel();

        applicationForm.setCirculation(department);
//        System.out.println("部门"+department);
        //添加申请单



//        int sortTwo1 = applicationForm.getSortTwo();

        // 获取服务器当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        applicationForm.setDate(formattedTime);

        long l = System.currentTimeMillis();
        int sort = applicationForm.getSort();
        if (sort == 1) {
            //出库
           applicationForm.setOrderNumber("CK_"+l);

        } else if (sort == 0) {
            //入库
            applicationForm.setOrderNumber("RK_"+l);

        }
        else if (sort ==2){
            //采购
            applicationForm.setOrderNumber("CG_"+l);
        }
        mapper1.add(applicationForm);
        int id = applicationForm.getId();
        //添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);
        mapper9.addTakeOrder(takeOrder);


        //查询是否是采购入库
        int sortTwo = applicationForm.getSortTwo();
        if (sortTwo==9){
            //采购入库

            if (jsonArrays.size()>2 && jsonArrays.get(2)!=null ){
                JSONArray jsonArray = jsonArrays.get(2);


                String s = jsonArray.toJSONString();
                List<Invoice> invoices = JSONArray.parseArray(s, Invoice.class);
                for (int i = 0; i < invoices.size(); i++) {
                    Invoice invoice = invoices.get(i);
                    invoice.setAppId(id);
                }
                //调用mapper
                mapper8.insertAllInvoice(invoices);
            }
            else if (jsonArrays.size()==3){
                JSONArray jsonArray = jsonArrays.get(3);

                Object o = jsonArray.get(0);


// 将 Object 强转为 JSONObject
                JSONObject jsonObj = (JSONObject) o;
                Object appFormId = jsonObj.get("appFormId");
                for (int i = 0; i < applicationContents.size(); i++) {
                    ApplicationContent applicationContent = applicationContents.get(i);
                    applicationContent.setApplicationId((Integer) appFormId);
                }
            }

            //采购入库要更新转化为采购入库的数量

            mapper.updateActual(applicationContents);
//            System.out.println("检测"+applicationContents);

        }

        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setApplicationId(id);

            //查询价格
            if (sortTwo!=9 || (sortTwo==9 && jsonArrays.size()<=2)){

//                int productId = applicationContent.getProductId();
//                int vault = applicationContent.getVault();
                //根据prodId和vault查询当前单价
//                double v = mapper7.selectPrice(productId, vault);
                //设置价格
                applicationContent.setAppPrice(applicationContent.getAppPrice());
            }
        }




        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setActualNumber(0);
        }
        //添加申请单物料信息
        mapper.add(applicationContents);


        //如果是退货入库，退货出库，归还，采购入库的申请单要生成关系网
        if (sortTwo==8||sortTwo==10||sortTwo==13||sortTwo==9){
            System.out.println(jsonArrays);
            JSONArray jsonArray;
            if (sortTwo==9){

                //采购入库的储存位置不同
                if(jsonArrays.size()<3){
                    jsonArray = jsonArrays.get(1);
                }
                else {
                    jsonArray = jsonArrays.get(3);
                }

            }
            else {
                jsonArray = jsonArrays.get(2);
            }

            Object o = jsonArray.get(0);


// 将 Object 强转为 JSONObject
            JSONObject jsonObj = (JSONObject) o;
            Object appFormId = jsonObj.get("appFormId");
            if (appFormId!=null ){
                Relationship relationship = new Relationship(0, Integer.parseInt(appFormId.toString()),id,0,0,0);
                //调用service
                RelationshipService relationshipService = new RelationshipServiceImpl();
                relationshipService.addRelationship(relationship);
            }



            //如果是采购入库的话要进行原来关系的
        }


        //如果当前申请单时退货出库申请单的话
        if (sortTwo==10){
            //查询需要修改为撤销中的申请单id
            RelationshipServiceImpl relationshipService = new RelationshipServiceImpl();

            Integer i = relationshipService.selectOld(id);
            if (i!=null){
                mapper1.updateStatus(7,i );
            }



        }


        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);
        if (level==2){
            examine.setMinister(1);
        }
        if (level==3){
            examine.setMinister(1);
        }
        //获取审核表mapper
        ExamineMapper mapper2 = sqlSession.getMapper(ExamineMapper.class);
        //查询审核流程
        ExamineAllTypeMapper mapper3 = sqlSession.getMapper(ExamineAllTypeMapper.class);
        ExamineStepContentMapper mapper4 = sqlSession.getMapper(ExamineStepContentMapper.class);

        //查询审核步骤
        List<ExamineAllType> examineAllTypes = mapper3.selectStep(sortTwo);
        List<ExamineStepContent>examineStepContents = new ArrayList<>();

        //获取申请单的总价格
        List<Map<String, Object>> maps = mapper1.selectPriceAndCount(Collections.singletonList(applicationForm));

        Object totalPrice = maps.get(0).get("totalPrice");

        //获取申请单所有的单价

        List<Map<String, Object>> unitPrice = mapper.selectPrice(applicationContents);



        ExamineConditionContentMapper mapper6 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //除部长审核外的审核流程

        if (examineAllTypes.size()>0){
            //存在审核步骤，查询步骤都有什么
            for (int i = 0; i < examineAllTypes.size(); i++) {
                ExamineAllType examineAllType = examineAllTypes.get(i);
                //查询审核条件(审核部门，审核步骤)
                int id1 = examineAllType.getId();
                List<Map<String, Object>> maps1 = mapper6.selectConditionByStepId(id1);

                if (maps1.size()>0){
                    boolean b = false;
                    //存在审核条件
                    for (int j = 0; j < maps1.size(); j++) {
                        Map<String, Object> map = maps1.get(j);
                        Object name = map.get("name");
                        if (name.equals("单价")){
                            Object content = map.get("content");

                            //判断是否有单价符合该条件
                            for (int k = 0; k < unitPrice.size(); k++) {
                                Object price1 = unitPrice.get(k).get("price");//得到单价，判断是否符合

                               b = Compare.compare4(content.toString(), price1.toString());
                                if (b){
                                    break;
                                }
                            }
                        }
                        else if (name.equals("总价")){
                            Object content = map.get("content");
                            //判断总价是否符合
                            b = Compare.compare4(content.toString(), totalPrice.toString());
                            if (b){
                                break;
                            }
                        }

                        if (b){
                            break;
                        }
                    }

                    if (b){
                        //符合这一步的审核条件，生成具体审核步骤内容
                        ExamineStepContent examineStepContent = new ExamineStepContent();
                        examineStepContent.setExamineStepId(id1);
                        examineStepContent.setAppFormId(applicationForm.getId());
                        //添加步骤
                        if (level==2 || level==3||level==4) {
                            examineStepContents.add(examineStepContent);
                        }


                        //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                        break;

                    }


                }
                else {
                    //没有条件，有步骤，生成具体审核步骤内容

                    ExamineStepContent examineStepContent = new ExamineStepContent();
                    examineStepContent.setExamineStepId(id1);
                    examineStepContent.setAppFormId(applicationForm.getId());
                    //添加审核步骤内容
                    if (level==2 || level==3||level==4){
                        examineStepContents.add(examineStepContent);
                    }

                    //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                    break;
                }

            }
            mapper2.addExamine(examine);
            if (examineStepContents.size()>0){
                //添加具体审核步骤内容
                mapper4.add(examineStepContents);
                int examineStepId = examineStepContents.get(0).getExamineStepId();
                ExamineAllType examineAllType = mapper3.selectById(examineStepId);
                int departmentId = examineAllType.getDepartmentId();
                //更新审申请单上的审核部门
                mapper1.updateCirculation(departmentId,id);

            }

            //查询审核步骤是否都已通过
            boolean complete=true;
//            List<Examine> examines = mapper2.selectByAppId(id);
            List<Map<String, Object>> maps1 = mapper4.selectByAppId(id);

            for (int i = 0; i < maps1.size(); i++) {
                Map<String, Object> map = maps1.get(i);
                Object result = map.get("result");
                if (!result.equals(1)){
                    complete=false;
                    break;
                }
            }
            if (maps1.size()>0){
                //存在需要审核的步骤
                if (complete){
                    //审核都已通过
                    mapper1.updateCirculationBoss(id);
                }
            }
            else {
                //不存在需要审核的步骤,确保审核直接通过添加了level == 4
                if (level==3 || level==2 || level ==4){
                    mapper1.updateCirculationBoss(id);
                }
            }



        }
        else {

            //没有步骤，又是部长或总经理审核的，直接完成申请单到仓库去
            //确保审核直接通过添加了level == 4
            if (level==3 || level==2||level==4){
                mapper1.updateCirculationBoss(id);

            }
            mapper2.addExamine(examine);

        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


//采购拆分添加申请单
    public void addAllCf(ApplicationForm applicationForm,List<ApplicationContent>applicationContents,List<JSONArray>jsonArrays){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        UserMapper mapper5 = sqlSession.getMapper(UserMapper.class);
        InvoiceMapper mapper8 = sqlSession.getMapper(InvoiceMapper.class);
        TakeOrderMapper mapper9 = sqlSession.getMapper(TakeOrderMapper.class);


        //查询申请人的部门
        User user = mapper5.selectById(applicationForm.getUserId());
        int department = user.getDepartment();
        int level = user.getLevel();

        applicationForm.setCirculation(department);
//        System.out.println("部门"+department);
        //添加申请单



//        int sortTwo1 = applicationForm.getSortTwo();

        // 获取服务器当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        applicationForm.setDate(formattedTime);

        long l = System.currentTimeMillis();
        int sort = applicationForm.getSort();
      if (sort == 0) {
            //入库
            applicationForm.setOrderNumber("RK_"+l);

        }

        mapper1.add(applicationForm);
      //得到新增的入库申请单信息
        int id = applicationForm.getId();


        //添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);


        mapper9.addTakeOrder(takeOrder);


        //查询是否是采购入库
        int sortTwo = applicationForm.getSortTwo();
        if (sortTwo==9){
            //采购入库

            if (jsonArrays.size()>2 && jsonArrays.get(2)!=null ){
                JSONArray jsonArray = jsonArrays.get(2);


                String s = jsonArray.toJSONString();
                List<Invoice> invoices = JSONArray.parseArray(s, Invoice.class);
                for (int i = 0; i < invoices.size(); i++) {
                    Invoice invoice = invoices.get(i);
                    invoice.setAppId(id);
                }
                //调用mapper
                mapper8.insertAllInvoice(invoices);
            }
//            System.out.println("看看"+applicationContents);

            //采购入库要更新转化为采购入库的数量

            mapper.updateActual(applicationContents);

        }


        //要更新关系网后再设置appId因为现在是申请内容的原申请单Id
        List<Relationship> relationships = new ArrayList<>();

        for (int i = 0; i < applicationContents.size(); i++) {
            int applicationId = applicationContents.get(i).getApplicationId();
            int id1 = applicationContents.get(i).getId();
            Relationship relationship = new Relationship(0,applicationId,id,id1,0,applicationContents.get(i).getAppNumber());
            relationships.add(relationship);
        }








        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setApplicationId(id);

            //查询价格
            if (sortTwo!=9 || (sortTwo==9 && jsonArrays.size()<=2)){

//                int productId = applicationContent.getProductId();
//                int vault = applicationContent.getVault();
                //根据prodId和vault查询当前单价
//                double v = mapper7.selectPrice(productId, vault);
                //设置价格
                applicationContent.setAppPrice(applicationContent.getAppPrice());
            }
        }




        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setActualNumber(0);
        }
        //添加申请单物料信息
        mapper.add(applicationContents);



            //调用service

        relationshipService.addRelationshipList(relationships);



        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);
        if (level==2){
            examine.setMinister(1);
        }
        if (level==3){
            examine.setMinister(1);
        }
        //获取审核表mapper
        ExamineMapper mapper2 = sqlSession.getMapper(ExamineMapper.class);
        //查询审核流程
        ExamineAllTypeMapper mapper3 = sqlSession.getMapper(ExamineAllTypeMapper.class);
        ExamineStepContentMapper mapper4 = sqlSession.getMapper(ExamineStepContentMapper.class);

        //查询审核步骤
        List<ExamineAllType> examineAllTypes = mapper3.selectStep(sortTwo);
        List<ExamineStepContent>examineStepContents = new ArrayList<>();

        //获取申请单的总价格
        List<Map<String, Object>> maps = mapper1.selectPriceAndCount(Collections.singletonList(applicationForm));

        Object totalPrice = maps.get(0).get("totalPrice");

        //获取申请单所有的单价

        List<Map<String, Object>> unitPrice = mapper.selectPrice(applicationContents);



        ExamineConditionContentMapper mapper6 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //除部长审核外的审核流程

        if (examineAllTypes.size()>0){
            //存在审核步骤，查询步骤都有什么
            for (int i = 0; i < examineAllTypes.size(); i++) {
                ExamineAllType examineAllType = examineAllTypes.get(i);
                //查询审核条件(审核部门，审核步骤)
                int id1 = examineAllType.getId();
                List<Map<String, Object>> maps1 = mapper6.selectConditionByStepId(id1);

                if (maps1.size()>0){
                    boolean b = false;
                    //存在审核条件
                    for (int j = 0; j < maps1.size(); j++) {
                        Map<String, Object> map = maps1.get(j);
                        Object name = map.get("name");
                        if (name.equals("单价")){
                            Object content = map.get("content");

                            //判断是否有单价符合该条件
                            for (int k = 0; k < unitPrice.size(); k++) {
                                Object price1 = unitPrice.get(k).get("price");//得到单价，判断是否符合

                                b = Compare.compare4(content.toString(), price1.toString());
                                if (b){
                                    break;
                                }
                            }
                        }
                        else if (name.equals("总价")){
                            Object content = map.get("content");
                            //判断总价是否符合
                            b = Compare.compare4(content.toString(), totalPrice.toString());
                            if (b){
                                break;
                            }
                        }

                        if (b){
                            break;
                        }
                    }

                    if (b){
                        //符合这一步的审核条件，生成具体审核步骤内容
                        ExamineStepContent examineStepContent = new ExamineStepContent();
                        examineStepContent.setExamineStepId(id1);
                        examineStepContent.setAppFormId(applicationForm.getId());
                        //添加步骤
                        if (level==2 || level==3||level==4) {
                            examineStepContents.add(examineStepContent);
                        }


                        //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                        break;

                    }


                }
                else {
                    //没有条件，有步骤，生成具体审核步骤内容

                    ExamineStepContent examineStepContent = new ExamineStepContent();
                    examineStepContent.setExamineStepId(id1);
                    examineStepContent.setAppFormId(applicationForm.getId());
                    //添加审核步骤内容
                    if (level==2 || level==3||level==4){
                        examineStepContents.add(examineStepContent);
                    }

                    //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                    break;
                }

            }
            mapper2.addExamine(examine);
            if (examineStepContents.size()>0){
                //添加具体审核步骤内容
                mapper4.add(examineStepContents);
                int examineStepId = examineStepContents.get(0).getExamineStepId();
                ExamineAllType examineAllType = mapper3.selectById(examineStepId);
                int departmentId = examineAllType.getDepartmentId();
                //更新审申请单上的审核部门
                mapper1.updateCirculation(departmentId,id);

            }

            //查询审核步骤是否都已通过
            boolean complete=true;
//            List<Examine> examines = mapper2.selectByAppId(id);
            List<Map<String, Object>> maps1 = mapper4.selectByAppId(id);

            for (int i = 0; i < maps1.size(); i++) {
                Map<String, Object> map = maps1.get(i);
                Object result = map.get("result");
                if (!result.equals(1)){
                    complete=false;
                    break;
                }
            }
            if (maps1.size()>0){
                //存在需要审核的步骤
                if (complete){
                    //审核都已通过
                    mapper1.updateCirculationBoss(id);
                }
            }
            else {
                //不存在需要审核的步骤,确保审核直接通过添加了level == 4
                if (level==3 || level==2 || level ==4){
                    mapper1.updateCirculationBoss(id);
                }
            }



        }
        else {

            //没有步骤，又是部长或总经理审核的，直接完成申请单到仓库去
            //确保审核直接通过添加了level == 4
            if (level==3 || level==2||level==4){
                mapper1.updateCirculationBoss(id);

            }
            mapper2.addExamine(examine);

        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //生成添加新的订购单
    public void addAllDg(ApplicationForm applicationForm,List<ApplicationContent>applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        UserMapper mapper5 = sqlSession.getMapper(UserMapper.class);
        TakeOrderMapper mapper7 = sqlSession.getMapper(TakeOrderMapper.class);
        RelationshipMapper mapper8 = sqlSession.getMapper(RelationshipMapper.class);
        ExamineLogMapper mapper9 = sqlSession.getMapper(ExamineLogMapper.class);


        //查询申请人的部门
        User user = mapper5.selectById(applicationForm.getUserId());
        int department = user.getDepartment();
        int level = user.getLevel();

        applicationForm.setCirculation(department);
//        System.out.println("部门"+department);
        //添加申请单



//        int sortTwo1 = applicationForm.getSortTwo();

        // 获取服务器当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        applicationForm.setDate(formattedTime);

        long l = System.currentTimeMillis();
        int sort = applicationForm.getSort();
        if (sort == 3) {
            //订购单
            applicationForm.setOrderNumber("DG_"+l);

        }
//        System.out.println("检测"+applicationForm);
        mapper1.add(applicationForm);
        //得到新增的订购申请单信息
        int id = applicationForm.getId();
        //添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);


        mapper7.addTakeOrder(takeOrder);


        //查询是否是订购单
        int sortTwo = applicationForm.getSortTwo();
        if (sortTwo==15){
            //订购单


//            System.out.println("看看"+applicationContents);

            //订购申请单要更新其他申请单的申请数量（已订购数量）

//            mapper.updateAppDgNumber(applicationContents);

        }
        //根据申请单查询最新的物料信息审核价格
        List<Map<String, Object>> maps2 = mapper.selectNewPriceByAppId(applicationContents);

        //要更新关系网后再设置appId因为现在是申请内容的原申请单Id
        List<Relationship> relationships = new ArrayList<>();

        for (int i = 0; i < applicationContents.size(); i++) {
            int applicationId = applicationContents.get(i).getApplicationId();
            int id1 = applicationContents.get(i).getId();
            Relationship relationship = new Relationship(0,applicationId,id,id1,0,applicationContents.get(i).getAppNumber());
            relationships.add(relationship);
        }








        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setApplicationId(id);

        }




        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setActualNumber(0);
        }

        System.out.println("查看"+applicationContents);
        //查询每个申请单的最新的审核时间



// 使用 Map 进行分组，key 是 productId 和 vault 的组合
        Map<String, ApplicationContent> mergedMap = new HashMap<>();

        for (ApplicationContent item : applicationContents) {
            // 创建唯一键：productId_vault
            String key = item.getProductId() + "_" + item.getVault();

            if (mergedMap.containsKey(key)) {
                // 如果已存在，合并数据
                ApplicationContent existing = mergedMap.get(key);

                // 求和
                existing.setAppNumber(existing.getAppNumber() + item.getAppNumber());
                existing.setActualNumber(existing.getActualNumber() + item.getActualNumber());

                // 计算加权平均价格
                // 平均价格 = (原总金额 + 新总金额) / (原总数量 + 新总数量)
                double existingTotalValue = existing.getAppPrice() * existing.getAppNumber();
                double newTotalValue = item.getAppPrice() * item.getAppNumber();
                double totalAppNumber = existing.getAppNumber() + item.getAppNumber();

                if (totalAppNumber > 0) {
                    double avgPrice = (existingTotalValue + newTotalValue) / totalAppNumber;
                    existing.setAppPrice(avgPrice);
                }

            } else {
                // 如果不存在，创建新对象（注意要深拷贝，防止影响原数据）

                ApplicationContent applicationContent = new ApplicationContent(item.getId(),item.getApplicationId(),item.getProductId(),item.getAppNumber(),item.getActualNumber(),item.getVault(),item.getAppPrice(),item.getFinProductId(),item.getFinProductNumber(),item.getLastLevel(),item.getBomTitleId());

                mergedMap.put(key, applicationContent);
            }
        }

// 获取合并后的列表
        List<ApplicationContent> mergedList = new ArrayList<>(mergedMap.values());



        // 创建一个 Map 来存储每个 product_id 和 vault 对应的最新 app_price
        Map<String, Double> latestPriceMap = new HashMap<>();

// 将 maps2 中的数据放入 Map 中，key 为 "product_id_vault"
        for (Map<String, Object> map : maps2) {
            Object productId = map.get("product_id");
            Object vault = map.get("vault");
            Object appPrice = map.get("app_price");

            if (productId != null && vault != null && appPrice != null) {
                String key = productId.toString() + "_" + vault.toString();
                try {
                    Double price = new Double(appPrice.toString());
                    latestPriceMap.put(key, price);
                } catch (NumberFormatException e) {
                    // 转换失败，跳过
                }
            }
        }

// 更新 mergedList 中的 appPrice
        for (ApplicationContent content : mergedList) {
            String key = content.getProductId() + "_" + content.getVault();
            if (latestPriceMap.containsKey(key)) {
                System.out.println(key);
                System.out.println(latestPriceMap.get(key));
                content.setAppPrice(latestPriceMap.get(key));
            }
        }

        //添加申请单物料信息
        mapper.add(mergedList);



        //调用service

        mapper8.addRelationshipList(relationships);



        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);
        if (level==2){
            examine.setMinister(1);
        }
        if (level==3){
            examine.setMinister(1);
        }
        //获取审核表mapper
        ExamineMapper mapper2 = sqlSession.getMapper(ExamineMapper.class);
        //查询审核流程
        ExamineAllTypeMapper mapper3 = sqlSession.getMapper(ExamineAllTypeMapper.class);
        ExamineStepContentMapper mapper4 = sqlSession.getMapper(ExamineStepContentMapper.class);

        //查询审核步骤
        List<ExamineAllType> examineAllTypes = mapper3.selectStep(sortTwo);
        List<ExamineStepContent>examineStepContents = new ArrayList<>();

        //获取申请单的总价格
        List<Map<String, Object>> maps = mapper1.selectPriceAndCount(Collections.singletonList(applicationForm));

        Object totalPrice = maps.get(0).get("totalPrice");

        //获取申请单所有的单价

        List<Map<String, Object>> unitPrice = mapper.selectPrice(applicationContents);



        ExamineConditionContentMapper mapper6 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //除部长审核外的审核流程

        if (examineAllTypes.size()>0){
            //存在审核步骤，查询步骤都有什么
            for (int i = 0; i < examineAllTypes.size(); i++) {
                ExamineAllType examineAllType = examineAllTypes.get(i);
                //查询审核条件(审核部门，审核步骤)
                int id1 = examineAllType.getId();
                List<Map<String, Object>> maps1 = mapper6.selectConditionByStepId(id1);

                if (maps1.size()>0){
                    boolean b = false;
                    //存在审核条件
                    for (int j = 0; j < maps1.size(); j++) {
                        Map<String, Object> map = maps1.get(j);
                        Object name = map.get("name");
                        if (name.equals("单价")){
                            Object content = map.get("content");

                            //判断是否有单价符合该条件
                            for (int k = 0; k < unitPrice.size(); k++) {
                                Object price1 = unitPrice.get(k).get("price");//得到单价，判断是否符合

                                b = Compare.compare4(content.toString(), price1.toString());
                                if (b){
                                    break;
                                }
                            }
                        }
                        else if (name.equals("总价")){
                            Object content = map.get("content");
                            //判断总价是否符合
                            b = Compare.compare4(content.toString(), totalPrice.toString());
                            if (b){
                                break;
                            }
                        }

                        if (b){
                            break;
                        }
                    }

                    if (b){
                        //符合这一步的审核条件，生成具体审核步骤内容
                        ExamineStepContent examineStepContent = new ExamineStepContent();
                        examineStepContent.setExamineStepId(id1);
                        examineStepContent.setAppFormId(applicationForm.getId());
                        //添加步骤
                        if (level==2 || level==3||level==4) {
                            examineStepContents.add(examineStepContent);
                        }


                        //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                        break;

                    }


                }
                else {
                    //没有条件，有步骤，生成具体审核步骤内容

                    ExamineStepContent examineStepContent = new ExamineStepContent();
                    examineStepContent.setExamineStepId(id1);
                    examineStepContent.setAppFormId(applicationForm.getId());
                    //添加审核步骤内容
                    if (level==2 || level==3||level==4){
                        examineStepContents.add(examineStepContent);
                    }

                    //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                    break;
                }

            }
            mapper2.addExamine(examine);
            if (examineStepContents.size()>0){
                //添加具体审核步骤内容
                mapper4.add(examineStepContents);
                int examineStepId = examineStepContents.get(0).getExamineStepId();
                ExamineAllType examineAllType = mapper3.selectById(examineStepId);
                int departmentId = examineAllType.getDepartmentId();
                //更新审申请单上的审核部门
                mapper1.updateCirculation(departmentId,id);

            }

            //查询审核步骤是否都已通过
            boolean complete=true;
//            List<Examine> examines = mapper2.selectByAppId(id);
            List<Map<String, Object>> maps1 = mapper4.selectByAppId(id);

            for (int i = 0; i < maps1.size(); i++) {
                Map<String, Object> map = maps1.get(i);
                Object result = map.get("result");
                if (!result.equals(1)){
                    complete=false;
                    break;
                }
            }
            if (maps1.size()>0){
                //存在需要审核的步骤
                if (complete){
                    //审核都已通过
                    mapper1.updateCirculationBoss(id);
                }
            }
            else {
                //不存在需要审核的步骤,确保审核直接通过添加了level == 4
                if (level==3 || level==2 || level ==4){
                    mapper1.updateCirculationBoss(id);
                }
            }



        }
        else {

            //没有步骤，又是部长或总经理审核的，直接完成申请单到仓库去
            //确保审核直接通过添加了level == 4
            if (level==3 || level==2||level==4){
                mapper1.updateCirculationBoss(id);

            }
            mapper2.addExamine(examine);

        }

        //这是生成订购单，查询该订购单的数据源：采购单的ID，查询采购单ID下的内容是否已经全部完成申请了，全部完成后，修改这些申请单为已完成状态
        List<ApplicationForm> applicationForms = mapper8.selectOldByNew(id);

        for (int i = 0; i < applicationForms.size(); i++) {
            int id1 = applicationForms.get(i).getId();
            //查询该申请单下是否已经全部申请了
            boolean b = mapper.selectComplete(id1);

            if (!b){
                //更新申请单为完成状态
//                mapper.updateComplete(id1);
            }
        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //生成添加新的质检单
    public void addAllZj(ApplicationForm applicationForm,List<ApplicationContent>applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        UserMapper mapper5 = sqlSession.getMapper(UserMapper.class);
        TakeOrderMapper mapper7 = sqlSession.getMapper(TakeOrderMapper.class);
        RelationshipMapper mapper8 = sqlSession.getMapper(RelationshipMapper.class);

        //查询申请人的部门
        User user = mapper5.selectById(applicationForm.getUserId());
        int department = user.getDepartment();
        int level = user.getLevel();

        applicationForm.setCirculation(department);
//        System.out.println("部门"+department);
        //添加申请单



//        int sortTwo1 = applicationForm.getSortTwo();

        // 获取服务器当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        applicationForm.setDate(formattedTime);

        long l = System.currentTimeMillis();
        int sort = applicationForm.getSort();
        if (sort == 4) {
            //质检单
            applicationForm.setOrderNumber("ZJ_"+l);

        }
//        System.out.println("检测"+applicationForm);
        mapper1.add(applicationForm);
        //得到新增的订购申请单信息
        int id = applicationForm.getId();

        //添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);


        mapper7.addTakeOrder(takeOrder);

        //查询是否是质检单
        int sortTwo = applicationForm.getSortTwo();
        if (sortTwo==16){
            //质检单


//            System.out.println("看看"+applicationContents);

            //质检单要更新原申请单的已申请质检数量

            mapper.updateAppZjNumber(applicationContents);

        }


        //要更新关系网后再设置appId因为现在是申请内容的原申请单Id
        List<Relationship> relationships = new ArrayList<>();

        for (int i = 0; i < applicationContents.size(); i++) {
            int applicationId = applicationContents.get(i).getApplicationId();
            int id1 = applicationContents.get(i).getId();
            Relationship relationship = new Relationship(0,applicationId,id,id1,0,applicationContents.get(i).getAppNumber());
            relationships.add(relationship);
        }











        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setApplicationId(id);

        }




        for (int i = 0; i < applicationContents.size(); i++) {
            ApplicationContent applicationContent = applicationContents.get(i);
            applicationContent.setActualNumber(0);
        }

        // 使用 Map 进行分组，key 是 productId 和 vault 的组合
        Map<String, ApplicationContent> mergedMap = new HashMap<>();

        for (ApplicationContent item : applicationContents) {
            // 创建唯一键：productId_vault
            String key = item.getProductId() + "_" + item.getVault();

            if (mergedMap.containsKey(key)) {
                // 如果已存在，合并数据
                ApplicationContent existing = mergedMap.get(key);

                // 保存合并前的数量和金额用于计算
                BigDecimal existingTotalValue = BigDecimal.valueOf(existing.getAppPrice())
                        .multiply(BigDecimal.valueOf(existing.getAppNumber()));
                BigDecimal newTotalValue = BigDecimal.valueOf(item.getAppPrice())
                        .multiply(BigDecimal.valueOf(item.getAppNumber()));

                // 合并数量
                double mergedAppNumber = existing.getAppNumber() + item.getAppNumber();
                double mergedActualNumber = existing.getActualNumber() + item.getActualNumber();

                // 计算加权平均价格
                BigDecimal totalValue = existingTotalValue.add(newTotalValue);
                BigDecimal avgPrice = BigDecimal.ZERO;

                if (mergedAppNumber > 0) {
                    avgPrice = totalValue.divide(BigDecimal.valueOf(mergedAppNumber), 6, RoundingMode.HALF_UP);
                }

                // 更新已存在的对象
                existing.setAppNumber(mergedAppNumber);
                existing.setActualNumber(mergedActualNumber);
                existing.setAppPrice(avgPrice.doubleValue());

            } else {
                // 如果不存在，创建新对象
                ApplicationContent applicationContent = new ApplicationContent(
                        item.getId(),
                        item.getApplicationId(),
                        item.getProductId(),
                        item.getAppNumber(),
                        item.getActualNumber(),
                        item.getVault(),
                        item.getAppPrice(),
                        item.getFinProductId(),
                        item.getFinProductNumber(),
                        item.getLastLevel(),
                        item.getBomTitleId()
                );
                mergedMap.put(key, applicationContent);
            }
        }

// 获取合并后的列表
        List<ApplicationContent> mergedList = new ArrayList<>(mergedMap.values());
        //添加申请单物料信息
        mapper.add(mergedList);
        System.out.println("合并"+mergedList);



        //调用service

        mapper8.addRelationshipList(relationships);




        //建立质检单其他内容
        List<Inspection> inspectionList = new ArrayList<>();
        for (int i = 0; i < mergedList.size(); i++) {
            ApplicationContent applicationContent = mergedList.get(i);
            Inspection inspection =new Inspection(0,id,applicationContent.getProductId(),applicationContent.getVault(),0,"",0);
            inspectionList.add(inspection);
        }

            inspectionService.add(inspectionList);


        //调用service

        relationshipService.addRelationshipList(relationships);

        //更新bomtitleid为0
        mapper.updateBomTitleId(applicationContents);
        //更新申请内哦让为
//        mapper.updateApplicationId(applicationContents,id);




        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);
        if (level==2){
            examine.setMinister(1);
        }
        if (level==3){
            examine.setMinister(1);
        }
        //获取审核表mapper
        ExamineMapper mapper2 = sqlSession.getMapper(ExamineMapper.class);
        //查询审核流程
        ExamineAllTypeMapper mapper3 = sqlSession.getMapper(ExamineAllTypeMapper.class);
        ExamineStepContentMapper mapper4 = sqlSession.getMapper(ExamineStepContentMapper.class);

        //查询审核步骤
        List<ExamineAllType> examineAllTypes = mapper3.selectStep(sortTwo);
        List<ExamineStepContent>examineStepContents = new ArrayList<>();

        //获取申请单的总价格
        List<Map<String, Object>> maps = mapper1.selectPriceAndCount(Collections.singletonList(applicationForm));


        Object totalPrice = maps.get(0).get("totalPrice");

        //获取申请单所有的单价

        List<Map<String, Object>> unitPrice = mapper.selectPrice(applicationContents);



        ExamineConditionContentMapper mapper6 = sqlSession.getMapper(ExamineConditionContentMapper.class);

        //除部长审核外的审核流程

        if (examineAllTypes.size()>0){
            //存在审核步骤，查询步骤都有什么
            for (int i = 0; i < examineAllTypes.size(); i++) {
                ExamineAllType examineAllType = examineAllTypes.get(i);
                //查询审核条件(审核部门，审核步骤)
                int id1 = examineAllType.getId();
                List<Map<String, Object>> maps1 = mapper6.selectConditionByStepId(id1);

                if (maps1.size()>0){
                    boolean b = false;
                    //存在审核条件
                    for (int j = 0; j < maps1.size(); j++) {
                        Map<String, Object> map = maps1.get(j);
                        Object name = map.get("name");
                        if (name.equals("单价")){
                            Object content = map.get("content");

                            //判断是否有单价符合该条件
                            for (int k = 0; k < unitPrice.size(); k++) {
                                Object price1 = unitPrice.get(k).get("price");//得到单价，判断是否符合

                                b = Compare.compare4(content.toString(), price1.toString());
                                if (b){
                                    break;
                                }
                            }
                        }
                        else if (name.equals("总价")){
                            Object content = map.get("content");
                            //判断总价是否符合
                            b = Compare.compare4(content.toString(), totalPrice.toString());
                            if (b){
                                break;
                            }
                        }

                        if (b){
                            break;
                        }
                    }

                    if (b){
                        //符合这一步的审核条件，生成具体审核步骤内容
                        ExamineStepContent examineStepContent = new ExamineStepContent();
                        examineStepContent.setExamineStepId(id1);
                        examineStepContent.setAppFormId(applicationForm.getId());
                        //添加步骤
                        if (level==2 || level==3||level==4) {
                            examineStepContents.add(examineStepContent);
                        }


                        //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                        break;

                    }


                }
                else {
                    //没有条件，有步骤，生成具体审核步骤内容

                    ExamineStepContent examineStepContent = new ExamineStepContent();
                    examineStepContent.setExamineStepId(id1);
                    examineStepContent.setAppFormId(applicationForm.getId());
                    //添加审核步骤内容
                    if (level==2 || level==3||level==4){
                        examineStepContents.add(examineStepContent);
                    }

                    //只添加一个审核步骤，审核后再添加另一个步骤（一个一个添加）
                    break;
                }

            }
            mapper2.addExamine(examine);
            if (examineStepContents.size()>0){
                //添加具体审核步骤内容
                mapper4.add(examineStepContents);
                int examineStepId = examineStepContents.get(0).getExamineStepId();
                ExamineAllType examineAllType = mapper3.selectById(examineStepId);
                int departmentId = examineAllType.getDepartmentId();
                //更新审申请单上的审核部门
                mapper1.updateCirculation(departmentId,id);

            }

            //查询审核步骤是否都已通过
            boolean complete=true;
//            List<Examine> examines = mapper2.selectByAppId(id);
            List<Map<String, Object>> maps1 = mapper4.selectByAppId(id);

            for (int i = 0; i < maps1.size(); i++) {
                Map<String, Object> map = maps1.get(i);
                Object result = map.get("result");
                if (!result.equals(1)){
                    complete=false;
                    break;
                }
            }
            if (maps1.size()>0){
                //存在需要审核的步骤
                if (complete){
                    //审核都已通过
                    mapper1.updateCirculationBoss(id);
                }
            }
            else {
                //不存在需要审核的步骤,确保审核直接通过添加了level == 4
                if (level==3 || level==2 || level ==4){
                    mapper1.updateCirculationBoss(id);
                }
            }



        }
        else {

            //没有步骤，又是部长或总经理审核的，直接完成申请单到仓库去
            //确保审核直接通过添加了level == 4
            if (level==3 || level==2||level==4){
                mapper1.updateCirculationBoss(id);

            }
            mapper2.addExamine(examine);

        }

//        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //查看申请单数据(电子仓库)
   public List<ApplicationContent> selectByAppIdDz(int applicationId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       //调用mapper
       List<ApplicationContent> applicationContents = mapper.selectByAppIdDz(applicationId);
       //释放资源
       sqlSession.close();
       //返回数据
       return applicationContents;
   }

    //查看申请单数据(产品仓库)
    public List<ApplicationContent> selectByAppIdCp(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<ApplicationContent> applicationContents = mapper.selectByAppIdCp(applicationId);
        //释放资源
        sqlSession.close();
        //返回数据
        return applicationContents;
    }


    //查询申请单数据的详细信息（电子仓库物料信息）
   public List<Map<String,Object>> selectAppContentDz(List<ApplicationContent> applicationContents){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectAppContentDz(applicationContents);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //查询申请单数据的详细信息（产品仓库物料信息）
    public List<Map<String,Object>> selectAppContentCp(List<ApplicationContent> applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAppContentCp(applicationContents);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }



    //更改申请单为撤销状态
    public void updateRevoke(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        RelationshipMapper mapper1 = sqlSession.getMapper(RelationshipMapper.class);
        ApplicationContentMapper mapper2 = sqlSession.getMapper(ApplicationContentMapper.class);
        //查询该申请单的类型
        List<ApplicationForm> applicationForms = mapper.selectById(id);
        int sortTwo = applicationForms.get(0).getSortTwo();
        if (sortTwo==9){
            //该申请单是采购入库，那就查询旧的采购申请单，将该申请单入库的数据回退,查询采购申请单的id
            Integer oldId = mapper1.selectOld(id);
            //得到原来的采购申请单id,查询原来的申请单内容和现在的申请单内容
            List<ApplicationContent> oldApplicationContents = mapper2.selectAll(oldId);//旧的申请单内容
            List<ApplicationContent> newApplicationContents = mapper2.selectAll(id);//新的申请单内容
            for (int i = 0; i < oldApplicationContents.size(); i++) {
                ApplicationContent oldApplicationContent = oldApplicationContents.get(i);
                int productId = oldApplicationContent.getProductId();
                int vault = oldApplicationContent.getVault();
                int finProductId = oldApplicationContent.getFinProductId();
                double actualNumber = oldApplicationContent.getActualNumber();//申请的采购入库的数量
                for (int j = 0; j < newApplicationContents.size(); j++) {
                    ApplicationContent applicationContent = newApplicationContents.get(j);
                    int productId1 = applicationContent.getProductId();
                    int vault1 = applicationContent.getVault();
                    int finProductId1 = applicationContent.getFinProductId();
                    double appNumber = applicationContent.getAppNumber();

                    if (productId1==productId && vault==vault1 && finProductId==finProductId1){
                        oldApplicationContent.setActualNumber(actualNumber-appNumber);
                    }
                }
            }
            //更新旧的申请单
            mapper2.updateActual(oldApplicationContents);
            //查询旧的申请单是否存在出入库，如果没有就修改申请单状态
            //修改就旧申请单的状态
            boolean b = mapper2.selectIfComplete(oldId);
            if (b){
                //未完成
                mapper.updateStatus(0, oldId);
            }
            else {
                //已完成
                mapper.updateStatus(1, oldId);
            }

        }


        //调用mapper
        //查询这个要撤销的申请单是否已经存在出入库的情况了，如果不存在出入库情况那就直接修改为已撤销
        boolean b = mapper2.selectIfActualNumber(id);
        if (!b){
            //不存在出入库，修改为已撤销
            mapper.updateRevoke(id);
        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //更新申请单，电子仓库，产品仓库 出库
   public void updateAllCk(List<ApplicationContent> applicationContents,List<Product> products,List<FinProduct> finProducts,List<Log>logs){

        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
       FormCkMapper mapper2 = sqlSession.getMapper(FormCkMapper.class);
       FormRkMapper formRkMapper = sqlSession.getMapper(FormRkMapper.class);
       RelationshipMapper mapper3 = sqlSession.getMapper(RelationshipMapper.class);


       //获取服务器时间
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       String formattedTime = LocalDateTime.now().format(formatter);

       //调用mapper
       if (applicationContents.size()>0){
           mapper.updateActual(applicationContents);
       }

       if (products!=null && products.size()>0){
           for (int i = 0; i < products.size(); i++) {
               Product product = products.get(i);
               int id = product.getId();
               int vault = product.getVault();
               for (int j = 0; j < products.size(); j++) {
                   Product product1 = products.get(j);
                   int id1 = product1.getId();
                   int vault1 = product1.getVault();
                   if (i!=j){
                       if (id==id1 && vault==vault1){
                           BigDecimal number = new BigDecimal(product.getNumber());
                           BigDecimal number1 = new BigDecimal(product1.getNumber());

                           BigDecimal add = number1.add(number);

                           product.setNumber(String.valueOf(add));
                           products.remove(product1);
                       }
                   }
               }

           }

           mapper.updatePtCk(products);
       }

       if (finProducts!=null && finProducts.size()>0){
           for (int i = 0; i < finProducts.size(); i++) {
               FinProduct finProduct = finProducts.get(i);
               int id = finProduct.getId();
               int vault =finProduct.getVault();
               for (int j = 0; j < finProducts.size(); j++) {
                   FinProduct finProduct1 = finProducts.get(j);
                   int id1 = finProduct1.getId();
                   int vault1 = finProduct1.getVault();
                   if (i!=j){
                       if (id==id1 && vault==vault1){
                           int number = finProduct.getFinNumber();
                           int number1 = finProduct1.getFinNumber();
                           int i1 = number1 + number;
                          finProduct.setFinNumber(i1);
                          finProducts.remove(finProduct1);
                       }
                   }
               }

           }
           mapper.updateFptCk(finProducts);
       }


       ApplicationContent applicationContent = applicationContents.get(0);
       int applicationId = applicationContent.getApplicationId();
       boolean b = mapper.selectComplete(applicationId);

       if (!b){
           //更新申请单为完成状态
           mapper.updateComplete(applicationId);

           //检测是否为退货出库的申请单
           List<ApplicationForm> applicationForms = mapper1.selectById(applicationId);
           ApplicationForm applicationForm = applicationForms.get(0);

           int sortTwo = applicationForm.getSortTwo();

           if (sortTwo==10){
               //该申请单是退货出库的申请单,查看关联的申请单
               Integer i = mapper3.selectOld(applicationId);

               if (i!=null){
                   //查询该申请单是否是采购入库单

                   //更新为已撤销状态
                   //查询该申请单的类型
                   List<ApplicationForm> applicationForms1 = mapper1.selectById(i);
                   int sortTwo1 = applicationForms1.get(0).getSortTwo();

                   if (sortTwo1==9){
                       //该申请单时采购入库，那就查询旧的采购申请单，将该申请单入库的数据回退,查询采购申请单的id
                       Integer oldId = mapper3.selectOld(i);

                       if (oldId!=null){
                           //得到原来的采购申请单id,查询原来的申请单内容和现在的申请单内容
                           List<ApplicationContent> oldApplicationContents = mapper.selectAll(oldId);//旧的申请单内容
                           List<ApplicationContent> newApplicationContents = mapper.selectAll(i);//新的申请单内容
                           System.out.println(oldApplicationContents);
                           for (int j = 0; j < oldApplicationContents.size(); j++) {

                               ApplicationContent oldApplicationContent = oldApplicationContents.get(j);
                               int productId = oldApplicationContent.getProductId();
                               int vault = oldApplicationContent.getVault();
                               int finProductId = oldApplicationContent.getFinProductId();
                               double actualNumber = oldApplicationContent.getActualNumber();//申请的采购入库的数量

                               for (int k = 0; k < newApplicationContents.size(); k++) {
                                   ApplicationContent applicationContent1 = newApplicationContents.get(k);
                                   int productId1 = applicationContent1.getProductId();
                                   int vault1 = applicationContent1.getVault();
                                   int finProductId1 = applicationContent1.getFinProductId();
                                   double appNumber = applicationContent1.getAppNumber();

                                   if (productId1==productId && vault==vault1 && finProductId==finProductId1){
                                       oldApplicationContent.setActualNumber(actualNumber-appNumber);
                                   }
                               }
                           }

                           //更新旧的申请单
                           System.out.println("lllll"+oldApplicationContents);
                           mapper.updateActual(oldApplicationContents);
                           //查询旧的申请单是否存在出入库，如果没有就修改申请单状态
                           //修改就旧申请单的状态
                           boolean b1 = mapper.selectIfComplete(oldId);
                           if (b1){
                               //未完成
                               mapper1.updateStatus(0, oldId);
                           }
                           else {
                               //已完成
                               mapper1.updateStatus(1, oldId);
                           }
                       }



                   }


                   //调用mapper
                   //查询这个要撤销的申请单是否已经存在出入库的情况了，如果不存在出入库情况那就直接修改为已撤销
                   boolean b2 = mapper.selectIfActualNumber(i);
                   if (!b2){
                       //不存在出入库，修改为已撤销
                       mapper1.updateRevoke(i);
                   }
               }





           }
       }

       System.out.println("55555");
       for (int i = 0; i < logs.size(); i++) {
           logs.get(i).setDate(formattedTime);
       }
       mapper.addLog(logs);
       //修改申请单状态，修改为待签字状态
       mapper1.updateStatus(6,applicationId);

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //更新申请单，电子仓库，产品仓库 入库
    public void updateAllRk(List<ApplicationContent> applicationContents,List<Product> products,List<FinProduct> finProducts,List<Log>logs){

        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        FormRkMapper mapper2 = sqlSession.getMapper(FormRkMapper.class);
        FormCkMapper mapper3 = sqlSession.getMapper(FormCkMapper.class);
        ProductMapper mapper4 = sqlSession.getMapper(ProductMapper.class);
        RelationshipMapper mapper5 = sqlSession.getMapper(RelationshipMapper.class);
        MaterialReturnReceiveMapper materialReturnReceiveMapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        ReturnNumberMapper returnNumberMapper = sqlSession.getMapper(ReturnNumberMapper.class);
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        //调用mapper

        if (applicationContents.size()>0){
            mapper.updateActual(applicationContents);
        }
        if (products!=null && products.size()>0){
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                int id = product.getId();
                int vault = product.getVault();
                for (int j = 0; j < products.size(); j++) {
                    Product product1 = products.get(j);
                    int id1 = product1.getId();
                    int vault1 = product1.getVault();
                    if (i!=j){
                        if (id==id1 && vault==vault1){
                            BigDecimal number = new BigDecimal(product.getNumber());
                            BigDecimal number1 = new BigDecimal(product1.getNumber());

                            BigDecimal add = number1.add(number);
                            product.setNumber(String.valueOf(add));
                            products.remove(product1);
                        }
                    }
                }

            }

            mapper.updatePtRk(products);


        }
        if (finProducts!=null && finProducts.size()>0){
            for (int i = 0; i < finProducts.size(); i++) {
                FinProduct finProduct = finProducts.get(i);
                int id = finProduct.getId();
                int vault =finProduct.getVault();
                for (int j = 0; j < finProducts.size(); j++) {
                    FinProduct finProduct1 = finProducts.get(j);
                    int id1 = finProduct1.getId();
                    int vault1 = finProduct1.getVault();
                    if (i!=j){
                        if (id==id1 && vault==vault1){
                            int number = finProduct.getFinNumber();
                            int number1 = finProduct1.getFinNumber();
                            int i1 = number1 + number;
                            finProduct.setFinNumber(i1);
                            finProducts.remove(finProduct1);
                        }
                    }
                }

            }
            mapper.updateFptRk(finProducts);
        }



        ApplicationContent applicationContent = applicationContents.get(0);
        //获取质检单ID
        int applicationId = applicationContent.getApplicationId();

        boolean b = mapper.selectComplete(applicationId);
        if (!b){
            mapper.updateComplete(applicationId);
        }
        //获取申请单分类
        List<ApplicationForm> applicationForms = mapper1.selectById(applicationId);
        ApplicationForm applicationForm = applicationForms.get(0);
        int sortTwo = applicationForm.getSortTwo();

//        //判断是否是退货入库
//        if (sortTwo==8){
//            //退货入库,退货入库是记录在出库上，总数量减去就行了
//            //出库，创建新的出库条目表
//            List<FormCk> formCkList = new ArrayList<>();
//            for (int i = 0; i < applicationContents.size(); i++) {
//                ApplicationContent applicationContent1 = applicationContents.get(i);
//                int applicationId1 = applicationContent1.getApplicationId();
//                int productId = applicationContent1.getProductId();
//                int vault = applicationContent1.getVault();
//                double appNumber = applicationContent1.getAppNumber();
//
////                double appPrice = applicationContent1.getAppPrice();
//
//                //设置新的formCK
//                FormCk formCk = new FormCk();
//                formCk.setAppFormId(applicationId1);
//                formCk.setCkNumber(appNumber);
//                formCk.setCkSort(sortTwo);
//                formCk.setCkTime(formattedTime);
//                formCk.setProductId(productId);
//                formCk.setVault(vault);
//
//
//                //查询上次的数量
//                FormCk formCk1 = mapper3.selectLast(productId, vault);
//                if (formCk1!=null){
//                    //不为空
//                    double ckTotal = formCk1.getCkTotal();
//                    // 通过字符串初始化，避免精度问题
//                    BigDecimal num1 = new BigDecimal(ckTotal);
//                    BigDecimal num2 = new BigDecimal(appNumber);
//                    BigDecimal subtract= num1.subtract(num2);
//                    double v = subtract.doubleValue();
//                    formCk.setCkTotal(v);
//
//                }
//                else {
//                    //为空
//                    formCk.setCkTotal(appNumber);
//                }
//
//                formCkList.add(formCk);
//            }
//            //添加出库条目信息
//            mapper3.add(formCkList);
//        }
//        else {
//            //其他入库
//
//            List<FormRk>formRkList = new ArrayList<>();
//            for (int i = 0; i < applicationContents.size(); i++) {
//                ApplicationContent applicationContent1 = applicationContents.get(i);
//                int applicationId1 = applicationContent1.getApplicationId();
//                int productId = applicationContent1.getProductId();
//                int vault = applicationContent1.getVault();
//                double appPrice = applicationContent1.getAppPrice();
//
//                //设置新的formRK
//                FormRk formRk = new FormRk();
//                formRk.setAppFormId(applicationId1);
//                formRk.setActualPrice(appPrice);
//                formRk.setPriceSort(0);
//                formRk.setProductId(productId);
//                formRk.setVault(vault);
//                formRk.setRkTime(formattedTime);
//                formRk.setRkSort(sortTwo);
//
//                formRkList.add(formRk);
//            }
//            //添加入库条目信息
//            mapper2.add(formRkList);
//        }


        //如果是采购入库，要更新价格，这个是必须要运行的与上面的两个互补干扰


        if (sortTwo == 9) {

            mapper4.updatePrice(applicationContents);

            // 采购入库需要判断是否当前申请单的采购申请单是否已经完成全部入库了
            // 获取new_app_form_id,根据新的id去找旧的id
            List<ApplicationForm> applicationForms1 =
                    mapper5.selectOldByNew(applicationId);
            // 陈海燕申请的采购入库只能走到这里

            // Integer i = mapper5.selectOld(applicationId);

            // 得到了旧的采购申请单的id，查询该申请单的产品是否已经全部申请入库了
            if (applicationForms1.size() > 0) {

                int id = applicationForms1.get(0).getId(); // 质检单ID

                // 查询采购单明细
                List<ApplicationContent> cgApplicationContents =
                        mapper5.selectAllAppNumberByRk(applicationId);

                for (ApplicationContent rkContent : applicationContents) {

                    int productId = rkContent.getProductId();
                    int vault = rkContent.getVault();
                    double rkAllNumber = rkContent.getAppNumber(); // 入库单申请数量

                    // 查询让步接收
                    List<MaterialReturnReceive> rbList =
                            materialReturnReceiveMapper.selectRbByZj(id);

                    // 查询退货不继续采购
                    List<MaterialReturnReceive> returnList =
                            materialReturnReceiveMapper.selectReturnReceiveByProduct(
                                    productId, vault
                            );

                    ApplicationContent matchedCg = null;

                    /* ================= 第一阶段：让步接收 ================= */
                    for (MaterialReturnReceive rb : rbList) {

                        System.out.println("让步" + rb);

                        // ✅ 必须是让步接收
                        if (rb.getHandleType() != 2) {
                            continue;
                        }

                        // ✅ 必须是同一物料、同一仓库
                        if (rb.getProductId() != productId
                                || rb.getVault() != vault) {
                            continue;
                        }

                        double rbQty = rb.getReturnQty().doubleValue();
                        double rbQtyed = rb.getReturnQtyed().doubleValue();
                        double rbRemain = rbQty - rbQtyed;
                        System.out.println("物料："+productId);
                        System.out.println("让步数量"+rbQty);
                        System.out.println("已使用让步数量"+rbQtyed);

                        if (rbRemain < 0.000001) {
                            continue;
                        }

                        double useQty = Math.min(rkAllNumber, rbRemain);
                        System.out.println("userQty" + useQty);

                        rb.setReturnQtyed(BigDecimal.valueOf(rbQtyed + useQty));
                        materialReturnReceiveMapper.updateReturnQtyed(
                                rb.getReturnQtyed().doubleValue(),
                                rb.getId()
                        );

                        // ✅ 找 matchedCg
                        for (ApplicationContent cgContent : cgApplicationContents) {
                            if (cgContent.getProductId() == productId
                                    && cgContent.getVault() == vault) {
                                matchedCg = cgContent;
                                break;
                            }
                        }

                        if (matchedCg == null) {
                            throw new RuntimeException(
                                    "未找到采购单明细：" + productId + "-" + vault
                            );
                        }

                        ReturnNumber rn =
                                new ReturnNumber(0, useQty, matchedCg.getId(), 2);
                        returnNumberMapper.insert(rn);

                        rkAllNumber -= useQty;
                        System.out.println(rkAllNumber);

                        if (rkAllNumber < 0.000001) {
                            break;
                        }
                    }
                    System.out.println("rkAllNumber"+rkAllNumber);
                    /* ================= 第二阶段：采购单入库 ================= */

                    /*
                     * 如果经过第一阶段（让步接收）后，
                     * 入库单明细仍有剩余数量未处理，
                     * 则需要从【采购单明细】中继续扣减
                     */
                    if (rkAllNumber > 0.000001) {

                        /*
                         * 遍历所有采购单明细，
                         * 寻找与当前入库单明细【物料 + 仓库】完全匹配的采购记录
                         */
                        for (ApplicationContent cgContent : cgApplicationContents) {

                            /*
                             * 只处理同一物料、同一仓库的采购单明细
                             * productId 和 vault 必须完全一致
                             */
                            if (cgContent.getProductId() != productId
                                    || cgContent.getVault() != vault) {
                                continue;
                            }

                            /*
                             * 记录当前匹配到的采购单明细
                             * 后续退货 / 让步接收统计都会用到
                             */
                            matchedCg = cgContent;

                            /*
                             * 已入库数量（历史已入库）
                             */
                            double actualNumber = cgContent.getActualNumber();
                            System.out.println("历史入库数量"+actualNumber);

                            /*
                             * 该采购单明细最多还能入多少：
                             * 采购数量 - 已入库数量
                             */
                            double maxCanInStock =
                                    cgContent.getAppNumber() - actualNumber;
                            System.out.println("还能入库数量："+maxCanInStock);
                            /*
                             * 如果该采购单明细已经全部入库，
                             * 则跳过，继续看下一个采购单明细
                             */
                            if (maxCanInStock < 0.000001) {
                                continue;
                            }

                            /*
                             * 本次实际可入库数量：
                             * 取“入库单剩余数量”和“采购单可入库数量”的较小值
                             */
                            System.out.println("入库单剩余数量"+rkAllNumber);
                            double realInStock =
                                    Math.min(rkAllNumber, maxCanInStock);
                            System.out.println("本次实际可入库数量"+realInStock);
                            /*
                             * 计算新的已入库数量
                             */
                            double newActualNumber = actualNumber + realInStock;
                            System.out.println("入库数量"+newActualNumber);

                            /*
                             * 更新采购单明细对象的内存值
                             */
                            cgContent.setActualNumber(newActualNumber);

                            /*
                             * 持久化更新采购单明细的已入库数量
                             */
                            mapper.updateActualNumber(
                                    cgContent.getId(),
                                    newActualNumber
                            );

                            /*
                             * 扣减入库单明细的剩余未处理数量
                             */
                            rkAllNumber -= realInStock;

                            /*
                             * 同步更新入库单明细对象中的剩余数量
                             *（用于后续校验或日志）
                             */
                            rkContent.setAppNumber(rkAllNumber);

                            /*
                             * 如果入库单剩余数量已经为 0，
                             * 则当前入库单明细处理完成，跳出循环
                             */
                            if (rkAllNumber < 0.000001) {
                                break;
                            }
                        }
                    }

                    /* ================= 第三阶段：退货不继续采购 ================= */
                    if (rkAllNumber > 0.000001) {

                        double remainNeed = rkAllNumber;

                        for (MaterialReturnReceive ret : returnList) {

                            System.out.println("退货" + ret);

                            double returnQty = ret.getReturnQty().doubleValue();
                            double returnQtyed = ret.getReturnQtyed().doubleValue();

                            double returnRemain = returnQty - returnQtyed;
                            if (returnRemain < 0.000001) {
                                continue;
                            }

                            double useQty = Math.min(remainNeed, returnRemain);

                            ret.setReturnQtyed(
                                    BigDecimal.valueOf(returnQtyed + useQty)
                            );
                            materialReturnReceiveMapper.updateReturnQtyed(
                                    ret.getReturnQtyed().doubleValue(),
                                    ret.getId()
                            );

                            ReturnNumber rn =
                                    new ReturnNumber(0, useQty, matchedCg.getId(), 1);

                            returnNumberMapper.insert(rn);

                            remainNeed -= useQty;

                            if (remainNeed < 0.000001) {
                                break;
                            }
                        }
                    }

                    /* ================= 校验是否还有剩余 ================= */
                    if (rkAllNumber > 0.000001) {
                        throw new RuntimeException(
                                "入库单物料未找到足够来源：" + productId + "-" + vault
                        );
                    }
                }

                /* ================= 最终：所有明细完成才完成申请单 ================= */
                boolean allFinished = true;
                System.out.println(cgApplicationContents);
                for (ApplicationContent cg : cgApplicationContents) {

                    double actualNumber = cg.getActualNumber();
                    double appNumber = cg.getAppNumber();

                    System.out.println("采购" + cg.getId());

                    double rbUsed =
                            returnNumberMapper.sumByCgIdAndType(2,cg.getId());
                    double returnUsed =
                            returnNumberMapper.sumByCgIdAndType(1,cg.getId());

                    System.out.println("aaaaa");
                    System.out.println(
                            actualNumber + rbUsed + returnUsed - appNumber
                    );
                    System.out.println("入库" + actualNumber);
                    System.out.println("让步" + rbUsed);
                    System.out.println("退货" + returnUsed);
                    System.out.println("总数" + actualNumber);

                    double totalUsed = actualNumber + rbUsed + returnUsed;

                    if (totalUsed < appNumber - 0.000001) {
                        allFinished = false;
                        break;
                    }
                }

                System.out.println(allFinished);
                System.out.println(applicationId);

                if (allFinished) {
                    Integer purchaseOrderId =
                            cgApplicationContents.get(0).getApplicationId();
                    mapper1.updateStatus(1, purchaseOrderId);
                }
            }
        }


        //要写如果是让步接收入库
        if (sortTwo==17){
            //让步接收入库
            //1.根据让步接收数据差寻质检单数据，然后根据质检单数据查询采购单数据，然后修改采购单入库数据
            List<ApplicationContent> cgApplicationContents = mapper5.selectAllAppNumberByRk(applicationId);

            for (ApplicationContent rkContent : applicationContents) {
                int productId = rkContent.getProductId();
                int vault = rkContent.getVault();
                double rkAllNumber = rkContent.getAppNumber(); // 入库单剩余数量



                // 9. 入库单还有剩余，说明没匹配完
                if (rkAllNumber > 0.000001) {
                    throw new RuntimeException("入库单物料未找到足够采购单：" + productId + "-" + vault);
                }
            }

        }


//出入库成功后进行日志的记录,修改时间为服务器时间
        System.out.println("这里了吗");
        for (int i = 0; i < logs.size(); i++) {
            logs.get(i).setDate(formattedTime);
        }
        //提交日志
        mapper.addLog(logs);
        //修改申请单状态，修改为待签字状态
        mapper1.updateStatus(6,applicationId);

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //更新申请单已经申请的质检信息
    public void updateAllZj(List<ApplicationContent> applicationContents,List<Product>products,List<FinProduct> finProducts,List<Log>logs,List<Inspection>inspectionList){

        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
        RelationshipMapper mapper3 = sqlSession.getMapper(RelationshipMapper.class);
        InspectionMapper mapper2 = sqlSession.getMapper(InspectionMapper.class);
        TakeOrderMapper mapper4 = sqlSession.getMapper(TakeOrderMapper.class);
        ExamineMapper mapper5 = sqlSession.getMapper(ExamineMapper.class);



        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        //调用mapper
        if (applicationContents.size()>0){
            mapper.updateActual(applicationContents);
        }

        //查询关联的订购单类型
        List<ApplicationForm> applicationForms = mapper3.selectOldByNew(applicationContents.get(0).getApplicationId());
        for (int i = 0; i < applicationForms.size(); i++) {
            int id = applicationForms.get(i).getId();

            boolean b = mapper.selectComplete(id);

            if (!b){
                //更新申请单为完成状态
                mapper.updateComplete(id);

            }
            else {
                //更新订购单的状态为进行中
                mapper1.updateStatus(0,id);
            }
        }




        for (int i = 0; i < logs.size(); i++) {
            logs.get(i).setDate(formattedTime);
        }
        mapper.addLog(logs);


        //修改申请单状态，修改为质检完成状态

        mapper1.updateStatus(1,applicationContents.get(0).getApplicationId());
        //更新质检信息

//        mapper2.updateList(inspectionList);

        //要添加入库单（该入库单不需要任何审核）
       //查询申请单信息
        List<ApplicationForm> applicationForms1 = mapper1.selectById(applicationContents.get(0).getApplicationId());

        //查询的质检单信息，现在要生成入库单信息
        ApplicationForm applicationForm = applicationForms1.get(0);
        int id2 = applicationForm.getId();//旧的ID
        applicationForm.setId(0);
        applicationForm.setSort(0);
        applicationForm.setSortTwo(9);
        applicationForm.setDate(formattedTime);
        applicationForm.setCompleteStatus(3);
        long l = System.currentTimeMillis();
        //入库
        applicationForm.setOrderNumber("RK_"+l);
        applicationForm.setCirculationBoss(1);
        mapper1.add(applicationForm);
        int id = applicationForm.getId();//新的ID
        System.out.println(id);
        //添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);
        mapper4.addTakeOrder(takeOrder);
        System.out.println("这里呢");

        for (int i = 0; i < applicationContents.size(); i++) {
//            Inspection inspection = inspectionList.get(i);
//            int productId = inspection.getProductId();
//            int vault = inspection.getVault();
//            int pass = inspection.getPass();
            ApplicationContent applicationContent = applicationContents.get(i);
//            if (pass==2 && productId==applicationContent.getProductId() && vault==applicationContent.getVault()){
//                applicationContents.remove(applicationContent);
//                System.out.println("remove");
//            }
            applicationContent.setAppNumber(applicationContent.getActualNumber());
            applicationContent.setActualNumber(0);
            applicationContent.setApplicationId(id);
            applicationContent.setBomTitleId(applicationContent.getId());
        }
        System.out.println("这里了吗"+applicationContents);

        //添加申请单物料信息
        mapper.add(applicationContents);
        List<Relationship> relationshipList = new ArrayList<>();
        for (int i = 0; i < applicationContents.size(); i++) {
            int id1 = applicationContents.get(i).getId();
            int bomTitleId = applicationContents.get(i).getBomTitleId();
            Relationship relationship = new Relationship(0,id2, id,bomTitleId,id1,applicationContents.get(i).getAppNumber());
            relationshipList.add(relationship);
        }

        mapper3.addRelationshipList(relationshipList);
        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);

            examine.setMinister(1);
        mapper5.addExamine(examine);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //更新质检的状态（只更新让步接收）
    public void updateZbjs(List<Inspection>inspectionList){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InspectionMapper mapper2 = sqlSession.getMapper(InspectionMapper.class);
        //调用mapper
        //更新质检信息
        mapper2.updateList(inspectionList);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }




//    //更新订单信息（撤销后提交的然后删除之前的，最后添加现在的）
//   public void updateByProveAll(ApplicationForm applicationForm,int applicationId,List<ApplicationContent>applicationContents){
//        //获取session
//       SqlSession sqlSession = factory.openSession();
//       //获取mapper
//       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
//       //调用mapper
//       mapper.updateByProve(applicationForm);
//       mapper.deleteByProve(applicationId);
//       mapper.add(applicationContents);
//       //提交事务
//       sqlSession.commit();
//       //释放资源
//       sqlSession.close();
//   }


    //根据id查询存放位置（电子仓库）
   public List<Map<String,Object>> selectLocationDz(List<ApplicationContent>applicationContents){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectLocationDz(applicationContents);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //根据id查询存放位置（产品仓库）
    public List<Map<String,Object>> selectLocationCp(List<ApplicationContent>applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
//        System.out.println(applicationContents);
        List<Map<String, Object>> maps = mapper.selectLocationCp(applicationContents);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询申请单中产品的属性信息(电子仓库)
    public List<Map<String,Object>>selectAttDz(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAttDz(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询申请单中产品的属性信息(电子仓库)
    public List<Map<String,Object>>selectAttCp(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAttCp(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询该申请单要制造什么
    public List<Map<String,Object>>selectWhatsThis(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectWhatsThis(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }




    //点击查看自己的申请单复原过程-------------------------------------------------------------
//1.查询lastlevel=0并且fin_product_id=0的产品
    public List<Map<String,Object>> selectOneLevel(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<ApplicationContent> applicationContents = mapper.selectOneLevel(applicationId);

       if (applicationContents.size()>0){
           List<Map<String, Object>> maps = mapper.selectProductByIdVault(applicationContents,applicationId);
           //释放资源
           sqlSession.close();
           //返回值
           return maps;
       }
       else {

           sqlSession.close();
           return new ArrayList<>();
       }

    }

//2.查询lastlevel=0并且fin_product_id!=0的产品(这里查询的都是已经展开的，所以肯定是成品库中的)
    public List<Map<String,Object>> selectOneLevelZk(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<ApplicationContent> applicationContents = mapper.selectOneLevelZk(applicationId);
        if (applicationContents.size()>0){
            List<Map<String, Object>> maps = mapper.selectProductByIdVaultZk(applicationContents, applicationId);
            //释放资源
            sqlSession.close();
            //返回值
            return maps;
        }
        else {
            sqlSession.close();
            return new ArrayList<>();
        }
    }


    //查询展开里面的内容
    public List<Map<String,Object>>selectProductByIdZkContent( int finProductId, int applicationId,int lastLevel){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectProductByIdZkContent(finProductId, applicationId,lastLevel);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询可以展开的产品
    public List<Map<String,Object>> selectCanZk(int finProductId,int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectCanZk(finProductId, applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }








    //查询该申请单中是否存在已经有出库信息的
    public boolean selectIfActualNumber(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        boolean b = mapper.selectIfActualNumber(applicationId);
        //释放资源
        sqlSession.close();
        //返回只
        return b;
    }
    //查询申请单的所有物料信息单价
   public List<Map<String,Object>> selectPrice(List<ApplicationContent>applicationContents){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       List<Map<String, Object>> maps = mapper.selectPrice(applicationContents);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }







    //    根据产品id，仓库，申请单id查询这个申请单中的入库信息
    public List<ApplicationContent> selectRkNumberById(int applicationId,int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<ApplicationContent> applicationContents = mapper.selectRkNumberById(applicationId, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return applicationContents;
    }


    //根据申请ID 更新app_price
    public void  updateAppPrice(List<ApplicationContent> applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        mapper.updateAppPrice(applicationContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //根据申请单ID   只更新申请数量
    public void  updateAppNumber(List<ApplicationContent> applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        mapper.updateAppNumber(applicationContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }




    //查询采购单生成的所有采购入库单的总和
    public List<Map<String,Object>> selectCgAndRkSum(int appFormId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectCgAndRkSum(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //查询该申请单中的全部内容
    public List<ApplicationContent>selectAll(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<ApplicationContent> applicationContents = mapper.selectAll(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return applicationContents;
    }

    //查询该申请单是否已经完成了(返回true表示该申请单未完成)
    public  boolean selectIfComplete(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        boolean b = mapper.selectIfComplete(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询是否已经存在出入库情况
   public boolean ifActualNumber( int applicationId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
       //调用mapper
       boolean b = mapper.selectIfActualNumber(applicationId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查询所有的申请单数据（两个仓库的）
    public List<Map<String,Object>>selectByAppIdAllVault(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectByAppIdAllVault(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询质检单的质检信息
    public List<Map<String,Object>> selectZjAndAppContent(int applicationId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationContentMapper mapper = sqlSession.getMapper(ApplicationContentMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectZjAndAppContent(applicationId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }












    // ===================== 主表操作 =====================
    // ======================== 查询（不需要commit） ========================
    @Override
    public ApplicationContent getContentById(Integer id) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        ApplicationContent content = mapper.selectByIdCg(id);
        session.close();
        return content;
    }

    @Override
    public ApplicationDetail getDetailByContentId(Integer contentId) {
        SqlSession session = factory.openSession();
        ApplicationDetailMapper mapper = session.getMapper(ApplicationDetailMapper.class);
        ApplicationDetail detail = mapper.selectByContentId(contentId);
        session.close();
        return detail;
    }

    @Override
    public List<ApplicationReceiveBatch> getBatchList(Integer contentId) {
        SqlSession session = factory.openSession();
        ApplicationReceiveBatchMapper mapper = session.getMapper(ApplicationReceiveBatchMapper.class);
        List<ApplicationReceiveBatch> list = mapper.selectBatchByContentId(contentId);
        session.close();
        return list;
    }

    @Override
    public BigDecimal countTotalReceiveNum(Integer contentId) {
        SqlSession session = factory.openSession();
        ApplicationReceiveBatchMapper mapper = session.getMapper(ApplicationReceiveBatchMapper.class);
        BigDecimal num = mapper.sumReceiveNum(contentId, 0);
        session.close();
        return num == null ? BigDecimal.ZERO : num;
    }

    // ======================== 新增/修改/删除（必须 commit） ========================
    @Override
    public int saveContent(ApplicationContent content) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        int rows = mapper.insert(content);
        session.commit(); // ✅ 提交事务
        session.close();  // ✅ 关闭连接
        return rows;
    }

    @Override
    public int editContent(ApplicationContent content) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        int rows = mapper.update(content);
        session.commit();
        session.close();
        return rows;
    }

    @Override
    public int saveDetail(ApplicationDetail detail) {
        SqlSession session = factory.openSession();
        ApplicationDetailMapper mapper = session.getMapper(ApplicationDetailMapper.class);
        int rows = mapper.insert(detail);
        session.commit();
        session.close();
        return rows;
    }

    @Override
    public int editDetail(ApplicationDetail detail) {
        SqlSession session = factory.openSession();
        ApplicationDetailMapper mapper = session.getMapper(ApplicationDetailMapper.class);
        int rows = mapper.update(detail);
        session.commit();
        session.close();
        return rows;
    }

    @Override
    public void addReceiveBatch(ApplicationReceiveBatch batch) {
        SqlSession session = factory.openSession();
        ApplicationReceiveBatchMapper mapper = session.getMapper(ApplicationReceiveBatchMapper.class);
        int rows = mapper.insert(batch);
        session.commit();  // ✅ 提交
        session.close();   // ✅ 关闭

        // 同步实际数量
        syncActualNumber(batch.getContentId());
    }

    @Override
    public void syncActualNumber(Integer contentId) {
        BigDecimal total = countTotalReceiveNum(contentId);
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        mapper.updateActualNum(contentId, total);
        session.commit();  // ✅ 修改必须提交
        session.close();   // ✅ 必须关闭
    }



    @Override
    public List<Map<String, Object>> getAppAndDetail(Integer contentId) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        List<Map<String, Object>> list = mapper.getAppAndDetail(contentId);
        session.close();
        return list;
    }



    @Override
    public PageBean<Map<String, Object>> selectAppAndDetailPage(Object currentPage, Object pageSize) {

        // ===================== 修复核心：Object 转 int =====================
        int currPage = 1;
        int size = 100;

        if (currentPage != null && !"".equals(currentPage)) {
            currPage = Integer.parseInt(currentPage.toString());
        }
        if (pageSize != null && !"".equals(pageSize)) {
            size = Integer.parseInt(pageSize.toString());
        }

        // 默认值处理
        if (size <= 0) {
            size = 100;
        }
        if (currPage <= 0) {
            currPage = 1;
        }

        // 计算起始位置
        int start = (currPage - 1) * size;

        // ===================== 以下保持你原来的格式 =====================
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);

        List<Map<String, Object>> rows = mapper.selectAppAndDetailByPage(start, size);
        int totalCount = mapper.selectTotalCount();

        session.close();

        PageBean<Map<String, Object>> pageBean = new PageBean<>();
        pageBean.setRows(rows);
        pageBean.setTotalCount(totalCount);

        return pageBean;
    }



    @Override
    public void autoSaveData(Map<String, Object> saveMap) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper contentMapper = session.getMapper(ApplicationContentMapper.class);
        ApplicationDetailMapper detailMapper = session.getMapper(ApplicationDetailMapper.class);

        // 1. 更新主表
        contentMapper.autoSaveApplicationContent(saveMap);
        Integer contentId = Integer.valueOf(saveMap.get("id").toString());

        try {
            // 2. 直接更新！
            // 如果更新影响行数 = 0 → 说明没有这条数据 → 再插入
            int rows = detailMapper.updateDetail(saveMap);
            if (rows == 0) {
                saveMap.put("application_content_id", contentId);
                detailMapper.insertDetail(saveMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.commit();
        session.close();
    }

    @Override
    public Integer copyRowData(Integer oldId, Integer newNum) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper contentMapper = session.getMapper(ApplicationContentMapper.class);
        ApplicationDetailMapper detailMapper = session.getMapper(ApplicationDetailMapper.class);

        ApplicationContent applicationContent = contentMapper.selectByIdCg(oldId);
        applicationContent.setAppNumber(newNum);
        System.out.println(applicationContent);


        contentMapper.insert(applicationContent);
        System.out.println(applicationContent);
        Integer newId = applicationContent.getId();
        System.out.println("newId"+newId);
        detailMapper.insertCopyDetail(oldId,newId);
        System.out.println("ffffggg");
        session.commit();
        session.close();
        return newId;
    }

    @Override
    public void editApplicationNum(Integer id, Integer appNum,Integer addNum,String date) {
        SqlSession session = factory.openSession();
        ApplicationContentMapper mapper = session.getMapper(ApplicationContentMapper.class);
        ApplicationDetailMapper applicationDetailMapper = session.getMapper(ApplicationDetailMapper.class);
        mapper.updateApplicationNumber(id,appNum);
        ApplicationContent applicationContent = mapper.selectByIdCg(id);
        applicationContent.setAppNumber(addNum);
        mapper.insert(applicationContent);
        Integer newId = applicationContent.getId();
        applicationDetailMapper.insertCopyDetail(id,newId);
        session.commit();
        session.close();
    }
}
