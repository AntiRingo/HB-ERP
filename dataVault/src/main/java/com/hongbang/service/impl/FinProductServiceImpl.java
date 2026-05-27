package com.hongbang.service.impl;

import com.hongbang.mapper.FinProductMapper;
import com.hongbang.mapper.ProductExamineMapper;
import com.hongbang.pojo.FinProduct;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;
import com.hongbang.pojo.ProductExamine;
import com.hongbang.service.FinProductService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FinProductServiceImpl implements FinProductService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询该分类下是否有物料
    public boolean selectByParentId(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectByParentId(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询该分类下的所有物料( )
    public List<FinProduct> selectAllInSort(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<FinProduct> finProducts = mapper.selectAllInSort(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return finProducts;

    }


    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
    public List<FinProduct>selectAllInSortDeleteSign(int parentId, String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<FinProduct> finProducts = mapper.selectAllInSortDeleteSign(parentId, name);
        //释放资源
        sqlSession.close();
        //返回值
        return finProducts;
    }

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时使用，已弃用的和未弃用的都查询 )
   public List<FinProduct>selectAllInSortDeleteSignUpdate(int parentId,String name, int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       //调用mapper
       List<FinProduct> finProducts = mapper.selectAllInSortDeleteSignUpdate(parentId, name, id);
       //释放资源
       sqlSession.close();
       //返回值
       return finProducts;
   }

    //查询是否有物料存在
    @Override
    public boolean ifProduct(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.ifProduct();
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询所有的物料信息
    public List<FinProduct> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<FinProduct> finProducts = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return finProducts;
    }

    //新增物料
    @Override
    public void add(FinProduct finProduct,int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        ProductExamineMapper mapper1 = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        mapper.add(finProduct);
        //获取新增的物料id
        int id = finProduct.getId();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        //设置审核申请
        ProductExamine productExamine = new ProductExamine(0,userId,id,1, 0,null,formattedTime,0);
        System.out.println(productExamine);
        //添加审核申请
        mapper1.addProductExamine(productExamine);

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //删除物料
    @Override
    public void deleteById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //重新启用
    public void enableFinProduct(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        mapper.enableFinProduct(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //真正删除
    public void deleteReally(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        mapper.deleteReally(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //根据已有的物料id，筛选出来在某个分类下的物料id
    public List<Integer> selectPidBySidAndPid(int sortId,List<Integer> productsId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);

        //调用mapper
        List<Integer> list = mapper.selectPidBySidAndPid(sortId, productsId);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //模糊查询(搜索已弃用的物料信息，不分页)
    public List<Product> searchAbandoned(String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Product> products = mapper.searchAbandoned(str);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询当前分类下有没有物料名称相同的物料(添加)
    public boolean selectAddNameIfExist(FinProduct finProduct){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectAddNameIfExist(finProduct);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询物料号是否重复(新增物料的时候)
    @Override
    public boolean selectMN(int finSortId,String finMaterialNumber){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectMN(finSortId, finMaterialNumber);
        //释放资源
        sqlSession.close();
        //返回值
        return b;

    }



    //查询该分类下的物料
    @Override
    public PageBean<FinProduct> selectByPid(int finSortId, int currentPage, int pageSize) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<FinProduct> finProducts = mapper.selectByPid(finSortId, begin, size);
        int i = mapper.totalCounts(finSortId);
        //封装pagebean
        PageBean<FinProduct> pageBean = new PageBean<>();
        pageBean.setRows(finProducts);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //获取物料信息进行回显
    @Override
    public List<FinProduct>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<FinProduct> finProducts = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finProducts;
    }

    //获取物料信息进行回显
    @Override
    public List<Product>selectByIdAsProduct(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectByIdAsProduct(id);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }


    //更新物料信息
    @Override
    public void updateById(FinProduct finProduct){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        mapper.updateById(finProduct);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }



    //查询当前分类下有没有物料名称相同的物料(修改)
    public boolean selectNameIfExist(FinProduct finProduct){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectNameIfExist(finProduct);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
    //查询物料号是否重复(新增物料的时候)
    @Override
    public boolean selectUpMN(int finSortId,String finMaterialNumber,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectUpMN(finSortId, finMaterialNumber,id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;

    }

    //根据分类id查询该分类下是否有产品
    @Override
    public boolean selectIfProduct(int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        boolean b = mapper.selectIfProduct(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询（添加BOM表信息时查询两个表中的数据）
   public  PageBean<Map<String,Object>> searchFromTwoTable(String str, int currentPage, int pageSize){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       int size=pageSize;
       int begin=(currentPage-1)*size;

       //调用mapper
       List<Map<String, Object>> maps = mapper.searchFromTwoTable(str, begin, size);
       int i = mapper.totalCount(str);
       PageBean<Map<String,Object>> pageBean = new PageBean<>();
       pageBean.setRows(maps);
       pageBean.setTotalCount(i);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;

   }


    //显示页面使用list<Product>是正确的   ------------------------------------------------------------------------------------------------------------------------

    //查询该分类下的所有物料( )
   public List<Product> selectAllInSortDisplay( int finSortId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       //调用mapper
       List<Product> products = mapper.selectAllInSortDisplay(finSortId);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }


    //查询product基本信息
   public List<Product>selectProductByAllIdDisplay(List<Integer>integers){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       //调用mapper
       List<Product> products = mapper.selectProductByAllIdDisplay(integers);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
    }

    //筛选后的结果，按价格正序
   public List<Product>BrandAscDisplay( List<Integer>integers){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       //调用mapper
       List<Product> products = mapper.BrandAscDisplay(integers);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }

    //筛选后的结果，按价格倒叙
    public List<Product>BrandDescDisplay(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Product> products = mapper.BrandDescDisplay(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }



    //查询该分类下的所有物料,根据库存数量倒叙
    public List<Product>NumberDescDisplay(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Product> products = mapper.NumberDescDisplay(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询该分类下的所有物料,根据库存数量正序
    public List<Product>NumberAscDisplay(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Product> products = mapper.NumberAscDisplay(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询该分类下所有的物料信息，分页查询
    public PageBean<Product>selectAllInSortLimitDisplay(List<Map<String,Object>>maps,int currentPage,int pageSize,int ifs,int price,int priceDesc,int number,int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<Product> products = mapper.selectAllInSortLimitDisplay(maps, begin, size,ifs,price,priceDesc, number, numberDesc);
        int i = mapper.selectAllInSortLimitCount(maps);
        PageBean<Product>pageBean = new PageBean<>();
        pageBean.setRows(products);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }
    //查询该分类下所有的物料信息，分页查询
    public PageBean<Product>selectAllInSortLimitDisplayOnly(int parentId,int currentPage,int pageSize,int ifs,int price,int priceDesc,int number,int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<Product> products = mapper.selectAllInSortLimitDisplayOnly(parentId, begin, size,ifs,price,priceDesc, number, numberDesc);
        int i = mapper.selectAllInSortLimitCountOnly(parentId);
        PageBean<Product>pageBean = new PageBean<>();
        pageBean.setRows(products);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //查询已经弃用的物料信息
    public PageBean<Product> selectAbandonedFinProduct(int currentPage, int pageSize){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        int i = mapper.selectAbandonedFinCount();
        int size = pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<Product> products = mapper.selectAbandonedFinProduct(begin,size);
        PageBean<Product> pageBean = new PageBean<>();
        pageBean.setTotalCount(i);
        pageBean.setRows(products);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }


    //查询该分类下的所有产品信息，不包括自己
    public List<FinProduct> selectCopyBySort( int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //获取mapper
        List<FinProduct> finProducts = mapper.selectCopyBySort(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finProducts;
    }

    //查询当前分类下的信息不包括自己
   public List<FinProduct> selectSearchCopyBySort(int id,String str){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
       //获取mapper
       List<FinProduct> finProducts = mapper.selectSearchCopyBySort(id,str);
       //释放资源
       sqlSession.close();
       //返回值
       return finProducts;
   }


    //导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    public  List<FinProduct> selectProductByMaterialNumber(String materialNumber){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<FinProduct> finProduct = mapper.selectProductByMaterialNumber(materialNumber);
        //释放资源
        sqlSession.close();
        //返回值
        return finProduct;
    }

//   搜索 ----------------------------------------------------------------
//搜索功能，不带单位
public   List<Integer> searchProduct(String str,int parentId){
    //获取session
    SqlSession sqlSession = factory.openSession();
    //获取mapper
    FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
    //调用mapper
    List<Integer> integers = mapper.searchProduct(str, parentId);
    //释放资源
    sqlSession.close();
    //返回值
    return integers;
}

    //搜索功能，带单位
    public   List<Integer> searchProductUnit(String str,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchProductUnit(str, parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }



    //更新价格
    public void updatePrice(String price,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinProductMapper mapper = sqlSession.getMapper(FinProductMapper.class);
        //调用mapper
        mapper.updatePrice(price,id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }
}
