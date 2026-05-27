package com.hongbang.service.impl;

import com.hongbang.mapper.FinAttributeMapper;
import com.hongbang.pojo.*;
import com.hongbang.service.FinAttributeService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;

public class FinAttributeServiceImpl implements FinAttributeService {
    //获取工厂
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
//分类属性名--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该分类下的属性
   public List<FinAttributeName> selectAttributeNameByParentId(int finSortId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
       //调用mapper
       List<FinAttributeName> finAttributeNames = mapper.selectAttributeNameByParentId(finSortId);
       //释放资源
       sqlSession.close();
       //返回值
       return finAttributeNames;
   }

    //查询公共属性
    public List<FinAttributeName> selectPublic(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeName> finAttributeNames = mapper.selectPublic();
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeNames;
    }

    //获取信息进行回显
    @Override
    public List<FinAttributeName> selectAttributeNameById( int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeName> finAttributeNames = mapper.selectAttributeNameById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeNames;
    }


    //查询该分类下的所有属性
    @Override
    public List<FinAttributeName>selectAttributeName(int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeName> finAttributeNames = mapper.selectAttributeName(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeNames;
    }


    //添加属性
    public void add(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.add(finAttributeName);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据id更新属性信息
    @Override
    public  void updateAttributeNameById(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.updateAttributeNameById(finAttributeName);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        int i = mapper.selectIfLengthExist(id);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }

    //根据id查询要修改的属性的信息
    @Override
    public List<FinAttributeName>selectById(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeName> finAttributeNames = mapper.selectById(id);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeNames;
    }



    //判断该属性是否是范围类型的
    public   boolean selectIfRange(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectIfRange(id);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //根据ID修改length
    public void updateLength(int finAttLength,int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);

        //调用mapper
        mapper.updateLength(finAttLength, id);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.deleteById(id);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询更新自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public boolean selectUpdateNameExist(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectUpdateNameExist(finAttributeName);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }


    //查询添加自定义公共属性时的属性名是否重复，与不是公共属性的以及自定义公共属性的
    public boolean selectNameExist(String finAttName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectNameExist(finAttName);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //添加公共属性
    public void addPublic(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.addPublic(finAttributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询时唯一标志的内容是否重复（添加时使用）
    public boolean selectUniqueContent(int parentId, String content){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectUniqueContent(parentId, content);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询时唯一标志的内容是否重复（修改时使用）
   public boolean selectUniqueContentUpdate( int parentId, String content, int productId){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
       //调用mapper
       boolean b = mapper.selectUniqueContentUpdate(parentId, content, productId);
       //释放资源
       sqlSession.close();
       //返回值
       return b;
   }



//分类属性内容--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //查询该属性中是否有属性值被用到物料上
    public boolean selectIfUseByParentId(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectIfUseByParentId(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //查询属性值以及编码是否被物料用到
    public boolean selectIfUse(int parentId,String content){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectIfUse(parentId, content);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }
    //查询该属性填写的所有内容是否为空
    public List<FinAttributeContent>selectContentNull(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeContent> finAttributeContents = mapper.selectContentNull(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeContents;
    }

    //增加完属性后，循环向content表中添加数据
    public void addContentAfterName(List<FinAttributeContent>finAttributeContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);

        //调用mapper
        mapper.addContentAfterName(finAttributeContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();

    }

    //查询当前物料属性中所有被用到的信息
    public List<FinAttributeContent>selectACByParentId(int finAttNameId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeContent> finAttributeContents = mapper.selectACByParentId(finAttNameId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeContents;
    }

    //跟随者物料的添加进而添加该物料后添加的属性信息
    @Override
    public void addAttributeContent (List<FinAttributeContent>list){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.addAttributeContent(list);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //更新物料前查询信息用来构成输入框
    @Override
    public List<Map<String,Object>>selectBeforeContentUpdate(int finAttNameId, int finProductId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectBeforeContentUpdate(finAttNameId,finProductId);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //更新完基本属性后，更新后添加的属性
    @Override
    public void updateAttributeContent (List<FinAttributeContent>list){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.updateAttributeContent(list);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询该属性书否存在（更新时使用）
    public boolean selectAttributeContentIfExist(FinAttributeContent finAttributeContent){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectAttributeContentIfExist(finAttributeContent);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        int i = mapper.selectAttributeContentDistinct(parentId);
        //释放资源
        sqlSession.close();
        //返回值
        return i;
    }




    //    显示页面使用(List<AttributeName>是对的)-----------------------------------------------------------------------------------------------------------------------
//查询当前分类的上级属性和公共属性
    public List<AttributeName> selectLastAndPublic(List<Sort>sorts){
       //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectLastAndPublic(sorts);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //去重查询该属性的所有已使用的属性
    public List <Map<String,Object>> selectAllUsed(int parentId,int sortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
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
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List <Map<String,Object>> attributeContents = mapper.selectAllUsedByList(parentId,maps);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }


    //一个一个条件的筛选
   public List<Integer> selectOneByOneDisplay(List<AttributeContent>attributeContents){
       //获取session
       SqlSession sqlSession = factory.openSession();
       //获取mapper
       FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
       //调用mapper
       List<Integer> integers = mapper.selectOneByOneDisplay(attributeContents);
       //释放资源
       sqlSession.close();
       //返回值
       return integers;
   }

    //根据产品id查询该产品的所有后添加的属性
    public List<AttributeContent> selectByProductIdDisplay(int productId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectByProductIdDisplay(productId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }

    //查询该分类中的所有后添加的属性
    public List<AttributeName>selectAttributeNameDisplay( int finSortId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<AttributeName> attributeNames = mapper.selectAttributeNameDisplay(finSortId);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeNames;
    }

    //根据数据查询所有的后添加的属性
    public List<AttributeContent> selectByListProductDisplay(List<Product>products){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<AttributeContent> attributeContents = mapper.selectByListProductDisplay(products);
        //释放资源
        sqlSession.close();
        //返回值
        return attributeContents;
    }

    //查询已被弃用的物料信息后添加的属性名信息以及属性信息
    public List<Map<String,Object>> selectAbandonedFinAttribute(int id){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectAbandonedFinAttribute(id);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }

    //添加完公共属性后，如果有物料信息，给每个物料信息添加空的值
    public void afterAddPublicIfFinProductExist( int parentId,  List<FinProduct> finProducts){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.afterAddPublicIfFinProductExist(parentId,finProducts);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据属性名和分类id查询属性id
    public List<FinAttributeName> selectNameIdExcel(String name, int parentId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeName> attributeNames = mapper.selectNameIdExcel(name,parentId);
        //释放资源
        sqlSession.close();
        return attributeNames;
    }
    //只更新单位
    public void updateUnit(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.updateUnit(finAttributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //查询属性名是否存在
    public boolean  selectAttNameIfExistExcel(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        boolean b = mapper.selectAttNameIfExistExcel(finAttributeName);
        //释放资源
        sqlSession.close();
        //返回值
        return b;
    }

    //添加属性名信息后，获取刚添加的id
    public void addAttNameExcelAndId(FinAttributeName finAttributeName){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.addAttNameExcelAndId(finAttributeName);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }


    //添加属性名信息后，如果有物料，添加空的属性
    public void addAttributeContentExcel (List<FinAttributeContent>finAttributeContents){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        mapper.addAttributeContentExcel(finAttributeContents);
        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }

    //根据content 和 sortID查询productId；第二段
    public List<Integer> selectPidByContentPid( int parentId,String content,List<Integer>productsId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<Integer> list = mapper.selectPidByContentPid(parentId, content, productsId);
        //释放资源
        sqlSession.close();
        //返回值
        return list;
    }

    //在已有的物料id中查询最大的并且开启了映射中流水码功能的流水码
    public List<FinAttributeContent> selectContentByLsAndPid(int attNameId,List<Integer> productsId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<FinAttributeContent> finAttributeContents = mapper.selectContentByLsAndPid(attNameId, productsId);
        //释放资源
        sqlSession.close();
        //返回值
        return finAttributeContents;
    }
    //查询属性值一级编码是否在物料信息中被使用（整个分类查询表）
    public List<Map<String,Object>> selectIfUseBySort(List<FinAttributeValue> finAttributeValues){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        FinAttributeMapper mapper = sqlSession.getMapper(FinAttributeMapper.class);
        //调用mapper
        List<Map<String, Object>> maps = mapper.selectIfUseBySort(finAttributeValues);
        //释放资源
        sqlSession.close();
        //返回值
        return maps;
    }
}
