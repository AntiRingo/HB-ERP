package com.hongbang.service;

import com.hongbang.pojo.AttributeName;
import com.hongbang.pojo.BomAttributeSetting;

import java.util.List;

public interface BomAttributeSettingService {

    //新增
    void  addAttributeSetting(BomAttributeSetting bomAttributeSetting);
    //删除
    void deleteAttributeSetting(int attributeId);

    //查询数据
    List<AttributeName> selectAll();
}
