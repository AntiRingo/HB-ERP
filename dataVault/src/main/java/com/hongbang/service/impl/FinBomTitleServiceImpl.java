package com.hongbang.service.impl;

import com.hongbang.mapper.FinBomTitleMapper;
import com.hongbang.pojo.FinBomTitle;
import com.hongbang.pojo.PageBean;
import com.hongbang.service.FinBomTitleService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinBomTitleServiceImpl implements FinBomTitleService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();


    //根据productId查询BOM表标题信息
    public List<FinBomTitle> selectBomTitle(int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        List<FinBomTitle> finBomTitles = mapper.selectBomTitle(productId);
        //释放资源
        sqlSession.close();
        //返回值
        return finBomTitles;
    }


    //插入BOM表标题信息
   public void addBomTitle(FinBomTitle finBomTitle ,int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
       //设置作者
       finBomTitle.setAuthor(id);
       //调用mapper
       mapper.addBomTitle(finBomTitle);

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //查询物料号标题和BOM表信息
    public List<Map<String,Object>> selectBomTitleAndProduct(int finProductId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectBomTitleAndProduct(finProductId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //删除BOM表标题,一级BOM表
   public void delete(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
       //调用mapper
       mapper.delete(id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //修改BOM表列表信息
    public void update(FinBomTitle finBomTitle){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        mapper.update(finBomTitle);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //获取数据进行回显
    public FinBomTitle selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        FinBomTitle finBomTitle = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        return finBomTitle;
    }

    //搜索功能
    public PageBean<FinBomTitle> search(int currentPage, int pageSize, String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;
        //调用mapper
        List<FinBomTitle> search = mapper.search(begin, size, str);
        int i = mapper.searchCount(str);
        //设置pageBean
        PageBean<FinBomTitle> pageBean = new PageBean<>();
        pageBean.setRows(search);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;

    }

    //查询所有的BOM表标题
    public PageBean<FinBomTitle>selectAllBomTitle(int currentPage,int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        int size = pageSize;
        int begin=(currentPage - 1)*pageSize;
        //调用mapper
        List<FinBomTitle> finBomTitles = mapper.selectAllBomTitle(begin, size);
        int i = mapper.selectAllBomTitleCount();
        //设置pageBean
        PageBean<FinBomTitle> pageBean = new PageBean<>();
        pageBean.setRows(finBomTitles);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //根据物料号或者物料名称查询
    public PageBean<FinBomTitle>selectAllByProductName(int currentPage, int pageSize,  String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        int size=pageSize;
        int begin=(currentPage-1)*size;
        //调用mapper
        List<FinBomTitle> search = mapper.selectAllByProductName(begin, size, str);
        int i = mapper.selectAllByProductNameCount(str);
        //设置pageBean
        PageBean<FinBomTitle> pageBean = new PageBean<>();
        pageBean.setRows(search);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //查询BOM表是否有作者(true就是有作者，false就是没有作者)
    public boolean selectIfAuthor(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        boolean b = mapper.selectIfAuthor(id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //更新BOM表的作者
   public void updateAuthor(int author, int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
       //调用mapper
       mapper.updateAuthor(author,id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //查看是否是该BOM表的作者
   public boolean selectIsAuthor(int author, int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomTitleMapper mapper = sqlSession.getMapper(FinBomTitleMapper.class);
       //调用mapper
       boolean b = mapper.selectIsAuthor(author, id);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

}
