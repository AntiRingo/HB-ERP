package com.hongbang.service;

import com.hongbang.pojo.AuthorUser;
import com.hongbang.pojo.AuthorUserReview;
import com.hongbang.pojo.Department;
import com.hongbang.pojo.UserPickerData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorUserService {

    //查询自己管理的人员
    List<UserPickerData> selectByAuthor(int authorId);

    //添加新的未审核的管理人员审核信息
    void addAuthorUser(AuthorUserReview authorUserReview, List<AuthorUser> authorUsers);

    //查询某个作者可以控制的人分为几个部门
    List<Department> selectAllDepart(int authorId);

    //查询该作者已经管理的人员
    List<AuthorUser> selectNowAuthor(@Param("authorId") int authorId);
}
