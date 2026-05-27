package com.hongbang.service.impl;

import com.hongbang.mapper.PublicFileMapper;
import com.hongbang.mapper.PublicNoticeMapper;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.PublicFile;
import com.hongbang.pojo.PublicNotice;
import com.hongbang.service.PublicNoticeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PublicNoticeServiceImpl implements PublicNoticeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //添加
   public void addPublicNotice(PublicNotice publicNotice, List<PublicFile> publicFiles){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       PublicNoticeMapper mapper = sqlSession.getMapper(PublicNoticeMapper.class);
       PublicFileMapper mapper1 = sqlSession.getMapper(PublicFileMapper.class);
       // 获取服务器当前时间
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       String formattedTime = LocalDateTime.now().format(formatter);
       publicNotice.setDate(formattedTime);
       //调用mapper
       mapper.addPublicNotice(publicNotice);
       int id = publicNotice.getId();

       for (int i = 0; i < publicFiles.size(); i++) {
           PublicFile publicFile = publicFiles.get(i);
           publicFile.setpId(id);
       }
       if (publicFiles.size()>0){
           mapper1.addPubicFile(publicFiles);
       }

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //查询
   public PageBean<PublicNotice> selectAll(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       PublicNoticeMapper mapper = sqlSession.getMapper(PublicNoticeMapper.class);
       //计算
//       int size = pageSize;
//       int begin = (currentPage - 1)* size;
       //调用mapper
       List<PublicNotice> publicNotices = mapper.selectAll();
       int i = mapper.selectAllCount();
       //设置pageBean
       PageBean<PublicNotice> pageBean = new PageBean<>();
       pageBean.setRows(publicNotices);
       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;
   }



}
