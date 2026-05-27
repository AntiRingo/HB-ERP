package com.hongbang.mapper;

import com.hongbang.pojo.Examine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ExamineMapper {
    //添加信息申请单时，添加该申请单的审核信息
    @Insert("insert into examine values (#{id},#{appFormId},1)")
    void addExamine(Examine examine);

    //审核功能（部长审核）
    @Update("update examine set minister = #{minister} where app_form_id = #{appFormId}")
    void updateMinister(@Param("minister") int minister,@Param("appFormId") int appFormId);



    //根据申请单id查询审核内容
    @Select("select * from examine where app_form_id = #{appFormId}")
    List<Examine> selectByAppId(@Param("appFormId") int appFormId);

}
