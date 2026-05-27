package com.hongbang.service.impl;

import com.hongbang.mapper.*;
import com.hongbang.pojo.*;
import com.hongbang.service.MaterialReturnReceiveService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MaterialReturnReceiveServiceImpl implements MaterialReturnReceiveService {

    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    public int saveReturnReceive(MaterialReturnReceive receive) {
        SqlSession sqlSession = factory.openSession();
        MaterialReturnReceiveMapper receiveMapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        ApplicationContentMapper applicationContentMapper = sqlSession.getMapper(ApplicationContentMapper.class);
        RelationshipMapper relationshipMapper = sqlSession.getMapper(RelationshipMapper.class);
        ProcurementOperationsMapper procurementOperationsMapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
        ApplicationFormMapper applicationFormMapper = sqlSession.getMapper(ApplicationFormMapper.class);
        InspectionMapper inspectionMapper = sqlSession.getMapper(InspectionMapper.class);

        int insert = 0;
        try {
            // 1. 插入退货接收记录
            insert = receiveMapper.insert(receive);
            Integer handleType = receive.getHandleType();

            // ==============================================
            // 处理退货逻辑
            // ==============================================
            if (handleType != null && handleType == 1) {

                int continuePurchase;
                //这里先设置为继续接受的，只是修改暂时使用，不涉及到数据库修改
                continuePurchase=1;
                Integer applicationContentId = receive.getApplicationContentId();//这里是质检单中的内容ID
                double returnQty = receive.getReturnQty().doubleValue();

                // 查询质检单明细
                ApplicationContent applicationContent = applicationContentMapper.selectByContentId(applicationContentId);
                if (applicationContent == null) {
                    throw new RuntimeException("未找到对应的申请单明细，ID：" + applicationContentId);
                }

                int newAppId = applicationContent.getApplicationId();//质检单ID
                int productId = applicationContent.getProductId();
                int vault = applicationContent.getVault();

                // 获取申请单时间
                String date = null;
                List<ApplicationForm> applicationForms = applicationFormMapper.selectById(newAppId);
                if (applicationForms != null && !applicationForms.isEmpty()) {
                    date = applicationForms.get(0).getDate();
                }

                // ==========================================
                // 【继续购买 = 1】：扣采购单actualNumber + 扣到货记录
                // ==========================================
                {
                    double remainReturnQty = returnQty;

//                    // 更新质检单的申请数量
//                    applicationContentMapper.updateReturnNumber(BigDecimal.valueOf(returnQty), applicationContentId);


                    // 扣 订购单实际数量
                    List<Relationship> relationships = relationshipMapper.selectAllOld(newAppId);//查询关联的订购单
                    if (relationships != null && !relationships.isEmpty()) {
                        for (Relationship relationship : relationships) {
                            if (remainReturnQty <= 0) break;

                            int oldAppFormId = relationship.getOldAppFormId();//获取订购单ID
                            ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
                            if (content == null) continue;

                            double actualNumber = content.getActualNumber();
                            if (actualNumber <= 0) continue;

                            double deduct = Math.min(remainReturnQty, actualNumber);
//                            //修改订购单中的已申请质检数量
//                            applicationContentMapper.updateActualNumber(content.getId(), actualNumber - deduct);
//                            // 更新由订购单生成质检记录的数量
//                            inspectionMapper.updateQuantity(actualNumber - deduct, content.getId(), date);
                            remainReturnQty -= deduct;
                        }
                    }

//                    // 扣 到货记录数量
//                    if (remainReturnQty > 0) {
//                        List<ProcurementOperations> opList = procurementOperationsMapper.selectByContentId(applicationContentId);
//
//                        if (opList != null && !opList.isEmpty()) {
//                            for (ProcurementOperations op : opList) {
//                                if (remainReturnQty <= 0) break;
//
//                                double batchQty = op.getBatchQuantity();
//                                if (batchQty <= 0) continue;
//
//                                double deduct = Math.min(remainReturnQty, batchQty);
//                                op.setBatchQuantity(batchQty - deduct);
//                                procurementOperationsMapper.updateById(op);
//                                remainReturnQty -= deduct;
//                            }
//                        }
//                    }

                    if (remainReturnQty > 0) {
                        throw new RuntimeException("退货数量超过可扣减总数，剩余：" + remainReturnQty);
                    }
                }

                // ==========================================
                // 【不继续购买 = 0】：扣申购单数量 appNumber
                // ==========================================
                if (continuePurchase == 0) {

                    double remainReturnQty = returnQty;
                    double remainReturnQty1 = returnQty;

                    // 获取关系列表
                    List<Relationship> relationships = relationshipMapper.selectAllOld(newAppId);
                    if (relationships != null && !relationships.isEmpty()) {
                        for (Relationship relationship : relationships) {
                            if (remainReturnQty <= 0) break;

                            // ✅ 这里已经改成你正确的字段
                            int oldAppFormId = relationship.getOldAppFormId();//获取订购单的
                            ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
                            System.out.println("appId"+oldAppFormId);
                            if (content == null) continue;

                            double appNumber = content.getAppNumber();
                            System.out.println("appNumber"+appNumber);
                            if (appNumber <= 0) continue;


                            double deduct = Math.min(remainReturnQty, appNumber);
                            System.out.println("deduct"+deduct);
                            applicationContentMapper.updateAppNumberOnce( appNumber - deduct,content.getId());//修改申购单的申请数量（appNumber）

                            remainReturnQty -= deduct;
                        }
                    }

                    if (remainReturnQty > 0) {
                        throw new RuntimeException("退货数量超出申购单可扣减总数，剩余：" + remainReturnQty);
                    }

                    //获取关系表
                    List<Relationship> relationships1 = relationshipMapper.selectSg(newAppId);
                    if (relationships1 != null && !relationships1.isEmpty()) {
                        for (Relationship relationship : relationships1) {
                            if (remainReturnQty1 <= 0) break;

                            // ✅ 这里已经改成你正确的字段
                            int oldAppFormId = relationship.getOldAppFormId();//获取申购单的
                            ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
                            if (content == null) continue;

                            double appNumber = content.getAppNumber();
                            if (appNumber <= 0) continue;
                            double actualNumber = content.getActualNumber();
                            if (actualNumber <= 0) continue;

                            double deduct = Math.min(remainReturnQty1, appNumber);
                            applicationContentMapper.updateAppNumberOnce( appNumber - deduct,content.getId());//修改申购单的申请数量（appNumber）
                            applicationContentMapper.updateActualNumber(content.getId(), actualNumber - deduct);//修改申购单的转化数量（actualNumber）
                            remainReturnQty1 -= deduct;
                        }
                    }

                    if (remainReturnQty1 > 0) {
                        throw new RuntimeException("剩余：" + remainReturnQty1);
                    }
                }
            }

            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            e.printStackTrace();
            throw new RuntimeException("保存退货失败：" + e.getMessage());
        } finally {
            sqlSession.close();
        }
        return insert;
    }


    //查询未分配的信息
    @MapKey("id")
   public PageBean<Map<String,Object>> selectNoFp( int currentPage,  int pageSize){
        //获取mapper
        SqlSession sqlSession = factory.openSession();
        MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;

        List<Map<String, Object>> maps = mapper.selectNoFp(begin, size);
        int i = mapper.selectNoFpCount();
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //根据contentId查询申请人都有谁（现在的contentID是质检单）
    public List<Map<String,Object>> selectAppUser (int contentId, int productId,  int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAppUser(contentId, productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //更新让步接收修改人
    public void updateReceiveUser(int receiveUser,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        //调用mapper
        mapper.updateReceiveUser(receiveUser,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据product_id,vault查询物料信息
    public Product selectProductOne(int id, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        //调用mapper
        Product product = mapper.selectProductOne(id, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return product;
    }


    //查询质检单的退货以及让步情况
   public List<MaterialReturnReceive> selectReturnReceiveByAppId(int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
       //调用mapper
       List<MaterialReturnReceive> materialReturnReceives = mapper.selectReturnReceiveByAppId(id);
       //释放资源
       sqlSession.close();
       //返回值
       return materialReturnReceives;
   }

    //查询所有的退货信息
   public PageBean<Map<String,Object>>selectTh(int currentPage,int pageSize){
       //获取mapper
       SqlSession sqlSession = factory.openSession();
       MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
       int size=pageSize;
       int begin = (currentPage-1)*size;
       List<Map<String,Object>> materialReturnReceives = mapper.selectTh(begin, size);
       int i = mapper.selectThCount();
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
       pageBean.setRows(materialReturnReceives);
       pageBean.setTotalCount(i);

       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;

   }



    //设置退货后是否继续申购物料信息
   public void updateContinue(int status,int id){
        //获取mapper
       SqlSession sqlSession = factory.openSession();
       MaterialReturnReceiveMapper mapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
       ApplicationContentMapper applicationContentMapper = sqlSession.getMapper(ApplicationContentMapper.class);
       RelationshipMapper relationshipMapper = sqlSession.getMapper(RelationshipMapper.class);
       ProcurementOperationsMapper procurementOperationsMapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
       ApplicationFormMapper mapper1 = sqlSession.getMapper(ApplicationFormMapper.class);
       ReturnNumberMapper returnNumberMapper = sqlSession.getMapper(ReturnNumberMapper.class);

       //调用mapper
       mapper.updateContinue(status,id);
       //根据ID查询数据
       MaterialReturnReceive receive = mapper.selectById(id);
       double returnQty = receive.getReturnQty().doubleValue();
       Integer applicationContentId = receive.getApplicationContentId();


       // 查询质检单明细
       ApplicationContent applicationContent = applicationContentMapper.selectByContentId(applicationContentId);
       if (applicationContent == null) {
           throw new RuntimeException("未找到对应的申请单明细，ID：" + applicationContentId);
       }

       int newAppId = applicationContent.getApplicationId();//获取质检单ID
       int productId = applicationContent.getProductId();
       int vault = applicationContent.getVault();
       // ==========================================
       // 【不继续购买 = 0】：扣申购单数量 appNumber
       // ==========================================
       double remainReturnQty = returnQty;
       double remainReturnQty1 = returnQty;
       if (status == 0) {


           // 获取关系列表
           List<Relationship> relationships = relationshipMapper.selectAllOld(newAppId);//根据质检单ID查询订购单信息
           if (relationships != null && !relationships.isEmpty()) {
               for (Relationship relationship : relationships) {
                   if (remainReturnQty <= 0) break;

                   // ✅ 这里已经改成你正确的字段
                   int oldAppFormId = relationship.getOldAppFormId();//获取订购单的ID
                   ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
                   System.out.println("appId"+oldAppFormId);
                   if (content == null) continue;

                   double appNumber = content.getAppNumber();
                   System.out.println("appNumber"+appNumber);
                   if (appNumber <= 0) continue;


                   double deduct = Math.min(remainReturnQty, appNumber);
                   System.out.println("deduct"+deduct);
//                   applicationContentMapper.updateAppNumberOnce( appNumber - deduct,content.getId());//修改订购单的申请数量（appNumber）

                   remainReturnQty -= deduct;
               }
           }

           if (remainReturnQty > 0) {
               throw new RuntimeException("退货数量超出申购单可扣减总数，剩余：" + remainReturnQty);
           }

           //获取关系表
           List<Relationship> relationships1 = relationshipMapper.selectSg(newAppId);//根据质检单查询申购单
           System.out.println(receive);
           System.out.println("zhelia");
//           if (relationships1 != null && !relationships1.isEmpty()) {
//               for (Relationship relationship : relationships1) {
//                   if (remainReturnQty1 <= 0) break;
//
//                   // ✅ 这里已经改成你正确的字段
//                   int oldAppFormId = relationship.getOldAppFormId();//获取申购单的ID
//                   ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
//                   System.out.println(content);
//                   if (content == null) continue;
//
//                   double appNumber = content.getAppNumber();
//                   if (appNumber <= 0) continue;
//                   double actualNumber = content.getActualNumber();
//                   if (actualNumber <= 0) continue;
//
//                   double deduct = Math.min(remainReturnQty1, appNumber);
//
////                   applicationContentMapper.updateAppNumberOnce( appNumber - deduct,content.getId());//修改申购单的申请数量（appNumber）
////                   applicationContentMapper.updateActualNumber(content.getId(), actualNumber - deduct);//修改申购单的转化数量（actualNumber）
//                   remainReturnQty1 -= deduct;
//               }
//           }
//
//           if (remainReturnQty1 > 0) {
//               throw new RuntimeException("剩余：" + remainReturnQty1);
//           }


           //1.根据让步接收数据差寻质检单数据，然后根据质检单数据查询采购单数据，然后修改采购单入库数据
           List<ApplicationContent> cgApplicationContents =relationshipMapper.selectAllAppNumberByRk(newAppId);
           System.out.println(cgApplicationContents);
           for (ApplicationContent cg : cgApplicationContents) {

               int productId1 = cg.getProductId();
               int vault1 = cg.getVault();

               double actualNumber = cg.getActualNumber();
               double appNumber = cg.getAppNumber();

               // 已用让步接收
               double rbUsed =
                       returnNumberMapper.sumByCgIdAndType(2,cg.getId());

               // 已用退货
               double returnUsed =
                       returnNumberMapper.sumByCgIdAndType(1,cg.getId());

               // 还差多少
               double remainNeed =
                       appNumber - (actualNumber + rbUsed + returnUsed);

               if (remainNeed <= 0.000001) {
                   continue;
               }
               System.out.println("申请数量"+appNumber);
               System.out.println("入库数量"+actualNumber);
               System.out.println("让步数量"+rbUsed);
               System.out.println("退货数量"+returnUsed);
               System.out.println("还差多少："+remainNeed);
               // ===== 使用退货不继续采购 =====
               List<MaterialReturnReceive> returnList =
                      mapper.selectReturnReceiveByProduct(
                               productId1, vault1
                       );

               for (MaterialReturnReceive ret : returnList) {

                   double returnQty1 = ret.getReturnQty().doubleValue();
                   double returnQtyed = ret.getReturnQtyed().doubleValue();

                   double returnRemain = returnQty1 - returnQtyed;
                   if (returnRemain < 0.000001) {
                       continue;
                   }

                   double useQty = Math.min(remainNeed, returnRemain);

                   // 更新退货已用数量
                   ret.setReturnQtyed(
                           BigDecimal.valueOf(returnQtyed + useQty)
                   );
                   mapper.updateReturnQtyed(
                           ret.getReturnQtyed().doubleValue(),
                           ret.getId()
                   );

                   // 记录退货使用
                   ReturnNumber rn = new ReturnNumber(0,useQty,cg.getId(),1);
                   System.out.println("插入"+rn);
                   returnNumberMapper.insert(rn);

                   remainNeed -= useQty;

                   if (remainNeed < 0.000001) {
                       break;
                   }
               }
           }

           // ===== 最终：判断是否所有明细完成 =====
           // ===== 最终：判断是否所有明细完成 =====
           boolean allFinished = true;

           for (ApplicationContent cg : cgApplicationContents) {
               System.out.println("采购单"+cg);
               double rbUsed =
                       returnNumberMapper.sumByCgIdAndType(2,cg.getId());
               double returnUsed =
                       returnNumberMapper.sumByCgIdAndType(1,cg.getId());
               System.out.println("让步"+rbUsed);
               System.out.println("退货"+returnUsed);
               double total =
                       cg.getActualNumber()
                               + rbUsed
                               + returnUsed;
               System.out.println("total"+total);
               System.out.println("appNumber"+cg.getAppNumber());
               if (total < cg.getAppNumber() - 0.000001) {
                   allFinished = false;
                   break; // ✅ 只要有一条没完成，就可以结束了
               }
           }
           System.out.println(allFinished);
           if (allFinished) {
               mapper1.updateStatus(
                       1,
                       cgApplicationContents.get(0).getApplicationId()
               );
           }

       }
       else {
           //继续购买
           if (remainReturnQty > 0) {

               // 获取关系列表
               List<Relationship> relationships = relationshipMapper.selectAllOld(newAppId);//根据质检单ID查询订购单信息
               if (relationships != null && !relationships.isEmpty()) {
                   for (Relationship relationship : relationships) {
                       if (remainReturnQty <= 0) break;

                       // ✅ 这里已经改成你正确的字段
                       int oldAppFormId = relationship.getOldAppFormId();//获取订购单的ID
                       ApplicationContent content = applicationContentMapper.selectByAppProduct(oldAppFormId, productId, vault);
                       System.out.println("appId"+oldAppFormId);
                       if (content == null) continue;

                       double appNumber = content.getAppNumber();
                       double actualNumber = content.getActualNumber();
                       System.out.println("appNumber"+appNumber);
                       if (appNumber <= 0) continue;


                       double deduct = Math.min(remainReturnQty, appNumber);
                       System.out.println("deduct"+deduct);
//                   applicationContentMapper.updateAppNumberOnce( appNumber - deduct,content.getId());//修改订购单的申请数量（appNumber）
                   applicationContentMapper.updateActualNumber(content.getId(), actualNumber - deduct);//修改转化数量
                       //修改到货数量
                       remainReturnQty -= deduct;
                   }
               }

               //扣到货数量
               List<ProcurementOperations> opList = procurementOperationsMapper.selectByContentId(applicationContentId);

               if (opList != null && !opList.isEmpty()) {
                   for (ProcurementOperations op : opList) {
                       if (remainReturnQty <= 0) break;

                       double batchQty = op.getBatchQuantity();
                       if (batchQty <= 0) continue;

                       double deduct = Math.min(remainReturnQty, batchQty);
                       op.setBatchQuantity(batchQty - deduct);
                       procurementOperationsMapper.updateById(op);
                       remainReturnQty -= deduct;
                   }
               }

               if (remainReturnQty > 0) {
                   throw new RuntimeException("退货数量超出申购单可扣减总数，剩余：" + remainReturnQty);
               }
//                    // 扣 到货记录数量
//                    if (remainReturnQty > 0) {
//                        List<ProcurementOperations> opList = procurementOperationsMapper.selectByContentId(applicationContentId);
//
//                        if (opList != null && !opList.isEmpty()) {
//                            for (ProcurementOperations op : opList) {
//                                if (remainReturnQty <= 0) break;
//
//                                double batchQty = op.getBatchQuantity();
//                                if (batchQty <= 0) continue;
//
//                                double deduct = Math.min(remainReturnQty, batchQty);
//                                op.setBatchQuantity(batchQty - deduct);
//                                procurementOperationsMapper.updateById(op);
//                                remainReturnQty -= deduct;
//                            }
//                        }
//                    }

//               List<ProcurementOperations> opList = procurementOperationsMapper.selectByContentId(applicationContentId);
//
//               if (opList != null && !opList.isEmpty()) {
//                   for (ProcurementOperations op : opList) {
//                       if (remainReturnQty <= 0) break;
//
//                       double batchQty = op.getBatchQuantity();
//                       if (batchQty <= 0) continue;
//
//                       double deduct = Math.min(remainReturnQty, batchQty);
//                       op.setBatchQuantity(batchQty - deduct);
//                       procurementOperationsMapper.updateById(op);
//                       remainReturnQty -= deduct;
//                   }
//               }
           }
       }
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }
}