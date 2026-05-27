package com.hongbang.service;

import com.hongbang.pojo.PageBean;
import com.hongbang.pojo.PublicFile;
import com.hongbang.pojo.PublicNotice;

import java.util.List;

public interface PublicNoticeService {

    //添加
    void addPublicNotice(PublicNotice publicNotice, List<PublicFile> publicFiles);

    //查询
    PageBean<PublicNotice> selectAll();

}
