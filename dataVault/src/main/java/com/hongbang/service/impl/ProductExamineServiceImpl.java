package com.hongbang.service.impl;

import com.hongbang.mapper.FinProductMapper;
import com.hongbang.mapper.ProductExamineMapper;
import com.hongbang.mapper.ProductMapper;
import com.hongbang.pojo.ProductExamine;
import com.hongbang.service.ProductExamineService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class ProductExamineServiceImpl implements ProductExamineService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //添加审核的物料信息
    public void addProductExamine(ProductExamine productExamine){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        mapper.addProductExamine(productExamine);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //审核
    public void  updateStatus(ProductExamine productExamine){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        ProductMapper mapper1 = sqlSession.getMapper(ProductMapper.class);
        FinProductMapper mapper2 = sqlSession.getMapper(FinProductMapper.class);
        //查询正在登录的人

        //调用mapper
        mapper.updateStatus(productExamine);
        //获取数据
        int id = productExamine.getId();
        //查询数据
        ProductExamine productExamine1 = mapper.selectById(id);
        //获取审核数据
        int status = productExamine.getStatus();
        if (status==1){
            //审核通过
            //审核通过后要将申请的物料信息更新成正使用

            int vault = productExamine1.getVault();
            if (vault==0){
                //零件仓库
                mapper1.enableProduct(productExamine1.getProductId());
            }
            else if (vault==1){
                //产品仓库
                mapper2.enableFinProduct(productExamine1.getProductId());

            }



        }
        else if (status==2){
            //审核未通过

        }



        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询所有审核内容
    public List<ProductExamine> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //获取申请审核信息
        List<ProductExamine> productExamines = mapper.selectAll();
        //释放资源
        sqlSession.commit();
        //返回值
        return productExamines;
    }


    //查询所有数据（带物料号、申请人、审核人）
    public List<Map<String,Object>>selectAllData(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllData();
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据物料id查询产品的属性
    public List<Map<String,Object>>selectAttribute(int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAttribute(productId,vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询自己申请的物料信息
    public List<Map<String,Object>>selectMyApplications(int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectMyApplications(userId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //删除
    public void deleteApplication(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductExamineMapper mapper = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        mapper.deleteApplication(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }
}
