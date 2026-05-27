package com.hongbang.service.impl;

import com.hongbang.mapper.*;
import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.ExamineAllType;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.ApplicationFormService;
import com.hongbang.util.Compare;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

public class ApplicationFormServiceImpl implements ApplicationFormService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

//    //添加申请单
//    @Override
//    public void add(ApplicationForm applicationForm){
//        //获取session
//        SqlSession sqlSession = factory.openSession();
//        //获取mapper
//        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
//        //调用mapper
//        mapper.add(applicationForm);
//
//        //提交事务
//        sqlSession.commit();
//        //释放资源
//        sqlSession.close();
//    }

    //删除申请表
    @Override
    public void delete(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.delete(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询申请单信息（包含申请人信息）
   public PageBean<Map<String,Object>> selectAllApplication(int sort,int currentPage,int pageSize,int ckId,boolean a,boolean ck,boolean rk,boolean cg,boolean zj,boolean dg){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       int size=pageSize;
       int begin=(currentPage-1)*size;
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectAllApplication(sort,begin,size,ckId,a,ck,rk,cg,zj,dg);
       int count = mapper.count(sort,ckId,a,ck,rk,cg,zj,dg);
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
       pageBean.setRows(maps);
       pageBean.setTotalCount(count);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;

   }


    //根据用户ID查询该用户的申请单
   public PageBean<ApplicationForm> selectByUserId(Map<String,Object> maps,int currentPage,int pageSize){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       int size=pageSize;
       int begin=(currentPage-1)*size;
       //调用mapper
       List<ApplicationForm> applicationForms = mapper.selectByUserId(maps,begin,size);
       int i = mapper.selectByUserIdCount(maps);
       //设置pageBean
       PageBean<ApplicationForm> pageBean = new PageBean<>();
       pageBean.setRows(applicationForms);
       pageBean.setTotalCount(i);

       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;
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
           //该申请单时采购入库，那就查询旧的采购申请单，将该申请单入库的数据回退,查询采购申请单的id
           Integer oldId = mapper1.selectOld(id);
           if (oldId!=null){
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
                       if (appNumber==0){
                           oldApplicationContent.setAppNumber(0);
                       }

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
                   mapper.updateStatus(0, oldId);//处理中
               }
               else {
                   //已完成
                   mapper.updateStatus(1, oldId);//已完成
               }
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

    //查看是否有新增的申请信息(入库申请)
    public List<Integer> selectIfNewApplicationRk(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectIfNewApplicationRk();
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //查看是否有新增的申请信息(出库申请)
   public List<Integer> selectIfNewApplicationCk(){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<Integer> list = mapper.selectIfNewApplicationCk();
       //释放资源
       sqlSession.close();
       //返回值
       return list;
   }

    //点击查看后将申请单从新增状态改为取货中状态
   public void updateStatusTwo(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       mapper.updateStatusTwo(id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //申请单筛选
    public  PageBean<Map<String,Object>>  screen(Map<String,Object> maps,int currentPage,int pageSize,int ckId,boolean a){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;

        //调用mapper

        List<Map<String,Object>> screen = mapper.screen(maps,begin,size,ckId,a);



        int i = mapper.screenCount(maps,ckId,a);
        PageBean<Map<String,Object>> applicationFormPageBean = new PageBean<>();


        if (screen.size()>0){


            List<Map<String, Object>> maps1 = mapper.screenUserName(screen);

            applicationFormPageBean.setRows(maps1);
            applicationFormPageBean.setTotalCount(i);
        }
        else {

            applicationFormPageBean.setRows(null);
            applicationFormPageBean.setTotalCount(i);
        }

        //释放资源
        sqlSession.close();
        //返回值
        return applicationFormPageBean;
    }

    //根据申请类型获取筛选人
   public List<Map<String,Object>> screenUserId(int sort,int ckId,boolean a){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.screenUserId(sort,ckId,a);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //模糊查询
   public PageBean<Map<String,Object>>searchOrderNumber(String str,int sort,int currentPage,int pageSize,int ckId,boolean a){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       int size=pageSize;
       int begin=(currentPage-1)*size;
       //调用mapper
       List<Map<String, Object>> maps = mapper.searchOrderNumber(str, sort, begin, size,ckId,a);
       int i = mapper.searchOrderNumberCount(str, sort,ckId,a);
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
       pageBean.setRows(maps);
       pageBean.setTotalCount(i);
       //返回值
       return pageBean;
   }
    //将申请单状态修改为已出库
    public void updateStatusFour(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.updateStatusFour(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询是否存在已出库状态的申请单
   public boolean selectPickUp(int userId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       boolean b = mapper.selectPickUp(userId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询是否存在已出库状态的申请单
    public List<Integer> selectPickUpAppId(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectPickUpAppId(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //修改申请单状态
    public void updateStatus( int status,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.updateStatus(status,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id查询申请单信息
    public List<ApplicationForm> selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<ApplicationForm> applicationForms = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return applicationForms;
    }


    //根据用户id查询该用户的申请单（已完成的和已撤销的除外）
    public List<ApplicationForm> selectAppFormNotCompleteAndDelete(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<ApplicationForm> applicationForms = mapper.selectAppFormNotCompleteAndDelete(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return applicationForms;
    }


    //仓库查看已经审核通过的申请
   public List<ApplicationForm>depotExamine(){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<ApplicationForm> applicationForms = mapper.depotExamine();
       //释放资源
       sqlSession.close();
       //返回值
       return applicationForms;
   }



    //部长查看需要自己审核的申请单
    public PageBean<ApplicationForm>ministerExamine( int department,int minister,int currentPage,int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<ApplicationForm> applicationForms = mapper.ministerExamine(department,minister,begin,size);
        int i = mapper.ministerExamineCount(department, minister);
        //设置pageBean
        PageBean<ApplicationForm> pageBean = new PageBean<>();
        pageBean.setTotalCount(i);
        pageBean.setRows(applicationForms);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;

    }

    //部长筛选
   public PageBean<ApplicationForm> ministerScreen(Map<String,Object> maps,int currentPage,int pageSize,int department){

        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       int begin = (currentPage-1)*pageSize;
       int size = pageSize;
       //调用mapper
       List<ApplicationForm> applicationForms = mapper.ministerScreen(maps, begin, size,department);
       int i = mapper.ministerScreenCount(maps,department);

       //设置pageBean
       PageBean<ApplicationForm> pageBean = new PageBean<>();
       pageBean.setRows(applicationForms);
       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;
   }



    //部长显示本部门的已经申请的人员（筛选使用）
   public List<Map<String,Object>> ministerScreenUser(Map<String,Object> maps,int department){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<Map<String, Object>> maps1 = mapper.ministerScreenUser(maps, department);
       //释放资源
       sqlSession.close();
       //返回值
       return maps1;
   }


    //部长显示申请部门(筛选使用)
   public List<Map<String,Object>> ministerScreenDepartment(Map<String,Object> maps,int department){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<Map<String, Object>> maps1 = mapper.ministerScreenDepartment(maps, department);
       //释放资源
       sqlSession.close();
       //返回值
       return maps1;
   }



    //根据申请单id查询是否总价钱
    public List<Map<String,Object>> selectPriceAndCount(List<ApplicationForm>applicationForms){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectPriceAndCount(applicationForms);
       //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据id查询申请单状态
   public List<Map<String,Object>>selectComplete(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectComplete(id);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //更新申请单上的审核部门
    public void updateCirculation(int circulation,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.updateCirculation(circulation,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询审核流程
   public  List<Map<String,Object>>selectLc(int appFormId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper,查询已经审核的流程
       List<Map<String, Object>> maps = mapper.selectLc(appFormId);
       //查询所有的流程
       List<ApplicationForm> applicationForms = mapper.selectById(appFormId);
       ApplicationForm applicationForm = applicationForms.get(0);
       int sortTwo = applicationForm.getSortTwo();
       ExamineAllTypeMapper mapper1 = sqlSession.getMapper(ExamineAllTypeMapper.class);
       ExamineConditionContentMapper mapper2 = sqlSession.getMapper(ExamineConditionContentMapper.class);
       ApplicationContentMapper mapper3 = sqlSession.getMapper(ApplicationContentMapper.class);
       List<ExamineAllType> examineAllTypes = mapper1.selectStep(sortTwo);
       if (examineAllTypes.size()>0){
           List<ApplicationContent> applicationContents =  mapper3.selectByAppIdCp(appFormId);
           List<ApplicationContent> applicationContents1 = mapper3.selectByAppIdDz(appFormId);
           List<ApplicationContent> newApplicationContents = new ArrayList<>();
           if (applicationContents.size()>0){
               newApplicationContents.addAll(applicationContents);
           }
           if (applicationContents1.size()>0){
               newApplicationContents.addAll(applicationContents1);
           }
           //获取申请单所有的单价
           List<Map<String, Object>> unitPrice = mapper3.selectPrice(newApplicationContents);
           //获取申请单的总价格
           List<Map<String, Object>> maps2 = mapper.selectPriceAndCount(Collections.singletonList(applicationForm));

           Object totalPrice = maps2.get(0).get("totalPrice");
           //得到了所有的审核流程，查询审核步骤都有什么
           for (int i = 0; i < examineAllTypes.size(); i++) {
               ExamineAllType examineAllType = examineAllTypes.get(i);
               //查询审核条件(审核部门，审核步骤)
               int id1 = examineAllType.getId();
               List<Map<String, Object>> maps1 = mapper2.selectConditionByStepId(id1);
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


                       //符合这一步的审核条件，根据步骤id查询部门
                       boolean a = true;
                       List<Map<String, Object>> maps3 = mapper1.selectDepartmentName(id1);
                       Map<String, Object> map1 = maps3.get(0);
                       map1.put("result","0");
                       for (int j = 0; j < maps.size(); j++) {

                           Map<String, Object> map = maps.get(j);
                           Object departmentName = map.get("departmentName");
                           Object departmentName1 = maps3.get(0).get("departmentName");

                           if (departmentName.equals(departmentName1)){
                               a=false;
                               break;
                           }
                       }
                       if (a){
                           //部门不存在已审核的步骤，判断步骤是否大于已审核的步骤
                        if (maps.size()>0){
                            String step = maps.get(maps.size() - 1).get("step").toString();

                            String  step1 = maps3.get(0).get("step").toString();
                            if (Integer.parseInt( step1)>Integer.parseInt( step)){
                                maps.add(maps3.get(0));
                            }
                        }
                        else {
                            maps.add(maps3.get(0));
                        }



                       }

                   }


               }
               else {
                   //没有审核添加就加入，等待下一步判断
                   boolean a = true;
                   List<Map<String, Object>> maps3 = mapper1.selectDepartmentName(id1);
                   Map<String, Object> map1 = maps3.get(0);
                   map1.put("result","0");
                   for (int j = 0; j < maps.size(); j++) {
                       Map<String, Object> map = maps.get(j);
                       Object departmentName = map.get("departmentName");
                       Object departmentName1 = maps3.get(0).get("departmentName");
                       if (departmentName.equals(departmentName1)){
                           a=false;
                           break;
                       }
                   }
                   if (a){
                       //部门不存在已审核的步骤，判断步骤是否大于已审核的步骤
                       if (maps.size()>0){
                           String step = maps.get(maps.size() - 1).get("step").toString();

                           String  step1 = maps3.get(0).get("step").toString();
                           if (Integer.parseInt( step1)>Integer.parseInt( step)){
                               maps.add(maps3.get(0));
                           }
                       }
                       else {
                           maps.add(maps3.get(0));
                       }
                   }

               }
           }
       }
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //修改circulationBoss,这个表示都审核完了，下一个该总经理了
    public void updateCirculationBoss(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        mapper.updateCirculationBoss(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //更新不通过的原因
   public void updateRefuse(String refuse,int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用mapper
       mapper.updateRefuse(refuse,id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //查询是否有新的申请单
    public List<Integer> selectIfExamine(int sort,int department){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectIfExamine(sort, department);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //查询申请单的发票是否已经全部上传并审核完成
    public boolean selectInvoiceSign(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        boolean b = mapper.selectInvoiceSign(id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //根据申请单id查询总价（只包含零件，成品价格在前端查询）
    public List<Map<String,Object>> selectPriceAndCountLj(List<ApplicationForm>applicationForms){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectPriceAndCountLj(applicationForms);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }



    //接单使用（查询所有出库，入库的并且是待查看状态的，并且是未接单状态的申请单）
   public List<ApplicationForm> selectTakeOrder(boolean ck,boolean rk,boolean cg,boolean zj,boolean dg){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
       //调用，apper
       List<ApplicationForm> applicationForms = mapper.selectTakeOrder(ck,rk,cg,zj,dg);
       //释放资源
       sqlSession.close();
       //返回值
       return applicationForms;
   }


    public PageBean<ApplicationForm> selectDgByUserId(int userId,int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);

        //计算
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<ApplicationForm> applicationForms = mapper.selectDgByUserId(userId, begin, size);
        int i = mapper.selectDgByUserIdCount(userId);
        //设置pageBean
        PageBean<ApplicationForm> pageBean = new PageBean<>();
        pageBean.setTotalCount(i);
        pageBean.setRows(applicationForms);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;


    }



    //查询自己申请的质检单
    public PageBean<ApplicationForm>selectMyZj(int userId,int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);

        //计算
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<ApplicationForm> applicationForms = mapper.selectMyZj(userId, begin, size);
        int i = mapper.selectMyZjCount(userId);
        //设置pageBean
        PageBean<ApplicationForm> pageBean = new PageBean<>();
        pageBean.setTotalCount(i);
        pageBean.setRows(applicationForms);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //申购单（即采购单）不需要进行接单，该类型的申请单由采购部部长进行拆分
    public PageBean<Map<String,Object>> selectAllCgApplication(int currentPage, int pageSize, boolean ck, boolean rk, boolean cg,boolean zj,Map<String,Object> mapss){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllCgApplication(begin,size,ck,rk,cg,zj,mapss);
        int count = mapper.selectAllCgApplicationCount(ck,rk,cg,zj,mapss);
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps);
        pageBean.setTotalCount(count);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //订购单，这个需要自己接单
    public PageBean<Map<String,Object>>selectAllDgApplication(int currentPage,int pageSize,boolean ck, boolean rk, boolean cg,  boolean zj, Map<String,Object> mapss, int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllDgApplication(begin,size,ck,rk,cg,zj,mapss,userId);
        int count = mapper.selectAllDgApplicationCount(ck,rk,cg,zj,mapss,userId);
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps);
        pageBean.setTotalCount(count);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    // 获取申请单状态
    public int getApplicationStatus(int applicationId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
            //调用mapper
            Integer status = mapper.getApplicationStatus(applicationId);
            //释放资源
            sqlSession.close();
            //返回值
            return status != null ? status : 0;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return 0;
        }
    }

    // 获取申请单详细信息
    public ApplicationForm getApplicationById(int applicationId) {
        //获取session
        SqlSession sqlSession = factory.openSession();

        try {
            //获取mapper
            ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
            //调用mapper
            ApplicationForm application = mapper.getApplicationById(applicationId);
            //释放资源
            sqlSession.close();
            //返回值
            return application;
        } catch (Exception e) {
            //释放资源
            sqlSession.close();
            //返回值
            return null;
        }
    }

    // 更新申请单状态
    public boolean updateStatusNew(int applicationId, int status) {

        //获取session
        SqlSession sqlSession = factory.openSession();
        boolean result = false;

        try {
            //获取mapper
            ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);
            //调用mapper
            boolean updateResult = mapper.updateStatusNew(applicationId, status);


            if (updateResult) {
                sqlSession.commit();
                result = true;
            }
        } catch (Exception e) {
            sqlSession.rollback();
            result = false;
        } finally {
            //释放资源
            sqlSession.close();
        }

        //返回值
        return result;
    }

    // 获取申请单状态（包含详细信息）
    public Map<String, Object> getApplicationStatusDetail(int applicationId) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        Map<String, Object> result = new HashMap<>();

        try {
            //获取mapper
            ApplicationFormMapper mapper = sqlSession.getMapper(ApplicationFormMapper.class);

            //获取申请单信息
            ApplicationForm application = mapper.getApplicationById(applicationId);

            if (application != null) {
                result.put("status", "success");
                result.put("data", application);
                result.put("status_code", application.getCompleteStatus());

                // 添加状态文本描述
                String statusText = getStatusText(application.getCompleteStatus());
                result.put("status_text", statusText);
            } else {
                result.put("status", "error");
                result.put("message", "申请单不存在");
            }
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "系统错误: " + e.getMessage());
        } finally {
            //释放资源
            sqlSession.close();
        }

        //返回值
        return result;
    }

    // 获取状态文本描述
    private String getStatusText(int statusCode) {
        switch (statusCode) {
            case 1:
                return "已完成";
            case 2:
                return "已拒绝";
            case 3:
                return "待处理";
            case 4:
                return "已取消";
            case 5:
                return "订购中";
            default:
                return "未知状态";
        }
    }
}

