package com.hongbang.service;

import com.hongbang.pojo.FinSort;
import com.hongbang.pojo.Sort;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface FinSortService {
    //获取其他级分类
    List<FinSort> selectOtherLevel(int parentId);

    //查询该数据属于那个分类,传的是该数据的parentId
    List<FinSort>selectOfSort(int id);

    //查询该数据的上一级分类中都有什么（返回上一级）
    List<FinSort>selectLastLevel(int id);

    //获取一级分类
    List<FinSort>selectOneLevel();

    //根据parentId获取level
    int selectLevel(int parentId);

    //添加分类的时候验证分类类名是否重复
    boolean selectNameAdd(String name);

    //添加的时候查询编码是否重复
    boolean ifCodeExistAdd(String code,int level,int parentId);

    //添加分类
    void addSort(FinSort finSort);

    //删除分类
    void deleteById(int id);


    //根据id查询该分类的信息
    List<FinSort>selectSortById(int id);

    //向下查询所有子集的id
    List<Map<String,Object>> selectAllDown(int id);

    //更新分类信息的时候验证分类名是否重复
    boolean selectNameUpdate( String finSortName, int id);

    //修改的时候查询编码是否存在
    boolean ifCodeExist(String finSortCode,int finSortLevel,int id,int parentId);

    //修改分类
    void updateSort(FinSort finSort);

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述
    void updateSortExceptCode(FinSort finSort);

    //查询该level的分级下所有的分类信息
    List<FinSort>selectByLevel(int finSortLevel);

    //循环更新编码
    void updateCodes(List<FinSort>finSorts);

    //检查是否有id为0的数据
    boolean select0();

    //将-1改为0
    void update0();

    //查询id为0的分类，也就是公共属性使用的单独分类
    List<FinSort>selectId0();

    //查询该分类中是否有数据
    boolean selectExist(int parentId);


    //查询分类列表里的全部数据
    List<FinSort> selectAll();

    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    List<Map> selectLast();

    //查询所有的一级分类
    List<FinSort>selectOne();

    //查询分类下的物料数量
    int selectNumber(int parentId);

//  显示页面要用  -----------------------------------------------------------------------------------------------------------------------
    //查询所有的一级分类（list<sort>是对的）

    List<Sort>selectOneDisplay();

    //获取其他级分类
    List<Sort> selectOtherLevelDisplay( int parentId);

    //根据id查询该分类信息
    List<Sort>selectSortByIdDisplay(int id);

    //查询该数据属于那个分类,传的是该数据的parentId
    List<Sort>selectOfSortDisplay( int id);

    //查询该分类下是否有物料
    boolean selectIfProduct( int parentId);



    // 一键导入要用  -----------------------------------------------------------------------------------------------------------------------
    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
    boolean selectSortIfExist(FinSort finSort);

    //根据编码parentIdlevel查询数据
    FinSort selectSortCode(FinSort finSort);

    //添加分类
    void addSortExcel(FinSort finSort);


    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    List<FinSort> selectByCode (int level,String code,int parentId);


}
