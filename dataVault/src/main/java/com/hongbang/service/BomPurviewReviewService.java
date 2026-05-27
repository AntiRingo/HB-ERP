package com.hongbang.service;

import com.hongbang.pojo.ApprovalRequests;
import com.hongbang.pojo.BomPurviewReview;

import java.util.List;

public interface BomPurviewReviewService {

    //查询未审核的数据
    List<ApprovalRequests> selectNoReview();
    //通过审核
    void updateStatus(int status, int id,int bomTitleId);

    //根据id查询BOM表TitleId
    BomPurviewReview selectById( int id);


    //    最新的审核作者管理哪些用户的 -------------------------------------------------------------------------------------------------------------------------------------------

    //查询全部的申请单内容
    List<ApprovalRequests> selectAllAuthorAndUserReview();
    //通过审核
    void updateStatusAuthorReview(int status, int id);

}
