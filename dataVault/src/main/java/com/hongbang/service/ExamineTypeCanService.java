package com.hongbang.service;

import com.hongbang.pojo.ExamineTypeCan;

public interface ExamineTypeCanService {

    //更新修改数量的权限
    void addExamineCan(ExamineTypeCan examineTypeCan);


    //更新修改数量的权限
    void  updateExamineCan(ExamineTypeCan examineTypeCan);

    //先删除权限，再重新添加
    void updatePermissions(ExamineTypeCan examineTypeCan);


    //根据步骤ID查询数据
    ExamineTypeCan selectByExamineId (int examineId);
}
