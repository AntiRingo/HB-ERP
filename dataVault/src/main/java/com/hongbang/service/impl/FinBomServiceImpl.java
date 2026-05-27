package com.hongbang.service.impl;

import com.hongbang.mapper.FinBomMapper;
import com.hongbang.mapper.FinBomTitleMapper;
import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.FinBomTitle;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;
import com.hongbang.service.FinBomService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinBomServiceImpl implements FinBomService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //根据fin_id查询是否存在BOM表信息
         public PageBean<Map<String,Object>> selectByPage(int finId){
             //获取session
             SqlSession sqlSession = factory.openSession();
             //获取mapper
             FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
//             int size=pageSize;
//             int begin=(currentPage-1)*size;
             //获取mapper
             List<Map<String, Object>> maps = mapper.selectByPage(finId);
             int i = mapper.totalCount(finId);
             PageBean<Map<String,Object>> pageBean = new PageBean<>();
             pageBean.setRows(maps);
             pageBean.setTotalCount(i);
             //释放资源
             sqlSession.close();
             //返回值
             return pageBean;


         }





    //循环添加BOM表信息
   public void add(List<FinBom>finBoms,int finId){
             //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);


       //再调用mapper添加
       if (finBoms.size()>0){
           //先删除
           mapper.deleteByUpdate(finId);

           mapper.add(finBoms);

       }else {
           //删除
           mapper.deleteByUpdate(finId);
       }

       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //查询当前BOM表下的所有产品信息，只要产品库的
    public  List<FinBom>selectBomVault(int finId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<FinBom> finBoms = mapper.selectBomVault(finId);

        //释放资源
        sqlSession.close();
        //返回值
        return finBoms;
    }


    //查询某个物料是否在BOM表中被使用
   public boolean ifUsed(int productId,int vault){
    //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
       //调用mapper
       boolean b = mapper.ifUsed(productId, vault);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询当前BOM表下的所有产品信息，只要产品库的
    public  List<FinBom>selectBomVaultAll(int finId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<FinBom> finBoms = mapper.selectBomVaultAll(finId);

        //释放资源
        sqlSession.close();
        //返回值
        return finBoms;
    }

    //查询当前BOM表下的内容
    public List<FinBom> selectBomContent(int finProductId){
             //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<FinBom> finBoms = mapper.selectBomContent(finProductId);
        //释放资源
        sqlSession.close();
        //返回值
        return finBoms;
    }


    //查询BOM表中物料信息的属性信息
    public List<Map<String,Object>> selectAttribute( int bomTitleId){
             //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAttribute(bomTitleId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询物料信息
    public List<Product> selectProduct(List<FinBom> finBoms){
             //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<Product> products = mapper.selectProduct(finBoms);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询BOM表的价格和数量（零件）
    public int selectPriceLj(int finProductId){
             //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        int i = mapper.selectPriceLj(finProductId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }


    //查询该BOM表中的产品信息
    public List<Map<String,Object>>selectFinFromBom(int finProductId){
             //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        List<Map<String,Object>> list = mapper.selectFinFromBom(finProductId);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //查询BOM表中的物料信息
   public FinBom selectProductById(int bomTitleId,int productId,int vault){
             //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
       //调用mapper
       FinBom finBom = mapper.selectProductById(bomTitleId, productId, vault);
       //释放资源
       sqlSession.close();
       //返回值
       return finBom;
   }

    public int totalCount(int finId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        //调用mapper
        int i = mapper.totalCount(finId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    public void addOther(FinBomTitle finBomTitle, List<FinBom> finBoms){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinBomMapper mapper = sqlSession.getMapper(FinBomMapper.class);
        FinBomTitleMapper mapper1 = sqlSession.getMapper(FinBomTitleMapper.class);
        //调用mapper
        mapper1.addBomTitle(finBomTitle);
        int id = finBomTitle.getId();
        for (int i = 0; i < finBoms.size(); i++) {
            finBoms.get(i).setBomTitleId(id);
        }
        mapper.add(finBoms);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }
}
