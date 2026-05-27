package com.hongbang.service.impl;


import com.hongbang.mapper.ProductExamineMapper;
import com.hongbang.mapper.ProductMapper;
import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.Product;
import com.hongbang.pojo.ProductExamine;
import com.hongbang.service.ProductService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {

    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //新增物料
    @Override
    public void add(Product product,int userId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        ProductExamineMapper mapper1 = sqlSession.getMapper(ProductExamineMapper.class);
        //调用mapper
        mapper.add(product);
        //获取新增的物料id
        int id = product.getId();
        //获取服务器时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        //设置审核申请
        ProductExamine productExamine = new ProductExamine(0,userId,id,product.getVault(), 0,null,formattedTime,0);
        //添加审核申请
        mapper1.addProductExamine(productExamine);

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //获取物料信息进行回显
    @Override
    public List<Product>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //更新物料信息
    @Override
    public void updateById(Product product){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        mapper.updateById(product);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询该分类下的物料
    @Override
    public PageBean<Product> selectByPid(int parentId,int currentPage,int pageSize) {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        int size=pageSize;
        int begin = (currentPage-1)*size;
        //调用mapper
        List<Product> products = mapper.selectByPid(parentId,begin,size);
        int i = mapper.totalCounts(parentId);
        //封装pagebean
        PageBean<Product> pageBean = new PageBean<>();
        pageBean.setRows(products);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
       return pageBean;
    }


    //删除物料
    @Override
    public void deleteById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        mapper.deleteById(id);
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
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       mapper.deleteReally(id);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //重新启用
    public void enableProduct(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        mapper.enableProduct(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //查询该分类下是否有物料
    @Override
    public boolean selectByParentId(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.selectByPrentId(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //模糊查询（搜索）
    @Override
    public PageBean<Product> search(int currentPage, int pageSize, String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        int size=pageSize;
        int begin=(currentPage-1)*pageSize;
        //调用mapper
        List<Product> search = mapper.search(begin, size, str);
        int i = mapper.totalCount(str);
        //封装pagebean
        PageBean<Product> pageBean = new PageBean<>();
        pageBean.setRows(search);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }

    //模糊查询(搜索已弃用的物料信息，不分页)
    public List<Product> searchAbandoned(String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.searchAbandoned(str);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }


    //查询该分类下的所有物料( )
    @Override
    public List<Product>selectAllInSort(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectAllInSort(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return products;

    }

    //查询该分类下的所有物料(添加物料信息时查询是否跟已启用的物料信息比较时实用 )
    public List<Product>selectAllInSortDeleteSign(int parentId,String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectAllInSortDeleteSign(parentId,name);
        //释放资源
        sqlSession.close();
        //返回值
        return products;

    }

    //查询该分类下的所有物料(修改物料信息时查询是否跟已启用的物料信息比较时实用 )
    public List<Product>selectAllInSortDeleteSignUpdate(int parentId,String name, int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectAllInSortDeleteSignUpdate(parentId,name,id);
        //释放资源
        sqlSession.close();
        //返回值
        return products;

    }

    //查询该分类下所有的物料信息，分页查询(包含分类中分类的物料信息)
    public PageBean<Product>selectAllInSortLimit(List<Map<String,Object>> maps,int currentPage,int pageSize,int ifs,int price,int priceDesc,int number,int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<Product> products = mapper.selectAllInSortLimit(maps, begin, size,ifs,price,priceDesc, number, numberDesc);
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
    public PageBean<Product>selectAllInSortLimitOnly(int parentId,int currentPage,int pageSize,int ifs,int price,int priceDesc,int number,int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<Product> products = mapper.selectAllInSortLimitOnly(parentId, begin, size,ifs,price,priceDesc, number, numberDesc);
        int i = mapper.selectAllInSortLimitCountOnly(parentId);
        PageBean<Product>pageBean = new PageBean<>();
        pageBean.setRows(products);
        pageBean.setTotalCount(i);
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }
    //根据物料信息查询所有的物料信息，分页查询
  public PageBean<Product>selectLimitByProduct(List<Product>products,  int currentPage, int pageSize, int ifs , int price,int priceDesc, int number, int numberDesc){

      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
      int begin = (currentPage-1)*pageSize;
      int size = pageSize;
      //调用mapper
      List<Product> products1 = mapper.selectLimitByProduct(products, begin, size, ifs, price, priceDesc, number, numberDesc);

      PageBean<Product>pageBean = new PageBean<>();
      pageBean.setRows(products1);
      pageBean.setTotalCount(products.size());
      //释放资源
      sqlSession.close();
      //返回值
      return pageBean;
  }

    //根据物料信息查询所有的物料信息，分页查询
    public PageBean<Product>selectLimitByFinProduct(List<Product>products,  int currentPage, int pageSize, int ifs , int price,int priceDesc, int number, int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        int begin = (currentPage-1)*pageSize;
        int size = pageSize;
        //调用mapper
        List<Product> products1 = mapper.selectLimitByFinProduct(products, begin, size, ifs, price, priceDesc, number, numberDesc);
        PageBean<Product>pageBean = new PageBean<>();
        pageBean.setRows(products1);
        pageBean.setTotalCount(products.size());
        //释放资源
        sqlSession.close();
        //返回值
        return pageBean;
    }



    //根据已有的数据进行排序，分页查询
   public List<Product>selectAllInSortLimitOrderBy(List<Product>products,int ifs,int price,int priceDesc,int number,int numberDesc){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Product> products1 = mapper.selectAllInSortLimitOrderBy(products, ifs, price, priceDesc, number, numberDesc);
       //释放资源
       sqlSession.close();
       //返回值
       return products1;
   }




    //查询是否有物料存在
    @Override
    public boolean ifProduct(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.ifProduct();
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询物料号是否重复(新增物料的时候)
    @Override
     public boolean selectMN(int parentId,String materialNumber){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.selectMN(parentId, materialNumber);
        //释放资源
        sqlSession.close();
        //返回值
        return b;

    }


    //查询是否跟弃用的物料号重复
   public List<Product> selectMNAbandoned(int parentId, String materialNumber,String name){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Product> products = mapper.selectMNAbandoned(parentId, materialNumber,name);
       //释放资源
       sqlSession.close();
       //返回值
       return products;

   }


    //查询物料号是否重复(新增物料的时候)
    @Override
    public boolean selectUpMN(int parentId,String materialNumber,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.selectUpMN(parentId, materialNumber,id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;

    }

    //根据分类id查询该分类下是否有产品
    @Override
   public boolean selectIfProduct(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.selectIfProduct(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询当前分类下有没有物料名称相同的物料(修改)
   public boolean selectNameIfExist(Product product){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       boolean b = mapper.selectNameIfExist(product);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询当前分类下有没有物料名称相同的物料(添加)
    public boolean selectAddNameIfExist(Product product){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        boolean b = mapper.selectAddNameIfExist(product);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询所有的物料信息
    public List<Product> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询已经弃用的物料信息
   public PageBean<Product> selectAbandonedProduct(int currentPage, int pageSize){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       int i = mapper.selectAbandonedCount();
       int size = pageSize;
       int begin = (currentPage-1)*size;
       //调用mapper
       List<Product> products = mapper.selectAbandonedProduct(begin,size);
       PageBean<Product> pageBean = new PageBean<>();
       pageBean.setTotalCount(i);
       pageBean.setRows(products);
       //释放资源
       sqlSession.close();
       //返回值
       return pageBean;
   }



    //下面是excel表导入用到的
    //查询该分类下是否有产品

    public List<Product> selectProductExistExcel(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectProductExistExcel(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //数量页面使用
    //查询总数
   public int selectTotalCount(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       int i = mapper.selectTotalCount();
       //释放资源
       sqlSession.close();
       //返回值
       return i;
   }


    //搜索功能，不带单位
    public   List<Integer> searchProduct(String str,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
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
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchProductUnit(str, parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }

    //查询product基本信息
   public List<Product>selectProductByAllId(List<Integer>integers){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Product> products = mapper.selectProductByAllId(integers);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }




    //在搜索结果中搜索
   public List<Integer>selectInSelectP(List<Product>products,String str){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Integer> list = mapper.selectInSelectP(products, str);
       //释放资源
       sqlSession.close();
       //返回值
       return list;
   }


//    //查询该分类下的所有物料,根据价格倒叙
//  public  List<Product>selectAllInSortByBrandDesc(int parentId,int currentPage,int pageSize){
//      //获取session
//      SqlSession sqlSession = factory.openSession();
//      //获取mapper
//      ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
//      //调用mapper
//      List<Product> products = mapper.selectAllInSortByBrandDesc(parentId);
//      //释放资源
//      sqlSession.close();
//      //返回值
//      return products;
//
//  }
//    //查询该分类下的所有物料,根据价格正序
//   public List<Product>selectAllInSortByBrandAsc(int parentId,int currentPage,int pageSize){
//       //获取session
//       SqlSession sqlSession = factory.openSession();
//       //获取mapper
//       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
//       //调用mapper
//       List<Product> products = mapper.selectAllInSortByBrandAsc(parentId);
//       //释放资源
//       sqlSession.close();
//       //返回值
//       return products;
//   }
//
//    //查询该分类下的所有物料,根据库存数量倒叙
//   public List<Product>selectAllInSortByNumberDesc(int parentId,int currentPage,int pageSize){
//       //获取session
//       SqlSession sqlSession = factory.openSession();
//       //获取mapper
//       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
//       //调用mapper
//       List<Product> products = mapper.selectAllInSortByNumberDesc(parentId);
//       //释放资源
//       sqlSession.close();
//       //返回值
//       return products;
//   }
//    //查询该分类下的所有物料,根据库存数量正序
//   public List<Product>selectAllInSortByNumberAsc(int parentId,int currentPage,int pageSize){
//       //获取session
//       SqlSession sqlSession = factory.openSession();
//       //获取mapper
//       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
//       //调用mapper
//       List<Product> products = mapper.selectAllInSortByNumberAsc(parentId);
//       //释放资源
//       sqlSession.close();
//       //返回值
//       return products;
//   }


    //筛选后的结果，按价格倒叙
    public List<Product>BrandDesc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.BrandDesc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //筛选后的结果，按价格正序
   public List<Product>BrandAsc(List<Integer>integers){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Product> products = mapper.BrandAsc(integers);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }

    //查询该分类下的所有物料,根据库存数量倒叙
    public List<Product>NumberDesc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.NumberDesc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询该分类下的所有物料,根据库存数量正序
    public List<Product>NumberAsc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.NumberAsc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }


    //查询product基本信息根据价格正序，搜索
    public List<Product>selectProductByBrandAsc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectProductByBrandAsc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询product基本信息根据价格正序，倒叙
    public List<Product>selectProductByBrandDesc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectProductByBrandDesc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;

    }


    //查询product基本信息根据库存数量正序，搜索
   public List<Product>selectProductByNumberAsc(List<Integer>integers){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Product> products = mapper.selectProductByNumberAsc(integers);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }

    //查询product基本信息根据库存数量正序，搜索
  public List<Product>selectProductByNumberDesc(List<Integer>integers){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
      //调用mapper
      List<Product> products = mapper.selectProductByNumberDesc(integers);
      //释放资源
      sqlSession.close();
      //返回值
      return products;
  }


    //根据本地储存在数据查询产品信息在前端显示
   public Product selectProductByLocation(Product products){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
        Product  products1 = mapper.selectProductByLocation(products);
       //释放资源
       sqlSession.close();
       //返回值
       return products1;
   }

    //根据物料id查询content 和mapping
    public List<Map<String,Object>> jz(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Map<String, Object>> jz = mapper.jz(id);
        //释放资源
        sqlSession.close();
        //返回值
        return jz;
    }


    //根据id循环更新物料号
    public void updateMaterialNumberById(List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        mapper.updateMaterialNumberById(products);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //入库出库操作
  public void  vault (List<ApplicationContent> applicationContents){
        //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
      //调用mapper
      mapper.vault(applicationContents);
      //释放资源
      sqlSession.close();
  }


    //显示页面使用--------------------------------------------------------------------------------------------------------------------------------------------------------------



    //搜索功能，不带单位
    public   List<Integer> searchFinProduct(String str,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchFinProduct(str, parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }


    //查询product基本信息
    public List<Product>selectFinProductByAllId(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectFinProductByAllId(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //搜索功能，带单位
    public   List<Integer> searchFinProductUnit(String str,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchFinProductUnit(str, parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }


    //根据已有的数据进行排序，分页查询
    public List<Product>selectAllInFinSortLimitOrderBy(List<Product>products,int ifs,int price,int priceDesc,int number,int numberDesc){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products1 = mapper.selectAllInFinSortLimitOrderBy(products, ifs, price, priceDesc, number, numberDesc);
        //释放资源
        sqlSession.close();
        //返回值
        return products1;
    }

    //在搜索结果中搜索
    public List<Integer>selectInSelectFP(List<Product>products,String str){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        System.out.println(products.size());
        List<Integer> list = mapper.selectInSelectFP(products, str);

        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }


    //查询product基本信息根据价格正序，搜索
    public List<Product>selectFinProductByBrandAsc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectFinProductByBrandAsc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询product基本信息根据价格正序，倒叙
    public List<Product>selectFinProductByBrandDesc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectFinProductByBrandDesc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;

    }


    //查询product基本信息根据库存数量正序，搜索
    public List<Product>selectFinProductByNumberAsc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectFinProductByNumberAsc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询product基本信息根据库存数量正序，搜索
    public List<Product>selectFinProductByNumberDesc(List<Integer>integers){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectFinProductByNumberDesc(integers);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }

    //查询该分类最后插入的物料号，为了获取最后两位流水码
   public List<Map<String,Object>> selectMaterialNumber(int sortId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectMaterialNumber(sortId);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }

    //根据已有的物料id，筛选出来在某个分类下的物料id
    public List<Integer> selectPidBySidAndPid(int sortId,List<Integer> productsId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);

        //调用mapper
        List<Integer> list = mapper.selectPidBySidAndPid(sortId, productsId);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //查询物料信息（添加BOM表的时候查看）
     public List<Map<String,Object>>selectProductWhenAddBom(int id,int vault){
        //获取session
         SqlSession sqlSession = factory.openSession();
         //获取mapper
         ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
         //调用mapper
         List<Map<String, Object>> maps = mapper.selectProductWhenAddBom(id, vault);
         //释放资源
         sqlSession.close();
         //返回值
         return maps;
     }


    //导入BOM表使用--------------------------------------------------------------------------------------------------------------------------------------------------------------

    //根据物料号查询物料信息
    public  List<Product> selectProductByMaterialNumber(String materialNumber){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> product = mapper.selectProductByMaterialNumber(materialNumber);
        //释放资源
        sqlSession.close();
        //返回值
        return product;
    }

    //---------------------------------------------------------------------------------
//根据物料id和仓库查询价格
    public double selectPrice(int productId,int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        double v = mapper.selectPrice(productId, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return v;
    }



    //根据物料号查询物料的信息
    public List<Map<String,Object>> selectByMaterialNumber(String materialNumber){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectByMaterialNumber(materialNumber);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //根据物料名更新产品的数量
    public void updateNumberByName(List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        mapper.updateNumberByName(products);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据物料名称查询产品数据
    public List<Product> selectProductByName(String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectProductByName(name);
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    //结存使用
    public List<Product> selectJieCun(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
        //调用mapper
        List<Product> products = mapper.selectJieCun();
        //释放资源
        sqlSession.close();
        //返回值
        return products;
    }
}


