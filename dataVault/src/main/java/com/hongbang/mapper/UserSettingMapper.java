package com.hongbang.mapper;

import com.hongbang.pojo.UserSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserSettingMapper {
    //添加用户配置信息
    @Insert("insert into user_setting values (#{id},#{sort},#{user},#{infoHead},#{info})")
    void addSetting(UserSetting userSetting);

    //修改用户配置信息
    @Update("update user_setting set info = #{info} where user = #{user} and sort = #{sort} and info_head = #{infoHead}")
    void updateSetting(UserSetting userSetting);

    //读取用户配置
    @Select("select * from user_setting where user = #{user} and sort = #{sort} and info_head = #{infoHead}")
    UserSetting selectSetting(@Param("user") String user,@Param("sort") String sort,@Param("infoHead") String infoHead);

    //查询配置是否存在
    @Select("select count(*) from user_setting where user = #{user} and sort =#{sort} and info_head = #{infoHead}")
    boolean selectIfExist(@Param("user") String user,@Param("sort") String sort,@Param("infoHead") String infoHead);
}
