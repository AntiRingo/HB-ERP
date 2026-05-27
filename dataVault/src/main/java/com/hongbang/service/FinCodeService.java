package com.hongbang.service;

import com.hongbang.pojo.Code;
import com.hongbang.pojo.FinCode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FinCodeService {
    //查询最大分级
    int maxLevel();

    //查询该分类下是否有规则存在
    boolean ifCode(int finSortLevel);


    //获取规则并回显
    List<FinCode> selectCode(int finSortLevel);

    //添加code
    void addCode(FinCode finCode);

    //查询这条规则是不是在使用
    boolean selectIfLevel(int finSortLevel);

    //删除规则
    void delete(int id);

    //查询所有的编码规则
    List<FinCode>selectAll();

    //查询一共有几条规则
    int selectCount();

    //更新规则
    void updateCode(FinCode finCode);

}
