package com.hongbang.service;

import com.hongbang.pojo.BasicAttribute;
import com.hongbang.pojo.FinBasicAttribute;

import java.util.List;

public interface FinBasicAttributeService {

    List<FinBasicAttribute>selectAll();


    //查询是否属性名与系统公共属性重复
    boolean selectExist(String name);
}
