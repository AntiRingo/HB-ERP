package com.hongbang.web.servlet;

import com.alibaba.fastjson.JSON;
import com.hongbang.pojo.*;
import com.hongbang.service.*;
import com.hongbang.service.impl.*;
import com.hongbang.util.Compare;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/examine/*")
public class ExamineServlet extends BaseServlet {
    //获取service
    ExamineService examineService = new ExamineServiceImpl();
    ExamineStepContentService examineStepContentService = new ExamineStepContentServiceImpl();
    ApplicationFormService applicationFormService = new ApplicationFormServiceImpl();
    ExamineLogService examineLogService = new ExamineLogServiceImpl();
    ExamineAllTypeService examineAllTypeService = new ExamineAllTypeServiceImpl();
    ExamineConditionContentService examineConditionContentService = new ExamineConditionContentServiceImpl();
    ApplicationContentService applicationContentService = new ApplicationContentServiceImpl();
    //审核功能（部长审核）
    public void updateMinisterOrBoss(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //接收未通过的原因
        BufferedReader bufferedReader = request.getReader();
        String refuse = bufferedReader.readLine();
        //接收要修改的申请单id
        String appFormId = request.getParameter("appFormId");
        //接收审核结果
        String status = request.getParameter("status");

        //查询正在登录的用户信息
        HttpSession session = request.getSession();
        Object user = session.getAttribute("username");


        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);



        //2、判断user是否为null,如果不为null说明用户登录过了
        response.setContentType("text/json;charset=utf-8");
        if (user!=null){
            String s = JSON.toJSONString(user);
            User user1 = JSON.parseObject(s, User.class);
            int id = user1.getId();
           ExamineLog examineLog = new ExamineLog();
           examineLog.setAppFormId(Integer.parseInt(appFormId));
           examineLog.setDate(formattedTime);
           examineLog.setUserId(id);
//           获取账号所在的部门
            int department = user1.getDepartment();
            if (Integer.parseInt(status)==1){
               examineLog.setExamineStatus("通过");
           }
           else if (Integer.parseInt(status)==2){
               examineLog.setExamineStatus("否决");
               //根据申请单id更新不通过的原因
                applicationFormService.updateRefuse(refuse, Integer.parseInt(appFormId));
            }
            int level;
            String agent = request.getParameter("agent");
            //查看是否是代理审核
            if (agent==null){
                level= user1.getLevel();
            }
            else {
                level=3;
            }
          if (level==3 || level==2){
                //查询minister是否已经审核过了，如果部长未审核先更新部长审核，部长审核过了更新步骤审核
                List<Examine> examines = examineService.selectByAppId(Integer.parseInt(appFormId));
                if (examines.size()>0){
                    int minister = examines.get(0).getMinister();
                    if (minister==0){
                        //调用service
                        examineService.updateMinister(Integer.parseInt(status), Integer.parseInt(appFormId),examineLog);

                        if (Integer.parseInt(status)!=2){
                            //通过审核，查看是否符合其他审核步骤
                            //查询申请单类型
                            List<ApplicationForm> applicationForms1 = applicationFormService.selectById(Integer.parseInt(appFormId));
                            int typeId = applicationForms1.get(0).getSortTwo();
                            List<ExamineAllType> examineAllTypes = examineAllTypeService.selectStep(typeId);
                            boolean e= false;
                            for (int j = 0; j < examineAllTypes.size(); j++) {

                                    //查询大于当前审核的步骤比如当前审核的是第一步，那就查询第一步后面的
                                    //查询审核条件(审核部门，审核步骤)
                                    int id2 = examineAllTypes.get(j).getId();
                                    List<Map<String, Object>> maps = examineConditionContentService.selectConditionByStepId(id2);
                                    List<ExamineStepContent> newExamineStepContents = new ArrayList<>();
                                    if (maps.size()>0){
                                        //存在审核条件,判断是否符合审核条件
                                        //查询申请单内容
                                        List<ApplicationContent> applicationContents = applicationContentService.selectByAppIdCp(Integer.parseInt(appFormId));
                                        List<ApplicationContent> applicationContents1 = applicationContentService.selectByAppIdDz(Integer.parseInt(appFormId));
                                        List<ApplicationContent> newApplicationContent = new ArrayList<>();
                                        if (applicationContents.size()>0){
                                            newApplicationContent.addAll(applicationContents);
                                        }
                                        if (applicationContents1.size()>0){
                                            newApplicationContent.addAll(applicationContents1);
                                        }
                                        boolean b = false;
                                        for (int k = 0; k < maps.size(); k++) {
                                            Map<String, Object> map = maps.get(k);
                                            Object name = map.get("name");
                                            Object content = map.get("content");
                                            if (name.equals("单价")){
                                                //获取申请单中的所有单价
                                                List<Map<String, Object>> unitPrice = applicationContentService.selectPrice(newApplicationContent);
                                                for (int l = 0; l < unitPrice.size(); l++) {
                                                    Object price1 = unitPrice.get(l).get("price");//得到单价，判断是否符合
                                                    b = Compare.compare4(content.toString(), price1.toString());
                                                    if (b){
                                                        break;
                                                    }
                                                }


                                            }
                                            else if (name.equals("总价")){
                                                //获取申请单中总价
                                                //获取申请单的总价格
                                                List<ApplicationForm> applicationForms = applicationFormService.selectById(Integer.parseInt(appFormId));
                                                List<Map<String, Object>> maps1 = applicationFormService.selectPriceAndCount(applicationForms);
                                                Object totalPrice = maps1.get(0).get("totalPrice");
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
                                            e=true;
                                            ExamineStepContent examineStepContent = new ExamineStepContent();
                                            examineStepContent.setExamineStepId(id2);
                                            examineStepContent.setAppFormId(Integer.parseInt(appFormId));
                                            //添加步骤
                                            newExamineStepContents.add(examineStepContent);
                                            examineStepContentService.add(newExamineStepContents);
                                            //

                                            int examineStepIds = newExamineStepContents.get(0).getExamineStepId();
                                            ExamineAllType examineAllTypess = examineAllTypeService.selectById(examineStepIds);
                                            int departmentId = examineAllTypess.getDepartmentId();
                                            //更新审申请单上的审核部门
                                            applicationFormService.updateCirculation(departmentId, Integer.parseInt(appFormId));
                                            //只添加一个步骤
                                            j=examineAllTypes.size();
                                        }

                                    }
                                    else {
                                        e=true;
                                        //不存在审核条件,添加审核步骤，修改申请单下一审核部门
                                        ExamineStepContent examineStepContent = new ExamineStepContent();
                                        examineStepContent.setExamineStepId(id2);
                                        examineStepContent.setAppFormId(Integer.parseInt(appFormId));
                                        //添加步骤
                                        newExamineStepContents.add(examineStepContent);
                                        examineStepContentService.add(newExamineStepContents);
                                        //

                                        int examineStepIds = newExamineStepContents.get(0).getExamineStepId();
                                        ExamineAllType examineAllTypess = examineAllTypeService.selectById(examineStepIds);
                                        int departmentId = examineAllTypess.getDepartmentId();
                                        //更新审申请单上的审核部门
                                        applicationFormService.updateCirculation(departmentId, Integer.parseInt(appFormId));
                                        //只添加一个步骤
                                        j=examineAllTypes.size();

                                    }

                            }
                            if (!e){

                                //没有其他符合条件的审核步骤了，这是最后一个
                                //节点上的全部审核通过，改变申请表的状态为审核完成
                                applicationFormService.updateCirculationBoss(Integer.parseInt(appFormId));
                            }
                        }
                    }
                    else{
                        //部长审核过了，查看审核流程上是否存在本部门
                        List<Map<String,Object>> examineStepContents = examineStepContentService.selectByAppId(Integer.parseInt(appFormId));
                        for (int i = 0; i < examineStepContents.size(); i++) {
                            Object department_id = examineStepContents.get(i).get("department_id");
                            if (department_id.equals(department)){
                                //部门相同，修改该步骤的审核状态
                                Object id1 = examineStepContents.get(i).get("id");
                                examineStepContentService.update(Integer.parseInt(status),(Integer) id1);
                                //添加审核记录日志
                                examineLogService.addLog(examineLog);
                                if (Integer.parseInt(status)!=2){
                                    System.out.println(examineStepContents.get(i));
                                    //通过了审核，获取当前审核通过的步骤内容
                                    Object examineStepId = examineStepContents.get(i).get("examine_step_id");

                                    ExamineAllType examineAllType = examineAllTypeService.selectById((Integer) examineStepId);
                                    int step = examineAllType.getStep();
                                    int typeId = examineAllType.getTypeId();
                                    //查询下一个符合要求的审核步骤
                                    List<ExamineAllType> examineAllTypes = examineAllTypeService.selectStep(typeId);
                                    //判断是否还有其他符合要求的审核步骤
                                    boolean e= false;
                                    for (int j = 0; j < examineAllTypes.size(); j++) {
                                        int step1 = examineAllTypes.get(j).getStep();
                                        if (step1>step){
                                            //查询大于当前审核的步骤比如当前审核的是第一步，那就查询第一步后面的
                                            //查询审核条件(审核部门，审核步骤)
                                            int id2 = examineAllTypes.get(j).getId();
                                            List<Map<String, Object>> maps = examineConditionContentService.selectConditionByStepId(id2);
                                            List<ExamineStepContent> newExamineStepContents = new ArrayList<>();
                                            if (maps.size()>0){
                                                //存在审核条件,判断是否符合审核条件
                                                //查询申请单内容
                                                List<ApplicationContent> applicationContents = applicationContentService.selectByAppIdCp(Integer.parseInt(appFormId));
                                                List<ApplicationContent> applicationContents1 = applicationContentService.selectByAppIdDz(Integer.parseInt(appFormId));
                                                List<ApplicationContent> newApplicationContent = new ArrayList<>();
                                                if (applicationContents.size()>0){
                                                    newApplicationContent.addAll(applicationContents);
                                                }
                                                if (applicationContents1.size()>0){
                                                    newApplicationContent.addAll(applicationContents1);
                                                }
                                                boolean b = false;
                                                for (int k = 0; k < maps.size(); k++) {
                                                    Map<String, Object> map = maps.get(k);
                                                    Object name = map.get("name");
                                                    Object content = map.get("content");
                                                    if (name.equals("单价")){
                                                        //获取申请单中的所有单价
                                                        List<Map<String, Object>> unitPrice = applicationContentService.selectPrice(newApplicationContent);
                                                        for (int l = 0; l < unitPrice.size(); l++) {
                                                            Object price1 = unitPrice.get(l).get("price");//得到单价，判断是否符合
                                                            b = Compare.compare4(content.toString(), price1.toString());
                                                            if (b){
                                                                break;
                                                            }
                                                        }


                                                    }
                                                    else if (name.equals("总价")){
                                                        //获取申请单中总价
                                                        //获取申请单的总价格
                                                        List<ApplicationForm> applicationForms = applicationFormService.selectById(Integer.parseInt(appFormId));
                                                        List<Map<String, Object>> maps1 = applicationFormService.selectPriceAndCount(applicationForms);
                                                        Object totalPrice = maps1.get(0).get("totalPrice");
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
                                                    e=true;
                                                    ExamineStepContent examineStepContent = new ExamineStepContent();
                                                    examineStepContent.setExamineStepId(id2);
                                                    examineStepContent.setAppFormId(Integer.parseInt(appFormId));
                                                    //添加步骤
                                                    newExamineStepContents.add(examineStepContent);
                                                    examineStepContentService.add(newExamineStepContents);
                                                    //

                                                    int examineStepIds = newExamineStepContents.get(0).getExamineStepId();
                                                    ExamineAllType examineAllTypess = examineAllTypeService.selectById(examineStepIds);
                                                    int departmentId = examineAllTypess.getDepartmentId();
                                                    //更新审申请单上的审核部门
                                                    applicationFormService.updateCirculation(departmentId, Integer.parseInt(appFormId));
                                                    //只添加一个步骤
                                                    j=examineAllTypes.size();
                                                }

                                            }
                                            else {
                                                e=true;
                                                //不存在审核条件,添加审核步骤，修改申请单下一审核部门
                                                ExamineStepContent examineStepContent = new ExamineStepContent();
                                                examineStepContent.setExamineStepId(id2);
                                                examineStepContent.setAppFormId(Integer.parseInt(appFormId));
                                                //添加步骤
                                                newExamineStepContents.add(examineStepContent);
                                                examineStepContentService.add(newExamineStepContents);
                                                //

                                                    int examineStepIds = newExamineStepContents.get(0).getExamineStepId();
                                                    ExamineAllType examineAllTypess = examineAllTypeService.selectById(examineStepIds);
                                                    int departmentId = examineAllTypess.getDepartmentId();
                                                    //更新审申请单上的审核部门
                                                   applicationFormService.updateCirculation(departmentId, Integer.parseInt(appFormId));
                                                   //只添加一个步骤
                                                    j=examineAllTypes.size();

                                            }
                                        }
                                    }
                                    if (!e){
                                        //没有其他符合条件的审核步骤了，这是最后一个
                                        //节点上的全部审核通过，改变申请表的状态为审核完成
                                        applicationFormService.updateCirculationBoss(Integer.parseInt(appFormId));
                                    }

                                }
                            }
                        }

                    }




                }



                //响应数据
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write("success");

            }

        }else {
            response.getWriter().write("false");
        }

    }



}
