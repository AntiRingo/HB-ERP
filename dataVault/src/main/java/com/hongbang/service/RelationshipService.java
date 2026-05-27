package com.hongbang.service;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Relationship;

import java.util.List;

public interface RelationshipService {

    //添加
    void addRelationship(Relationship relationship);

    //根据新的查询旧的
    List<ApplicationForm> selectOldByNew(int newAppFormId);

    //根据新的id去查询旧的id
    Integer selectOld(int newAppFormId);


    //循环添加多条
    void addRelationshipList(List<Relationship> relationships);



}
