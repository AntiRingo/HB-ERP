package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.service.ProcurementService;
import com.hongbang.service.impl.ProcurementServiceImpl;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/procurement/*")
public class ProcurementServlet extends BaseServlet {

    //获取service
    ProcurementService procurementService = new ProcurementServiceImpl();

    // 开始采购
    public void startProcurement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());
            int operatorId = Integer.parseInt(params.get("operator_id").toString());

            //调用service
            Map<String, Object> result = procurementService.startProcurement(contentId, operatorId);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "系统错误: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 记录到货
    public void recordArrival(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());
            int operatorId = Integer.parseInt(params.get("operator_id").toString());
            double batchQuantity = Double.parseDouble(params.get("batch_quantity").toString());
            String notes = params.get("notes") != null ? params.get("notes").toString() : "";

            //调用service
            Map<String, Object> result = procurementService.recordArrival(contentId, operatorId, batchQuantity, notes);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "系统错误: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 完成采购
    public void completeProcurement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());
            int operatorId = Integer.parseInt(params.get("operator_id").toString());

            //调用service
            Map<String, Object> result = procurementService.completeProcurement(contentId, operatorId);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "系统错误: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 获取物料采购记录
    public void getMaterialProcurementRecords(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());

            //调用service
            List<Map<String, Object>> records = procurementService.getMaterialProcurementRecords(contentId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("data", records);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取采购记录失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 检查是否已开始采购
    public void checkProcurementStarted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());

            //调用service
            boolean started = procurementService.isProcurementStarted(contentId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("started", started);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "检查状态失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 获取当前已到货数量
    public void getCurrentArrivedQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());

            //调用service
            double arrivedQuantity = procurementService.getCurrentArrivedQuantity(contentId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("arrived_quantity", arrivedQuantity);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取到货数量失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 获取需求数量
    public void getRequiredQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());

            //调用service
            double requiredQuantity = procurementService.getRequiredQuantity(contentId);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("required_quantity", requiredQuantity);

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取需求数量失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }

    // 获取物料到货状态
    public void getMaterialArrivalStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            //接收前端传来的数据
            BufferedReader bufferedReader = request.getReader();
            String jsonStr = bufferedReader.readLine();

            //解析JSON数据
            Map<String, Object> params = JSON.parseObject(jsonStr, Map.class);

            int contentId = Integer.parseInt(params.get("content_id").toString());

            //调用service
            Map<String, Object> status = procurementService.getMaterialArrivalStatus(contentId);

            Map<String, Object> result = new HashMap<>();

            if (status != null && !status.isEmpty()) {
                result.put("status", "success");
                result.put("data", status);
            } else {
                result.put("status", "error");
                result.put("message", "未找到物料状态");
            }

            //转化为json数据
            String jsonResult = JSON.toJSONString(result);

            //响应数据
            response.getWriter().write(jsonResult);

        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "获取状态失败: " + e.getMessage());

            String jsonError = JSON.toJSONString(errorResult);
            response.getWriter().write(jsonError);
        }
    }
}