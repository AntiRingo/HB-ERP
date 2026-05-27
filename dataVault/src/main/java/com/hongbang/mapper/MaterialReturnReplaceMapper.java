package com.hongbang.mapper;

import com.hongbang.pojo.MaterialReturnReplace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MaterialReturnReplaceMapper {

    //查询需要自己处理的让步接收信息
    @MapKey("id")
    List<Map<String,Object>>selectRangBuByUser(@Param("user") int user, @Param("page") int page, @Param("pageSize") int pageSize);
    int selectRangBuByUserCount(@Param("user") int user);

    //插入让步接收修改信息
    @Insert("insert into material_return_replace values (#{id},#{receiveId},#{newProduct},#{newVault},#{remark})")
    void add(MaterialReturnReplace materialReturnReplace);

    /**
     * 根据receiveId查询物料更换记录
     * @param receiveId 原退货/让步单id
     * @return 更换记录实体
     */
    MaterialReturnReplace getByReceiveId(@Param("receiveId") Integer receiveId);
}
