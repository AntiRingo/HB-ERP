package com.hongbang.service.impl;

import com.hongbang.mapper.ProcurementOperationsMapper;
import com.hongbang.service.ProcurementService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcurementServiceImpl implements ProcurementService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    // 开始采购
    public Map<String, Object> startProcurement(int contentId, int operatorId) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        Map<String, Object> result = new HashMap<>();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);

            //检查是否已开始采购
            int startedCount = mapper.checkProcurementStarted(contentId);
            if (startedCount > 0) {
                result.put("status", "warning");
                result.put("message", "采购已开始，无需重复操作");
                return result;
            }

            //开始采购
            int startResult = mapper.startProcurement(contentId, operatorId, "开始采购");

            if (startResult > 0) {
                sqlSession.commit();
                result.put("status", "success");
                result.put("message", "采购开始记录已创建");
            } else {
                result.put("status", "error");
                result.put("message", "开始采购失败");
            }
        } catch (Exception e) {
            sqlSession.rollback();
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        } finally {
            //释放资源
            sqlSession.close();
        }

        //返回值
        return result;
    }

    // 记录到货
    public Map<String, Object> recordArrival(int contentId, int operatorId, double batchQuantity, String notes) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        Map<String, Object> result = new HashMap<>();

        try {
            //获取mapper
            ProcurementOperationsMapper procurementMapper = sqlSession.getMapper(ProcurementOperationsMapper.class);

            //获取需求数量和当前已到货数量
            Double requiredQuantity = procurementMapper.getRequiredQuantity(contentId);
            Double currentArrived = procurementMapper.getCurrentArrivedQuantity(contentId);

            if (requiredQuantity == null || currentArrived == null) {
                result.put("status", "error");
                result.put("message", "未找到申请单明细");
                return result;
            }

            //计算新的累计数量
            double newArrived = currentArrived + batchQuantity;

            //检查是否超量
            if (newArrived > requiredQuantity) {
                result.put("status", "error");
                result.put("message", String.format("到货数量%.3f超过剩余数量%.3f",
                        batchQuantity, requiredQuantity - currentArrived));
                return result;
            }

            //记录到货
            int arrivalResult = procurementMapper.recordArrival(contentId, operatorId, batchQuantity,
                    notes != null ? notes : "本次到货");

            if (arrivalResult <= 0) {
                result.put("status", "error");
                result.put("message", "记录到货失败");
                return result;
            }

            //更新申请单明细的已到货数量
            int updateResult = procurementMapper.updateArrivedQuantity(contentId, newArrived);

            if (updateResult <= 0) {
                result.put("status", "error");
                result.put("message", "更新到货数量失败");
                return result;
            }

            sqlSession.commit();

            result.put("status", "success");
            result.put("message", String.format("到货记录已保存，累计到货: %.3f/%.3f", newArrived, requiredQuantity));
            result.put("new_arrived_quantity", newArrived);
            result.put("required_quantity", requiredQuantity);
            result.put("remaining_quantity", requiredQuantity - newArrived);

        } catch (Exception e) {
            sqlSession.rollback();
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        } finally {
            //释放资源
            sqlSession.close();
        }

        //返回值
        return result;
    }

    // 完成采购
    public Map<String, Object> completeProcurement(int contentId, int operatorId) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        Map<String, Object> result = new HashMap<>();

        try {
            //获取mapper
            ProcurementOperationsMapper procurementMapper = sqlSession.getMapper(ProcurementOperationsMapper.class);

            //检查是否已全部到货
            Double requiredQuantity = procurementMapper.getRequiredQuantity(contentId);
            Double currentArrived = procurementMapper.getCurrentArrivedQuantity(contentId);

            if (requiredQuantity == null || currentArrived == null) {
                result.put("status", "error");
                result.put("message", "未找到申请单明细");
                return result;
            }

            if (currentArrived < requiredQuantity) {
                result.put("status", "error");
                result.put("message", String.format("未全部到货，当前: %.3f/%.3f", currentArrived, requiredQuantity));
                return result;
            }

            //完成采购
            int completeResult = procurementMapper.completeProcurement(contentId, operatorId, "采购完成");

            if (completeResult > 0) {
                sqlSession.commit();
                result.put("status", "success");
                result.put("message", "采购完成记录已创建");
            } else {
                result.put("status", "error");
                result.put("message", "完成采购失败");
            }
        } catch (Exception e) {
            sqlSession.rollback();
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        } finally {
            //释放资源
            sqlSession.close();
        }

        //返回值
        return result;
    }

    // 获取物料采购记录
    public List<Map<String, Object>> getMaterialProcurementRecords(int contentId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
            //调用mapper
            List<Map<String, Object>> records = mapper.getMaterialProcurementRecords(contentId);
            //释放资源
            sqlSession.close();
            //返回值
            return records;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return new ArrayList<>();
        }
    }

    // 检查是否已开始采购
    public boolean isProcurementStarted(int contentId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
            //调用mapper
            int count = mapper.checkProcurementStarted(contentId);
            //释放资源
            sqlSession.close();
            //返回值
            return count > 0;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return false;
        }
    }

    // 获取当前已到货数量
    public double getCurrentArrivedQuantity(int contentId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
            //调用mapper
            Double quantity = mapper.getCurrentArrivedQuantity(contentId);
            //释放资源
            sqlSession.close();
            //返回值
            return quantity != null ? quantity : 0.0;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return 0.0;
        }
    }

    // 获取需求数量
    public double getRequiredQuantity(int contentId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
            //调用mapper
            Double quantity = mapper.getRequiredQuantity(contentId);
            //释放资源
            sqlSession.close();
            //返回值
            return quantity != null ? quantity : 0.0;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return 0.0;
        }
    }



    // 获取物料到货状态
    public Map<String, Object> getMaterialArrivalStatus(int contentId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ProcurementOperationsMapper mapper = sqlSession.getMapper(ProcurementOperationsMapper.class);
            //调用mapper
            Map<String, Object> status = mapper.getMaterialArrivalStatus(contentId);

            if (status == null || status.isEmpty()) {
                // 如果没有记录，创建默认状态
                status = new HashMap<>();
                status.put("content_id", contentId);
                status.put("procurement_status", "pending");
                status.put("current_arrived_quantity", 0.0);
                status.put("required_quantity", 0.0);
                status.put("remaining_quantity", 0.0);
                status.put("arrival_count", 0);
                status.put("progress_percent", 0.0);
            }

            //释放资源
            sqlSession.close();
            //返回值
            return status;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            Map<String, Object> defaultStatus = new HashMap<>();
            defaultStatus.put("content_id", contentId);
            defaultStatus.put("procurement_status", "pending");
            defaultStatus.put("current_arrived_quantity", 0.0);
            defaultStatus.put("required_quantity", 0.0);
            defaultStatus.put("remaining_quantity", 0.0);
            defaultStatus.put("arrival_count", 0);
            defaultStatus.put("progress_percent", 0.0);
            return defaultStatus;
        }
    }
}