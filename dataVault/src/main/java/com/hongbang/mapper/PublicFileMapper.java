package com.hongbang.mapper;

import com.hongbang.pojo.PublicFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PublicFileMapper {

    //添加文件信息
    void addPubicFile(@Param("publicFiles")List<PublicFile> publicFiles);
    //查询文件信息
    @Select("select * from public_file where p_id = #{pId}")
    List<PublicFile> selectFile(@Param("pId") int Pid);
}
