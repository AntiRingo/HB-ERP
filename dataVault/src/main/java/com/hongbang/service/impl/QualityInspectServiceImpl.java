package com.hongbang.service.impl;

import com.hongbang.mapper.QualityInspectMapper;
import com.hongbang.pojo.QualityInspect;
import com.hongbang.service.QualityInspectService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class QualityInspectServiceImpl implements QualityInspectService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    public int saveInspect(QualityInspect inspect) {
        SqlSession sqlSession = factory.openSession();
        QualityInspectMapper mapper = sqlSession.getMapper(QualityInspectMapper.class);
        int rows = mapper.insert(inspect);
        sqlSession.commit();
        sqlSession.close();
        return rows;
    }
}
