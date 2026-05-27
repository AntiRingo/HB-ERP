package com.hongbang.service.impl;

import com.hongbang.mapper.MaterialCategoryAssignMapper;
import com.hongbang.pojo.Sort;
import com.hongbang.service.MaterialCategoryAssignService;
import com.hongbang.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialCategoryAssignServiceImpl implements MaterialCategoryAssignService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    // 1. 查询分类下已分配的用户ID
    public List<Long> listAssignedUserIdsByCategoryId( String categoryId){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialCategoryAssignMapper mapper = sqlSession.getMapper(MaterialCategoryAssignMapper.class);
        //调用mapper
        List<Long> longs = mapper.listAssignedUserIdsByCategoryId(categoryId);
        //释放资源
        sqlSession.close();
        //返回值
        return longs;

    }

    // 2. 删除并添加该分类的所有分配
    public void deleteByCategoryId(String categoryId,List<Long> userIds){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialCategoryAssignMapper mapper = sqlSession.getMapper(MaterialCategoryAssignMapper.class);
        //调用mapper
        mapper.deleteByCategoryId(categoryId);

        if (userIds!=null){
            mapper.batchInsert(categoryId,userIds);
        }

        //提交事务
        sqlSession.commit();
        //释放资源
        sqlSession.close();
    }



    // 4. 查询所有已分配的分类ID
    public List<String> listAssignedCategoryIds(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialCategoryAssignMapper mapper = sqlSession.getMapper(MaterialCategoryAssignMapper.class);
        //调用mapper
        List<String> strings = mapper.listAssignedCategoryIds();
        //释放资源
        sqlSession.close();
        //返回值
        return strings;
    }


    public Map<String, List<Long>> getAllAssignMap() {
        try (SqlSession session = factory.openSession()) {
            MaterialCategoryAssignMapper mapper = session.getMapper(MaterialCategoryAssignMapper.class);
            List<Map<String, Object>> list = mapper.listAllAssigns();

            // 转为 Map<categoryId, List<userId>>
            Map<String, List<Long>> map = new HashMap<>();
            for (Map<String, Object> item : list) {
                String categoryId = (String) item.get("category_id");
                Long userId = (Long) item.get("user_id");

                map.computeIfAbsent(categoryId, k -> new ArrayList<>()).add(userId);
            }
            return map;
        }
    }

    //查询所有存在物料信息的分类
    public List<Sort> selectAllSort(){
        //获取session
        SqlSession sqlSession = factory.openSession();
        //获取mapper
        MaterialCategoryAssignMapper mapper = sqlSession.getMapper(MaterialCategoryAssignMapper.class);
        //调用mapper
        List<Sort> sorts = mapper.selectAllSort();
        //释放资源
        sqlSession.close();
        //返回值
        return sorts;
    }
}
