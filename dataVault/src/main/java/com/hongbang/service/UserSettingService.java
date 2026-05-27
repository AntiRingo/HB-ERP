package com.hongbang.service;

import com.hongbang.pojo.UserSetting;

public interface UserSettingService {

    //添加用户配置信息
    void addSetting(UserSetting userSetting);

    //修改用户配置信息
    void updateSetting(UserSetting userSetting);

    //读取用户配置
    UserSetting selectSetting(String user, String sort, String infoHead);

    //查询配置是否存在
    boolean selectIfExist(String user, String sort, String infoHead);
}
