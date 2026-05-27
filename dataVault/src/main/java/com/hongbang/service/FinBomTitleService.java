package com.hongbang.service;

import com.hongbang.pojo.FinBomTitle;
import com.hongbang.pojo.PageBean;

import java.util.List;
import java.util.Map;

public interface FinBomTitleService  {

    //根据productId查询BOM表标题信息
    List<FinBomTitle> selectBomTitle(int productId);

    //插入BOM表标题信息
    void addBomTitle(FinBomTitle finBomTitle ,int id);

    //查询物料号标题和BOM表信息
    List<Map<String,Object>> selectBomTitleAndProduct(int finProductId);

    //删除BOM表标题,一级BOM表
    void delete(int id);


    //修改BOM表列表信息
    void update(FinBomTitle finBomTitle);

    //获取数据进行回显
    FinBomTitle selectById(int id);


    //搜索功能
    PageBean<FinBomTitle>search(int begin, int size, String str);

    //查询所有的BOM表标题
    PageBean<FinBomTitle>selectAllBomTitle(int begin,int size);

    //根据物料号或者物料名称查询
   PageBean<FinBomTitle>selectAllByProductName(int begin, int size,  String str);

    //查询BOM表是否有作者(true就是有作者，false就是没有作者)
    boolean selectIfAuthor(int id);

    //更新BOM表的作者
    void updateAuthor(int author, int id);

    //查看是否是该BOM表的作者
    boolean selectIsAuthor(int author, int id);

}
