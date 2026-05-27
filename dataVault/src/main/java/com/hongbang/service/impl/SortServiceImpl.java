package com.hongbang.service.impl;

import com.hongbang.mapper.SortMapper;
import com.hongbang.pojo.Sort;
import com.hongbang.service.SortService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class SortServiceImpl implements SortService {

    //获取工厂
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    //查询一级分类
    public List<Sort> selectOneLevel() {
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOneLevel();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    @Override
    //获取二级分类
    public List<Sort>selectOtherLevel(int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOtherLevel(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    @Override
    //查询该数据的上一层都有什么（返回上一级）
    public List<Sort>selectLastLevel(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectLastLevel(id);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    @Override
    //获取全部分类
    public List<Sort>selectAll(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    @Override
    //增加分类
    public void addSort(Sort sort){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.addSort(sort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    @Override
    //查询分类中是否有数据存在
    public boolean selectExist(int parentsId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.selectExist(parentsId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }



    @Override
    //修改分类
    public void updateSort(Sort sort){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.updateSort(sort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    @Override
    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    public List<Map> selectLast(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Map> sorts = mapper.selectLast();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    @Override
    //查询该数据属于那个分类,传的是该数据的parentId
    public List<Sort>selectOfSort(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOfSort(id);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    @Override
    //根据id查询该分类的信息
    public List<Sort>selectSortById(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectSortById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return  sorts;
    }

    @Override
    //根据parentId查询该分类的level
    public int selectLevel(int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        int i = mapper.selectLevel(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    @Override
    //根据id删除分类
    public void deleteById(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //更新时查询编码是否存在
    public boolean ifCodeExist(String code,int level,int id,int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.ifCodeExist(code,level,id,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //一级分类修改的时候验证编码是否重复，查询零件库以及产品库两个库
   public boolean ifCodeExistFromTwoTable(String code,int level , int id,int parentId){
        //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.ifCodeExistFromTwoTable(code, level, id, parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //新增时查询编码是否存在
    public boolean ifCodeExistAdd(String code,int level,int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.ifCodeExistAdd(code,level,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //一级分类添加的是否验证编码是否重复，查询零件库以及产品库两个库
   public boolean ifCodeExistAddFromTwoTable( String code, int level, int parentId){
        //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.ifCodeExistAddFromTwoTable(code, level, parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //添加分类信息的时候验证分类名是否重复
    public boolean selectNameAdd(String name){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.selectNameAdd(name);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //更新分类信息时验证分类名是否重复
    public boolean selectNameUpdate( String name, int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.selectNameUpdate(name, id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }



    @Override
    public List<Sort>getData(int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> data = mapper.getData(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return data;
    }

    //查询所有子集
    @Override
   public   List<Map<String,Object>> selectAllDown(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllDown(id);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询该level级下的所有分类信息
    @Override
    public List<Sort>selectByLevel(int level){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectByLevel(level);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //循环更新编码
    @Override
    public void updateCodes(List<Sort>sorts){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.updateCodes(sorts);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述

    public  void updateSortExceptCode(Sort sort){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.updateSortExceptCode(sort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //检查是否有id为0的数据
   public boolean  select0(){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.select0();
       //释放资源
       sqlSession.close();
       //返回数据
       return b;
   }

    //将-1改为0
  public  void update0(){
      //获取session
      SqlSession sqlSession = sqlSessionFactory.openSession();
      //获取mapper
      SortMapper mapper = sqlSession.getMapper(SortMapper.class);
      //调用mapper
      mapper.update0();
      //提交事务
      sqlSession.commit();
      //释放资源
      sqlSession.close();

  }

    //查询id为0的分类，也就是公共属性使用的单独分类
   public List<Sort>selectId0(){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       List<Sort> sorts = mapper.selectId0();
       //释放资源
       sqlSession.close();
       //返回值
       return sorts;
   }



//------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //以下是excel导入用到的代码

    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
   public boolean selectSortIfExist(Sort sort){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.selectSortIfExist(sort);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查询名称是否重复
   public boolean selectSortNameIfExist(Sort sort){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.selectSortNameIfExist(sort);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询编码是否重复
   public boolean selectSortCodeIfExist(Sort sort){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.selectSortCodeIfExist(sort);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //根据level，parent，code查询分类数据
    public Sort selectSortCode(Sort sort){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        Sort sort1 = mapper.selectSortCode(sort);
        //释放资源
        sqlSession.close();
        //返回值
        return sort1;
    }
    //添加分类
   public void addSortExcel(Sort sort){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       mapper.addSortExcel(sort);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();

   }

    //更新分类
   public void updateSortExcel(Sort sort){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       mapper.updateSortExcel(sort);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }//以上的添加和更新只适用于前三级


    //添加分类，循环添加，四级分类使用
    public void addSortFourExcel(List<Sort>sortList){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        mapper.addSortFourExcel(sortList);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据名称查询分类id
   public int  selectIdExcel(String name){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       int i = mapper.selectIdExcel(name);
       //释放资源
       sqlSession.close();
       return i;
   }




    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    public List<Sort> selectByCode (int level, String code, int parentId){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectByCode(level, code,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------
    //下面是数量页面用到的

    //查询有物料的分类
    public List<Sort>selectHaveProduct(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectHaveProduct();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //查询所有二级分类
    public List<Sort> selectTwoLevel(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectTwoLevel();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    //查询分类下的物料数量
   public int selectNumber(int parentId){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       int i = mapper.selectNumber(parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return i;

   }


   //查询所有的一级分类
    public  List<Sort>selectOne(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOne();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }


    //根据属性id查询该属性所在的分类下是否有物料信息
    public boolean selectIfExistProductByNameId(int id){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        boolean b = mapper.selectIfExistProductByNameId(id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询名称和编码是否相同
   public boolean nameAndCodeIfExist( String name,String code){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       boolean b = mapper.nameAndCodeIfExist(name,code);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查询所有的四级分类
    public List<Sort> selectAllLevelFour(){
        //获取session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper
        SortMapper mapper = sqlSession.getMapper(SortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectAllLevelFour();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //根据产品信息查询分类
   public List<Sort>selectByProduct(int productId,  int vault){
       //获取session
       SqlSession sqlSession = sqlSessionFactory.openSession();
       //获取mapper
       SortMapper mapper = sqlSession.getMapper(SortMapper.class);
       //调用mapper
       List<Sort> sorts = mapper.selectByProduct(productId, vault);
       //释放资源
       sqlSession.close();
       //返回值
       return sorts;
   }
}

