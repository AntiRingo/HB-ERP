package com.hongbang.mapper;

import com.hongbang.pojo.FinBomTitle;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinBomTitleMapper {

    //根据productId查询BOM表标题信息
    @Select("select * from fin_bom_title where fin_bom_title.fin_product_id = #{finProductId}")
    List<FinBomTitle> selectBomTitle(@Param("finProductId") int finProductId);

    //插入BOM表标题信息
//    @Insert("insert into fin_bom_title values(#{id},#{finProductId},#{title},#{description},#{author}) ")
    void addBomTitle(FinBomTitle finBomTitle);

    //查询物料号标题和BOM表信息
    @Select("select fin_bom_title.id,fin_product_id as finProductId,title,fin_bom_title.description,fin_product.fin_product_name as finProductName,fin_material_number as finMaterialNumber from fin_bom_title " +
            "left join fin_product on fin_bom_title.fin_product_id = fin_product.id where fin_product_id=#{finProductId}")
    List<Map<String,Object>> selectBomTitleAndProduct(@Param("finProductId") int finProductId);

    //删除BOM表标题,一级BOM表
    @Delete("delete from fin_bom_title where id = #{id}")
    void delete(@Param("id") int id);

    //修改BOM表列表信息
    @Update("update fin_bom_title set title = #{title},description = #{description} where id = #{id}")
    void update(FinBomTitle finBomTitle);

    //获取数据进行回显
    @Select("select * from fin_bom_title where id =#{id}")
    FinBomTitle selectById(@Param("id") int id);

    //搜索功能
    @Select("select fin_bom_title.* from fin_bom_title left join fin_product on fin_bom_title.fin_product_id = fin_product.id where concat(fin_bom_title.title,fin_bom_title.description) LIKE CONCAT('%',#{str},'%') and fin_product.deleteSign=0 " +
            " limit #{begin},#{size}")
    List<FinBomTitle>search(@Param("begin") int begin, @Param("size")int size, @Param("str") String str);
    //搜索数量
    @Select("select count(*) from fin_bom_title left join fin_product on fin_bom_title.fin_product_id = fin_product.id where concat(fin_bom_title.title,fin_bom_title.description) LIKE CONCAT('%',#{str},'%') and fin_product.deleteSign=0 ")
    int searchCount( @Param("str") String str);

    //查询所有的BOM表标题
    @Select("select fin_bom_title.* from fin_bom_title left join fin_product on fin_bom_title.fin_product_id = fin_product.id where fin_product.deleteSign=0 " +
            " limit #{begin},#{size}")
    List<FinBomTitle>selectAllBomTitle(@Param("begin") int begin, @Param("size")int size);
    //查询所有的BOM表标题数量
    @Select("select count(*) from fin_bom_title left join fin_product on fin_bom_title.fin_product_id = fin_product.id where fin_product.deleteSign=0 " )
   int selectAllBomTitleCount();

    //根据物料号或者物料名称查询
    List<FinBomTitle>selectAllByProductName(@Param("begin") int begin, @Param("size")int size, @Param("str") String str);
    int selectAllByProductNameCount(@Param("str") String str);

    //查询BOM表的用户权限
  @Select("SELECT user.id,user.name ,bom_purview.user_purview FROM user LEFT JOIN bom_purview  ON user.id = bom_purview.user_purview")
    List<Map<String,Object>> selectUserPurview();

  //查询BOM表是否有作者(true就是有作者，false就是没有作者)
    @Select("select count(*) from fin_bom_title where id = #{id} and (author is not Null or length(author)>0)")
    boolean selectIfAuthor(@Param("id") int id);

    //更新BOM表的作者
    @Update("update fin_bom_title set author = #{author} where id = #{id}")
    void updateAuthor(@Param("author") int author,@Param("id") int id);

    //查看是否是该BOM表的作者
    @Select("select count(*) from fin_bom_title where author = #{author} and id = #{id}")
    boolean selectIsAuthor(@Param("author") int author,@Param("id") int id);





}
