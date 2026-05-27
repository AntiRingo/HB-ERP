package com.hongbang.mapper;

import com.hongbang.pojo.Code;
import com.hongbang.pojo.FinCode;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface FinCodeMapper {
    //查询最大分级
    @Select("select max(level) from fin_code")
    int maxLevel();

    //查询该分类等级是否有规则存在
    @Select("select count(*) from fin_code where level = #{level}")
    boolean ifCode(@Param("level") int level);


    //获取规则并回显
    @Select("select * from fin_code where level=#{level}")
    List<FinCode> selectCode(@Param("level") int level);

    //添加规则
    @Insert("insert into fin_code values(#{id},#{length},#{description},#{remove},#{level})")
    void addCode(FinCode finCode);

    //查询这条规则是不是在使用
    @Select("select count(*) from fin_sort where fin_sort_level = #{finSortLevel}")
    boolean selectIfLevel(@Param("finSortLevel") int finSortLevel);

    //删除规则
    @Delete("delete from fin_code where id = #{id}")
    void delete(@Param("id") int id);

    //查询所有的编码规则
    @Select("select * from fin_code where level != 0 order by level")
    List<FinCode>selectAll();

    //查询一共有几条规则
    @Select("select count(*) from fin_code ")
    int selectCount();

    //更新规则
    @Update("update fin_code set  length=#{length},description=#{description},remove=#{remove} where level = #{level}")
    void updateCode(FinCode finCode);
}
