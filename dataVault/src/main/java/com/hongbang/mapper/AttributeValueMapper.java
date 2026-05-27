package com.hongbang.mapper;

import com.hongbang.pojo.AttributeValue;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface AttributeValueMapper {
    //添加
    @Insert("insert into attribute_value values (#{id},#{attNameId},#{code},#{attValue})")
    void add(AttributeValue attributeValue);

    //修改
    @Update("update attribute_value set att_name_id = #{attNameId},code=#{code},att_value = #{attValue} where id = #{id}")
    void update(AttributeValue attributeValue);

    //删除
    @Delete("delete from attribute_value where id = #{id}")
    void delete(@Param("id") int id);

    //回显
    @Select("select * from attribute_value where id = #{id}")
    List<AttributeValue> selectById(@Param("id") int id);

    //查看当前属性下的所有属性值
    @Select("select * from attribute_value where att_name_id = #{attNameId}")
   List<AttributeValue> selectByAttNameId(@Param("attNameId") int attNameId);

    //判断属性值是否重复
    @Select("select count(*)from attribute_value where id !=#{id} and att_name_id = #{attNameId} and  BINARY att_value =  #{attValue}")
    boolean selectValueExist(AttributeValue attributeValue);

    //判断编码值是否重复
    @Select("select count(*)from attribute_value where id !=#{id} and att_name_id = #{attNameId}  and BINARY code = #{code}")
    boolean selectCodeExist(AttributeValue attributeValue);

    //判断属性值是否重复
    @Select("select count(*)from attribute_value where  att_name_id = #{attNameId} and BINARY att_value =  #{attValue}")
    boolean selectValueExistAdd(AttributeValue attributeValue);

    //判断编码值是否重复
    @Select("select count(*)from attribute_value where att_name_id = #{attNameId}  and BINARY code =  #{code}")
    boolean selectCodeExistAdd(AttributeValue attributeValue);

    //输入联想
    @Select("select att_value,code from attribute_value where concat(att_value) LIKE CONCAT('%',#{str},'%')and attribute_value.att_name_id = #{attNameId} ")
    List<Map<String,Object>>inputLX(@Param("str") String str,@Param("attNameId") int attNameId);

    //根据nameId,attValue去查询编码
    @Select("select * from attribute_value where att_name_id = #{attNameId} and BINARY att_value =  #{attValue}")
    List<AttributeValue> selectCode(AttributeValue attributeValue);

    //根据属性ID去查询该属性下是否有编码表存在
    @Select("select count(*) from attribute_value where att_name_id = #{attNameId}")
    boolean selectIfCode(@Param("attNameId") int attNameId);

    //属性值作为编码，循环添加
    /**
     * 批量插入数据
     * @param attributeValues
     * @return
     */
    void addCodeAuto (@Param("attributeValues")List<AttributeValue> attributeValues);

    //分页查询
    @Select("select * from attribute_value where att_name_id = #{attNameId} limit #{begin},#{size}")
    List<AttributeValue> selectAttributeValueLimit(@Param("attNameId") int attNameId, @Param("begin") int begin,@Param("size") int size);
    @Select("select count(*)  from attribute_value where att_name_id = #{attNameId}")
            int totalCount(@Param("attNameId") int attNameId);


    //模糊查询
    @Select("select * from attribute_value where concat(attribute_value.att_value,attribute_value.code) LIKE CONCAT('%',#{str},'%')  limit #{begin},#{size} ")
    List<AttributeValue> search(@Param("str") String str,@Param("begin") int begin,@Param("size") int size);
    //查询总数
    @Select("select count(*) from attribute_value where concat(attribute_value.att_value,attribute_value.code) LIKE CONCAT('%',#{str},'%')  limit #{begin},#{size} ")
    int searchCount(@Param("str") String str,@Param("begin") int begin,@Param("size") int size);


    //下面是excel导入用到的
    //循环加入编码
    void addCodeExcel(@Param("attributeValues")List<AttributeValue> attributeValues);

    //查询属性值是否重复
    @Select("select count(*) from attribute_value where code = #{code} and att_name_id = #{attNameId} and BINARY att_value = #{attValue}")
    boolean selectValueExistExcel(AttributeValue attributeValue);

    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，属性值是否与数据库中的重复
    @Select("select count(*) from attribute_value where att_name_id = #{attNameId} and BINARY att_value = #{attValue} ")
    boolean selectValueCodeExistExcel(AttributeValue attributeValue);

    //在excel文件中确定数据库中没有的数据时，判断要添加的数据中，编码是否与数据库中的重复
    @Select("select count(*) from attribute_value where att_name_id = #{attNameId} and BINARY code =  #{code} ")
    boolean selectValueCodesExistExcel(AttributeValue attributeValue);



@Select("SELECt DISTINCT content from attribute_content where parentId = 2903")
    List<Map<String,Object>> select1();

@Select("select * from attribute_value where attribute_value.att_name_id = 2903")
    List<AttributeValue> select2();


//查询所有数据
    @Select("select * from attribute_value")
    List<AttributeValue> selectAll();






}
