package com.hongbang.service;

import com.hongbang.pojo.OutboundType;

import java.util.List;

public interface OutBoundTypeService {

    //查询出库类型
    List<OutboundType> selectOutBound();

    //查询入库类型
    List<OutboundType> selectInBound();

    //查询所有类型
    List<OutboundType> selectAllType();



    //根据id查询
    List<OutboundType>selectById(int id);

    //查询采购类型
    List<OutboundType> selectCgBound();

    //查询质检类型
    List<OutboundType> selectZjBound();

}
