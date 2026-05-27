package com.hongbang.mapper;

import com.hongbang.pojo.Sort;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SortMapper {

    //查询分类
    @Select("select * from Sort where parent_id = 0 and id !=0")
    List<Sort>selectOneLevel();

    //获取其他级分类
    @Select("select * from Sort where parent_id=#{parentId} and id!=0 ")
    List<Sort>selectOtherLevel(@Param("parentId") int parentId);

    //查询该数据的上一级数据
    @Select(" select * from sort where parent_id in (select parent_id from sort where id=#{id}) and id!=0")
    List<Sort> selectLastLevel(@Param("id") int id);

    //查询该数据属于那个分类,传的是该数据的parentId
    @Select("select * from sort where id = #{id} ")
    List<Sort>selectOfSort(@Param("id") int id);

    //查询分类列表里的全部数据
    @Select("select * from Sort where id !=0")
    List<Sort> selectAll();

    //增加分类
    @Insert("insert into Sort values(#{id},#{parentId},#{name},#{level},#{describe},#{code},0)")
    void addSort(Sort sort);


    //查询该分类中是否有数据
    @Select("select count(*) from sort where parent_id = #{parentId}")
    boolean selectExist(@Param("parentId") int parentId);

    //修改分类信息
    @Update("update sort set name=#{name},`describe`=#{describe},code=#{code} where id=#{id}")
    void updateSort(Sort sort);

    //查询最后一层的id（该分类下没有分类了，就该是数据了）
    @Select(" select id,vault from sort where id  not in  (select id from sort where id   in(select parent_id from sort))")
    List<Map> selectLast();

    //根据id查询该分类信息
    @Select("select * from sort where id =#{id}")
    List<Sort>selectSortById(@Param("id") int id);

    //根据parentId获取level
    @Select("select distinct level from sort where id=#{parentId}")
    int selectLevel(@Param("parentId") int parentId);

    //删除分类
    @Delete("delete  from sort where id = #{id}")
    void deleteById(@Param("id") int id);

    //修改的时候查询编码是否重复
    @Select("select count(*) from sort where code=#{code} and level=#{level} and id!=#{id} and parent_id=#{parentId}")
    boolean ifCodeExist(@Param("code") String code,@Param("level") int level ,@Param("id") int id,@Param("parentId") int parentId);

    //一级分类修改的时候验证编码是否重复，查询零件库以及产品库两个库
    @Select("select SUM(a) from (select count(*) a from sort where code=#{code} and level=#{level} and id!=#{id} and parent_id=#{parentId} " +
            " UNION all select count(*) a  from fin_sort where fin_sort_code=#{code} and fin_sort_level=#{level} and id!=#{id} and parent_id=#{parentId} ) as b")
    boolean ifCodeExistFromTwoTable(@Param("code") String code,@Param("level") int level ,@Param("id") int id,@Param("parentId") int parentId);

    //添加的时候验证编码是否重复
    @Select("select count(*) from sort where code=#{code} and level=#{level} and parent_id = #{parentId}")
    boolean ifCodeExistAdd(@Param("code") String code,@Param("level") int level,@Param("parentId") int parentId);

    //一级分类添加的时候验证编码是否重复，查询零件库以及产品库两个库
    @Select("select SUM(a) from (select count(*) a from sort where code=#{code} and level=#{level} and parent_id = #{parentId}" +
            "  UNION all select count(*) a  from fin_sort where fin_sort_code=#{code} and fin_sort_level=#{level} and parent_id = #{parentId} ) as b")
    boolean ifCodeExistAddFromTwoTable(@Param("code") String code,@Param("level") int level,@Param("parentId") int parentId);


    //添加分类的时候验证分类名是否重复
    @Select("select count(*) from sort where name=#{name}")
    boolean selectNameAdd(@Param("name") String name);

    //更新分类信息的时候验证是分类名是否重复
    @Select("select count(*) from sort where name=#{name} and id !=#{id}")
    boolean selectNameUpdate(@Param("name") String name,@Param("id") int id);

   @Select("select * from sort where parent_id =#{parentId}")
    List<Sort>getData(@Param("parentId") int parentId);


   //查询所有子集的id
   @Select("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(all_sub_ids, ',', rn), ',', -1) AS id_list\n" +
           "FROM (\n" +
           "SELECT GROUP_CONCAT(id) AS all_sub_ids,\n" +
           "ANY_VALUE(LENGTH(GROUP_CONCAT(id SEPARATOR ',')) - LENGTH(REPLACE(GROUP_CONCAT(id SEPARATOR ','), ',', '')) + 1) AS c,\n" +
           "@rownum := @rownum + 1 AS rn\n" +
           "FROM (\n" +
           "SELECT * FROM (\n" +
           "SELECT id,parent_id\n" +
           "FROM sort\n" +
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

   //查询该level的分级下所有的分类信息
    @Select("select * from sort where level = #{level}")
    List<Sort>selectByLevel(@Param("level") int level);


    //循环更新编码
    void updateCodes(@Param("sorts")List<Sort>sorts);

    //在编码规则更新后，原有的编码不符合规则，但是现在想要更新分类名称或者分类描述
    @Update("update sort set name = #{name},`describe`=#{describe} where id = #{id}")
    void updateSortExceptCode(Sort sort);

    //检查是否有id为0的数据
    @Select("select count(*) from sort where id = 0")
    boolean select0();

    //将-1改为0
    @Update("update sort set id = 0 where id = -1")
    void update0();

    //查询id为0的分类，也就是公共属性使用的单独分类
    @Select("select * from sort where id=0")
    List<Sort>selectId0();




    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    //以下是excel导入所用到的

    //查询该分类是否已经存在，存在就执行更新操作。不存在就执行添加操作
    @Select("select count(*) from sort where name = #{name}  and level = #{level} and parent_id = #{parentId} and code = #{code}")
    boolean selectSortIfExist(Sort sort);

    //查询名称是否重复
    @Select("select count(*) from sort where name = #{name}")
    boolean selectSortNameIfExist(Sort sort);

    //查询编码是否重复
    @Select("select count(*) from sort where  level = #{level} and parent_id = #{parentId} and code = #{code}")
    boolean selectSortCodeIfExist(Sort sort);
    //根据编码parentIdlevel查询数据
    @Select("select * from sort where  level = #{level} and parent_id = #{parentId} and code = #{code}")
    Sort selectSortCode(Sort sort);


    //添加分类
    void addSortExcel(Sort sort);

    //更新分类
    @Update("update sort set parent_id = #{parentId},name = #{name},level=#{level},code = #{code} where id = #{id}")
    void updateSortExcel(Sort sort);//以上的添加和更新只适用于前三级

    //添加分类，循环添加，四级分类使用
    void addSortFourExcel(@Param("sortLists") List<Sort>sortLists);

    //根据名称查询分类id
    @Select("select id from sort where name = #{name}")
   int  selectIdExcel(@Param("name") String name);

    //查询名称和编码是否相同
    @Select("select * from sort where name = #{name} and code = #{code}")
    boolean nameAndCodeIfExist(@Param("name") String name,@Param("code") String code);

    //根据编码查询分类（适用于导入根据编码，查询分类是否存在使用）
    @Select("select  * from sort where level=#{level} and code = #{code} and parent_id =#{parentId}")
    List<Sort> selectByCode (@Param("level") int level,@Param("code") String code,@Param("parentId") int parentId);

    //根据属性id查询该属性所在的分类下是否有物料信息

    @Select("select count(*) FROM product where product.parentId = (select id from sort where id = (select parentId from attribute_name where attribute_name.id = #{id}))")
    boolean selectIfExistProductByNameId(@Param("id") int id);





    //----------------------------------------------------------------------------------------------------------------------------------------------------------
    //下面是显示数量页面用到的

    //查询有物料的分类
    List<Sort>selectHaveProduct();

    //查询所有二级分类
    @Select("select  * from sort where level = 2 and id !=0")
    List<Sort> selectTwoLevel();

    //查询分类下的物料数量
    @Select("select count(*) from product where deleteSign = 0 and parentId = #{parentId}")
    int selectNumber(@Param("parentId") int parentId);

    //查询所有的一级分类
    @Select("select  * from sort where level = 1 and id !=0")
    List<Sort>selectOne();


    //分类物料审核人界面----------------------------------------------------------------------------------------------------------------------------------------------------------
//查询所有的四级分类
    @Select("select * from sort where level = 4")
    List<Sort> selectAllLevelFour();

    //根据产品信息查询分类
    List<Sort>selectByProduct(@Param("productId") int productId,@Param("vault") int vault);





}
