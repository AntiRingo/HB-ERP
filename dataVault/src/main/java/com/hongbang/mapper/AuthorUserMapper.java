package com.hongbang.mapper;

import com.hongbang.pojo.AuthorUser;
import com.hongbang.pojo.Department;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface AuthorUserMapper {
    //查询自己管理的人员
    @Select("select user.*,author_user.can_edit from author_user left join user on user_id = user.id where author_id = #{authorId} and author_user.delete_sign = 0")
    List<Map<String,Object>>selectByAuthor(@Param("authorId") int authorId);

    //查询自己管理的人员分为几个部门
    @Select("select distinct department from author_user left join user on user_id = user.id where author_id = #{authorId}")
    List<Integer>selectDepartCount(@Param("authorId") int authorId);

    //添加新的未审核的管理人员审核信息
    void addAuthorUser(@Param("authorUsers") List<AuthorUser> authorUsers);

    //查询该作者已经管理的人员
    @Select("select * from author_user where author_id = #{authorId} and delete_sign = 0 ")
    List<AuthorUser> selectNowAuthor(@Param("authorId") int authorId);

    //更新作者的管理人员   1.先删除原来正在使用的（如果是拒绝，则不修改现在正在使用的）
    @Update("update author_user set delete_sign = 1 where author_id = #{authorId} and delete_sign = 0")
    void updateNowAuthorUser(@Param("authorId") int authorId);
    ////更新作者的管理人员  2.更新现在申请的
    @Update("update author_user set delete_sign = #{deleteSign} where author_id = #{authorId} and review_app_id = #{reviewAppId}")
    void updateNewAuthorUser(@Param("deleteSign") int deleteSign,@Param("authorId") int authorId,@Param("reviewAppId") int reviewAppId);

    //查询某个作者可以控制的人分为几个部门
    @Select("select * from department where id in (select distinct department from user where id in (select user_id from author_user where author_id = #{authorId} and delete_sign = 0) )  ")
    List<Department> selectAllDepart(@Param("authorId") int authorId);

    //根据申请单id查询申请单内容
    @Select("select * from author_user where review_app_id = #{reviewAppId}")
    List<AuthorUser> selectByReviewId(@Param("reviewAppId") int reviewAppId);

    //将现在正在使用的某个人给删除掉
    @Update("update author_user set delete_sign = 1 where  author_id = #{authorId} and user_id = #{userId}")
    void deleteSomeOne(@Param("authorId") int authorId,@Param("userId") int userId);

    //根据id查询
    @Select("select * from author_user where id = #{id}")
    AuthorUser selectById(@Param("id") int id);

    //查询某个人是否被管理
    @Select("select count(*) from author_user where author_id = #{authorId} and user_id = #{userId} and delete_sign = 0")
    boolean selectByUserId(@Param("authorId") int authorId,@Param("userId") int userId);

    //查询否个人被管理的具体情况
    @Select("select * from author_user where author_id = #{authorId} and user_id = #{userId} and delete_sign = 0")
    AuthorUser selectUserByAuthor(@Param("authorId") int authorId,@Param("userId") int userId);

    //查询最新的，已经被删除的权限
    @Select("select * from author_user where author_id = #{authorId} and user_id = #{userId} and delete_sign = 1 order by id desc")
    List<AuthorUser> selectDeleteUserByAuthor(@Param("authorId") int authorId,@Param("userId") int userId);

    //根据申请的id将该申请改为正在使用的
    @Update("update author_user set delete_sign = 0 where id = #{id}")
    void updateById(@Param("id") int id);
}
