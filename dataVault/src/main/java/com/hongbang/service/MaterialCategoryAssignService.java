package com.hongbang.service;

import com.hongbang.pojo.Sort;

import java.util.List;
import java.util.Map;

public interface MaterialCategoryAssignService {
    // 1. 查询分类下已分配的用户ID
    List<Long> listAssignedUserIdsByCategoryId(String categoryId);

    // 2. 删除该分类的所有分配
    void deleteByCategoryId(String categoryId,List<Long> userIds);

//    // 3. 批量插入用户分配
//    void batchInsert(String categoryId, List<Long> userIds);

    // 4. 查询所有已分配的分类ID
    List<String> listAssignedCategoryIds();

    // 查询所有分配记录
    Map<String, List<Long>> getAllAssignMap();


    //查询所有存在物料信息的分类
    List<Sort> selectAllSort();
}
