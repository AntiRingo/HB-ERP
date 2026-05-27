package com.hongbang.mapper;

import com.hongbang.pojo.FinSort;
import com.hongbang.pojo.Sort;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinSortMapper {
    //获取其他级分类
    @Select("select * from fin_sort where parent_id=#{parentId} and id!=0 ")
    List<FinSort> selectOtherLevel(@Param("parentId") int parentId);

    //查询该数据属于那个分类,传的是该数据的parentId
    @Select("select * from fin_sort where id = #{id} ")
    List<FinSort>selectOfSort(@Param("id") int id);

    //查询该数据的上一级数据
    @Select(" select * from fin_sort where parent_id in (select parent_id from fin_sort where id=#{id}) and id!=0")
    List<FinSort> selectLastLevel(@Param("id") int id);

    //查询分类
    @Select("select * from fin_sort where parent_id = 0 and id !=0")
    List<FinSort>selectOneLevel();

    //根据parentId获取level
    @Select("select distinct fin_sort_level from fin_sort where id=#{parentId}")
    int selectLevel(@Param("parentId") int parentId);

    //添加分类的时候验证分类名是否重复
    @Select("select count(*) from fin_sort where fin_sort_name=#{finSortName}")
    boolean selectNameAdd(@Param("finSortName") String finSortName);

    //添加的时候验证编码是否重复
    @Select("select count(*) from fin_sort where fin_sort_code=#{finSortCode} and fin_sort_level=#{finSortLevel} and parent_id = #{parentId}")
    boolean ifCodeExistAdd(@Param("finSortCode") String finSortCode,@Param("finSortLevel") int finSortLevel,@Param("parentId") int parentId);

    //增加分类
    @Insert("insert into fin_sort values(#{id},#{parentId},#{finSortName},#{finSortLevel},#{finSortDescription},#{finSortCode},1)")
    void addSort(FinSort finSort);

    //删除分类
    @Delete("delete  from fin_sort where id = #{id}")
    void deleteById(@Param("id") int id);

    //根据id查询该分类信息
    @Select("select * from fin_sort where id =#{id}")
    List<FinSort>selectSortById(@Param("id") int id);


    //查询所有子集的id
    @Select("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(all_sub_ids, ',', rn), ',', -1) AS id_list\n" +
            "FROM (\n" +
            "SELECT GROUP_CONCAT(id) AS all_sub_ids,\n" +
            "ANY_VALUE(LENGTH(GROUP_CONCAT(id SEPARATOR ',')) - LENGTH(REPLACE(GROUP_CONCAT(id SEPARATOR ','), ',', '')) + 1) AS c,\n" +
            "@rownum := @rownum + 1 AS rn\n" +
            "FROM (\n" +
            "SELECT * FROM (\n" +
            "SELECT id,parent_id\n" +
            "FROM fin_sort\n" +
            "ORDER BY parent_id, id\n" +
            ") org_query,\n" +
            "(SELECT @id :=#{id}) initialisation\n" +
            "WHERE FIND_IN_SET(parent_id, @id) > 0\n" +
            "AND @id := CONCAT(@id, ',', id)\n" +
            ") sub_query,\n" +
            "(SELECT @rownum := 0) r\n" +
            "GROUP BY rn\n" +
            ") ids;")
    List<Map<String,Object>> selectAllDown(@Param("id") int id);


    //更新分类信息的时候验证是分类名是否重复
    @Select("select count(*) from fin_sort where fin_sort_name=#{finSortName} and id !=#{id}")
    boolean selectNameUpdate(@Param("finSortName") String finSortName,@Param("id") int id);


    //修改的时候查询编码是否重复
    @Select("select count(*) from fin_sort where fin_sort_code=#{finSortCode} and fin_sort_level=#{finSortLevel} and id!=#{id} and parent_id=#{parentId}")
    boolean ifCodeExist(@Param("finSortCode") String finSortCode,@Param("finSortLevel") int finSortLevel ,@Param("id") int id,@Param("parentId") int parentId);

    //修改分类信息
    @Update("update fin_sort set fin_sort_name=#{finSortName},fin_sort_description=#{finSortDescription},fin_sort_code=#{finSortCode} where id=#{id}")
    void updateSort(FinSort finSort);

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述
    @Update("update fin_sort set fin_sort_name = #{finSortName},fin_sort_description=#{finSortDescription} where id = #{id}")
    void updateSortExceptCode(FinSort finSort);

    //查询该level的分级下所有的分类信息
    @Select("select * from fin_sort where fin_sort_level = #{finSortLevel}")
    List<FinSort>selectByLevel(@Param("finSortLevel") int finSortLevel);

    //循环更新编码
    void updateCodes(@Param("finSorts")List<FinSort>finSorts);

    //检查是否有id为0的数据
    @Select("select count(*) from fin_sort where id = 0")
    boolean select0();

    //将-1改为0
    @Update("update fin_sort set id = 0 where id = -1")
    void update0();

    //查询id为0的分类，也就是公共属性使用的单独分类
    @Select("select * from fin_sort where id=0")
    List<FinSort>selectId0();


    //查询该分类中是否有数据
    @Select("select count(*) from fin_sort where parent_id = #{parentId}")
    boolean selectExist(@Param("parentId") int parentId);

    //查询分类列表里的全部数据
    @Select("select * from fin_sort where id !=0")
    List<FinSort> selectAll();

    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    @Select(" select id,vault from fin_sort where id  not in  (select id from fin_sort where id   in(select parent_id from fin_sort))")
    List<Map> selectLast();

    //查询所有的一级分类
    @Select("select  * from fin_sort where fin_sort_level = 1 and id !=0")
    List<FinSort>selectOne();
//显示页面要用(list<sort>是对的)---------------------------------------------------------------------------------------------------------------------------------------------
    //查询所有的一级分类
    @Select("select  id, parent_id, fin_sort_name as name, fin_sort_level as level, fin_sort_description as 'describe', fin_sort_code as code, vault from fin_sort where fin_sort_level = 1 and id !=0")
    List<Sort>selectOneDisplay();


    //查询分类下的物料数量
    @Select("select count(*) from fin_product where deleteSign = 0 and fin_sort_id = #{parentId}")
    int selectNumber(@Param("parentId") int parentId);

    //获取其他级分类
    @Select("select id, parent_id, fin_sort_name as name, fin_sort_level as level, fin_sort_description as 'describe', fin_sort_code as code, vault  from fin_sort where parent_id=#{parentId} and id!=0 ")
    List<Sort> selectOtherLevelDisplay(@Param("parentId") int parentId);

    //根据id查询该分类信息
    @Select("select id, parent_id, fin_sort_name as name, fin_sort_level as level, fin_sort_description as 'describe', fin_sort_code as code, vault  from fin_sort where id =#{id}")
    List<Sort>selectSortByIdDisplay(@Param("id") int id);

    //查询该数据属于那个分类,传的是该数据的parentId
    @Select("select id, parent_id, fin_sort_name as name, fin_sort_level as level, fin_sort_description as 'describe', fin_sort_code as code, vault from fin_sort where id = #{id} ")
    List<Sort>selectOfSortDisplay(@Param("id") int id);

    //查询该分类下是否有物料
    @Select("select count(*) from fin_product where fin_sort_id = #{parentId}")
    boolean selectIfProduct(@Param("parentId") int parentId);

    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
    @Select("select count(*) from fin_sort where fin_sort_name = #{finSortName}  and fin_sort_level = #{finSortLevel} and parent_id = #{parentId} and fin_sort_code = #{finSortCode}")
    boolean selectSortIfExist(FinSort finSort);

    //根据编码parentIdlevel查询数据
    @Select("select * from fin_sort where  fin_sort_level = #{finSortLevel} and parent_id = #{parentId} and fin_sort_code = #{finSortCode}")
    FinSort selectSortCode(FinSort finSort);


    //添加分类
    void addSortExcel(FinSort finSort);

    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    @Select("select  * from fin_sort where fin_sort_level=#{level} and fin_sort_code = #{code} and parent_id =#{parentId}")
    List<FinSort> selectByCode (@Param("level") int level,@Param("code") String code,@Param("parentId") int parentId);


}

