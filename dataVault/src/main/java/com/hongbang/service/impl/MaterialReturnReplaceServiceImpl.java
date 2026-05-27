package com.hongbang.service.impl;

import com.hongbang.mapper.*;
import com.hongbang.pojo.*;
import com.hongbang.service.MaterialReturnReplaceService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MaterialReturnReplaceServiceImpl implements MaterialReturnReplaceService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();


    //查询需要自己处理的让步接收信息
    @MapKey("id")
   public PageBean<Map<String,Object>> selectRangBuByUser(int user,  int currentPage,  int pageSize){
        //获取mapper
        SqlSession sqlSession = factory.openSession();
        MaterialReturnReplaceMapper mapper = sqlSession.getMapper(MaterialReturnReplaceMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;

        List<Map<String, Object>> maps = mapper.selectRangBuByUser(user,begin,size);
        int i = mapper.selectRangBuByUserCount(user);
        PageBean<Map<String,Object>> pageBean = new PageBean<>();
        pageBean.setRows(maps);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //插入让步接收修改信息
    public void add(MaterialReturnReplace materialReturnReplace,User user){
        //获取mapper
        SqlSession sqlSession = factory.openSession();
        MaterialReturnReplaceMapper mapper = sqlSession.getMapper(MaterialReturnReplaceMapper.class);
        ApplicationFormMapper applicationFormMapper = sqlSession.getMapper(ApplicationFormMapper.class);
        TakeOrderMapper takeOrderMapper = sqlSession.getMapper(TakeOrderMapper.class);
        ExamineMapper examineMapper = sqlSession.getMapper(ExamineMapper.class);
        MaterialReturnReceiveMapper returnReceiveMapper = sqlSession.getMapper(MaterialReturnReceiveMapper.class);
        ApplicationContentMapper applicationContentMapper = sqlSession.getMapper(ApplicationContentMapper.class);


        //调用mapper
        mapper.add(materialReturnReplace);

        //要添加入库单（该入库单不需要任何审核）
        //生成单号
        long l = System.currentTimeMillis();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        ApplicationForm applicationForm = new ApplicationForm(0,"RK_"+l,formattedTime,0,user.getId(),3,17,"",2,1,"","",0,0);

        //添加入库
        applicationFormMapper.add(applicationForm);
        //获取入库单ID
        int id = applicationForm.getId();
//添加新的接单信息
        TakeOrder takeOrder = new TakeOrder(0, id,0, 0);
       //添加接单信息
        takeOrderMapper.addTakeOrder(takeOrder);
        //设置申请审核表
        Examine examine = new Examine();
        examine.setAppFormId(id);
        examine.setMinister(1);
        examineMapper.addExamine(examine);
        //查询申请数量
        int receiveId = materialReturnReplace.getReceiveId();
        //查询
        MaterialReturnReceive materialReturnReceive = returnReceiveMapper.selectById(receiveId);
       //查询数量
        double returnQty = materialReturnReceive.getReturnQty().doubleValue();
        //查询价格
        Integer applicationId = materialReturnReceive.getApplicationId();
        //查询原质检单的内容
        ApplicationContent applicationContent1 = applicationContentMapper.selectByContentId(applicationId);
        //生成申请单内容
        ApplicationContent applicationContent = new ApplicationContent(0,id,materialReturnReplace.getNewProduct(),returnQty, 0, materialReturnReplace.getNewVault(),applicationContent1.getAppPrice(), 0,0,0,0);
        //添加申请单内容
        applicationContentMapper.add(Collections.singletonList(applicationContent));

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    /**
     * 根据让步单id查询更换记录
     * @param receiveId 让步/退货单主键id
     * @return 更换记录
     */
    public MaterialReturnReplace getByReceiveId(Integer receiveId){
        //获取mapper
        SqlSession sqlSession = factory.openSession();
        MaterialReturnReplaceMapper mapper = sqlSession.getMapper(MaterialReturnReplaceMapper.class);
        //调用mapper
        MaterialReturnReplace byReceiveId = mapper.getByReceiveId(receiveId);
        //释放资源
        sqlSession.close();
        //返回值
        return byReceiveId;
    }

}
