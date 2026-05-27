package com.hongbang.mapper;

import com.hongbang.pojo.ApplicationForm;
import com.hongbang.pojo.ExamineLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ExamineLogMapper {
    //新建审核日志
    @Insert("insert into examine_log values (#{id},#{date},#{appFormId},#{userId},#{examineStatus})")
    void addLog(ExamineLog examineLog);

    //查询日志记录
    @MapKey("")
//    level用来判断是部长还是总经理
    List<Map<String,Object>> selectExamineLog(@Param("maps") Map<String,Object> maps, @Param("begin") int begin, @Param("size") int size,@Param("level") int level,@Param("department") int department);
    int selectExamineLogCount(@Param("maps") Map<String,Object> maps,@Param("level") int level,@Param("department") int department );

    //查询审核人
    @MapKey("")
    List<Map<String,Object>> selectUser(@Param("maps") Map<String,Object> maps,@Param("level") int level,@Param("department") int department);


    //根据账号等级查询申请单信息
    List<ApplicationForm> selectAppByLevel(@Param("maps") Map<String,Object> maps,@Param("begin") int begin,@Param("size") int size,@Param("department") int department,@Param("level") int level );

    //根据账号等级查询申请单信息
    int selectAppByLevelCount(@Param("maps") Map<String,Object> maps,@Param("department") int department,@Param("level") int level );

    //根据申请单id查询审核记录
    @Select("select a.*,departmentName from(select examine_log.*,user.`name`,user.level,department from examine_log LEFT JOIN user on examine_log.user_id = `user`.id where app_form_id = #{appFormId}) as a  \n" +
            "LEFT JOIN department on a.department = department.id")
    List<Map<String,Object>>selectLogByAppId(@Param("appFormId") int appFormId);

    //根据申请单id查询否决的有多少个
    @Select("select count(*) from examine_log where app_form_id = #{appFormId} and examine_status = '否决'")
    boolean selectExamineStatusCount(@Param("appFormId") int appFormId);

    //根据等级查询申请人
    @MapKey("")
    List<Map<String,Object>> selectAppUser(@Param("maps") Map<String,Object> maps,@Param("level") int level,@Param("department") int department);

    //根据等级查询部门
    @MapKey("")
    List<Map<String,Object>> selectAppDepartment(@Param("maps") Map<String,Object> maps,@Param("level") int level,@Param("department") int department);


}
