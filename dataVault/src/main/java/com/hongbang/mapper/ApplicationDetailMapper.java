package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 功能：操作 application_detail 附表
 * 作用：存储价格、供应商、日期等信息
 */
public interface ApplicationDetailMapper {
    ApplicationDetail selectByContentId(Integer applicationContentId); // 根据主表ID查询
    int insert(ApplicationDetail detail);                              // 新增
    int update(ApplicationDetail detail);                              // 修改

    // 修改明细表数据
    int updateDetail(Map<String,Object> map);

    // 复制新增明细数据
    int insertCopyDetail(@Param("oldId")Integer oldId, @Param("newId")Integer newId);

    // 查询明细是否存在
    int checkExistByContentId(Integer contentId);

    // 不存在时新增明细
    void insertDetail(Map<String, Object> map);
}