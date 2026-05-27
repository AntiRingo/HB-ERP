package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.Inspection;
import com.hongbang.service.InspectionService;
import com.hongbang.service.impl.InspectionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/inspection/*")
public class InspectionServlet extends BaseServlet {

    //service
    InspectionService service = new InspectionServiceImpl();

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    //接收前端传来的数据
//        BufferedReader bufferedReader = request.getReader();
//        String s = bufferedReader.readLine();
//        //json数据序列化
//        Inspection inspection = JSON.parseObject(s, Inspection.class);
//        //调用service
//        service.add(inspection);
//        //响应成功标识
//        response.setContentType("text/json;charset=utf-8");
//        response.getWriter().write("success");
    }

    //通过或者不通过
    public void updatePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接收前端传来的信息
        BufferedReader bufferedReader = request.getReader();
        String pass = bufferedReader.readLine();
        //数据序列化
        Inspection inspection = JSON.parseObject(pass, Inspection.class);
        //调用service
        service.updatePass(inspection);
        //响应成功标识
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write("success");
    }

    //已经质检过的质检单查询质检信息
    public void selectZjInformation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //接受前端传来的数据
        BufferedReader bufferedReader = request.getReader();
        String appId = bufferedReader.readLine();
        //调用service
        List<Inspection> inspectionList = service.selectZjInformation(Integer.parseInt(appId));
        //转换为json数据
        String s = JSON.toJSONString(inspectionList);
        //响应数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(s);
    }


    private InspectionService inspectionService;



    /**
     * 获取质检记录
     */

    public void getMaterialQCInspectionRecords(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("content_id", jsonObject.getInteger("content_id"));

            Map<String, Object> result = service.getMaterialQCInspectionRecords(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取质检状态
     */

    public void getMaterialQCInspectionStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("content_id", jsonObject.getInteger("content_id"));
            if (jsonObject.containsKey("application_id")) {
                params.put("application_id", jsonObject.getInteger("application_id"));
            }

            Map<String, Object> result = service.getMaterialQCInspectionStatus(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取可申请质检数量
     */

    public void getAvailableQCQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("content_id", jsonObject.getInteger("content_id"));
            System.out.println(params);
            Map<String, Object> result = service.getAvailableQCQuantity(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取已申请质检数量
     */

    public void getQCRequestedQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("content_id", jsonObject.getInteger("content_id"));

            Map<String, Object> result = service.getQCRequestedQuantity(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取质检申请单列表
     */

    public void getQCApplications(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> result = service.getQCApplications(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取质检申请单详情
     */

    public void getQCApplicationDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("qc_application_id", jsonObject.getInteger("qc_application_id"));

            Map<String, Object> result = service.getQCApplicationDetail(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 更新质检申请单状态
     */

    public void updateQCApplicationStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();
            params.put("qc_application_id", jsonObject.getInteger("qc_application_id"));
            params.put("status", jsonObject.getInteger("status"));
            params.put("operator_id", jsonObject.getInteger("operator_id"));
            params.put("notes", jsonObject.getString("notes"));

            Map<String, Object> result = service.updateQCApplicationStatus(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    /**
     * 获取质检进度统计
     */

    public void getQCProgressStats(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> result = service.getQCProgressStats(params);

            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "请求处理失败: " + e.getMessage());
            response.getWriter().write(JSON.toJSONString(errorResult));
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }





    /**
     * 批量申请质检
     */

    public void batchRequestQCInspection(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();

            if (jsonObject.containsKey("items")) {
                params.put("items", jsonObject.getJSONArray("items"));
            } else {
                sendError(response, "缺少items参数");
                return;
            }

            if (jsonObject.containsKey("operator_id")) {
                params.put("operator_id", jsonObject.getInteger("operator_id"));
            } else {
                sendError(response, "缺少operator_id参数");
                return;
            }

            params.put("notes", jsonObject.getString("notes"));

            Map<String, Object> result = service.batchRequestQCInspection(params);
            response.getWriter().write(JSON.toJSONString(result));

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "请求处理失败: " + e.getMessage());
        }
    }

    /**
     * 批量获取可申请质检数量
     */

    public void batchGetAvailableQCQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");

        try {
            String requestBody = getRequestBody(request);
            JSONObject jsonObject = JSON.parseObject(requestBody);

            Map<String, Object> params = new HashMap<>();

            if (jsonObject.containsKey("content_ids")) {
                params.put("content_ids", jsonObject.getJSONArray("content_ids"));
            } else {
                sendError(response, "缺少content_ids参数");
                return;
            }
            System.out.println("这里看看"+params);

            Map<String, Object> result = service.batchGetAvailableQCQuantity(params);
            response.getWriter().write(JSON.toJSONString(result));

        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "请求处理失败: " + e.getMessage());
        }
    }
}
