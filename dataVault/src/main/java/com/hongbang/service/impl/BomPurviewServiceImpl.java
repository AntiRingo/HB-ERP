package com.hongbang.service.impl;

import com.hongbang.mapper.BomPurviewMapper;
import com.hongbang.mapper.BomPurviewReviewMapper;
import com.hongbang.pojo.BomPurview;
import com.hongbang.pojo.BomPurviewReview;
import com.hongbang.service.BomPurviewService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BomPurviewServiceImpl implements BomPurviewService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //查询某个BOM表可以查看的用户
   public List<BomPurview> selectUserPurview(int id){

       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);
       //调用mapper
       List<BomPurview> list = mapper.selectUserPurview(id);
       //释放资源
       sqlSession.close();
       //返回值
       return list;
   }

    //删除某个BOM表的所有权限
    public void deletePurview(int finBomTitleId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);
        //调用mapper
        mapper.deletePurview(finBomTitleId);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //删除后循环添加权限
    public void addPurview(BomPurviewReview bomPurviewReview,List<BomPurview> bomPurview, int finBomTitleId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);


//        //调用mapper

        if (bomPurview.size()>0){
            mapper.deletePurview(finBomTitleId);
            //添加
            mapper.addPurview(bomPurview);
        }
        else {
            //直接删除现在所有的
            mapper.deletePurview(finBomTitleId);
        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //添加作者修改的用户
   public void addAuthorUserPurview(BomPurviewReview bomPurviewReview, List<BomPurview> bomPurview, int finBomTitleId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);
       BomPurviewReviewMapper mapper1 = sqlSession.getMapper(BomPurviewReviewMapper.class);

//        //调用mapper
//        mapper.deletePurview(finBomTitleId);
       if (bomPurview.size()>0){
           //先添加申请单数据
           mapper1.addBomReviewApp(bomPurviewReview);
           int id = bomPurviewReview.getId();
           //给申请权限内容设置申请单Id
           for (int i = 0; i < bomPurview.size(); i++) {
               BomPurview bomPurview1 = bomPurview.get(i);
               bomPurview1.setReviewAppId(id);
           }
           //添加
           mapper.addPurview(bomPurview);
       }
       else {
           //直接删除现在所有的
           mapper.deletePurview(finBomTitleId);
       }

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }


    //查询某个BOM表中某个用户是否有查看权限
    public boolean selectHavePurview(int finBomTitleId,  int userPurview){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);
        //调用mapper
        boolean b = mapper.selectHavePurview(finBomTitleId, userPurview);
        //释放资源
        sqlSession.close();
        //返回值
        return b;

    }


    //查询某个BOM表中某个用户是否有编辑权限
    public boolean selectHaveCanEditPurview(int finBomTitleId, int userPurview){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        BomPurviewMapper mapper = sqlSession.getMapper(BomPurviewMapper.class);
        //调用mapper
        boolean b = mapper.selectHaveCanEditPurview(finBomTitleId, userPurview);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
}
