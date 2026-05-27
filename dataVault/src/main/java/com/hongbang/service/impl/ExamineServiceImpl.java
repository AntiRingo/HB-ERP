package com.hongbang.service.impl;

import com.hongbang.mapper.ExamineLogMapper;
import com.hongbang.mapper.ExamineMapper;
import com.hongbang.pojo.Examine;
import com.hongbang.pojo.ExamineLog;
import com.hongbang.service.ExamineService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class ExamineServiceImpl implements ExamineService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加信息申请单时，添加该申请单的审核信息
   public void addExamine(Examine examine){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ExamineMapper mapper = sqlSession.getMapper(ExamineMapper.class);
       //调用mapper
       mapper.addExamine(examine);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //审核功能（部长审核）
    public void updateMinister(int minister, int appFormId, ExamineLog examineLog){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineMapper mapper = sqlSession.getMapper(ExamineMapper.class);
        ExamineLogMapper mapper1 = sqlSession.getMapper(ExamineLogMapper.class);
        //调用mapper
        mapper.updateMinister(minister,appFormId);
        mapper1.addLog(examineLog);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }




    //根据申请单id查询审核内容
    public List<Examine> selectByAppId(int appFormId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ExamineMapper mapper = sqlSession.getMapper(ExamineMapper.class);
        //调用mapper
        List<Examine> examines = mapper.selectByAppId(appFormId);
        //释放资源
        sqlSession.close();
        //返回值
        return examines;
    }
}
