package com.hongbang.mapper;

import com.hongbang.pojo.BomPurview;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BomPurviewMapper {
    //查询某个BOM表可以查看的用户
    @Select("select * from bom_purview where fin_bom_title_id = #{id} and delete_sign = 0 ")
    List<BomPurview> selectUserPurview(@Param("id") int id);

    //删除某个BOM表的所有权限
    @Delete("update bom_purview set delete_sign = 1 where fin_bom_title_id = #{finBomTitleId} and delete_sign=0")
    void deletePurview(@Param("finBomTitleId") int finBomTitleId);

    //删除后循环添加权限
    void addPurview(@Param("bomPurview") List<BomPurview> bomPurview);

    //查询某个BOM表中某个用户是否有查看权限
    @Select("select count(*) from bom_purview where fin_bom_title_id = #{finBomTitleId} and user_purview = #{userPurview} and delete_sign = 0")
    boolean selectHavePurview(@Param("finBomTitleId") int finBomTitleId,@Param("userPurview") int userPurview);

    //查询某个BOM表中某个用户是否有编辑权限
    @Select("select count(*) from bom_purview where fin_bom_title_id = #{finBomTitleId} and user_purview = #{userPurview} and can_edit = 1 and delete_sign = 0")
    boolean selectHaveCanEditPurview(@Param("finBomTitleId") int finBomTitleId,@Param("userPurview") int userPurview);

    //查询正在使用的权限用户第
    @Select("select user_purview from bom_purview left join bom_purview_review on bom_purview.review_app_id = bom_purview_review.id where fin_bom_title_id = #{finBomTitleId} and delete_sign = 0 and status = 1 ")
    List<Integer> selectNowUsed(@Param("finBomTitleId") int finBomTitleId);

    //更新最新申请的申请单
    @Update("update bom_purview set delete_sign = #{deleteSign} where review_app_id = #{id} and delete_sign =2 ")
    void updateDeleteSign(@Param("id") int id,@Param("deleteSign") int deleteSign);

    //查询由某个作者管理的某个用户的所有权限
    @Select("select * from bom_purview where delete_sign = 0 and user_purview = #{userId} and fin_bom_title_id in (select id from fin_bom_title where author = #{authorId})")
    List<BomPurview> selectUserIdByAuthor(@Param("userId") int userId,@Param("authorId") int authorId);

    //删除作者管理所有BOM表中某个人的所有权限
    @Update("update bom_purview set delete_sign = 1 where user_purview = #{userId} and fin_bom_title_id in (select id from fin_bom_title where author = #{authorId}) ")
    void deleteAllUserByAuthor(@Param("userId") int userId,@Param("authorId") int authorId);

    //删除作者管理所有BOM表中某个人的编辑权限
    @Update("update bom_purview set can_edit = 0 where user_purview = #{userId} and delete_sign = 0 and fin_bom_title_id in (select id from fin_bom_title where author = #{authorId} ) ")
    void deleteAllUserCanEditByAuthor(@Param("userId") int userId,@Param("authorId") int authorId);
}
