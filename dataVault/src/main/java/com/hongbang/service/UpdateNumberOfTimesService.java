package com.hongbang.service;

import com.hongbang.pojo.UpdateNumberOfTimes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UpdateNumberOfTimesService {

    //新增
    void add(UpdateNumberOfTimes updateNumberOfTimes);


    //修改
    void update(UpdateNumberOfTimes updateNumberOfTimes);

    //获取当前的数据
    UpdateNumberOfTimes selectByPid(int productId);

    //判断是否有记录
    boolean selectIfExist(@Param("productId") int productId);

    //判断是否有记录
    boolean selectIfExist1(@Param("productId") int productId);
}
