package com.hongbang.mapper;

import com.hongbang.pojo.TemporaryUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TemporaryUserMapper {
    //临时用户

    //添加临时用户的信息
    @Insert("insert into temporary_user values (#{id},#{createTime},#{overTime},#{userId},#{userName},#{passWord},#{department})")
    void addTemporaryUser(TemporaryUser temporaryUser);

    //删除临时用户信息(当现在的时间大于删除时间时就删除)
    @Delete("delete  from temporary_user where #{time} >= over_time")
    void deleteByTime(@Param("time") String time);

    //查询临时账户用户名是否存在
    @Select("select count(*) from temporary_user where user_name = #{userName}")
    boolean selectExistUserName(@Param("userName") String userName);

    //查询所有的临时账户细信息
    @Select("select a.id,user_name,userName as createUser,departmentName,create_time,over_time,a.department as departmentId from " +
            "(SELECT temporary_user.*,`user`.userName from temporary_user LEFT JOIN user on temporary_user.user_id = `user`.id) as a " +
            "LEFT JOIN department on a.department = department.id")
    List<Map<String,Object>> selectAllTemporaryUser();

    //查询该部门下所有的临时账户信息
    @Select("select a.id,user_name,userName as createUser,departmentName,create_time,over_time,a.department as departmentId from " +
            "(SELECT temporary_user.*,`user`.userName from temporary_user LEFT JOIN user on temporary_user.user_id = `user`.id) as a" +
            " LEFT JOIN department on a.department = department.id where department.id = #{departmentId}")
    List<Map<String,Object>> selectAllTemporaryUserByDepartment(@Param("departmentId") int departmentId);

    //根据id删除临时账户
    @Delete("delete from temporary_user where id =#{id}")
    void delete(@Param("id") int id);

    //更新到期时间
    @Update("update temporary_user set over_time = #{overTime} where id = #{id}")
    void updateOverTime(@Param("overTime") String overTime,@Param("id") int id);

    //验证临时账户是否正确，部门也要正确
    @Select("select count(*) from temporary_user where user_name = #{userName} and pass_word = #{passWord} and department = #{department}")
    boolean loginExist(TemporaryUser temporaryUser);

}
