package com.hongbang.service.impl;

import com.hongbang.mapper.AttributeMapper;
import com.hongbang.pojo.*;
import com.hongbang.service.AttributeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class AttributeServiceImpl implements AttributeService {



    SqlSessionFactory factory= SqlSessionFactoryUtils.getSqlSessionFactory();

    //查询所有属性信息
    public List<AttributeName> selectAll(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAll();
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }




    //添加属性
    @Override
    public void add(AttributeName attributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.add(attributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询该分类下的所有属性
    @Override
    public List<AttributeName>selectAttributeName(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAttributeName(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //根据id查询要修改的属性的信息
    @Override
    public List<AttributeName>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //根据id更新要修改的信息
    @Override
    public void updateById(AttributeName attributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.updateById(attributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //只更新单位
    public void updateUnit(AttributeName attributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.updateUnit(attributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id删除属性信息
    @Override
    public void deleteById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //跟随者物料的添加进而添加该物料后添加的属性信息
    @Override
    public void addAttributeContent (List<AttributeContent>list){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.addAttributeContent(list);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //更新物料前查询信息用来构成输入框
    @Override
    public List<Map<String,Object>>selectBeforeContentUpdate(int parentId,int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectBeforeContentUpdate(parentId,productId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }
    //根据添加后的属性id获取该属性所在分类下的物料id；即content的productid
   public List<Map<String,Object>> selectProductIdByParentId(int id){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectProductIdByParentId(id);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //增加完属性后，循环向content表中添加数据
    public void addContentAfterName(List<AttributeContent>attributeContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);

        //调用mapper
        mapper.addContentAfterName(attributeContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //更新完基本属性后，更新后添加的属性
    @Override
    public void updateAttributeContent (List<AttributeContent>list){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.updateAttributeContent(list);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //新增分类属性
    @Override
    public void addSortAttribute(AttributeName attributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.addSortAttribute(attributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询该分类下的属性
    public List<AttributeName> selectAttributeNameByParentId(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAttributeNameByParentId(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //获取信息进行回显
    @Override
    public List<AttributeName> selectAttributeNameById( int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAttributeNameById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //根据id更新属性信息
    @Override
    public  void updateAttributeNameById(AttributeName attributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.updateAttributeNameById(attributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id删除属性信息
    public void deleteAttributeNameById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据ID修改length
    public void updateLength(int length,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);

        //调用mapper
        mapper.updateLength(length, id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //在属性名表中查询长度是否存在
    public int selectIfLengthExist(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        int i = mapper.selectIfLengthExist(id);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //查询属性值以及编码是否被物料用到
   public boolean selectIfUse(int parentId,String content){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectIfUse(parentId, content);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询该属性中是否有属性值被用到物料上
   public boolean selectIfUseByParentId(int parentId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectIfUseByParentId(parentId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }


    //判断该属性是否是范围类型的
     public   boolean selectIfRange(int id){
         //获取session
         SqlSession sqlSession = factory.openSession();
         //获取mapper
         AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
         //调用mapper
         boolean b = mapper.selectIfRange(id);
         //释放资源
         sqlSession.close();
         //返回值
         return b;
     }


    //查询当前物料属性中所有被用到的信息
    public List<AttributeContent>selectACByParentId(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectACByParentId(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }


    //根据产品id查询该产品的所有后添加的属性
   public List<AttributeContent> selectByProductId(int productId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<AttributeContent> attributeContents = mapper.selectByProductId(productId);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeContents;
   }

    //查询该属性书否存在（更新时使用）
   public boolean selectAttributeContentIfExist(AttributeContent attributeContent){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectAttributeContentIfExist(attributeContent);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //添加公共属性
   public void addPublic(AttributeName attributeName){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       mapper.addPublic(attributeName);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
    }

    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
   public void afterAddPublicIfProductExist( int parentId,  List<Product> products){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       mapper.afterAddPublicIfProductExist(parentId,products);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
   }

    //查询公共属性
   public List<AttributeName> selectPublic(){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<AttributeName> attributeNames = mapper.selectPublic();
       //释放资源
       sqlSession.close();
       //返回值
       return attributeNames;
   }


    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
   public boolean selectNameExist(String name){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectNameExist(name);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
   public boolean selectUpdateNameExist(AttributeName attributeName){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectUpdateNameExist(attributeName);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }

    //查询该属性填写的所有内容是否为空
    public List<AttributeContent>selectContentNull(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectContentNull(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }

    //查询当前分类的上级属性和公共属性
    public List<AttributeName> selectLastAndPublic(List<Sort>sorts){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectLastAndPublic(sorts);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }


    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
   public List<Map<String,Object>> selectAbandonedAttribute(int id){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectAbandonedAttribute(id);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }


    //查询时唯一标志的内容是否重复（添加时使用）
    public boolean selectUniqueContent(int parentId, String content){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectUniqueContent(parentId, content);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询时唯一标志的内容是否重复（修改时使用）
    public boolean selectUniqueContentUpdate(int parentId,String content, int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectUniqueContentUpdate(parentId, content, productId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询该属性内容有多少个，开启唯一值功能时判断使用
    public int selectAttributeContentCount(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        int i = mapper.selectAttributeContentCount(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //查询不重复的属性内容有多少个。开启唯一值功能时判断使用
    public int selectAttributeContentDistinct(int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        int i = mapper.selectAttributeContentDistinct(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }


    //流水码功能：查询该分类下最大的流水码
    public List<Map<String,Object>> selectMaxSerialCode(int sortId,int attNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectMaxSerialCode(sortId, attNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }


    //根据content 和 sortID查询productId；第一段
    public List<Integer> selectPidByContent(int parentId, String content){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectPidByContent(parentId, content);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //根据content 和 sortID查询productId；第二段
   public List<Integer> selectPidByContentPid( int parentId,String content,List<Integer>productsId){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Integer> list = mapper.selectPidByContentPid(parentId, content, productsId);
       //释放资源
       sqlSession.close();
       //返回值
       return list;
   }

    //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    public List<AttributeContent> selectContentByLsAndPid(int attNameId,List<Integer> productsId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectContentByLsAndPid(attNameId, productsId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }


   //下面是excel导入时使用的
   //查询属性名是否存在
  public boolean  selectAttNameIfExistExcel(AttributeName attributeName){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
      //调用mapper
      boolean b = mapper.selectAttNameIfExistExcel(attributeName);
      //释放资源
      sqlSession.close();
      //返回值
      return b;
  }

    //循环添加属性名信息
    public void addAttNameExcel(List<AttributeName>attributeNames){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.addAttNameExcel(attributeNames);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //添加属性名信息后，获取刚添加的id
   public void addAttNameExcelAndId(AttributeName attributeName){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       mapper.addAttNameExcelAndId(attributeName);
       //提交事务
       sqlSession.commit();
       //释放资源
       sqlSession.close();
    }

    //添加属性名信息后，如果有物料，添加空的属性
    public void addAttributeContentExcel (List<AttributeContent>attributeContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        mapper.addAttributeContentExcel(attributeContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //根据属性名和分类id查询属性id
   public List<AttributeName> selectNameIdExcel(String name, int parentId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<AttributeName> attributeNames = mapper.selectNameIdExcel(name,parentId);
       //释放资源
       sqlSession.close();
       return attributeNames;
   }

    //数量显示页面
    //根据属性名id和物料id查询这个属性值的内容
    public List<AttributeContent>selectContent( int parentId, int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectContent(parentId, productId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }


    //根据选中的内容查询产品id
   public  List<AttributeContent> selectProductId(String content){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<AttributeContent> attributeContents = mapper.selectProductId(content);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeContents;
   }


    //动态查询
   public List<Product> selectAnd(List<AttributeContent>attributeContents){

       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Product> products = mapper.selectAnd(attributeContents);
       //释放资源
       sqlSession.close();
       //返回值
       return products;
   }


    //一个一个条件的筛选
   public List<Integer> selectOneByOne(List<AttributeContent>attributeContents){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Integer> integers = mapper.selectOneByOne(attributeContents);
       //释放资源
       sqlSession.close();
       //返回值
       return integers;
   }


    //智能筛选
    public  List<AttributeContent> IntelligentFiltering(List<AttributeContent>attributeContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents1 = mapper.IntelligentFiltering(attributeContents);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents1;
    }




    //搜索功能，不带单位
  public List<Integer> searchContent(String str,int parentId){
        //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
      //调用mapper
      List<Integer> integers = mapper.searchContent(str, parentId);
      //释放资源
      sqlSession.close();
      //返回值
      return integers;
  }




    //搜索功能，带单位
    public List<Integer> searchContentUnit1(String str,int parentId,String unit){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchContentUnit1(str, parentId,unit);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }


    //在搜索结果中搜索，不带单位
    public List<Integer>selectInSelect(String str,List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectInSelect(str, products);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }


    //在搜索结果中搜索，带单位
  public  List<Integer>selectInSelectUnit(String str,List<Product>products, String unit){
      //获取session
      SqlSession sqlSession = factory.openSession();
      //获取mapper
      AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
      //调用mapper
      List<Integer> list = mapper.selectInSelectUnit(str, products, unit);
      //释放资源
      sqlSession.close();
      //返回值
      return list;
  }

    //去重查询该属性的所有已使用的属性
   public List <Map<String,Object>> selectAllUsed(int parentId,int sortId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List <Map<String,Object>> attributeContents = mapper.selectAllUsed(parentId,sortId);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeContents;
   }

    //去重查询该属性的所有已使用的属性(所有分类下的物料信息)
    public List <Map<String,Object>> selectAllUsedByList(int parentId,List<Map<String,Object>> maps){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List <Map<String,Object>> attributeContents = mapper.selectAllUsedByList(parentId,maps);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }

    //根据数据查询所有的后添加的属性
   public List<AttributeContent> selectByListProduct(List<Product>products){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<AttributeContent> attributeContents = mapper.selectByListProduct(products);
       //释放资源
       sqlSession.close();
       //返回值
       return attributeContents;
   }


    //    在显示页面使用=====================================================================================================================



    //搜索功能，不带单位
    public List<Integer> searchFinContent(String str,int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchFinContent(str, parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }




    //搜索功能，带单位
    public List<Integer> searchFinContentUnit1(String str,int parentId,String unit){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> integers = mapper.searchFinContentUnit1(str, parentId,unit);
        //释放资源
        sqlSession.close();
        //返回值
        return integers;
    }

    //根据数据查询所有的后添加的属性
    public List<AttributeContent> selectByListFinProduct(List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectByListFinProduct(products);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }


    //在搜索结果中搜索，不带单位
    public List<Integer>selectInFinSelect(String str,List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectInFinSelect(str, products);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }


    //在搜索结果中搜索，带单位
    public  List<Integer>selectInFinSelectUnit(String str,List<Product>products, String unit){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectInFinSelectUnit(str, products, unit);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
   public List<Map<String,Object>> selectIfUseBySort(List<AttributeValue> attributeValues){
        //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
       //调用mapper
       List<Map<String, Object>> maps = mapper.selectIfUseBySort(attributeValues);
       //释放资源
       sqlSession.close();
       //返回值
       return maps;
   }



    //    根据申请单内容查询属性信息
    public  List<Map<String,Object>> selectByAppProductIdAndVault(List<ApplicationContent>applicationContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectByAppProductIdAndVault(applicationContents);

        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询属性信息(品牌和封装)
    public List<Map<String,Object>>selectBrandFz(int id, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectBrandFz(id, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //查询所有属性信息
    @MapKey("id")
    public List<Map<String,Object>>selectAllAttribute(int id, int vault){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        AttributeMapper mapper = sqlSession.getMapper(AttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAllAttribute(id, vault);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

}
