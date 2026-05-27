package com.hongbang.service;

import com.hongbang.pojo.ExamineAllType;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ExamineAllTypeService {


    //根据出入库类型查询审核步骤
    List<ExamineAllType> selectStep(int typeId);


    //添加步骤
    void add(String s);

    //删除
    void delete(int id);


    //修改
    void update(String s);

    //查询审核步骤中是否已经存在该部门了
    boolean selectDepartExist(ExamineAllType examineAllType);

    //查询审核步骤中是否已经存在该部门了(修改时使用)
    boolean selectDepartExistUpdate(ExamineAllType examineAllType);


    //查看该类型的审核流程有几步
    int selectExistCount(int typeId);

    //删除步骤后改类型大于这个步骤的-1
    void updateStep( int step, int typeId);

    //插入步骤，大于该步骤的都+1
    void updateStepAdd( int step, int typeId);

    //根据id查询
    ExamineAllType selectById(int id);

    //插入
    void insert(String s);



}
