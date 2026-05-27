package com.hongbang.service.impl;

import com.hongbang.mapper.FinSortMapper;
import com.hongbang.mapper.SortMapper;
import com.hongbang.pojo.FinSort;
import com.hongbang.pojo.Mapping;
import com.hongbang.pojo.Sort;
import com.hongbang.service.FinSortService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinSortServiceImpl implements FinSortService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //获取其他级分类
   public List<FinSort> selectOtherLevel(int parentId){
      //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
       //调用mapper
       List<FinSort> finSorts = mapper.selectOtherLevel(parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return finSorts;


   }

    //查询该数据属于那个分类,传的是该数据的parentId
    public List<FinSort>selectOfSort(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectOfSort(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }

    //查询该数据的上一层都有什么（返回上一级）
    public List<FinSort>selectLastLevel(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectLastLevel(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }

    //查询一级分类
    public List<FinSort> selectOneLevel() {
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectOneLevel();
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }

    //根据parentId查询该分类的level
    public int selectLevel(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        int i = mapper.selectLevel(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //添加分类信息的时候验证分类名是否重复
    public boolean selectNameAdd(String name){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.selectNameAdd(name);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //新增时查询编码是否存在
    public boolean ifCodeExistAdd(String code,int level,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.ifCodeExistAdd(code,level,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //增加分类
    public void addSort(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.addSort(finSort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //根据id删除分类
    public void deleteById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id查询该分类的信息
    public List<FinSort>selectSortById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectSortById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }


    //查询所有子集
    public   List<Map<String,Object>> selectAllDown(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllDown(id);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //更新分类信息时验证分类名是否重复
    public boolean selectNameUpdate( String finSortName, int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.selectNameUpdate(finSortName, id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //更新时查询编码是否存在
    public boolean ifCodeExist(String finSortCode,int finSortLevel,int id,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.ifCodeExist(finSortCode,finSortLevel,id,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //修改分类
    public void updateSort(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.updateSort(finSort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述

    public  void updateSortExceptCode(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.updateSortExceptCode(finSort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询该level级下的所有分类信息
    @Override
    public List<FinSort>selectByLevel(int finSortLevel){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectByLevel(finSortLevel);
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }

    //循环更新编码
    @Override
    public void updateCodes(List<FinSort>finSorts){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.updateCodes(finSorts);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //检查是否有id为0的数据
    public boolean  select0(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
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
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.update0();
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //查询id为0的分类，也就是公共属性使用的单独分类
    public List<FinSort>selectId0(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectId0();
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }

    @Override
    //查询分类中是否有数据存在
    public boolean selectExist(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.selectExist(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    @Override
    //获取全部分类
    public List<FinSort>selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }



    @Override
    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    public List<Map> selectLast(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<Map> sorts = mapper.selectLast();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //查询所有的一级分类
    public  List<FinSort>selectOne(){
        //获取session
        SqlSession sqlSession =factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> finSorts = mapper.selectOne();
        //释放资源
        sqlSession.close();
        //返回值
        return finSorts;
    }


    //查询分类下的物料数量
    public int selectNumber(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        int i = mapper.selectNumber(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;

    }



//  显示页面要用  -----------------------------------------------------------------------------------------------------------------------
    //查询所有的一级分类（list<sort>是对的）

   public List<Sort>selectOneDisplay(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
       //调用mapper
       List<Sort> sorts = mapper.selectOneDisplay();
       //释放资源
       sqlSession.close();
       //返回值
       return sorts;
   }

    //获取其他级分类
    public List<Sort> selectOtherLevelDisplay(int parentId){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOtherLevelDisplay(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //根据id查询该分类信息
    public List<Sort>selectSortByIdDisplay(int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectSortByIdDisplay(id);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //查询该数据属于那个分类,传的是该数据的parentId
    public List<Sort>selectOfSortDisplay( int id){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectOfSortDisplay(id);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }

    //查询该分类下是否有物料
   public boolean selectIfProduct( int parentId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
       //调用mapper
       boolean b = mapper.selectIfProduct(parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
    public boolean selectSortIfExist(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        boolean b = mapper.selectSortIfExist(finSort);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //根据level，parent，code查询分类数据
    public FinSort selectSortCode(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        FinSort sort1 = mapper.selectSortCode(finSort);
        //释放资源
        sqlSession.close();
        //返回值
        return sort1;
    }

    //添加分类
    public void addSortExcel(FinSort finSort){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        mapper.addSortExcel(finSort);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }


    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    public List<FinSort> selectByCode (int level, String code, int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinSortMapper mapper = sqlSession.getMapper(FinSortMapper.class);
        //调用mapper
        List<FinSort> sorts = mapper.selectByCode(level, code,parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }
}
