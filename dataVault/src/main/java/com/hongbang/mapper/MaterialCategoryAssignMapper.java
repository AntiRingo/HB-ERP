package com.hongbang.mapper;

import com.hongbang.pojo.Sort;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MaterialCategoryAssignMapper {

    // 1. 查询分类下已分配的用户ID
    List<Long> listAssignedUserIdsByCategoryId(@Param("categoryId") String categoryId);

    // 2. 删除该分类的所有分配
    void deleteByCategoryId(@Param("categoryId") String categoryId);

    // 3. 批量插入用户分配
    void batchInsert(@Param("categoryId") String categoryId,
                     @Param("userIds") List<Long> userIds);

    // 4. 查询所有已分配的分类ID
    List<String> listAssignedCategoryIds();

    // 查询所有分配记录
    @MapKey("id")
    List<Map<String, Object>> listAllAssigns();

    //查询所有存在物料信息的分类
    @Select("select * from sort where id in (select distinct parentId from product where deleteSign!=1) ")
    List<Sort> selectAllSort();
}
