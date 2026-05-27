package com.hongbang.mapper;

import com.hongbang.pojo.MaterialReturnReceive;
import com.hongbang.pojo.Product;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface MaterialReturnReceiveMapper {

    //插入退货或者让步
    int insert(MaterialReturnReceive receive);

    //查询让步接收数据的的代码
    @Select("select * from material_return_receive where handle_type = 2")
    List<MaterialReturnReceive> selectRbJS();

    //根据product_id,vault查询物料信息
    List<Product> selectProduct(@Param("MaterialReturnReceives") List<MaterialReturnReceive> materialReturnReceives);

    //查询未分配的信息
    @MapKey("id")
    List<Map<String,Object>> selectNoFp(@Param("page") int page,@Param("pageSize") int pageSize);

    int selectNoFpCount();

    //根据contentId查询申请人都有谁（现在的contentID是质检单）
    @Select("select user.id,name,userName\n" +
            "from user where id in (select DISTINCT user_id from application_form where id in (\n" +
            "select DISTINCT application_id from application_content where application_id in (" +
            "select  distinct old_app_form_id from relationship where relationship.new_app_form_id in (" +
            "select old_app_form_id from relationship where relationship.new_app_form_id in(" +
            "select application_id from application_content where id = #{contentId}))) and product_id = #{productId} and vault = #{vault}))")
    List<Map<String,Object>>selectAppUser (@Param("contentId") int contentId,@Param("productId") int productId,@Param("vault") int vault);

    //更新让步接收修改人
    @Update("update material_return_receive set receive_user = #{receiveUser} where id = #{id}")
    void updateReceiveUser(@Param("receiveUser") int receiveUser,@Param("id") int id);

    //根据product_id,vault查询物料信息
    Product selectProductOne(@Param("id") int id,@Param("vault") int vault);

    //根据ID查询
    @Select("select * from material_return_receive where id = #{id}")
    MaterialReturnReceive selectById(@Param("id") int id);


    //查询质检单的退货以及让步情况
    @Select("select * from material_return_receive where application_id = #{id}")
    List<MaterialReturnReceive> selectReturnReceiveByAppId(@Param("id") int id);

    //查询所有的退货信息
   @MapKey("id")
    List<Map<String,Object>>selectTh(@Param("page") int page,@Param("pageSize") int pageSize);

    @Select("select count(*) from material_return_receive where handle_type = 1")
    int selectThCount();

    //设置退货后是否继续申购物料信息
    @Update("update material_return_receive set continue_purchase = #{status} where id = #{id}")
    void updateContinue(@Param("status") int status,@Param("id") int id);

    //查询是否存在让步接收和退货操作
    @Select("select count(*) from material_return_receive where application_id = #{applicationId}")
    boolean selectIfExist(@Param("applicationId") int applicationId);

    //查询退货不继续购买,然后退货还未记录在采购单上的物料信息
    @Select("select * from material_return_receive where continue_purchase=0 and return_qty> return_qtyed and product_id = #{productId} and vault = #{vault}")
    List<MaterialReturnReceive> selectReturnReceiveByProduct(@Param("productId") int productId,@Param("vault") int vault);

    @Update("update material_return_receive set return_qtyed = #{returnQtyed} where id = #{id}")
    void updateReturnQtyed(@Param("returnQtyed") double returnQtyed,@Param("id") int id);

    //查询让步接收的数据根据质检单
    @Select("select * from material_return_receive where handle_type = 2 and application_id = #{applicationId}")
    List<MaterialReturnReceive> selectRbByZj(@Param("applicationId") int applicationId);



}
