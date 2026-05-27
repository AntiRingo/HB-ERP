package com.hongbang.mapper;

import com.hongbang.pojo.AuthorUserReview;
import com.hongbang.pojo.BomPurview;
import com.hongbang.pojo.BomPurviewReview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface BomPurviewReviewMapper {
    //新建bom表权限申请单
    void addBomReviewApp(BomPurviewReview bomPurviewReview);

    //查询未审核的申请单数据
    @Select("select * from bom_purview_review")
    List<BomPurviewReview> selectNoReview();

    //查询未审核的申请单内容
    @Select("select bom_purview.* from bom_purview_review left join bom_purview on bom_purview_review.id = bom_purview.review_app_id where bom_purview_review.id=#{id}" )
            List<BomPurview> selectNoBomPurview(@Param("id") int id);

    //通过审核
    @Update("update bom_purview_review set status = #{status} where id = #{id}")
    void updateStatus(@Param("status") int status,@Param("id") int id);

    //根据id查询BOM表TitleId
    @Select("select * from bom_purview_review where id = #{id}")
    BomPurviewReview selectById(@Param("id") int id);


//    最新的审核作者管理哪些用户的 -------------------------------------------------------------------------------------------------------------------------------------------
    //新建作者申请管理用户
    void addAuthorUserReviewApp(AuthorUserReview authorUserReview);

    //查询全部的申请单
    @Select("select a.*,departmentName as department from   (select author_user_review.id,times as timestamp,applicant,status,user.name,department as dept from author_user_review left join user on user.id =applicant) as a left join department on a.dept = department.id  order by id desc ")
    List<Map<String,Object>> selectAllAuthorAndUserReview();

    //查询所有的申请单的申请内容
    @Select(" select a.*,user.name from ( select author_user.user_id,type,can_edit,author_user_review.id,author_id from author_user_review left join author_user on author_user_review.id = author_user.review_app_id) as a left join user on user_id = user.id where user_id >0")
    List<Map<String,Object>> selectAllAuthor();

    //通过审核
    @Update("update author_user_review set status = #{status} where id = #{id}")
    void updateStatusAuthorReview(@Param("status") int status,@Param("id") int id);


    AuthorUserReview selectByIdAuthor(@Param("id") int id);

    //查询申请单是否已经被审核:true是未审核，false是已经审核
    @Select("select count(*) from author_user_review where id = #{id} and status = 0")
    boolean selectIfComplete(@Param("id") int id);



}
