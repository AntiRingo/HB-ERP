package com.hongbang.service.impl;

import com.alibaba.fastjson.JSON;
import com.hongbang.mapper.*;
import com.hongbang.pojo.*;
import com.hongbang.service.InspectionService;
import com.hongbang.util.Compare;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InspectionServiceImpl implements InspectionService {

//    private InspectionMapper inspectionMapper;



    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加
    public void add(List<Inspection> inspection){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InspectionMapper mapper = sqlSession.getMapper(InspectionMapper.class);
        //调用mapper
        mapper.add(inspection);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //通过或者不通过
    public void updatePass(Inspection inspection){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InspectionMapper mapper = sqlSession.getMapper(InspectionMapper.class);
        //调用mapper
        mapper.updatePass(inspection);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //已经质检过的质检单查询质检信息
    public List<Inspection> selectZjInformation(int appId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        InspectionMapper mapper = sqlSession.getMapper(InspectionMapper.class);
        //调用mapper
        List<Inspection> inspectionList = mapper.selectZjInformation(appId);
        //释放资源
        sqlSession.close();
        //返回值
        return inspectionList;
    }










    // 申请单状态常量
    private static final int STATUS_COMPLETED = 1;      // 已完成
    private static final int STATUS_REJECTED = 2;       // 已拒绝
    private static final int STATUS_NEW = 3;            // 新增
    private static final int STATUS_CANCELLED = 4;      // 已取消
    private static final int STATUS_PENDING = 5;        // 待处理
    private static final int STATUS_SIGN_PENDING = 6;   // 待签字



    @Override
    public Map<String, Object> getMaterialQCInspectionRecords(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int contentId = Integer.parseInt(params.get("content_id").toString());

            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("contentId", contentId);

            List<Map<String, Object>> records = inspectionMapper.getQCRecordsByContent(contentId);

            result.put("status", "success");
            result.put("data", records);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取记录失败: " + e.getMessage());
        }
sqlSession.close();;
        return result;
    }

    @Override
    public Map<String, Object> getMaterialQCInspectionStatus(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int contentId = Integer.parseInt(params.get("content_id").toString());

            // 获取物料质检详情
            Map<String, Object> qcDetails = inspectionMapper.selectQCDetailsByContent(contentId);

            if (qcDetails == null || qcDetails.isEmpty()) {
                result.put("status", "error");
                result.put("message", "物料不存在");
                return result;
            }

            // 获取质检统计
            Map<String, Object> qcStats = inspectionMapper.selectQCStats(contentId);

            // 组装返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("content_id", contentId);

            // 获取物料信息
            Object productIdObj = qcDetails.get("product_id");
            Object productNameObj = qcDetails.get("product_name");
            Object materialNumberObj = qcDetails.get("material_number");

            if (productIdObj != null) {
                data.put("product_id", productIdObj);
            }
            if (productNameObj != null) {
                data.put("product_name", productNameObj);
            }
            if (materialNumberObj != null) {
                data.put("material_number", materialNumberObj);
            }

            // 获取数量信息
            Object appNumberObj = qcDetails.get("app_number");
            Object actualNumberObj = qcDetails.get("actual_number");
            Object qcRequestedObj = qcDetails.get("qc_requested_quantity");
            Object qcAvailableObj = qcDetails.get("qc_available_quantity");
            Object qcCountObj = qcDetails.get("qc_request_count");

            double appNumber = appNumberObj != null ? Double.parseDouble(appNumberObj.toString()) : 0;
            double actualNumber = actualNumberObj != null ? Double.parseDouble(actualNumberObj.toString()) : 0;
            double qcRequested = qcRequestedObj != null ? Double.parseDouble(qcRequestedObj.toString()) : 0;
            double qcAvailable = qcAvailableObj != null ? Double.parseDouble(qcAvailableObj.toString()) : actualNumber;
            int qcCount = qcCountObj != null ? Integer.parseInt(qcCountObj.toString()) : 0;

            data.put("required_quantity", appNumber);
            data.put("actual_arrived_quantity", actualNumber);
            data.put("qc_requested_quantity", qcRequested);
            data.put("qc_available_quantity", qcAvailable);
            data.put("qc_request_count", qcCount);

            if (qcStats != null && !qcStats.isEmpty()) {
                Object totalQCRequestedObj = qcStats.get("total_qc_requested");
                Object totalQCCompletedObj = qcStats.get("total_qc_completed");
                Object qcAppCountObj = qcStats.get("qc_application_count");

                double totalQCRequested = totalQCRequestedObj != null ? Double.parseDouble(totalQCRequestedObj.toString()) : 0;
                double totalQCCompleted = totalQCCompletedObj != null ? Double.parseDouble(totalQCCompletedObj.toString()) : 0;
                int qcAppCount = qcAppCountObj != null ? Integer.parseInt(qcAppCountObj.toString()) : 0;

                data.put("total_qc_requested", totalQCRequested);
                data.put("total_qc_completed", totalQCCompleted);
                data.put("qc_application_count", qcAppCount);
            } else {
                data.put("total_qc_requested", 0);
                data.put("total_qc_completed", 0);
                data.put("qc_application_count", 0);
            }

            // 计算质检进度
            double progressPercent = 0;
            if (actualNumber > 0) {
                progressPercent = (qcRequested / actualNumber) * 100;
            }
            data.put("progress_percent", progressPercent);

            result.put("status", "success");
            result.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取状态失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }

    @Override
    public Map<String, Object> getAvailableQCQuantity(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int contentId = Integer.parseInt(params.get("content_id").toString());

            // 获取可申请质检数量
            Map<String, Object> availableData = inspectionMapper.selectAvailableQCQuantity(contentId);

            if (availableData == null || availableData.isEmpty()) {
                // 如果没有记录，尝试从application_content获取
                Double actualNumber = inspectionMapper.getActualNumber(contentId);
                Double appNumber = inspectionMapper.getAppNumber(contentId);

                if (actualNumber == null) {
                    result.put("status", "error");
                    result.put("message", "物料不存在");
                    return result;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("actual_number", actualNumber != null ? actualNumber : 0);
                data.put("app_number", appNumber != null ? appNumber : 0);
                data.put("total_qc_requested", 0);
                data.put("available_quantity", actualNumber != null ? actualNumber : 0);

                result.put("status", "success");
                result.put("data", data);
            } else {
                result.put("status", "success");
                result.put("data", availableData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取数量失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }

    @Override
    public Map<String, Object> getQCRequestedQuantity(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int contentId = Integer.parseInt(params.get("content_id").toString());

            double requestedQuantity = inspectionMapper.selectQCRequestedQuantity(contentId);

            result.put("status", "success");
            result.put("requested_quantity", requestedQuantity);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取数量失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }

    // ... 其他方法保持不变，但确保使用正确的字段名
    // getQCApplications, getQCApplicationDetail, updateQCApplicationStatus, getQCProgressStats

    @Override
    public Map<String, Object> getQCApplications(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            List<Map<String, Object>> qcApplications = inspectionMapper.selectQCApplications();

            result.put("status", "success");
            result.put("data", qcApplications);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取质检单列表失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }

    @Override
    public Map<String, Object> getQCApplicationDetail(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int qcApplicationId = Integer.parseInt(params.get("qc_application_id").toString());

            // 获取质检单基本信息
            Map<String, Object> application = inspectionMapper.selectQCApplicationById(qcApplicationId);

            if (application == null) {
                result.put("status", "error");
                result.put("message", "质检申请单不存在");
                return result;
            }

            // 获取质检单物料列表
            List<Map<String, Object>> materials = inspectionMapper.selectQCMaterialsByApplication(qcApplicationId);

            // 计算统计信息
            double totalQCQuantity = 0;
            int materialCount = materials != null ? materials.size() : 0;
            int completedCount = 0;

            if (materials != null) {
                for (Map<String, Object> material : materials) {
                    Object qcQuantityObj = material.get("qc_quantity");
                    if (qcQuantityObj != null) {
                        totalQCQuantity += Double.parseDouble(qcQuantityObj.toString());
                    }
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("application", application);
            data.put("materials", materials);
            data.put("total_qc_quantity", totalQCQuantity);
            data.put("material_count", materialCount);
            data.put("completed_count", completedCount);

            result.put("status", "success");
            result.put("data", data);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取质检单详情失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }

    @Override

    public Map<String, Object> updateQCApplicationStatus(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            int qcApplicationId = Integer.parseInt(params.get("qc_application_id").toString());
            int status = Integer.parseInt(params.get("status").toString());
            String notes = params.getOrDefault("notes", "").toString();
            int operatorId = Integer.parseInt(params.get("operator_id").toString());

            // 检查质检单是否存在
            int exists = inspectionMapper.checkQCApplicationExists(qcApplicationId);
            if (exists == 0) {
                result.put("status", "error");
                result.put("message", "质检申请单不存在");
                return result;
            }

            // 验证状态值
            if (status < 1 || status > 6) {
                result.put("status", "error");
                result.put("message", "状态值无效");
                return result;
            }

            // 更新质检单状态
            int updateResult = inspectionMapper.updateQCApplicationStatus(qcApplicationId, status, notes);

            if (updateResult > 0) {
                result.put("status", "success");
                result.put("message", "状态更新成功");

                // 返回更新后的信息
                Map<String, Object> application = inspectionMapper.selectQCApplicationById(qcApplicationId);
                result.put("data", application);
            } else {
                result.put("status", "error");
                result.put("message", "状态更新失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        }
sqlSession.commit();
        sqlSession.close();
        return result;
    }

    @Override
    public Map<String, Object> getQCProgressStats(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            Map<String, Object> stats = inspectionMapper.selectQCProgressStats();

            if (stats == null) {
                stats = new HashMap<>();
                stats.put("total_applications", 0);
                stats.put("completed_applications", 0);
                stats.put("pending_applications", 0);
                stats.put("total_qc_quantity", 0);
                stats.put("total_materials", 0);
            }

            result.put("status", "success");
            result.put("data", stats);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取统计信息失败: " + e.getMessage());
        }
sqlSession.close();
        return result;
    }



    @Override

    public Map<String, Object> batchRequestQCInspection(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        ApplicationContentMapper contentMapper = sqlSession.getMapper(ApplicationContentMapper.class);
        TakeOrderMapper takeOrderMapper = sqlSession.getMapper(TakeOrderMapper.class);
        RelationshipMapper relationshipMapper = sqlSession.getMapper(RelationshipMapper.class);
        ApplicationFormMapper applicationFormMapper = sqlSession.getMapper(ApplicationFormMapper.class);

        try {
            List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");
            int operatorId = Integer.parseInt(params.get("operator_id").toString());
            String notes = params.getOrDefault("notes", "").toString();

            if (items == null || items.isEmpty()) {
                result.put("status", "error");
                result.put("message", "请选择要申请质检的物料");
                return result;
            }

           User operatorInfo = userMapper.selectById(operatorId);
            if (operatorInfo == null) {
                result.put("status", "error");
                result.put("message", "操作人不存在");
                return result;
            }

            String operatorName = operatorInfo.getName() != null ?
                    operatorInfo.getName() : "未知用户";

            // 检查是否有待处理的质检申请单
            Integer qcApplicationId = null;
//            Map<String, Object> pendingQC = inspectionMapper.getPendingQCApplication();
//
//            if (pendingQC != null && pendingQC.get("id") != null) {
//                qcApplicationId = Integer.parseInt(pendingQC.get("id").toString());
//
//            }
//            else {
                // 创建新的质检申请单
                String orderNumber = inspectionMapper.generateQCOrderNumber();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = LocalDateTime.now().format(formatter);

                long l = System.currentTimeMillis();



                    orderNumber = "ZJ_"+l;


                Map<String, Object> qcAppParams = new HashMap<>();
                qcAppParams.put("orderNumber", orderNumber);
                qcAppParams.put("userId", operatorId);
                qcAppParams.put("notes", "批量质检申请单");
                qcAppParams.put("data", formattedTime);
                qcAppParams.put("sort",4);
            qcAppParams.put("sortTwo",16);
            qcAppParams.put("reason","");
            qcAppParams.put("circulation",27);


                int insertResult1 = inspectionMapper.insertQCApplication(qcAppParams);
                if (insertResult1 > 0 && qcAppParams.get("id") != null) {
                    qcApplicationId = Integer.parseInt(qcAppParams.get("id").toString());
                } else {
                    result.put("status", "error");
                    result.put("message", "创建质检申请单失败");
                    return result;
                }

                //添加接单信息
            TakeOrder takeOrder = new TakeOrder(0, qcApplicationId,0, 0);

                takeOrderMapper.addTakeOrder(takeOrder);
                //添加申请单内容信息
            List<ApplicationContent> applicationContentList = new ArrayList<>();


//            }

            // 验证每个物料的可申请数量
            List<Map<String, Object>> validItems = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();
//  id:hbZjData[i].id,
//                applicationId:hbZjData[i].application_id,
//                productId:hbZjData[i].product_id,
//                appNumber:wait[i].value,
//                actualNumber: hbZjData[i].actual_number,//实际出/入库数量
//                // actualNumber:document.getElementById(""+hbZjData[i].product_id+hbZjData[i].vault+"").value,
//                vault:hbZjData[i].vault,
//                appPrice:rkP[i].value,
//                finProductId:"",
//                finProductNumber:"",
//                lastLevel:"",
//                bomTitleId:hbZjData[i].id,//暂时记录在这里
            for (Map<String, Object> item : items) {
                System.out.println(item);
                int contentId = Integer.parseInt(item.get("content_id").toString());
                double qcQuantity = Double.parseDouble(item.get("qc_quantity").toString());
                int oldAppId=Integer.parseInt(item.get("old_appFormId").toString());

                // 获取物料信息
                Map<String, Object> contentInfo = contentMapper.selectById(contentId);
                if (contentInfo == null) {
                    errorMessages.add("物料ID " + contentId + " 不存在");
                    continue;
                }

                // 获取实际数量
                Double actualNumberObj = inspectionMapper.getActualNumber(contentId);
                double actualNumber = actualNumberObj != null ? actualNumberObj : 0;

                if (actualNumber <= 0) {
                    errorMessages.add("物料ID " + contentId + " 尚未到货");
                    continue;
                }

                // 获取可申请质检数量
                Map<String, Object> availableData = inspectionMapper.selectAvailableQCQuantity(contentId);
                double availableQuantity = 0;

                if (availableData != null && availableData.get("available_quantity") != null) {
                    availableQuantity = Double.parseDouble(availableData.get("available_quantity").toString());
                } else {
                    availableQuantity = actualNumber;
                }

                if (qcQuantity > availableQuantity) {
                    errorMessages.add("物料ID " + contentId + " 申请数量超过可申请数量");
                    continue;
                }

                if (qcQuantity > actualNumber) {
                    errorMessages.add("物料ID " + contentId + " 申请数量超过实际到货数量");
                    continue;
                }

                // 添加到有效列表
                Map<String, Object> validItem = new HashMap<>();
                validItem.put("content_id", contentId);
                validItem.put("qc_application_id", qcApplicationId);
                validItem.put("operator_id", operatorId);
                validItem.put("qc_quantity", qcQuantity);
                validItems.add(validItem);


                ApplicationContent applicationContent = new ApplicationContent(Integer.parseInt(contentInfo.get("id").toString()),qcApplicationId,Integer.parseInt(contentInfo.get("product_id").toString()),qcQuantity,contentId,Integer.parseInt(contentInfo.get("vault").toString()),Double.parseDouble(contentInfo.get("app_price").toString()),0,0,0,oldAppId);
            applicationContentList.add(applicationContent);
            }


            // 使用 Map 进行分组，key 是 productId 和 vault 的组合
            Map<String, ApplicationContent> mergedMap = new HashMap<>();

            for (ApplicationContent item : applicationContentList) {
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


            //添加申请单信息
            contentMapper.add(mergedList);

            //要更新关系网后再设置appId因为现在是申请内容的原申请单Id
            List<Relationship> relationships = new ArrayList<>();
            for (int i = 0; i < applicationContentList.size(); i++) {
                int applicationId = applicationContentList.get(i).getApplicationId();
                int id1 = applicationContentList.get(i).getId();
                double actualNumber = applicationContentList.get(i).getActualNumber();//这里是旧的applicationcontentID

                Relationship relationship = new Relationship(0,applicationContentList.get(i).getBomTitleId(),applicationId, (int) actualNumber,id1,applicationContentList.get(i).getAppNumber());
                relationships.add(relationship);
            }

            //建立质检单其他内容
            List<Inspection> inspectionList = new ArrayList<>();
            for (int i = 0; i < mergedList.size(); i++) {
                ApplicationContent applicationContent = mergedList.get(i);
                Inspection inspection =new Inspection(0,qcApplicationId,applicationContent.getProductId(),applicationContent.getVault(),0,"",0);
                inspectionList.add(inspection);
            }

            inspectionMapper.add(inspectionList);

            relationshipMapper.addRelationshipList(relationships);

            //查询质检单信息
            List<ApplicationContent> applicationContents = contentMapper.selectAll(qcApplicationId);


            //更新bomtitleid为0
            contentMapper.updateBomTitleId(applicationContents);
            //更新actualNumber为0
            contentMapper.updateActualNumberZero(applicationContents);



            if (validItems.isEmpty()) {
                result.put("status", "error");
                result.put("message", "没有有效的申请项");
                if (!errorMessages.isEmpty()) {
                    result.put("errors", errorMessages);
                }
                return result;
            }

            // 批量插入质检申请记录
            int insertResult = inspectionMapper.batchInsertQCRequest(validItems);

            if (insertResult > 0) {
                result.put("status", "success");
                result.put("message", "批量申请质检成功");
                result.put("qc_application_id", qcApplicationId);
                result.put("success_count", validItems.size());
                result.put("total_count", items.size());

                if (!errorMessages.isEmpty()) {
                    result.put("errors", errorMessages);
                }

                // 获取质检申请单信息
                Map<String, Object> qcApplication = inspectionMapper.selectQCApplicationById(qcApplicationId);
                if (qcApplication != null) {
                    result.put("qc_application", qcApplication);
                }
            } else {
                result.put("status", "error");
                result.put("message", "批量申请质检失败");
            }
            int level = operatorInfo.getLevel();
            //设置申请审核表
            Examine examine = new Examine();
            examine.setAppFormId(qcApplicationId);
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
            List<ExamineAllType> examineAllTypes = mapper3.selectStep(16);
            List<ExamineStepContent>examineStepContents = new ArrayList<>();


            String s = JSON.toJSONString(qcAppParams);
            ApplicationForm applicationForm = JSON.parseObject(s, ApplicationForm.class);

            //获取申请单的总价格
            List<Map<String, Object>> maps = applicationFormMapper.selectPriceAndCount(Collections.singletonList(applicationForm));


            Object totalPrice = maps.get(0).get("totalPrice");

            //获取申请单所有的单价

            List<Map<String, Object>> unitPrice = contentMapper.selectPrice(applicationContentList);



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
                    applicationFormMapper.updateCirculation(departmentId,qcApplicationId);

                }

                //查询审核步骤是否都已通过
                boolean complete=true;
//            List<Examine> examines = mapper2.selectByAppId(id);
                List<Map<String, Object>> maps1 = mapper4.selectByAppId(qcApplicationId);

                for (int i = 0; i < maps1.size(); i++) {
                    Map<String, Object> map = maps1.get(i);
                    Object result1 = map.get("result");
                    if (!result1.equals(1)){
                        complete=false;
                        break;
                    }
                }
                if (maps1.size()>0){
                    //存在需要审核的步骤
                    if (complete){
                        //审核都已通过
                        applicationFormMapper.updateCirculationBoss(qcApplicationId);
                    }
                }
                else {
                    //不存在需要审核的步骤,确保审核直接通过添加了level == 4
                    if (level==3 || level==2 || level ==4){
                        applicationFormMapper.updateCirculationBoss(qcApplicationId);
                    }
                }



            }
            else {

                //没有步骤，又是部长或总经理审核的，直接完成申请单到仓库去
                //确保审核直接通过添加了level == 4
                if (level==3 || level==2||level==4){
                    applicationFormMapper.updateCirculationBoss(qcApplicationId);

                }
                mapper2.addExamine(examine);

            }


        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        }
        sqlSession.commit();
        sqlSession.close();
        return result;
    }

    @Override
    public Map<String, Object> batchGetAvailableQCQuantity(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        SqlSession sqlSession = factory.openSession();
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);
        try {
            List<Integer> contentIds = (List<Integer>) params.get("content_ids");

            if (contentIds == null || contentIds.isEmpty()) {
                result.put("status", "error");
                result.put("message", "请选择物料");
                return result;
            }

            List<Map<String, Object>> availableData = inspectionMapper.batchSelectAvailableQCQuantity(contentIds);

            result.put("status", "success");
            result.put("data", availableData);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
            result.put("message", "获取批量可申请数量失败: " + e.getMessage());
        }
sqlSession.commit();
        sqlSession.close();
        return result;
    }
}
