package com.hongbang.service.impl;

import com.hongbang.mapper.PublicFileMapper;
import com.hongbang.pojo.PublicFile;
import com.hongbang.service.PublicFileService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class PublicFileServiceImpl implements PublicFileService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //查询文件信息
    public List<PublicFile> selectFile(int Pid){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        PublicFileMapper mapper = sqlSession.getMapper(PublicFileMapper.class);
        //调用mapper
        List<PublicFile> publicFiles = mapper.selectFile(Pid);
        //释放资源
        sqlSession.close();
        //返回值
        return publicFiles;
    }
}
