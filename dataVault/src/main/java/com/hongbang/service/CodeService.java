package com.hongbang.service;

import com.hongbang.pojo.Code;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CodeService {
//查询该分类下是否有规则存在
    boolean ifCode(int level);

    //添加code
    void addCode(Code code);

    //获取规则并回显
    List<Code>selectCode(int level);

    //更新规则
    void updateCode(Code code);

    //查询所有编码规则
    List<Code>selectAll();

    //删除分级编码规则
    void delete(int id);

    //查询共有几条编码规则
    int selectCount();

    //查询这条规则是不是在使用
    boolean selectIfLevel(int level);

    //查询最大分级数
    int maxLevel();

}
