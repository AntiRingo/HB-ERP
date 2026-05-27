package com.hongbang.service;

import com.hongbang.pojo.Sort;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SortService {

    //获取一级分类
    List<Sort>selectOneLevel();

    //获取二级分类
    List<Sort>selectOtherLevel(int parentId);

    //查询该数据的上一级分类中都有什么（返回上一级）
    List<Sort>selectLastLevel(int id);

    //获取全部分类
    List<Sort>selectAll();

    //添加分类
    void addSort(Sort sort);

    //查看该分类中是否有数据存在
    boolean selectExist(int parentId);


    //修改分类
    void updateSort(Sort sort);

    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    List<Map>selectLast();

    //查询该数据属于那个分类,传的是该数据的parentId
    List<Sort>selectOfSort(int id);

    //根据id查询该分类的信息
   List<Sort>selectSortById(int id);

   //根据parentId获取level
    int selectLevel(int parentId);

    //删除分类
    void deleteById(int id);

    //修改的时候查询编码是否存在
    boolean ifCodeExist(String code,int level,int id,int parentId);

    //一级分类修改的时候验证编码是否重复，查询零件库以及产品库两个库
    boolean ifCodeExistFromTwoTable(String code,int level , int id,int parentId);

    //添加的时候查询编码是否重复
    boolean ifCodeExistAdd(String code,int level,int parentId);

    //添加分类的时候验证分类类名是否重复
    boolean selectNameAdd(String name);

    //更新分类信息的时候验证分类名是否重复
    boolean selectNameUpdate( String name, int id);

    List<Sort>getData(int parentId);

    //向下查询所有子集的id
    List<Map<String,Object>> selectAllDown(int id);

    //查询该level级下的所有分类信息
    List<Sort>selectByLevel(int level);

    //循环更新编码信息
    void updateCodes(List<Sort>sorts);

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述
    void updateSortExceptCode(Sort sort);

    //检查是否有id为0的数据
    boolean select0();

    //将-1改为0
    void update0();

    //查询id为0的分类，也就是公共属性使用的单独分类
    List<Sort>selectId0();






//-----------------------------------------------------------------------------------------------------------------------------------------------
    //以下是excel导入用到的代码

    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
    boolean selectSortIfExist(Sort sort);

    //查询名称是否重复
    boolean selectSortNameIfExist(Sort sort);

    //查询编码是否重复
    boolean selectSortCodeIfExist(Sort sort);

    //一级分类添加的是否验证编码是否重复，查询零件库以及产品库两个库
    boolean ifCodeExistAddFromTwoTable( String code, int level, int parentId);

    //根据level，parent，code查询分类数据
    Sort selectSortCode(Sort sort);

    //添加分类
    void addSortExcel(Sort sort);

    //更新分类
    void updateSortExcel(Sort sort);//以上的添加和更新只适用于前三级

    //添加分类，循环添加，四级分类使用
    void addSortFourExcel(List<Sort>sortList);

    //根据名称查询分类id
    int  selectIdExcel( String name);



    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    List<Sort> selectByCode ( int level, String code,int parentId);

    //查询名称和编码是否相同
    boolean nameAndCodeIfExist( String name,String code);


    //--------------------------------------------------------------------------------------------------------------------------------------------------
    //下面是数量页面用到的

    //查询有物料的分类
    List<Sort>selectHaveProduct();

    //查询所有二级分类
    List<Sort> selectTwoLevel();

    //查询分类下的物料数量
    int selectNumber(int parentId);

    //查询所有的一级分类
    //查询所有的一级分类
     List<Sort>selectOne();


    //根据属性id查询该属性所在的分类下是否有物料信息
    boolean selectIfExistProductByNameId(int id);

    //查询所有的四级分类
    List<Sort> selectAllLevelFour();

    //根据产品信息查询分类
    List<Sort>selectByProduct(@Param("productId") int productId, @Param("vault") int vault);

}
