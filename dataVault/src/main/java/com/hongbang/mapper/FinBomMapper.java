package com.hongbang.mapper;

import com.hongbang.pojo.FinBom;
import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FinBomMapper {

//根据fin_id查询是否存在BOM表信息


    @Select("(SELECT * from fin_bom LEFT JOIN product ON fin_bom.productId=product.id and fin_bom.vault=product.vault where fin_bom.vault='0' and fin_bom.bom_title_id=#{finId})UNION ALL\n" +
            "(SELECT * from fin_bom LEFT JOIN fin_product ON fin_bom.productId=fin_product.id and fin_bom.vault=fin_product.vault where fin_bom.vault='1' and fin_bom.bom_title_id=#{finId}) ")
    List<Map<String,Object>> selectByPage(@Param("finId") int finId);

    @Select("select count(*) from fin_bom where bom_title_id = #{finId}")
    int totalCount(@Param("finId") int finId);

    //删除BOM表的信息(BOM表修改前是要先删除所有的然后重新添加)
    @Delete("delete from fin_bom where bom_title_id = #{finId}")
    void deleteByUpdate(@Param("finId") int finId);

    //循环添加BOM表信息
    void add(@Param("finBoms") List<FinBom>finBoms);

    //查询当前BOM表下的所有产品信息，只要产品库的
    @Select("select fin_bom.id,bom_title_id as bomTitleId,status,productId,vault,number,notes,part_number from fin_bom left  JOIN fin_bom_title on fin_bom.bom_title_id = fin_bom_title.id where fin_bom_title.fin_product_id = #{finId} and vault=1")
    List<FinBom>selectBomVault(@Param("finId") int finId);

    //查询某个物料是否在BOM表中被使用
    @Select("select count(*) from  fin_bom where productId = #{productId} and vault = #{vault}")
    boolean ifUsed(@Param("productId") int productId,@Param("vault") int vault);

    //查询当前BOM表下的所有产品信息
    @Select("select * from fin_bom where bom_title_id in (select id from fin_bom_title where fin_bom_title.fin_product_id = #{finId})")
    List<FinBom>selectBomVaultAll(@Param("finId") int finId);

    //查询当前BOM表下的内容
    @Select("select * from fin_bom where bom_title_id in (select id from fin_bom_title where fin_bom_title.fin_product_id = #{finProductId})")
    List<FinBom> selectBomContent(@Param("finProductId") int finProductId);

    //查询BOM表中物料信息的属性信息
    @Select("(SELECT productId,attributeName,attributeNameUnit,content,vault,nameId from product LEFT JOIN\n" +
            "        (SELECT * from (select attribute_content.productId,attribute_content.content,attribute_name.name as attributeName,attribute_name.unit as attributeNameUnit,attribute_name.id as nameId  from attribute_content left JOIN attribute_name on attribute_name.id = attribute_content.parentId ) as a)\n" +
            "        as b on  b.productId = product.id where product.id in (select * from(SELECT fin_bom.productId from fin_bom LEFT JOIN product ON fin_bom.productId=product.id and fin_bom.vault=product.vault where fin_bom.vault='0' and fin_bom.bom_title_id=#{bomTitleId}\n" +
            "        ) as c))\n" +
            "UNION ALL\n" +
            "( SELECT fin_product_id as productId,attributeName,attributeNameUnit,fin_att_content as content,vault,nameId from fin_product LEFT JOIN\n" +
            "        (SELECT * from (select fin_attribute_content.fin_product_id,fin_attribute_content.fin_att_content,fin_attribute_name.fin_att_name as attributeName,fin_attribute_name.fin_att_unit as attributeNameUnit,fin_attribute_name.id as nameId\n" +
            "        from fin_attribute_content left JOIN fin_attribute_name on fin_attribute_name.id = fin_attribute_content.fin_att_name_id ) as a)\n" +
            "        as b on  b.fin_product_id = fin_product.id where fin_product.id in (select * from (SELECT fin_bom.productId from fin_bom LEFT JOIN fin_product ON fin_bom.productId=fin_product.id and fin_bom.vault=fin_product.vault where fin_bom.vault='1' and fin_bom.bom_title_id=#{bomTitleId}\n" +
            "        ) as c))")
        List<Map<String,Object>> selectAttribute(@Param("bomTitleId") int bomTitleId);


    //查询物料信息
    List<Product> selectProduct(@Param("finBoms") List<FinBom> finBoms);

    //查询BOM表的价格和数量（零件）
    @Select(" select if(SUM(fin_bom.number*product.brand) is null,0,SUM(fin_bom.number*product.brand))  as sum  from fin_bom  LEFT JOIN product on product.id = fin_bom.productId where fin_bom.vault = 0 and bom_title_id in(select id from fin_bom_title where fin_product_id = #{finProductId})")
   int selectPriceLj(@Param("finProductId") int finProductId);

    //查询该BOM表中的产品信息
    @Select("select productId,number,notes,part_number from fin_bom where bom_title_id in(select id from fin_bom_title where fin_product_id = #{finProductId}) and vault = 1 ")
    List<Map<String,Object>>selectFinFromBom(@Param("finProductId") int finProductId);

    //查询BOM表中的物料信息
    @Select("select * from fin_bom where bom_title_id = #{bomTitleId} and productId = #{productId} and vault = #{vault}")
    FinBom selectProductById(@Param("bomTitleId") int bomTitleId,@Param("productId") int productId,@Param("vault") int vault);




}
