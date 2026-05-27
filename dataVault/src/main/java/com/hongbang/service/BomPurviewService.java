package com.hongbang.service;

import com.hongbang.pojo.BomPurview;
import com.hongbang.pojo.BomPurviewReview;

import java.util.List;

public interface BomPurviewService {
    //查询某个BOM表可以查看的用户
    List<BomPurview> selectUserPurview(int id);

    //删除某个BOM表的所有权限
    void deletePurview(int finBomTitleId);

    //删除后循环添加权限
    void addPurview(BomPurviewReview bomPurviewReview, List<BomPurview> bomPurview, int finBomTitleId);

    //查询某个BOM表中某个用户是否有查看权限
    boolean selectHavePurview(int finBomTitleId,  int userPurview);

    //查询某个BOM表中某个用户是否有编辑权限
    boolean selectHaveCanEditPurview(int finBomTitleId, int userPurview);

    //添加作者修改的用户
    void addAuthorUserPurview(BomPurviewReview bomPurviewReview, List<BomPurview> bomPurview, int finBomTitleId);
}
