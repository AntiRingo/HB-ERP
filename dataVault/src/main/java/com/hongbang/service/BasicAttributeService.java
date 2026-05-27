package com.hongbang.service;

import com.hongbang.pojo.BasicAttribute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BasicAttributeService {
    List<BasicAttribute>selectAll();


    //查询是否属性名与系统公共属性重复
    boolean selectExist(String name);
}
