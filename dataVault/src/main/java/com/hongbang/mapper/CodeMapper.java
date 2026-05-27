package com.hongbang.mapper;

import com.hongbang.pojo.Code;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface CodeMapper {

    //查询该分类等级是否有规则存在
    @Select("select count(*) from code where level = #{level}")
    boolean ifCode(@Param("level") int level);

    //添加规则
    @Insert("insert into code values(#{id},#{length},#{description},#{remove},#{level})")
    void addCode(Code code);

    //获取规则并回显
    @Select("select * from code where level=#{level}")
    List<Code>selectCode(@Param("level") int level);

    //更新规则
    @Update("update code set  length=#{length},description=#{description},remove=#{remove} where level = #{level}")
    void updateCode(Code code);

    //查询所有的编码规则
    @Select("select * from code where level != 0 order by level")
    List<Code>selectAll();

    //删除规则
    @Delete("delete from code where id = #{id}")
    void delete(@Param("id") int id);

    //查询一共有几条规则
    @Select("select count(*) from code ")
    int selectCount();

    //查询这条规则是不是在使用
    @Select("select count(*) from sort where level = #{level}")
    boolean selectIfLevel(@Param("level") int level);

    //查询最大分级
    @Select("select max(level) from code")
    int maxLevel();




}
