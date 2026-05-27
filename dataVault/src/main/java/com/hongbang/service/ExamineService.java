package com.hongbang.service;

import com.hongbang.pojo.Examine;
import com.hongbang.pojo.ExamineLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ExamineService {
    //添加信息申请单时，添加该申请单的审核信息
    void addExamine(Examine examine);

    //审核功能（部长审核）
    void updateMinister(int minister, int appFormId, ExamineLog examineLog);


    //根据申请单id查询审核内容
    List<Examine> selectByAppId(int appFormId);
}
