package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationContent;
import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.Relationship;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface RelationshipMapper {
    //添加
    @Insert("insert into relationship values (#{id},#{oldAppFormId},#{newAppFormId},0,0,#{number})")
    void addRelationship(Relationship relationship);

    //根据新的查询旧的
    @Select("select application_form.* from relationship left join application_form on relationship.old_app_form_id = application_form.id and new_app_form_id = #{newAppFormId} where length(application_form.id)>0")
    List<ApplicationForm>  selectOldByNew(@Param("newAppFormId") int newAppFormId);

    //根据新的id去查询旧的id
    @Select("select old_app_form_id from relationship where new_app_form_id = #{newAppFormId}")
    Integer selectOld(@Param("newAppFormId") int newAppFormId);

    //根据新的查询旧的（数组）
    @Select("select * from relationship where new_app_form_id = #{newAppFormId}")
    List<Relationship> selectAllOld(@Param("newAppFormId") int newAppFormId);

    //循环添加多条
    void addRelationshipList(@Param("relationships") List<Relationship> relationships);

    //查询根据最后一级申请单查询第一级的申请单
    List<Relationship>selectFirstLevelAppForm(@Param("lastAppFormId") int lastAppFormId);



    //根据最后一级查询第一级申请单
    List<Integer>selectFirst(@Param("lastAppFormId") int lastAppFormId);
    //查询根据最后一级（入库单）查询采购单


    //查询根据最后一级申请单查询第一级的申请单id
    List<ApplicationContent>selectFirstLevelAppContent(@Param("lastAppFormId") int lastAppFormId);


    //查询第一级是否已经全部完成（入库的时候需要改变原始申请单状态）
    @MapKey("id")
    List<Map<String,Object>>selectFirstIfCompleteByLast(@Param("lastAppFormId") int lastAppFormId);

    //根据质检单查询申购单
    @Select("select  distinct* from relationship where relationship.new_app_form_id in (select old_app_form_id from relationship where relationship.new_app_form_id = #{zjAppId})")
    List<Relationship>selectSg(@Param("zjAppId") int zjAppId);





    //第一步：根据入库单查询所有的采购的申请总量
    List<ApplicationContent> selectAllAppNumberByRk(@Param("rkId") int rkId);


}
