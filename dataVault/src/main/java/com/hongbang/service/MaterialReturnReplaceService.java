package com.hongbang.service;

import com.hongbang.pojo.MaterialReturnReplace;
import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface MaterialReturnReplaceService {


    //查询需要自己处理的让步接收信息
    @MapKey("id")
    PageBean<Map<String,Object>> selectRangBuByUser(@Param("user") int user, @Param("page") int page, @Param("pageSize") int pageSize);

    //插入让步接收修改信息
    void add(MaterialReturnReplace materialReturnReplace, User user);

    /**
     * 根据让步单id查询更换记录
     * @param receiveId 让步/退货单主键id
     * @return 更换记录
     */
    MaterialReturnReplace getByReceiveId(Integer receiveId);
}
