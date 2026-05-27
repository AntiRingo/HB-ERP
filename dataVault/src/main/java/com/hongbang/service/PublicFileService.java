package com.hongbang.service;

import com.hongbang.pojo.PublicFile;

import java.util.List;

public interface PublicFileService {

    //查询文件信息
    List<PublicFile> selectFile(int Pid);
}
