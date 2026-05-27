package com.hongbang.service;

import com.hongbang.pojo.Model;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ModelService {
    //查询机型
    List<Model> selectModel();

    //查询机型是否存在
    boolean selectModelExist(String modelName);
}
