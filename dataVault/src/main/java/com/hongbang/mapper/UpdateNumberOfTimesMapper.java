package com.hongbang.mapper;

import com.hongbang.pojo.UpdateNumberOfTimes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UpdateNumberOfTimesMapper {

    //新增
    @Insert("insert into update_number_of_times values (#{id},#{productId},#{updateNumber})")
    void add(UpdateNumberOfTimes updateNumberOfTimes);

    //修改
    @Update("update update_number_of_times set update_number = #{updateNumber} where product_id=#{productId}")
    void update(UpdateNumberOfTimes updateNumberOfTimes);

    //获取当前的数据
    @Select("select * from update_number_of_times where product_id = #{productId}")
    UpdateNumberOfTimes selectByPid(@Param("productId") int productId);

    //判断是否有记录
    @Select("select count(*) from update_number_of_times where product_id = #{productId} and update_number >=0")
    boolean selectIfExist(@Param("productId") int productId);

    //判断是否有物料数量有更改
    @Select("select count(*) from update_number_of_times where product_id = #{productId} and update_number >0")
    boolean selectIfExist1(@Param("productId") int productId);


}
