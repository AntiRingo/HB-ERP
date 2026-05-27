package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.hongbang.pojo.QualityInspect;
import com.hongbang.service.QualityInspectService;
import com.hongbang.service.impl.QualityInspectServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

@WebServlet("/qualityInspect/*")
public class QualityInspectServlet extends BaseServlet {
    public void saveInspect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader bufferedReader = request.getReader();
        String param = bufferedReader.readLine();
        JSONObject json = JSONObject.parseObject(param);

        QualityInspect inspect = new QualityInspect();
        inspect.setApplicationContentId(json.getInteger("applicationContentId"));
        inspect.setProductId(json.getInteger("productId"));
        inspect.setVault(json.getInteger("vault"));
        inspect.setInspectResult(json.getInteger("inspectResult"));
        inspect.setInspectRemark(json.getString("inspectRemark"));

        QualityInspectService service = new QualityInspectServiceImpl();
        int rows = service.saveInspect(inspect);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(String.valueOf(rows > 0));
    }
}
