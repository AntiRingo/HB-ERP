package com.hongbang.mapper;

import com.hongbang.pojo.PublicNotice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PublicNoticeMapper {
    //添加
    void addPublicNotice(PublicNotice publicNotice);

    //查询
    @Select("select * from public_notice where id = #{id}")
    PublicNotice selectById(@Param("id") int id);

    //查询所有
    @Select("select * from public_notice order by date desc ")
    List<PublicNotice> selectAll();
    @Select("select count(*) from public_notice")
    int selectAllCount();
}
