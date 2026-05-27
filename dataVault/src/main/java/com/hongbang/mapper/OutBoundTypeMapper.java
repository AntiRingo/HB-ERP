package com.hongbang.mapper;

import com.hongbang.pojo.OutboundType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OutBoundTypeMapper {
    //查询出库类型
    @Select("select * from outbound_type where sort = 1 ")
    List<OutboundType> selectOutBound();

    //查询入库类型
    @Select("select * from outbound_type where sort = 0 and id not in (8,13)")
    List<OutboundType> selectInBound();

    //查询所有类型
    @Select("select * from outbound_type ")
    List<OutboundType> selectAllType();

    //查询采购类型
    @Select("select * from outbound_type where sort = 2")
    List<OutboundType> selectCgBound();


    //根据id查询
    @Select("select * from outbound_type  where id = #{id}")
    List<OutboundType>selectById(@Param("id") int id);

    //查询质检类型
    @Select("select * from outbound_type where sort = 4 ")
    List<OutboundType> selectZjBound();

}
